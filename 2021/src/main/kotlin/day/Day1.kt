package day

import common.DailyChallenge

class Day1 : DailyChallenge {
    /*
      def static MAGIC_NUMBER = 2020

    long puzzle1(List<String> values) {
        def complements = new HashSet()
        long n = toIntValues(values).stream()
                .filter {
                    complements.add(MAGIC_NUMBER - it)
                    return complements.remove(it)
                }
                .findFirst().get()
        return n * (MAGIC_NUMBER - n)
    }

    long puzzle2(List<String> values) {
        def intValues = toIntValues(values)
        def complements = [intValues , intValues ].combinations()
                .findAll { a, b -> a < b && a + b < MAGIC_NUMBER }
                .collectEntries { a, b -> [(MAGIC_NUMBER - a - b): [a, b]] }

        def n = intValues .find { complements.containsKey(it) }
        return [n, *complements[n]].inject { a, b -> a * b } as long
    }
     */
    companion object {
        val MAGIC_NUMBER = 2020L
    }

    override fun puzzle1(values: List<String>): Long {
        val input = toLongValues(values)
        return calculateProduct(input, MAGIC_NUMBER)
    }

    private fun calculateProduct(input: List<Long>, magicNumber: Long): Long {
        val n = findComplement(input, magicNumber)!!
        return n * (magicNumber - n)
    }

    private fun findComplement(input: List<Long>, magicNumber: Long): Long? {
        val set = HashSet<Long>()
        return input.find {
            if (set.contains(it)) true else {
                set.add(magicNumber - it)
                false
            }
        }
    }

    override fun puzzle2(values: List<String>): Long {
        val input = toLongValues(values)
        return input.asSequence().map {
            val complement = findComplement(input.minus(it), MAGIC_NUMBER - it) ?: -1
            complement * it * (MAGIC_NUMBER - it - complement)
        }.filter { it > 0 }
            .first()
    }

}
