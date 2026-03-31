package com.bitezy.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bitezy.app.data.local.UserGoalEntity
import com.bitezy.app.ui.theme.*

@Composable
fun GoalsScreen(viewModel: DashboardViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 48.dp, bottom = 120.dp)
    ) {
        item {
            Text("My goal", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(24.dp))
            GoalSelector(state.goal?.goal ?: "Maintain")
            Spacer(modifier = Modifier.height(24.dp))
            GoalTargetCard(state.goal)
            Spacer(modifier = Modifier.height(32.dp))
            WeeklySummary(state)
            Spacer(modifier = Modifier.height(32.dp))
            AiTipWidget()
        }
    }
}

@Composable
fun GoalSelector(activeGoalString: String) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PillChoice(text = "Lose weight", isSelected = activeGoalString.contains("Lose", ignoreCase = true))
            PillChoice(text = "Protein-rich diet", isSelected = activeGoalString.contains("Protein", ignoreCase = true))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PillChoice(text = "Gain energy", isSelected = activeGoalString.contains("energy", ignoreCase = true))
            PillChoice(text = "Maintain", isSelected = activeGoalString.contains("Maintain", ignoreCase = true))
        }
    }
}

@Composable
fun PillChoice(text: String, isSelected: Boolean) {
    val bkgColor = if (isSelected) GreenPrimaryLight else Color.Transparent
    val txtColor = if (isSelected) Color.White else Color.Gray
    val strokeColor = if (isSelected) GreenPrimaryLight else Color.LightGray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bkgColor)
            .border(1.dp, strokeColor, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = txtColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun GoalTargetCard(goal: UserGoalEntity?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GreenSecondaryLight)
            .padding(20.dp)
    ) {
        Text("${goal?.goal ?: "Setup Target"} target", color = GreenPrimaryLight, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${goal?.targetCalories ?: 2000} kcal/day · Target: ${goal?.targetProtein ?: 120}g protein", color = Color(0xFF2E4B11), fontSize = 16.sp, lineHeight = 22.sp)
    }
}

@Composable
fun WeeklySummary(state: DashboardState) {
    val targetCals = state.goal?.targetCalories ?: 2000
    val targetSteps = state.goal?.targetSteps ?: 8000

    Text("Today's Snapshot", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    Spacer(modifier = Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MacroLine(name = "Daily kcal progress", current = state.totalCalories, total = targetCals, label = "${state.totalCalories} / $targetCals", color = GreenPrimaryLight)
        MacroLine(name = "Step goal", current = state.steps.toInt(), total = targetSteps, label = "${state.steps} / $targetSteps", color = MacroProteinBlue)
        MacroLine(name = "Food score avg", current = (state.avgFoodScore * 10).toInt(), total = 100, label = String.format("%.1f / 10", state.avgFoodScore), color = MacroCarbOrange)
    }
}

@Composable
fun AiTipWidget() {
    Text("AI tip", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9FAFB))
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(GreenPrimaryLight)
        )
        Text(
            "Your database is now fully wired up! Keep logging meals to give Gemini more context for personalized tips.",
            modifier = Modifier.padding(16.dp),
            color = Color.DarkGray,
            fontSize = 15.sp,
            lineHeight = 22.sp
        )
    }
}
