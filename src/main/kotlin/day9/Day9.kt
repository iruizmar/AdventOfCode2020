package day9

class Day9 {
    private val preambleSize = 25
    private val inputTest = listOf(
        "35",
        "20",
        "15",
        "25",
        "47",
        "40",
        "62",
        "55",
        "65",
        "95",
        "102",
        "117",
        "150",
        "182",
        "127",
        "219",
        "299",
        "277",
        "309",
        "576"
    )
    private val realInput = Day9Input.input
    private val input = realInput.map { it.toLong() }
    fun part1(): Long {
        input.forEachIndexed { i, x ->
            if (i < preambleSize) return@forEachIndexed
            if (isValid(x, input.subList(i - preambleSize, i))) return@forEachIndexed
            else {
                println(x)
                return x
            }
        }
        return -1
    }

    fun part2() {
        val badNumber = part1()
        var foundList: List<Long>? = null
        input.forEachIndexed { i, x ->
            val list = mutableListOf(x)
            var sum = x
            input.subList(i + 1, input.size).forEach { y ->
                sum += y
                list.add(y)
                if (list.size > 1 && sum == badNumber) {
                    foundList = list
                    return@forEachIndexed
                }
            }
        }
        foundList?.let {
            val sorted = it.sorted()
            println(sorted[0] + sorted[sorted.size - 1])
        }
    }

    private fun isValid(number: Long, preamble: List<Long>): Boolean {
        preamble.forEachIndexed { i, x ->
            preamble.forEachIndexed { ii, y ->
                if (i != ii && (x + y) == number) return true
            }
        }
        return false
    }
}