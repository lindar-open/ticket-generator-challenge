package com.attilapalfi.lindar.bingoticketgenerator.numberprovider

interface NumberProvider {

    fun nextNumber(ticket: Int, row: Int, column: Int): Int

    fun mustBeNumber(ticket: Int, row: Int, column: Int): Boolean

    fun mustBeBlank(ticket: Int, row: Int, column: Int): Boolean

    fun canBeBlank(ticket: Int, row: Int, column: Int): Boolean

    fun isBlank(ticket: Int, row: Int, column: Int): Boolean

    fun generateTickets(): SixTickets
}