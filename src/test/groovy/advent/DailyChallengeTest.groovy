package advent

import spock.lang.Specification

abstract class DailyChallengeTest extends Specification {

    def input
    def expectedResultPuzzle1
    def expectedResultPuzzle2
    def classToTest

    def puzzle1() {
        given:
        def instance = classToTest.newInstance()
        def inputAsString = toString(input)

        expect:
        instance.puzzle1(inputAsString) == expectedResultPuzzle1
    }

    def puzzle2() {
        given:
        def instance = classToTest.newInstance()
        def inputAsString = toString(input)

        expect:
        instance.puzzle2(inputAsString) == expectedResultPuzzle2
    }

    static def toString(l) { l.collect { it -> it.toString() } }

}
