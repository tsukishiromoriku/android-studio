package com.ecommerce.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ecommerce.app.data.ProductRepository
import com.ecommerce.app.model.Product
import com.ecommerce.app.ui.theme.AmberRating
import com.ecommerce.app.ui.theme.BorderLight
import com.ecommerce.app.ui.theme.CoralBadge
import com.ecommerce.app.ui.theme.TextPrimary
import com.ecommerce.app.ui.theme.TextSecondary
import com.ecommerce.app.ui.theme.TextTertiary

@Composable
fun ProductCard(
    product: Product,
    onProductClick: (String) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier,
    isProductFavorite: Boolean = product.isFavorite,
    onToggleWishlist: (Product) -> Unit = {}
) {
    var isFavorite by remember(isProductFavorite) { mutableStateOf(isProductFavorite) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onProductClick(product.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            // Image with Overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color(0xFFF1F5F9))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Favorite Wishlist Button (Properly sized and fitted)
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.92f))
                        .clickable {
                            isFavorite = !isFavorite
                            onToggleWishlist(product)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorit",
                        tint = if (isFavorite) CoralBadge else TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Product Details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Brand / Category Tag
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = TextTertiary
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Product Name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp
                    ),
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(36.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating & Sold Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = AmberRating,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = product.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = "• ${product.soldCount} terjual",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp
                        ),
                        color = TextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Price and Add to Cart Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = ProductRepository.formatRupiah(product.price),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (product.originalPrice != null) {
                            Text(
                                text = ProductRepository.formatRupiah(product.originalPrice),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 10.sp,
                                    textDecoration = TextDecoration.LineThrough
                                ),
                                color = TextTertiary
                            )
                        }
                    }

                    // Add Button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { onAddToCart(product) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah ke Keranjang",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
