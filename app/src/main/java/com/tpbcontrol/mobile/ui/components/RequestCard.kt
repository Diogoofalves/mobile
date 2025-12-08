package com.tpbcontrol.mobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpbcontrol.mobile.data.remote.dto.MedicationRequestDto

@Composable
fun RequestCard(request: MedicationRequestDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = request.medicationName ?: "Solicitacao ${request.id}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Animal: ${request.animalIdentification ?: "N/D"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fazenda: ${request.farm?.name ?: "N/D"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(
                    text = "Status: ${request.status ?: "pendente"}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "  Prioridade: ${request.priority ?: "-"}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
