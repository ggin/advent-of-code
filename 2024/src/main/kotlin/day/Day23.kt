package day

import common.DailyChallenge
import common.Input
import kotlin.streams.toList

class Day23 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val map1 = input.values.map { it.split("-")[0] to it.split("-")[1] }
            .groupBy({ it.first }, { it.second })
        val map2 = input.values.map { it.split("-")[0] to it.split("-")[1] }
            .groupBy({ it.second }, { it.first })
            .mapValues { (k, v) -> v.toSet() + map1.getOrDefault(k, listOf()).toSet() }
        val map = map1.filterKeys { it !in map2.keys } + map2

        val computersStartingWithT = map.filterKeys { it.startsWith("t") }

        val result = computersStartingWithT.flatMap { (computer1, linkedToComputer1) ->
            linkedToComputer1.flatMap { computer2 ->
                map.getOrDefault(computer2, setOf())
                    .filter { computer3: String ->
                        computer3 != computer1 && map.getOrDefault(computer3, setOf()).contains(computer1)
                    }.map { Pair(it, computer2) }
            }.map {
                listOf(computer1, it.first, it.second).sorted().let { Triple(it[0], it[1], it[2]) }
            }
        }.toSet()
        return result.size.toLong()
    }

    private fun maxGrouping(
        m: Map<String, Collection<String>>,
        candidates: Set<String>,
        groupingSoFar: Set<String>
    ): Set<String> {
        val newCandidates = candidates
            .filterNot { groupingSoFar.contains(it) }
            .filter { key -> groupingSoFar.all { m[key]!!.contains(it) } }
        val nextGroupings = newCandidates
            .map { (groupingSoFar + it).sorted().toSet() }
        if (nextGroupings.isEmpty()) return groupingSoFar

        return nextGroupings.map { maxGrouping(m, newCandidates.toSet(), it) }.maxBy { it.size }
    }

    override fun puzzle2(input: Input): Long {
        val map1 = input.values.map { it.split("-")[0] to it.split("-")[1] }
            .groupBy({ it.first }, { it.second })
        val map2 = input.values.map { it.split("-")[0] to it.split("-")[1] }
            .groupBy({ it.second }, { it.first })
            .mapValues { (k, v) -> v.toSet() + map1.getOrDefault(k, listOf()).toSet() }
        val map = map1.filterKeys { it !in map2.keys } + map2

        val results = map.flatMap { (computer1, linkedToComputer1) ->
            linkedToComputer1.flatMap { computer2 ->
                map.getOrDefault(computer2, setOf())
                    .filter { computer3: String ->
                        computer3 != computer1 && map.getOrDefault(computer3, setOf()).contains(computer1)
                    }.map { Pair(it, computer2) }
            }.map {
                listOf(computer1, it.first, it.second).sorted().let { setOf(it[0], it[1], it[2]) }
            }
        }.toSet()

        val password = results.parallelStream().map { result ->
            println("Working on $result")
            maxGrouping(map, map.keys, result)
        }.toList().maxBy { it.size }.sorted().joinToString(",")
        println(password)
        // bx,cl,ga,hk,im,kw,lx,mr,oi,ow,pz,sg,wu WRONG*/
        return 0L
    }
}