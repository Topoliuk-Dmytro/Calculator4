package com.example.calculator4.calculations

import kotlin.math.sqrt
import kotlin.math.max

/**
 * Task 1: Вибрати кабелі для живлення двотрансформаторної підстанції
 * (Select cables for powering two-transformer substation)
 * Based on Example 7.1
 */
data class Task1Input(
    val voltage: Double = 10.0, // кВ
    val shortCircuitCurrent: Double = 2.5, // кА
    val fictitiousTime: Double = 2.5, // секунди
    val transformerPower: Double = 2000.0, // кВА (2 × 1000)
    val calculatedLoad: Double = 1300.0, // кВА
    val operatingTime: Double = 4000.0, // години
    val economicCurrentDensity: Double = 1.4, // A/mm²
    val thermalConstant: Double = 92.0 // A·s^0.5/mm² для кабелів з алюмінієвими жилами
)

data class Task1Result(
    val normalCurrent: Double, // A
    val postEmergencyCurrent: Double, // A
    val economicCrossSection: Double, // mm²
    val minThermalCrossSection: Double, // mm²
    val recommendedCrossSection: Double, // mm²
    val selectedCable: String,
    val thermalStabilityCheck: Boolean
)

object Task1Calculator {
    fun calculate(input: Task1Input): Task1Result {
        // Розрахунковий струм для нормального режиму
        val normalCurrent = input.calculatedLoad / (sqrt(3.0) * input.voltage) * 1000 // A
        
        // Розрахунковий струм для післяаварійного режиму (подвоєний)
        val postEmergencyCurrent = 2 * normalCurrent
        
        // Економічний переріз
        val economicCrossSection = normalCurrent / input.economicCurrentDensity
        
        // Мінімальний переріз з умови термічної стійкості
        val minThermalCrossSection = (input.shortCircuitCurrent * 1000 * sqrt(input.fictitiousTime)) / input.thermalConstant
        
        // Рекомендований переріз (більший з двох)
        val recommendedCrossSection = max(economicCrossSection, minThermalCrossSection)
        
        // Вибір стандартного перерізу (25, 35, 50, 70, 95, 120, 150, 185, 240)
        val standardSections = listOf(25.0, 35.0, 50.0, 70.0, 95.0, 120.0, 150.0, 185.0, 240.0)
        val selectedSection = standardSections.firstOrNull { it >= recommendedCrossSection } 
            ?: standardSections.last()
        
        // Перевірка термічної стійкості
        val thermalStabilityCheck = selectedSection >= minThermalCrossSection
        
        // Формування назви кабеля (AAB 10 3×XX)
        val selectedCable = "AAB 10 3×${selectedSection.toInt()}"
        
        return Task1Result(
            normalCurrent = normalCurrent,
            postEmergencyCurrent = postEmergencyCurrent,
            economicCrossSection = economicCrossSection,
            minThermalCrossSection = minThermalCrossSection,
            recommendedCrossSection = recommendedCrossSection,
            selectedCable = selectedCable,
            thermalStabilityCheck = thermalStabilityCheck
        )
    }
}

