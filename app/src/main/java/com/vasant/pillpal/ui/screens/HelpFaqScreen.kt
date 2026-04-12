package com.PillPal.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.PillPal.ui.theme.BackgroundColor
import com.PillPal.ui.theme.SecondaryContainerColor
import com.PillPal.ui.theme.fontColor
import com.PillPal.ui.theme.jetbrainFamily

data class FaqItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpFaqScreen(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp

    val faqItems = listOf(
        FaqItem(
            "How do I add a medication?",
            "Tap the '+' button at the bottom of the home screen. Fill in the medication name, dosage, type, and time, then tap 'Save Medication'."
        ),
        FaqItem(
            "How do I set reminders?",
            "When adding a medication, select the time you need to take it. DoseFlow will automatically send you a notification at that time."
        ),
        FaqItem(
            "Can I mark a medication as taken?",
            "Yes! Swipe the medication card to the right on the home screen to mark it as completed."
        ),
        FaqItem(
            "How do I delete a medication?",
            "Swipe the medication card to the left on the home screen to delete it."
        ),
        FaqItem(
            "What is the Medical Assistant?",
            "The Medical Assistant is an AI-powered feature that can answer questions about your medications, side effects, and general health advice."
        ),
        FaqItem(
            "Is my data safe?",
            "Yes, your medication data is stored locally on your device. We do not share your personal health information with third parties."
        ),
        FaqItem(
            "How do I change notification sounds?",
            "Go to Settings > Notification Sound to choose a custom sound for your medication reminders."
        ),
        FaqItem(
            "Can I back up my data?",
            "Go to Settings > Backup Data to save your medication information. You can restore it anytime using the Restore Data option."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Help & FAQs",
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
                    HelpHeaderCard(isTablet)
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Frequently Asked Questions", isTablet)
                }

                items(faqItems.size) { index ->
                    FaqAccordionItem(
                        faqItem = faqItems[index],
                        isTablet = isTablet
                    )
                    Spacer(modifier = Modifier.height(if (isTablet) 10.dp else 8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 28.dp else 24.dp))
                    SectionHeader("Need More Help?", isTablet)
                }

                item {
                    HelpContactCard(
                        icon = Icons.Outlined.Email,
                        title = "Email Support",
                        subtitle = "vasant04@gmail.com",
                        isTablet = isTablet
                    )
                }

                item {
                    HelpContactCard(
                        icon = Icons.Outlined.Chat,
                        title = "Chat with Assistant",
                        subtitle = "Use our AI Medical Assistant",
                        isTablet = isTablet
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(if (isTablet) 40.dp else 32.dp))
                }
            }
        }
    }
}

@Composable
private fun HelpHeaderCard(isTablet: Boolean) {
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
                        imageVector = Icons.Outlined.HelpOutline,
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(if (isTablet) 36.dp else 30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isTablet) 20.dp else 16.dp))

            Text(
                text = "How can we help you?",
                fontFamily = jetbrainFamily,
                fontSize = if (isTablet) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = fontColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Find answers to common questions\nabout using DoseFlow",
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
private fun FaqAccordionItem(
    faqItem: FaqItem,
    isTablet: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(if (isTablet) 20.dp else 16.dp))
                .clickable { expanded = !expanded }
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
                    modifier = Modifier.size(if (isTablet) 44.dp else 40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.QuestionAnswer,
                            contentDescription = null,
                            tint = SecondaryContainerColor,
                            modifier = Modifier.size(if (isTablet) 24.dp else 20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(if (isTablet) 16.dp else 12.dp))

                Text(
                    text = faqItem.question,
                    fontFamily = jetbrainFamily,
                    fontSize = if (isTablet) 16.sp else 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fontColor,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = SecondaryContainerColor,
                    modifier = Modifier.size(if (isTablet) 28.dp else 24.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = androidx.compose.animation.expandVertically(
                    animationSpec = tween(300),
                    expandFrom = Alignment.Top
                ),
                exit = androidx.compose.animation.shrinkVertically(
                    animationSpec = tween(300),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = if (isTablet) 80.dp else 68.dp,
                        end = if (isTablet) 20.dp else 16.dp,
                        bottom = if (isTablet) 20.dp else 16.dp
                    )
                ) {
                    HorizontalDivider(
                        color = fontColor.copy(alpha = 0.1f),
                        modifier = Modifier.padding(bottom = if (isTablet) 12.dp else 8.dp)
                    )
                    Text(
                        text = faqItem.answer,
                        fontFamily = jetbrainFamily,
                        fontSize = if (isTablet) 14.sp else 13.sp,
                        color = fontColor.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun HelpContactCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isTablet: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isTablet) 8.dp else 6.dp)
            .clip(RoundedCornerShape(if (isTablet) 20.dp else 16.dp))
            .clickable {},
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
