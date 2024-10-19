package com.miv.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object ImportedFileTable: IntIdTable("imported_csv", "id") {
    val filename = varchar("filename", 64)
}