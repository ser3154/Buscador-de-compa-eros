package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.*

enum class ParticipationDecision(val label: String) {
    PENDING("Pendiente"), ACCEPTED("Aceptada"), REJECTED("Rechazada")
}

/** Diseño Figma 95:4050. El estado se eleva a la navegación; no envía solicitudes a un servidor. */
@Composable
fun ParticipationRequestsScreen(
    decisions: List<ParticipationDecision>,
    onDecision: (Int, ParticipationDecision) -> Unit,
    onBack: () -> Unit
) {
    var rejecting by rememberSaveable { mutableStateOf(-1) }
    val names = listOf("Elena Torres", "Diego Pérez")
    val participants = 3 + decisions.count { it == ParticipationDecision.ACCEPTED }
    Scaffold(containerColor = PlanningColors.background,
        topBar = { Column(Modifier.statusBarsPadding()) { PlanningHeader("Solicitudes para participar", onBack) } }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("Una vuelta al Retiro, sin prisa", color = PlanningColors.ink, fontSize = 25.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)
            Text("Sáb. 24 oct. · 10:00", color = PlanningColors.teal, fontSize = 13.sp)
            PlanningBadge("$participants de 8 participantes · ${8 - participants} cupos")
            Text("Revisa quién quiere compartir el plan.", color = PlanningColors.muted, fontSize = 13.sp)
            names.forEachIndexed { index, name ->
                PlanningCard {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.size(38.dp).background(PlanningColors.soft, CircleShape), contentAlignment = Alignment.Center) {
                            Text(if (index == 0) "ET" else "DP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PlanningColors.teal)
                        }
                        Column {
                            Text(name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = PlanningColors.ink)
                            Text(if (index == 0) "Hace 15 minutos" else "Hace 1 hora", fontSize = 11.sp, color = PlanningColors.muted)
                        }
                    }
                    Text("Me gustaría participar y conocer gente nueva.", color = PlanningColors.ink, fontSize = 13.sp, lineHeight = 19.sp)
                    PlanningBadge(decisions[index].label)
                    if (decisions[index] == ParticipationDecision.PENDING) {
                        PlanningAction("Aceptar", { onDecision(index, ParticipationDecision.ACCEPTED) }, primary = true)
                        PlanningAction("Rechazar", { rejecting = index }, outlined = true)
                    } else {
                        Text(if (decisions[index] == ParticipationDecision.ACCEPTED) "Se agregó a la lista de participantes." else "Esta solicitud fue rechazada.",
                            color = PlanningColors.muted, fontSize = 12.sp)
                        PlanningAction("Deshacer", { onDecision(index, ParticipationDecision.PENDING) }, outlined = true)
                    }
                }
            }
            Text("Demostración de estados. Estas acciones no modifican datos reales.", color = PlanningColors.muted, fontSize = 11.sp)
            PlanningAction("Volver al calendario", onBack)
            Spacer(Modifier.height(24.dp))
        }
    }
    if (rejecting in names.indices) AlertDialog(onDismissRequest = { rejecting = -1 },
        title = { Text("Rechazar solicitud") }, text = { Text("¿Rechazar la solicitud de ${names[rejecting]}?") },
        confirmButton = { TextButton(onClick = { onDecision(rejecting, ParticipationDecision.REJECTED); rejecting = -1 }) { Text("Rechazar") } },
        dismissButton = { TextButton(onClick = { rejecting = -1 }) { Text("Cancelar") } })
}

@Preview(showBackground = true, widthDp = 402, heightDp = 874)
@Composable
private fun ParticipationRequestsPreview() {
    servando.contreras.buscadordecompaerosdeactividades.ui.theme.BuscadorDeCompañerosDeActividadesTheme {
        ParticipationRequestsScreen(List(2) { ParticipationDecision.PENDING }, { _, _ -> }, {})
    }
}
