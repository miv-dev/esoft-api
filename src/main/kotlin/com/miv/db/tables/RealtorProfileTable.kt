package com.miv.db.tables

import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object RealtorProfileTable : IdTable<UUID>("realtors") {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val middleName = varchar("middle_name", 50)
    val dealShare = double("deal_share").nullable()

    override val id: Column<EntityID<UUID>> = reference("user_id", UserTable, ReferenceOption.CASCADE)
}