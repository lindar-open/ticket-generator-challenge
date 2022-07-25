import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.system.measureTimeMillis

class Strip {
    companion object {
        var allAttempts = 0
        var successfulAttempts = 0
    }

    // will store the mapping between the position in the strip matrix (18 x 9) and the value in that cell
    private var numberList: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    // holds the possible cells that could be empty
    private var positionPool = List(162) {
        val row = it / 9
        val col = it % 9
        Pair(row, col)
    }.shuffled()

    // holds the number of empty cells on each row
    private var cntPerRow = List(18) {0}.toMutableList()

    // holds the list of row indexes for the empty cells in each column
    private var columns = List(9) { mutableSetOf<Int>() }.toMutableList()

    // holds the number of empty cells in each column of 3 cells within each ticket in the strip
    private var ticketColumns = List(6) {List(9) {0}.toMutableList() }

    // counts the number of cells in the matrix that were decided to not contain numbers
    // will be 72 when the algorithm ends
    private var emptyPositionsTaken = 0

    private fun resetPrivateFields() {
        numberList.clear()
        positionPool = List(162) {
            val row = it / 9
            val col = it % 9
            Pair(row, col)
        }.shuffled()
        numberList = mutableMapOf()
        cntPerRow = List(18){0}.toMutableList()
        columns = List(9) { mutableSetOf<Int>() }.toMutableList()
        ticketColumns = List(6) {List(9) {0}.toMutableList() }
        emptyPositionsTaken = 0
    }

    init {
        // will try the random algorithm until successful
        while (true) {
            ++allAttempts
            try {
                resetPrivateFields()
                initStrip()
                ++successfulAttempts
                break
            } catch (_: Exception) {
            }
        }
    }

    // initStrip will randomly assign cells in the matrix that are empty, the rest will contain numbers
    private fun initStrip() {
        // if unable to fulfil the requirements throw exception
        if (!buildEmptyPositions())
            throw Exception()
        assignNumbers()
    }

    // returns false if unable to come up with a solution
    private fun buildEmptyPositions(): Boolean {
        for (position in positionPool) {
            if (emptyPositionsTaken == 72)
                break

            tryEmptyPosition(position)
        }

        // there should be 72 empty cells in the matrix such that 90 will be filled with numbers
        return emptyPositionsTaken == 72
    }

    private fun tryEmptyPosition(position: Pair<Int, Int>): Boolean {
        // a row must have 4 empty cells, no more
        if (cntPerRow[position.first] >= 4)
            return false

        val columnEmptyCellLimit = when (position.second) {
            0 -> 9 // the first column in the strip has 9 empty cells
            8 -> 7 // the last column in the strip has 7 empty cells
            else -> 8 // all the other columns have 8 empty cells each
        }

        if (columns[position.second].size >= columnEmptyCellLimit)
            return false

        // the ticket to which the cell belongs is determined by dividing position.first by 3
        if (ticketColumns[position.first / 3][position.second] == 2)
            return false

        // no criteria is being violated, an empty cell is decided
        ++emptyPositionsTaken
        cntPerRow[position.first]++
        columns[position.second].add(position.first)
        ticketColumns[position.first / 3][position.second]++

        return true
    }

    // this function is called after the empty cells have been assigned, then a solution is guaranteed to exist
    // and we have to assign numbers to non-empty cells
    private fun assignNumbers() {
        // solve each row at a time
        for (row in 0 until 9) {
            // the number of non-empty cells on the current row
            val permSize = when(row) {
                0 -> 9
                8 -> 11
                else -> 10
            }

            // permute the numbers assigned to this row (e.g. 20 through 29 on the third row)
            val permutation = List(permSize) {
                it + row * 10 + if (row == 0) 1 else 0
            }.shuffled()

            // the sequence below sorts the numbers on the non-empty cells within the same ticket, without
            // altering the relative order between elements on different strips
            val tempValues = mutableListOf<Int>()
            val tempIndices = mutableListOf<Int>()
            var previousColumn = 0
            var permutationIndex = 0

            for (column in 0 until 18) {
                if (column !in columns[row]) {
                    // the ticket has changed, so sort elements found on the previous ticket and add them to the numberList
                    if (column/3 != previousColumn/3) {
                        tempValues.sort()
                        for ((index, value) in tempIndices.withIndex())
                            numberList[Pair(value, row)] = tempValues[index]

                        tempValues.clear()
                        tempIndices.clear()
                    }

                    tempValues.add(permutation[permutationIndex++])
                    tempIndices.add(column)

                    previousColumn = column
                }
            }

            // the last ticket wasn't changed, so we have to manually cover it since it didn't activate
            // the condition in the if branch
            tempValues.sort()
            for ((index, value) in tempIndices.withIndex())
                numberList[Pair(value, row)] = tempValues[index]
        }
    }

    fun validate(): Boolean {
        if (numberList.keys.any {
            it.first < 0 || it.first >= 18 || it.second < 0 || it.second >= 9
        })
            return false
        if (numberList.values.toSet() != (1 .. 90).toSet())
            return false
        for (i in 0 until 18) {
            var cnt = 0
            for (j in 0 until 9)
                if (numberList.containsKey(Pair(i, j)))
                    ++cnt
            if (cnt != 5)
                return false
        }
        for (j in 0 until 9) {
            var cnt = 0
            var minimum = 100
            var maximum = 0
            for (i in 0 until 18)
                if (numberList.containsKey(Pair(i, j))) {
                    val value = numberList[Pair(i, j)]!!
                    ++cnt
                    minimum = min(minimum, value)
                    maximum = max(maximum, value)
                }
            if (j == 0 && cnt == 9 && minimum == 1 && maximum == 9)
                continue
            if (j == 8 && cnt == 11 && minimum == 80 && maximum == 90)
                continue
            if (cnt == 10 && minimum == j * 10 && maximum == j * 10 + 9)
                continue
            return false
        }
        for (j in 0 until 9)
            for (i in 0 until 6) {
                val i1 = i * 3
                val i2 = (i+1) * 3
                val numbersOnTicketColumn = (i1 until i2).mapNotNull { numberList[Pair(it, j)] }
                if (numbersOnTicketColumn != numbersOnTicketColumn.sorted())
                    return false
                if (numbersOnTicketColumn.isEmpty())
                    return false
            }
        return true
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
    val stripsToGenerate = 10000

    val millis = measureTimeMillis {
        for (i in 0 until stripsToGenerate) {
            val s = Strip()
            if (!s.validate()) {
                println(s.toString())
                return@measureTimeMillis
            }
        }
    }

    println("Total attempts made: ${Strip.allAttempts}")
    println("Successful attempts: ${Strip.successfulAttempts}")
    println("Time it took to generate $stripsToGenerate strips: $millis ms")
    println()
    println("An example of a strip")
    println(Strip())
}