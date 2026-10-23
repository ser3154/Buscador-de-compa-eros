package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.BackButton
import servando.contreras.buscadordecompaerosdeactividades.componenets.CoincideTextField
import servando.contreras.buscadordecompaerosdeactividades.componenets.InfoBanner
import servando.contreras.buscadordecompaerosdeactividades.componenets.OutlineButton
import servando.contreras.buscadordecompaerosdeactividades.componenets.PrimaryButton
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideBackground
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideDark
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideHint

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onSend: () -> Unit
) {
    var email by remember { mutableStateOf("") }

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
            Text("Recuperar contraseña", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        }

        Spacer(Modifier.height(20.dp))
        Text("¿Olvidaste tu contraseña?", fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(8.dp))
        Text(
            "No pasa nada. Escribe tu correo y te enviaremos un enlace para crear una nueva.",
            fontSize = 12.sp, color = CoincideHint
        )
        Spacer(Modifier.height(16.dp))

        InfoBanner(
            title = "Revisa tu bandeja de entrada",
            subtitle = "Si no lo ves, mira también en spam."
        )

        Spacer(Modifier.height(20.dp))
        CoincideTextField(
            label = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "hola@tucorreo.com",
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Enviar enlace", onClick = onSend)
        Spacer(Modifier.height(12.dp))
        OutlineButton("Volver a iniciar sesión", onClick = onBack)
    }
}