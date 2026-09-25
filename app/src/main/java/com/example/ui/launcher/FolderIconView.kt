package com.example.ui.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.launcher.AppItem
import com.example.data.launcher.FolderItem

@Composable
fun FolderIconPreview(
    folder: FolderItem,
    onClick: () -> Unit,
    size: Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    val cornerRadius = size * 0.26f

    Column(
        modifier = modifier
            .clickable { onClick() }
            .testTag("folder_${folder.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Translucent frosted glass container
        Box(
            modifier = Modifier
                .size(size)
                .shadow(4.dp, RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.White.copy(alpha = 0.28f))
                .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(cornerRadius))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            // 3x3 Mini Icons
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val chunked = folder.apps.take(9).chunked(3)
                for (row in chunked) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (app in row) {
                            AppIconRenderer(
                                app = app,
                                size = size * 0.25f
                            )
                        }
                        // Fill empty spaces in row
                        for (i in 0 until (3 - row.size)) {
                            Spacer(modifier = Modifier.size(size * 0.25f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Label with shadow
        Text(
            text = folder.name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.85f),
                    offset = Offset(0f, 1.5f),
                    blurRadius = 3f
                )
            )
        )
    }
}

@Composable
fun FolderExpandedDialog(
    folder: FolderItem,
    onAppClick: (AppItem) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("expanded_folder_dialog"),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E212B).copy(alpha = 0.94f)
            ),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Folder Title
                Text(
                    text = folder.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Grid of apps inside folder
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(folder.apps, key = { it.id }) { app ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onAppClick(app)
                                    onDismiss()
                                }
                                .padding(4.dp)
                        ) {
                            AppIconRenderer(app = app, size = 56.dp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = app.name,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
