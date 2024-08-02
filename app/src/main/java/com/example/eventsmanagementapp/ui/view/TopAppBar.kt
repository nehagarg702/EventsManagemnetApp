package com.example.eventsmanagementapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: (() -> Unit)? = null,
    onRightButtonClick: (() -> Unit)? = null,
    rightIconImageVector: ImageVector = Icons.Default.Check
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .shadow(4.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (onBackClick != null) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (onRightButtonClick != null) {
            // Display the right-side icon with click action
            IconButton(onClick = { onRightButtonClick() }) {
                Icon(
                    imageVector = rightIconImageVector,
                    contentDescription = "Right Action",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

