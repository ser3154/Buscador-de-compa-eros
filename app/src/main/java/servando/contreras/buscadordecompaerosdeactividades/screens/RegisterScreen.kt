package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.BackButton
import servando.contreras.buscadordecompaerosdeactividades.componenets.CoincideTextField
import servando.contreras.buscadordecompaerosdeactividades.componenets.InfoBanner
import servando.contreras.buscadordecompaerosdeactividades.componenets.PrimaryButton
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideBackground
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideDark
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideHint

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegister: () -> Unit
){
    var name by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoincideBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text("Registro de usuario", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        }

        Spacer(Modifier.height(20.dp))
        Text("Empieza por ser tú.", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(8.dp))
        Text(
            "Cuéntanos un poco de ti. El próximo buen plan empieza aquí.",
            fontSize = 12.sp, color = CoincideHint
        )
        Spacer(Modifier.height(16.dp))

        InfoBanner(
            title = "Ponle cara a tu perfil",
            subtitle = "Fotografía obligatoria * Añadir foto",
            onClick = {}
        )

        Spacer(Modifier.height(20.dp))
        Text("Un poco sobre ti", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(4.dp))
        Text("Los campos son obligatorios, salvo donde se indique.", fontSize = 11.sp, color = CoincideHint)
        Spacer(Modifier.height(14.dp))

        CoincideTextField("Nombre", name, { name = it }, "Lucía Martín")
        Spacer(Modifier.height(12.dp))
        CoincideTextField(
            "Nickname", nickname, { nickname = it }, "@lucia.en.movimiento",
            helper = "Así te verá la comunidad."
        )
        Spacer(Modifier.height(12.dp))
        CoincideTextField(
            "Género / pronombres", gender, { gender = it }, "Mujer · ella",
            helper = "Elige cómo quieres que nos dirijamos a ti."
        )
        Spacer(Modifier.height(12.dp))
        CoincideTextField(
            "Correo electrónico", email, { email = it }, "lucia.martin@email.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))
        CoincideTextField("Fecha de nacimiento", birth, { birth = it }, "24 / 05 / 2002")
        Spacer(Modifier.height(12.dp))
        CoincideTextField("Contraseña", password, { password = it }, "Crea una contraseña", isPassword = true)

        Spacer(Modifier.height(20.dp))
        PrimaryButton("Crear cuenta", onClick = onRegister)
        Spacer(Modifier.height(16.dp))
    }
}