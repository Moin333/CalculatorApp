package com.example.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.Stack

@Composable
fun CalculatorUI(viewModel: CalculatorViewModel, navController: NavHostController) {
    var input by remember { mutableStateOf("0") }
    var showHistory by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val historyWidth = (screenWidth * 2.2 / 3).dp
    val buttonSize by animateDpAsState(
        targetValue = if (showHistory) (screenWidth / 6).dp else (screenWidth / 4.5).dp,
        label = "Button Size Animation",
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    // History Panel Offset
    val historyPanelOffset by animateDpAsState(
        targetValue = if (showHistory) 0.dp else -historyWidth,
        label = "History Panel Offset",
        animationSpec = tween(durationMillis = 400, easing = LinearEasing)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Icons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.FullscreenExit,
                contentDescription = "Minimize Screen",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf(Icons.Outlined.AccessTime, Icons.Outlined.GridView, Icons.Outlined.MoreVert).forEachIndexed { index, icon ->
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable{
                                when (index) {
                                    0 -> showHistory = !showHistory
                                    1 -> navController.navigate("unit_converter")
                                }
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Display
        // Input TextField
        TextField(
            value = input,
            onValueChange = { newValue -> input = newValue },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
                .weight(0.4f),
            singleLine = true,
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 48.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )


        // Buttons
        val buttons = listOf(
            listOf("C", "%", "Backspace", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("00", "0", ".", "="),
        )

        Row (modifier = Modifier.weight(1f)) {

            AnimatedVisibility(
                visible = showHistory,
                enter = fadeIn(animationSpec = tween(400, easing = LinearEasing)),
                exit = fadeOut(animationSpec = tween(200, easing = LinearEasing))
            ) {
                Box(
                    modifier = Modifier
                        .width(historyWidth)
                        .offset(x = historyPanelOffset)
                        .background(Color.DarkGray)
                ) {
                    HistoryScreen(viewModel) { selectedInput ->
                        input = selectedInput
                        showHistory = false
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x32848682)) // Keypad background color
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (showHistory) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.weight(1f)
                        ) {
                            listOf("Backspace", "/", "*", "-", "+", "=").forEach { symbol ->
                                if (symbol == "Backspace") {
                                    Surface(
                                        modifier = Modifier
                                            .size(buttonSize) // Set the size of the button
                                            .background(Color.Transparent), // Optional, keeps the circular nature
                                        shape = androidx.compose.foundation.shape.CircleShape, // Makes it circular
                                        color = Color.DarkGray, // Set the background color of the "button"
                                        onClick = {
                                            input = handleButtonClick("<-", input, viewModel)
                                        },
                                        tonalElevation = 4.dp, // Optional, adds elevation for Material styling
                                        contentColor = Color.White // Sets the content color (for ripple)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center, // Centers the icon within the circular button
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                                contentDescription = "Backspace",
                                                modifier = Modifier.size(buttonSize * 0.4f), // Adjust the icon size
                                                tint = Color.White
                                            )
                                        }
                                    }
                                } else {
                                    CalculatorButton(symbol, buttonSize, viewModel) {
                                        input = handleButtonClick(symbol, input, viewModel)
                                    }
                                }
                            }
                        }
                    } else {
                        buttons.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { symbol ->
                                    if (symbol == "Backspace") {
                                        Box(
                                            modifier = Modifier
                                                .size(buttonSize) // Set the container size to match other buttons
                                                .clickable {
                                                    input =
                                                        handleButtonClick("<-", input, viewModel)
                                                },
                                            contentAlignment = Alignment.Center // Center the icon within the container
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                                contentDescription = "Backspace",
                                                tint = Color.White,
                                                modifier = Modifier.size(buttonSize * 0.3f) // Adjust the icon size
                                            )
                                        }
                                    } else {
                                        CalculatorButton(symbol, buttonSize, viewModel) {
                                            input = handleButtonClick(symbol, input, viewModel)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun handleEqualsClick(input: String, viewModel: CalculatorViewModel): String {
    return try {
        val result = eval(input)
        viewModel.addOrUpdate(Calculator(inputValue = input, calculatedValue = result))
        result
    } catch (e: Exception) {
        "Error"
    }
}

@Composable
fun CalculatorButton(symbol: String, size: Dp, viewModel: CalculatorViewModel, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(size) // Ensures button is circular
            .aspectRatio(1f), // Keeps the button's aspect ratio
        colors = when (symbol) {
            in listOf("/", "*", "-", "+") -> ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
            "=" -> ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF68B19),
                contentColor = Color.White
            )
            else -> ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        }
    ) {
        Text(text = symbol, fontSize = 24.sp)
    }
}


fun handleButtonClick(symbol: String, currentInput: String, viewModel: CalculatorViewModel): String {
    return when (symbol) {
        "C" -> "0" // Clear resets to "0"
        "<-" -> if (currentInput.length > 1) {
            currentInput.dropLast(1)
        } else {
            "0"
        }
        "=" -> handleEqualsClick(currentInput, viewModel)
        else -> {
            // Prevent invalid sequences
            if (currentInput == "0" && symbol in listOf("0", "00", ".")) {
                currentInput // Avoid starting with "00" or "."
            } else if (currentInput == "0") {
                symbol
            } else if (symbol in listOf("+", "-", "*", "/") &&
                currentInput.last() in listOf('+', '-', '*', '/')) {
                currentInput.dropLast(1) + symbol // Replace last operator
            } else {
                currentInput + symbol
            }
        }
    }
}


fun eval(expression: String): String {
    return try {
        val result = evaluatePostfix(toPostfix(expression))
        if(result % 1 == 0.0) result.toInt().toString() else result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

fun toPostfix(expression: String): List<String> {
    val precedence = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2)
    val output = mutableListOf<String>()
    val operators = Stack<Char>()
    var number = ""

    for (char in expression) {
        when {
            char.isDigit() || char == '.' -> number += char
            char == '(' -> operators.push(char)
            char == ')' -> {
                if (number.isNotEmpty()) {
                    output.add(number)
                    number = ""
                }
                while (operators.isNotEmpty() && operators.peek() != '(') {
                    output.add(operators.pop().toString())
                }
                if (operators.isNotEmpty() && operators.peek() == '(') {
                    operators.pop() // Remove '('
                }
            }
            char in precedence -> {
                if (number.isNotEmpty()) {
                    output.add(number)
                    number = ""
                }
                while (operators.isNotEmpty() && operators.peek() != '(' &&
                    precedence[char]!! <= precedence[operators.peek()]!!) {
                    output.add(operators.pop().toString())
                }
                operators.push(char)
            }
        }
    }
    if (number.isNotEmpty()) output.add(number)
    while (operators.isNotEmpty()) output.add(operators.pop().toString())
    return output
}


fun evaluatePostfix(postfix: List<String>): Double {
    val stack = Stack<Double>()

    for (token in postfix) {
        when {
            token.toDoubleOrNull() != null -> stack.push(token.toDouble())
            token in listOf("+", "-", "*", "/") -> {
                val b = stack.pop()
                val a = stack.pop()
                stack.push(
                    when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        else -> throw IllegalArgumentException("Unknown operator")
                    }
                )
            }
        }
    }
    return stack.pop()
}


