package com.example.calculator

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.Locale
import java.util.Stack
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

@Composable
fun CalculatorUI(viewModel: CalculatorViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val activity = context as? Activity

    // Unlock orientation by default for non-locked pages
    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    var input by remember { mutableStateOf("0") }
    var showHistory by remember { mutableStateOf(false) }
    var showScientific by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showMinimize by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = configuration.screenWidthDp >= configuration.screenHeightDp
    val historyWidth = (screenWidth * 2.2 / 3).dp
    val buttonSize by animateDpAsState(
        targetValue = if (showHistory) (screenWidth / 6).dp else if (showScientific) (screenWidth / 6.7).dp else (screenWidth / 4.5).dp,
        label = "Button Size Animation",
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    // History Panel Offset
    val historyPanelOffset by animateDpAsState(
        targetValue = if (showHistory) 0.dp else -historyWidth,
        label = "History Panel Offset",
        animationSpec = tween(durationMillis = 400, easing = LinearEasing)
    )

    // Keypad and Output Sizes for Landscape Mode
    val outputHeight = (screenHeight * 0.6f / 3).dp
    val keypadHeight = (screenHeight * 2.4f / 3).dp
    val lButtonSize = (screenWidth / 22.5).dp // Adjust button size for scientific keypad
    val buttonWidth = if (isLandscape) lButtonSize * 1.2f else lButtonSize
    val buttonHeight = lButtonSize

    val landscapeButtons = listOf(
        listOf("sin", "rad", "deg", "C", "%", "Backspace", "/"),
        listOf("cos", "tan", "inv", "7", "8", "9", "*"),
        listOf("log", "ln", "!", "4", "5", "6", "-"),
        listOf("π", "e", "^", "1", "2", "3", "+"),
        listOf("√", "(", ")", "00", "0", ".", "="),
    )

    if (isLandscape) {
        // Landscape Mode UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Output Section (1/3 height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(outputHeight)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 16.dp, end = 24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = input,
                    style = TextStyle(
                        fontSize = 36.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )
                )
            }

            // Keypad Section (2/3 height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(keypadHeight)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    landscapeButtons.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(72.5.dp)
                        ) {
                            row.forEach { symbol ->
                                if (symbol == "Backspace") {
                                    Surface(
                                        modifier = Modifier
                                            .width(buttonWidth) // Set the size of the button
                                            .height(buttonHeight)
                                            .background(Color.Transparent), // Optional, keeps the circular nature
                                        shape = androidx.compose.foundation.shape.CircleShape, // Makes it circular
                                        color = Color.Transparent, // Set the background color of the "button"
                                        onClick = {
                                            input =
                                                handleButtonClick("<-", input, viewModel)
                                        },
                                        contentColor = Color.White // Sets the content color (for ripple)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center, // Centers the icon within the circular button
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                                contentDescription = "Backspace",
                                                modifier = Modifier.size(lButtonSize * 0.5f), // Adjust the icon size
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                } else {
                                    CalculatorButton(symbol, lButtonSize, viewModel) {
                                        input = handleButtonClick(symbol, input, viewModel)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
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
                if (showMinimize) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "Minimize Screen",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(24.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    listOf(
                        Icons.Outlined.AccessTime,
                        Icons.Outlined.GridView,
                        Icons.Outlined.MoreVert
                    ).forEachIndexed { index, icon ->
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = icon,
                            contentDescription = "Icon",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    when (index) {
                                        0 -> {
                                            if (showScientific) {
                                                showScientific = false
                                            }
                                            showHistory = !showHistory
                                        }

                                        1 -> navController.navigate("unit_converter")
                                        2 -> expanded = true
                                    }
                                }
                        )
                    }
                    Box(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.TopEnd,
                    ) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .width(140.dp),
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (showScientific) "   Basic" else "   Scientific",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    if (showHistory) {
                                        showHistory = false
                                    }
                                    showScientific = !showScientific
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (showHistory) "   Normal" else "   History",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    if (showScientific) {
                                        showScientific = false
                                    }
                                    showHistory = !showHistory
                                }
                            )
                        }
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
                    color = MaterialTheme.colorScheme.onBackground,
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

            val scientificButtons = listOf(
                listOf("sin", "cos", "tan", "rad", "deg"),
                listOf("log", "ln", "(", ")", "inv"),
                listOf("!", "C", "%", "Backspace", "/"),
                listOf("^", "7", "8", "9", "*"),
                listOf("√", "4", "5", "6", "-"),
                listOf("π", "1", "2", "3", "+"),
                listOf("e", "00", "0", ".", "="),
            )

            Row(modifier = Modifier.weight(1f)) {

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
                        .background(MaterialTheme.colorScheme.surface) // Keypad background color
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (showScientific) {
                            scientificButtons.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    row.forEach { symbol ->
                                        if (symbol == "Backspace") {
                                            Surface(
                                                modifier = Modifier
                                                    .size(buttonSize) // Set the size of the button
                                                    .background(Color.Transparent), // Optional, keeps the circular nature
                                                shape = androidx.compose.foundation.shape.CircleShape, // Makes it circular
                                                color = Color.Transparent, // Set the background color of the "button"
                                                onClick = {
                                                    input =
                                                        handleButtonClick("<-", input, viewModel)
                                                },
                                                contentColor = Color.White // Sets the content color (for ripple)
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center, // Centers the icon within the circular button
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                                        contentDescription = "Backspace",
                                                        modifier = Modifier.size(buttonSize * 0.3f), // Adjust the icon size
                                                        tint = MaterialTheme.colorScheme.onSurface
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
                            }
                        }
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
                                            color = MaterialTheme.colorScheme.surfaceVariant, // Set the background color of the "button"
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
                                                    tint = MaterialTheme.colorScheme.onSurface
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
                        }
                        if (!showHistory && !showScientific) {
                            buttons.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEach { symbol ->
                                        if (symbol == "Backspace") {
                                            Surface(
                                                modifier = Modifier
                                                    .size(buttonSize) // Set the size of the button
                                                    .background(Color.Transparent), // Optional, keeps the circular nature
                                                shape = androidx.compose.foundation.shape.CircleShape, // Makes it circular
                                                color = Color.Transparent, // Set the background color of the "button"
                                                onClick = {
                                                    input =
                                                        handleButtonClick("<-", input, viewModel)
                                                },
                                                contentColor = Color.White // Sets the content color (for ripple)
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center, // Centers the icon within the circular button
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                                        contentDescription = "Backspace",
                                                        modifier = Modifier.size(buttonSize * 0.3f), // Adjust the icon size
                                                        tint = MaterialTheme.colorScheme.onSurface
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
fun CalculatorButton(
    symbol: String,
    size: Dp,
    viewModel: CalculatorViewModel,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp >= configuration.screenHeightDp

    val buttonWidth = if (isLandscape) size * 1.2f else size
    val buttonHeight = size

    val textSize = if (isLandscape) {
        when (symbol) {
            in listOf("sin", "cos", "tan", "rad", "deg", "log", "ln", "inv") -> (size.value * 0.45).sp
            else -> (size.value * 0.5).sp
        }
    } else {
        when (symbol) {
            in listOf("sin", "cos", "tan", "rad", "deg", "log", "ln", "inv") -> (size.value * 0.34).sp
            else -> (size.value * 0.34).sp
        }
    }

    Surface(
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .aspectRatio(1f)
            .background(Color.Transparent),
        shape = androidx.compose.foundation.shape.CircleShape,
        onClick = onClick,
        color = when (symbol) {
            in listOf("/", "*", "-", "+") -> MaterialTheme.colorScheme.surfaceVariant
            "=" -> MaterialTheme.colorScheme.primary
            else -> Color.Transparent
        },
        contentColor = when (symbol) {
            in listOf("sin", "cos", "tan", "rad", "log", "ln", "(", ")", "inv", "!", "^", "√", "π", "e") -> MaterialTheme.colorScheme.secondary
            "deg" -> MaterialTheme.colorScheme.primary
            "=" -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurface
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = symbol,
                fontSize = textSize
            )
        }
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
        // Round off result if necessary to 3 decimal places
        val roundedResult = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format(Locale.ROOT, "%.3f", result) // Ensure consistent formatting
        }
        roundedResult
    } catch (e: Exception) {
        "Error"
    }
}


fun toPostfix(expression: String): List<String> {
    val precedence = mapOf(
        '+' to 1, '-' to 1, '*' to 2, '/' to 2, '^' to 3
    )
    val functions = setOf("sin", "cos", "tan", "log", "ln", "√", "!")
    val output = mutableListOf<String>()
    val operators = Stack<String>()
    var number = ""

    for (i in expression.indices) {
        val char = expression[i]

        when {
            char.isDigit() || char == '.' -> number += char
            char == '(' -> operators.push("(")
            char == ')' -> {
                if (number.isNotEmpty()) {
                    output.add(number)
                    number = ""
                }
                while (operators.isNotEmpty() && operators.peek() != "(") {
                    output.add(operators.pop())
                }
                if (operators.isNotEmpty() && operators.peek() == "(") {
                    operators.pop()
                }
                // Check if a function precedes the closing parenthesis
                if (operators.isNotEmpty() && operators.peek() in functions) {
                    output.add(operators.pop())
                }
            }
            char in precedence.keys -> {
                if (number.isNotEmpty()) {
                    output.add(number)
                    number = ""
                }
                while (operators.isNotEmpty() && operators.peek() != "(" &&
                    precedence[char]!! <= precedence[operators.peek()[0]]!!) { // Use [0] to get the first Char
                    output.add(operators.pop()) // No need to convert; already a String
                }
                operators.push(char.toString()) // Push as a String
            }
            else -> { // Assume it's part of a function name
                number += char
                if (functions.contains(number)) {
                    operators.push(number)
                    number = ""
                }
            }
        }
    }

    if (number.isNotEmpty()) output.add(number)
    while (operators.isNotEmpty()) output.add(operators.pop())

    return output
}


fun evaluatePostfix(postfix: List<String>): Double {
    val stack = Stack<Double>()

    for (token in postfix) {
        when {
            token.toDoubleOrNull() != null -> stack.push(token.toDouble())
            token in listOf("+", "-", "*", "/", "^") -> {
                val b = stack.pop()
                val a = stack.pop()
                stack.push(
                    when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        "^" -> a.pow(b)
                        else -> throw IllegalArgumentException("Unknown operator")
                    }
                )
            }
            token in listOf("sin", "cos", "tan", "log", "ln", "√", "!") -> {
                val a = stack.pop()
                stack.push(
                    when (token) {
                        "sin" -> sin(Math.toRadians(a))
                        "cos" -> cos(Math.toRadians(a))
                        "tan" -> tan(Math.toRadians(a))
                        "log" -> log10(a)
                        "ln" -> ln(a)
                        "√" -> sqrt(a)
                        "!" -> factorial(a.toInt())
                        else -> throw IllegalArgumentException("Unknown function")
                    }
                )
            }
        }
    }
    return stack.pop()
}


fun factorial(n: Int): Double {
    return if (n == 0 || n == 1) 1.0 else n * factorial(n - 1)
}


