package com.miv.db.entities

import com.miv.db.tables.ImportedFileTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ImportedFileEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ImportedFileEntity>(ImportedFileTable)

    var filename by ImportedFileTable.filename

}