package com.attilapalfi.lindar.bingoticketgenerator.numberprovider

import com.attilapalfi.lindar.bingoticketgenerator.random.DeterministicRandomProvider
import com.attilapalfi.lindar.bingoticketgenerator.random.SimpleRandomProvider
import org.junit.jupiter.api.Test

class ArrayBasedNumberProviderTest {

    private lateinit var underTest: ArrayBasedNumberProvider

    @Test
    fun `test with deterministic random provider`() {
        underTest = ArrayBasedNumberProvider(DeterministicRandomProvider())

        val sixTickets = underTest.generateTickets()

        println(sixTickets)
    }

    @Test
    fun `test with simple random provider`() {
        underTest = ArrayBasedNumberProvider(SimpleRandomProvider())

        val sixTickets = underTest.generateTickets()

        println(sixTickets)
    }
}