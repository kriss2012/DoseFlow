package com.PillPal.ui.screens

import android.app.AlertDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.PillPal.data.db.MedicineEvent
import androidx.compose.ui.res.painterResource
import com.PillPal.R
import com.PillPal.ui.components.AddMedTop
import com.PillPal.ui.presentation.MedicineState
import com.PillPal.ui.presentation.MedicineType
import com.PillPal.ui.theme.BackgroundColor
import com.PillPal.ui.theme.SecondaryContainerColor
import com.PillPal.ui.theme.jetbrainFamily
import com.PillPal.ui.theme.pillColor
import com.PillPal.ui.viewmodel.MedicineViewModel
import com.PillPal.utils.ALARM_PERMISSION
import com.PillPal.utils.NOTIFICATION_PERMISSION
import com.PillPal.utils.getFormattedTime
import com.PillPal.utils.getTimeInMillis
import com.PillPal.utils.hasPermission
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

@Composable
fun AddMedsScreen(
        navController: NavHostController,
    medicineViewModel: MedicineViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val exactAlarmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM").apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    } else null
    val notificaionIntent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    ).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }

    Scaffold(
        topBar = { AddMedTop(navController) },
        containerColor = BackgroundColor,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            // Fixed CTA Save button
            Surface(
                color = BackgroundColor,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = {
                            val needsExactAlarmPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermission(context, ALARM_PERMISSION)
                            if (needsExactAlarmPermission) {
                                AlertDialog.Builder(context).setTitle("Permission Required")
                                    .setMessage(
                                        "To ensure you receive timely medication reminders Dose-flow needs permission to set precise alarms.Please tap 'Go to Settings' and enable 'Allow setting alarms and reminders' for our app ."
                                    ).setPositiveButton("Go to Settings") { _, _ ->
                                        exactAlarmIntent?.let { context.startActivity(it) }
                                    }.setNegativeButton("Cancel", null)
                                    .show()
                            } else if (!hasPermission(context, NOTIFICATION_PERMISSION)) {
                                AlertDialog.Builder(context).setTitle("Permission Required")
                                    .setMessage(
                                        "To ensure you receive timely medication reminders Dose-flow needs permission to set precise alarms.Please tap 'Go to Settings' and enable 'Allow Notification' for our app ."
                                    ).setPositiveButton("Go to Settings") { _, _ ->
                                        context.startActivity(notificaionIntent)
                                    }.setNegativeButton("Cancel", null)
                                    .show()
                            } else {
                                medicineViewModel.onEvent(MedicineEvent.SaveMedicine(context))
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            "Save Medication",
                            fontFamily = jetbrainFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(BackgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AddMedsInputSection(
                title = "Medicine Name",
                value = medicineViewModel.state.collectAsStateWithLifecycle().value.medicineName,
                onValueChange = { medicineViewModel.onEvent(MedicineEvent.MedicineNameChanged(it)) },
                placeholder = "e.g. Paracetamol",
                icon = R.drawable.framemedicine
            )

            DosageInputRow(state = medicineViewModel.state, onEvent = medicineViewModel::onEvent)

            MedicineTypeSelector(currentState = medicineViewModel.state, onEvent = medicineViewModel::onEvent)

            AddMedsInputSection(
                title = "Notes",
                value = medicineViewModel.state.collectAsStateWithLifecycle().value.note ?: "",
                onValueChange = { medicineViewModel.onEvent(MedicineEvent.NoteChanged(it)) },
                placeholder = "e.g. Take after food",
                singleLine = false,
                minLines = 3,
                icon = R.drawable.guides
            )

            AddTimePill(medicineViewModel)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MedicineTypeSelector(
    currentState: MutableStateFlow<MedicineState>,
    onEvent: (MedicineEvent) -> Unit
) {
    val value = currentState.collectAsStateWithLifecycle()
    val selected = value.value.med_type ?: MedicineType.TABLET

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.medicine),
                    contentDescription = null,
                    tint = SecondaryContainerColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Medicine Type",
                    fontFamily = jetbrainFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MedicineType.entries.forEach { type ->
                    val isSelected = type == selected
                    FilterChip(
                        selected = isSelected,
                        onClick = { onEvent(MedicineEvent.MedicineTypeChanged(type)) },
                        label = {
                            Text(
                                text = type.displayName,
                                fontFamily = jetbrainFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SecondaryContainerColor,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosageInputRow(
    state: MutableStateFlow<MedicineState>,
    onEvent: (MedicineEvent) -> Unit
) {
    val value = state.collectAsStateWithLifecycle()
    val medicineType = value.value.med_type ?: MedicineType.TABLET

    // Auto-determine unit based on medicine type
    val defaultUnit = when (medicineType) {
        MedicineType.TABLET, MedicineType.CAPSULE-> "tablet"
        MedicineType.SYRUP-> "ml"
        MedicineType.DROPS-> "drops"
        MedicineType.OTHERS-> "mg" // default for other
    }

    // Available units based on medicine type
    val availableUnits = when (medicineType) {
        MedicineType.TABLET, MedicineType.CAPSULE-> listOf("tablet")
        MedicineType.SYRUP-> listOf("ml")
        MedicineType.DROPS-> listOf("drops")
        MedicineType.OTHERS-> listOf("mg", "ml", "g") // flexible for "Other"
    }

    val dosageText = value.value.dosage
    val parts = remember(dosageText, defaultUnit) {
        val tokens = dosageText.trim().split(" ").filter { it.isNotBlank() }
        val amount = tokens.firstOrNull { it.any(Char::isDigit) } ?: ""
        val unit = tokens.drop(1).firstOrNull()?.lowercase() ?: defaultUnit
        amount to unit
    }

    var amount by remember(parts) { mutableStateOf(parts.first) }
    var expanded by remember { mutableStateOf(false) }
    var unit by remember(parts, defaultUnit) {
        mutableStateOf(if (availableUnits.contains(parts.second)) parts.second else defaultUnit)
    }

    // Update unit when medicine type changes
    LaunchedEffect(medicineType) {
        unit = defaultUnit
        val cleanAmount = amount.filter { it.isDigit() || it == '.' }
        val composed = if (cleanAmount.isNotEmpty()) "$cleanAmount $unit" else ""
        onEvent(MedicineEvent.AddDosageChange(composed))
    }

    fun pushDosage() {
        val cleanAmount = amount.filter { it.isDigit() || it == '.' }
        val composed = if (cleanAmount.isNotEmpty()) "$cleanAmount $unit" else ""
        onEvent(MedicineEvent.AddDosageChange(composed))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.medicine),
                    contentDescription = null,
                    tint = SecondaryContainerColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dosage",
                    fontFamily = jetbrainFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Amount input
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter { ch -> ch.isDigit() || ch == '.' }
                        pushDosage()
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("0") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SecondaryContainerColor,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                // Unit dropdown
                if (availableUnits.size > 1) {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                        OutlinedTextField(
                            value = unit,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().width(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryContainerColor,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            availableUnits.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt, fontFamily = jetbrainFamily) },
                                    onClick = {
                                        unit = opt
                                        expanded = false
                                        pushDosage()
                                    }
                                )
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.width(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AddTimePill(medicineViewModel: MedicineViewModel) {
    var showTimePicker by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.framemedicine),
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reminder Time",
                        fontFamily = jetbrainFamily,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (time.isEmpty()) "Not set" else time,
                    fontFamily = jetbrainFamily,
                    color = if (time.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else SecondaryContainerColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 28.dp)
                )
            }

            Surface(
                shape = CircleShape,
                color = SecondaryContainerColor.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.framemedicine),
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (showTimePicker) {
            AddTimePickerDialog(onConfirm = { hour, minute ->
                val mili = getTimeInMillis(hour, minute)
                time = getFormattedTime(mili)
                medicineViewModel.onEvent(
                    MedicineEvent.DateChanged(
                        date = getTimeInMillis(
                            hour = hour,
                            minute = minute
                        )
                    )
                )
                showTimePicker = false
            }, onDisMiss = {
                showTimePicker = false
            })
        }
    }
}

@Composable
fun AddMedsInputSection(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    icon: Int? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = SecondaryContainerColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    fontFamily = jetbrainFamily,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = placeholder,
                        fontFamily = jetbrainFamily,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                singleLine = singleLine,
                minLines = minLines,
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = jetbrainFamily),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecondaryContainerColor,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimePickerDialog(
    onConfirm: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    onDisMiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val pickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )
    Dialog(onDismissRequest = { onDisMiss() }) {
        Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 10.dp) {
            Column {
                TimePicker(state = pickerState)
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { onDisMiss() }) { Text("Cancel") }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        onConfirm(
                            pickerState.hour, pickerState.minute
                        )
                    }) { Text("OK") }
                }

            }
        }
    }
}


@Composable
fun CustomButton(title: String, onClick: () -> Unit, color: Color) {
    Card(
        modifier = Modifier
            .padding(34.dp)
            .border(
                width = 1.dp, shape = RoundedCornerShape(11.dp), color = Color.Gray
            )
            .clickable {
                onClick()
            }, colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Text(
            text = title,
            fontFamily = jetbrainFamily,
            color = Color.Black.copy(alpha = 0.8f),
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 15.dp, bottom = 15.dp)
        )

    }
}
