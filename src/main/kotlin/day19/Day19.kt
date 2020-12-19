package day19

import java.io.File

class Day19 {
    private val input =
        File("./main/resources/day19/input.txt").useLines { it.toList() }
    private val messages = this.input.dropWhile { it.isNotEmpty() }.drop(1)
    private val rulesInput = this.input.takeWhile { it.isNotEmpty() }.map {
        val parts = it.split(":")
        parts[0].toInt() to parts[1].trim()
    }.toMap().toSortedMap()

    fun part1() {
        println(messages.filter { matches(it, listOf(0)) }.size)
    }

    fun part2() {
        rulesInput[8] = "42 | 42 8"
        rulesInput[11] = "42 31 | 42 11 31"
        println(messages.filter { matches(it, listOf(0)) }.size)
    }

    private fun matches(message: String, rules: List<Int>): Boolean {
        if (message.isEmpty()) {
            return rules.isEmpty()
        }
        if (rules.isEmpty()) {
            return false
        }
        val first = rulesInput[rules.first()]!!
        if (first[0] == '"') {
            return if (message.startsWith(first[1])) {
                matches(message.drop(1), rules.drop(1))
            } else {
                false
            }
        }
        return first.split("|").map { it.trim() }.firstOrNull {
            matches(message, it.split(" ").map { it.toInt() } + rules.drop(1))
        } != null
    }
}