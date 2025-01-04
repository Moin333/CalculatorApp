package com.example.calculator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class ConverterType(val icon: androidx.compose.ui.graphics.vector.ImageVector, val name: String)

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun UnitConverterPage(navController: NavHostController){

    val context = LocalContext.current
    val activity = context as? Activity

    // Lock orientation to portrait when this page is displayed
    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Unlock orientation when leaving the page (only when navigating back)
        onDispose {
            // Check if the current destination is null (indicating a back navigation)
            val currentBackStackEntry = navController.currentBackStackEntry
            if (currentBackStackEntry == null) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }

    val converters = listOf(
        ConverterType(Icons.Outlined.CurrencyExchange, "Currency"),
        ConverterType(Icons.Outlined.Straighten, "Length"),
        ConverterType(Icons.Outlined.GridOn, "Area"),
        ConverterType(Icons.Outlined.Speed, "Speed"),
        ConverterType(Icons.Outlined.FitnessCenter, "Weight"),
        ConverterType(Icons.Outlined.Thermostat, "Temperature"),
        ConverterType(Icons.Outlined.Bolt, "Power"),
        ConverterType(Icons.Outlined.Compress, "Pressure"),
        ConverterType(Icons.Outlined.LocalDrink, "Volume"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable{
                        navController.popBackStack()
                    },
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Unit Converter",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(46.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(converters) { converter ->
                DifferentConverter(
                    icon = converter.icon,
                    name = converter.name,
                    onClick = { when (converter.name) {
                        "Currency" -> navController.navigate("currency_converter")
                        "Length" -> navController.navigate("length_converter")
                        "Area" -> navController.navigate("area_converter")
                        "Speed" -> navController.navigate("speed_converter")
                        "Weight" -> navController.navigate("weight_converter")
                        "Temperature" -> navController.navigate("temperature_converter")
                        "Power" -> navController.navigate("power_converter")
                        "Pressure" -> navController.navigate("pressure_converter")
                        "Volume" -> navController.navigate("volume_converter")
                    }
                    }
                )
            }
        }

    }
}

@Composable
fun DifferentConverter(icon: androidx.compose.ui.graphics.vector.ImageVector, name: String, onClick: () -> Unit){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val buttonWidth = (screenWidth / 5).dp
    val buttonHeight = (screenHeight / 8).dp

    Surface(
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .background(Color.Transparent),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10),
        color = Color.Transparent,
        onClick = onClick,
        tonalElevation = 4.dp,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp
                )
            }
        }
    }
}



