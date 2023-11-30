package day

import common.Input
import java.util.*

typealias Stacks = MutableMap<Int, Stack<Char>>

class Day5 {

    private val regex = "move (?<m>\\d+) from (?<s>\\d+) to (?<e>\\d+)".toRegex()

    fun puzzle1(input: Input): String {
        val stacks = parseStacks(input)
        moveStacks(input, stacks, this::moveCratesIndividually)
        return (1..stacks.size).map { stacks[it]!!.peek() }.joinToString("")
    }

    fun puzzle2(input: Input): String {
        val stacks = parseStacks(input)
        moveStacks(input, stacks, this::moveCratesInGroups)
        return (1..stacks.size).map { stacks[it]!!.peek() }.joinToString("")
    }

    private fun parseStacks(input: Input): MutableMap<Int, Stack<Char>> {
        val stacks = mutableMapOf<Int, Stack<Char>>()
        input.values.takeWhile { it.contains("[") }
            .forEach { line ->
                line.chunked(4) { it[1] }.forEachIndexed { index, crate ->
                    if (crate != ' ')
                        stacks.getOrPut(index + 1) { Stack<Char>() }.add(0, crate)
                }
            }
        return stacks
    }

    private fun moveStacks(
        input: Input,
        stacks: MutableMap<Int, Stack<Char>>,
        strategy: (stacks: Stacks, nbCrates: Int, fromStack: Int, toStack: Int) -> Unit
    ) {
        input.values.filter { it.startsWith("move") }
            .forEach {
                regex.find(it)!!.groupValues.let { match ->
                    strategy(
                        stacks,
                        match[1].toInt(),
                        match[2].toInt(),
                        match[3].toInt()
                    )
                }
            }
    }

    private fun moveCratesIndividually(stacks: Stacks, nbCrates: Int, fromStack: Int, toStack: Int) {
        (1..nbCrates).forEach { _ -> stacks[toStack]!!.push(stacks[fromStack]!!.pop()) }
    }

    private fun moveCratesInGroups(stacks: Stacks, nbCrates: Int, fromStack: Int, toStack: Int) {
        val insertIndex = stacks[toStack]!!.size
        (1..nbCrates).forEach { _ -> stacks[toStack]!!.add(insertIndex, stacks[fromStack]!!.pop()) }
    }

    private fun filepath(suffix: String) = "/day5-${suffix}.txt"

    fun getInput(suffix: String) = Input(filepath(suffix))


    fun run() {
        val input = getInput("input")
        println("Running day 5 challenge")
        println("Puzzle 1: ${puzzle1(input)}")
        println("Puzzle 2: ${puzzle2(input)}")
    }

}
