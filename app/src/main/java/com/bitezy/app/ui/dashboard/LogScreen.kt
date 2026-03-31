package com.bitezy.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitezy.app.data.ai.GeminiResult
import com.bitezy.app.data.ai.GeminiService
import com.bitezy.app.data.local.AppDatabase
import com.bitezy.app.data.local.FoodLogEntity
import com.bitezy.app.ui.theme.GreenPrimaryLight
import kotlinx.coroutines.launch

@Composable
fun LogScreen(onBack: () -> Unit) {
    var textInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<GeminiResult?>(null) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val aiService = remember { GeminiService() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .padding(top = 40.dp)
    ) {
        Text("Log a Meal", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Text("What did you eat today?", color = Color.Gray, fontSize = 16.sp)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            placeholder = { Text("e.g., 2 scambled eggs and a slice of toast") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimaryLight,
                unfocusedBorderColor = Color.LightGray
            ),
            maxLines = 5
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                if (textInput.isNotBlank()) {
                    isLoading = true
                    result = null
                    scope.launch {
                        result = aiService.analyzeFood(textInput)
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimaryLight)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Analyze with AI ✨", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        result?.let { gemini ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(gemini.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Calories", color = Color.DarkGray)
                        Text("${gemini.calories} kcal", fontWeight = FontWeight.Bold, color = GreenPrimaryLight)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Protein: ${gemini.protein}g", color = Color.Gray)
                        Text("Carbs: ${gemini.carbs}g", color = Color.Gray)
                        Text("Fat: ${gemini.fat}g", color = Color.Gray)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("💡 Tip: ${gemini.tip}", color = Color.DarkGray, fontSize = 14.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            scope.launch {
                                val log = FoodLogEntity(
                                    mealName = gemini.name,
                                    calories = gemini.calories,
                                    protein = gemini.protein,
                                    carbs = gemini.carbs,
                                    fat = gemini.fat,
                                    foodScore = gemini.foodScore,
                                    tip = gemini.tip
                                )
                                db.appDao().insertFoodLog(log)
                                onBack() // Return to dashboard
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Confirm & Save", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
