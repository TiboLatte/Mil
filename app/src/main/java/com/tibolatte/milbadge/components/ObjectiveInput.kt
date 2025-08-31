package com.tibolatte.milbadge.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.ObjectiveType


@Composable
fun ObjectiveInput(
    badge: Badge,
    onComplete: (value: Any) -> Unit // value: Int pour COUNT/DURATION, Boolean pour YES_NO
) {
    when (badge.objectiveType) {
        ObjectiveType.COUNT, ObjectiveType.DURATION -> {
            var input by remember { mutableStateOf("") }
            Column {
                Text("Objectif: ${badge.progress?.second ?: "—"}")
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = input,
                    onValueChange = { input = it.filter { char -> char.isDigit() } },
                    label = { Text("Entrez un nombre") }
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    val value = input.toIntOrNull() ?: 0
                    onComplete(value)
                }) {
                    Text("Valider")
                }
            }
        }

        ObjectiveType.YES_NO -> {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onComplete(true) }) { Text("Oui") }
                Button(onClick = { onComplete(false) }) { Text("Non") }
            }
        }

        ObjectiveType.CUSTOM -> {
            Text("Objectif personnalisé à définir")
        }
    }
}