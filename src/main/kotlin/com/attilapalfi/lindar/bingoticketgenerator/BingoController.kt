package com.attilapalfi.lindar.bingoticketgenerator

import com.attilapalfi.lindar.bingoticketgenerator.numberprovider.SixTickets
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/bingo")
class BingoController(
    private val ticketGenerator: TicketGeneratorService
) {

    @GetMapping("newStrips")
    fun generateNewStrips(): ResponseEntity<SixTickets> {
        return ResponseEntity.ok(ticketGenerator.generateStripOfSix())
    }
}