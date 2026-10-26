package servando.contreras.buscadordecompaerosdeactividades.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
object PlanningColors {
    val background @Composable get() = MaterialTheme.colorScheme.background
    val ink @Composable get() = MaterialTheme.colorScheme.onBackground
    val muted @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    val teal @Composable get() = MaterialTheme.colorScheme.secondary
    val soft @Composable get() = MaterialTheme.colorScheme.secondaryContainer
    val border @Composable get() = MaterialTheme.colorScheme.outlineVariant
    val primary @Composable get() = MaterialTheme.colorScheme.primary
    val selected @Composable get() = MaterialTheme.colorScheme.tertiaryContainer
}

@Composable
fun PlanningHeader(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        PlanningArrow("‹", "Volver", onBack)
        Text(title, Modifier.weight(1f), color = PlanningColors.ink, fontSize = 16.sp,
            lineHeight = 23.sp, fontWeight = FontWeight.Bold)
        Text("coincide.", color = PlanningColors.teal, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PlanningArrow(label: String, description: String, onClick: () -> Unit) {
    OutlinedButton(onClick, Modifier.size(44.dp, 48.dp).semantics { contentDescription = description },
        shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, PlanningColors.border),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = PlanningColors.teal)) {
        Text(label, fontSize = 18.sp)
    }
}

@Composable
fun PlanningCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, PlanningColors.border)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Composable
fun PlanningBadge(label: String) {
    Surface(shape = RoundedCornerShape(16.dp), color = PlanningColors.soft) {
        Text(label, Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = PlanningColors.teal, fontSize = 11.sp, lineHeight = 16.sp)
    }
}

@Composable
fun PlanningAction(label: String, onClick: () -> Unit, primary: Boolean = false, outlined: Boolean = false) {
    if (outlined) {
        OutlinedButton(onClick, Modifier.fillMaxWidth().heightIn(min = 48.dp),
            shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, PlanningColors.border),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PlanningColors.teal)) {
            Text(label, fontSize = 14.sp)
        }
    } else {
        PrimaryButton(label, onClick, containerColor = if (primary) PlanningColors.primary else PlanningColors.soft,
            contentColor = if (primary) MaterialTheme.colorScheme.onPrimary else PlanningColors.teal, cornerRadius = 12.dp)
    }
}

