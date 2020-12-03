package advent

class Day3 implements DailyChallenge {

    long puzzle1(List<String> values) {
        nbTrees(values, 3, 1)
    }

    long puzzle2(List<String> values) {
        [[1, 1], [3, 1], [5, 1], [7, 1], [1, 2]].collect { r, d -> nbTrees(values, r, d)}.inject { x, y -> x * y}
    }

    long nbTrees(List<String> v, int right, int down) {
        (0..<v.size() / down).count { v[it*down][it*right % v[0].size()] == '#'}
    }

}
