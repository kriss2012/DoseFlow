package com.PillPal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.PillPal.R
import com.PillPal.ui.theme.BackgroundColor
import com.PillPal.ui.theme.SecondaryContainerColor
import com.PillPal.ui.theme.fontColor
import com.PillPal.ui.theme.jetbrainFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "About",
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
                            tint = fontColor
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
                .background(BackgroundColor)
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .widthIn(max = if (isTablet) 800.dp else screenWidth),
                contentPadding = PaddingValues(
                    vertical = if (isTablet) 20.dp else 16.dp,
                    horizontal = if (isTablet) 24.dp else 20.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AppLogoSection(isTablet)
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("App Info", isTablet)
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.Info,
                        title = "Version",
                        subtitle = "1.0.0",
                        isTablet = isTablet
                    )
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.Update,
                        title = "Last Updated",
                        subtitle = "April 2026",
                        isTablet = isTablet
                    )
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.Code,
                        title = "Built With",
                        subtitle = "Kotlin & Jetpack Compose",
                        isTablet = isTablet
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Legal", isTablet)
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.PrivacyTip,
                        title = "Privacy Policy",
                        subtitle = "How we handle your data",
                        isTablet = isTablet
                    )
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.Description,
                        title = "Terms of Service",
                        subtitle = "Our terms and conditions",
                        isTablet = isTablet
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Connect", isTablet)
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.Email,
                        title = "Email Us",
                        subtitle = "vasant04@gmail.com",
                        isTablet = isTablet
                    )
                }

                item {
                    AboutInfoCard(
                        icon = Icons.Outlined.Star,
                        title = "Rate DoseFlow",
                        subtitle = "Share your feedback on the store",
                        isTablet = isTablet
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 40.dp else 32.dp))

                    Text(
                        text = "Made with care for your health",
                        fontFamily = jetbrainFamily,
                        fontSize = if (isTablet) 14.sp else 12.sp,
                        color = fontColor.copy(alpha = 0.4f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(if (isTablet) 40.dp else 32.dp))
                }
            }
        }
    }
}

@Composable
private fun AppLogoSection(isTablet: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTablet) 24.dp else 20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SecondaryContainerColor,
                            SecondaryContainerColor.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(if (isTablet) 32.dp else 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(if (isTablet) 100.dp else 80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.medicine),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(if (isTablet) 56.dp else 44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (isTablet) 20.dp else 16.dp))

                Text(
                    text = "DoseFlow",
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 36.sp else 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Your Health Companion",
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 16.sp else 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(if (isTablet) 16.dp else 12.dp))

                Text(
                    text = "Never miss a dose again. DoseFlow helps\nyou track medications, get reminders,\nand stay on top of your health.",
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun AboutInfoCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
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
                    color = fontColor
                )
                Spacer(modifier = Modifier.height(if (isTablet) 4.dp else 2.dp))
                Text(
                    text = subtitle,
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    color = fontColor.copy(alpha = 0.6f)
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = fontColor.copy(alpha = 0.4f),
                modifier = Modifier.size(if (isTablet) 28.dp else 24.dp)
            )
        }
    }
}
