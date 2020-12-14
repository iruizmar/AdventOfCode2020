package day11

import java.io.File

class Day11 {
    private var seatMap: MutableMap<Coordinates, Seat> =
        File("./main/resources/day11/input.txt").useLines { it.toList() }.let { lines ->
            val map: MutableMap<Coordinates, Seat> = mutableMapOf()
            lines.forEachIndexed { x, l ->
                l.forEachIndexed { y, c ->
                    val coords = Coordinates(x, y)
                    map[coords] = Seat.fromChar(c, coords)
                }
            }
            map
        }
    private val maxX = seatMap.keys.last().x
    private val maxY = seatMap.keys.last().y
    private val directions = listOf(
        Pair(-1, -1),
        Pair(-1, 0),
        Pair(-1, 1),
        Pair(0, -1),
        Pair(0, 1),
        Pair(1, -1),
        Pair(1, 0),
        Pair(1, 1),
    )

    fun part1() {
        var changedSeats: Int
        do {
            changedSeats = 0
            val satesMap = seatMap.map { (c, s) -> c to s.state }.toMap()
            seatMap.forEach { (_, s) ->
                if (mutate(s, satesMap, true)) changedSeats++
            }
        } while (changedSeats != 0)

        println(seatMap.filter { it.value.state == SeatState.occupied }.size)
    }

    fun part2() {
        var changedSeats: Int
        do {
            changedSeats = 0
            val satesMap = seatMap.map { (c, s) -> c to s.state }.toMap()
            seatMap.forEach { (_, s) ->
                if (mutate(s, satesMap, false)) changedSeats++
            }
        } while (changedSeats != 0)

        println(seatMap.filter { it.value.state == SeatState.occupied }.size)
    }


    private fun mutate(seat: Seat, seatMap: Map<Coordinates, SeatState>, onlyAdjacent: Boolean): Boolean {
        var occupiedCount = 0
        directions.forEach { direction ->
            var currentMultiplier = 1
            var visibleState: SeatState
            var currentX = seat.coordinates.x + currentMultiplier * direction.first
            var currentY = seat.coordinates.y + currentMultiplier * direction.second
            if (currentX > -1 && currentY > -1 && currentX <= maxX && currentY <= maxY) {
                do {
                    visibleState = seatMap[Coordinates(currentX, currentY)] ?: error("Impossible")
                    if (visibleState == SeatState.occupied) occupiedCount++
                    currentMultiplier++
                    currentX = seat.coordinates.x + currentMultiplier * direction.first
                    currentY = seat.coordinates.y + currentMultiplier * direction.second
                } while (!onlyAdjacent && visibleState == SeatState.floor && currentX > -1 && currentY > -1 && currentX <= maxX && currentY <= maxY)
            }
        }
        val prevState = seat.state
        when (seat.state) {
            SeatState.occupied -> {
                if (occupiedCount >= 5) seat.state = SeatState.empty
            }
            SeatState.empty -> {
                if (occupiedCount == 0) seat.state = SeatState.occupied
            }
            else -> {
            }
        }
        return prevState != seat.state
    }

    private fun printMap(seatMap: Map<Coordinates, Seat>) {
        var prevX = 0
        var string = ""
        seatMap.forEach { (c, s) ->
            if (prevX != c.x) {
                println(string)
                string = ""
                prevX = c.x
            }
            string += when (s.state) {
                SeatState.occupied -> {
                    "#"
                }
                SeatState.empty -> {
                    "L"
                }
                else -> {
                    "."
                }
            }
        }
    }
}

class Seat(var state: SeatState, val coordinates: Coordinates) {
    companion object {
        fun fromChar(char: Char, coordinates: Coordinates): Seat {
            return when (char) {
                '.' -> Seat(SeatState.floor, coordinates)
                '#' -> Seat(SeatState.occupied, coordinates)
                else -> Seat(SeatState.empty, coordinates)
            }
        }
    }
}

data class Coordinates(val x: Int, val y: Int) {
    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinates

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}

enum class SeatState {
    floor,
    occupied,
    empty
}