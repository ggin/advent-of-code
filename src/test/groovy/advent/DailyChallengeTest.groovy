package advent

import spock.lang.Specification

abstract class DailyChallengeTest extends Specification {

    def input
    def inputPuzzle1
    def inputPuzzle2
    def expectedResultPuzzle1
    def expectedResultPuzzle2
    def classToTest

    def puzzle1() {
        given:
        def instance = classToTest.newInstance()
        def inputAsString = inputPuzzle1 ?: input

        expect:
            instance.puzzle1(inputAsString) == expectedResultPuzzle1
    }

    def puzzle2() {
        given:
        def instance = classToTest.newInstance()
        def inputAsString = inputPuzzle2 ?: input

        expect:
            instance.puzzle2(inputAsString) == expectedResultPuzzle2
    }

}
