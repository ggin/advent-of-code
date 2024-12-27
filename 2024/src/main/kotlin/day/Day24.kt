package day

import common.DailyChallenge
import common.Input

class Day24 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val wires =
            input.values.filter { it.contains(":") }
                .associate { Pair(it.split(":")[0], it.split(":")[1].trim() == "1") }.toMutableMap()
        val gates = input.values.filter { it.contains("->") }.map {
            GateOperation(it.substring(0, 3), it.split(" ")[2], it.split(" ")[1], it.split(">")[1].trim())
        }.toMutableList()

        while (gates.isNotEmpty()) {
            val gatesToOperate = gates.filter { wires.contains(it.gate1) && wires.contains(it.gate2) }
            gatesToOperate.forEach { gateOp ->
                val outcome = operate(gateOp, wires)
                wires[gateOp.dest] = outcome
            }
            gates.removeAll(gatesToOperate)
        }
        val zKeys = wires.keys.filter { it.startsWith("z") }.sortedDescending()
        val zString = zKeys.map { if (wires[it]!!) "1" else "0" }.reduce { acc, s -> acc + s }
        return zString.toLong(2)
    }

    data class GateOperation(val gate1: String, val gate2: String, val op: String, val dest: String)

    private fun operate(operation: GateOperation, wires: Map<String, Boolean>): Boolean {
        val gate1 = wires[operation.gate1]!!
        val gate2 = wires[operation.gate2]!!
        return when (operation.op) {
            "AND" -> gate1 && gate2
            "OR" -> gate1 || gate2
            "XOR" -> gate1 xor gate2
            else -> throw IllegalArgumentException("Unsupported op $operation.op")
        }
    }


    override fun puzzle2(input: Input): Long {
        val originalGates = input.values.filter { it.contains("->") }.map {
            GateOperation(it.substring(0, 3), it.split(" ")[2], it.split(" ")[1], it.split(">")[1].trim())
        }

        val gates = swapPairs(originalGates, setOf(Pair("z05", "tst"), Pair("sps", "z11"), Pair("z23", "frt"), Pair("cgh", "pmd")))
        var carryOver = "wrs"
        (1..44).forEach { index ->
            val x = "x${index.toString().padStart(2, '0')}"
            val y = "y${index.toString().padStart(2, '0')}"
            val z = "z${index.toString().padStart(2, '0')}"
            println("Processing $z")

            val xor1 = gates.findDestGate(x, y, "XOR")
            val z1 = gates.findDestGate(xor1, carryOver, "XOR")
            if (z1 != z) println("Different Zs, computed $z1, actual $z")

            val and1 = gates.findDestGate(x, y, "AND")
            val and2 = gates.findDestGate(xor1, carryOver, "AND")
            carryOver = gates.findDestGate(and1, and2, "OR")
        }

        val result = setOf(Pair("z05", "tst"), Pair("sps", "z11"), Pair("z23", "frt"), Pair("cgh", "pmd"))
            .flatMap { listOf(it.first, it.second ) }
            .sorted()
            .joinToString(",")
        println(result)
        return 0L
    }

    private fun List<GateOperation>.findDestGate(s1: String, s2: String, op: String): String {
        val destGate = this.firstOrNull { setOf(s1, s2).contains(it.gate1) && setOf(s1, s2).contains(it.gate2) && it.op == op }?.dest
        if (destGate == null) println("Could not find dest gate for s1 $s1, s2 $s2, op $op")
        return destGate!!
    }

    private fun swapPairs(gates: List<GateOperation>, swaps: Set<Pair<String, String>>): List<GateOperation> {
        val newGates = gates.toMutableList()
        swaps.forEach { swap ->
            val index1 = gates.indexOfFirst { it.dest == swap.first }
            val gate1 = gates.first { it.dest == swap.first }
            val index2 = gates.indexOfFirst { it.dest == swap.second }
            val gate2 = gates.first { it.dest == swap.second }
            newGates[index1] = GateOperation(gate1.gate1, gate1.gate2, gate1.op, gate2.dest)
            newGates[index2] = GateOperation(gate2.gate1, gate2.gate2, gate2.op, gate1.dest)
        }
        return newGates
    }

}