package com.example.calculator4.calculations

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Task 3: Визначити струми КЗ для підстанції ХПнЕМ
 * (Determine short-circuit currents for KhPNEM substation)
 * Based on Example 7.4
 */
data class Task3Input(
    // Параметри трансформатора
    val transformerNominalPower: Double = 6.3, // МВА
    val highVoltage: Double = 115.0, // кВ
    val lowVoltage: Double = 11.0, // кВ
    val transformerShortCircuitVoltage: Double = 11.1, // %
    
    // Нормальний режим
    val normalResistance: Double = 10.65, // Ом
    val normalReactance: Double = 24.02, // Ом
    
    // Мінімальний режим
    val minResistance: Double = 34.88, // Ом
    val minReactance: Double = 65.68 // Ом
)

data class Task3ModeResult(
    val resistance: Double, // Ом
    val reactance: Double, // Ом
    val impedance: Double, // Ом
    val threePhaseCurrent110kV: Double, // A (приведений до 110 кВ)
    val twoPhaseCurrent110kV: Double, // A (приведений до 110 кВ)
    val resistance10kV: Double, // Ом (на шинах 10 кВ)
    val reactance10kV: Double, // Ом (на шинах 10 кВ)
    val impedance10kV: Double, // Ом (на шинах 10 кВ)
    val threePhaseCurrent10kV: Double, // A (на шинах 10 кВ)
    val twoPhaseCurrent10kV: Double // A (на шинах 10 кВ)
)

data class Task3Result(
    val transformerReactance: Double, // Ом
    val normalMode: Task3ModeResult,
    val minMode: Task3ModeResult
)

object Task3Calculator {
    fun calculate(input: Task3Input): Task3Result {
        // Опір трансформатора
        val transformerReactance = (input.transformerShortCircuitVoltage / 100.0) * 
            (input.highVoltage.pow(2.0) / input.transformerNominalPower)
        
        // Коефіцієнт приведення
        val conversionCoefficient = input.lowVoltage.pow(2.0) / input.highVoltage.pow(2.0)
        
        // Нормальний режим
        val normalMode = calculateMode(
            resistance = input.normalResistance,
            reactance = input.normalReactance + transformerReactance,
            highVoltage = input.highVoltage,
            lowVoltage = input.lowVoltage,
            conversionCoefficient = conversionCoefficient
        )
        
        // Мінімальний режим
        val minMode = calculateMode(
            resistance = input.minResistance,
            reactance = input.minReactance + transformerReactance,
            highVoltage = input.highVoltage,
            lowVoltage = input.lowVoltage,
            conversionCoefficient = conversionCoefficient
        )
        
        return Task3Result(
            transformerReactance = transformerReactance,
            normalMode = normalMode,
            minMode = minMode
        )
    }
    
    private fun calculateMode(
        resistance: Double,
        reactance: Double,
        highVoltage: Double,
        lowVoltage: Double,
        conversionCoefficient: Double
    ): Task3ModeResult {
        // Імпеданс на рівні 110 кВ
        val impedance = sqrt(resistance.pow(2.0) + reactance.pow(2.0))
        
        // Струми на рівні 110 кВ
        val threePhaseCurrent110kV = (highVoltage * 1000) / (sqrt(3.0) * impedance)
        val twoPhaseCurrent110kV = threePhaseCurrent110kV * sqrt(3.0) / 2.0
        
        // Опори на шинах 10 кВ
        val resistance10kV = resistance * conversionCoefficient
        val reactance10kV = reactance * conversionCoefficient
        val impedance10kV = sqrt(resistance10kV.pow(2.0) + reactance10kV.pow(2.0))
        
        // Струми на шинах 10 кВ
        val threePhaseCurrent10kV = (lowVoltage * 1000) / (sqrt(3.0) * impedance10kV)
        val twoPhaseCurrent10kV = threePhaseCurrent10kV * sqrt(3.0) / 2.0
        
        return Task3ModeResult(
            resistance = resistance,
            reactance = reactance,
            impedance = impedance,
            threePhaseCurrent110kV = threePhaseCurrent110kV,
            twoPhaseCurrent110kV = twoPhaseCurrent110kV,
            resistance10kV = resistance10kV,
            reactance10kV = reactance10kV,
            impedance10kV = impedance10kV,
            threePhaseCurrent10kV = threePhaseCurrent10kV,
            twoPhaseCurrent10kV = twoPhaseCurrent10kV
        )
    }
}

