package com.tpbcontrol.mobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tpbcontrol.mobile.data.remote.dto.RegisterRequest
import com.tpbcontrol.mobile.ui.components.ErrorCard
import com.tpbcontrol.mobile.ui.components.LabeledTextField
import com.tpbcontrol.mobile.ui.components.PrimaryButton
import com.tpbcontrol.mobile.ui.state.AuthUiState

private val userTypes = listOf(
    "veterinario" to "Veterinario",
    "funcionario" to "Funcionario",
    "fazenda" to "Fazenda"
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegisterScreen(
    uiState: AuthUiState,
    onRegister: (RegisterRequest) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(userTypes.first().first) }
    var specialization by remember { mutableStateOf("") }
    var crmvNumber by remember { mutableStateOf("") }
    var farmExperience by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var mainAddress by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    fun validateAndSubmit() {
        localError = null
        if (password != confirmPassword) {
            localError = "As senhas devem ser iguais."
            return
        }
        if (fullName.isBlank()) {
            localError = "Informe o nome completo."
            return
        }
        val request = RegisterRequest(
            fullName = fullName.trim(),
            email = email.trim(),
            password = password,
            userType = userType,
            phone = phone.takeIf { it.isNotBlank() },
            specialization = specialization.takeIf { it.isNotBlank() && userType == "veterinario" },
            crmvNumber = crmvNumber.takeIf { it.isNotBlank() && userType == "veterinario" },
            farmExperience = farmExperience.takeIf { it.isNotBlank() && userType == "funcionario" },
            companyName = companyName.takeIf { it.isNotBlank() && userType == "fazenda" },
            cnpj = cnpj.takeIf { it.isNotBlank() && userType == "fazenda" },
            mainAddress = mainAddress.takeIf { it.isNotBlank() && userType == "fazenda" }
        )
        onRegister(request)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Criar conta",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Cadastre-se para usar o TPB Control",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        if (localError != null) {
            ErrorCard(message = localError!!)
            Spacer(modifier = Modifier.height(8.dp))
        } else if (!uiState.error.isNullOrBlank()) {
            ErrorCard(message = uiState.error!!)
            Spacer(modifier = Modifier.height(8.dp))
        }

        LabeledTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Nome completo",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = userTypes.first { it.first == userType }.second,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de usuario") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                userTypes.forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            userType = value
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        LabeledTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        LabeledTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Telefone (opcional)",
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
        Spacer(modifier = Modifier.height(12.dp))
        LabeledTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar senha",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true
        )

        when (userType) {
            "veterinario" -> {
                Spacer(modifier = Modifier.height(12.dp))
                LabeledTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    label = "Especializacao",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                LabeledTextField(
                    value = crmvNumber,
                    onValueChange = { crmvNumber = it },
                    label = "CRMV",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            "funcionario" -> {
                Spacer(modifier = Modifier.height(12.dp))
                LabeledTextField(
                    value = farmExperience,
                    onValueChange = { farmExperience = it },
                    label = "Experiencia na fazenda",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )
            }
            "fazenda" -> {
                Spacer(modifier = Modifier.height(12.dp))
                LabeledTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = "Nome da empresa",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                LabeledTextField(
                    value = cnpj,
                    onValueChange = { cnpj = it },
                    label = "CNPJ",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                LabeledTextField(
                    value = mainAddress,
                    onValueChange = { mainAddress = it },
                    label = "Endereco principal",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Criar conta",
            onClick = { validateAndSubmit() },
            loading = uiState.loading
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ja tenho conta",
            modifier = Modifier
                .clickable { onNavigateToLogin() }
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
