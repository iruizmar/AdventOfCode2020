package day12

import java.io.File
import kotlin.math.abs

class Day12 {
    private var orders: List<Order> =
        File("./main/resources/day12/input.txt").useLines { it.toList() }.map { Order.fromLine(it) }

    fun part1() {
        var boatState = BoatState(Direction.North, 0, 0, Pair(1, 10))
        orders.forEach {
            boatState = it.move(boatState)
        }
        println(abs(boatState.northSouth) + abs(boatState.eastWest))
    }

    fun part2() {
        var boatState = BoatState(Direction.North, 0, 0, Pair(1, 10))
        orders.forEach {
            boatState = it.move2(boatState)
        }
        println(abs(boatState.northSouth) + abs(boatState.eastWest))
    }

}

class Order(private val movement: Movement, private val amount: Int) {
    companion object {
        fun fromLine(line: String): Order {
            return Order(
                Movement.values().find { m -> m.c == line[0] } ?: error("Impossible!"),
                line.substring(1).toInt()
            )
        }
    }

    fun move(boatState: BoatState): BoatState {
        when (movement) {
            Movement.North -> boatState.northSouth += amount
            Movement.East -> boatState.eastWest += amount
            Movement.West -> boatState.eastWest -= amount
            Movement.South -> boatState.northSouth -= amount
            Movement.TurnLeft ->
                boatState.lookingDirection = boatState.lookingDirection.turn(-amount)
            Movement.TurnRight ->
                boatState.lookingDirection = boatState.lookingDirection.turn(amount)
            Movement.Forward -> when (boatState.lookingDirection) {
                Direction.North -> boatState.northSouth += amount
                Direction.East -> boatState.eastWest += amount
                Direction.West -> boatState.eastWest -= amount
                Direction.South -> boatState.northSouth -= amount
            }
        }
        return boatState
    }

    fun move2(boatState: BoatState): BoatState {
        when (movement) {
            Movement.North ->
                boatState.waypointPosition =
                    Pair(boatState.waypointPosition.first + amount, boatState.waypointPosition.second)
            Movement.East ->
                boatState.waypointPosition =
                    Pair(boatState.waypointPosition.first, boatState.waypointPosition.second + amount)
            Movement.West ->
                boatState.waypointPosition =
                    Pair(boatState.waypointPosition.first, boatState.waypointPosition.second - amount)
            Movement.South ->
                boatState.waypointPosition =
                    Pair(boatState.waypointPosition.first - amount, boatState.waypointPosition.second)
            Movement.TurnLeft -> {
                boatState.lookingDirection = boatState.lookingDirection.turn(-amount)
            }
            Movement.TurnRight -> {
                boatState.lookingDirection = boatState.lookingDirection.turn(amount)
            }
            Movement.Forward -> {
                boatState.northSouth += amount * boatState.waypointPosition.first
                boatState.eastWest += amount * boatState.waypointPosition.second
            }
        }
        if (boatState.lookingDirection !is Direction.North) { //Waypoint must be rotated
            when (boatState.lookingDirection) {
                Direction.East -> {
                    boatState.waypointPosition =
                        Pair(-boatState.waypointPosition.second, boatState.waypointPosition.first)
                }
                Direction.West -> {
                    boatState.waypointPosition =
                        Pair(boatState.waypointPosition.second, -boatState.waypointPosition.first)
                }
                Direction.South -> {
                    boatState.waypointPosition =
                        Pair(-boatState.waypointPosition.first, -boatState.waypointPosition.second)
                }
            }
            boatState.lookingDirection = Direction.North
        }
        return boatState
    }
}

data class BoatState(
    var lookingDirection: Direction,
    var northSouth: Int,
    var eastWest: Int,
    var waypointPosition: Pair<Int, Int>
)

open class Direction(private val deg: Int) {
    object North : Direction(0)
    object East : Direction(90)
    object West : Direction(270)
    object South : Direction(180)

    fun turn(amount: Int): Direction {
        return when (abs(deg + amount + 360) % 360) {
            0 -> North
            90 -> East
            270 -> West
            180 -> South
            else -> error("Impossible!")
        }
    }
}

enum class Movement(val c: Char) {
    North('N'),
    East('E'),
    West('W'),
    South('S'),
    TurnLeft('L'),
    TurnRight('R'),
    Forward('F')
}