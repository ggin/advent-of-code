package advent

class Day2 implements DailyChallenge {
    def static PATTERN = "(?<x>\\d+)-(?<y>\\d+)\\s(?<l>[a-z]):\\s(?<t>[a-z]+)"

    long puzzle1(List<String> values) {
        values.collect {  parseLine(it) }
                .count { it.with { (x..y).contains(t.count(l)) } }
    }

    long puzzle2(List<String> values) {
        values.collect {parseLine(it) }
                .count{it.with {t[x - 1] == l ^ t[y - 1] == l } }
    }

    static Expando parseLine(String line) {
        (line =~ PATTERN).with {
            it.matches()
            new Expando(
                    x: it.group('x') as int,
                    y: it.group('y') as int,
                    l: it.group('l'),
                    t: it.group('t')
            )
        }
    }

}
