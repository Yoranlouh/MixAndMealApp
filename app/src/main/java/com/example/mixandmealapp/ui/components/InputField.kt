package com.example.mixandmealapp.ui.components

import android.R.attr.description
import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.R
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.DarkText

@Composable
fun InputFieldSmall(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
) {
    Column(modifier = Modifier) {

        // Label above the text field
        Text(
            text = label,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(8.dp))

        // The text box
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text(text = placeholder) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun InputFieldTextBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
) {
    Column(modifier = Modifier) {

        Text(
            text = label,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text(text = placeholder) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun InputTextFieldLogin(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}

