package day17

import java.io.File

class Day17 {
    private val input =
        File("./main/resources/day17/input.txt").useLines { it.toList() }

    fun part1() {
        println(countFinalActivatedPoints(
            input.mapIndexed { row, line ->
                line.toCharArray().toList().mapIndexedNotNull { col, char ->
                    if (char == '#')
                        listOf(col, row, 0)
                    else null
                }
            }.flatten().toSet()
        ))
    }

    fun part2() {
        println(countFinalActivatedPoints(
            input.mapIndexed { row, line ->
                line.toCharArray().toList().mapIndexedNotNull { col, char ->
                    if (char == '#')
                        listOf(col, row, 0, 0)
                    else null
                }
            }.flatten().toSet()
        ))
    }

    private fun countFinalActivatedPoints(initialState: Set<List<Int>>): Int {
        var dimensionState = initialState
        val neighborDirections = neighborDirections(dimensionState.first().size)
        repeat(6) {
            val activatedNeighborsMap = dimensionState.flatMap { point -> //Get all neighbors for each point
                neighborPoints(neighborDirections, point)
            }.fold(mutableMapOf<List<Int>, Int>()) {  accMap, point ->
                //Create a map containing how many activated points (only those that are on the list) are
                //neighbor of each point
                accMap[point] = accMap.getOrDefault(point, 0) + 1
                accMap
            }

            dimensionState = activatedNeighborsMap.filter { (point, activatedNeigbors) -> //Take new activated points
                (point in dimensionState && activatedNeigbors in 2..3) || //Was activated and has 2 or 3 neighbors
                        (point !in dimensionState && activatedNeigbors == 3) //Was not activated and has 3 neighbors
            }.map { it.key }.toSet()
        }
        return dimensionState.size
    }

    //Returns all neighbor directions for n dimensions
    private fun neighborDirections(dim: Int) =
        (1 until dim).fold((-1..1).map { i -> listOf(i) }) { points, _ ->
            points.flatMap { p -> (-1..1).map { p + it } }
        }.filterNot {
            it.all { coord -> coord == 0 }
        }

    private fun neighborPoints(directions: List<List<Int>>, point: List<Int>) =
        directions.map { n -> //For each neighbor direction
            point.mapIndexed { i, coord ->
                n[i] + coord // Return a new point with each coordinate added
            }
        }
}