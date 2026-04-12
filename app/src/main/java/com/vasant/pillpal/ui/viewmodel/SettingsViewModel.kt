package com.PillPal.ui.viewmodel

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.PillPal.data.sharedPref.NotificationPrefs
import com.PillPal.repository.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: NotificationPrefs,
    @ApplicationContext private val context: Context,
    private val auth: Auth
) : ViewModel() {

    data class SettingsUiState(
        val notificationsEnabled: Boolean,
        val soundEnabled: Boolean,
        val vibration: Boolean,
        val soundUri: String?,
        val soundTitle: String
    )

    private val _loggedOut = MutableStateFlow(false)
    val loggedOut = _loggedOut.asStateFlow()

    val uiState: StateFlow<SettingsUiState> = combine(
        prefs.enabled,
        prefs.soundEnabled,
        prefs.vibration,
        prefs.soundUri
    ) { enabled: Boolean, soundEnabled: Boolean, vibration: Boolean, uriStr: String? ->
        val title = uriStr?.let { uriString ->
            try {
                val uri = Uri.parse(uriString)
                val r = RingtoneManager.getRingtone(context, uri)
                r?.getTitle(context) ?: DEFAULT_SOUND_TITLE
            } catch (e: Exception) { DEFAULT_SOUND_TITLE }
        } ?: DEFAULT_SOUND_TITLE
        SettingsUiState(
            notificationsEnabled = enabled,
            soundEnabled = soundEnabled,
            vibration = vibration,
            soundUri = uriStr,
            soundTitle = title
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState(true, true, false, null, DEFAULT_SOUND_TITLE)
    )

    fun setNotificationsEnabled(value: Boolean) = prefs.setEnabled(value)
    fun setSoundEnabled(value: Boolean) = prefs.setSoundEnabled(value)
    fun setVibration(value: Boolean) = prefs.setVibration(value)
    fun setSoundUri(uri: Uri?) = prefs.setSoundUri(uri?.toString())

    fun logout() {
        viewModelScope.launch {
            auth.LogOut()
            // Clear app login flag so NavigationApp picks AuthScreens on next start
            val prf = context.getSharedPreferences("login", MODE_PRIVATE)
            prf.edit().putBoolean("IS_LOGGED_IN", false).apply()
            _loggedOut.value = true
        }
    }

    companion object {
        const val DEFAULT_SOUND_TITLE = "Default notification sound"
    }
}
