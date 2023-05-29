package com.example.appfirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appfirestore.ui.theme.AppFirestoreTheme
import androidx.compose.material3.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppFirestoreTheme {
                App()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App() {
    val db = FirebaseFirestore.getInstance()

    val nome = remember { mutableStateOf("") }
    val endereco = remember { mutableStateOf("") }
    val bairro = remember { mutableStateOf("") }
    val cep = remember { mutableStateOf("") }
    val cidade = remember { mutableStateOf("") }
    val estado = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "App Firebase - método create",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = endereco.value,
            onValueChange = { endereco.value = it },
            label = { Text("Endereço") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = bairro.value,
            onValueChange = { bairro.value = it },
            label = { Text("Bairro") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = cep.value,
            onValueChange = { cep.value = it },
            label = { Text("CEP") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = cidade.value,
            onValueChange = { cidade.value = it },
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = estado.value,
            onValueChange = { estado.value = it },
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val userData = hashMapOf(
                    "nome" to nome.value,
                    "endereco" to endereco.value,
                    "bairro" to bairro.value,
                    "cep" to cep.value,
                    "cidade" to cidade.value,
                    "estado" to estado.value
                )

                // Gravar os dados no Firestore
                db.collection("usuario")
                    .add(userData)

                // Limpar os campos
                nome.value = ""
                endereco.value = ""
                bairro.value = ""
                cep.value = ""
                cidade.value = ""
                estado.value = ""
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Cadastrar")
        }
    }
}