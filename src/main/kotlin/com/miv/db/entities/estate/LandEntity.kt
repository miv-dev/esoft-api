package com.miv.db.entities.estate

import com.miv.db.tables.estate.LandTable
import com.miv.models.estate.Land
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class LandEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<LandEntity>(LandTable){
        fun get(id: UUID): LandEntity {
            val compositeID = CompositeID {
                it[LandTable.realState] = id
            }
            return get(compositeID)
        }
    }

    val realState by EstateEntity referencedOn LandTable.realState
    var totalArea by LandTable.totalArea

    fun toModel() = Land(
        totalArea = totalArea,
    )
}