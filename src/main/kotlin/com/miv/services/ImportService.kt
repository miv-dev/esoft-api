package com.miv.services

interface ImportService {

    suspend fun getImportedFiles(): List<String>

    suspend fun newImportedFile(name: String)

}