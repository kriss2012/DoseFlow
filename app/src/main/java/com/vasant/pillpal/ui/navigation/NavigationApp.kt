package com.PillPal.ui.navigation

import android.content.Context.MODE_PRIVATE
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.PillPal.ui.screens.AddMedsScreen
import com.PillPal.ui.screens.AuthScreens.SignIn
import com.PillPal.ui.screens.AuthScreens.SignUpScreen
import com.PillPal.ui.screens.AuthScreens.WelcomeScreen
import com.PillPal.ui.screens.AuthScreens.SplashScreen
import com.PillPal.ui.screens.AuthScreens.GuestLoginScreen
import com.PillPal.ui.screens.AboutScreen
import com.PillPal.ui.screens.ChatScreen
import com.PillPal.ui.screens.FeedbackScreen
import com.PillPal.ui.screens.HelpFaqScreen
import com.PillPal.ui.screens.HomeScreen
import com.PillPal.ui.screens.NotificationsScreen
import com.PillPal.ui.screens.SettingsScreen

@Composable
fun NavigationApp(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val prf = context.getSharedPreferences("login", MODE_PRIVATE)
    val isLoggedIn = prf.getBoolean("IS_LOGGED_IN", false)

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.AuthScreens
    ) {
        navigation<NavigationRoute.AuthScreens>(startDestination = AuthenticationRoute.SplashScreen) {

            composable<AuthenticationRoute.SplashScreen> {
                SplashScreen(navController, isLoggedIn)
            }

            composable<AuthenticationRoute.WelcomeScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    )
                }
            ) {
                WelcomeScreen(navController, windowSizeClass = windowSizeClass)
            }

            composable<AuthenticationRoute.LoginScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(900)
                    )
                }
            ) {
                SignIn(navController)
            }

            composable<AuthenticationRoute.SingUpScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(900)
                    )
                }
            ) {
                SignUpScreen(navController, windowSizeClass = windowSizeClass)
            }

            composable<AuthenticationRoute.GuestLoginScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    )
                }
            ) {
                GuestLoginScreen(navController)
            }
        }
        navigation<NavigationRoute.MainScreens>(MainUiRoute.HomeScreen) {
            composable<MainUiRoute.HomeScreen>(enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(700)
                )
            }) {
                HomeScreen(navController)
            }
            composable<MainUiRoute.AddMedicineScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    )
                },
            )
            {
                AddMedsScreen(navController)
            }
            composable<MainUiRoute.ChatScreen> {

                ChatScreen(navController)

            }
            composable<MainUiRoute.NotificationScreen> {

                NotificationsScreen(navController)
            }
            composable<MainUiRoute.SettingScreen> {
                SettingsScreen(navController)
            }
            composable<MainUiRoute.ProfileScreen> { }
            composable<MainUiRoute.AboutScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    )
                }
            ) {
                AboutScreen(navController)
            }
            composable<MainUiRoute.HelpFaqScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    )
                }
            ) {
                HelpFaqScreen(navController)
            }
            composable<MainUiRoute.FeedbackScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    )
                }
            ) {
                FeedbackScreen(navController)
            }
        }

    }


}
