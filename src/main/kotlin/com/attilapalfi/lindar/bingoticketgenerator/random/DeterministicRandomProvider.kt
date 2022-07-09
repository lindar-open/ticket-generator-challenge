package com.attilapalfi.lindar.bingoticketgenerator.random

class DeterministicRandomProvider : RandomProvider {
    override fun nextInt(until: Int): Int {
        return 0
    }
}