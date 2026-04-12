package com.PillPal.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.PillPal.R
import com.PillPal.ui.navigation.MainUiRoute
import com.PillPal.ui.theme.jetbrainFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedTop(
    navController: NavHostController
) {
    CenterAlignedTopAppBar(
        title = {
                Text(
                    text = "Add Medication",
                    fontFamily = jetbrainFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium
                )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(MainUiRoute.HomeScreen) }) {
                Icon(
                    painter = painterResource(id = R.drawable.backarrow),
                    contentDescription = "Back Icon"

                )
            }
        },
        colors  = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddMedTopPreview() {
    val navController = rememberNavController()
    AddMedTop(navController = navController)
}
