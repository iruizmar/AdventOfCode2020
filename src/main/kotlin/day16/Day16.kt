package day16

import java.io.File

class Day16 {
    data class Rule(val name: String, val range1: IntRange, val range2: IntRange)

    private val input = File("./main/resources/day16/input.txt").useLines { it.toList() }
    private val rules = input.takeWhile { it.isNotEmpty() }.map {
        val ruleRegex = """(\w+ ?\w+?): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
        val (name, minRange1, maxRange1, minRange2, maxRange2) = ruleRegex.find(it)!!.destructured
        Rule(
            name, IntRange(minRange1.toInt(), maxRange1.toInt()),
            IntRange(minRange2.toInt(), maxRange2.toInt())
        )
    }
    private var myTicket =
        input[input.indexOfFirst { it == "your ticket:" } + 1].split(",").map { it.toInt() }
    private val nearTickets =
        input.subList(input.indexOfFirst { it == "nearby tickets:" } + 1, input.size)
            .map { it.split(",").map { it.toInt() } }

    fun part1() {
        println(nearTickets.flatten().filter { f -> !rules.any { r -> r.range1.contains(f) || r.range2.contains(f) } }
            .sum())
    }

    fun part2() {
        val validTickets: List<List<Int>> = nearTickets.union(listOf(myTicket)).filter { t ->
            t.all { f ->
                rules.any { r -> r.range1.contains(f) || r.range2.contains(f) }
            }
        }
        val possiblePositions: Map<String, MutableList<Int>> = rules.map { r ->
            Pair(r.name, (myTicket.indices).toMutableList())
        }.toMap()
        validTickets.forEach { t ->
            t.forEachIndexed { i, f ->
                rules.filter { r -> //Which rules are not valid for this field?
                    !r.range1.contains(f) && !r.range2.contains(f)
                }.forEach { r -> //Remove them from possiblePositions
                    possiblePositions[r.name]!!.remove(i)
                }
            }
        }
        while (!possiblePositions.values.all { it.size == 1 }) { //This is supposed to finish, isn't it?
            val foundPositions =
                possiblePositions.filterValues { it.size == 1 }.flatMap { it.value }.toList()
            possiblePositions.filterValues { it.size > 1 }.forEach { pp ->
                pp.value.removeIf { position -> foundPositions.contains(position) }
            }
        }

        val departureIndexes =
            possiblePositions.filterKeys { it.split(" ")[0] == "departure" }.map { it.value[0] }

        println(myTicket.filterIndexed { i, _ -> departureIndexes.contains(i) }.map { it.toLong() }
            .reduce { acc, i -> acc * i })
    }
}