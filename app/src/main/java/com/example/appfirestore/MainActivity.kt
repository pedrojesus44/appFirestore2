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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appfirestore.ui.theme.AppFirestoreTheme
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore


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

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "cadastro") {
        composable("cadastro") {
            CadastroScreen(navController)
        }
        composable("consulta") {
            ConsultaScreen(navController)
        }
    }
}

@Composable
fun CadastroScreen(navController: NavHostController) {
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
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("consulta")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Ir para Consulta")
        }
    }
}

@Composable
fun ConsultaScreen(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()

    val usuarios = remember { mutableStateListOf<HashMap<String, String>>() }
    val index = remember { mutableStateOf(0) }

    fun carregarDados() {
        db.collection("usuario")
            .get()
            .addOnSuccessListener { result ->
                usuarios.clear()
                for (document in result) {
                    val userData = hashMapOf<String, String>()
                    for ((key, value) in document.data) {
                        userData[key] = value.toString()
                    }
                    usuarios.add(userData)
                }

                // Se a lista de usuários não estiver vazia, definir o índice para o primeiro registro
                if (usuarios.isNotEmpty()) {
                    index.value = 0
                }
            }
            .addOnFailureListener { exception ->
                println("Erro ao carregar dados: $exception")
            }
    }


    @Composable
    fun mostrarDados() {
        val usuarioAtual = usuarios.getOrNull(index.value)

        usuarioAtual?.let {
            val nomeValue = it["nome"] ?: ""
            val enderecoValue = it["endereco"] ?: ""
            val bairroValue = it["bairro"] ?: ""
            val cepValue = it["cep"] ?: ""
            val cidadeValue = it["cidade"] ?: ""
            val estadoValue = it["estado"] ?: ""

            TextField(
                value = nomeValue,
                onValueChange = { },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = enderecoValue,
                onValueChange = { },
                label = { Text("Endereço") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = bairroValue,
                onValueChange = { },
                label = { Text("Bairro") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = cepValue,
                onValueChange = { },
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = cidadeValue,
                onValueChange = { },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = estadoValue,
                onValueChange = { },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "App Firebase - método select",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        mostrarDados()

        Button(
            onClick = {
                if (index.value > 0) {
                    index.value--
                }
            },
            enabled = index.value > 0, // Desabilitar o botão se estiver no primeiro registro
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Anterior")
        }

        Button(
            onClick = {
                if (index.value < usuarios.size - 1) {
                    index.value++
                }
            },
            enabled = index.value < usuarios.size - 1, // Desabilitar o botão se estiver no último registro
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Próximo")
        }

        Button(
            onClick = {
                carregarDados()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Carregar Dados")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("cadastro")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Voltar para cadastro")
        }
    }
}



