package com.PillPal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.PillPal.R
import com.PillPal.data.db.Medicine
import com.PillPal.data.db.MedicineEvent
import com.PillPal.ui.navigation.MainUiRoute
import com.PillPal.ui.presentation.MedicineType
import com.PillPal.ui.theme.SecondaryContainerColor
import com.PillPal.ui.theme.jetbrainFamily
import com.PillPal.ui.viewmodel.MedicineViewModel
import com.PillPal.utils.getFormattedTime
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeContent(
    navController: NavHostController,
    padding: PaddingValues,
    medicineViewModel: MedicineViewModel = hiltViewModel()
) {
    val medicine = medicineViewModel.meds.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header Section
            item {
                GreetingSection()
            }

            // Quick Stats Section
            item {
                QuickStatsSection(medicine.value)
            }

            // Medicine List Section Header
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Medications",
                            fontFamily = jetbrainFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = getCurrentDate(),
                            fontFamily = jetbrainFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    if (medicine.value.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SecondaryContainerColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${medicine.value.size} meds",
                                fontFamily = jetbrainFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = SecondaryContainerColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Medicine List or Empty State
            if (medicine.value.isEmpty()) {
                item {
                    EmptyStateSection(navController)
                }
            } else {
                items(medicine.value, key = { meds -> meds.id }) { data ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            when (it) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    medicineViewModel.onEvent(MedicineEvent.DeleteMedicine(data))
                                    true
                                }
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    medicineViewModel.onEvent(MedicineEvent.PendingMedicine(data))
                                    false
                                }
                                else -> false
                            }
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            SwipeDismissBackground(dismissState)
                        }
                    ) {
                        MedicinePillSection(
                            date = getFormattedTime(data.time),
                            medicineName = data.medName,
                            dosage = data.dosage,
                            isCompleted = data.isCompleted,
                            medType = data.medType,
                            notes = data.note ?: ""
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun GreetingSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                SecondaryContainerColor,
                                SecondaryContainerColor.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = getGreeting(),
                            fontFamily = jetbrainFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Stay Healthy! 💊",
                            fontFamily = jetbrainFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Track your medications easily",
                            fontFamily = jetbrainFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    // Notification Icon
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.25f),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatsSection(medicines: List<Medicine>) {
    val completedCount = medicines.count { it.isCompleted }
    val pendingCount = medicines.size - completedCount
    val completionRate = if (medicines.isNotEmpty()) {
        (completedCount.toFloat() / medicines.size * 100).toInt()
    } else 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Completed Card
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.CheckCircle,
            value = "$completedCount",
            label = "Completed",
            color = Color(0xFF4CAF50),
            backgroundColor = Color(0xFFE8F5E9)
        )

        // Pending Card
        StatCard(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.framemedicine),
            value = "$pendingCount",
            label = "Pending",
            color = Color(0xFFFF9800),
            backgroundColor = Color(0xFFFFF3E0)
        )

        // Progress Card
        StatCard(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.medicine),
            value = "$completionRate%",
            label = "Progress",
            color = SecondaryContainerColor,
            backgroundColor = SecondaryContainerColor.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    painter: androidx.compose.ui.graphics.painter.Painter? = null,
    value: String,
    label: String,
    color: Color,
    backgroundColor: Color
) {
    Card(
        modifier = modifier.aspectRatio(0.85f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.15f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(22.dp)
                        )
                    } else if (painter != null) {
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                fontFamily = jetbrainFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color.copy(alpha = 0.9f)
            )
            Text(
                text = label,
                fontFamily = jetbrainFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyStateSection(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon Container
                Surface(
                    shape = CircleShape,
                    color = SecondaryContainerColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.medicine),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = SecondaryContainerColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "No Medications Yet",
                    fontFamily = jetbrainFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Start your health journey by adding your first medication. We'll help you stay on track!",
                    fontFamily = jetbrainFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate(MainUiRoute.AddMedicineScreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryContainerColor
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Add Your First Medication",
                        fontFamily = jetbrainFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeDismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.targetValue) {
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFEF5350)
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF66BB6A)
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color),
        contentAlignment = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Icon(
            imageVector = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                Icons.Default.Delete
            } else {
                Icons.Filled.CheckCircle
            },
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .size(32.dp)
        )
    }
}

@Composable
fun MedicinePillSection(
    date: String,
    medicineName: String,
    dosage: String,
    isCompleted: Boolean = false,
    medType: MedicineType? = null,
    notes: String = ""
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = if (isCompleted) 1.dp else 3.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (isCompleted) Color.Transparent else SecondaryContainerColor.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                Color(0xFFF1F8F4)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Medicine Icon with Type-based design
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isCompleted) {
                    Color(0xFF66BB6A).copy(alpha = 0.15f)
                } else {
                    getMedicineTypeColor(medType).copy(alpha = 0.12f)
                },
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(getMedicineTypeIcon(medType)),
                        contentDescription = "Medicine Icon",
                        modifier = Modifier.size(32.dp),
                        tint = if (isCompleted) {
                            Color(0xFF4CAF50)
                        } else {
                            getMedicineTypeColor(medType)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Medicine Details
            Column(modifier = Modifier.weight(1f)) {
                // Medicine Name
                Text(
                    text = medicineName,
                    fontFamily = jetbrainFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (isCompleted) {
                        Color(0xFF2C2C2C).copy(alpha = 0.7f)
                    } else {
                        Color(0xFF1A1A1A)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Time and Dosage Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Time
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCompleted) {
                            Color(0xFF66BB6A).copy(alpha = 0.12f)
                        } else {
                            Color(0xFFF5F5F5)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.framemedicine),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (isCompleted) Color(0xFF4CAF50) else Color(0xFF757575)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = date,
                                fontFamily = jetbrainFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isCompleted) Color(0xFF4CAF50) else Color(0xFF424242)
                            )
                        }
                    }

                    // Dosage
                    if (dosage.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = getMedicineTypeColor(medType).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = dosage,
                                fontFamily = jetbrainFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = getMedicineTypeColor(medType),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Medicine Type and Notes
                if (medType != null || notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        medType?.let {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = getMedicineTypeColor(it).copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = it.displayName,
                                    fontFamily = jetbrainFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = getMedicineTypeColor(it),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        if (notes.isNotBlank()) {
                            Text(
                                text = "• $notes",
                                fontFamily = jetbrainFamily,
                                fontSize = 11.sp,
                                color = Color(0xFF9E9E9E),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Status Indicator
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isCompleted) {
                    Color(0xFF66BB6A)
                } else {
                    Color(0xFFFF9800)
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isCompleted) "Done" else "Pending",
                        fontFamily = jetbrainFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Helper Functions
private fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
    return sdf.format(Date())
}

private fun getMedicineTypeColor(type: MedicineType?): Color {
    return when (type) {
        MedicineType.TABLET -> Color(0xFF5C6BC0)
        MedicineType.CAPSULE -> Color(0xFFEC407A)
        MedicineType.SYRUP -> Color(0xFF42A5F5)
        MedicineType.DROPS -> Color(0xFF26A69A)
        MedicineType.OTHERS -> Color(0xFF78909C)
        null -> SecondaryContainerColor
    }
}

private fun getMedicineTypeIcon(type: MedicineType?): Int {
    return when (type) {
        MedicineType.TABLET, MedicineType.CAPSULE -> R.drawable.framemedicine
        MedicineType.SYRUP, MedicineType.DROPS -> R.drawable.medicine
        MedicineType.OTHERS -> R.drawable.framemedicine
        null -> R.drawable.framemedicine
    }
}
