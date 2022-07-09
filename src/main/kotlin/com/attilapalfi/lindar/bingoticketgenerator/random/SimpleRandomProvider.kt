package com.attilapalfi.lindar.bingoticketgenerator.random

import kotlin.random.Random

class SimpleRandomProvider : RandomProvider {

    private val random = Random(System.currentTimeMillis())

    override fun nextInt(until: Int): Int {
        return if (until == 0) {
            0
        } else {
            random.nextInt(until)
        }
    }
}