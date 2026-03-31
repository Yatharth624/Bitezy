package com.bitezy.app.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitezy.app.ui.theme.GreenPrimaryLight
import com.bitezy.app.ui.theme.GreenSecondaryLight
import com.bitezy.app.ui.theme.MacroFatRed
import kotlin.random.Random

@Composable
fun MonthScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 48.dp, bottom = 120.dp)
    ) {
        item {
            Text("This month", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(8.dp))
            
            // Primary Metric
            Row(verticalAlignment = Alignment.Bottom) {
                Text("+2.4%", fontSize = 48.sp, fontWeight = FontWeight.Black, color = GreenPrimaryLight)
                Spacer(modifier = Modifier.width(8.dp))
                Text("kcal balance", fontSize = 18.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            // Bar Chart
            ThirtyDayBarChart()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Insight Card
            MonthInsightCard()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Average Score Card
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SmallMetricCard(label = "Avg food score", value = "7.4", modifier = Modifier.weight(1f))
                SmallMetricCard(label = "Days tracked", value = "18", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ThirtyDayBarChart() {
    // Generate 30 days of random data for the static view
    // Ranges from -500 to +500 kcal deficit/surplus
    val mockData = rememberMockData()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val zeroY = canvasHeight / 2f
            
            val barCount = mockData.size
            val barSpacing = 8.dp.toPx()
            val totalSpacing = barSpacing * (barCount - 1)
            val barWidth = (canvasWidth - totalSpacing) / barCount
            
            // Draw Zero Line
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(0f, zeroY),
                end = Offset(canvasWidth, zeroY),
                strokeWidth = 2.dp.toPx()
            )

            // Draw Bars
            mockData.forEachIndexed { index, value ->
                val xPos = index * (barWidth + barSpacing)
                
                // Scale value to max chart height (500 kcal = half height)
                val normalizedValue = (value / 500f) * (canvasHeight / 2f)
                
                val color = if (value < 0) GreenPrimaryLight else MacroFatRed
                val yStart = if (value < 0) zeroY else zeroY - normalizedValue
                val height = kotlin.math.abs(normalizedValue).coerceAtLeast(4f) // min height so zeroes show slightly

                drawRoundRect(
                    color = color,
                    topLeft = Offset(xPos, yStart),
                    size = Size(barWidth, height),
                    cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                )
            }
        }
    }
}

@Composable
fun MonthInsightCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GreenSecondaryLight)
            .padding(20.dp)
    ) {
        Text("Monthly Review", color = GreenPrimaryLight, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "You have eaten approximately 1,200 more calories than you burned this month. Stick slightly closer to your targets to hit the deficit goal.",
            color = Color(0xFF2E4B11), 
            fontSize = 15.sp, 
            lineHeight = 22.sp
        )
    }
}

@Composable
fun SmallMetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F5F9))
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = Color.Gray, fontSize = 14.sp)
    }
}

fun rememberMockData(): List<Float> {
    val seed = Random(42) // Fixed seed for consistent UI
    return List(30) {
        // Values between -400 and +300
        seed.nextInt(-400, 300).toFloat()
    }
}
