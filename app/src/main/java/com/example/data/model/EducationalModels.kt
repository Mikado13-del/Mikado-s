package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class SubjectCategory(
  val displayName: String,
  val iconName: String,
  val color: Color,
  val tagline: String
) {
  SCIENCE("Earth & Physics", "Science", ScienceTint, "Thermodynamics, Hydrology & Cosmos"),
  TECHNOLOGY("Computer Science", "Terminal", TechTint, "Algorithms, AI & Networking"),
  MATHEMATICS("Mathematics", "Calculate", MathTint, "Calculus, Logic & Probability"),
  HISTORY("World History", "AccountBalance", HistoryTint, "Revolutions, Civilizations & Eras"),
  PHILOSOPHY("Critical Thinking", "Psychology", PhilosophyTint, "Cognitive Biases & Socratic Logic")
}

data class Subject(
  val id: String,
  val category: SubjectCategory,
  val title: String,
  val description: String,
  val lessonCount: Int
)

data class LessonSection(
  val title: String,
  val content: String,
  val keyFormulaOrConcept: String? = null,
  val didYouKnow: String? = null
)

data class Lesson(
  val id: String,
  val subjectCategory: SubjectCategory,
  val title: String,
  val subtitle: String,
  val readTimeMinutes: Int,
  val xpReward: Int = 50,
  val interactiveProcessId: String? = null,
  val sections: List<LessonSection>,
  val keyTakeaways: List<String>,
  val quizQuestions: List<QuizQuestion>
)

data class QuizQuestion(
  val id: String,
  val question: String,
  val options: List<String>,
  val correctOptionIndex: Int,
  val explanation: String
)

data class FlashcardItem(
  val id: String,
  val category: SubjectCategory,
  val question: String,
  val answer: String,
  val deepNote: String
)

data class FormulaItem(
  val id: String,
  val title: String,
  val formula: String,
  val category: SubjectCategory,
  val explanation: String,
  val application: String
)

// Interactive visual explanation model
data class ProcessStage(
  val id: String,
  val stepNumber: Int,
  val name: String,
  val shortDescription: String,
  val detailedScience: String,
  val thermodynamicMetric: String, // e.g. "ΔH_vap = +40.7 kJ/mol (Endothermic)"
  val atmosphericImpact: String,
  val visualHighlightArea: String, // "ocean", "clouds", "mountains", "canopy", "subsurface"
  val keyCheckpointQuestion: QuizQuestion
)

data class InteractiveProcess(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: SubjectCategory,
  val overview: String,
  val stages: List<ProcessStage>
)
