package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizQuestion
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  quizTitle: String,
  questions: List<QuizQuestion>,
  onCompleteQuiz: (scorePercentage: Int, xpEarned: Int) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var currentQuestionIndex by remember { mutableIntStateOf(0) }
  var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
  var isSubmitted by remember { mutableStateOf(false) }
  var correctCount by remember { mutableIntStateOf(0) }
  var isQuizFinished by remember { mutableStateOf(false) }

  val question = questions.getOrNull(currentQuestionIndex)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = quizTitle,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_quiz")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp),
      contentAlignment = Alignment.Center
    ) {
      if (isQuizFinished) {
        // Quiz Results Summary Screen
        val percentage = ((correctCount.toFloat() / questions.size) * 100).toInt()
        val xpEarned = correctCount * 25

        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_results_card")
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(if (percentage >= 70) EduEmerald.copy(alpha = 0.2f) else EduAmber.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (percentage >= 70) Icons.Default.EmojiEvents else Icons.Default.School,
                contentDescription = null,
                tint = if (percentage >= 70) EduEmerald else EduAmber,
                modifier = Modifier.size(40.dp)
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
              text = if (percentage >= 70) "Mastery Achieved!" else "Good Effort!",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "You scored $correctCount out of ${questions.size} ($percentage%)",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = EduAmber.copy(alpha = 0.15f)
            ) {
              Text(
                text = "+$xpEarned XP Awarded",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = EduAmber,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
              )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = {
                onCompleteQuiz(percentage, xpEarned)
                onBack()
              },
              colors = ButtonDefaults.buttonColors(containerColor = EduBluePrimary),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_finish_quiz")
            ) {
              Text("Save Progress & Return")
            }
          }
        }
      } else if (question != null) {
        // Active Question Screen
        Column(
          modifier = Modifier
            .fillMaxSize()
            .testTag("active_question_view"),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            // Progress Bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "${((currentQuestionIndex.toFloat() / questions.size) * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
              progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
              color = EduBluePrimary,
              trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Question Text Card
            Text(
              text = question.question,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Options
            question.options.forEachIndexed { index, option ->
              val isSelected = selectedAnswerIndex == index
              val isCorrect = index == question.correctOptionIndex

              val (bgColor, textColor) = when {
                !isSubmitted && isSelected -> EduBluePrimary.copy(alpha = 0.2f) to EduBluePrimary
                !isSubmitted -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
                isCorrect -> EduEmerald.copy(alpha = 0.2f) to EduEmerald
                isSelected && !isCorrect -> EduRose.copy(alpha = 0.2f) to EduRose
                else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
              }

              Surface(
                shape = RoundedCornerShape(14.dp),
                color = bgColor,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (!isSubmitted && isSelected) EduBluePrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 5.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .clickable(enabled = !isSubmitted) { selectedAnswerIndex = index }
                  .testTag("quiz_option_$index")
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(28.dp)
                      .clip(CircleShape)
                      .background(
                        if (isSubmitted && isCorrect) EduEmerald
                        else if (isSubmitted && isSelected) EduRose
                        else if (isSelected) EduBluePrimary
                        else MaterialTheme.colorScheme.surfaceVariant
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = ('A' + index).toString(),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = if (isSelected || (isSubmitted && (isCorrect || isSelected))) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }

            // Explanation Reveal
            if (isSubmitted) {
              Spacer(modifier = Modifier.height(14.dp))
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (selectedAnswerIndex == question.correctOptionIndex) EduEmerald.copy(alpha = 0.12f) else EduAmber.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = question.explanation,
                  style = MaterialTheme.typography.bodySmall,
                  modifier = Modifier.padding(12.dp),
                  lineHeight = 18.sp
                )
              }
            }
          }

          // Bottom Action: Check Answer / Next Question
          Column(modifier = Modifier.fillMaxWidth()) {
            if (!isSubmitted) {
              Button(
                onClick = {
                  if (selectedAnswerIndex != null) {
                    isSubmitted = true
                    if (selectedAnswerIndex == question.correctOptionIndex) {
                      correctCount++
                    }
                  }
                },
                enabled = selectedAnswerIndex != null,
                colors = ButtonDefaults.buttonColors(containerColor = EduBluePrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("btn_check_answer")
              ) {
                Text("Verify Answer")
              }
            } else {
              Button(
                onClick = {
                  if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                    selectedAnswerIndex = null
                    isSubmitted = false
                  } else {
                    isQuizFinished = true
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EduEmerald),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("btn_next_question")
              ) {
                Text(if (currentQuestionIndex < questions.size - 1) "Next Question" else "See Final Results")
              }
            }
          }
        }
      }
    }
  }
}
