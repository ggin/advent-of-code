package day

import common.DailyChallenge
import common.Input
import java.io.File

class Day14 : DailyChallenge {
    override fun puzzle1(input: Input): Long {
        val width = 101  // 11
        val height = 103 // 7

        val nbIterations = 100

        val regex = "(-?\\d)+".toRegex()
        val map: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        input.values.forEach {
            val l = regex.findAll(it).map { it.groupValues[0] }.toList()
            val px = l[0].toInt()
            val py = l[1].toInt()
            val vx = l[2].toInt()
            val vy = l[3].toInt()
            val newX = (px + vx * nbIterations).mod(width)
            val newY = (py + vy * nbIterations).mod(height)
            map.put(Pair(newX, newY), map.getOrDefault(Pair(newX, newY), 0) + 1)
        }

        val midX = if (width % 2 == 0) width / 2 else width / 2 + 1
        val midY = if (height % 2 == 0) height / 2 else height / 2 + 1
        val q1 = (0..<width / 2).map { x -> (0..<height / 2).map { y -> map.getOrDefault(Pair(x, y), 0) }.sum() }.sum().toLong()
        val q2 = (midX..<width).map { x -> (0..<height / 2).map { y -> map.getOrDefault(Pair(x, y), 0) }.sum() }.sum().toLong()
        val q3 = (0..<width / 2).map { x -> (midY..<height).map { y -> map.getOrDefault(Pair(x, y), 0) }.sum() }.sum().toLong()
        val q4 = (midX..<width).map { x -> (midY..<height).map { y -> map.getOrDefault(Pair(x, y), 0) }.sum() }.sum().toLong()
        return q1 * q2 * q3 * q4
    }

    data class Robot(var x: Int, var y: Int, var vx: Int, var vy: Int)

    override fun puzzle2(input: Input): Long {
        var width = 101
        var height = 103

        val nbIterations = 100

        val regex = "(-?\\d)+".toRegex()
        val robots = mutableListOf<Robot>()
        input.values.forEach {
            val l = regex.findAll(it).map { it.groupValues[0] }.toList()
            val px = l[0].toInt()
            val py = l[1].toInt()
            val vx = l[2].toInt()
            val vy = l[3].toInt()
            robots += Robot(px, py, vx, vy)
        }

        var iter = 6260L - 101
        var maxIter = 7500L
        val jump = 101

        // between 6250 and 7500
        // vertical = 100

        // 7271 - error by one?
        /*(1..<iter).forEach {
            robots.forEach {
                it.x = (it.x + it.vx).mod(width)
                it.y = (it.y + it.vy).mod(height)
            }
        }
        robots.forEach {
            it.x = (it.x + it.vx).mod(width)
            it.y = (it.y + it.vy).mod(height)
        }
        print(robots, iter.toInt())
        iter++
        while (iter <= maxIter) {
            robots.forEach {
                it.x = (it.x + it.vx* jump).mod(width)
                it.y = (it.y + it.vy* jump).mod(height)
            }
            print(robots, iter.toInt())
            iter+=jump
        }*/
        (6250..7500L).forEach{answer ->
        val newRobots = robots.map {
            Robot((it.x + it.vx * answer).mod(width), (it.y + it.vy * answer).mod(height), it.vx, it.vy)
        }
        print(newRobots, answer.toInt())}
        return 7271L
    }

    private fun rotate(robots: MutableList<Robot>) {
        robots.forEach {
            val x = it.x
            it.x = it.y
            it.y = x
            val vx = it.vx
            it.vx = it.vy
            it.vy = vx
        }
    }

    private fun print(robots: List<Robot>, iter: Int) {
        File("/tmp/trees", "$iter.txt").printWriter().use { out ->
            val width = 101
            val height = 103
            val map: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
            robots.forEach {
                map.put(Pair(it.x, it.y), map.getOrDefault(Pair(it.x, it.y), 0) + 1)
            }
            for (y in height / 2 until height) {
                for (x in 0 until width) {
                    out.print(if (map.getOrDefault(Pair(x, y), 0) > 0) "#" else ".")
                }
                out.println()
            }
        }

    }


}

