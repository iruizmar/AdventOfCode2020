package day18

import java.io.File

class Day18 {
    private val input =
        File("./main/resources/day18/input.txt").useLines { it.toList() }

    fun part1() = println(input.map { solve(it, this::solveInOrder) }.sum())
    fun part2() = println(input.map { solve(it, this::solveWithAddFirst) }.sum())

    private fun solve(expression: String, flatExpressionSolver: (flatExpression: String) -> Long): Long {
        var flatExpression = expression.replace(" ", "")
        while (flatExpression.contains("(")) {
            for (i in flatExpression.indices) {
                val nextClose = flatExpression.indexOf(")", i)
                val nextOpen =
                    flatExpression.indexOf("(", i + 1).let { if (it == -1) flatExpression.length else it }
                if (flatExpression[i] == '(' && nextClose < nextOpen) {
                    flatExpression = flatExpression.substring(0, i) + flatExpressionSolver.invoke(
                        flatExpression.substring(
                            i + 1,
                            nextClose
                        )
                    ) + flatExpression.substring(nextClose + 1)
                    break
                }
            }
        }
        return flatExpressionSolver.invoke(flatExpression)
    }

    private fun solveInOrder(operation: String): Long {
        val operations = operation.filter { it == '+' || it == '*' }.toMutableList()
        return operation.split('+', '*').map { it.toLong() }
            .reduce { acc, l -> if (operations.removeFirst() == '+') acc + l else acc * l }
    }

    private fun solveWithAddFirst(operation: String): Long {
        return operation.split("*").map { solveInOrder(it) }.reduce { acc, l -> acc * l }
    }
}