package pl.finitas.app.manage_spendings_feature.presentation.add_spending.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import pl.finitas.app.core.presentation.components.ClickableIcon
import pl.finitas.app.manage_spendings_feature.presentation.add_spending.AddSpendingViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

private const val APPLICATION_ID = "pl.finitas.app"

@Composable
fun ScanReceiptIcon(viewModel: AddSpendingViewModel) {
    val context = LocalContext.current
    val uri = context.fileUri()

    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        }
    }

    CameraIcon(viewModel, context) {
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (capturedImageUri.path?.isNotEmpty() == true) {
        context.contentResolver.openInputStream(capturedImageUri)?.use {
            viewModel.processImage(it.readBytes())
        }
        capturedImageUri = Uri.EMPTY
    }
}

@Composable
private fun CameraIcon(
    viewModel: AddSpendingViewModel,
    context: Context,
    onClick: () -> Unit,
) {
    if (viewModel.finishedSpendingState.categories.isNotEmpty()) {
        ClickableIcon(
            imageVector = Icons.Rounded.CameraAlt,
            onClick = onClick,
        )
    } else {
        ClickableIcon(
            imageVector = Icons.Rounded.CameraAlt,
            onClick = {
                // todo: dania - other validation?
                Toast.makeText(
                    context,
                    "You need to add at least on category",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}

private fun Context.fileUri(): Uri {
    val timeStamp = SimpleDateFormat.getTimeInstance().format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val file = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return FileProvider.getUriForFile(
        Objects.requireNonNull(this), "$APPLICATION_ID.provider", file
    )
}


