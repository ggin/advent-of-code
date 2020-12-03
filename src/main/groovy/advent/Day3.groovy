package advent

class Day3 implements DailyChallenge {

    /*
    Right 1, down 1.
Right 3, down 1. (This is the slope you already checked.)
Right 5, down 1.
Right 7, down 1.
Right 1, down 2.
     */
    long puzzle1(List<String> values) {
        def m = values.collect { it.split('').collect { it == "." ? 0 : 1 } }
        nbTrees(m, 3, 1)
    }

    long nbTrees(def m, int right, int down) {
        def x = 0
        def y = 0
        def trees = 0
        while (y < m.size()) {
            if (m[y][x] == 1) {
                trees++
            }
            x = (x + right) % m[y].size()
            y += down
        }
        trees
    }

    long puzzle2(List<String> values) {
        def m = values.collect { it.split('').collect { it == "." ? 0 : 1 } }
        nbTrees(m, 1, 1) * nbTrees(m, 3, 1) * nbTrees(m, 5, 1) * nbTrees(m, 7, 1) * nbTrees(m, 1, 2)
    }

}
