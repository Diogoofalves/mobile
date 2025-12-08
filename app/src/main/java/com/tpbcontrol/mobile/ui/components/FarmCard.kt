package com.tpbcontrol.mobile.ui.components

import androidx.compose.foundation.layout.Column
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
import com.tpbcontrol.mobile.data.remote.dto.FarmDto

@Composable
fun FarmCard(farm: FarmDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = farm.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = farm.address ?: "Sem endereco informado",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Pendentes: ${farm.requestsStats?.pending ?: 0}  |  Concluidas: ${farm.requestsStats?.completed ?: 0}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
