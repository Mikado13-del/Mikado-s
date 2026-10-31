package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EducationalRepository(
  private val dao: OmniLearnDao
) {
  val userProgress: Flow<UserProgressEntity?> = dao.getUserProgress()
  val completedLessons: Flow<List<CompletedLessonEntity>> = dao.getAllCompletedLessons()
  val bookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
  val flashcardProgress: Flow<List<FlashcardProgressEntity>> = dao.getAllFlashcardProgress()
  val savedNotes: Flow<List<SavedNoteEntity>> = dao.getAllSavedNotes()

  fun getLessons(): List<Lesson> = EducationalContent.Lessons

  fun getLessonById(id: String): Lesson? =
    EducationalContent.Lessons.find { it.id == id }

  fun getInteractiveProcess(processId: String): InteractiveProcess? {
    return if (processId == EducationalContent.WaterCycleProcess.id) {
      EducationalContent.WaterCycleProcess
    } else {
      null
    }
  }

  fun getFlashcards(): List<FlashcardItem> = EducationalContent.Flashcards

  fun getFormulas(): List<FormulaItem> = EducationalContent.Formulas

  fun getNoteForLesson(lessonId: String): Flow<SavedNoteEntity?> =
    dao.getNoteForLesson(lessonId)

  fun isLessonBookmarked(lessonId: String): Flow<Boolean> =
    dao.isBookmarked(lessonId)

  suspend fun completeLesson(lessonId: String, scorePercentage: Int, xpReward: Int) {
    dao.insertCompletedLesson(
      CompletedLessonEntity(
        lessonId = lessonId,
        completedAt = System.currentTimeMillis(),
        quizScorePercentage = scorePercentage
      )
    )

    // Update user streak and progress
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val current = dao.getUserProgress().firstOrNull() ?: UserProgressEntity()

    val newTodayCount = if (current.todayDateString == today) {
      current.todayCompletedCount + 1
    } else {
      1
    }

    val newStreak = if (current.lastActiveDate == today) {
      current.currentStreakDays
    } else {
      current.currentStreakDays + 1
    }

    dao.upsertUserProgress(
      current.copy(
        totalXp = current.totalXp + xpReward,
        currentStreakDays = newStreak,
        lastActiveDate = today,
        todayCompletedCount = newTodayCount,
        todayDateString = today,
        lessonsCompletedCount = current.lessonsCompletedCount + 1
      )
    )
  }

  suspend fun awardXp(amount: Int) {
    val current = dao.getUserProgress().firstOrNull() ?: UserProgressEntity()
    dao.upsertUserProgress(
      current.copy(totalXp = current.totalXp + amount)
    )
  }

  suspend fun setFlashcardStatus(cardId: String, status: Int) {
    dao.upsertFlashcardProgress(
      FlashcardProgressEntity(
        cardId = cardId,
        masteryStatus = status,
        lastReviewedTimestamp = System.currentTimeMillis()
      )
    )
    if (status == 2) { // Mastered
      awardXp(15)
    }
  }

  suspend fun saveNote(lessonId: String, noteText: String) {
    if (noteText.isBlank()) {
      dao.deleteNote(lessonId)
    } else {
      dao.upsertNote(
        SavedNoteEntity(
          lessonId = lessonId,
          noteText = noteText,
          lastModified = System.currentTimeMillis()
        )
      )
    }
  }

  suspend fun toggleBookmark(lessonId: String, currentlyBookmarked: Boolean) {
    if (currentlyBookmarked) {
      dao.removeBookmark(lessonId)
    } else {
      dao.addBookmark(BookmarkEntity(lessonId = lessonId))
    }
  }
}
