package br.com.unisal.fiuza.calculadoracustomized

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.unisal.fiuza.calculadoracustomized.ui.theme.CalculadorafiuzaTheme
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class MainActivity : ComponentActivity() {

    var display by mutableStateOf("0")

    val operatorStack = mutableListOf<String>()

    val operandStack = mutableListOf<String>()

    var waitingForOperand = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadorafiuzaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    createCalculator(display)
                }
            }
        }
    }

    @Composable
    fun createCalculator(display: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display: glass "panel" with a neon border, using the
            // surfaceVariant/onSurfaceVariant semantic roles and the
            // theme's "large" shape for the rounding.
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                tonalElevation = 6.dp,
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = display
                )
            }

            // sine, cosine, tangent and pi
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("Sin", CalculatorButton.SIN)
                createSmallButton("Cos", CalculatorButton.COS)
                createSmallButton("Tan", CalculatorButton.TAN)
                createSmallButton("Pi", CalculatorButton.PI)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // square root, inverse, factorial and power
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("√", CalculatorButton.SQRT)
                createSmallButton("Inv", CalculatorButton.INVERSE)
                createSmallButton("!", CalculatorButton.FACTORIAL)
                createSmallButton("^", CalculatorButton.POWER)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("%", CalculatorButton.PERCENT)
                createSmallButton("/", CalculatorButton.DIVIDE)
                createSmallButton("*", CalculatorButton.MULTIPLY)
                createSmallButton("-", CalculatorButton.SUBTRACT)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("7", CalculatorButton.SEVEN)
                createSmallButton("8", CalculatorButton.EIGHT)
                createSmallButton("9", CalculatorButton.NINE)
                createSmallButton("+", CalculatorButton.ADD)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("4", CalculatorButton.FOUR)
                createSmallButton("5", CalculatorButton.FIVE)
                createSmallButton("6", CalculatorButton.SIX)
                createSmallButton(",", CalculatorButton.DECIMAL)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("1", CalculatorButton.ONE)
                createSmallButton("2", CalculatorButton.TWO)
                createSmallButton("3", CalculatorButton.THREE)
                createSmallButton("=", CalculatorButton.EQUALS)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("+/-", CalculatorButton.TOGGLE_SIGN)
                createSmallButton("0", CalculatorButton.ZERO)
                createSmallButton("C", CalculatorButton.CLEAR)
                createSmallButton("<-", CalculatorButton.BACKSPACE)
            }
        }
    }

    @Composable
    fun createSmallButton(text: String, button: CalculatorButton) {
        // The equals button gets the theme's "extraLarge" (pill) shape,
        // reinforcing its special highlight; the others use the "large" shape.
        val shape = if (button == CalculatorButton.EQUALS) {
            MaterialTheme.shapes.extraLarge
        } else {
            MaterialTheme.shapes.large
        }

        // Label typography hierarchy:
        // - Equals: bigger and heavier (special highlight).
        // - Scientific functions: compact label (labelSmall), with wide
        //   letter spacing and uppercase — evokes a panel/HUD label.
        // - Other buttons: the theme's default labelLarge.
        val isScientific = button == CalculatorButton.SIN || button == CalculatorButton.COS ||
                button == CalculatorButton.TAN || button == CalculatorButton.INVERSE ||
                button == CalculatorButton.FACTORIAL || button == CalculatorButton.PI

        val labelStyle = when {
            button == CalculatorButton.EQUALS -> MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.labelLarge.fontSize * 1.2f
            )

            isScientific -> MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.4.sp
            )

            else -> MaterialTheme.typography.labelLarge
        }

        Button(
            modifier = Modifier.width(80.dp).height(50.dp),
            shape = shape,
            colors = buttonColorsFor(button),
            onClick = {
                // digits (0-9 and decimal point) go one way, operations go the other
                if (button.ordinal <= CalculatorButton.DECIMAL.ordinal) {
                    onNumberPress(button)
                } else {
                    onOperationPress(button)
                }
            }
        ) {
            Text(text.uppercase(), style = labelStyle)
        }
    }

    @Composable
    fun buttonColorsFor(button: CalculatorButton): ButtonColors {
        val colorScheme = MaterialTheme.colorScheme
        return when (button) {
            // Equals button: special highlight via tertiary
            CalculatorButton.EQUALS -> ButtonDefaults.buttonColors(
                containerColor = colorScheme.tertiary,
                contentColor = colorScheme.onTertiary
            )

            // Arithmetic operators: greater visual emphasis via primaryContainer
            CalculatorButton.ADD, CalculatorButton.SUBTRACT, CalculatorButton.MULTIPLY,
            CalculatorButton.DIVIDE, CalculatorButton.POWER, CalculatorButton.PERCENT -> {
                ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primaryContainer,
                    contentColor = colorScheme.onPrimaryContainer
                )
            }

            // Scientific functions: secondary role, in container
            CalculatorButton.SIN, CalculatorButton.COS, CalculatorButton.TAN,
            CalculatorButton.FACTORIAL, CalculatorButton.PI, CalculatorButton.INVERSE,
            CalculatorButton.SQRT -> {
                ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondaryContainer,
                    contentColor = colorScheme.onSecondaryContainer
                )
            }

            // Clear button: attention/error color, isolated from the other utility buttons
            CalculatorButton.CLEAR -> {
                ButtonDefaults.buttonColors(
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError
                )
            }

            // Other utility actions: "full" secondary role
            CalculatorButton.TOGGLE_SIGN, CalculatorButton.BACKSPACE -> {
                ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onSecondary
                )
            }

            // Numeric buttons: neutral appearance
            else -> ButtonDefaults.buttonColors(
                containerColor = colorScheme.surfaceVariant,
                contentColor = colorScheme.onSurfaceVariant
            )
        }
    }

    fun onNumberPress(button: CalculatorButton) {
        // if a calculation was just closed, the next number starts fresh from zero
        if (waitingForOperand) {
            display = "0"
            waitingForOperand = false
        }

        // avoids piling up leading zeros like "000"
        if ((display == "0") && (button.name == CalculatorButton.ZERO.name)) {
            return
        }

        // a number can only have one decimal separator
        if ((display.contains(",")) && (button.name == CalculatorButton.DECIMAL.name)) {
            return
        }

        var digit = button.name
        digit = if (digit == CalculatorButton.DECIMAL.name) {
            ","
        } else {
            button.ordinal.toString()
        }

        display = if (display.length == 1 && display == "0") {
            digit
        } else {
            display + digit
        }
    }

    fun onOperationPress(button: CalculatorButton) {
        when (button) {
            CalculatorButton.CLEAR -> {
                clear()
                return
            }
            CalculatorButton.EQUALS -> {
                calculateEquals()
                return
            }
            CalculatorButton.TOGGLE_SIGN -> {
                toggleSign()
                return
            }
            CalculatorButton.BACKSPACE -> {
                backspace()
                return
            }
            CalculatorButton.SIN, CalculatorButton.COS, CalculatorButton.TAN,
            CalculatorButton.FACTORIAL, CalculatorButton.PI, CalculatorButton.INVERSE,
            CalculatorButton.SQRT -> {
                unaryOperation(button)
                return
            }
            else -> {
            }
        }


        if (waitingForOperand) {
            if (operatorStack.isNotEmpty()) {
                operatorStack[operatorStack.lastIndex] = button.name
            } else {
                operatorStack.add(button.name)
                operandStack.add(display)
            }
            waitingForOperand = true
            return
        }

        if (operatorStack.isEmpty()) {
            operatorStack.add(button.name)
            operandStack.add(display)
        } else {
            calculateEquals()
            operatorStack.add(button.name)
            operandStack.add(display)
        }

        waitingForOperand = true
    }

    // Resolves the pending calculation stored in the operator/operand stacks
    fun calculateEquals() {
        if (operatorStack.isEmpty() || operandStack.isEmpty()) {
            // no pending calculation, nothing to do
            waitingForOperand = true
            return
        }

        val operator = operatorStack.removeAt(operatorStack.lastIndex)
        val operand = operandStack.removeAt(operandStack.lastIndex).toFloatOrNullBr()
        val current = display.toFloatOrNullBr()

        // if for some reason the number isn't valid, show an error
        if (operand == null || current == null) {
            display = "Erro"
            waitingForOperand = true
            return
        }

        val result: Float? = when (operator) {
            CalculatorButton.ADD.name -> operand + current

            CalculatorButton.SUBTRACT.name -> operand - current

            CalculatorButton.MULTIPLY.name -> operand * current

            CalculatorButton.DIVIDE.name -> {
                if (current == 0f) {
                    // can't divide by zero
                    display = "Não é possível dividir por zero"
                    null
                } else {
                    operand / current
                }
            }

            CalculatorButton.POWER.name -> {
                val power = operand.toDouble().pow(current.toDouble())
                if (power.isNaN() || power.isInfinite()) {
                    display = "Erro"
                    null
                } else {
                    power.toFloat()
                }
            }

            CalculatorButton.PERCENT.name -> {
                // example: 200 % 10 = -> calculates 10% of 200, which is 20
                operand * (current / 100f)
            }

            else -> null
        }

        if (result != null) {
            display = formatResult(result)
        }

        waitingForOperand = true
    }

    fun unaryOperation(button: CalculatorButton) {
        if (button == CalculatorButton.PI) {
            display = formatResult(3.14f)
            waitingForOperand = true
            return
        }

        val value = display.toFloatOrNullBr()
        if (value == null) {
            display = "Erro"
            waitingForOperand = true
            return
        }

        val result: Float? = when (button) {
            CalculatorButton.SIN -> sin(Math.toRadians(value.toDouble())).toFloat()

            CalculatorButton.COS -> cos(Math.toRadians(value.toDouble())).toFloat()

            CalculatorButton.TAN -> {
                val cosine = cos(Math.toRadians(value.toDouble()))
                if (abs(cosine) < 1e-10) {
                    display = "Erro: tangente indefinida"
                    null
                } else {
                    tan(Math.toRadians(value.toDouble())).toFloat()
                }
            }

            CalculatorButton.FACTORIAL -> {
                if (value < 0f || value != Math.floor(value.toDouble()).toFloat()) {
                    // factorial only exists for non negative int
                    display = "Erro: fatorial inválido"
                    null
                } else if (value > 20f) {
                    // beyond 20! the result no longer fits in a Long
                    display = "Erro: valor muito grande"
                    null
                } else {
                    factorial(value.toInt()).toFloat()
                }
            }

            CalculatorButton.INVERSE -> {
                if (value == 0f) {
                    // the inverse of zero would mean dividing by zero
                    display = "Não é possível dividir por zero"
                    null
                } else {
                    1f / value
                }
            }

            CalculatorButton.SQRT -> {
                if (value < 0f) {
                    // there's no real square root of a negative number
                    display = "Erro: raiz de número negativo"
                    null
                } else {
                    sqrt(value.toDouble()).toFloat()
                }
            }

            else -> null
        }

        if (result != null) {
            display = formatResult(result)
        }

        waitingForOperand = true
    }

    // Calculates the factorial
    fun factorial(n: Int): Long {
        var result = 1L
        for (i in 2..n) {
            result *= i
        }
        return result
    }

    //(positive <-> negative)
    fun toggleSign() {
        if (display == "0") return
        display = if (display.startsWith("-")) display.substring(1) else "-$display"
    }

    //Deletes the last typed character, like a backspace key
    fun backspace() {
        if (waitingForOperand) return
        display = if (display.length > 1) display.dropLast(1) else "0"
    }

    // Resets everything
    fun clear() {
        operatorStack.clear()
        operandStack.clear()
        waitingForOperand = false
        display = "0"
    }

    //Converts a display string (which uses a comma as the decimal separator)

    fun String.toFloatOrNullBr(): Float? = this.replace(",", ".").toFloatOrNull()

    //Formats a float result for display: drops the ".0" when the number is an int
    fun formatResult(value: Float): String {
        if (value.isNaN() || value.isInfinite()) {
            return "Erro"
        }
        return if (value == value.toLong().toFloat()) {
            value.toLong().toString()
        } else {
            value.toString().replace(".", ",")
        }
    }

    enum class CalculatorButton {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        DECIMAL,

        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        POWER,
        PERCENT,

        SIN,
        COS,
        TAN,
        FACTORIAL,
        PI,
        INVERSE,
        SQRT,

        TOGGLE_SIGN,
        BACKSPACE,
        EQUALS,
        CLEAR
    }

    @Composable
    fun ThemeSelector(
        selectedTheme: MaterialTheme,
        onThemeSelected: (MaterialTheme) -> Unit
    ) {

    }
}