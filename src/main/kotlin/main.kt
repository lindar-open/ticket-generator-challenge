import kotlin.system.measureTimeMillis

class Strip {
    companion object {
        var allAttempts = 0
        var successfulAttempts = 0
    }

    private var numberList: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    init {
        while (true) {
            ++allAttempts
            try {
                numberList.clear()
                initStrip()
                ++successfulAttempts
                break
            } catch (_: Exception) {
            }
        }
    }

    private fun initStrip() {
        val emptyPositionList = mutableListOf<Pair<Int, Int>>()

        val positionPool = List(162) {
            val row = it / 9
            val col = it % 9
            Pair(row, col)
        }.toMutableSet()
        val cntPerRow = List(18) {0}.toMutableList()
        val columns = List(9) { mutableSetOf<Int>() }.toMutableList()
        val ticketColumns = List(6) {List(9) {0}.toMutableList() }

        while (positionPool.isNotEmpty()) {
            if (emptyPositionList.size == 72)
                break

            val position = positionPool.random()
            positionPool.remove(position)

            if (cntPerRow[position.first] >= 4)
                continue

            var maxSize = 8
            if (position.second == 0)
                maxSize = 9
            if (position.second == 8)
                maxSize = 7
            if (columns[position.second].size >= maxSize)
                continue

            if (ticketColumns[position.first / 3][position.second] == 2)
                continue

            emptyPositionList.add(position)
            cntPerRow[position.first]++
            columns[position.second].add(position.first)
            ticketColumns[position.first / 3][position.second]++
        }

        if (emptyPositionList.size != 72)
            throw Exception()

        for (i in 0 .. 8) {
            var permSize = 10
            if (i == 0)
                permSize = 9
            if (i == 8)
                permSize = 11
            val permutation = List(permSize) {it + i * 10 + if (i == 0) 1 else 0}.shuffled()
            val tempValues = mutableListOf<Int>()
            val tempIndices = mutableListOf<Int>()
            var previousJ = 0
            var permutationIndex = 0

            for (j in 0 .. 17) {
                if (j !in columns[i]) {
                    if (j/3 != previousJ/3) {
                        tempValues.sort()
                        for ((index, value) in tempIndices.withIndex())
                            numberList[Pair(value, i)] = tempValues[index]

                        tempValues.clear()
                        tempIndices.clear()
                    }

                    tempValues.add(permutation[permutationIndex++])
                    tempIndices.add(j)

                    previousJ = j
                }
            }

            tempValues.sort()
            for ((index, value) in tempIndices.withIndex())
                numberList[Pair(value, i)] = tempValues[index]
        }
    }

    override fun toString(): String {
        var str = ""

        val numbers = List(18) {List(9) {0}.toMutableList() }
        for (i in 0 .. 17)
            for (j in 0 .. 8)
                if (Pair(i, j) in numberList)
                    numbers[i][j] = numberList[Pair(i, j)]!!

        for (i in 0 .. 17) {
            for (j in 0 .. 8) {
                str += if (numbers[i][j] == 0)
                    "-  "
                else numbers[i][j].toString().padEnd(3, ' ')
            }
            str += "\n"
            if (i % 3 == 2)
                str += "\n"
        }

        return str
    }
}

fun main() {
    val millis = measureTimeMillis {
        for (i in 0 until 10000)
            Strip()
    }

    println(Strip.allAttempts)
    println(Strip.successfulAttempts)

    println(millis)
}