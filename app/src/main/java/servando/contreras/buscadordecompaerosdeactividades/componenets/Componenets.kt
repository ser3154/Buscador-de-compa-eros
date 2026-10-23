package servando.contreras.buscadordecompaerosdeactividades.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideBorder
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideDark
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideHint
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincidePrimary
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideSoft
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideTeal

@Composable
 fun CoincideTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    helper: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = CoincideDark)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 13.sp, color = CoincideHint) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = CoincidePrimary,
                unfocusedBorderColor = CoincideBorder,
                focusedTextColor = CoincideDark,
                unfocusedTextColor = CoincideDark
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (helper != null) {
            Spacer(Modifier.height(4.dp))
            Text(helper, fontSize = 11.sp, color = CoincideHint)
        }
    }
}

@Composable
fun PrimaryButton (text: String, onClick: () -> Unit, modifier: Modifier = Modifier){
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CoincidePrimary, contentColor = CoincideDark)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SoftButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier){
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(44.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CoincideSoft, contentColor = CoincideTeal)
    ) {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
@Composable
fun OutlineButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, CoincideBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = CoincideTeal)
    ) {
        Text(text, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun BackButton(onClick: () -> Unit){
    Box(
        modifier = Modifier
            .size(38.dp)
            .border(1.dp, CoincideBorder, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable{ onClick() },
        contentAlignment = Alignment.Center
    ){
        Text("‹", fontSize = 22.sp, color = CoincideDark)
    }
}

@Composable
fun InfoBanner(title: String, subtitle: String, onClick: (() -> Unit)? = null){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CoincideSoft, RoundedCornerShape(12.dp))
            .border(1.dp, CoincidePrimary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(14.dp)
    ) {
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, fontSize = 11.sp, color = CoincideTeal)
    }
}

