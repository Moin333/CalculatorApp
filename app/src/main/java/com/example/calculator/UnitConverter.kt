package com.example.calculator

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class ConverterType(val icon: androidx.compose.ui.graphics.vector.ImageVector, val name: String)

@Composable
fun UnitConverterPage(navController: NavHostController){

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
            .background(color = Color.Black),
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
                tint = Color.White
            )

            Text(
                text = "Unit Converter",
                color = Color.White,
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
                DifferentConverter(icon = converter.icon, name = converter.name)
            }
        }

    }
}

@Composable
fun DifferentConverter(icon: androidx.compose.ui.graphics.vector.ImageVector, name: String){
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
        onClick = {},
        tonalElevation = 4.dp,
        contentColor = Color.White
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
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}



