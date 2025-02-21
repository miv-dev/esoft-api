package com.miv.handlers.impl

import com.miv.dto.ClientDTO
import com.miv.dto.RealtorDTO
import com.miv.handlers.UserHandler
import com.miv.models.user.Profile
import com.miv.models.user.Role
import com.miv.services.ProfileService
import com.miv.services.SearchService
import com.miv.services.UserService
import io.ktor.http.content.*
import io.ktor.server.plugins.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO
import javax.inject.Inject
import kotlin.io.path.Path

class UserHandlerImpl @Inject constructor(
    private val userService: UserService,
    private val searchService: SearchService,
    private val profileService: ProfileService
) : UserHandler {
    override suspend fun search(
        query: String?,
        role: Role
    ): List<Profile> = searchService.searchUsers(query, role)

    override suspend fun createClient(data: ClientDTO): Profile = newSuspendedTransaction {
        profileService.createClient(
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            phone = data.phone,
            email = data.email,
        ).toModel()
    }

    override suspend fun createRealtor(data: RealtorDTO): Profile = newSuspendedTransaction {
        profileService.createRealtor(
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            dealShare = data.dealShare
        ).toModel()
    }

    override suspend fun deleteUser(uuid: String) {
        userService.delete(UUID.fromString(uuid))
    }

    override suspend fun getUserByID(id: String): Profile {
        val uuid = UUID.fromString(id)
        return profileService.getProfileByUserID(uuid)
    }

    override suspend fun updateAvatar(id: String, data: MultiPartData): Profile {
        val uuid = UUID.fromString(id)

        val part = data.readPart()

        if (part !is PartData.FileItem) {
            throw IllegalArgumentException("Avatar must be a file")
        }

        val avatar = saveFile("avatar", part)
        userService.updateAvatar(uuid, avatar)

        return profileService.getProfileByUserID(uuid)
    }

    private suspend fun saveFile(partName: String, data: PartData.FileItem): String {

        val url = "media/${UUID.randomUUID()}"
        withContext(Dispatchers.IO) {
            Files.createDirectories(Paths.get(url))
        }

        val inputStream = data.provider().readRemaining().readByteArray()
        val file = File(url + "/${data.originalFileName ?: "avatar.png"}").apply { writeBytes(inputStream) }




        return file.path
    }


    override suspend fun updateRealtor(data: RealtorDTO, uuid: String): Profile = newSuspendedTransaction {
        val id = UUID.fromString(uuid)
        profileService.updateProfile(
            id,
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            dealShare = data.dealShare
        ).toModel()
    }

    override suspend fun updateClient(data: ClientDTO, uuid: String): Profile = newSuspendedTransaction {
        val id = UUID.fromString(uuid)
        profileService.updateProfile(
            id,
            firstName = data.firstName,
            lastName = data.lastName,
            middleName = data.middleName,
            phone = data.phone,
            email = data.email,
        ).toModel()
    }
}