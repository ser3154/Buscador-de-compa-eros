package servando.contreras.buscadordecompaerosdeactividades.screens

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.*
import servando.contreras.buscadordecompaerosdeactividades.data.UserProfile
import servando.contreras.buscadordecompaerosdeactividades.data.initialsOf
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.BuscadorDeCompañerosDeActividadesTheme

private const val MAX_PHOTO_BYTES = 10L * 1024 * 1024

@Composable
fun SettingsScreen(
    profile: UserProfile,
    darkTheme: Boolean,
    biometricEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onBiometricChange: (Boolean) -> Unit,
    onSave: (UserProfile) -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf(profile.fullName) }
    var username by rememberSaveable { mutableStateOf(profile.username) }
    var email by rememberSaveable { mutableStateOf(profile.email) }
    var city by rememberSaveable { mutableStateOf(profile.city) }
    var bio by rememberSaveable { mutableStateOf(profile.bio) }
    var photoUri by rememberSaveable { mutableStateOf(profile.photoUri) }
    var photoError by rememberSaveable { mutableStateOf<String?>(null) }
    var showErrors by rememberSaveable { mutableStateOf(false) }
    var confirmLogout by rememberSaveable { mutableStateOf(false) }

    val nameValid = name.isNotBlank()
    val emailValid = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val photo = rememberProfilePhoto(photoUri)

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val resolver = context.contentResolver
        val type = resolver.getType(uri)
        val size = runCatching {
            resolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { if (it.moveToFirst()) it.getLong(0) else null }
        }.getOrNull()
        photoError = when {
            type != null && type !in listOf("image/jpeg", "image/png") -> "Elige una foto JPG o PNG."
            size != null && size > MAX_PHOTO_BYTES -> "La foto pesa más de 10 MB."
            else -> {
                runCatching { resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
                photoUri = uri.toString()
                null
            }
        }
    }

    Scaffold(
        containerColor = PlanningColors.background,
        topBar = { Column(Modifier.statusBarsPadding()) { PlanningHeader("Configuración", onBack) } }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Configuración de la cuenta", fontSize = 26.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            Text("Edita tu perfil y elige cómo se ve Coincide.", fontSize = 14.sp, lineHeight = 20.sp, color = PlanningColors.muted)

            // Foto de perfil
            Surface(
                onClick = { pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Cambiar foto de perfil" },
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, PlanningColors.border)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileAvatar(initialsOf(name).ifEmpty { "?" }, photo, 64.dp)
                    Text("Foto de perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = PlanningColors.teal, fontWeight = FontWeight.Medium)) { append("Cambiar foto") }
                            append(" · JPG o PNG · máximo 10 MB")
                        },
                        fontSize = 12.sp, color = PlanningColors.muted
                    )
                    photoError?.let { Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error) }
                }
            }

            Text("Tu perfil", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            CoincideTextField(
                "Nombre completo", name, { name = it },
                helper = if (showErrors && !nameValid) "Escribe tu nombre." else null
            )
            CoincideTextField("Nombre de usuario", username, { username = it.replace(" ", "") })
            CoincideTextField(
                "Correo electrónico", email, { email = it },
                keyboardType = KeyboardType.Email,
                helper = if (showErrors && !emailValid) "Escribe un correo válido." else null
            )
            CoincideTextField("Ciudad", city, { city = it })
            CoincideTextArea("Biografía o descripción", bio, { bio = it }, helper = "Cuenta qué planes te gustan.")

            HorizontalDivider(color = PlanningColors.border)
            Text("Apariencia", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            PlanningCard {
                Text(if (darkTheme) "Modo oscuro" else "Modo claro", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
                Text("Cian como color principal", fontSize = 12.sp, color = PlanningColors.muted)
                PlanningAction(if (darkTheme) "Activar modo claro" else "Activar modo oscuro", { onDarkThemeChange(!darkTheme) })
            }

            HorizontalDivider(color = PlanningColors.border)
            Text("Seguridad", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            PlanningAction("Cambiar contraseña", onChangePassword, outlined = true)
            Row(
                Modifier.fillMaxWidth().toggleable(value = biometricEnabled, role = Role.Switch, onValueChange = onBiometricChange),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Inicio de sesión con biometría", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PlanningColors.ink)
                    Text("Usa tu huella o Face ID al entrar.", fontSize = 12.sp, color = PlanningColors.muted)
                }
                Switch(checked = biometricEnabled, onCheckedChange = null)
            }
            PlanningAction("Guardar cambios", {
                if (!nameValid || !emailValid) {
                    showErrors = true
                } else {
                    onSave(
                        profile.copy(
                            fullName = name.trim(),
                            username = username.trim().removePrefix("@"),
                            email = email.trim(),
                            city = city.trim(),
                            bio = bio.trim(),
                            photoUri = photoUri
                        )
                    )
                    Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
                }
            }, primary = true)
            PlanningAction("Cerrar sesión", { confirmLogout = true }, outlined = true)
            Spacer(Modifier.height(24.dp))
        }
    }

    if (confirmLogout) {
        AlertDialog(
            onDismissRequest = { confirmLogout = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres salir de tu cuenta?") },
            confirmButton = { TextButton(onClick = { confirmLogout = false; onLogout() }) { Text("Cerrar sesión") } },
            dismissButton = { TextButton(onClick = { confirmLogout = false }) { Text("Cancelar") } }
        )
    }
}

@Preview(showBackground = true, widthDp = 402, heightDp = 1500)
@Composable
private fun SettingsScreenPreview() {
    BuscadorDeCompañerosDeActividadesTheme(darkTheme = false) {
        SettingsScreen(UserProfile.Sample, false, true, {}, {}, {}, {}, {}, {})
    }
}
