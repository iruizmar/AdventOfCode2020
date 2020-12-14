package day10

import java.io.File


class Day10 {
    private var input = File("./main/resources/day10/input.txt").useLines { it.toList() }.map { it.toInt() }
    fun part1() {
        input = input.sorted()
        input = input + (input.last() + 3)
        var currentJoltage = 0
        var numberOf1Difs = 0
        var numberOf3Difs = 0
        input.forEach { j ->
            val dif = j - currentJoltage
            if (dif <= 3) {
                currentJoltage = j
                if (dif == 1) numberOf1Difs++
                else if (dif == 3) numberOf3Difs++
            }
        }
        println(numberOf1Difs)
        println(numberOf3Difs)
        println(numberOf1Difs * numberOf3Difs)
    }

    fun part2() {
        input = input.sorted()
        input = input + (input.last() + 3)
        val arrangements = getArrangements(input.toMutableList(), 0)
        println(arrangements)
    }

    private val cache: MutableMap<Int, Map<List<Int>, Long>> = mutableMapOf()

    private fun getArrangements(
        remainingAdapters: MutableList<Int>,
        previousJoltage: Int,
    ): Long {
        //Last element
        if (remainingAdapters.isEmpty()) {
            return 1
        }

        //Is in cache?
        getFromCache(previousJoltage, remainingAdapters)?.let {
            return it
        }

        val nextAdapter: Int = remainingAdapters.removeAt(0)
        var count = getArrangements(remainingAdapters, nextAdapter)

        if (remainingAdapters.isNotEmpty() && remainingAdapters[0] - previousJoltage <= 3) {
            count += getArrangements(remainingAdapters, previousJoltage)
        }

        remainingAdapters.add(0, nextAdapter)
        putInCache(previousJoltage, remainingAdapters, count)
        return count
    }

    private fun putInCache(previousJoltage: Int, remainingAdapters: MutableList<Int>, count: Long) {
        val map: MutableMap<List<Int>, Long> = HashMap()
        map[remainingAdapters] = count
        cache[previousJoltage] = map
    }

    private fun getFromCache(previousJoltage: Int, remainingAdapters: MutableList<Int>): Long? =
        cache[previousJoltage]?.let {
            it[remainingAdapters]
        }

}