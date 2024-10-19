package com.miv.db.tables

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RealtorProfileTable : CompositeIdTable("realtors") {
    val user = reference("user_id", UserTable, ReferenceOption.CASCADE)
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val middleName = varchar("middle_name", 50)
    val dealShare = double("deal_share").nullable()

    init {
        addIdColumn(user)
    }

    override val primaryKey = PrimaryKey(user)
}