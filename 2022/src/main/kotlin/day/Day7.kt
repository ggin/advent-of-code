package day

import common.DailyChallenge
import common.Input
import java.util.*

class Day7 : DailyChallenge {
    // 4473403

    override fun puzzle1(input: Input): Long {
        val tree = parseTree(input.values)
        var candidates = mutableListOf<FileNode>()
        filterCandidates(tree, candidates)
        return candidates.sumOf { it.dirSize() }
    }

    private fun filterCandidates(tree: FileNode, candidates: MutableList<FileNode>) {
        if (tree.isDir()) {
            if (tree.dirSize() <= 100000) {
                candidates += tree
            }
            tree.children.values.forEach { filterCandidates(it, candidates) }
        }
    }

    private fun filterCandidates2(tree: FileNode, candidates: MutableList<FileNode>, requiredSize: Long) {
        if (tree.isDir()) {
            if (tree.dirSize() > requiredSize) {
                candidates += tree
            }
            tree.children.values.forEach { filterCandidates2(it, candidates, requiredSize) }
        }
    }

    override fun puzzle2(input: Input): Long {
        val tree = parseTree(input.values)
        var currentUnusedSpace = 70000000 - tree.dirSize()
        val requiredDirSize = 30000000 - currentUnusedSpace
        var candidates = mutableListOf<FileNode>()
        filterCandidates2(tree, candidates, requiredDirSize)
        return candidates.minOf { it.dirSize() }
    }

    private fun parseTree(values: List<String>): FileNode {
        var currentDir = FileNode("/", 0, null, mutableMapOf())
        val queue = LinkedList(values.drop(1))
        while (queue.isNotEmpty()) {
            val line = queue.pop()
            if (line.startsWith("$")) {
                if (line.contains("cd")) {
                    currentDir = parseCdCommand(line, currentDir)
                } else if (line.contains("ls")) {
                    var children = mutableListOf<String>()
                    while (queue.isNotEmpty() && !queue.peek().startsWith("$")) {
                        children += queue.pop()
                    }
                    updateChildren(currentDir, children)
                }
            }
        }
        return rootNode(currentDir);
    }

    private val cdRegex = "\\$ cd (?<args>.*)".toRegex()

    private fun parseCdCommand(line: String, currentDirectory: FileNode): FileNode {
        return when (val cdParam = cdRegex.find(line)!!.groupValues[1]) {
            "/" -> rootNode(currentDirectory)
            ".." -> currentDirectory.parent!!
            else -> currentDirectory.cd(cdParam)
        }
    }

    private fun updateChildren(currentDir: FileNode, children: List<String>) {
        children.forEach { child ->
            if (child.startsWith("dir")) {
                val dirName = "dir (?<args>.*)".toRegex().find(child)!!.groupValues[1]
                currentDir.children.putIfAbsent(dirName, FileNode(dirName, 0, currentDir, mutableMapOf()))
            } else {
                val group = "(?<size>\\d+) (?<filename>.*)".toRegex().find(child)!!.groupValues
                currentDir.children.putIfAbsent(
                    group[2],
                    FileNode(group[2], group[1].toLong(), currentDir, mutableMapOf())
                )
            }
        }
    }

    private fun rootNode(currentDirectory: FileNode): FileNode =
        if (currentDirectory.parent != null) rootNode(currentDirectory.parent) else currentDirectory

    class FileNode(
        val name: String,
        private val size: Long,
        val parent: FileNode?,
        val children: MutableMap<String, FileNode>
    ) {
        internal fun cd(dir: String) = children[dir]!!
        internal fun isDir() = children.isNotEmpty()
        internal fun dirSize(): Long = children.values.sumOf { if (it.isDir()) it.dirSize() else it.size }

        internal fun filterDir(predicate: (n: FileNode) -> Boolean): List<FileNode> {
            return children.values.filter(predicate) +
                    children.values.flatMap { filterDir(predicate) }
        }
    }
    
}
