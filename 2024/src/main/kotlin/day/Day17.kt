package day

import common.DailyChallenge
import common.Input
import kotlin.math.pow

class Day17 : DailyChallenge {

    data class Registers(var a: Long, var b: Long, var c: Long)

    enum class Operand(val operand: Int, val function: (Registers) -> Long) {
        ZERO(0, { 0L }),
        ONE(1, { 1L }),
        TWO(2, { 2L }),
        THREE(3, { 3L }),
        FOUR(4, { r -> r.a }),
        FIVE(5, { r -> r.b }),
        SIX(6, { r -> r.c }),
        SEVEN(7, { _ -> throw UnsupportedOperationException() }),;

        fun combo(registers: Registers): Long {
            return function(registers)
        }

        fun literal(): Long = operand.toLong()

    }

    data class Program(var pointer: Int = 0, private val output: MutableList<String> = mutableListOf()) {
        fun addOutput(l: Long) {
            output += l.toString()
        }

        fun printOutput() = output.joinToString(",")
    }


    enum class Instructions(val opcode: Int, val function: (Registers, Operand, Program) -> Unit) {
        ADV(0, { r, operand, _ -> r.a /= 2.0.pow(operand.combo(r).toDouble()).toLong() }),
        BXL(1, { r, operand, _ -> r.b = r.b xor operand.literal() }),
        BST(2, { r, operand, _ -> r.b = operand.combo(r).mod(8L) }),
        JNZ(3, { r, operand, ip -> if (r.a != 0L) ip.pointer = operand.literal().toInt() - 1 }),
        BXC(4, { r, _, _ -> r.b = r.b xor r.c }),
        OUT(5, { r, operand, program -> program.addOutput(operand.combo(r).mod(8L)) }),
        BDV(6, { r, operand, _ -> r.b = r.a / 2.0.pow(operand.combo(r).toDouble()).toLong() }),
        CDV(7, { r, operand, _ -> r.c = r.a / 2.0.pow(operand.combo(r).toDouble()).toLong() });
    }

    override fun puzzle1(input: Input): Long {
        val values = input.values
        val registers = Registers(
            values[0].split(":")[1].trim().toLong(),
            values[1].split(":")[1].trim().toLong(),
            values[2].split(":")[1].trim().toLong()
        )
        val instructions = values[4].split(":")[1].trim().split(",").map { it.toInt() }.chunked(2)
        val program = Program()
        while (program.pointer != instructions.size) {
            val instruction = instructions[program.pointer]
            val i = Instructions.entries.find { it.opcode == instruction[0] }!!
            val o = Operand.entries.find { it.operand == instruction[1] }!!
            i.function(registers, o, program)
            program.pointer++
        }

        println(program.printOutput())
        return program.printOutput().replace(",","").toLong()
    }

    override fun puzzle2(input: Input): Long {
        val values = input.values
        val registers = Registers(
            2855440L,
            values[1].split(":")[1].trim().toLong(),
            values[2].split(":")[1].trim().toLong()
        )
        val expectedOutput = values[4].split(":")[1].trim()
        val instructions = expectedOutput.split(",").map { it.toInt() }.chunked(2)
        return runProgram(instructions, "", 0L, expectedOutput)!!
    }

    private fun runProgram(instructions: List<List<Int>>, output: String, a: Long, expectedOutput: String) : Long? {
        if (output == expectedOutput) return a
        if (!expectedOutput.endsWith(output)) return null
        return (0..7).map { 8 * a + it}.mapNotNull { newA ->
            val registers = Registers(newA, 0, 0)
            val program = Program()
            while (program.pointer != instructions.size) {
                val instruction = instructions[program.pointer]
                val i = Instructions.entries.find { it.opcode == instruction[0] }!!
                val o = Operand.entries.find { it.operand == instruction[1] }!!
                if (i != Instructions.JNZ) {
                    i.function(registers, o, program)
                }
                program.pointer++
            }
            val newOutput = if (output == "") program.printOutput() else program.printOutput() + "," + output
            runProgram(instructions, newOutput, newA, expectedOutput)
        }.minOrNull()
    }
}