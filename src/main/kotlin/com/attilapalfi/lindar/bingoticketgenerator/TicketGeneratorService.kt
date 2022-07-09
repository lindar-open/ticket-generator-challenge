package com.attilapalfi.lindar.bingoticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.numberprovider.NumberProvider
import com.attilapalfi.lindar.bingoticketgenerator.numberprovider.SixTickets
import org.springframework.stereotype.Service

@Service
class TicketGeneratorService(private val numberProvider: NumberProvider) {

    fun generateStripOfSix(): SixTickets {
        numberProvider.generateTickets()
        for (ticketIndex in 0 until 6) {

        }
        TODO()
    }
}
