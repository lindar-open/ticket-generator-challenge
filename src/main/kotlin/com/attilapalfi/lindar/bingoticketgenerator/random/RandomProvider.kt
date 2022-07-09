package com.attilapalfi.lindar.bingoticketgenerator.random

interface RandomProvider {
    fun nextInt(until: Int): Int
}