package com.bitezy.app.ui.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bitezy.app.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun TodayScreen(viewModel: DashboardViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val healthConnectManager = viewModel.healthConnectManager

    val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

    val requestPermissionsLauncher = rememberLauncherForActivityResult(requestPermissionActivityContract) { granted ->
        if (granted.containsAll(healthConnectManager.permissions)) {
            viewModel.refreshSteps()
        }
    }

    LaunchedEffect(Unit) {
        if (healthConnectManager.isAvailable() && !healthConnectManager.hasAllPermissions()) {
            requestPermissionsLauncher.launch(healthConnectManager.permissions)
        } else if (healthConnectManager.hasAllPermissions()) {
            viewModel.refreshSteps()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 48.dp, bottom = 120.dp)
    ) {
        item {
            HeaderSection()
            Spacer(modifier = Modifier.height(32.dp))
            FoodScoreSection(state.avgFoodScore)
            Spacer(modifier = Modifier.height(32.dp))
            SummaryRow(state)
            Spacer(modifier = Modifier.height(32.dp))
            MacrosSection(state)
            Spacer(modifier = Modifier.height(32.dp))
            StepsWidget(state.steps, state.goal?.targetSteps ?: 8000)
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Today", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(GreenSecondaryLight)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text("Tracking Active", color = GreenPrimaryLight, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun FoodScoreSection(score: Float) {
    val displayScore = if (score == 0f) 0f else score
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular Progress Ring
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.LightGray.copy(alpha=0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = GreenPrimaryLight,
                    startAngle = -90f,
                    sweepAngle = 360f * (displayScore / 10f),
                    useCenter = false,
                    style = Stroke(8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(String.format("%.1f", displayScore), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
        
        Spacer(modifier = Modifier.width(24.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text("Food score", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(4.dp))
            Text(if (displayScore >= 7f) "Good balance!" else if (displayScore == 0f) "Log a meal to get a score" else "Needs improvement", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun SummaryRow(state: DashboardState) {
    val targetCals = state.goal?.targetCalories ?: 2000
    val remaining = targetCals - state.totalCalories

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SummaryMiniCard(value = "${state.totalCalories}", label = "kcal in", modifier = Modifier.weight(1f))
        SummaryMiniCard(value = "${targetCals}", label = "target", modifier = Modifier.weight(1f))
        SummaryMiniCard(value = "${remaining}", label = "remaining", modifier = Modifier.weight(1f))
    }
}

@Composable
fun SummaryMiniCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F5F9))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(label, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun MacrosSection(state: DashboardState) {
    val targetPro = state.goal?.targetProtein ?: 120
    val targetCarb = 250 // Hardcoded for MVP assuming flexible approach
    val targetFat = 65
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MacroLine(name = "Protein", current = state.totalProtein, total = targetPro, label = "${state.totalProtein}g / ${targetPro}g", color = MacroProteinBlue)
        MacroLine(name = "Carbs", current = state.totalCarbs, total = targetCarb, label = "${state.totalCarbs}g / ${targetCarb}g", color = MacroCarbOrange)
        MacroLine(name = "Fat", current = state.totalFat, total = targetFat, label = "${state.totalFat}g / ${targetFat}g", color = MacroFatRed)
    }
}

@Composable
fun MacroLine(name: String, current: Int, total: Int, label: String, color: Color) {
    val progress = if (total == 0) 0f else (current.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
            Text(label, fontSize = 14.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Color.LightGray.copy(alpha=0.3f)
        )
    }
}

@Composable
fun StepsWidget(steps: Long, stepTarget: Int) {
    val pct = if (stepTarget == 0) 0 else ((steps.toFloat() / stepTarget.toFloat()) * 100).roundToInt()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE0F2FE))
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("$steps", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
            Text("steps today", color = Color(0xFF0284C7), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Goal: $stepTarget", color = Color(0xFF0284C7), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("$pct% of goal", color = Color(0xFF0284C7).copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}
