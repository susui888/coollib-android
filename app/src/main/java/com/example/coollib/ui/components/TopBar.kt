package com.example.coollib.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import com.example.coollib.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior:TopAppBarScrollBehavior,
    onScanClick: () -> Unit,
    onProfileClick: () -> Unit = {}
) {

    TopAppBar(

        title = {
            Text("CoolLib")
        },

        scrollBehavior = scrollBehavior,

        actions = {

            IconButton(onClick = { onScanClick }) {
                Icon(
                    imageVector = Icons.Filled.QrCodeScanner,
                    contentDescription = "Scan"
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Share"
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onProfileClick() }
            )
        }
    )
}