package com.dev.simeta.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomToast(
    message: String,
    isVisible: Boolean,
    type: ToastType = ToastType.INFO,
    onDismiss: () -> Unit
) {
    // Determine background color based on type
    val backgroundColor = when (type) {
        ToastType.SUCCESS -> Color(0xFFB4E5BC) // Greenish background
        ToastType.ERROR -> Color(0xFFE5BEB4)   // Reddish background
        ToastType.INFO -> Color(0xFFFFF7CA)    // Yellowish background
    }

    // Determine text color based on type
    val textColor = when (type) {
        ToastType.SUCCESS -> Color(0xFF008B44) // Green text
        ToastType.ERROR -> Color(0xFFF72E2E)   // Red text
        ToastType.INFO -> Color(0xFFF0D228)    // Yellow text
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }) + scaleIn(
                initialScale = 0.8f
            ),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }) + scaleOut(
                targetScale = 0.8f
            ), // Add scale-out effect
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 32.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 28.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    color = textColor,
                    fontSize = 16.sp
                )
            }
        }
    }


    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(3000)
            onDismiss()
        }
    }
}

enum class ToastType {
    SUCCESS,
    ERROR,
    INFO
}
