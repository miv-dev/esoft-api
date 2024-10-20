
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.server)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}

group = "com.miv"
version = "1.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.java.time)
    implementation(libs.hikari.cp)
    implementation(libs.db.flyway)
    implementation(libs.db.postgres)
    implementation(libs.csv.reader)

    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
