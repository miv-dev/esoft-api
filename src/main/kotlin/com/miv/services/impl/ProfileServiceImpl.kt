package com.miv.services.impl

import com.miv.db.entities.ClientEntity
import com.miv.db.entities.RealtorEntity
import com.miv.db.tables.ClientProfileTable
import com.miv.db.tables.RealtorProfileTable
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

        val id = CompositeID {
            it[RealtorProfileTable.user] = user.id.value
        }
        RealtorEntity.new(id) {
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

    override suspend fun createClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        phone: String?,
        email: String?
    ): ClientEntity = newSuspendedTransaction {

        if (checkClientExists(phone, email)) throw BadRequestException("User is  exist")

        val user = userService.create(Role.CLIENT)
        val id = CompositeID {
            it[ClientProfileTable.user] = user.id.value
        }
        ClientEntity.new(id) {
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


    override suspend fun searchClients(query: String?): List<ClientEntity> = newSuspendedTransaction {
        query?.let {
            ClientEntity.all().filter {
                levenshtein(it.firstName, query) <= MAX_DISTANCE ||
                        levenshtein(it.lastName, query) <= MAX_DISTANCE ||
                        levenshtein(it.middleName, query) <= MAX_DISTANCE
            }
        } ?: ClientEntity.all().toList()
    }

    override suspend fun searchRealtors(query: String?): List<RealtorEntity> = newSuspendedTransaction {
        query?.let {
            RealtorEntity.all().filter {
                levenshtein(it.firstName, query) <= MAX_DISTANCE ||
                        levenshtein(it.lastName, query) <= MAX_DISTANCE ||
                        levenshtein(it.middleName, query) <= MAX_DISTANCE
            }

        } ?: RealtorEntity.all().toList()
    }


    companion object {
        const val MAX_DISTANCE = 3
        val FILENAMES = listOf(
            "agents.csv",
            "clients.csv",
        )
    }
}


fun levenshtein(lhs: CharSequence?, rhs: CharSequence): Int {
    when {
        lhs == null -> return rhs.length
        lhs == rhs -> return 0
        lhs.isEmpty() -> return rhs.length
        rhs.isEmpty() -> return lhs.length
    }
    val lhsLength = lhs!!.length + 1
    val rhsLength = rhs.length + 1

    var cost = Array(lhsLength) { it }
    var newCost = Array(lhsLength) { 0 }

    for (i in 1..<rhsLength) {
        newCost[0] = i

        for (j in 1..<lhsLength) {
            val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[lhsLength - 1]
}