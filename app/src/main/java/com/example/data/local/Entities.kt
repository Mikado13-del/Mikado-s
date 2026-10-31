package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
  @PrimaryKey val id: Int = 1,
  val totalXp: Int = 150,
  val currentStreakDays: Int = 3,
  val lastActiveDate: String = "",
  val dailyGoalLessons: Int = 3,
  val todayCompletedCount: Int = 1,
  val todayDateString: String = "",
  val lessonsCompletedCount: Int = 0
)

@Entity(tableName = "completed_lessons")
data class CompletedLessonEntity(
  @PrimaryKey val lessonId: String,
  val completedAt: Long = System.currentTimeMillis(),
  val quizScorePercentage: Int = 100
)

@Entity(tableName = "flashcard_progress")
data class FlashcardProgressEntity(
  @PrimaryKey val cardId: String,
  val masteryStatus: Int = 0, // 0 = New, 1 = Reviewing, 2 = Mastered
  val lastReviewedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_notes")
data class SavedNoteEntity(
  @PrimaryKey val lessonId: String,
  val noteText: String,
  val lastModified: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarked_lessons")
data class BookmarkEntity(
  @PrimaryKey val lessonId: String,
  val addedAt: Long = System.currentTimeMillis()
)
