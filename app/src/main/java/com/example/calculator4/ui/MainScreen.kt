package com.example.calculator4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onTask1Click: () -> Unit,
    onTask2Click: () -> Unit,
    onTask3Click: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Калькулятор розрахунку струмів КЗ",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        
        Text(
            text = "Виберіть завдання для розрахунку:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        TaskCard(
            title = "Завдання 1",
            description = "Вибрати кабелі для живлення двотрансформаторної підстанції системи внутрішнього електропостачання підприємства напругою 10 кВ",
            onClick = onTask1Click
        )
        
        TaskCard(
            title = "Завдання 2",
            description = "Визначити струми КЗ на шинах 10 кВ ГПП",
            onClick = onTask2Click
        )
        
        TaskCard(
            title = "Завдання 3",
            description = "Визначити струми КЗ для підстанції Хмельницьких північних електричних мереж (ХПнЕМ) з трьома режимами: нормальний, мінімальний, аварійний",
            onClick = onTask3Click
        )
    }
}

@Composable
private fun TaskCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

