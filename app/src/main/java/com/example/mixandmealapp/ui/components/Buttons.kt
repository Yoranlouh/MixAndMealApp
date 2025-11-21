package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object MixAndMealColours {
    val backgroundButton = Color(0xFF16752D)
    val backgroundButtonAlt = Color(0xFFF17D23)
    val buttonText = Color.White
    val buttonFontSize = 24.sp
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MixAndMealColours.backgroundButton,
            contentColor = MixAndMealColours.buttonText
        )
    ) {
        Text(
            text = text,
            fontSize = MixAndMealColours.buttonFontSize
        )
    }
}
