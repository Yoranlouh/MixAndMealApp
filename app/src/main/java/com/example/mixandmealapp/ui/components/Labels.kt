package com.example.mixandmealapp.ui.components

import android.R.attr.label
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object GlobalTextLabel {
    val backgroundLabel = Color(0xFFFFFFFF)
    val colorLabelText = Color(0xFF0A2533)
    val labelTextSize = 24.sp
    val labelWidth = 274.dp
    val labelButtonWidth = 81.dp
}

@Composable
fun TextLabel(
    label: String
) {
    var count by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .width(GlobalTextLabel.labelWidth)
            .background(
                color = GlobalTextLabel.backgroundLabel,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Text on the left
        Text(
            text = label,
            color = GlobalTextLabel.colorLabelText,
            fontSize = GlobalTextLabel.labelTextSize,
            fontWeight = FontWeight.SemiBold
        )

        // Buttons row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Minus Button
            Button(
                onClick = { if (count > 0) count-- },
                modifier = Modifier.width(GlobalTextLabel.labelButtonWidth),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC))
            ) {
                Text("-", fontSize = 24.sp, color = Color.Black)
            }

            // Count number
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Plus Button
            Button(
                onClick = { count++ },
                modifier = Modifier.width(GlobalTextLabel.labelButtonWidth),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC))
            ) {
                Text("+", fontSize = 24.sp, color = Color.Black)
            }
        }
    }
}
