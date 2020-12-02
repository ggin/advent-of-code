package advent

import java.util.regex.Matcher

class Day2 {
    def static PATTERN = "(?<x>\\d+)-(?<y>\\d+)\\s(?<l>[a-z]):\\s(?<t>[a-z]+)"
    def static VALUES = Day1.class.getResource("/day2-input.txt").readLines()

    static long puzzle1() {
        VALUES.collect { (it =~ PATTERN).with { parseLine(it) } }
                .count { it.with { (x..y).contains(t.count(l)) } }
    }

    static long puzzle2() {
        VALUES.collect { (it =~ PATTERN).with { parseLine(it) } }
        .count{it.with {t[x - 1] == l ^ t[y - 1] == l } }
    }

    private static Expando parseLine(Matcher it) {
        it.matches()
        new Expando(
                x: it.group('x') as int,
                y: it.group('y') as int,
                l: it.group('l'),
                t: it.group('t')
        )
    }

    static void main(String[] args) {
        println puzzle1()
        println puzzle2()
    }
}
