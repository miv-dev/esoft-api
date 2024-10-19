package com.miv.utils

import io.ktor.server.application.*

fun Application.longProperty(path: String): Long =
    stringProperty(path).toLong()

