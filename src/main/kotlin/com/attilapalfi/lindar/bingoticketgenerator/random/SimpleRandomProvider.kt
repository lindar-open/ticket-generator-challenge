package com.attilapalfi.lindar.bingoticketgenerator.random

import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
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