package com.bitezy.app.data.ai

import android.graphics.Bitmap
import com.bitezy.app.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class GeminiResult(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val foodScore: Float,
    val tip: String
)

class GeminiService {
    // We strictly instruct Gemini to output pure JSON.
    private val systemInstruction = """
        You are a nutrition analyst. Estimate the macros for the provided food description or image.
        Return ONLY valid JSON with this exact structure, no markdown, no code blocks:
        {
          "name": "General Name of Meal",
          "calories": 250,
          "protein": 15,
          "carbs": 30,
          "fat": 5,
          "foodScore": 8.0,
          "tip": "Short actionable diet tip"
        }
        FoodScore should be out of 10.0 based on healthiness. Provide best-guess estimates if exact amounts are unknown.
    """.trimIndent()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun analyzeFood(textInput: String, image: Bitmap? = null): GeminiResult? = withContext(Dispatchers.IO) {
        try {
            val response = generativeModel.generateContent(
                content {
                    if (image != null) {
                        image(image)
                    }
                    text(systemInstruction)
                    text("User Input: $textInput")
                }
            )

            val rawText = response.text ?: return@withContext null
            // Sometimes Gemini wraps JSON in ```json ... ``` despite instructions. Clean it up.
            val cleanJsonStr = rawText.replace("```json", "", ignoreCase = true)
                .replace("```", "")
                .trim()
                
            val jsonObj = JSONObject(cleanJsonStr)

            GeminiResult(
                name = jsonObj.getString("name"),
                calories = jsonObj.getInt("calories"),
                protein = jsonObj.getInt("protein"),
                carbs = jsonObj.getInt("carbs"),
                fat = jsonObj.getInt("fat"),
                foodScore = jsonObj.getDouble("foodScore").toFloat(),
                tip = jsonObj.getString("tip")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
