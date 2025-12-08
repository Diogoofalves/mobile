package com.tpbcontrol.mobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tpbcontrol.mobile.ui.components.ErrorCard
import com.tpbcontrol.mobile.ui.components.LabeledTextField
import com.tpbcontrol.mobile.ui.components.PrimaryButton
import com.tpbcontrol.mobile.ui.state.AuthUiState

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // limpa erro quando volta para a tela
        if (uiState.error != null) {
            // no-op, error vem do viewmodel
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TPB Control",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Acesse para acompanhar solicitacoes e tarefas",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        if (!uiState.error.isNullOrBlank()) {
            ErrorCard(message = uiState.error!!)
            Spacer(modifier = Modifier.height(12.dp))
        }

        LabeledTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        LabeledTextField(
            value = password,
            onValueChange = { password = it },
            label = "Senha",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Entrar",
            onClick = { onLogin(email.trim(), password) },
            loading = uiState.loading,
            enabled = email.isNotBlank() && password.isNotBlank()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Criar conta",
            modifier = Modifier
                .clickable { onNavigateToRegister() }
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
