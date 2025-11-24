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
import com.example.calculator4.calculations.Task3Calculator
import com.example.calculator4.calculations.Task3Input
import com.example.calculator4.calculations.Task3Result
import com.example.calculator4.calculations.Task3ModeResult
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task3Screen(onBack: () -> Unit) {
    var transformerNominalPower by remember { mutableStateOf("6.3") }
    var highVoltage by remember { mutableStateOf("115.0") }
    var lowVoltage by remember { mutableStateOf("11.0") }
    var transformerShortCircuitVoltage by remember { mutableStateOf("11.1") }
    var normalResistance by remember { mutableStateOf("10.65") }
    var normalReactance by remember { mutableStateOf("24.02") }
    var minResistance by remember { mutableStateOf("34.88") }
    var minReactance by remember { mutableStateOf("65.68") }
    
    var result by remember { mutableStateOf<Task3Result?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Завдання 3: Струми КЗ для підстанції ХПнЕМ") },
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
                "Визначити струми КЗ для підстанції з трьома режимами",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Divider()
            
            Text(
                "Параметри трансформатора:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = transformerNominalPower,
                onValueChange = { transformerNominalPower = it },
                label = { Text("Номінальна потужність (МВА)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = highVoltage,
                onValueChange = { highVoltage = it },
                label = { Text("Висока напруга (кВ)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = lowVoltage,
                onValueChange = { lowVoltage = it },
                label = { Text("Низька напруга (кВ)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = transformerShortCircuitVoltage,
                onValueChange = { transformerShortCircuitVoltage = it },
                label = { Text("Напруга КЗ трансформатора (%)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Divider()
            
            Text(
                "Нормальний режим:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = normalResistance,
                onValueChange = { normalResistance = it },
                label = { Text("Опір системи Rс.н (Ом)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = normalReactance,
                onValueChange = { normalReactance = it },
                label = { Text("Реактивний опір Xс.н (Ом)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Divider()
            
            Text(
                "Мінімальний режим:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = minResistance,
                onValueChange = { minResistance = it },
                label = { Text("Опір системи Rс.min (Ом)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = minReactance,
                onValueChange = { minReactance = it },
                label = { Text("Реактивний опір Xс.min (Ом)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    try {
                        val input = Task3Input(
                            transformerNominalPower = transformerNominalPower.toDouble(),
                            highVoltage = highVoltage.toDouble(),
                            lowVoltage = lowVoltage.toDouble(),
                            transformerShortCircuitVoltage = transformerShortCircuitVoltage.toDouble(),
                            normalResistance = normalResistance.toDouble(),
                            normalReactance = normalReactance.toDouble(),
                            minResistance = minResistance.toDouble(),
                            minReactance = minReactance.toDouble()
                        )
                        result = Task3Calculator.calculate(input)
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
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Опір трансформатора:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            ResultRow("Xт:", "${round(r.transformerReactance * 10) / 10} Ом")
                        }
                    }
                    
                    ModeResultCard("Нормальний режим", r.normalMode)
                    ModeResultCard("Мінімальний режим", r.minMode)
                }
            }
        }
    }
}

@Composable
private fun ModeResultCard(title: String, mode: Task3ModeResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "На рівні 110 кВ (приведені):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            ResultRow("Опір (R):", "${round(mode.resistance * 100) / 100} Ом")
            ResultRow("Реактивний опір (X):", "${round(mode.reactance * 100) / 100} Ом")
            ResultRow("Імпеданс (Z):", "${round(mode.impedance * 100) / 100} Ом")
            ResultRow("Струм трифазного КЗ:", "${round(mode.threePhaseCurrent110kV)} А")
            ResultRow("Струм двофазного КЗ:", "${round(mode.twoPhaseCurrent110kV)} А")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "На шинах 10 кВ:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            ResultRow("Опір (R):", "${round(mode.resistance10kV * 100) / 100} Ом")
            ResultRow("Реактивний опір (X):", "${round(mode.reactance10kV * 100) / 100} Ом")
            ResultRow("Імпеданс (Z):", "${round(mode.impedance10kV * 100) / 100} Ом")
            ResultRow("Струм трифазного КЗ:", "${round(mode.threePhaseCurrent10kV)} А")
            ResultRow("Струм двофазного КЗ:", "${round(mode.twoPhaseCurrent10kV)} А")
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

