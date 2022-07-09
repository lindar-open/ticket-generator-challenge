package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TicketValueComparatorTest {

    private val ticketValueComparator = TicketValueComparator()

    @Test
    fun testComparator() {
        val input = intArrayOf(33, 31, -1)
        val expected = intArrayOf(31, 33, -1)

        val result = input.sortedWith(ticketValueComparator)

        println(input.toList())
        assertArrayEquals(expected, result.toIntArray())
    }

}