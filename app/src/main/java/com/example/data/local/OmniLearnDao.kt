package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OmniLearnDao {

  @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
  fun getUserProgress(): Flow<UserProgressEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertUserProgress(progress: UserProgressEntity)

  @Query("SELECT * FROM completed_lessons")
  fun getAllCompletedLessons(): Flow<List<CompletedLessonEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCompletedLesson(lesson: CompletedLessonEntity)

  @Query("SELECT * FROM flashcard_progress")
  fun getAllFlashcardProgress(): Flow<List<FlashcardProgressEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertFlashcardProgress(progress: FlashcardProgressEntity)

  @Query("SELECT * FROM saved_notes WHERE lessonId = :lessonId LIMIT 1")
  fun getNoteForLesson(lessonId: String): Flow<SavedNoteEntity?>

  @Query("SELECT * FROM saved_notes")
  fun getAllSavedNotes(): Flow<List<SavedNoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertNote(note: SavedNoteEntity)

  @Query("DELETE FROM saved_notes WHERE lessonId = :lessonId")
  suspend fun deleteNote(lessonId: String)

  @Query("SELECT * FROM bookmarked_lessons")
  fun getAllBookmarks(): Flow<List<BookmarkEntity>>

  @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_lessons WHERE lessonId = :lessonId)")
  fun isBookmarked(lessonId: String): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addBookmark(bookmark: BookmarkEntity)

  @Query("DELETE FROM bookmarked_lessons WHERE lessonId = :lessonId")
  suspend fun removeBookmark(lessonId: String)
}
