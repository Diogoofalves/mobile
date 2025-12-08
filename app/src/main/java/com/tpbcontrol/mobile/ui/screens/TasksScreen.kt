package com.tpbcontrol.mobile.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tpbcontrol.mobile.data.remote.dto.MedicationRequestDto
import com.tpbcontrol.mobile.ui.components.EmptyState
import com.tpbcontrol.mobile.ui.components.ErrorCard
import com.tpbcontrol.mobile.ui.components.LoadingView
import com.tpbcontrol.mobile.ui.components.RequestCard
import com.tpbcontrol.mobile.ui.state.ListUiState

@Composable
fun TasksScreen(
    state: ListUiState<MedicationRequestDto>,
    userType: String?,
    modifier: Modifier = Modifier
) {
    if (userType != "funcionario") {
        Text(
            text = "Tarefas disponiveis apenas para funcionarios.",
            modifier = modifier.padding(16.dp)
        )
        return
    }

    when {
        state.loading -> LoadingView()
        state.error != null -> ErrorCard(message = state.error)
        state.items.isEmpty() -> EmptyState(message = "Nenhuma tarefa atribuida.")
        else -> LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.items, key = { it.id }) { task ->
                RequestCard(request = task)
            }
        }
    }
}
