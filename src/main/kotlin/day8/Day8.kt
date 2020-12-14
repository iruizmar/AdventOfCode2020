package day8

class Day8 {
    fun part1() {
        var accumulatorValue = 0
        val runOperationsIndex = mutableListOf<Int>()
        var operationIndex = 0
        while (true) {
            if (runOperationsIndex.contains(operationIndex)) return
            val operationParts = Day8Input.input[operationIndex].split(" ")
            val operation = operationParts[0]
            val operationAmount = operationParts[1].toInt()
            //println("$operation $operationAmount: $accumulatorValue")
            runOperationsIndex.add(operationIndex)
            when (operation) {
                "acc" -> {
                    accumulatorValue += operationAmount
                    operationIndex++
                }
                "jmp" -> {
                    operationIndex += operationAmount
                }
                else -> {
                    operationIndex++
                }
            }
            println(accumulatorValue)
        }
    }

    fun part2() {
        val input = listOf(
            "nop +0",
            "acc +1",
            "jmp +4",
            "acc +3",
            "jmp -3",
            "acc -99",
            "acc +1",
            "jmp -4",
            "acc +6"
        )
        Day8Input.input.forEachIndexed { index, operationString ->
            val inputClone = Day8Input.input.toMutableList()
            val (operation, amount) = extractOperation(operationString)
            when (operation) {
                "nop" -> {
                    inputClone[index] = inputClone[index].replace("nop", "jmp")
                }
                "jmp" -> {
                    inputClone[index] = inputClone[index].replace("jmp", "nop")
                }
                else -> {
                    return@forEachIndexed
                }
            }
            println(inputClone)
            val result = runProgram(inputClone)
            if (result != -1) {
                println(result)
                return
            }
        }
    }

    private fun runProgram(program: List<String>): Int {
        var accumulatorValue = 0
        val runOperationsIndex = mutableListOf<Int>()
        var operationIndex = 0
        while (operationIndex < program.size) {
            if (runOperationsIndex.contains(operationIndex)) return -1
            val (operation, amount) = extractOperation(program[operationIndex])
            //println("$operation $operationAmount: $accumulatorValue")
            runOperationsIndex.add(operationIndex)
            when (operation) {
                "acc" -> {
                    accumulatorValue += amount
                    operationIndex++
                }
                "jmp" -> {
                    operationIndex += amount
                }
                else -> {
                    operationIndex++
                }
            }
        }
        return accumulatorValue;
    }

    private fun extractOperation(op: String): Pair<String, Int> {
        val operationParts = op.split(" ")
        val operation = operationParts[0]
        val operationAmount = operationParts[1].toInt()
        return Pair(
            operation,
            operationAmount
        )
    }
}