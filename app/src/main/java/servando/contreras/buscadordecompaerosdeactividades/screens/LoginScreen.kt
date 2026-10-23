package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.R
import servando.contreras.buscadordecompaerosdeactividades.componenets.OutlineButton
import servando.contreras.buscadordecompaerosdeactividades.componenets.PrimaryButton
import servando.contreras.buscadordecompaerosdeactividades.componenets.SoftButton
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideBackground
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideDark
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideHint
import servando.contreras.buscadordecompaerosdeactividades.componenets.CoincideTextField

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onBiometric: () -> Unit,
    onRegister: () -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoincideBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text("Bienvenido", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(14.dp))
        Image(
        painter = painterResource(R.drawable.login_image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF7FB77E), Color(0xFFC9A66B))))
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Tu gente. \nTus próximos planes.",
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = CoincideDark
        )
        Spacer(Modifier.height(10.dp))
        Text("Encuentra con quién hacer eso que te gusta.", fontSize = 12.sp, color = CoincideHint)
        Spacer(Modifier.height(16.dp))

        CoincideTextField(
            label = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "hola@tucorreo.com",
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
        )

        Spacer(Modifier.height(12.dp))
        CoincideTextField(
            label = "Contraseña",
            value = password,
            onValueChange = { password = it },
            placeholder = "Introduce tu contraseña",
            isPassword = true
        )
        Spacer(Modifier.height(12.dp))
        OutlineButton("¿Olvidaste tu\ncontraseña?", onClick = onForgotPassword)
        Spacer(Modifier.height(12.dp))
        PrimaryButton("Entrar", onClick = onLogin)

        Spacer(Modifier.height(12.dp))
        Text(
            "O entra con tu huella",
            fontSize = 11.sp,
            color = CoincideHint,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(8.dp))
        SoftButton("Usar biometría", onClick = onBiometric)
        Spacer(Modifier.height(12.dp))
        OutlineButton("Crear cuenta", onClick = onRegister)
        Spacer(Modifier.height(16.dp))
    }
}