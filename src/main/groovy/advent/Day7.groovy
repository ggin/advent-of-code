package advent

class Day7 implements DailyChallenge {

    def p1 = /(?<colour>\w+ \w+) bags /
    def p2 = / (?<q>\d+) (?<c>\w+ \w+) bag(s)?(.)?/
    def colourToFind = "shiny gold"

    long puzzle1(List<String> values) {
        Map<String, List<String>> map = [:]
        values.each { value ->
            def m = value.split("contain")
            def matcher = m[0] =~ p1
            matcher.matches()
            def parent = matcher.group("colour")
            def m2 = m[1].split(",")
            m2.each {
                if (it != " no other bags.") {
                    def matcher2 = it =~ p2
                    matcher2.matches()
                    def q = matcher2.group("q")
                    def c = matcher2.group("c")
                    map.putIfAbsent(c, [])
                    map.get(c).add(parent)
                }
            }
        }
        recurs(map, colourToFind).size() - 1 // minus the colour we want to search
    }

    Set<String> recurs(Map<String, List<String>> map, String s) {
        if (map.containsKey(s)) {
            def colourSet = map[s].collect { recurs(map, it) }.flatten() as Set
            colourSet.add(s)
            colourSet
        } else {
            Set.of(s)
        }
    }

    long puzzle2(List<String> values) {
        Map<String, Map<String, Integer>> map = [:]
        values.each { value ->
            def m = value.split("contain")
            def matcher = m[0] =~ p1
            matcher.matches()
            def parent = matcher.group("colour")
            def m2 = m[1].split(",")
            m2.each {
                if (it != " no other bags.") {
                    def matcher2 = it =~ p2
                    matcher2.matches()
                    def q = matcher2.group("q") as Integer
                    def c = matcher2.group("c") as String
                    map.putIfAbsent(parent, [:])
                    map.get(parent).put(c, q)
                }
            }
        }
        println map
        recurs2(map, colourToFind)
    }

    long recurs2(Map<String, Map<String, Integer>> map, String s) {
        if (map.containsKey(s)) {
            map[s].collect { colour, number ->
                number + (number * recurs2(map, colour))
            }.sum() as long
        } else {
            0
        }

    }
}
