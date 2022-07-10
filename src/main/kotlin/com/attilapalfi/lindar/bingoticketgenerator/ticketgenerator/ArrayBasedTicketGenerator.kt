package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.random.RandomProvider
import java.util.stream.IntStream

class ArrayBasedTicketGenerator(randomProvider: RandomProvider) : AbstractTicketGenerator(randomProvider) {
    private lateinit var numberArrays: ArrayList<ArrayList<Int>>
    private lateinit var isBlankArrays: ArrayList<ArrayList<ArrayList<Boolean>>>

    override fun doInit() {
        numberArrays = copyNumbersAsArrayList()
        isBlankArrays = ArrayList(TICKETS)
        IntStream.range(0, TICKETS).forEach {
            isBlankArrays.add(copyBlanksAsArrayList())
        }
    }

    override fun nextNumber(ticket: Int, row: Int, column: Int): Int {
        if (numberArrays[column].isEmpty()) {
            // this should not happen
            sixTickets.invalid = true
            return BLANK
        }
        decrementRemainingNumbersInRow(ticket, row)
        val randomIndex = randomProvider.nextInt(numberArrays[column].size)
        return numberArrays[column].removeAt(randomIndex)
    }

    override fun mustBeNumber(ticket: Int, row: Int, column: Int, columnIterationsLeft: Int): Boolean {
        if (getRemainingNumbersInRow(ticket, row) == columnIterationsLeft + 1) {
            return true
        }
        val remainingTotalRows = remainingTotalRows(ticket, row)
        return remainingTotalRows == numberArrays[column].size
    }

    override fun mustBeBlank(
        ticket: Int,
        row: Int,
        column: Int,
        columnIterator: Int,
        columnIndices: List<Int>
    ): Boolean {
        val mustBeBlankByRow =
            remainingBlanksInRow(ticket, row) == maxPossibleBlanksInRow(ticket, row, columnIterator, columnIndices)
        val mustBeBlankByColumn = remainingBlanksInColumn[column]!! == maxPossibleBlanksInColumn(ticket, row, column)

        if (mustBeBlankByRow || mustBeBlankByColumn) {
            remainingBlanksInColumn[column] = remainingBlanksInColumn[column]!! - 1
            isBlankArrays[ticket][row].removeAt(0)
            return true
        }

        return false
    }

    override fun canBeBlank(ticket: Int, row: Int, column: Int): Boolean {
        val canBeBlankByColumn = sixTickets.tickets[ticket].canBeBlankByColumn(row, column)
        val canBeBlankByRow = isBlankArrays[ticket][row][0]
        return canBeBlankByColumn && canBeBlankByRow && remainingBlanksInColumn[column]!! > 0
    }

    override fun isBlank(ticket: Int, row: Int, column: Int): Boolean {
        val randomIndex = randomProvider.nextInt(isBlankArrays[ticket][row].size)
        val isBlank = isBlankArrays[ticket][row].removeAt(randomIndex)
        if (isBlank) {
            remainingBlanksInColumn[column] = remainingBlanksInColumn[column]!! - 1
        }
        return isBlank
    }

    private fun remainingBlanksInRow(ticket: Int, row: Int) = isBlankArrays[ticket][row].count { it }

    private fun copyNumbersAsArrayList(): ArrayList<ArrayList<Int>> {
        val numberArrays = ArrayList<ArrayList<Int>>(numberArrayStorage.size)
        numberArrayStorage.forEach {
            numberArrays.add(arrayListOf(*it.copyOf().toTypedArray()))
        }
        return numberArrays
    }

    private fun copyBlanksAsArrayList(): ArrayList<ArrayList<Boolean>> {
        val isBlankArrays = ArrayList<ArrayList<Boolean>>(isBlankArrayStorage.size)
        isBlankArrayStorage.forEach {
            isBlankArrays.add(arrayListOf(*it.copyOf().toTypedArray()))
        }
        return isBlankArrays
    }

    companion object {
        private val numberArrayStorage: Array<IntArray> = Array(COLUMNS) { arrayIndex ->
            if (arrayIndex == 0) {
                val array = IntArray(NUMBERS_IN_FIRST_COLUMN)
                for (i in 1..array.size) {
                    array[i - 1] = i
                }
                array
            } else if (arrayIndex < 8) {
                val array = IntArray(NUMBERS_IN_MIDDLE_COLUMNS)
                for (i in array.indices) {
                    array[i] = 10 * arrayIndex + i
                }
                array
            } else {
                val array = IntArray(NUMBERS_IN_LAST_COLUMN)
                for (i in array.indices) {
                    array[i] = 10 * arrayIndex + i
                }
                array
            }
        }

        private val isBlankArrayStorage: Array<BooleanArray> = Array(3) {
            val array = BooleanArray(COLUMNS)
            for (i in array.indices) {
                array[i] = i < BLANKS_IN_ROW
            }
            array
        }
    }

}