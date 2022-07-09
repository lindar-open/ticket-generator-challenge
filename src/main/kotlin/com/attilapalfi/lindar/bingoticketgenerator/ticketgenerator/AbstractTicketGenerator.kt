package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.random.RandomProvider
import kotlin.math.min

const val BLANK = -1

sealed class AbstractTicketGenerator(protected val randomProvider: RandomProvider) : TicketGenerator {
    protected lateinit var sixTickets: SixTickets
    protected lateinit var remainingBlanksInColumn: MutableMap<Int, Int>
    private lateinit var remainingNumbersInStripRow: MutableMap<Int, Int>

    override fun generateTickets(): SixTickets {
        initNewStripOfSix()
        var columnStart = 6
        for (ticket in 0 until 6) {
            for (row in 0 until 3) {
                columnStart += 3
                columnStart %= 9

                var columnIterationsLeft = 8
                var columnIterations = 0
                while (columnIterations < 9) {
                    val column = (columnStart + columnIterations) % 9
                    if (canBeBlank(ticket, row, column) && mustBeBlank(ticket, row, column, columnIterationsLeft)) {
                        addBlank(ticket, row, column)
                    } else if (mustBeNumber(ticket, row, column, columnIterationsLeft)) {
                        addNumber(ticket, row, column)
                    } else {
                        if (isBlank(ticket, row, column)) {
                            addBlank(ticket, row, column)
                        } else {
                            addNumber(ticket, row, column)
                        }
                    }
                    columnIterations++
                    columnIterationsLeft--
                }
            }
        }
        fixInvalidSpaces()
        return sixTickets
    }

    abstract fun fixInvalidSpaces()

    protected abstract fun doInit()

    protected abstract fun nextNumber(ticket: Int, row: Int, column: Int): Int

    protected abstract fun mustBeNumber(ticket: Int, row: Int, column: Int, columnIterationsLeft: Int): Boolean

    protected abstract fun mustBeBlank(ticket: Int, row: Int, column: Int, columnIterationsLeft: Int): Boolean

    protected abstract fun canBeBlank(ticket: Int, row: Int, column: Int): Boolean

    protected abstract fun isBlank(ticket: Int, row: Int, column: Int): Boolean

    protected fun remainingTotalRows(ticket: Int, row: Int): Int {
        return ((6 - (ticket + 1)) * 3) + (3 - row)
    }

    protected fun getRemainingNumbersInStripRow(ticket: Int, row: Int): Int {
        return remainingNumbersInStripRow[ticket * 3 + row]!!
    }

    protected fun decrementRemainingNumbersInStripRow(ticket: Int, row: Int) {
        remainingNumbersInStripRow[ticket * 3 + row] = remainingNumbersInStripRow[ticket * 3 + row]!! - 1
    }

    protected fun maxPossibleBlanksInColumn(ticket: Int, row: Int, column: Int): Int {
        val remainingFullTickets = 6 - (ticket + 1)
        var possibleBlanks = remainingFullTickets * 2
        var possibleBlanksInTicketColumn = 0
        for (i in row until 3) {
            if (sixTickets.tickets[ticket].canBeBlankByColumn(i, column)) {
                possibleBlanksInTicketColumn++
            }
        }
        possibleBlanks += min(possibleBlanksInTicketColumn, 2)
        return possibleBlanks
    }

    protected fun maxPossibleBlanksInRow(ticket: Int, row: Int, column: Int, columnIterationsLeft: Int): Int {
        var columnIndex = column
        var columnIterator = 0
        var possibleBlanks = 0
        while (columnIterator <= columnIterationsLeft) {
            if (canBeBlank(ticket, row, columnIndex)) {
                possibleBlanks++
            }
            columnIndex = (columnIndex + 1) % 9
            columnIterator++
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
        remainingNumbersInStripRow = HashMap()
        for (i in 0 until 18) {
            remainingNumbersInStripRow[i] = 5
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