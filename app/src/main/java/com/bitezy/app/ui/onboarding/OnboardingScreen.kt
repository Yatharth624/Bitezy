package com.bitezy.app.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// --- Theme Colors ---
private val Slate900 = Color(0xFF0F172A)
private val Slate800 = Color(0xFF1E293B)
private val Slate700 = Color(0xFF334155)
private val Emerald500 = Color(0xFF10B981)
private val Emerald400 = Color(0xFF34D399)
private val Teal300 = Color(0xFF5EEAD4)
private val TextWhite = Color(0xFFF1F5F9)
private val TextSecondary = Color(0xFF94A3B8)

data class QuestionnaireResult(
    val name: String,
    val goal: String,
    val weightKg: Float,
    val heightCm: Float
)

data class DietPlan(
    val calories: Int,
    val protein: Int,
    val waterLitres: Int,
    val steps: Int,
    val description: String
)

import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.bitezy.app.data.local.AppDatabase
import com.bitezy.app.data.local.UserGoalEntity

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var result by remember { mutableStateOf<QuestionnaireResult?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }

    if (result == null) {
        QuestionnaireScreen(onFinished = { result = it })
    } else {
        SummaryScreen(
            name = result!!.name,
            goal = result!!.goal,
            weightKg = result!!.weightKg,
            heightCm = result!!.heightCm,
            onContinue = {
                val plan = generateDietPlan(goal = result!!.goal, weightKg = result!!.weightKg)
                scope.launch {
                    db.appDao().saveUserGoal(UserGoalEntity(
                        name = result!!.name,
                        goal = result!!.goal,
                        targetCalories = plan.calories,
                        targetProtein = plan.protein,
                        targetWater = plan.waterLitres,
                        targetSteps = plan.steps
                    ))
                    onFinish()
                }
            }
        )
    }
}

@Composable
fun QuestionnaireScreen(onFinished: (QuestionnaireResult) -> Unit) {
    // State to track the current step
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 7

    // User Data State
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Female") }
    var birthYear by remember { mutableIntStateOf(2000) }
    var heightCm by remember { mutableFloatStateOf(170f) }
    var weightKg by remember { mutableFloatStateOf(70f) }
    var goal by remember { mutableStateOf("Improve health") }
    var fastingExp by remember { mutableStateOf("First time") }

    fun nextStep() {
        if (currentStep < totalSteps - 1) {
            currentStep++
        } else {
            onFinished(
                QuestionnaireResult(
                    name = name,
                    goal = goal,
                    weightKg = weightKg,
                    heightCm = heightCm
                )
            )
        }
    }

    fun prevStep() {
        if (currentStep > 0) currentStep--
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Slate900)) {
        // --- AMBIENT BACKGROUND EFFECTS ---
        // Top Right Orb
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Emerald500.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )
        // Bottom Left Orb
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 100.dp)
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0D9488).copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent, // Transparent to show background
            topBar = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                    ) {
                        if (currentStep > 0) {
                            IconButton(
                                onClick = { prevStep() },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(44.dp)
                                    .background(Slate800.copy(alpha = 0.5f), CircleShape)
                                    .border(1.dp, Slate700, CircleShape)
                            ) {
                                Icon(
                                    Icons.Rounded.ChevronLeft,
                                    contentDescription = "Back",
                                    tint = TextWhite
                                )
                            }
                        }

                        // Step Indicator (e.g., "1/7")
                        Text(
                            text = "${currentStep + 1} / $totalSteps",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }

                    // Segmented Progress Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(totalSteps) { index ->
                            val color = if (index <= currentStep) Emerald500 else Slate800
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(color)
                            )
                        }
                    }
                }
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                ) {
                    val isNextEnabled = currentStep != 0 || name.isNotBlank()

                    Button(
                        onClick = { nextStep() },
                        enabled = isNextEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Slate800
                        ),
                        contentPadding = PaddingValues(),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        // Gradient Button Background
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (isNextEnabled) Modifier.background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Emerald500,
                                                Color(0xFF059669)
                                            )
                                        )
                                    ) else Modifier.background(Slate800)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (currentStep == totalSteps - 1) "Finish" else "Next Step",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isNextEnabled) TextWhite else TextSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.Rounded.ChevronRight,
                                    contentDescription = null,
                                    tint = if (isNextEnabled) TextWhite else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width / 2 } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width / 2 } + fadeOut())
                        } else {
                            (slideInHorizontally { width -> -width / 2 } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> width / 2 } + fadeOut())
                        }
                    },
                    label = "step_transition"
                ) { step ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        when (step) {
                            0 -> StepName(name = name, onNameChange = { name = it })
                            1 -> StepGender(selected = gender, onSelect = { gender = it })
                            2 -> StepBirthYear(selected = birthYear, onSelect = { birthYear = it })
                            3 -> StepHeight(currentCm = heightCm, onChange = { heightCm = it })
                            4 -> StepWeight(
                                currentKg = weightKg,
                                userHeightCm = heightCm,
                                onChange = { weightKg = it })

                            5 -> StepGoal(selected = goal, onSelect = { goal = it })
                            6 -> StepFasting(selected = fastingExp, onSelect = { fastingExp = it })
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
//              SUMMARY SCREEN
// ==========================================

@Composable
fun SummaryScreen(
    name: String,
    goal: String,
    weightKg: Float,
    heightCm: Float,
    onContinue: () -> Unit
) {
    val plan = remember(goal, weightKg) { generateDietPlan(goal = goal, weightKg = weightKg) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
    ) {

        // Ambient glow
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Emerald500.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Your Plan is Ready",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            // ⬇ Extra space so it doesn't feel clustered
            Spacer(modifier = Modifier.height(12.dp))

            // Name in gradient
            Text(
                text = name.ifBlank { "Diet Mate User" },
                style = TextStyle(
                    brush = Brush.linearGradient(listOf(Emerald400, Teal300)),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Small pill showing goal
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Slate800.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Goal: $goal",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = plan.description,
                color = TextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Stats cards
            SummaryCard(label = "Daily Calories", value = "${plan.calories} kcal")
            Spacer(modifier = Modifier.height(18.dp))

            SummaryCard(label = "Protein Target", value = "${plan.protein} g")
            Spacer(modifier = Modifier.height(18.dp))

            SummaryCard(label = "Water Intake", value = "${plan.waterLitres} L")
            Spacer(modifier = Modifier.height(18.dp))

            SummaryCard(label = "Steps Goal", value = "${plan.steps} steps")

            Spacer(modifier = Modifier.weight(1f))

            // Start Tracking button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Emerald500, Color(0xFF059669))
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start Tracking",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Slate800.copy(alpha = 0.7f))
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                color = TextSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                value,
                color = Emerald400,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun generateDietPlan(goal: String, weightKg: Float): DietPlan {
    // Rough maintenance calories per kg (no activity questions yet)
    val maintenance = (weightKg * 32f).roundToInt()

    return when (goal) {
        "Lose weight" -> {
            val calories = (maintenance - 350).coerceAtLeast(1200)
            val protein = (weightKg * 1.8f).roundToInt()
            DietPlan(
                calories = calories,
                protein = protein,
                waterLitres = (weightKg * 0.04f).roundToInt().coerceAtLeast(2),
                steps = 9000,
                description = "We’ve set a slight calorie deficit to help you lose fat in a sustainable way while keeping protein high to protect your muscles."
            )
        }

        "Protein-rich diet", "Protein-rich Diet", "Protein rich diet" -> {
            val calories = (maintenance + 150)
            val protein = (weightKg * 2.0f).roundToInt()
            DietPlan(
                calories = calories,
                protein = protein,
                waterLitres = (weightKg * 0.045f).roundToInt().coerceAtLeast(3),
                steps = 8000,
                description = "Your plan prioritizes higher protein to support muscle growth, recovery and satiety, with a slight calorie surplus or maintenance."
            )
        }

        "Gain energy" -> {
            val calories = (maintenance + 200)
            val protein = (weightKg * 1.6f).roundToInt()
            DietPlan(
                calories = calories,
                protein = protein,
                waterLitres = (weightKg * 0.04f).roundToInt().coerceAtLeast(3),
                steps = 10000,
                description = "We’ve added a bit more carbs and overall calories so you feel more energetic throughout the day."
            )
        }

        "Longevity" -> {
            val protein = (weightKg * 1.4f).roundToInt()
            DietPlan(
                calories = maintenance,
                protein = protein,
                waterLitres = (weightKg * 0.04f).roundToInt().coerceAtLeast(3),
                steps = 8500,
                description = "Balanced calories with enough protein, good fats and fiber to support long-term health and prevention."
            )
        }

        "Maintain" -> {
            val protein = (weightKg * 1.6f).roundToInt()
            DietPlan(
                calories = maintenance,
                protein = protein,
                waterLitres = (weightKg * 0.035f).roundToInt().coerceAtLeast(2),
                steps = 8000,
                description = "We’ve kept you around maintenance so you can maintain your current weight while still eating in a structured, healthy way."
            )
        }

        "Improve health" -> {
            val protein = (weightKg * 1.6f).roundToInt()
            DietPlan(
                calories = maintenance,
                protein = protein,
                waterLitres = (weightKg * 0.04f).roundToInt().coerceAtLeast(3),
                steps = 8500,
                description = "This plan focuses on balanced calories, enough protein, hydration and movement to improve overall health markers."
            )
        }

        else -> {
            val protein = (weightKg * 1.6f).roundToInt()
            DietPlan(
                calories = maintenance,
                protein = protein,
                waterLitres = 3,
                steps = 8000,
                description = "A balanced plan based on your details. As you use Bitezy, we’ll fine-tune it more to your lifestyle."
            )
        }
    }
}


// ==========================================
//              STYLED COMPONENTS
// ==========================================

@Composable
fun StepHeader(text: String) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Emerald400, Teal300)
    )

    Text(
        text = text,
        style = TextStyle(
            brush = gradientBrush,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth()
    )
}

@Composable
fun SelectionCard(
    title: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Emerald400 else Slate700.copy(alpha = 0.5f)
    val containerColor =
        if (isSelected) Emerald500.copy(alpha = 0.15f) else Slate800.copy(alpha = 0.5f)
    val textColor = if (isSelected) Emerald400 else TextWhite

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = title,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Emerald400,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ==========================================
//              STEPS IMPL
// ==========================================

@Composable
fun StepName(name: String, onNameChange: (String) -> Unit) {
    StepHeader("What Is Your Name?")

    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        textStyle = TextStyle(
            color = TextWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        placeholder = {
            Text(
                "Enter Name",
                color = Slate700,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Emerald400,
            unfocusedBorderColor = Slate700,
            cursorColor = Emerald400,
            focusedContainerColor = Slate800.copy(alpha = 0.5f),
            unfocusedContainerColor = Slate800.copy(alpha = 0.5f)
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
    )
}

@Composable
fun StepGender(selected: String, onSelect: (String) -> Unit) {
    StepHeader("What's Your Gender?")
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        listOf("Male" to "👨", "Female" to "👩", "Other" to "👤").forEach { (label, icon) ->
            SelectionCard(label, icon, selected == label) { onSelect(label) }
        }
    }
}

@Composable
fun StepBirthYear(selected: Int, onSelect: (Int) -> Unit) {
    StepHeader("Year of Birth")
    val years = (1950..2015).toList().reversed()
    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = years.indexOf(selected).coerceAtLeast(0))

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 160.dp)
        ) {
            items(years) { year ->
                val isSelected = year == selected
                val alpha = if (isSelected) 1f else 0.4f

                Text(
                    text = year.toString(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = (if (isSelected) Emerald400 else TextWhite).copy(alpha = alpha),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .clickable { onSelect(year) }
                )
            }
        }

        // Fading Gradient Overlay for Wheel Effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Slate900,
                        0.4f to Color.Transparent,
                        0.6f to Color.Transparent,
                        1f to Slate900
                    )
                )
        )
    }
}

@Composable
fun StepHeight(currentCm: Float, onChange: (Float) -> Unit) {
    StepHeader("What Is Your Height?")
    var isCm by remember { mutableStateOf(true) }

    UnitToggle(isCm, "cm", "ft") { isCm = it }

    Spacer(modifier = Modifier.height(60.dp))

    val displayValue = if (isCm) "${currentCm.roundToInt()}" else convertCmToFt(currentCm)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = displayValue,
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            style = TextStyle(brush = Brush.linearGradient(listOf(Emerald400, Teal300)))
        )
        Text(
            text = if (isCm) "centimeters" else "feet",
            fontSize = 18.sp,
            color = TextSecondary
        )
    }

    Spacer(modifier = Modifier.height(40.dp))

    val range = if (isCm) 100f..250f else 3.28f..8.2f
    val currentVal = if (isCm) currentCm else currentCm / 30.48f

    CustomSlider(
        value = currentVal,
        range = range,
        onValueChange = {
            onChange(if (isCm) it else it * 30.48f)
        }
    )
}

@Composable
fun StepWeight(currentKg: Float, userHeightCm: Float, onChange: (Float) -> Unit) {
    StepHeader("What Is Your Weight?")
    var isKg by remember { mutableStateOf(true) }

    UnitToggle(isKg, "kg", "lbs") { isKg = it }

    Spacer(modifier = Modifier.height(40.dp))

    val displayValue =
        if (isKg) "${currentKg.roundToInt()}" else "${(currentKg * 2.20462).roundToInt()}"

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = displayValue,
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            style = TextStyle(brush = Brush.linearGradient(listOf(Emerald400, Teal300)))
        )
        Text(
            text = if (isKg) "kilograms" else "pounds",
            fontSize = 18.sp,
            color = TextSecondary
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    val range = if (isKg) 30f..150f else 66f..330f
    val currentVal = if (isKg) currentKg else currentKg * 2.20462f

    CustomSlider(value = currentVal, range = range) {
        onChange(if (isKg) it else it / 2.20462f)
    }

    Spacer(modifier = Modifier.height(40.dp))

    // BMI Logic
    val heightM = userHeightCm / 100
    val bmi = currentKg / (heightM * heightM)
    val (label, color) = when {
        bmi < 18.5 -> "Underweight" to Color(0xFF60A5FA)
        bmi < 25.0 -> "Normal" to Emerald400
        bmi < 30.0 -> "Overweight" to Color(0xFFFBBF24)
        else -> "Obese" to Color(0xFFF87171)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate800.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .border(1.dp, Slate700, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "BMI: ${String.format("%.1f", bmi)}",
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun StepGoal(selected: String, onSelect: (String) -> Unit) {
    StepHeader("Your Main Goal?")
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(
            listOf(
                "Lose weight" to "⚖️", "Improve health" to "🔥",
                "Gain energy" to "⚡", "Longevity" to "🌿", "Maintain" to "🛡️",
                "Protein-rich diet" to "🍗"
            )
        ) { (label, icon) ->
            SelectionCard(label, icon, selected == label) { onSelect(label) }
        }
    }
}

@Composable
fun StepFasting(selected: String, onSelect: (String) -> Unit) {
    StepHeader("Fasting Experience?")
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        listOf(
            "First time" to "🥜",
            "Tried a few times" to "🌱",
            "It's my lifestyle" to "🌳"
        ).forEach { (label, icon) ->
            SelectionCard(label, icon, selected == label) { onSelect(label) }
        }
    }
}

// ==========================================
//              HELPERS
// ==========================================

@Composable
fun UnitToggle(
    option1Selected: Boolean,
    label1: String,
    label2: String,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .background(Slate800, CircleShape)
            .border(1.dp, Slate700, CircleShape)
            .padding(4.dp)
    ) {
        TogglePill(label1, option1Selected) { onToggle(true) }
        Spacer(modifier = Modifier.width(4.dp))
        TogglePill(label2, !option1Selected) { onToggle(false) }
    }
}

@Composable
fun TogglePill(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) Emerald500 else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (isSelected) TextWhite else TextSecondary,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range,
        colors = SliderDefaults.colors(
            thumbColor = TextWhite,
            activeTrackColor = Emerald400,
            inactiveTrackColor = Slate700
        ),
        thumb = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(TextWhite, CircleShape)
                    .border(4.dp, Emerald500, CircleShape)
            )
        }
    )
}

fun convertCmToFt(cm: Float): String {
    val totalInches = cm / 2.54
    val feet = (totalInches / 12).toInt()
    val inches = (totalInches % 12).toInt()
    return "$feet'$inches\""
}
