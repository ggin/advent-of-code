package advent

class Day9 implements DailyChallenge {

    def preambleLength = 25

    long puzzle1(List<String> values) {
        def numbers = toLongValues(values) as List<Long>
        def index = preambleLength
        while (isSumOfPreambleNumbers(numbers, index)) {
            index++
        }
        numbers.get(index)
    }

    long puzzle2(List<String> values) {
        def numbers = toLongValues(values) as List<Long>
        def index = preambleLength
        while (isSumOfPreambleNumbers(numbers, index)) {
            index++
        }
        def invalidNumber = numbers.get(index)
        sumOfContiguousNumbers(numbers, invalidNumber, 0)
    }

    boolean isSumOfPreambleNumbers(List<Integer> numbers, int index) {
        def numberToTest = numbers[index]
        def preambleNumbers = numbers.subList(index-preambleLength, index)
        [preambleNumbers, preambleNumbers].combinations()
            .findAll { a, b -> a  < b}
            .collect { a, b -> a + b }
            .any { s -> numberToTest == s }
    }

    long sumOfContiguousNumbers(List<Integer> numbers, long sum, int startIdx) {
        def endIdx = startIdx + 2
        long runningSum = numbers[startIdx] + numbers[startIdx + 1]
        while (runningSum < sum && endIdx < numbers.size()) {
            runningSum += numbers[endIdx]
            endIdx++
        }
        if (runningSum == sum) {
            def min = numbers.subList(startIdx, endIdx - 1).min()
            def max = numbers.subList(startIdx, endIdx - 1).max()
            return min + max
        } else {
            return sumOfContiguousNumbers(numbers, sum, startIdx + 1)
        }

    }
}
