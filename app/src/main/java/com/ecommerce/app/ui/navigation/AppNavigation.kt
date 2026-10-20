package com.ecommerce.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ecommerce.app.model.Product
import com.ecommerce.app.ui.screens.CartItem
import com.ecommerce.app.ui.screens.CartScreen
import com.ecommerce.app.ui.screens.HomeScreen
import com.ecommerce.app.ui.screens.ProductDetailScreen
import com.ecommerce.app.ui.screens.SplashScreen
import com.ecommerce.app.ui.screens.WishlistScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object Cart : Screen("cart")
    data object Wishlist : Screen("wishlist")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cartItems = remember { mutableStateListOf<CartItem>() }

    fun addToCart(product: Product) {
        val existingIndex = cartItems.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val current = cartItems[existingIndex]
            cartItems[existingIndex] = current.copy(quantity = current.quantity + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun increaseQuantity(product: Product) {
        val index = cartItems.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val current = cartItems[index]
            cartItems[index] = current.copy(quantity = current.quantity + 1)
        }
    }

    fun decreaseQuantity(product: Product) {
        val index = cartItems.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val current = cartItems[index]
            if (current.quantity > 1) {
                cartItems[index] = current.copy(quantity = current.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    fun removeFromCart(product: Product) {
        cartItems.removeAll { it.product.id == product.id }
    }

    val totalItemCount = cartItems.sumOf { it.quantity }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToWishlist = {
                    navController.navigate(Screen.Wishlist.route)
                },
                cartItemCount = totalItemCount,
                onAddToCart = { product ->
                    addToCart(product)
                }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                cartItemCount = totalItemCount,
                onAddToCart = { product ->
                    addToCart(product)
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                cartItems = cartItems,
                onIncreaseQuantity = { increaseQuantity(it) },
                onDecreaseQuantity = { decreaseQuantity(it) },
                onRemoveItem = { removeFromCart(it) },
                onClearCart = { cartItems.clear() },
                onBackClick = { navController.popBackStack() },
                onExploreShop = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Wishlist.route) {
            WishlistScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onAddToCart = { product ->
                    addToCart(product)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                cartItemCount = totalItemCount
            )
        }
    }
}
