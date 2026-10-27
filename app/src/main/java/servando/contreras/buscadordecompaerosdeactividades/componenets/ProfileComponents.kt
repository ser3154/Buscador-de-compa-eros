package servando.contreras.buscadordecompaerosdeactividades.componenets

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Avatar circular: muestra la foto si existe y, si no, las iniciales (como en el diseño). */
@Composable
fun ProfileAvatar(initials: String, photo: ImageBitmap?, size: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier.size(size).clip(CircleShape).background(PlanningColors.soft),
        contentAlignment = Alignment.Center
    ) {
        if (photo != null) {
            Image(photo, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        } else {
            Text(initials, fontSize = (size.value * 0.31f).sp, fontWeight = FontWeight.Bold, color = PlanningColors.teal)
        }
    }
}

/** Carga la foto elegida en el selector de fotos, reducida para no gastar memoria. */
@Composable
fun rememberProfilePhoto(uri: String?): ImageBitmap? {
    val context = LocalContext.current
    val photo by produceState<ImageBitmap?>(initialValue = null, uri) {
        value = if (uri.isNullOrEmpty()) null else withContext(Dispatchers.IO) { decodePhoto(context, Uri.parse(uri)) }
    }
    return photo
}

private fun decodePhoto(context: Context, uri: Uri, targetPx: Int = 320): ImageBitmap? = runCatching {
    val resolver = context.contentResolver
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri)) { decoder, info, _ ->
            decoder.setTargetSampleSize(maxOf(1, minOf(info.size.width, info.size.height) / targetPx))
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }.asImageBitmap()
    } else {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        var sample = 1
        while (minOf(bounds.outWidth, bounds.outHeight) / (sample * 2) >= targetPx) sample *= 2
        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }?.asImageBitmap()
    }
}.getOrNull()

/** Abre el menú de compartir de Android con un texto. */
fun shareText(context: Context, text: String, title: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(send, title))
}
