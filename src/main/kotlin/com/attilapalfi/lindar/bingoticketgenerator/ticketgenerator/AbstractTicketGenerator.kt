package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.random.RandomProvider
import kotlin.math.min

const val BLANK = -1
const val TICKETS = 6
const val ROWS = 3
const val NUMBERS_IN_ROW = 5
const val BLANKS_IN_ROW = 4
const val COLUMNS = NUMBERS_IN_ROW + BLANKS_IN_ROW
const val SPACES_IN_TICKET = COLUMNS * ROWS

const val NUMBERS_IN_FIRST_COLUMN = 9
const val NUMBERS_IN_MIDDLE_COLUMNS = 10
const val NUMBERS_IN_LAST_COLUMN = 11
const val TOTAL_ROWS_IN_STRIP_OF_SIX = 18

sealed class AbstractTicketGenerator(protected val randomProvider: RandomProvider) : TicketGenerator {
    protected lateinit var sixTickets: SixTickets
    protected lateinit var remainingBlanksInColumn: MutableMap<Int, Int>
    private lateinit var remainingNumbersInRow: MutableMap<Int, Int>


    override fun generateTickets(): SixTickets {
        initNewStripOfSix()
        loop@ for (ticket in 0 until TICKETS) {
            if (sixTickets.invalid) break@loop
            for (row in 0 until ROWS) {
                if (sixTickets.invalid) break@loop
                fillRow(ticket, row)
            }
        }
        return returnOrRegenerateIfInvalid()
    }

    private fun fillRow(ticket: Int, row: Int) {
        val mustBlankSpaces = HashSet<Int>()
        val mustNumberSpaces = HashSet<Int>()
        val columnIndices = Array(COLUMNS) { it }.toMutableList().shuffled()
        fillWithMandatoryBlanksAndNumbers(columnIndices, ticket, row, mustBlankSpaces, mustNumberSpaces)
        fillTheRestWithBlanksAndNumbers(columnIndices, mustBlankSpaces, mustNumberSpaces, ticket, row)
        setInvalidIfNeeded(ticket, row)
    }

    private fun fillWithMandatoryBlanksAndNumbers(
        columnIndices: List<Int>,
        ticket: Int,
        row: Int,
        mustBlankSpaces: HashSet<Int>,
        mustNumberSpaces: HashSet<Int>
    ) {
        var columnIterationsLeft = 8
        columnIndices.forEachIndexed { columnIterator, column ->
            if (canBeBlank(ticket, row, column) && mustBeBlank(
                    ticket,
                    row,
                    column,
                    columnIterator,
                    columnIndices
                )
            ) {
                mustBlankSpaces.add(column)
                addBlank(ticket, row, column)
            } else if (mustBeNumber(ticket, row, column, columnIterationsLeft)) {
                mustNumberSpaces.add(column)
                addNumber(ticket, row, column)
            }
            columnIterationsLeft--
        }
    }

    private fun fillTheRestWithBlanksAndNumbers(
        columnIndices: List<Int>,
        mustBlankSpaces: HashSet<Int>,
        mustNumberSpaces: HashSet<Int>,
        ticket: Int,
        row: Int
    ) {
        var columnIterationsLeft = 8
        columnIndices.forEach { column ->
            if (!mustBlankSpaces.contains(column) && !mustNumberSpaces.contains(column)) {
                if (canBeBlank(ticket, row, column) && isBlank(ticket, row, column)) {
                    addBlank(ticket, row, column)
                } else {
                    addNumber(ticket, row, column)
                }
            }
            columnIterationsLeft--
        }
    }

    private fun setInvalidIfNeeded(ticket: Int, row: Int) {
        if (isRowInvalidValid(ticket, row)) {
            sixTickets.invalid = true
        }
    }

    private fun isRowInvalidValid(ticket: Int, row: Int) =
        sixTickets.tickets[ticket].getRow(row).count { it > 0 } != NUMBERS_IN_ROW

    private fun returnOrRegenerateIfInvalid(): SixTickets {
        return if (sixTickets.invalid) {
            generateTickets()
        } else {
            sixTickets
        }
    }

    abstract fun mustBeBlank(
        ticket: Int,
        row: Int,
        column: Int,
        columnIterator: Int,
        columnIndices: List<Int>
    ): Boolean

    protected abstract fun doInit()

    protected abstract fun nextNumber(ticket: Int, row: Int, column: Int): Int

    protected abstract fun mustBeNumber(ticket: Int, row: Int, column: Int, columnIterationsLeft: Int): Boolean

    protected abstract fun canBeBlank(ticket: Int, row: Int, column: Int): Boolean

    protected abstract fun isBlank(ticket: Int, row: Int, column: Int): Boolean

    protected fun remainingTotalRows(ticket: Int, row: Int): Int {
        return ((TICKETS - (ticket + 1)) * ROWS) + (ROWS - row)
    }

    protected fun getRemainingNumbersInRow(ticket: Int, row: Int): Int {
        return remainingNumbersInRow[ticket * ROWS + row]!!
    }

    protected fun decrementRemainingNumbersInRow(ticket: Int, row: Int) {
        remainingNumbersInRow[ticket * ROWS + row] = remainingNumbersInRow[ticket * ROWS + row]!! - 1
    }

    protected fun maxPossibleBlanksInColumn(ticket: Int, row: Int, column: Int): Int {
        val remainingFullTickets = TICKETS - (ticket + 1)
        var possibleBlanks = remainingFullTickets * 2
        var possibleBlanksInTicketColumn = 0
        for (i in row until ROWS) {
            if (sixTickets.tickets[ticket].canBeBlankByColumn(i, column)) {
                possibleBlanksInTicketColumn++
            }
        }
        possibleBlanks += min(possibleBlanksInTicketColumn, 2)
        return possibleBlanks
    }

    protected fun maxPossibleBlanksInRow(
        ticket: Int,
        row: Int,
        columnIterator: Int,
        columnIndices: List<Int>
    ): Int {
        var possibleBlanks = 0
        for (i in columnIterator until columnIndices.size) {
            if (canBeBlank(ticket, row, columnIndices[i])) {
                possibleBlanks++
            }
        }
        return possibleBlanks
    }

    private fun initNewStripOfSix() {
        sixTickets = SixTickets()
        remainingBlanksInColumn = hashMapOf(
            Pair(0, 9),
            Pair(1, 8),
            Pair(2, 8),
            Pair(3, 8),
            Pair(4, 8),
            Pair(5, 8),
            Pair(6, 8),
            Pair(7, 8),
            Pair(8, 7),
        )
        remainingNumbersInRow = HashMap()
        for (i in 0 until TOTAL_ROWS_IN_STRIP_OF_SIX) {
            remainingNumbersInRow[i] = NUMBERS_IN_ROW
        }
        doInit()
    }

    private fun addNumber(ticket: Int, row: Int, column: Int) {
        val number = nextNumber(ticket, row, column)
        sixTickets.tickets[ticket].setValue(row, column, number)
    }

    private fun addBlank(ticket: Int, row: Int, column: Int) {
        sixTickets.tickets[ticket].setValue(row, column, BLANK)
    }
}