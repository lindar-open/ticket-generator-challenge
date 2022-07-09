package com.attilapalfi.lindar.bingoticketgenerator.web

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("bingo")
class BingoController(
    private val ticketGenerator: TicketGeneratorService
) {

    @GetMapping("newStrips", produces = ["text/formatted"])
    fun generateNewStrips(): ResponseEntity<String> {
        return ResponseEntity.ok(ticketGenerator.generateStripOfSix().toString())
    }
}