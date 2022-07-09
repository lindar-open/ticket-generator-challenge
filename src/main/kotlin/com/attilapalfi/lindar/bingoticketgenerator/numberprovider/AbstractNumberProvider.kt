package com.attilapalfi.lindar.bingoticketgenerator.numberprovider

import com.attilapalfi.lindar.bingoticketgenerator.random.RandomProvider

const val BLANK = -1

abstract class AbstractNumberProvider(protected val randomProvider: RandomProvider) : NumberProvider {
    protected lateinit var sixTickets: SixTickets

    override fun generateTickets(): SixTickets {
        initNewStripOfSix()
        var columnStart = 6
        for (ticket in 0 until 6) {
            for (row in 0 until 3) {
                columnStart += 3
                columnStart %= 9
                var columnIterations = 0
                while (columnIterations < 9) {
                    val column = (columnStart + columnIterations) % 9
                    if (mustBeNumber(ticket, row, column)) {
                        addNumber(ticket, row, column)
                    } else if (mustBeBlank(ticket, row, column)) {
                        addBlank(ticket, row, column)
                    } else if (canBeBlank(ticket, row, column)) {
                        if (isBlank(ticket, row, column)) {
                            addBlank(ticket, row, column)
                        } else {
                            addNumber(ticket, row, column)
                        }
                    } else {
                        addNumber(ticket, row, column)
                    }
                    columnIterations++
                }
            }
        }
        return sixTickets
    }

    private fun addNumber(ticket: Int, row: Int, column: Int) {
        val number = nextNumber(ticket, row, column)
        sixTickets.tickets[ticket].setValue(row, column, number)
    }

    private fun addBlank(ticket: Int, row: Int, column: Int) {
        sixTickets.tickets[ticket].setValue(row, column, BLANK)
    }

    private fun initNewStripOfSix() {
        sixTickets = SixTickets()
        doInit()
    }

    protected fun remainingTotalRows(ticket: Int, row: Int): Int {
        return ((6 - (ticket + 1)) * 3) + (3 - row)
    }

    protected abstract fun doInit()
}