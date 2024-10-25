package com.miv.services.impl

import com.miv.db.entities.estate.ApartmentEntity
import com.miv.db.entities.estate.HouseEntity
import com.miv.db.entities.estate.LandEntity
import com.miv.db.entities.estate.EstateEntity
import com.miv.db.tables.estate.*
import com.miv.models.estate.EstateType
import com.miv.models.estate.*
import com.miv.services.DistrictService
import com.miv.services.EstateService
import com.miv.utils.levenshtein
import io.ktor.server.plugins.*
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import javax.inject.Inject

class EstateServiceImpl @Inject constructor(
    private val districtService: DistrictService,
) : EstateService {
    override suspend fun search(
        query: String?,
        type: EstateType?,
        districtID: Int?,
        sortByVariant: SortVariant?,
        sortOrder: SortOrder
    ): List<EstateClass> {
        val results = transaction {
            val sql = EstateTable
                .join(
                    otherTable = RealStateDistrictTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = { EstateTable.id eq RealStateDistrictTable.realState }
                )
                .leftJoin(HouseTable)
                .leftJoin(LandTable)
                .leftJoin(ApartmentTable)
                .selectAll()
            type?.let {
                sql.andWhere { EstateTable.type eq type }
            }
            districtID?.let {
                sql.andWhere { RealStateDistrictTable.district eq districtID }
            }
            sortByVariant?.let {
                when (sortByVariant) {
                    SortVariant.ADDRESS -> sql.orderBy(
                        EstateTable.addressStreet to sortOrder,
                        EstateTable.addressCity to sortOrder
                    )

                    SortVariant.HOUSE -> sql.orderBy(EstateTable.addressHouse to sortOrder)
                    SortVariant.NUMBER -> sql.orderBy(EstateTable.addressNumber to sortOrder)
                }
            }

            sql.map {
                val realState = EstateEntity.wrapRow(it).toModel()
                when (it[EstateTable.type]) {
                    EstateType.HOUSE -> {
                        House(
                            realState,
                            it[HouseTable.totalArea],
                            it[HouseTable.totalRooms],
                            it[HouseTable.totalFloors],
                        )
                    }

                    EstateType.APARTMENT -> {
                        Apartment(
                            realState,
                            it[ApartmentTable.totalArea],
                            it[ApartmentTable.totalRooms],
                            it[ApartmentTable.floor]
                        )
                    }

                    EstateType.LAND -> {
                        Land(
                            realState,
                            it[LandTable.totalArea],
                        )
                    }
                }
            }
        }
        query?.let {
            return results.filter {
                levenshtein(it.estate.addressCity, query) <= 3 ||
                        levenshtein(it.estate.addressHouse, query) <= 3 ||
                        levenshtein(it.estate.addressNumber, query) <= 1 ||
                        levenshtein(it.estate.addressStreet, query) <= 1
            }
        }

        return results
    }

    override suspend fun getByID(id: UUID): EstateClass {
        return transaction {
            EstateEntity.findById(id)?.let {
                when(it.type){
                    EstateType.HOUSE -> HouseEntity.get(id).toModel()
                    EstateType.APARTMENT -> ApartmentEntity.get(id).toModel()
                    EstateType.LAND -> LandEntity.get(id).toModel()
                }
            } ?:  throw BadRequestException("RealState with id '$id' does not exist")
        }
    }
    override suspend fun create(
        type: EstateType,
        latitude: Double,
        longitude: Double,
        addressCity: String?,
        addressHouse: String?,
        addressNumber: String?,
        addressStreet: String?,
        totalArea: Double?,
        totalFloors: Int?,
        totalRooms: Int?,
        floor: Int?,
    ): EstateClass {
        val districts = districtService.findDistrictByCoords(latitude, longitude)
        val estateEntity = newSuspendedTransaction {
            EstateEntity.new {
                this.type = type
                this.latitude = latitude
                this.longitude = longitude
                this.addressCity = addressCity
                this.addressHouse = addressHouse
                this.addressNumber = addressNumber
                this.addressStreet = addressStreet
                this.districts = districts
            }
        }

        return createOrUpdate(
            estateEntity.id.value,
            totalArea,
            totalFloors,
            totalRooms,
            floor,
            type,
            null
        )!!
    }


    override suspend fun update(
        id: UUID,
        type: EstateType,
        latitude: Double,
        longitude: Double,
        addressCity: String?,
        addressHouse: String?,
        addressNumber: String?,
        addressStreet: String?,
        totalArea: Double?,
        totalFloors: Int?,
        totalRooms: Int?,
        floor: Int?,
    ): EstateClass? = newSuspendedTransaction {
        val districts = districtService.findDistrictByCoords(latitude, longitude)
        var oldType: EstateType? = null
        val estateEntity = EstateEntity.findByIdAndUpdate(id) {
            oldType = it.type
            it.type = type

            it.latitude = latitude
            it.longitude = longitude
            it.addressCity = addressCity
            it.addressHouse = addressHouse
            it.addressNumber = addressNumber
            it.addressStreet = addressStreet
            it.districts = districts
        }

        estateEntity?.let {
            createOrUpdate(
                id,
                totalArea,
                totalFloors,
                totalRooms,
                floor,
                type,
                oldType
            )
        }
    }

    override suspend fun delete(id: UUID) {
       transaction {
           EstateEntity.findById(id)?.delete() ?: throw BadRequestException("RealState with id '$id' does not exist")
       }
    }


    private suspend fun createOrUpdate(
        id: UUID,
        totalArea: Double?,
        totalFloors: Int?,
        totalRooms: Int?,
        floor: Int?,
        type: EstateType,
        oldType: EstateType?,
    ): EstateClass? {
        oldType?.takeIf { it != type }?.let {
            deleteType(id, oldType)
        }
        return transaction {
            when (type) {
                EstateType.HOUSE -> {
                    val houseID = CompositeID {
                        it[HouseTable.realState] = id
                    }
                    when {
                        oldType == type -> {
                            HouseEntity.findByIdAndUpdate(houseID) {
                                it.totalFloors = totalFloors
                                it.totalRooms = totalRooms
                                it.totalArea = totalArea
                            }?.toModel()
                        }

                        else -> {
                            HouseEntity.new(houseID) {
                                this.totalFloors = totalFloors
                                this.totalRooms = totalRooms
                                this.totalArea = totalArea
                            }.toModel()
                        }
                    }

                }

                EstateType.APARTMENT -> {
                    val apartmentID = CompositeID {
                        it[ApartmentTable.realState] = id
                    }
                    when {
                        oldType == type -> {
                            ApartmentEntity.findByIdAndUpdate(apartmentID) {
                                it.totalArea = totalArea
                                it.totalRooms = totalRooms
                                it.floor = floor
                            }?.toModel()
                        }

                        else -> {
                            ApartmentEntity.new(apartmentID) {
                                this.totalArea = totalArea
                                this.totalRooms = totalRooms
                                this.floor = floor
                            }.toModel()
                        }
                    }
                }

                EstateType.LAND -> {
                    val landID = CompositeID {
                        it[LandTable.realState] = id
                    }
                    when {
                        oldType == type -> {
                            LandEntity.findByIdAndUpdate(landID) {
                                it.totalArea = totalArea
                            }?.toModel()
                        }

                        else -> {
                            LandEntity.new(landID) {
                                this.totalArea = totalArea
                            }.toModel()
                        }
                    }
                }
            }
        }
    }

    private suspend fun deleteType(uuid: UUID, type: EstateType) = newSuspendedTransaction {

        when (type) {
            EstateType.HOUSE -> {
                val houseID = CompositeID {
                    it[HouseTable.realState] = uuid
                }
                HouseEntity[houseID].delete()
            }

            EstateType.APARTMENT -> {
                val id = CompositeID {
                    it[ApartmentTable.realState] = uuid
                }
                ApartmentEntity[id].delete()
            }

            EstateType.LAND -> {
                val id = CompositeID {
                    it[HouseTable.realState] = uuid
                }
                LandEntity[id].delete()
            }
        }
    }



}