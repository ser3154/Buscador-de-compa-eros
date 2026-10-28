package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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