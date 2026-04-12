package com.PillPal.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.PillPal.ui.theme.BackgroundColor
import com.PillPal.ui.theme.SecondaryContainerColor
import com.PillPal.ui.theme.fontColor
import com.PillPal.ui.theme.jetbrainFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp
    val context = LocalContext.current

    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var showSuccess by remember { mutableStateOf(false) }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = {
                Text(
                    text = "Thank You!",
                    fontFamily = jetbrainFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Your feedback has been sent successfully. We appreciate your input!",
                    fontFamily = jetbrainFamily
                )
            },
            confirmButton = {
                TextButton(onClick = { showSuccess = false }) {
                    Text(
                        "OK",
                        fontFamily = jetbrainFamily,
                        color = SecondaryContainerColor
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Send Feedback",
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
                )
            ) {
                item {
                    FeedbackHeaderCard(isTablet)
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Rate Your Experience", isTablet)
                }

                item {
                    RatingCard(
                        rating = rating,
                        onRatingChange = { rating = it },
                        isTablet = isTablet
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Your Feedback", isTablet)
                }

                item {
                    FeedbackInputCard(
                        subject = subject,
                        onSubjectChange = { subject = it },
                        message = message,
                        onMessageChange = { message = it },
                        isTablet = isTablet
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                }

                item {
                    Button(
                        onClick = {
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:vasant04@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "[DoseFlow Feedback] $subject")
                                putExtra(Intent.EXTRA_TEXT, "Rating: ${rating}/5 stars\n\n$message")
                            }
                            try {
                                context.startActivity(Intent.createChooser(emailIntent, "Send feedback via"))
                            } catch (e: Exception) {
                                showSuccess = true
                            }

                            subject = ""
                            message = ""
                            rating = 0
                            showSuccess = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isTablet) 60.dp else 56.dp),
                        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryContainerColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Send Feedback",
                            fontFamily = jetbrainFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isTablet) 18.sp else 16.sp
                        )
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
private fun FeedbackHeaderCard(isTablet: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTablet) 24.dp else 20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isTablet) 28.dp else 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
                color = SecondaryContainerColor.copy(alpha = 0.15f),
                modifier = Modifier.size(if (isTablet) 72.dp else 60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Feedback,
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(if (isTablet) 36.dp else 30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isTablet) 20.dp else 16.dp))

            Text(
                text = "We value your feedback!",
                fontFamily = jetbrainFamily,
                fontSize = if (isTablet) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = fontColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Help us improve DoseFlow by sharing\nyour thoughts and suggestions",
                fontFamily = jetbrainFamily,
                fontSize = if (isTablet) 14.sp else 12.sp,
                color = fontColor.copy(alpha = 0.6f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun RatingCard(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    isTablet: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isTablet) 20.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How would you rate DoseFlow?",
                fontFamily = jetbrainFamily,
                fontSize = if (isTablet) 16.sp else 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = fontColor
            )

            Spacer(modifier = Modifier.height(if (isTablet) 16.dp else 12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(if (isTablet) 8.dp else 4.dp)
            ) {
                for (i in 1..5) {
                    IconButton(
                        onClick = { onRatingChange(i) },
                        modifier = Modifier.size(if (isTablet) 56.dp else 48.dp)
                    ) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "$i star",
                            tint = if (i <= rating) SecondaryContainerColor else fontColor.copy(alpha = 0.2f),
                            modifier = Modifier.size(if (isTablet) 40.dp else 32.dp)
                        )
                    }
                }
            }

            if (rating > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (rating) {
                        1 -> "Poor"
                        2 -> "Fair"
                        3 -> "Good"
                        4 -> "Very Good"
                        5 -> "Excellent"
                        else -> ""
                    },
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 15.sp else 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecondaryContainerColor
                )
            }
        }
    }
}

@Composable
private fun FeedbackInputCard(
    subject: String,
    onSubjectChange: (String) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    isTablet: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isTablet) 20.dp else 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(if (isTablet) 14.dp else 12.dp),
                    color = SecondaryContainerColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(if (isTablet) 44.dp else 40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = SecondaryContainerColor,
                            modifier = Modifier.size(if (isTablet) 24.dp else 20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(if (isTablet) 16.dp else 12.dp))

                Text(
                    text = "Tell us more",
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 17.sp else 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fontColor
                )
            }

            Spacer(modifier = Modifier.height(if (isTablet) 20.dp else 16.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = onSubjectChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Subject",
                        fontFamily = jetbrainFamily,
                        color = fontColor.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(if (isTablet) 16.dp else 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecondaryContainerColor,
                    unfocusedBorderColor = fontColor.copy(alpha = 0.2f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = SecondaryContainerColor,
                    focusedTextColor = fontColor,
                    unfocusedTextColor = fontColor
                ),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 16.sp else 14.sp
                )
            )

            Spacer(modifier = Modifier.height(if (isTablet) 16.dp else 12.dp))

            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isTablet) 180.dp else 140.dp),
                label = {
                    Text(
                        "Your feedback...",
                        fontFamily = jetbrainFamily,
                        color = fontColor.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(if (isTablet) 16.dp else 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecondaryContainerColor,
                    unfocusedBorderColor = fontColor.copy(alpha = 0.2f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = SecondaryContainerColor,
                    focusedTextColor = fontColor,
                    unfocusedTextColor = fontColor
                ),
                maxLines = 6,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 16.sp else 14.sp
                )
            )
        }
    }
}

