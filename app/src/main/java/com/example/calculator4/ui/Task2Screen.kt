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
import com.example.calculator4.calculations.Task2Calculator
import com.example.calculator4.calculations.Task2Input
import com.example.calculator4.calculations.Task2Result
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task2Screen(onBack: () -> Unit) {
    var systemVoltage by remember { mutableStateOf("10.5") }
    var shortCircuitPower by remember { mutableStateOf("200.0") }
    var transformerVoltage by remember { mutableStateOf("10.5") }
    var transformerNominalPower by remember { mutableStateOf("6.3") }
    var transformerShortCircuitVoltage by remember { mutableStateOf("10.5") }
    
    var result by remember { mutableStateOf<Task2Result?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Завдання 2: Струми КЗ на шинах 10 кВ ГПП") },
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
                "Визначити струми КЗ на шинах 10 кВ ГПП",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = systemVoltage,
                onValueChange = { systemVoltage = it },
                label = { Text("Середня номінальна напруга (кВ)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = shortCircuitPower,
                onValueChange = { shortCircuitPower = it },
                label = { Text("Потужність КЗ (МВА)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = transformerVoltage,
                onValueChange = { transformerVoltage = it },
                label = { Text("Напруга трансформатора (кВ)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = transformerNominalPower,
                onValueChange = { transformerNominalPower = it },
                label = { Text("Номінальна потужність трансформатора (МВА)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = transformerShortCircuitVoltage,
                onValueChange = { transformerShortCircuitVoltage = it },
                label = { Text("Напруга КЗ трансформатора (%)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    try {
                        val input = Task2Input(
                            systemVoltage = systemVoltage.toDouble(),
                            shortCircuitPower = shortCircuitPower.toDouble(),
                            transformerVoltage = transformerVoltage.toDouble(),
                            transformerNominalPower = transformerNominalPower.toDouble(),
                            transformerShortCircuitVoltage = transformerShortCircuitVoltage.toDouble()
                        )
                        result = Task2Calculator.calculate(input)
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
                        ResultRow("Опір системи (Xc):", "${round(r.systemReactance * 1000) / 1000} Ом")
                        ResultRow("Опір трансформатора (Xт):", "${round(r.transformerReactance * 1000) / 1000} Ом")
                        ResultRow("Сумарний опір (XΣ):", "${round(r.totalReactance * 1000) / 1000} Ом")
                        ResultRow("Струм трифазного КЗ (Iп0):", "${round(r.threePhaseShortCircuitCurrent * 1000) / 1000} кА")
                        ResultRow("Струм двофазного КЗ:", "${round(r.twoPhaseShortCircuitCurrent * 1000) / 1000} кА")
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

