package com.miv.services.impl

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
import com.miv.models.Profile
import com.miv.models.Role
import com.miv.services.ImportService
import com.miv.services.ProfileService
import com.miv.services.UserService
import io.github.pelletier197.csv.CsvProperty
import io.github.pelletier197.csv.reader.CsvReaders
import io.ktor.server.plugins.BadRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import javax.inject.Inject
import kotlin.math.min

class ProfileServiceImpl @Inject constructor(
    private val userService: UserService,
    private val importService: ImportService,
) : ProfileService {

    private val scope = CoroutineScope(Dispatchers.IO)

    data class AgentCsv(
        @CsvProperty("Id")
        val id: String,
        @CsvProperty("FirstName")
        val firstName: String,
        @CsvProperty("MiddleName")
        val middleName: String,
        @CsvProperty("LastName")
        val lastName: String,
        @CsvProperty("DealShare")
        val dealShare: Int,

        )

    data class ClientCsv(
        @CsvProperty("Id")
        val id: String,
        @CsvProperty("FirstName")
        val firstName: String,
        @CsvProperty("MiddleName")
        val middleName: String,
        @CsvProperty("LastName")
        val lastName: String,
        @CsvProperty("Phone")
        val phone: String?,
        @CsvProperty("Email")
        val email: String?,
    )


    init {
        scope.launch {
            importUsers()
        }
    }

    private inline fun <reified T> readCsv(path: String): List<T> {
        val inputStream =
            this.javaClass.classLoader?.getResourceAsStream(path) ?: throw RuntimeException("File not found")
        val reader = CsvReaders.forType<T>()
        return reader.read(inputStream).map { it.getResultOrThrow() }.toList()
    }

    override suspend fun getProfileByUserID(id: UUID): Profile = newSuspendedTransaction {
        val user = userService.getByID(id)


        when (user.role) {
            Role.CLIENT -> {
                ClientEntity[user.id].toModel()
            }

            Role.REALTOR -> {
                RealtorEntity[user.id].toModel()
            }

            Role.ADMIN -> throw BadRequestException("Admin user does not have a profile")
        }

    }

    override suspend fun checkProfileExists(id: UUID, role: Role): Boolean = newSuspendedTransaction {
        when (role) {
            Role.CLIENT -> ClientEntity.findById(id) != null
            Role.REALTOR -> RealtorEntity.findById(id) != null
            else -> false
        }
    }

    private suspend fun importUsers() {
        val filenames = importService.getImportedFiles()

        FILENAMES.forEach { filename ->
            if (filename !in filenames) {
                when {
                    filename.contains("agents") -> {
                        readCsv<AgentCsv>("csv/$filename").forEach {
                            createRealtor(
                                it.firstName,
                                it.lastName,
                                it.middleName,
                                it.dealShare.toDouble()
                            )
                        }
                    }

                    filename.contains("clients") -> {
                        readCsv<ClientCsv>("csv/$filename").forEach {
                            if (!it.email.isNullOrEmpty() or !it.phone.isNullOrEmpty()) {
                                try {
                                    createClient(
                                        it.firstName,
                                        it.lastName,
                                        it.middleName,
                                        it.phone,
                                        it.email,
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
                importService.newImportedFile(filename)
            }

        }
    }


    override suspend fun createRealtor(
        firstName: String,
        lastName: String,
        middleName: String,
        dealShare: Double?
    ): RealtorEntity = newSuspendedTransaction {

        val user = userService.create(Role.REALTOR)

        RealtorEntity.new(user.id.value) {
            this.firstName = firstName
            this.lastName = lastName
            this.middleName = middleName
            this.dealShare = dealShare
        }
    }

    private suspend fun checkClientExists(phone: String?, email: String?): Boolean = newSuspendedTransaction {
        val query = ClientProfileTable.selectAll()

        phone?.takeIf { it.isNotEmpty() }?.let {
            query.andWhere { ClientProfileTable.phone eq phone }
        }
        email?.takeIf { it.isNotEmpty() }?.let {
            query.andWhere { ClientProfileTable.email eq email }
        }

        ClientEntity.wrapRows(query).count() > 0
    }

    private suspend fun checkClientExists(uuid: UUID, phone: String?, email: String?): Boolean =
        newSuspendedTransaction {
            val query = ClientProfileTable.selectAll()
                .where { ClientProfileTable.id neq uuid }

            phone?.takeIf { it.isNotEmpty() }?.let {
                query.andWhere { ClientProfileTable.phone eq phone }
            }
            email?.takeIf { it.isNotEmpty() }?.let {
                query.andWhere { ClientProfileTable.email eq email }
            }

            ClientEntity.wrapRows(query).count() > 0
        }

    override suspend fun createClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        phone: String?,
        email: String?
    ): ClientEntity = newSuspendedTransaction {

        if (checkClientExists(phone, email)) throw BadRequestException("User is  exist")

        val user = userService.create(Role.CLIENT)

        ClientEntity.new(user.id.value) {
            this.firstName = firstName
            this.lastName = lastName
            this.middleName = middleName
            if (!phone.isNullOrEmpty()) {
                this.phone = phone
            }
            if (!email.isNullOrEmpty()) {
                this.email = email
            }
        }
    }

    override suspend fun updateProfile(
        uuid: UUID,
        firstName: String?,
        lastName: String?,
        middleName: String?,
        phone: String?,
        email: String?
    ): ClientEntity = newSuspendedTransaction {
        if (checkClientExists(uuid, phone, email)) throw BadRequestException("Another user is exist")

        ClientEntity.findByIdAndUpdate(uuid) {
            it.firstName = firstName
            it.lastName = lastName
            it.middleName = middleName
            if (!phone.isNullOrEmpty()) {
                it.phone = phone
            } else {
                it.phone = null
            }
            if (!email.isNullOrEmpty()) {
                it.email = email
            } else {
                it.email = null
            }
        } ?: throw BadRequestException("User isn't exist")
    }

    override suspend fun updateProfile(
        uuid: UUID,
        firstName: String,
        lastName: String,
        middleName: String,
        dealShare: Double?
    ): RealtorEntity = newSuspendedTransaction {
        RealtorEntity.findByIdAndUpdate(uuid) {
            it.firstName = firstName
            it.lastName = lastName
            it.middleName = middleName
            it.dealShare = dealShare
        } ?: throw BadRequestException("User isn't exist")
    }




    companion object {
        val FILENAMES = listOf(
            "agents.csv",
            "clients.csv",
        )
    }
}
