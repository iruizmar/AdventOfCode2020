package day15

import java.io.File

class Day15 {
    private val turns: MutableList<Int> =
        File("./main/resources/day15/input.txt")
            .useLines { it.toList() }[0].split(",")
            //.mapIndexed {i, n -> Pair(i, n.toInt()) }.toMap().toMutableMap()
            .map { it.toInt() }.toMutableList()

    fun part1() {
        while (turns.size < 2020) {
            val numbersSpoken = turns
            val lastNumber = numbersSpoken.last()
            if (numbersSpoken.count { it == lastNumber } > 1) {
                var first = false
                var turn = -1
                run loop@{
                    turns.reversed().forEachIndexed { i, n ->
                        if (n == lastNumber) {
                            if (!first) first = true
                            else {
                                turn = turns.size - i
                                return@loop
                            }
                        }
                    }
                }
                turns.add(turns.size - turn)
            } else {
                turns.add(0)
            }
        }
        println(turns.last())
    }

    fun part2() {
        val turnsSpoken: MutableMap<Int, MutableList<Int>> = mutableMapOf()
        turns.forEachIndexed { i, n ->
            if(!turnsSpoken.containsKey(n)) turnsSpoken[n] = mutableListOf()
            turnsSpoken[n]!!.add(i + 1)
        }
        while (turns.size < 30000000) {
            val numbersSpoken = turns
            val lastNumber = numbersSpoken.last()
            var newNumber = 0
            if(turnsSpoken[lastNumber]!!.size > 1) {
                val turnsList = turnsSpoken[lastNumber]!!
                newNumber = turnsList.last() - turnsList[turnsList.size - 2]
            }
            if(!turnsSpoken.containsKey(newNumber)) turnsSpoken[newNumber] = mutableListOf()
            turns.add(newNumber)
            turnsSpoken[newNumber]!!.add(turns.size)
        }
        println(turns.last())
    }
}