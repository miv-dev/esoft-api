package com.miv.services.impl

import com.miv.db.entities.ImportedFileEntity
import com.miv.db.tables.ImportedFileTable
import com.miv.services.ImportService
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject

class ImportServiceImpl @Inject constructor() : ImportService {
    override suspend fun getImportedFiles(): List<String> = newSuspendedTransaction {
        ImportedFileEntity.all().map { it.filename }
    }

    override suspend fun newImportedFile(name: String) {
        transaction {
            ImportedFileEntity.new {
                this.filename = name
            }
        }
    }
}