package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lesson
import com.example.data.model.SubjectCategory
import com.example.ui.AppScreen
import com.example.ui.theme.EduBluePrimary
import com.example.ui.theme.EduEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
  lessons: List<Lesson>,
  completedLessonIds: Set<String>,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  selectedCategory: SubjectCategory?,
  onSelectCategory: (SubjectCategory?) -> Unit,
  onNavigateTo: (AppScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("courses_screen_container")
  ) {
    // Top Bar / Title
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Text(
        text = "Explore Curriculum",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Deep micro-courses across science, technology, mathematics & logic",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("search_lessons_input"),
        placeholder = { Text("Search topics, formulas, concepts...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear search")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
      )
    }

    // Category Filter Chips Row
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        FilterChip(
          selected = selectedCategory == null,
          onClick = { onSelectCategory(null) },
          label = { Text("All Subjects") },
          modifier = Modifier.testTag("filter_chip_all")
        )
      }
      items(SubjectCategory.values()) { category ->
        FilterChip(
          selected = selectedCategory == category,
          onClick = { onSelectCategory(if (selectedCategory == category) null else category) },
          label = { Text(category.displayName) },
          leadingIcon = {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(category.color)
            )
          },
          modifier = Modifier.testTag("filter_chip_${category.name}")
        )
      }
    }

    // Lessons List
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("courses_list_lazy_column"),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      if (lessons.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "No matching lessons found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Try clearing search keywords or selecting another topic",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(lessons) { lesson ->
          val isCompleted = completedLessonIds.contains(lesson.id)
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .clickable { onNavigateTo(AppScreen.LessonDetail(lesson.id)) }
              .testTag("course_item_${lesson.id}"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isCompleted) EduEmerald.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = lesson.subjectCategory.color.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = lesson.subjectCategory.displayName.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = lesson.subjectCategory.color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                if (isCompleted) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Completed",
                      tint = EduEmerald,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Mastered",
                      style = MaterialTheme.typography.labelSmall,
                      color = EduEmerald,
                      fontWeight = FontWeight.Bold
                    )
                  }
                } else {
                  Text(
                    text = "${lesson.readTimeMinutes} min read",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = lesson.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = lesson.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )

              if (lesson.interactiveProcessId != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = EduBluePrimary.copy(alpha = 0.12f),
                  border = androidx.compose.foundation.BorderStroke(1.dp, EduBluePrimary.copy(alpha = 0.25f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.AutoFixHigh,
                      contentDescription = null,
                      tint = EduBluePrimary,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "Includes Interactive Simulation Lab",
                      style = MaterialTheme.typography.labelSmall,
                      color = EduBluePrimary,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
