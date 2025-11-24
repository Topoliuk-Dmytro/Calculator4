package com.example.calculator4.calculations

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Task 2: Визначити струми КЗ на шинах 10 кВ ГПП
 * (Determine short-circuit currents on 10 kV GPP buses)
 * Based on Example 7.2
 */
data class Task2Input(
    val systemVoltage: Double = 10.5, // кВ (середня номінальна напруга)
    val shortCircuitPower: Double = 200.0, // МВА
    val transformerVoltage: Double = 10.5, // кВ
    val transformerNominalPower: Double = 6.3, // МВА
    val transformerShortCircuitVoltage: Double = 10.5 // % (Uк%)
)

data class Task2Result(
    val systemReactance: Double, // Ом
    val transformerReactance: Double, // Ом
    val totalReactance: Double, // Ом
    val threePhaseShortCircuitCurrent: Double, // кА
    val twoPhaseShortCircuitCurrent: Double // кА
)

object Task2Calculator {
    fun calculate(input: Task2Input): Task2Result {
        // Опір системи
        val systemReactance = input.systemVoltage.pow(2.0) / input.shortCircuitPower
        
        // Опір трансформатора
        val transformerReactance = (input.transformerShortCircuitVoltage / 100.0) * 
            (input.transformerVoltage.pow(2.0) / input.transformerNominalPower)
        
        // Сумарний опір
        val totalReactance = systemReactance + transformerReactance
        
        // Початкове діюче значення струму трифазного КЗ
        val threePhaseShortCircuitCurrent = input.systemVoltage / (sqrt(3.0) * totalReactance)
        
        // Струм двофазного КЗ
        val twoPhaseShortCircuitCurrent = threePhaseShortCircuitCurrent * sqrt(3.0) / 2.0
        
        return Task2Result(
            systemReactance = systemReactance,
            transformerReactance = transformerReactance,
            totalReactance = totalReactance,
            threePhaseShortCircuitCurrent = threePhaseShortCircuitCurrent,
            twoPhaseShortCircuitCurrent = twoPhaseShortCircuitCurrent
        )
    }
}

