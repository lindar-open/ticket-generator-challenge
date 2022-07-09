package com.attilapalfi.lindar.bingoticketgenerator.random

import org.springframework.stereotype.Component

@Component
class DeterministicRandomProvider : RandomProvider {
    override fun nextInt(until: Int): Int {
        return 0
    }
}