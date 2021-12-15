package day

import common.DailyChallenge

class Day15 : DailyChallenge {

    override fun puzzle1(values: List<String>): Long {
        val g = Graph(values)
        var nodeToVisit = g.visitInitialNode()
        while (!g.isDestination(nodeToVisit)) {
            nodeToVisit = g.visitNode(nodeToVisit)
        }
        return nodeToVisit.v.toLong()
    }

    override fun puzzle2(values: List<String>): Long {
        val g = Graph(values)
        g.inflateMap()
        var nodeToVisit = g.visitInitialNode()
        while (!g.isDestination(nodeToVisit)) {
            nodeToVisit = g.visitNode(nodeToVisit)
        }
        return nodeToVisit.v.toLong()
    }


    class Node(val x: Int, val y: Int, var v: Int)

    class Graph(values: List<String>, private val candidates: MutableList<Node> = ArrayList()) {
        private val nodes = values.mapIndexed { row, s ->
            s.split("")
                .filter { it.isNotBlank() }
                .mapIndexed { col, v -> Node(row, col, v.toInt()) }
        }.flatten().toMutableList()

        fun inflateMap() {
            val newNodes = copies(nodes)
            nodes.clear()
            nodes.addAll(newNodes)
        }

        private fun copies(nodes: List<Node>): List<Node> {
            val subMapLength = nodes.maxOf { it.x } + 1
            val combinations = (0..4).flatMap { a -> (0..4).map {b -> Pair(a, b)}}
            return combinations.flatMap { (x, y) ->
                nodes.map {
                    val newValue = if (it.v + x + y > 9) it.v + x + y - 9 else it.v + x + y
                    Node(x * subMapLength + it.x, y * subMapLength + it.y, newValue)
                }
            }
        }

        fun visitInitialNode(): Node {
            val node = find(0, 0)!!
            neighbours(node).forEach { candidates.add(it) }
            return nodeWithLowestScore()
        }

        fun visitNode(node: Node): Node {
            neighbours(node)
                .filter { !isCandidate(it) }
                .forEach {
                    it.v += node.v
                    candidates.add(it)
                }
            nodes.removeIf(coordinatesMatch(node))
            candidates.removeIf(coordinatesMatch(node))
            return nodeWithLowestScore()
        }

        private fun coordinatesMatch(node: Node): (t: Node) -> Boolean =
            { it.x == node.x && it.y == node.y }

        private fun find(x: Int, y: Int) = nodes.firstOrNull { it.x == x && it.y == y }
        private fun isCandidate(node: Node) = candidates.any(coordinatesMatch(node))

        private fun neighbours(node: Node) =
            listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
                .mapNotNull { find(node.x + it.first, node.y + it.second) }

        private fun nodeWithLowestScore() = candidates.minByOrNull { it.v }!!

        fun isDestination(node: Node) = nodes.last().x == node.x && nodes.last().y == node.y
    }
}
