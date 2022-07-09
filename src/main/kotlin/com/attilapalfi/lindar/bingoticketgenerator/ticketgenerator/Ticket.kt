package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

class Ticket {

    private val ticket: IntArray = IntArray(27)
    private val ticketValueComparator = TicketValueComparator()

    fun setValue(row: Int, column: Int, value: Int) {
        if (row == 0) {
            ticket[0 * 9 + column] = value
        } else if (row == 1) {
            val array = intArrayOf(getValue(0, column), value)
            val sorted = array.sortedWith(ticketValueComparator)
            ticket[0 * 9 + column] = sorted[0]
            ticket[1 * 9 + column] = sorted[1]
        } else if (row == 2) {
            val array = intArrayOf(getValue(0, column), getValue(1, column), value)
            val sorted = array.sortedWith(ticketValueComparator)
            ticket[0 * 9 + column] = sorted[0]
            ticket[1 * 9 + column] = sorted[1]
            ticket[2 * 9 + column] = sorted[2]
        }
    }

    fun getValue(row: Int, column: Int): Int {
        return ticket[row * 9 + column]
    }

    fun canBeBlankByColumn(row: Int, column: Int): Boolean {
        val otherRow1 = (row + 1) % 3
        val otherRow2 = (row + 2) % 3
        return getValue(otherRow1, column) != BLANK || getValue(otherRow2, column) != BLANK
    }

    fun getValues(): List<Int> {
        return arrayListOf(*ticket.toTypedArray())
    }

    fun getRow(row: Int): IntArray {
        return ticket.copyOfRange(row * 9, (row + 1) * 9)
    }

    fun getRowsOfTicket(): List<List<Int>> {
        val rows = ArrayList<ArrayList<Int>>()
        for (i in 0 until 27) {
            val rowIndex: Int = i / 9
            if (i % 9 == 0) {
                rows.add(ArrayList())
            }
            rows[rowIndex].add(ticket[i])
        }
        return rows
    }

    fun getColumnsOfTicket(): List<List<Int>> {
        val columns = ArrayList<ArrayList<Int>>()
        for (i in 0 until 9) {
            columns.add(ArrayList())
        }
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                columns[column].add(getValue(row, column))
            }
        }
        return columns
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                stringBuilder.append(getValue(row, column)).append(" ")
            }
            stringBuilder.append("\n")
        }
        stringBuilder.append("\n---------------------\n")
        return stringBuilder.toString()
    }
}