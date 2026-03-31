package com.bitezy.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitezy.app.R

@Composable
fun GetStartedScreen(onGetStartedClick: () -> Unit) {
    // 1. Color Palette (Matching the Emerald/Slate Theme)
    val Slate900 = Color(0xFF0F172A)
    val Emerald500 = Color(0xFF10B981)
    val Teal300 = Color(0xFF5EEAD4)
    val Emerald400 = Color(0xFF34D399)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .align(Alignment.TopCenter)
                .background(Color.DarkGray)
        )
         Image(
            painter = painterResource(id = R.drawable.healthybowl3),
            contentDescription = "Background",
           modifier = Modifier
              .fillMaxWidth()
               .fillMaxHeight(0.65f) // Covers top 65%
              .align(Alignment.TopCenter),
          contentScale = ContentScale.Crop,
           alpha = 0.9f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Slate900.copy(alpha = 0.1f), // Top (Transparent-ish)
                            Slate900.copy(alpha = 0.7f), // Middle
                            Slate900,                    // Bottom (Solid)
                            Slate900
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // --- LAYER 3: Ambient Glowing Orbs ---
        // Top Right Orb
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = 180.dp)
                .size(250.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Emerald500.copy(alpha = 0.2f), Color.Transparent)
                    )
                )
        )
        // Bottom Left Orb
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-40).dp, y = 40.dp)
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF0D9488).copy(alpha = 0.2f), Color.Transparent)
                    )
                )
        )

        // --- LAYER 4: Content Container ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .fillMaxWidth()
        ) {
            // 4a. Glassmorphism Logo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Emerald500.copy(alpha = 0.1f))
                    .border(1.dp, Emerald400.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Eco, // Using Material Icon "Eco" as leaf
                    contentDescription = "Logo",
                    tint = Emerald400,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4b. Typography with Gradient Text
            Text(
                text = "Transform Your Body,",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 44.sp
            )

            // Gradient Text Logic
            val gradientBrush = Brush.horizontalGradient(
                colors = listOf(Emerald400, Teal300, Color(0xFFA7F3D0))
            )

            Text(
                text = "One Meal at a Time.",
                style = MaterialTheme.typography.displaySmall.copy(
                    brush = gradientBrush,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                lineHeight = 44.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Achieve your goals with personalized meal plans and intelligent tracking. Let's build a healthier you.",
                color = Color(0xFFCBD5E1), // Slate-300
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 4c. Main CTA Button with Gradient Background
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent // Make container transparent for gradient
                ),
                contentPadding = PaddingValues() // Remove default padding
            ) {
                // Gradient Box inside the button
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF10B981), Color(0xFF0D9488))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Get Started Now",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
