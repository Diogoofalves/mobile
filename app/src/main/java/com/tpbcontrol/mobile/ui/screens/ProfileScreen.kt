package com.tpbcontrol.mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpbcontrol.mobile.data.remote.dto.UserDto
import com.tpbcontrol.mobile.ui.components.PrimaryButton

@Composable
fun ProfileScreen(
    user: UserDto?,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = user?.fullName ?: "Sem nome",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(text = user?.email ?: "-", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tipo: ${user?.userType ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                if (!user?.phone.isNullOrBlank()) {
                    Text(text = "Telefone: ${user?.phone}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = "Sair",
            onClick = onLogout
        )
    }
}
