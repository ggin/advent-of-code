package day

import common.DailyChallenge
import common.Input


class Day10 : DailyChallenge {

    private val regex = "addx (?<x>[\\d-]+)".toRegex()

    override fun puzzle1(input: Input): Long {
        var register = 1L
        var result = 0L
        var cycle = 1
        input.values.forEach { line ->
            if (line == "noop") {
                cycle += 1
            } else {
                val x = regex.find(line)!!.groupValues[1].toInt()
                cycle += 1
                result = eval(cycle, register, result)
                cycle += 1
                register += x
            }
            result = eval(cycle, register, result)
        }
        return result
    }

    private fun eval(cycle: Int, register: Long, result: Long) =
        if ((cycle - 20).mod(40) == 0) result + register * cycle else result

    override fun puzzle2(input: Input): Long = 0L

    override fun puzzle2S(input: Input): String {
        var register = 1
        var result = ""
        var cycle = 1
        input.values.forEach { line ->
            result = eval2(cycle, register, result)
            if (line == "noop") {
                cycle += 1
            } else {
                val x = regex.find(line)!!.groupValues[1].toInt()
                cycle += 1
                result = eval2(cycle, register, result)
                cycle += 1
                register += x
            }
        }
        return result
    }

    private fun eval2(cycle: Int, x: Int, result: String) : String {
        val c = (cycle - 1).mod(40)
        var s = result
        if (cycle > 0 && c == 0) s += "\n"
        return s + (if (x - 1 == c || x == c || x + 1 == c) "#" else ".")
    }


}
