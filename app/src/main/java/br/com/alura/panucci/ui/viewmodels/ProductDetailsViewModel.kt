package br.com.alura.panucci.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.security.auth.login.LoginException
import kotlin.random.Random

class ProductDetailsViewModel(
    private val dao: ProductDao = ProductDao()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun findProductById(id: String) {
        viewModelScope.launch {
            //teste de timeout na tela
            val timeInMillis = Random.nextLong(500, 4000)
            delay(timeInMillis)
            dao.findById(id)?.let { product ->
                _uiState.update {
                    it.copy(product = product)
                }
            }
        }
    }

    fun applyDiscountPromotionalCode(promoCode: String?) {
        _uiState.update { state ->

            val discount = when (promoCode) {
                "{ALURA}" -> BigDecimal("0.1")
                else -> BigDecimal.ZERO
            }
            val value = state.product?.price?.run {
                this.minus(this.multiply(discount))
            }

            Log.i(TAG, "applyDiscountPromotionalCode: antes ${_uiState.value}")

            Log.i(TAG, "applyDiscountPromotionalCode: ${promoCode} value: ${value}")

            val newState = state.copy(product = value?.let { newValue ->
                Log.i(TAG, "applyDiscountPromotionalCode: newValue: ${newValue}")
                state.product.copy(price = newValue)
            })

            newState
        }

        Log.i(TAG, "applyDiscountPromotionalCode: depois ${_uiState.value}")

    }

    companion object {
        const val TAG = "ProductDetailsViewModel"
    }

}