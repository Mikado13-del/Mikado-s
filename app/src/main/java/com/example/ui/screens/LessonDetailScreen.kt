package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InteractiveProcess
import com.example.data.model.Lesson
import com.example.ui.components.WaterCycleSimulationView
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
  lesson: Lesson,
  interactiveProcess: InteractiveProcess?,
  isBookmarked: Boolean,
  isCompleted: Boolean,
  savedNote: String,
  isTtsSpeaking: Boolean,
  onToggleBookmark: () -> Unit,
  onToggleTts: (textToRead: String) -> Unit,
  onSaveNote: (String) -> Unit,
  onCompleteLesson: () -> Unit,
  onTakeQuiz: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var fontScale by remember { mutableFloatStateOf(1.0f) }
  var noteText by remember(savedNote) { mutableStateOf(savedNote) }
  var showNotesEditor by remember { mutableStateOf(false) }

  // Prepare full speech text for TTS
  val speechText = remember(lesson) {
    buildString {
      append(lesson.title)
      append(". ")
      append(lesson.subtitle)
      append(". ")
      lesson.sections.forEach { section ->
        append(section.title)
        append(". ")
        append(section.content)
        append(". ")
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = lesson.subjectCategory.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_lesson")) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          // TTS Read Aloud Button
          IconButton(
            onClick = { onToggleTts(speechText) },
            modifier = Modifier.testTag("btn_tts_listen")
          ) {
            Icon(
              imageVector = if (isTtsSpeaking) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
              contentDescription = "Listen to Lesson",
              tint = if (isTtsSpeaking) EduAmber else MaterialTheme.colorScheme.onSurface
            )
          }

          // Bookmark Button
          IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.testTag("btn_toggle_bookmark")
          ) {
            Icon(
              imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
              contentDescription = "Bookmark Lesson",
              tint = if (isBookmarked) EduAmber else MaterialTheme.colorScheme.onSurface
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("lesson_detail_lazy_column"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Header & Title Info
      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = lesson.subjectCategory.color.copy(alpha = 0.15f)
            ) {
              Text(
                text = lesson.subjectCategory.displayName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = lesson.subjectCategory.color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            // Font Resizer
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Text Size",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clickable {
                  fontScale = when (fontScale) {
                    0.9f -> 1.0f
                    1.0f -> 1.2f
                    else -> 0.9f
                  }
                }
              ) {
                Text(
                  text = when (fontScale) {
                    0.9f -> "A (Sm)"
                    1.2f -> "A (Lg)"
                    else -> "A (Md)"
                  },
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = lesson.title,
            style = MaterialTheme.typography.titleLarge,
            fontSize = (24 * fontScale).sp,
            fontWeight = FontWeight.Bold,
            lineHeight = (30 * fontScale).sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = lesson.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = (15 * fontScale).sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${lesson.readTimeMinutes} min read",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = EduAmber.copy(alpha = 0.15f)
            ) {
              Text(
                text = "+${lesson.xpReward} XP",
                style = MaterialTheme.typography.labelSmall,
                color = EduAmber,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }

      // 2. Embedded Interactive Simulation Lab (if applicable)
      if (interactiveProcess != null) {
        item {
          WaterCycleSimulationView(
            process = interactiveProcess,
            onCheckpointPassed = { /* already handled in view */ }
          )
        }
      }

      // 3. Lesson Sections
      items(lesson.sections.size) { index ->
        val section = lesson.sections[index]
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = section.title,
              style = MaterialTheme.typography.titleMedium,
              fontSize = (18 * fontScale).sp,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = section.content,
              style = MaterialTheme.typography.bodyMedium,
              fontSize = (15 * fontScale).sp,
              lineHeight = (22 * fontScale).sp
            )

            // Formula / Core Concept Callout
            if (section.keyFormulaOrConcept != null) {
              Spacer(modifier = Modifier.height(12.dp))
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Functions,
                    contentDescription = null,
                    tint = EduBlueDarkPrimary,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = section.keyFormulaOrConcept,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFBAE6FD),
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }

            // Did You Know Callout
            if (section.didYouKnow != null) {
              Spacer(modifier = Modifier.height(12.dp))
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = EduAmber.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, EduAmber.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = EduAmber,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = "DID YOU KNOW?",
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = EduAmber
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = section.didYouKnow,
                      style = MaterialTheme.typography.bodySmall,
                      lineHeight = 18.sp
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 4. Key Takeaways
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Checklist,
                contentDescription = null,
                tint = EduEmerald,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Key Takeaways",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            lesson.keyTakeaways.forEach { takeaway ->
              Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .padding(top = 6.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(EduEmerald)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = takeaway,
                  style = MaterialTheme.typography.bodyMedium,
                  fontSize = (14 * fontScale).sp
                )
              }
            }
          }
        }
      }

      // 5. Personal Study Notes Notepad (Persistent in Room)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { showNotesEditor = !showNotesEditor },
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.EditNote,
                  contentDescription = null,
                  tint = EduBluePrimary,
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "My Personal Study Notes",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold
                )
              }

              Icon(
                imageVector = if (showNotesEditor) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null
              )
            }

            AnimatedVisibility(visible = showNotesEditor) {
              Column(modifier = Modifier.padding(top = 12.dp)) {
                OutlinedTextField(
                  value = noteText,
                  onValueChange = {
                    noteText = it
                    onSaveNote(it)
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("input_lesson_note"),
                  placeholder = { Text("Jot down thoughts, questions, or mental models...") },
                  shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "Notes are saved automatically to your device.",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      // 6. Action Bottom Bar: Complete Lesson & Take Quiz
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 24.dp)
        ) {
          Button(
            onClick = onTakeQuiz,
            colors = ButtonDefaults.buttonColors(containerColor = EduBluePrimary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("btn_take_quiz")
          ) {
            Icon(Icons.Default.Quiz, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isCompleted) "Retake Knowledge Quiz" else "Complete Lesson & Take Quiz (+${lesson.xpReward} XP)",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
