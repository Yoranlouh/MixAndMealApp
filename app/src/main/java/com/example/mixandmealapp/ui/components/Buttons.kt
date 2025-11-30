package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

object MixAndMealColours {
    val backgroundButton = Color(0xFF16752D)
    val backgroundButtonAlt = Color(0xFFF17D23)
    val backgroundButtonImportant = Color.Red
    val buttonText = Color.White
    val buttonFontSize = 24.sp
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MixAndMealColours.backgroundButton,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = MixAndMealColours.buttonText
        )
    ) {
        Text(
            text = text,
            fontSize = MixAndMealColours.buttonFontSize
        )
    }
}

@Composable
fun TextOnlyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontSize: androidx.compose.ui.unit.TextUnit = MaterialTheme.typography.bodyLarge.fontSize
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = color
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = fontSize
        )
    }
}



@Composable
fun BackButton(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = ""
        )
    }
}


@Composable
fun CogwheelButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),   // space from the edges
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}

