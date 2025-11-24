package com.example.calculator4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calculator4.calculations.Task1Calculator
import com.example.calculator4.calculations.Task1Input
import com.example.calculator4.calculations.Task1Result
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task1Screen(onBack: () -> Unit) {
    var voltage by remember { mutableStateOf("10.0") }
    var shortCircuitCurrent by remember { mutableStateOf("2.5") }
    var fictitiousTime by remember { mutableStateOf("2.5") }
    var transformerPower by remember { mutableStateOf("2000.0") }
    var calculatedLoad by remember { mutableStateOf("1300.0") }
    var operatingTime by remember { mutableStateOf("4000.0") }
    var economicCurrentDensity by remember { mutableStateOf("1.4") }
    var thermalConstant by remember { mutableStateOf("92.0") }
    
    var result by remember { mutableStateOf<Task1Result?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Завдання 1: Вибір кабелів") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Вибрати кабелі для живлення двотрансформаторної підстанції",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = voltage,
                onValueChange = { voltage = it },
                label = { Text("Напруга (кВ)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = shortCircuitCurrent,
                onValueChange = { shortCircuitCurrent = it },
                label = { Text("Струм КЗ (кА)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = fictitiousTime,
                onValueChange = { fictitiousTime = it },
                label = { Text("Фіктивний час вимикання (с)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = transformerPower,
                onValueChange = { transformerPower = it },
                label = { Text("Потужність трансформатора (кВА)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = calculatedLoad,
                onValueChange = { calculatedLoad = it },
                label = { Text("Розрахункове навантаження (кВА)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = operatingTime,
                onValueChange = { operatingTime = it },
                label = { Text("Час роботи (год)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = economicCurrentDensity,
                onValueChange = { economicCurrentDensity = it },
                label = { Text("Економічна щільність струму (А/мм²)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = thermalConstant,
                onValueChange = { thermalConstant = it },
                label = { Text("Термічна стала (А·с^0.5/мм²)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    try {
                        val input = Task1Input(
                            voltage = voltage.toDouble(),
                            shortCircuitCurrent = shortCircuitCurrent.toDouble(),
                            fictitiousTime = fictitiousTime.toDouble(),
                            transformerPower = transformerPower.toDouble(),
                            calculatedLoad = calculatedLoad.toDouble(),
                            operatingTime = operatingTime.toDouble(),
                            economicCurrentDensity = economicCurrentDensity.toDouble(),
                            thermalConstant = thermalConstant.toDouble()
                        )
                        result = Task1Calculator.calculate(input)
                        showError = false
                    } catch (e: Exception) {
                        showError = true
                        errorMessage = "Помилка: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Розрахувати")
            }
            
            if (showError) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            result?.let { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Результати розрахунку:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultRow("Струм нормального режиму:", "${round(r.normalCurrent * 100) / 100} А")
                        ResultRow("Струм післяаварійного режиму:", "${round(r.postEmergencyCurrent * 100) / 100} А")
                        ResultRow("Економічний переріз:", "${round(r.economicCrossSection * 100) / 100} мм²")
                        ResultRow("Мін. переріз (термічна стійкість):", "${round(r.minThermalCrossSection * 100) / 100} мм²")
                        ResultRow("Рекомендований переріз:", "${round(r.recommendedCrossSection * 100) / 100} мм²")
                        ResultRow("Обраний кабель:", r.selectedCable)
                        ResultRow(
                            "Термічна стійкість:",
                            if (r.thermalStabilityCheck) "✓ Задовольняє" else "✗ Не задовольняє"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.Bold)
    }
}

