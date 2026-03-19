package com.example.coollib.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.coollib.R
import com.example.coollib.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    isLoggedIn: Boolean,
    userName: String? = null,
    onLogout: () -> Unit = {},
    onScanClick: () -> Unit
) {

    val context = LocalContext.current
    var showMenuSheet by remember { mutableStateOf(false) }

    TopBarContent(
        appName = R.string.app_name,
        scrollBehavior = scrollBehavior,
        showSheet = showMenuSheet,
        onToggleSheet = { showMenuSheet = it },
        isLoggedIn = isLoggedIn,
        userName = userName,
        onScanClick = onScanClick,
        onMenuAction = { action ->

            when (action) {

                "LOGIN" ->
                    navController.navigate(Screen.Login.route)

                "PROFILE" ->
                    navController.navigate("profile")

                "MY_LOANS" ->
                    navController.navigate(Screen.Loan.createRoute("borrowed"))

                "HISTORY" ->
                    navController.navigate(Screen.Loan.createRoute("history"))

                "ABOUT" ->
                    navController.navigate("about")

                "LOGOUT" -> {
                    onLogout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(
    modifier: Modifier = Modifier,
    @StringRes appName: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    showSheet: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    isLoggedIn: Boolean,
    userName: String?,
    onScanClick: () -> Unit,
    onMenuAction: (String) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(stringResource(appName)) },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            IconButton(
                onClick = { onScanClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scan",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .clickable {
                        onToggleSheet(true)
                    },
                contentScale = ContentScale.Crop
            )
        }
    )

    if (showSheet) {

        ModalBottomSheet(
            onDismissRequest = { onToggleSheet(false) },
            sheetState = sheetState
        ) {

            FullScreenMenuContent(
                isLoggedIn = isLoggedIn,
                userName = userName
            ) { action ->

                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {

                    onToggleSheet(false)
                    onMenuAction(action)

                }
            }
        }
    }
}

@Composable
private fun FullScreenMenuContent(
    isLoggedIn: Boolean,
    userName: String?,
    onItemClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        UserInfoHeader(isLoggedIn, userName)

        HorizontalDivider()

        MenuListItem(Icons.AutoMirrored.Filled.List, "My Loans") {
            onItemClick("MY_LOANS")
        }

        MenuListItem(Icons.Default.BookmarkBorder, "Reservations") {
            onItemClick("RESERVATIONS")
        }

        MenuListItem(Icons.Default.History, "History") {
            onItemClick("HISTORY")
        }

        HorizontalDivider()

        MenuListItem(Icons.Default.Person, "Profile") {
            onItemClick("PROFILE")
        }

        MenuListItem(Icons.Default.Settings, "Settings") {
            onItemClick("SETTINGS")
        }

        MenuListItem(Icons.Default.Info, "About") {
            onItemClick("ABOUT")
        }

        HorizontalDivider()

        if (isLoggedIn) {

            MenuListItem(
                Icons.AutoMirrored.Filled.ExitToApp,
                "Logout",
                Color.Red
            ) {
                onItemClick("LOGOUT")
            }

        } else {

            MenuListItem(
                Icons.Default.Person,
                "Login"
            ) {
                onItemClick("LOGIN")
            }

        }
    }
}

@Composable
private fun UserInfoHeader(
    isLoggedIn: Boolean,
    userName: String?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                )
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = userName ?: "Guest User",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (!isLoggedIn) {

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Please login to access your library",
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun MenuListItem(
    icon: ImageVector,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {

    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = {
            Text(text, color = textColor)
        },
        leadingContent = {
            Icon(
                icon,
                contentDescription = null,
                tint = if (textColor == Color.Red)
                    Color.Red
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    )
}
