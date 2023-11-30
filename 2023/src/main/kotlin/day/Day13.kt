package day

import common.DailyChallenge
import common.Input

// with some (a lot) of inspiration from https://github.com/tginsberg/advent-2022-kotlin/blob/main/src/main/kotlin/com/ginsberg/advent2022/Day13.kt
class Day13 : DailyChallenge {
    override fun puzzle1(input: Input) = input.values.asSequence().chunked(3)
        .map { Packet.of(it[0]).compareTo(Packet.of(it[1])) }
        .withIndex()
        .filter { it.value < 0 }.sumOf { it.index + 1 }.toLong()

    override fun puzzle2(input: Input): Long {
        val p1 = Packet.of("[[2]]")
        val p2 = Packet.of("[[6]]")
        return (input.values.filter { it.isNotEmpty() }.map { Packet.of(it) } + p1 + p2).sorted()
            .withIndex()
            .filter { it.value == p1 || it.value == p2 }
            .fold(1L) { acc, idx -> acc * (idx.index + 1) }
    }

    private sealed class Packet : Comparable<Packet> {

        companion object {

            fun of(input: String): Packet = of(
                input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                    .filter { it.isNotBlank() }
                    .filter { it != "," }
                    .iterator()
            )

            private fun of(input: Iterator<String>): Packet {
                val packets = mutableListOf<Packet>()
                while (input.hasNext()) {
                    when (val symbol = input.next()) {
                        "]" -> return ListPacket(packets)
                        "[" -> packets.add(of(input))
                        else -> packets.add(IntPacket(symbol.toInt()))
                    }
                }
                return ListPacket(packets)
            }
        }

        private class ListPacket(val packets: List<Packet>) : Packet() {
            override fun compareTo(other: Packet): Int =
                when (other) {
                    is IntPacket -> this.compareTo(other.toListPacket())
                    is ListPacket -> packets.zip(other.packets)
                        .map { it.first.compareTo(it.second) }
                        .firstOrNull { it != 0 } ?: packets.size.compareTo(other.packets.size)
                }
        }

        private class IntPacket(val amount: Int) : Packet() {
            fun toListPacket() = ListPacket(listOf(this))
            override fun compareTo(other: Packet): Int =
                when (other) {
                    is IntPacket -> amount.compareTo(other.amount)
                    is ListPacket -> toListPacket().compareTo(other)
                }
        }
    }

}
