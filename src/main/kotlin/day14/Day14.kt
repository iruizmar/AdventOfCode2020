package day14

import java.io.File

class Day14 {
    val input = File("./main/resources/day14/input.txt").useLines { it.toList() }
    private val program: List<ProgramOrder> = input.map {
        val parts = it.split(" ")
        when(parts[0].substring(0,3)) {
            "mem" -> {
                val memoryDirection = parts[0].substringAfter("[").substringBefore("]").toInt()
                val value = parts[2].toInt()
                ProgramOrder.MemorySet(memoryDirection, value)
            }
            else -> {
                ProgramOrder.ChangeMask(parts[2])
            }
        }

    }

    fun part1() {
        var mask = ""
        val memory: MutableMap<Int, String> = mutableMapOf()
        program.forEach{
            when(it) {
                is ProgramOrder.ChangeMask -> mask = it.mask
                is ProgramOrder.MemorySet -> {
                    var binaryValue = Integer.toBinaryString(it.value)
                    binaryValue = "0".repeat(36-binaryValue.length) + binaryValue
                    val stringBuilder = StringBuilder(binaryValue)
                    mask.forEachIndexed { i, char ->
                        if(char == 'X') return@forEachIndexed
                        else stringBuilder.setCharAt(i, char)
                    }
                    memory[it.memoryDirection] = stringBuilder.toString()
                }
            }
        }
        var sum = 0L
        memory.forEach {
            sum+= it.value.toLong(2)
        }
        println(sum)
    }

    fun part2() {
        var mask = ""
        val memory: MutableMap<Long, Long> = mutableMapOf()
        program.forEach { programOrder ->
            when (programOrder) {
                is ProgramOrder.ChangeMask -> mask = programOrder.mask
                is ProgramOrder.MemorySet -> {
                    var memoryAddress = Integer.toBinaryString(programOrder.memoryDirection)
                    memoryAddress = "0".repeat(36 - memoryAddress.length) + memoryAddress
                    var addresses = mutableListOf("")
                    mask.forEachIndexed { i, c ->
                        when(c) {
                            '1' -> {
                                addresses.forEachIndexed {aI, a -> addresses[aI] = a + '1' }
                            }
                            '0' -> {
                                addresses.forEachIndexed { aI, a -> addresses[aI] = a + memoryAddress[i] }
                            }
                            'X' -> {
                                val newAddresses = mutableListOf<String>()
                                addresses.forEach {
                                    newAddresses.add(it)
                                    newAddresses.add(it)
                                }
                                addresses = newAddresses
                                var current = '0'
                                addresses.forEachIndexed { aI, a ->
                                    addresses[aI] = a + current
                                    current = if(current == '0') '1' else '0'
                                }
                            }
                        }
                    }
                    addresses.forEach { ma ->
                        memory[ ma.toLong(2) ] = programOrder.value.toLong()
                    }
                }
            }
        }
        println(memory.values.reduce{acc: Long, l: Long -> acc + l })
    }

    open class ProgramOrder {
        data class MemorySet(val memoryDirection: Int, val value: Int): ProgramOrder()
        data class ChangeMask(val mask: String): ProgramOrder()
    }
}