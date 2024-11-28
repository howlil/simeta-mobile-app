
package com.dev.simeta.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    remember(message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
