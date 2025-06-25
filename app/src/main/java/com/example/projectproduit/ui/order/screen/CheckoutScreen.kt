package com.example.projectproduit.ui.order.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Address
import com.example.projectproduit.data.entities.Order
import com.example.projectproduit.data.entities.OrderItem
import com.example.projectproduit.data.entities.OrderStatus
import com.example.projectproduit.data.entities.PaymentMethod
import com.example.projectproduit.ui.cart.CartIntent
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.order.OrderIntent
import com.example.projectproduit.ui.order.OrderViewModel
import java.util.UUID
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.projectproduit.ui.product.ProductViewModel
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    userId: String,
    onOrderPlaced: () -> Unit,
    onCancel: () -> Unit
) {
    val cartState by cartViewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var street by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }

    var selectedPaymentMethod by rememberSaveable { mutableStateOf(PaymentMethod.CASH_ON_DELIVERY) }
    val paymentOptions = PaymentMethod.entries.toTypedArray()
    var paymentExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (cartState.items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", style = MaterialTheme.typography.headlineMedium)
            }
            return@Column
        }

        Text("Delivery Address", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = street,
            onValueChange = { street = it },
            label = { Text("Street") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = postalCode,
            onValueChange = { postalCode = it },
            label = { Text("Postal Code") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Text("Payment Method", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = paymentExpanded,
            onExpandedChange = { paymentExpanded = !paymentExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedPaymentMethod.name.replace('_', ' '),
                onValueChange = {},
                readOnly = true,
                label = { Text("Payment Method") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentExpanded)
                },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = paymentExpanded,
                onDismissRequest = { paymentExpanded = false }
            ) {
                paymentOptions.forEach { method ->
                    DropdownMenuItem(
                        text = { Text(method.name.replace('_', ' ')) },
                        onClick = {
                            selectedPaymentMethod = method
                            paymentExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Total: $${"%.2f".format(cartState.totalPrice)}", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (street.isBlank() || city.isBlank() || postalCode.isBlank() || country.isBlank()) {
                    Toast.makeText(context, "Please fill in all address fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val orderItems = cartState.items.map {
                    OrderItem(
                        product = it.product,
                        quantity = it.quantity,
                        unitPrice = it.product.price * (1 - (it.product.discountPercentage ?: 0.0) / 100),
                        discountApplied = it.product.discountPercentage ?: 0.0
                    )
                }

                val order = Order(
                    orderId = UUID.randomUUID().toString(),
                    orderDate = Date(),
                    items = orderItems,
                    totalAmount = cartState.totalPrice,
                    shippingFee = 0.0,
                    deliveryAddress = Address(street, city, postalCode, country),
                    paymentMethod = selectedPaymentMethod,
                    userId = userId,
                    status = OrderStatus.PENDING
                )

                coroutineScope.launch {
                    orderViewModel.onIntent(OrderIntent.PlaceOrder(order))
                    productViewModel.reduceStockAfterOrder(orderItems)
                    cartViewModel.onIntent(CartIntent.ClearCart)
                    Toast.makeText(context, "Order placed successfully", Toast.LENGTH_SHORT).show()
                    onOrderPlaced()
                }
            },
            enabled = cartState.items.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }

        TextButton(
            onClick = { onCancel() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel", color = MaterialTheme.colorScheme.primary)
        }
    }
}
