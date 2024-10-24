package com.miv.services.impl

import com.miv.db.entities.real.state.ApartmentEntity
import com.miv.db.entities.real.state.HouseEntity
import com.miv.db.entities.real.state.LandEntity
import com.miv.db.entities.real.state.RealStateEntity
import com.miv.db.tables.real.state.*
import com.miv.models.RealStateType
import com.miv.models.real.state.*
import com.miv.services.DistrictService
import com.miv.services.RealStateService
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

class RealStateServiceImpl @Inject constructor(
    private val districtService: DistrictService,
) : RealStateService {
    override suspend fun search(
        query: String?,
        type: RealStateType?,
        districtID: Int?,
        sortByVariant: SortVariant?,
        sortOrder: SortOrder
    ): List<RealStateClass> {
        val results = transaction {
            val sql = RealStateTable
                .join(
                    otherTable = RealStateDistrictTable,
                    joinType = JoinType.INNER,
                    additionalConstraint = { RealStateTable.id eq RealStateDistrictTable.realState }
                )
                .leftJoin(HouseTable)
                .leftJoin(LandTable)
                .leftJoin(ApartmentTable)
                .selectAll()
            type?.let {
                sql.andWhere { RealStateTable.type eq type }
            }
            districtID?.let {
                sql.andWhere { RealStateDistrictTable.district eq districtID }
            }
            sortByVariant?.let {
                when (sortByVariant) {
                    SortVariant.ADDRESS -> sql.orderBy(
                        RealStateTable.addressStreet to sortOrder,
                        RealStateTable.addressCity to sortOrder
                    )

                    SortVariant.HOUSE -> sql.orderBy(RealStateTable.addressHouse to sortOrder)
                    SortVariant.NUMBER -> sql.orderBy(RealStateTable.addressNumber to sortOrder)
                }
            }

            sql.map {
                val realState = RealStateEntity.wrapRow(it).toModel()
                when (it[RealStateTable.type]) {
                    RealStateType.HOUSE -> {
                        House(
                            realState,
                            it[HouseTable.totalArea],
                            it[HouseTable.totalRooms],
                            it[HouseTable.totalFloors],
                        )
                    }

                    RealStateType.APARTMENT -> {
                        Apartment(
                            realState,
                            it[ApartmentTable.totalArea],
                            it[ApartmentTable.totalRooms],
                            it[ApartmentTable.floor]
                        )
                    }

                    RealStateType.LAND -> {
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
                levenshtein(it.realState.addressCity, query) <= 3 ||
                        levenshtein(it.realState.addressHouse, query) <= 3 ||
                        levenshtein(it.realState.addressNumber, query) <= 1 ||
                        levenshtein(it.realState.addressStreet, query) <= 1
            }
        }

        return results
    }

    override suspend fun getByID(id: UUID): RealStateClass {
        return transaction {
            RealStateEntity.findById(id)?.let {
                when(it.type){
                    RealStateType.HOUSE -> HouseEntity.get(id).toModel()
                    RealStateType.APARTMENT -> ApartmentEntity.get(id).toModel()
                    RealStateType.LAND -> LandEntity.get(id).toModel()
                }
            } ?:  throw BadRequestException("RealState with id '$id' does not exist")
        }
    }
    override suspend fun create(
        type: RealStateType,
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
    ): RealStateClass {
        val districts = districtService.findDistrictByCoords(latitude, longitude)
        val realStateEntity = newSuspendedTransaction {
            RealStateEntity.new {
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
            realStateEntity.id.value,
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
        type: RealStateType,
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
    ): RealStateClass? = newSuspendedTransaction {
        val districts = districtService.findDistrictByCoords(latitude, longitude)
        var oldType: RealStateType? = null
        val realStateEntity = RealStateEntity.findByIdAndUpdate(id) {
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

        realStateEntity?.let {
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
           RealStateEntity.findById(id)?.delete() ?: throw BadRequestException("RealState with id '$id' does not exist")
       }
    }


    private suspend fun createOrUpdate(
        id: UUID,
        totalArea: Double?,
        totalFloors: Int?,
        totalRooms: Int?,
        floor: Int?,
        type: RealStateType,
        oldType: RealStateType?,
    ): RealStateClass? {
        oldType?.takeIf { it != type }?.let {
            deleteType(id, oldType)
        }
        return transaction {
            when (type) {
                RealStateType.HOUSE -> {
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

                RealStateType.APARTMENT -> {
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

                RealStateType.LAND -> {
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

    private suspend fun deleteType(uuid: UUID, type: RealStateType) = newSuspendedTransaction {

        when (type) {
            RealStateType.HOUSE -> {
                val houseID = CompositeID {
                    it[HouseTable.realState] = uuid
                }
                HouseEntity[houseID].delete()
            }

            RealStateType.APARTMENT -> {
                val id = CompositeID {
                    it[ApartmentTable.realState] = uuid
                }
                ApartmentEntity[id].delete()
            }

            RealStateType.LAND -> {
                val id = CompositeID {
                    it[HouseTable.realState] = uuid
                }
                LandEntity[id].delete()
            }
        }
    }



}