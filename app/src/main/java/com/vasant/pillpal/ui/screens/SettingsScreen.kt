package com.PillPal.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.PillPal.ui.theme.SecondaryContainerColor
import com.PillPal.ui.theme.jetbrainFamily
import androidx.hilt.navigation.compose.hiltViewModel
import com.PillPal.ui.viewmodel.SettingsViewModel
import com.PillPal.ui.navigation.AuthenticationRoute
import com.PillPal.ui.navigation.MainUiRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val loggedOut by viewModel.loggedOut.collectAsState()

    // Navigate to Login when logout finishes
    LaunchedEffect(loggedOut) {
        if (loggedOut) {
            navController.navigate(AuthenticationRoute.LoginScreen) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp
    val maxWidth = if (isTablet) 800.dp else screenWidth

    val soundPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val picked: Uri? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        }
        viewModel.setSoundUri(picked)
    }

    fun launchRingtonePicker(currentUri: String?) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select notification sound")
            if (currentUri != null) {
                putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(currentUri))
            }
        }
        soundPickerLauncher.launch(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontFamily = jetbrainFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isTablet) 24.sp else 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .widthIn(max = maxWidth),
                contentPadding = PaddingValues(
                    vertical = if (isTablet) 20.dp else 16.dp,
                    horizontal = if (isTablet) 24.dp else 20.dp
                )
            ) {
                // Notifications Section
                item { SectionHeader("Notifications", isTablet) }

                item {
                    SettingSwitchCard(
                        icon = Icons.Outlined.Notifications,
                        title = "Push Notifications",
                        subtitle = "Receive medication reminders",
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { viewModel.setNotificationsEnabled(it) },
                        isTablet = isTablet
                    )
                }

                item {
                    SettingSwitchCard(
                        icon = Icons.Outlined.MusicNote,
                        title = "Reminder Sound",
                        subtitle = "Play sound for reminders",
                        checked = uiState.soundEnabled,
                        onCheckedChange = { viewModel.setSoundEnabled(it) },
                        isTablet = isTablet
                    )
                }

                item {
                    SettingSwitchCard(
                        icon = Icons.Outlined.Vibration,
                        title = "Vibration",
                        subtitle = "Vibrate on reminders",
                        checked = uiState.vibration,
                        onCheckedChange = { viewModel.setVibration(it) },
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.MusicNote,
                        title = "Notification Sound",
                        subtitle = uiState.soundTitle,
                        onClick = { launchRingtonePicker(uiState.soundUri) },
                        isTablet = isTablet
                    )
                }

                // Appearance Section
                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Appearance", isTablet)
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Language,
                        title = "Language",
                        subtitle = "English",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                // Account Section
                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Account", isTablet)
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Person,
                        title = "Edit Profile",
                        subtitle = "Update your personal information",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Lock,
                        title = "Change Password",
                        subtitle = "Update your password",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Security,
                        title = "Privacy & Security",
                        subtitle = "Manage your privacy settings",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                // Data & Storage Section
                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Data & Storage", isTablet)
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.CloudUpload,
                        title = "Backup Data",
                        subtitle = "Backup your medication data",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.CloudDownload,
                        title = "Restore Data",
                        subtitle = "Restore from backup",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Storage,
                        title = "Storage Usage",
                        subtitle = "12.5 MB used",
                        onClick = {},
                        isTablet = isTablet
                    )
                }

                // Support Section
                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Support", isTablet)
                }

                item {
                    SettingClickCard(
                        icon = Icons.AutoMirrored.Outlined.Help,
                        title = "Help & FAQs",
                        subtitle = "Get help using DoseFlow",
                        onClick = { navController.navigate(MainUiRoute.HelpFaqScreen) },
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Feedback,
                        title = "Send Feedback",
                        subtitle = "Share your thoughts with us",
                        onClick = { navController.navigate(MainUiRoute.FeedbackScreen) },
                        isTablet = isTablet
                    )
                }

                item {
                    SettingClickCard(
                        icon = Icons.Outlined.Info,
                        title = "About",
                        subtitle = "Version 1.0.0",
                        onClick = { navController.navigate(MainUiRoute.AboutScreen) },
                        isTablet = isTablet
                    )
                }

                // Logout Section
                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(if (isTablet) 20.dp else 16.dp))
                            .clickable { viewModel.logout() },
                        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(if (isTablet) 20.dp else 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(if (isTablet) 14.dp else 12.dp),
                                color = Color(0xFFEF5350).copy(alpha = 0.2f),
                                modifier = Modifier.size(if (isTablet) 56.dp else 48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                                        contentDescription = null,
                                        tint = Color(0xFFEF5350),
                                        modifier = Modifier.size(if (isTablet) 28.dp else 24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(if (isTablet) 18.dp else 16.dp))

                            Text(
                                text = "Logout",
                                fontFamily = jetbrainFamily,
                                fontSize = if (isTablet) 18.sp else 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFEF5350)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 40.dp else 32.dp))
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, isTablet: Boolean = false) {
    Text(
        text = title,
        fontFamily = jetbrainFamily,
        fontSize = if (isTablet) 20.sp else 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = if (isTablet) 10.dp else 8.dp)
    )
}

@Composable
fun SettingSwitchCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isTablet: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isTablet) 8.dp else 6.dp),
        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isTablet) 20.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(if (isTablet) 14.dp else 12.dp),
                color = SecondaryContainerColor.copy(alpha = 0.15f),
                modifier = Modifier.size(if (isTablet) 56.dp else 48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(if (isTablet) 28.dp else 24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(if (isTablet) 18.dp else 16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 17.sp else 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(if (isTablet) 4.dp else 2.dp))
                Text(
                    text = subtitle,
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = SecondaryContainerColor,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0)
                )
            )
        }
    }
}





@Composable
fun SettingClickCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isTablet: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isTablet) 8.dp else 6.dp)
            .clip(RoundedCornerShape(if (isTablet) 20.dp else 16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isTablet) 20.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(if (isTablet) 14.dp else 12.dp),
                color = SecondaryContainerColor.copy(alpha = 0.15f),
                modifier = Modifier.size(if (isTablet) 56.dp else 48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(if (isTablet) 28.dp else 24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(if (isTablet) 18.dp else 16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 17.sp else 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(if (isTablet) 4.dp else 2.dp))
                Text(
                    text = subtitle,
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(if (isTablet) 28.dp else 24.dp)
            )
        }
    }
}
