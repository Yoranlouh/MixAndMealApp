package com.example.mixandmealapp.ui.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.mixandmealapp.repository.UserRepository
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.BrandGreen
import kotlinx.coroutines.launch
import java.io.File

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
    backgroundColor: Color = BrandGreen,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .size(height = 56.dp, width = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = MixAndMealColours.buttonText
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EditDeleteButtons(
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Edit button - Green
        Button(
            onClick = onEdit,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandGreen,
                contentColor = MixAndMealColours.buttonText
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Edit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Delete button - Red
        Button(
            onClick = onDelete,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = MixAndMealColours.buttonText
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Delete",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
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
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    IconButton(
        onClick = { 
            if (onClick != null) {
                onClick()
            } else {
                navController.popBackStack()
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = ""
        )
    }
}


//@Composable
//fun CogwheelButton(onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),   // space from the edges
//    ) {
//        IconButton(
//            onClick = onClick,
//            modifier = Modifier.align(Alignment.TopEnd)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Settings,
//                contentDescription = "Settings"
//            )
//        }
//    }
//}

// Fridge label component with an orange trash button on the right.
@Composable
fun Labels(
    label: String,
    modifier: Modifier = Modifier,
    // Do not auto-hide by default. Let the parent state drive UI removal to avoid
    // visual double-removal when the backing list also updates after onRemove.
    autoHideOnRemove: Boolean = false,
    onRemove: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(true) }
    if (!visible) return

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0A2533)
            )

            IconButton(
                onClick = {
                    onRemove?.invoke()
                    if (autoHideOnRemove) visible = false
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Remove",
                    tint = BrandOrange
                )
            }
        }
    }
}


@Composable
fun SettingsButton(
    title: String,
    description: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        supportingContent = {
            if (description != null) {
                Text(text = description)
            }
        },
        trailingContent = trailingContent,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
    Divider()
}

@Composable
fun LogoutButton(
    title: String,
    description: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                color = Color.Red, // White text for better contrast on red
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            if (description != null) {
                Text(
                    text = description,
                    color = Color.Red.copy(alpha = 0.8f) // Slightly transparent white
                )
            }
        },
        trailingContent = trailingContent,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
    Divider()
}


@Composable
fun OpenFridgeButton(
    text: String = "Open fridge",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // Delegate to the shared PrimaryButton to keep styling consistent with UploadScreen
    PrimaryButton(
        text = text,
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
fun SingleChoiceSegmentedButton(
    modifier: Modifier = Modifier,
    title: String,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit  // Lambda callback, not a function returning Int
) {
    val options = listOf("<10", "15", "30", "45", ">60")

    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, optionTitle ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { onOptionSelected(index) },  // Call parent's callback
                    selected = index == selectedIndex,
                    label = { Text(optionTitle) }
                )
            }
        }
    }
}




