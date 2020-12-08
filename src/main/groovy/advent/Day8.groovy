package advent

class Day8 implements DailyChallenge {

    def pattern = /(?<ins>(nop|acc|jmp)) (?<sign>[\+-])(?<value>\d+)/

    long puzzle1(List<String> values) {
        def (_, acc) = processList(values)
        acc
    }

    long puzzle2(List<String> values) {
        def index = 0
        def acc = 0
        values.eachWithIndex { value, i ->
            def newList = values.collect()
            if (!value.contains("acc")) {
                if (value.contains("jmp")) {
                    newList.set(i, value.replace("jmp", "nop"))
                } else if (value.contains("nop")) {
                    newList.set(i, value.replace("nop", "jmp"))
                }
                def (idx, a) = processList(newList)
                if (idx == values.size()) {
                    acc = a
                }
            }
        }
        acc
    }

    def processList(List<String> values) {
        def indexVisited = []
        def index = 0
        def acc = 0
        def (nextIndex, newAcc) = nextIndexAndAccumulator(values, index, acc)
        indexVisited += index
        while (!indexVisited.contains(nextIndex)) {
            acc = newAcc
            index = nextIndex
            indexVisited += index
            if (index >= values.size()) {
                break
            }
            (nextIndex, newAcc) = nextIndexAndAccumulator(values, index, acc)
        }
        [index, acc]
    }

    def nextIndexAndAccumulator(List<String> values, int indexToParse, int currentAcc) {
        def matcher = values.get(indexToParse) =~ pattern
        matcher.matches()
        def instruction = matcher.group("ins")
        if (instruction == "nop") {
            return [indexToParse+1,currentAcc]
        }
        def sign = matcher.group("sign")
        def value = matcher.group("value") as int
        if (instruction == "acc") {
            int newAcc = sign == "+" ? currentAcc + value : currentAcc - value
            return [indexToParse+1,newAcc]
        }
        if (instruction == "jmp") {
            int nextIndex = sign == "+" ? indexToParse + value : indexToParse - value
            return [nextIndex,currentAcc]
        }
    }

}
