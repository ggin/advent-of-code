package day

import common.DailyChallenge
import common.Input

class Day2Compact : DailyChallenge {

    override fun dayNumber() = 2

    override fun puzzle1(input: Input) = input.toPairs { s -> (s[0] - '@') % 23 } // A=X=1, B=Y=2, C=Z=3
        .sumOf { (p1, p2) -> p2 + ((p2 - p1 + 1).mod(3) * 3L) }

    override fun puzzle2(input: Input) = input.toPairs { s -> (s[0] - 'A') % 23 } // A=X=0, B=Y=1, C=Z=2
        .sumOf { (p1, outcome) -> (p1 + outcome - 1).mod(3) + 1 + outcome * 3L }

}
