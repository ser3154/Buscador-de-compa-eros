package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import servando.contreras.buscadordecompaerosdeactividades.R
import servando.contreras.buscadordecompaerosdeactividades.componenets.*
import servando.contreras.buscadordecompaerosdeactividades.data.ActivityHistory
import servando.contreras.buscadordecompaerosdeactividades.data.PastActivity
import servando.contreras.buscadordecompaerosdeactividades.data.SampleData
import servando.contreras.buscadordecompaerosdeactividades.data.UserProfile
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.BuscadorDeCompañerosDeActividadesTheme

private enum class ProfileTab(val label: String) { UPCOMING("Próximas"), ORGANIZED("Organizo"), HISTORY("Historial") }

private val shortMonths = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onStatistics: () -> Unit,
    onCalendar: () -> Unit,
    onNewActivity: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photo = rememberProfilePhoto(profile.photoUri)
    val tabsRequester = remember { BringIntoViewRequester() }
    var tab by rememberSaveable { mutableStateOf(ProfileTab.UPCOMING) }
    var showPublicProfile by rememberSaveable { mutableStateOf(false) }

    // Las cifras del perfil salen del historial de los últimos 3 meses (18 actividades, 5 organizadas).
    val history = remember {
        val today = ActivityHistory.today
        ActivityHistory.between(today.minusMonths(3).plusDays(1), today).sortedByDescending { it.date }
    }
    var openedActivity by remember { mutableStateOf<PastActivity?>(null) }

    fun openTab(target: ProfileTab) {
        tab = target
        scope.launch { tabsRequester.bringIntoView() }
    }

    Scaffold(
        containerColor = PlanningColors.background,
        topBar = {
            Row(
                Modifier.fillMaxWidth().statusBarsPadding().padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        profile.username, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text("coincide.", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PlanningColors.teal)
                }
                IconButton(onClick = onNewActivity) {
                    Icon(Icons.Outlined.Add, "Nueva actividad", tint = PlanningColors.ink)
                }
                IconButton(onClick = onSettings) {
                    Icon(Icons.Outlined.Menu, "Configuración", tint = PlanningColors.ink)
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            // Avatar con aro de historia + contadores
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                StoryAvatar(profile.initials, photo, onClick = onEditProfile)
                Spacer(Modifier.width(12.dp))
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ProfileCount("${history.size}", "actividades") { openTab(ProfileTab.HISTORY) }
                    ProfileCount("${history.count { it.organized }}", "organizadas") { openTab(ProfileTab.ORGANIZED) }
                    ProfileCount("${profile.friends}", "amigos") { onUnavailable("Mi círculo") }
                }
            }

            // Nombre, ciudad, biografía e intereses
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(profile.fullName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
                Text(profile.city, fontSize = 13.sp, color = PlanningColors.muted)
                if (profile.bio.isNotBlank()) {
                    Text(profile.bio, fontSize = 14.sp, lineHeight = 20.sp, color = PlanningColors.ink)
                }
                Spacer(Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    profile.interests.forEach { PlanningBadge(it) }
                }
            }

            // Botones como en Instagram: Editar perfil · Ver perfil público · Compartir
            Row(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CompactButton("Editar perfil", onEditProfile, filled = true, modifier = Modifier.weight(1f))
                CompactButton("Ver perfil público", { showPublicProfile = true }, filled = false, modifier = Modifier.weight(1f))
                Surface(
                    onClick = {
                        shareText(
                            context,
                            "Mira el perfil de ${profile.fullName} (@${profile.username}) en Coincide.",
                            "Compartir perfil"
                        )
                    },
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = PlanningColors.soft
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Share, "Compartir perfil", Modifier.size(18.dp), tint = PlanningColors.teal)
                    }
                }
            }

            // «Solo para ti» como historias destacadas
            Row(
                Modifier.padding(start = 16.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Outlined.Lock, null, Modifier.size(12.dp), tint = PlanningColors.muted)
                Text("Solo para ti", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.muted)
            }
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Highlight("Mi círculo", rememberVectorPainter(Icons.Outlined.Person), badge = profile.pendingFriendRequests) {
                    onUnavailable("Mi círculo · ${profile.friends} amigos · ${profile.pendingFriendRequests} solicitudes")
                }
                Highlight("Historial", rememberVectorPainter(Icons.AutoMirrored.Outlined.List)) { openTab(ProfileTab.HISTORY) }
                Highlight("Estadísticas", painterResource(R.drawable.stats_trending_up), onClick = onStatistics)
                Highlight("Ajustes", rememberVectorPainter(Icons.Outlined.Settings), onClick = onSettings)
                Highlight("Nueva", rememberVectorPainter(Icons.Outlined.Add), dashed = true, onClick = onNewActivity)
            }

            // Pestañas
            Column(Modifier.bringIntoViewRequester(tabsRequester)) {
                Row(Modifier.fillMaxWidth()) {
                    ProfileTab.entries.forEach { item ->
                        val painter = when (item) {
                            ProfileTab.UPCOMING -> rememberVectorPainter(Icons.Outlined.DateRange)
                            ProfileTab.ORGANIZED -> rememberVectorPainter(Icons.Outlined.Star)
                            ProfileTab.HISTORY -> painterResource(R.drawable.profile_grid)
                        }
                        ProfileTabItem(item.label, painter, selected = tab == item, modifier = Modifier.weight(1f)) { tab = item }
                    }
                }
                HorizontalDivider(color = PlanningColors.border)

                when (tab) {
                    ProfileTab.UPCOMING -> UpcomingTab(onCalendar)
                    ProfileTab.ORGANIZED -> OrganizedTab(profile.initials, photo) { onUnavailable("Administrar actividad") }
                    ProfileTab.HISTORY -> HistoryGrid(history) { openedActivity = it }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showPublicProfile) {
        PublicProfileDialog(profile, photo) { showPublicProfile = false }
    }
    openedActivity?.let { activity ->
        AlertDialog(
            onDismissRequest = { openedActivity = null },
            title = { Text(activity.title) },
            text = {
                Text(
                    "${activity.date.dayOfMonth} ${shortMonths[activity.date.monthValue - 1]} ${activity.date.year} · ${activity.category}\n" +
                        (if (activity.organized) "Organizaste este plan." else "Te uniste a este plan.") +
                        "\nCon ${activity.people.joinToString()}."
                )
            },
            confirmButton = { TextButton(onClick = { openedActivity = null }) { Text("Cerrar") } }
        )
    }
}

/** Avatar grande con aro degradado, como las historias de Instagram. */
@Composable
private fun StoryAvatar(initials: String, photo: ImageBitmap?, onClick: () -> Unit) {
    val ring = Brush.sweepGradient(
        listOf(PlanningColors.primary, PlanningColors.teal, Color(0xFF3DD6A3), PlanningColors.primary)
    )
    Box(Modifier.size(92.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.fillMaxSize().border(3.dp, ring, CircleShape))
        ProfileAvatar(
            initials, photo, 80.dp,
            Modifier.clip(CircleShape).clickable(onClickLabel = "Editar perfil", onClick = onClick)
        )
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .size(28.dp)
                .clip(CircleShape)
                .background(PlanningColors.background)
                .padding(3.dp)
                .clip(CircleShape)
                .background(PlanningColors.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Add, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun ProfileCount(value: String, label: String, onClick: () -> Unit) {
    Column(
        Modifier.clip(RoundedCornerShape(10.dp)).clickable(onClick = onClick).padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
        Text(label, fontSize = 12.sp, color = PlanningColors.ink)
    }
}

@Composable
private fun CompactButton(text: String, onClick: () -> Unit, filled: Boolean, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(10.dp)
    val padding = PaddingValues(horizontal = 8.dp)
    if (filled) {
        Button(
            onClick, modifier.height(40.dp), shape = shape, contentPadding = padding,
            colors = ButtonDefaults.buttonColors(containerColor = PlanningColors.soft, contentColor = PlanningColors.teal)
        ) { Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) }
    } else {
        OutlinedButton(
            onClick, modifier.height(40.dp), shape = shape, contentPadding = padding,
            border = BorderStroke(1.dp, PlanningColors.border),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = PlanningColors.teal)
        ) { Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1) }
    }
}

/** Círculo de «historia destacada». */
@Composable
private fun Highlight(label: String, icon: Painter, badge: Int = 0, dashed: Boolean = false, onClick: () -> Unit) {
    Column(
        Modifier.width(76.dp).clip(RoundedCornerShape(12.dp)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BadgedBox(badge = { if (badge > 0) Badge { Text("$badge") } }) {
            Box(
                Modifier
                    .size(64.dp)
                    .border(1.5.dp, if (dashed) PlanningColors.border else PlanningColors.primary.copy(alpha = 0.55f), CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(if (dashed) MaterialTheme.colorScheme.surface else PlanningColors.soft),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, Modifier.size(24.dp), tint = PlanningColors.teal)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 12.sp, color = PlanningColors.ink, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ProfileTabItem(label: String, icon: Painter, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val color = if (selected) PlanningColors.ink else PlanningColors.muted
    Column(
        modifier.clickable(role = Role.Tab, onClick = onClick).semantics { this.selected = selected },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, Modifier.size(20.dp), tint = color)
            Text(label, fontSize = 12.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium, color = color)
        }
        Box(Modifier.fillMaxWidth().height(2.dp).background(if (selected) PlanningColors.ink else Color.Transparent))
    }
}

@Composable
private fun UpcomingTab(onCalendar: () -> Unit) {
    val plan = SampleData.plans.first()
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Próximas actividades", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            TextButton(onClick = onCalendar) {
                Text("Ver calendario", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.teal)
            }
        }
        ActivityPost(
            category = plan.category, date = plan.date, title = plan.title, place = plan.place,
            organizer = "Andrea López", participants = "3 de 6 participantes", price = "$60–$120 MXN"
        )
    }
}

/** Actividad presentada como una publicación de Instagram. */
@Composable
private fun ActivityPost(
    category: String, date: String, title: String, place: String,
    organizer: String, participants: String, price: String
) {
    val context = LocalContext.current
    var liked by rememberSaveable { mutableStateOf(false) }
    var requested by rememberSaveable { mutableStateOf(false) }
    val shape = RoundedCornerShape(18.dp)
    Column(
        Modifier.fillMaxWidth().clip(shape).background(MaterialTheme.colorScheme.surface).border(1.dp, PlanningColors.border, shape)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileAvatar("AL", null, 32.dp)
            Column(Modifier.weight(1f)) {
                Text("$organizer organiza", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PlanningColors.ink)
                Text(participants, fontSize = 11.sp, color = PlanningColors.muted)
            }
            PlanningBadge(category)
        }
        Image(
            painterResource(R.drawable.profile_cafe_apuntes),
            contentDescription = "Tres personas estudian con café y portátiles en una cafetería",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
        )
        Row(Modifier.padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { liked = !liked }) {
                Icon(
                    if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    if (liked) "Quitar me interesa" else "Me interesa",
                    tint = if (liked) Color(0xFFE5484D) else PlanningColors.ink
                )
            }
            IconButton(onClick = { shareText(context, "$title · $date · $place", "Compartir actividad") }) {
                Icon(Icons.Outlined.Share, "Compartir actividad", tint = PlanningColors.ink)
            }
            Spacer(Modifier.weight(1f))
            Text(
                "${if (liked) 13 else 12} me interesa", Modifier.padding(end = 12.dp),
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.ink
            )
        }
        Column(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(date, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PlanningColors.teal)
            Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            Text(place, fontSize = 11.sp, color = PlanningColors.muted)
            Text(price, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PlanningColors.teal)
            Spacer(Modifier.height(4.dp))
            if (requested) {
                Text("Solicitud enviada. ${organizer.substringBefore(" ")} te avisará.", fontSize = 12.sp, color = PlanningColors.teal)
                PlanningAction("Cancelar solicitud", { requested = false }, outlined = true)
            } else {
                PlanningAction("Solicitar unirme", { requested = true }, primary = true)
            }
        }
    }
}

@Composable
private fun OrganizedTab(initials: String, photo: ImageBitmap?, onManage: () -> Unit) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Actividad organizada por mí", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
        PlanningCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileAvatar(initials, photo, 32.dp)
                Column {
                    Text("Tú organizas", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PlanningColors.ink)
                    Text("Visible para todos", fontSize = 11.sp, color = PlanningColors.muted)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Dom. 25 oct. · 17:00", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PlanningColors.teal)
                Text("Tarde de picnic y música", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
                Text("Parque del Oeste · 6 de 10 cupos", fontSize = 12.sp, color = PlanningColors.muted)
            }
            LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = PlanningColors.primary,
                trackColor = PlanningColors.soft,
                drawStopIndicator = {}
            )
            PlanningAction("Administrar", onManage)
        }
    }
}

/** Cuántas actividades del historial se ven antes de pulsar «Ver todo». */
private const val HISTORY_PREVIEW = 6

/** Cuadrícula tranquila de 3 columnas: tarjetas claras y solo las 6 más recientes al principio. */
@Composable
private fun HistoryGrid(items: List<PastActivity>, onOpen: (PastActivity) -> Unit) {
    var showAll by rememberSaveable { mutableStateOf(false) }
    val visible = if (showAll) items else items.take(HISTORY_PREVIEW)
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Últimos 3 meses", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
            Text("${items.size} actividades", fontSize = 12.sp, color = PlanningColors.muted)
        }
        visible.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { activity -> HistoryTile(activity, Modifier.weight(1f)) { onOpen(activity) } }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
        if (items.size > HISTORY_PREVIEW) {
            Spacer(Modifier.height(4.dp))
            PlanningAction(
                if (showAll) "Ver menos" else "Ver todo el historial (${items.size})",
                { showAll = !showAll },
                outlined = true
            )
        }
    }
}

@Composable
private fun HistoryTile(activity: PastActivity, modifier: Modifier, onClick: () -> Unit) {
    val dot = when (activity.category) {
        "Deporte" -> PlanningColors.primary
        "Estudio" -> PlanningColors.teal
        "Hobby" -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> PlanningColors.muted
    }
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, PlanningColors.border, shape)
            .clickable(onClickLabel = "Ver ${activity.title}", onClick = onClick)
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(dot))
            Spacer(Modifier.width(6.dp))
            Text(
                "${activity.date.dayOfMonth} ${shortMonths[activity.date.monthValue - 1]}",
                Modifier.weight(1f), fontSize = 11.sp, color = PlanningColors.muted
            )
            if (activity.organized) {
                Icon(Icons.Outlined.Star, "Organizada por ti", Modifier.size(14.dp), tint = PlanningColors.teal)
            }
        }
        Spacer(Modifier.weight(1f))
        Text(
            activity.title,
            fontSize = 13.sp, lineHeight = 17.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.ink,
            maxLines = 3, overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PublicProfileDialog(profile: UserProfile, photo: ImageBitmap?, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tu perfil público") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileAvatar(profile.initials, photo, 48.dp)
                    Column {
                        Text(profile.fullName, fontWeight = FontWeight.Bold)
                        Text("@${profile.username} · ${profile.city}", fontSize = 12.sp)
                    }
                }
                if (profile.bio.isNotBlank()) Text(profile.bio, fontSize = 14.sp)
                Text("Intereses: ${profile.interests.joinToString()}", fontSize = 13.sp)
                Text("Tu correo, tu historial y tus estadísticas solo los ves tú.", fontSize = 12.sp)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cerrar") } }
    )
}

@Preview(showBackground = true, widthDp = 402, heightDp = 1400)
@Composable
private fun ProfileScreenPreview() {
    BuscadorDeCompañerosDeActividadesTheme(darkTheme = false) {
        ProfileScreen(UserProfile.Sample, {}, {}, {}, {}, {}, {})
    }
}
