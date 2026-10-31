package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CompletedLessonEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.*
import com.example.data.repository.EducationalContent
import com.example.data.repository.EducationalRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

sealed class AppScreen {
  object Home : AppScreen()
  object Courses : AppScreen()
  data class LessonDetail(val lessonId: String) : AppScreen()
  data class InteractiveLab(val processId: String) : AppScreen()
  object Flashcards : AppScreen()
  object FormulaHub : AppScreen()
  object Profile : AppScreen()
  data class Quiz(val lessonId: String? = null) : AppScreen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val database = AppDatabase.getDatabase(application)
  private val repository = EducationalRepository(database.omniLearnDao())

  // Navigation State Stack
  private val _screenStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.Home))
  val currentScreen: StateFlow<AppScreen> = _screenStack
    .map { it.lastOrNull() ?: AppScreen.Home }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppScreen.Home)

  // User Progress
  val userProgress: StateFlow<UserProgressEntity> = repository.userProgress
    .map { it ?: UserProgressEntity() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProgressEntity())

  // Completed Lessons
  val completedLessons: StateFlow<List<CompletedLessonEntity>> = repository.completedLessons
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Bookmarks
  val bookmarkedLessonIds: StateFlow<Set<String>> = repository.bookmarks
    .map { list -> list.map { it.lessonId }.toSet() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

  // Flashcards Progress Map (cardId -> masteryStatus)
  val flashcardMasteryMap: StateFlow<Map<String, Int>> = repository.flashcardProgress
    .map { list -> list.associate { it.cardId to it.masteryStatus } }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

  // Search & Category Filters
  val selectedCategoryFilter = MutableStateFlow<SubjectCategory?>(null)
  val searchQuery = MutableStateFlow("")

  // Filtered Lessons
  val filteredLessons: StateFlow<List<Lesson>> = combine(
    selectedCategoryFilter,
    searchQuery
  ) { category, query ->
    var list = repository.getLessons()
    if (category != null) {
      list = list.filter { it.subjectCategory == category }
    }
    if (query.isNotBlank()) {
      list = list.filter {
        it.title.contains(query, ignoreCase = true) ||
          it.subtitle.contains(query, ignoreCase = true) ||
          it.sections.any { s -> s.content.contains(query, ignoreCase = true) }
      }
    }
    list
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getLessons())

  // TTS Reader
  private var textToSpeech: TextToSpeech? = null
  val isTtsSpeaking = MutableStateFlow(false)

  init {
    textToSpeech = TextToSpeech(application) { status ->
      if (status == TextToSpeech.SUCCESS) {
        textToSpeech?.language = Locale.US
      }
    }
  }

  // Navigation Methods
  fun navigateTo(screen: AppScreen) {
    if (_screenStack.value.lastOrNull() == screen) return
    _screenStack.update { it + screen }
  }

  fun navigateBack(): Boolean {
    if (_screenStack.value.size > 1) {
      stopTts()
      _screenStack.update { it.dropLast(1) }
      return true
    }
    return false
  }

  // Educational Actions
  fun getLessons(): List<Lesson> = repository.getLessons()

  fun getLesson(id: String): Lesson? = repository.getLessonById(id)

  fun getProcess(processId: String): InteractiveProcess? =
    repository.getInteractiveProcess(processId)

  fun getFlashcards(): List<FlashcardItem> = repository.getFlashcards()

  fun getFormulas(): List<FormulaItem> = repository.getFormulas()

  fun isLessonCompleted(lessonId: String): Boolean =
    completedLessons.value.any { it.lessonId == lessonId }

  fun isLessonBookmarked(lessonId: String): Boolean =
    bookmarkedLessonIds.value.contains(lessonId)

  fun toggleBookmark(lessonId: String) {
    viewModelScope.launch {
      val isBookmarked = isLessonBookmarked(lessonId)
      repository.toggleBookmark(lessonId, isBookmarked)
    }
  }

  fun completeLesson(lessonId: String, quizScorePercentage: Int = 100) {
    viewModelScope.launch {
      val lesson = repository.getLessonById(lessonId)
      val xp = lesson?.xpReward ?: 50
      repository.completeLesson(lessonId, quizScorePercentage, xp)
    }
  }

  fun awardXp(amount: Int) {
    viewModelScope.launch {
      repository.awardXp(amount)
    }
  }

  fun updateFlashcardStatus(cardId: String, status: Int) {
    viewModelScope.launch {
      repository.setFlashcardStatus(cardId, status)
    }
  }

  fun getLessonNote(lessonId: String): Flow<String> {
    return repository.getNoteForLesson(lessonId).map { it?.noteText ?: "" }
  }

  fun saveLessonNote(lessonId: String, noteText: String) {
    viewModelScope.launch {
      repository.saveNote(lessonId, noteText)
    }
  }

  // Text to speech narration for accessibility and auditory learning
  fun toggleTts(text: String) {
    if (isTtsSpeaking.value) {
      stopTts()
    } else {
      textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "OmniLearnTts")
      isTtsSpeaking.value = true
    }
  }

  fun stopTts() {
    textToSpeech?.stop()
    isTtsSpeaking.value = false
  }

  override fun onCleared() {
    super.onCleared()
    textToSpeech?.stop()
    textToSpeech?.shutdown()
  }
}
