package com.tpbcontrol.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpbcontrol.mobile.data.remote.dto.ActivityItemDto
import com.tpbcontrol.mobile.data.remote.dto.MedicationRequestDto
import com.tpbcontrol.mobile.ui.components.EmptyState
import com.tpbcontrol.mobile.ui.components.ErrorCard
import com.tpbcontrol.mobile.ui.components.LoadingView
import com.tpbcontrol.mobile.ui.state.DashboardUiState
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private fun formatDate(value: String?): String {
    return try {
        val parsed = OffsetDateTime.parse(value)
        parsed.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (_: Exception) {
        value ?: "-"
    }
}

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    userName: String?,
    modifier: Modifier = Modifier
) {
    when {
        state.loading -> LoadingView()
        state.error != null -> ErrorCard(message = state.error)
        else -> DashboardContent(state = state, userName = userName, modifier = modifier)
    }
}

@Composable
private fun DashboardContent(
    state: DashboardUiState,
    userName: String?,
    modifier: Modifier = Modifier
) {
    val stats = state.data?.stats
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Ola, ${userName ?: "usuario"}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Resumo rapido do TPB Control",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Indicadores", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatBox(title = "Pendentes", value = stats?.requests?.pending ?: stats?.tasks?.pending ?: 0)
                        StatBox(title = "Concluidas", value = stats?.requests?.completed ?: stats?.tasks?.completed ?: 0)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatBox(title = "Atrasadas", value = stats?.requests?.overdue ?: stats?.tasks?.overdue ?: 0)
                        StatBox(title = "Hoje", value = stats?.requests?.todayCompleted ?: stats?.tasks?.todayCompleted ?: 0)
                    }
                }
            }
        }

        if (state.pendingReviews.isNotEmpty()) {
            item {
                Text(
                    text = "Verificacoes pendentes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(state.pendingReviews, key = { it.id }) { req ->
                PendingReviewCard(req)
            }
        }

        item {
            Text(
                text = "Atividade recente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        if (state.recentActivity.isEmpty()) {
            item { EmptyState(message = "Nenhuma atividade registrada ainda.") }
        } else {
            items(state.recentActivity) { activity ->
                ActivityCard(activity)
            }
        }
    }
}

@Composable
private fun StatBox(title: String, value: Int?) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Text(
            text = (value ?: 0).toString(),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun PendingReviewCard(request: MedicationRequestDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = request.medicationName ?: "Solicitacao ${request.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Animal: ${request.animalIdentification ?: "-"} | Fazenda: ${request.farm?.name ?: "-"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Aplicada em: ${formatDate(request.appliedDatetime)}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ActivityCard(activity: ActivityItemDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(activity.title.orEmpty(), style = MaterialTheme.typography.titleMedium)
            activity.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "Status: ${activity.status ?: "-"} • ${formatDate(activity.timestamp)}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
