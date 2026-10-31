package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.EduBluePrimary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        OmniLearnApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun OmniLearnApp(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val userProgress by viewModel.userProgress.collectAsStateWithLifecycle()
  val completedLessons by viewModel.completedLessons.collectAsStateWithLifecycle()
  val bookmarkedLessonIds by viewModel.bookmarkedLessonIds.collectAsStateWithLifecycle()
  val flashcardMasteryMap by viewModel.flashcardMasteryMap.collectAsStateWithLifecycle()
  val filteredLessons by viewModel.filteredLessons.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val isTtsSpeaking by viewModel.isTtsSpeaking.collectAsStateWithLifecycle()

  val completedIdsSet = remember(completedLessons) {
    completedLessons.map { it.lessonId }.toSet()
  }

  // Handle system back navigation
  BackHandler(enabled = currentScreen !is AppScreen.Home) {
    viewModel.navigateBack()
  }

  val isBottomBarVisible = currentScreen is AppScreen.Home ||
    currentScreen is AppScreen.Courses ||
    currentScreen is AppScreen.Flashcards ||
    currentScreen is AppScreen.FormulaHub ||
    currentScreen is AppScreen.Profile

  Scaffold(
    modifier = modifier.fillMaxSize(),
    bottomBar = {
      if (isBottomBarVisible) {
        NavigationBar(
          modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("omnilearn_bottom_navigation")
        ) {
          NavigationBarItem(
            selected = currentScreen is AppScreen.Home,
            onClick = { viewModel.navigateTo(AppScreen.Home) },
            icon = {
              Icon(
                imageVector = if (currentScreen is AppScreen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                contentDescription = "Home"
              )
            },
            label = { Text("Home") },
            modifier = Modifier.testTag("nav_item_home")
          )

          NavigationBarItem(
            selected = currentScreen is AppScreen.Courses,
            onClick = { viewModel.navigateTo(AppScreen.Courses) },
            icon = {
              Icon(
                imageVector = if (currentScreen is AppScreen.Courses) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                contentDescription = "Explore"
              )
            },
            label = { Text("Courses") },
            modifier = Modifier.testTag("nav_item_courses")
          )

          NavigationBarItem(
            selected = currentScreen is AppScreen.Flashcards,
            onClick = { viewModel.navigateTo(AppScreen.Flashcards) },
            icon = {
              Icon(
                imageVector = if (currentScreen is AppScreen.Flashcards) Icons.Filled.Style else Icons.Outlined.Style,
                contentDescription = "Flashcards"
              )
            },
            label = { Text("Recall") },
            modifier = Modifier.testTag("nav_item_flashcards")
          )

          NavigationBarItem(
            selected = currentScreen is AppScreen.FormulaHub,
            onClick = { viewModel.navigateTo(AppScreen.FormulaHub) },
            icon = {
              Icon(
                imageVector = if (currentScreen is AppScreen.FormulaHub) Icons.Filled.Functions else Icons.Outlined.Functions,
                contentDescription = "Formulas"
              )
            },
            label = { Text("Theories") },
            modifier = Modifier.testTag("nav_item_formulas")
          )

          NavigationBarItem(
            selected = currentScreen is AppScreen.Profile,
            onClick = { viewModel.navigateTo(AppScreen.Profile) },
            icon = {
              Icon(
                imageVector = if (currentScreen is AppScreen.Profile) Icons.Filled.Person else Icons.Outlined.Person,
                contentDescription = "Profile"
              )
            },
            label = { Text("Profile") },
            modifier = Modifier.testTag("nav_item_profile")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(
          top = if (isBottomBarVisible) innerPadding.calculateTopPadding() else 0.dp,
          bottom = if (isBottomBarVisible) innerPadding.calculateBottomPadding() else 0.dp
        )
    ) {
      AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
          fadeIn() togetherWith fadeOut()
        },
        label = "ScreenTransition"
      ) { screen ->
        when (screen) {
          is AppScreen.Home -> {
            HomeScreen(
              userProgress = userProgress,
              lessons = filteredLessons,
              completedLessonIds = completedIdsSet,
              onNavigateTo = { viewModel.navigateTo(it) }
            )
          }

          is AppScreen.Courses -> {
            CoursesScreen(
              lessons = filteredLessons,
              completedLessonIds = completedIdsSet,
              searchQuery = searchQuery,
              onSearchQueryChange = { viewModel.searchQuery.value = it },
              selectedCategory = selectedCategory,
              onSelectCategory = { viewModel.selectedCategoryFilter.value = it },
              onNavigateTo = { viewModel.navigateTo(it) }
            )
          }

          is AppScreen.LessonDetail -> {
            val lesson = viewModel.getLesson(screen.lessonId)
            if (lesson != null) {
              val interactiveProcess = lesson.interactiveProcessId?.let { viewModel.getProcess(it) }
              val noteFlow = remember(lesson.id) { viewModel.getLessonNote(lesson.id) }
              val savedNote by noteFlow.collectAsStateWithLifecycle(initialValue = "")

              LessonDetailScreen(
                lesson = lesson,
                interactiveProcess = interactiveProcess,
                isBookmarked = bookmarkedLessonIds.contains(lesson.id),
                isCompleted = completedIdsSet.contains(lesson.id),
                savedNote = savedNote,
                isTtsSpeaking = isTtsSpeaking,
                onToggleBookmark = { viewModel.toggleBookmark(lesson.id) },
                onToggleTts = { viewModel.toggleTts(it) },
                onSaveNote = { viewModel.saveLessonNote(lesson.id, it) },
                onCompleteLesson = { viewModel.completeLesson(lesson.id) },
                onTakeQuiz = { viewModel.navigateTo(AppScreen.Quiz(lesson.id)) },
                onBack = { viewModel.navigateBack() }
              )
            } else {
              Box(modifier = Modifier.fillMaxSize()) {
                Text("Lesson not found.")
              }
            }
          }

          is AppScreen.InteractiveLab -> {
            val process = viewModel.getProcess(screen.processId)
            if (process != null) {
              InteractiveLabScreen(
                process = process,
                onAwardXp = { viewModel.awardXp(it) },
                onBack = { viewModel.navigateBack() }
              )
            } else {
              Box(modifier = Modifier.fillMaxSize()) {
                Text("Interactive lab not found.")
              }
            }
          }

          is AppScreen.Flashcards -> {
            FlashcardsScreen(
              flashcards = viewModel.getFlashcards(),
              masteryMap = flashcardMasteryMap,
              onUpdateStatus = { cardId, status ->
                viewModel.updateFlashcardStatus(cardId, status)
              }
            )
          }

          is AppScreen.FormulaHub -> {
            FormulaHubScreen(formulas = viewModel.getFormulas())
          }

          is AppScreen.Profile -> {
            ProfileScreen(
              userProgress = userProgress,
              completedLessonCount = completedLessons.size,
              masteredFlashcardsCount = flashcardMasteryMap.values.count { it == 2 }
            )
          }

          is AppScreen.Quiz -> {
            val lesson = screen.lessonId?.let { viewModel.getLesson(it) }
            val quizTitle = lesson?.let { "${it.title} Quiz" } ?: "Daily Knowledge Sprint"
            val quizQuestions = lesson?.quizQuestions ?: viewModel.getLessons().flatMap { it.quizQuestions }.shuffled().take(5)

            QuizScreen(
              quizTitle = quizTitle,
              questions = quizQuestions,
              onCompleteQuiz = { scorePercentage, xpEarned ->
                if (lesson != null) {
                  viewModel.completeLesson(lesson.id, scorePercentage)
                } else {
                  viewModel.awardXp(xpEarned)
                }
              },
              onBack = { viewModel.navigateBack() }
            )
          }
        }
      }
    }
  }
}
