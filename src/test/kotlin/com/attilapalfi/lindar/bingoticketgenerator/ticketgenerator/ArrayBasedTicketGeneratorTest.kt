package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.random.DeterministicRandomProvider
import com.attilapalfi.lindar.bingoticketgenerator.random.SimpleRandomProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class ArrayBasedTicketGeneratorTest {

    private lateinit var underTest: ArrayBasedTicketGenerator

    @Test
    fun `run tests 10 times`() {
        for (i in 1 .. 10) {
            `test with deterministic random provider`()
            `test with simple random provider`()
        }
    }

    @Test
    fun `test with deterministic random provider`() {
        underTest = ArrayBasedTicketGenerator(DeterministicRandomProvider())

        val sixTickets = underTest.generateTickets()
        println(sixTickets)

        validateSixTickets(sixTickets)
    }

    @Test
    fun `test with simple random provider`() {
        underTest = ArrayBasedTicketGenerator(SimpleRandomProvider())

        val sixTickets = underTest.generateTickets()
        println(sixTickets)

        validateSixTickets(sixTickets)
    }

    private fun validateSixTickets(sixTickets: SixTickets) {
        val numberSet = HashSet<Int>(90)
        for (i in 1..90) {
            numberSet.add(i)
        }
        var iterator = 0
        sixTickets.tickets.forEachIndexed { ticketIndex, ticket ->
            validateRowRules(ticketIndex, ticket)
            validateColumnRules(ticketIndex, ticket)
            ticket.getValues().forEach { value ->
                val ticketValueIndex = iterator - (ticketIndex * 27)
                val rowInTicket = ticketValueIndex / 9
                val columnInTicket = ticketValueIndex - (rowInTicket * 9)

                if (value != BLANK) {
                    validateValueIsNotDuplicated(value, numberSet)
                    validateValueIsInCorrectColumn(value, columnInTicket)
                }
                iterator++
            }
        }
    }

    private fun validateRowRules(ticketIndex: Int, ticket: Ticket) {
        val rows = ticket.getRowsOfTicket()
        assertEquals(3, rows.size)
        rows.forEachIndexed { rowIndex, row ->
            val numbersInRow = row.count { it > 0 }
            val blanksInRow = row.count { it < 0 }
            assertEquals(
                5,
                numbersInRow,
                "Row '$rowIndex' in ticket '$ticketIndex' has invalid amount of numbers: '$numbersInRow'"
            )
            assertEquals(
                4,
                blanksInRow,
                "Row '$rowIndex' in ticket '$ticketIndex' has invalid amount of blanks: '$blanksInRow'"
            )
        }
    }

    private fun validateColumnRules(ticketIndex: Int, ticket: Ticket) {
        val columns = ticket.getColumnsOfTicket()
        assertEquals(9, columns.size)
        columns.forEachIndexed { columnIndex, column ->
            val blanksInColumn = column.count { it < 0 }
            assertTrue(
                blanksInColumn < 3,
                "Invalid number of blanks in column '$columnIndex' of ticket '$ticketIndex': '$blanksInColumn'"
            )

            val sortedColumn = column.toTypedArray()
            sortedColumn.sortWith(TicketValueComparator())

            assertArrayEquals(
                sortedColumn,
                column.toTypedArray(),
                "Values in column '$columnIndex' in ticket '$ticketIndex' are not in ascending order"
            )
        }
    }

    private fun validateValueIsNotDuplicated(value: Int, numberSet: HashSet<Int>) {
        if (!numberSet.contains(value)) {
            fail("Number '$value' was duplicated in the tickets.")
        } else {
            numberSet.remove(value)
        }
    }

    private fun validateValueIsInCorrectColumn(value: Int, columnInTicket: Int) {
        if (value == 90) {
            assertEquals(8, columnInTicket)
        } else {
            if (value < columnInTicket * 10 || value >= columnInTicket * 10 + 10) {
                fail("Invalid value in column '$columnInTicket': '$value'")
            }
        }
    }
}