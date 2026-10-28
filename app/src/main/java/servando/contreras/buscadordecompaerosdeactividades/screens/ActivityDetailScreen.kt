package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.*
import servando.contreras.buscadordecompaerosdeactividades.data.ActivityItem
import servando.contreras.buscadordecompaerosdeactividades.data.ParticipationState
import servando.contreras.buscadordecompaerosdeactividades.data.SampleData
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.*

@Composable
fun ActivityDetailScreen(activityId: Int, onBack: () -> Unit) {
    val activity = SampleData.activities.firstOrNull { it.id == activityId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoincideBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text(
                "Detalle de actividad", fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = CoincideDark, modifier = Modifier.weight(1f)
            )
            Text("coincide.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideTeal)
        }
        Spacer(Modifier.height(16.dp))

        if (activity == null) {
            Text("Actividad no encontrada", fontSize = 16.sp, color = CoincideDark)
        } else {
            ActivityDetailContent(activity, onBack)
        }
    }
}

@Composable
private fun ActivityDetailContent(activity: ActivityItem, onBack: () -> Unit) {

    var participation by remember { mutableStateOf(activity.participation) }

    ActivityImage(activity.imageRes)
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CategoryChip(if (activity.isPublic) "Pública" else "Privada")
        CategoryChip(activity.category)
    }
    Spacer(Modifier.height(12.dp))
    Text(activity.title, fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
    Spacer(Modifier.height(4.dp))
    Text("${activity.date} · ${activity.time}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = CoincideTeal)
    Spacer(Modifier.height(14.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Avatar(activity.organizerName)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(activity.organizerName, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = CoincideDark)
            Text("Organizador/a", fontSize = 11.sp, color = CoincideHint)
        }
    }
    Spacer(Modifier.height(16.dp))
    Box(Modifier.fillMaxWidth().height(1.dp).background(CoincideBorder))
    Spacer(Modifier.height(16.dp))
    Text("Sobre el plan", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
    Spacer(Modifier.height(8.dp))
    Text(activity.description, fontSize = 13.sp, lineHeight = 20.sp, color = CoincideDark)
    Spacer(Modifier.height(16.dp))
    InfoCard(Icons.Default.DateRange, "Cuándo nos vemos", "${activity.date} · ${activity.time}")
    Spacer(Modifier.height(10.dp))
    InfoCard(Icons.Default.Place, "Punto de encuentro", "${activity.place} · ${activity.distance}")
    Spacer(Modifier.height(10.dp))
    InfoCard(Icons.Default.Info, "Precio aproximado", activity.priceRange)
    Spacer(Modifier.height(10.dp))
    InfoCard(
        Icons.Default.Person, "Participantes",
        "${activity.confirmed.size} de ${activity.maxParticipants} lugares ocupados"
    )
    Spacer(Modifier.height(10.dp))
    InfoCard(
        Icons.Default.Check, "Como unirse",
        if (activity.requiresApproval) "Requiere aceptacion del organizador" else "Union directa"
    )

    Spacer(Modifier.height(20.dp))
    Text(
        "Participantes confirmados (${activity.confirmed.size}/${activity.maxParticipants})",
        fontSize = 17.sp, fontWeight = FontWeight.Bold, color = CoincideDark
    )
    Spacer(Modifier.height(10.dp))
    if (activity.confirmed.isEmpty()) {
        Text("Aun no hay confirmados", fontSize = 12.sp, color = CoincideHint)
    } else {
        activity.confirmed.forEach { name ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Avatar(name, size = 32.dp)
                Spacer(Modifier.width(10.dp))
                Text(name, fontSize = 13.sp, color = CoincideDark)
            }
        }
    }

    Spacer(Modifier.height(20.dp))
    when (participation) {
        ParticipationState.VISITOR -> PrimaryButton(
            if (activity.requiresApproval) "Solicitar unirme" else "Unirme a la actividad",
            onClick = {
                participation = if (activity.requiresApproval) ParticipationState.PENDING
                else ParticipationState.PARTICIPANT
            }
        )
        ParticipationState.PENDING -> {
            InfoBanner("Solicitud enviada", "El organizador revisara tu solicitud")
            Spacer(Modifier.height(12.dp))
            OutlineButton("Cancelar solicitud", onClick = { participation = ParticipationState.VISITOR })
        }
        ParticipationState.PARTICIPANT -> {
            InfoBanner("¡Ya formas parte del plan!", "Tu lugar esta confirmado")
            Spacer(Modifier.height(12.dp))
            OutlineButton("Abandonar actividad", onClick = { participation = ParticipationState.VISITOR })
        }
        ParticipationState.CREATOR -> {
            InfoBanner("Este plan es tuyo", "Aqui podras editarlo o eliminarlo")
        }
    }
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun ActivityImage(imageRes: Int?) {
    val shape = RoundedCornerShape(14.dp)
    if (imageRes != null) {
        Image(
            painter = painterResource(imageRes), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(190.dp).clip(shape)
        )
    } else {
        Box(
            modifier = Modifier.fillMaxWidth().height(190.dp).clip(shape)
                .background(Brush.linearGradient(listOf(Color(0xFF8D6E63), Color(0xFFD7B98E))))
        )
    }
}

@Composable
private fun Avatar(name: String, size: Dp = 38.dp) {
    val initials = name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")
    Box(
        modifier = Modifier.size(size).clip(CircleShape).background(CoincideSoft),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoincideTeal)
    }
}

@Composable
private fun InfoCard(icon: ImageVector, label: String, value: String) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White, shape)
            .border(1.dp, CoincideBorder, shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = CoincideTeal, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, fontSize = 11.sp, color = CoincideHint)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = CoincideDark)
        }
    }
}