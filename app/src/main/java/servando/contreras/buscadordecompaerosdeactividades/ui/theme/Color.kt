package servando.contreras.buscadordecompaerosdeactividades.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

// Semantic colors shared by all screens; resolved from the active device theme.
val CoincideBackground @Composable get() = MaterialTheme.colorScheme.background
val CoincidePrimary @Composable get() = MaterialTheme.colorScheme.primary
val CoincideDark @Composable get() = MaterialTheme.colorScheme.onBackground
val CoincideTeal @Composable get() = MaterialTheme.colorScheme.secondary
val CoincideSoft @Composable get() = MaterialTheme.colorScheme.secondaryContainer
val CoincideBorder @Composable get() = MaterialTheme.colorScheme.outlineVariant
val CoincideHint @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
