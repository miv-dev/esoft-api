package com.miv.services.impl

import com.miv.db.entities.district.DistrictEntity
import com.miv.services.DistrictService
import com.miv.services.ImportService
import com.miv.services.impl.DistrictServiceImpl.Point
import io.github.pelletier197.csv.CsvProperty
import io.github.pelletier197.csv.reader.CsvReaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Polygon
import javax.inject.Inject

class DistrictServiceImpl @Inject constructor(
    private val importService: ImportService,

    ) : DistrictService {
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getDistricts(): List<DistrictEntity> = newSuspendedTransaction {
        DistrictEntity.all().toList()
    }

    override suspend fun findDistrictByCoords(lat: Double, lng: Double): SizedCollection<DistrictEntity> =
        newSuspendedTransaction {
            DistrictEntity.all().filter {
                val points = areaToPoint(it.area)

                isPointInPolygon(lat, lng, points)
            }.let {
                SizedCollection(it)
            }
        }

    data class DistrictCSV(
        @CsvProperty("name")
        val name: String,
        @CsvProperty("area")
        val area: String
    )

    init {
        scope.launch {
            importDistricts()
        }
    }

    private suspend fun importDistricts() {
        val filenames = importService.getImportedFiles()
        if (FILENAME !in filenames) {
            readCsv<DistrictCSV>("csv/$FILENAME").forEach {
                transaction {
                    DistrictEntity.new {
                        area = it.area
                        name = it.name
                    }
                }
            }
            importService.newImportedFile(FILENAME)
        }

    }

    private inline fun <reified T> readCsv(path: String): List<T> {
        val inputStream =
            this.javaClass.classLoader?.getResourceAsStream(path) ?: throw RuntimeException("File not found")
        val reader = CsvReaders.forType<T>()
        return reader.read(inputStream).map { it.getResultOrThrow() }.toList()
    }

    private fun isPointInPolygon(lat: Double, lng: Double, polygon: List<Point>): Boolean {
        val point = Point(lat, lng)
        var intersections = 0
        val n = polygon.size
        for (i in 0 until n) {
            val vertex1 = polygon[i]
            val vertex2 = polygon[(i + 1) % n]
            if (isOnSegment(point, vertex1, vertex2)) return true

            if (doesIntersect(point, vertex1, vertex2)) intersections++
        }
        return intersections % 2 != 0
    }

    private fun isOnSegment(point: Point, vertex1: Point, vertex2: Point): Boolean {
        val minX = minOf(vertex1.lat, vertex2.lat)
        val maxX = maxOf(vertex1.lat, vertex2.lat)
        val minY = minOf(vertex1.lng, vertex2.lng)
        val maxY = maxOf(vertex1.lng, vertex2.lng)

        return (point.lat in minX..maxX && point.lng in minY..maxY) &&
                (vertex2.lat - vertex1.lat) * (point.lng - vertex1.lng) == (vertex2.lng - vertex1.lng) * (point.lat - vertex1.lat)
    }

    private fun doesIntersect(point: Point, vertex1: Point, vertex2: Point): Boolean {
        if (vertex1.lng > vertex2.lng) {
            return doesIntersect(point, vertex2, vertex1)
        }

        if (point.lng == vertex1.lng || point.lng == vertex2.lng) {
            return true
        }

        if (point.lng < vertex1.lng || point.lng > vertex2.lng || point.lat >= maxOf(vertex1.lat, vertex2.lat)) {
            return false
        }

        if (point.lat < minOf(vertex1.lat, vertex2.lat)) {
            return true
        }

        val slope = (vertex2.lat - vertex1.lat) / (vertex2.lng - vertex1.lng)
        val intersectX = vertex1.lat + (point.lng - vertex1.lng) * slope

        return point.lat < intersectX
    }

    private fun areaToPoint(area: String): List<Point> = area.removePrefix("(").removeSuffix(")").split("),(").map {
        val coords = it.removePrefix("(").removeSuffix(")").split(",")
        Point(coords[0].toDouble(), coords[1].toDouble())
    }


    data class Point(
        val lat: Double,
        val lng: Double
    )

    companion object {
        const val FILENAME = "districts.csv"
    }
}


