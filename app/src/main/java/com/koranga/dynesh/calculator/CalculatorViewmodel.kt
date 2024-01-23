package com.koranga.dynesh.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewmodel: ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction)  {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.nmbr)
            is CalculatorAction.Calculate -> calculate()
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            else -> {}
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.num1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if (state.operation == null && !state.num1.contains(".")) {
            state = state.copy(
                num1 = state.num1 + "."
            )
            return
        }

        if (state.num1 != null && state.operation != null && state.num2 == null) {
            state = state.copy(
                num2 = state.num2 + "0."
            )
            return
        }

        if (state.num1 != null && state.operation != null && state.num2 != null && !state.num2.contains(".")) {
            state = state.copy(
                num2 = state.num2 + "."
            )
            return
        }
    }

    private fun calculate() {
        val number1 = state.num1.toDoubleOrNull()
        val number2 = state.num2.toDoubleOrNull()

        if (number1 != null && number2 != null) {
            val result = when(state.operation){
                CalculatorOperation.Add -> number1 + number2
                CalculatorOperation.Subtract -> number1 - number2
                CalculatorOperation.Multiply -> number1 * number2
                CalculatorOperation.Divide -> number1 / number2
                null -> return
                else -> {}
            }
            state = state.copy(
                num1 = result.toString().take(15),
                num2 = "",
                operation = null
            )
        }
    }

    private fun enterNumber(nmbr: Int) {
        if (state.operation == null) {
            if (state.num1.length > MAX_LENGTH){
                return
            }
            state = state.copy(
                num1 = state.num1 + nmbr
            )
            return
        }

        if (state.num2.length > MAX_LENGTH) {
            return
        }

        state = state.copy(
            num2 = state.num2 + nmbr
        )


    }

    companion object{
        private const val MAX_LENGTH = 8
    }

    private fun delete() {
        when {
            state.num2.isNotBlank() -> state = state.copy(
                num2 = state.num2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.num1.isNotBlank() -> state = state.copy(
                num1 = state.num1.dropLast(1)
            )
        }
    }
}