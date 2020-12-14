package day13

import java.io.File
import java.lang.Math.floorMod

class Day13 {
    private val input = File("./main/resources/day13/input.txt").useLines { it.toList() }
    private val minTs = input[0].toInt()
    private val bIds = input[1].split(",").map { if (it == "x") -1 else it.toLong() }

    fun part1() {
        var currentTs = minTs
        var foundTs = -1
        var foundBusId = -1L
        do {
            bIds.forEach { bId ->
                if (bId == -1L) return@forEach
                if (currentTs % bId == 0L) {
                    foundTs = currentTs
                    foundBusId = bId
                }
            }
            currentTs++
        } while (foundTs == -1)
        println((foundTs - minTs) * foundBusId)
    }

    fun part2x() {
        var earliestTs = bIds[0]
        var increment = earliestTs
        bIds.forEachIndexed { i, bId ->
            if (bId == -1L) return@forEachIndexed
            val mod = bId - (i % bId)
            while (earliestTs % bId != mod)
                earliestTs += increment
            increment = lcm(increment, bId)
        }
        println(earliestTs)
    }

    private fun lcm(n1: Long, n2: Long): Long {
        // maximum number between n1 and n2 is stored in lcm
        var lcm = if (n1 > n2) n1 else n2

        // Always true
        while (true) {
            if (lcm % n1 == 0L && lcm % n2 == 0L) {
                break
            }
            ++lcm
        }
        return lcm
    }

    fun part2(): Long = input.getOrNull(1)
        .orEmpty()
        .splitToSequence(',')
        .withIndex()
        .mapNotNull { (i, s) -> s.toIntOrNull()?.let { floorMod(-i, it) to it } }
        .fold(0L to 1L) { (r1, q1), (r2, q2) ->
            crt(r1, q1, r2.toLong(), q2.toLong())
        }
        .first


    private fun crt(r1: Long, q1: Long, r2: Long, q2: Long): Pair<Long, Long> {
        var a = r1
        var b = r2
        while (a != b) {
            if (a < b) {
                a += (b - a + q1 - 1) / q1 * q1
            } else {
                b += (a - b + q2 - 1) / q2 * q2
            }
        }
        return a to q1 * q2
    }
}