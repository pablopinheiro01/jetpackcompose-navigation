package br.com.alura.panucci.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(
        ProductDetailsUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    fun findProductById(id: String) {
        _uiState.update { ProductDetailsUiState.Loading }
        viewModelScope.launch {
            //teste de timeout na tela
            val timeInMillis = Random.nextLong(500, 4000)
            delay(timeInMillis)
            val dataState = dao.findById(id)?.let { product ->
                ProductDetailsUiState.Success(product = product)
            }?: ProductDetailsUiState.Failure

            _uiState.update { dataState }
        }
    }

    fun applyDiscountPromotionalCode(promoCode: String?) {
        viewModelScope.launch {
            _uiState.update { state ->
                when (state) {
                    is ProductDetailsUiState.Success -> {
                        val discount = when (promoCode) {
                            "{ALURA}" -> BigDecimal("0.1")
                            else -> BigDecimal.ZERO
                        }
                        val value = state.product?.price?.run {
                            this.minus(this.multiply(discount))
                        }

                        Log.i(TAG, "applyDiscountPromotionalCode: antes ${_uiState.value}")

                        Log.i(TAG, "applyDiscountPromotionalCode: ${promoCode} value: ${value}")

                        val product = value?.let { newValue ->
                            state.product.copy(price = newValue)
                        } ?: state.product

                        ProductDetailsUiState.Success(product)

                    }
                    else -> {
                        ProductDetailsUiState.Loading
                    }
                }
            }

        }

        Log.i(TAG, "applyDiscountPromotionalCode: depois ${_uiState.value}")

    }

    companion object {
        const val TAG = "ProductDetailsViewModel"
    }

}