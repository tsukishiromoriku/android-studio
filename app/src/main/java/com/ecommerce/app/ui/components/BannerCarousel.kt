package com.ecommerce.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecommerce.app.ui.theme.BlueDark
import com.ecommerce.app.ui.theme.BluePrimary

@Composable
fun PromoBanner(
    modifier: Modifier = Modifier,
    tag: String = "PROMO SPESIAL HARI INI",
    title: String = "Diskon Kilat Hingga 50%",
    subtitle: String = "Gratis ongkir ke seluruh Indonesia tanpa min. belanja"
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            BluePrimary,
            BlueDark,
            Color(0xFF312E81)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(brush = gradientBrush)
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFB703))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tag,
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                ),
                color = Color.White
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp
                ),
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}
