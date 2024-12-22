package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Stack


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorUI()
        }
    }
}

@Composable
fun CalculatorUI() {
    var input by remember {
        mutableStateOf("0")
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(imageVector = Icons.Default.FullscreenExit,
                contentDescription = "Minimize Screen",
                tint =  Color.White,
                modifier = Modifier.size(40.dp)
                )

            Row {
                Icon(imageVector = Icons.Default.AccessTime,
                    contentDescription = "History",
                    tint =  Color.White,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(imageVector = Icons.Default.GridView,
                    contentDescription = "History",
                    tint =  Color.White,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(imageVector = Icons.Filled.MoreVert,
                    contentDescription = "History",
                    tint =  Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(140.dp))

        Text(
            text = input,
            fontSize = 56.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                textDirection = TextDirection.Rtl
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, top = 40.dp)
                .align(Alignment.End)
        )


        Spacer(modifier = Modifier.height(72.dp))

        val buttons = listOf(
            listOf("C", "%","<-","/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("00", "0", ".", "="),
        )


        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x38383838))
            ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ){
                for (row in buttons) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        for (button in row) {
                            CalculatorButton(button) {
                                input = handleButtonClick(button, input)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(symbol: String, onClick: () -> Unit){
    val buttonSizes = if (symbol in listOf("/", "*", "-", "+", "=")) {
        Modifier.size(78.dp)
    } else {
        Modifier.size(90.dp)
    }
    val buttonColors = when {
        symbol in listOf("/", "*", "-", "+") -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        }
        symbol == "=" -> {
            ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF68B19),
                contentColor = Color.White
            )
        }
        else -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        }
    }
    Button(
        onClick = onClick,
        modifier = buttonSizes,
        colors = buttonColors
    ) {
       Text(symbol, fontSize = 32.sp)
    }
}


fun handleButtonClick(symbol: String, currentInput: String): String{
    return  when (symbol) {
        "C" -> "0"
        "=" -> try {
            eval(currentInput)
        } catch (e: Exception) {
            "Error"
        }
        else -> {
            if(currentInput == "0" && symbol in listOf("+", "-", "*", "/")) {
                "0"
            } else if (currentInput == "0"){
                symbol
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
            char in precedence -> {
                if (number.isNotEmpty()) {
                    output.add(number)
                    number = ""
                }
                while (operators.isNotEmpty() && precedence[char]!! <= precedence[operators.peek()]!!) {
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
