package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Stack


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val statusBarColor = Color.Black // Choose your desired color
            // Set the status bar color
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = statusBarColor,
                    darkIcons = false // Use light icons for dark backgrounds
                )
            }
            CalculatorUI()
        }
    }
}

@Composable
fun CalculatorUI() {
    var input by remember { mutableStateOf("0") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonSize = (screenWidth / 4.5).dp

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
                listOf(Icons.Outlined.AccessTime, Icons.Outlined.GridView, Icons.Outlined.MoreVert).forEach {
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        imageVector = it,
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
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
            listOf("C", "%", "<-", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("00", "0", ".", "="),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0x32848682)) // Keypad background color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { symbol ->
                            CalculatorButton(symbol, buttonSize) {
                                input = handleButtonClick(symbol, input)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(symbol: String, size: Dp, onClick: () -> Unit) {
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


fun handleButtonClick(symbol: String, currentInput: String): String {
    return when (symbol) {
        "C" -> "0" // Clear resets to "0"
        "<-" -> if (currentInput.length > 1) {
            currentInput.dropLast(1)
        } else {
            "0"
        }
        "=" -> try {
            eval(currentInput)
        } catch (e: Exception) {
            "Error"
        }
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


@Preview(showBackground = true)
@Composable
fun PreviewCalculatorUI(){
    CalculatorUI()
}
