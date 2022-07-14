package com.attilapalfi.lindar.bingoticketgenerator.ticketgenerator

sealed interface TicketGenerator {
    fun generateTickets(): SixTickets
}