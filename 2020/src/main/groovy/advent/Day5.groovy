package advent

class Day5 implements DailyChallenge {

    long puzzle1(List<String> values) {
        seatIDs(values).max()
    }

    long puzzle2(List<String> values) {
        def l = seatIDs(values)
        (l.min()..l.max()).find { !l.contains(it)}
    }


    def rows(Integer l, Integer u, String c) {
        c == "F" ? [l, (l + u).intdiv(2)] : [(l +u).intdiv(2) + 1, u]
    }

    def columns(def l, def u, def c) {
        c == "L" ? [l, (l + u).intdiv(2)] : [(l+u).intdiv(2) + 1, u]
    }

    def seatIDs(def values) {
        values.collect { value ->
            def l = 0
            def u = 127
            def a = 0
            def b = 7
            def i = 0
            while (l != u) { (l, u) = rows(l, u, value[i++]) }
            while (a != b) { (a, b) = columns(a, b, value[i++]) }
            l * 8 + a as long
        }
    }
}
