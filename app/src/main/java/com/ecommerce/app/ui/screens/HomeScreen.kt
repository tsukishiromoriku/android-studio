package com.ecommerce.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.ecommerce.app.data.ProductRepository
import com.ecommerce.app.model.Product
import com.ecommerce.app.ui.components.CategoryChip
import com.ecommerce.app.ui.components.ProductCard
import com.ecommerce.app.ui.components.SearchBarComponent
import com.ecommerce.app.ui.theme.BackgroundLight
import com.ecommerce.app.ui.theme.BluePrimary
import com.ecommerce.app.ui.theme.CoralBadge
import com.ecommerce.app.ui.theme.TextPrimary
import com.ecommerce.app.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    cartItemCount: Int,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToWishlist: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("all") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val products by ProductRepository.productsState.collectAsState()
    val categories by ProductRepository.categoriesState.collectAsState()
    val wishlistIds by ProductRepository.wishlistIdsState.collectAsState()

    LaunchedEffect(Unit) {
        ProductRepository.syncWithMySQL()
    }

    val filteredProducts = remember(searchQuery, selectedCategoryId, products) {
        ProductRepository.filterProducts(searchQuery, selectedCategoryId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundLight,
        topBar = {
            // Header Bar with statusBarsPadding to prevent overlapping with system status bar
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Jualin.id Frog Logo from Image #4
                        Image(
                            painter = painterResource(id = com.ecommerce.app.R.drawable.logo_jualin),
                            contentDescription = "Logo Jualin.id",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )

                        Column {
                            Text(
                                text = "Jualin.id",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp,
                                    letterSpacing = (-0.5).sp
                                ),
                                color = BluePrimary
                            )
                            Text(
                                text = "Belanja Mudah & Terpercaya",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 11.sp
                                ),
                                color = TextSecondary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Wishlist Icon with Badge
                        Box(contentAlignment = Alignment.TopEnd) {
                            IconButton(onClick = onNavigateToWishlist) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Wishlist",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            if (wishlistIds.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp, end = 4.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(CoralBadge),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (wishlistIds.size > 9) "9+" else wishlistIds.size.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Cart Icon with Badge
                        Box(contentAlignment = Alignment.TopEnd) {
                            IconButton(onClick = onNavigateToCart) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart,
                                    contentDescription = "Keranjang Belanja",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            if (cartItemCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp, end = 4.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(CoralBadge),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (cartItemCount > 9) "9+" else cartItemCount.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Bar
            item(span = { GridItemSpan(2) }) {
                SearchBarComponent(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            // Category Chips Row
            item(span = { GridItemSpan(2) }) {
                Column {
                    Text(
                        text = "Kategori Produk",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                category = category,
                                isSelected = selectedCategoryId == category.id,
                                onSelect = { selectedCategoryId = category.id }
                            )
                        }
                    }
                }
            }

            // Section Title
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Katalog Produk",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = TextPrimary
                    )

                    Text(
                        text = "${filteredProducts.size} Produk",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = TextSecondary
                    )
                }
            }

            // Empty State
            if (filteredProducts.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "Kosong",
                            tint = TextSecondary,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Produk Tidak Ditemukan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coba gunakan kata kunci atau pilih kategori lain",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                searchQuery = ""
                                selectedCategoryId = "all"
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BluePrimary
                            )
                        ) {
                            Text("Reset Pencarian")
                        }
                    }
                }
            } else {
                // Product Grid Items
                items(filteredProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = onProductClick,
                        isProductFavorite = wishlistIds.contains(product.id),
                        onToggleWishlist = { item ->
                            ProductRepository.toggleWishlist(item.id)
                            scope.launch {
                                val isAdded = !wishlistIds.contains(item.id)
                                snackbarHostState.showSnackbar(
                                    if (isAdded) "${item.name.take(20)} ditambahkan ke Wishlist"
                                    else "${item.name.take(20)} dihapus dari Wishlist"
                                )
                            }
                        },
                        onAddToCart = { item ->
                            onAddToCart(item)
                            scope.launch {
                                snackbarHostState.showSnackbar("${item.name.take(20)}... berhasil ditambahkan")
                            }
                        }
                    )
                }
            }

            // Bottom Spacing
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
