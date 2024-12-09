package day

import common.DailyChallenge
import common.Input
import kotlin.time.Duration.Companion.seconds

class Day9 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val s = input.singleValue
        var id = 0
        var blockFile = true
        val l: MutableList<Int?> = mutableListOf()
        s.split("").filter { it.isNotEmpty() }.forEach {
            if (blockFile) {
                l.addAll(MutableList(it.toInt()) { id })
                id++
            } else {
                l.addAll(MutableList(it.toInt()) { null })
            }
            blockFile = !blockFile
        }
        var idxFront = 0
        var idxBack = l.size - 1
        while (idxFront < idxBack) {
            if (l[idxFront] == null) {
                while (l[idxBack] == null) {
                    idxBack--
                }
                l[idxFront] = l[idxBack]
                l[idxBack] = null
            }
            idxFront++
        }
        return l.mapIndexedNotNull { index, i -> if (i != null) i.toLong() * index else 0L }.sum()
    }

    override fun puzzle2(input: Input): Long {
        val s = input.singleValue
        var id = 0
        var blockFile = true
        val l: MutableList<Pair<Int?, Int>> = mutableListOf()
        s.split("").filter { it.isNotEmpty() }.forEach {
            if (blockFile) {
                l += Pair(id, it.toInt())
                id++
            } else {
                l += Pair(null, it.toInt())
            }
            blockFile = !blockFile
        }
        var idxBack = l.size - 1
        while (idxBack > 0) {
            if (l[idxBack].first != null) {
                val idxToInsert = (1..<idxBack).find { l[it].first == null && l[it].second >= l[idxBack].second}
                if (idxToInsert != null) {
                    val diff = l[idxToInsert].second - l[idxBack].second
                    l[idxToInsert] = l[idxBack]
                    l[idxBack] = Pair(null, l[idxToInsert].second)
                    if (diff > 0) {
                        l.add(idxToInsert+1, Pair(null, diff))
                    }
                }
            }
            idxBack--

        }
        println(l)
        var idx = 0
        return l.flatMap {
            ll -> (1..ll.second).map { (ll.first ?:0L).toLong() * idx++}
        }.sum()
    }
}