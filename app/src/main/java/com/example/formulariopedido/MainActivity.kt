package com.example.formulariopedido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí es donde mandamos a llamar a tu pantalla
                    FormularioPedido()
                }
            }
        }
    }
}

@Composable
fun FormularioPedido() {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var producto by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val nombreError = nombre.isNotEmpty() && nombre.length < 3
    val telefonoError = telefono.isNotEmpty() && (!telefono.all { it.isDigit() } || telefono.length < 8)
    val direccionError = direccion.isNotEmpty() && direccion.isBlank()
    val cantidadError = cantidad.isNotEmpty() && (cantidad.toIntOrNull() ?: 0) <= 0

    val formValido = nombre.length >= 3 &&
            telefono.all { it.isDigit() } && telefono.length >= 8 &&
            direccion.isNotBlank() &&
            producto.isNotBlank() &&
            (cantidad.toIntOrNull() ?: 0) > 0

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Formulario de Pedido", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                isError = nombreError,
                supportingText = { if (nombreError) Text("Mínimo 3 caracteres") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                isError = telefonoError,
                supportingText = { if (telefonoError) Text("Solo números, mínimo 8 dígitos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                isError = direccionError,
                supportingText = { if (direccionError) Text("Dirección obligatoria") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = producto,
                onValueChange = { producto = it },
                label = { Text("Producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                isError = cantidadError,
                supportingText = { if (cantidadError) Text("Debe ser mayor que 0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
                label = { Text("Notas (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        nombre = ""
                        telefono = ""
                        direccion = ""
                        producto = ""
                        cantidad = ""
                        notas = ""
                    },
                    enabled = !isLoading
                ) {
                    Text("Limpiar")
                }

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(2000)
                            isLoading = false
                            snackbarHostState.showSnackbar("Pedido enviado exitosamente")
                            nombre = ""
                            telefono = ""
                            direccion = ""
                            producto = ""
                            cantidad = ""
                            notas = ""
                        }
                    },
                    enabled = formValido && !isLoading
                ) {
                    Text("Enviar")
                }
            }
        }
    }
}