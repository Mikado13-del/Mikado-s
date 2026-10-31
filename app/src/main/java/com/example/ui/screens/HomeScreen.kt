package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserProgressEntity
import com.example.data.model.Lesson
import com.example.data.model.SubjectCategory
import com.example.ui.AppScreen
import com.example.ui.theme.*

@Composable
fun HomeScreen(
  userProgress: UserProgressEntity,
  lessons: List<Lesson>,
  completedLessonIds: Set<String>,
  onNavigateTo: (AppScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_lazy_column"),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // 1. Hero Header Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .testTag("hero_banner_box")
      ) {
        Image(
          painter = painterResource(id = R.drawable.banner_edu_hero),
          contentDescription = "Cosmic Educational Discovery Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for readability
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color(0xCC0B101B),
                  Color(0xFF0B101B)
                ),
                startY = 40f
              )
            )
        )

        // Text inside Hero Banner
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = EduAmber.copy(alpha = 0.9f)
          ) {
            Text(
              text = "OMNILEARN ACADEMY",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF1E293B),
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Explore the Universe of Knowledge",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Interactive simulations, deep science & mental models",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCBD5E1)
          )
        }
      }
    }

    // 2. Daily Streak & Goal Progress Card
    item {
      DailyStreakCard(
        userProgress = userProgress,
        modifier = Modifier.padding(16.dp)
      )
    }

    // 3. Featured Interactive Simulation Lab Spotlight
    item {
      FeaturedSimulationCard(
        onLaunch = { onNavigateTo(AppScreen.InteractiveLab("process_water_cycle")) },
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }

    // 4. Quick Subject Domains Navigation
    item {
      Column(modifier = Modifier.padding(top = 20.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Subject Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          TextButton(
            onClick = { onNavigateTo(AppScreen.Courses) },
            modifier = Modifier.testTag("btn_see_all_subjects")
          ) {
            Text("See All", color = EduBluePrimary)
          }
        }

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(SubjectCategory.values()) { category ->
            SubjectCategoryChip(
              category = category,
              onClick = { onNavigateTo(AppScreen.Courses) }
            )
          }
        }
      }
    }

    // 5. Recommended Micro-Lessons
    item {
      Column(modifier = Modifier.padding(top = 20.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Essential Micro-Lessons",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        lessons.forEach { lesson ->
          LessonHomeCard(
            lesson = lesson,
            isCompleted = completedLessonIds.contains(lesson.id),
            onClick = { onNavigateTo(AppScreen.LessonDetail(lesson.id)) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
          )
        }
      }
    }

    // 6. Daily Brain Boost Challenge Banner
    item {
      Spacer(modifier = Modifier.height(16.dp))
      DailyBrainBoostCard(
        onStart = { onNavigateTo(AppScreen.Quiz()) },
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }
  }
}

@Composable
fun DailyStreakCard(
  userProgress: UserProgressEntity,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("daily_streak_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Streak Info
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(EduAmber.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = "Streak Fire",
            tint = EduAmber,
            modifier = Modifier.size(28.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "${userProgress.currentStreakDays}",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = EduAmber
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "DAY STREAK",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = "Daily Goal: ${userProgress.todayCompletedCount}/${userProgress.dailyGoalLessons} lessons",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // XP Badge
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = EduBluePrimary.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, EduBluePrimary.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Stars,
            contentDescription = "XP",
            tint = EduBluePrimary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${userProgress.totalXp} XP",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = EduBluePrimary
          )
        }
      }
    }
  }
}

@Composable
fun FeaturedSimulationCard(
  onLaunch: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(22.dp))
      .clickable { onLaunch() }
      .testTag("featured_simulation_card"),
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFF131D31)
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3E5C))
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Image Banner for Water Cycle
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_water_cycle),
          contentDescription = "Water Cycle Cross-Section",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        // Chip
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color(0xDD0284C7),
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(10.dp)
        ) {
          Text(
            text = "FEATURED INTERACTIVE LAB",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "The Global Hydrological Cycle",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Interactive step-by-step visual exploration of evaporation, condensation, precipitation, and groundwater dynamics.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF94A3B8)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Timeline,
              contentDescription = null,
              tint = EduAmber,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "6 Interactive Stages",
              style = MaterialTheme.typography.labelSmall,
              color = EduAmber
            )
          }

          Button(
            onClick = onLaunch,
            colors = ButtonDefaults.buttonColors(containerColor = EduBluePrimary),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier.testTag("btn_launch_simulation")
          ) {
            Text("Launch Lab", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun SubjectCategoryChip(
  category: SubjectCategory,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = category.color.copy(alpha = 0.12f),
    border = androidx.compose.foundation.BorderStroke(1.dp, category.color.copy(alpha = 0.35f)),
    modifier = Modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("chip_subject_${category.name}")
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(10.dp)
          .clip(CircleShape)
          .background(category.color)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = category.displayName,
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = category.tagline,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 10.sp
        )
      }
    }
  }
}

@Composable
fun LessonHomeCard(
  lesson: Lesson,
  isCompleted: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .testTag("lesson_card_${lesson.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isCompleted) EduEmerald.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Subject category color bar
      Box(
        modifier = Modifier
          .width(4.dp)
          .height(48.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(lesson.subjectCategory.color)
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = lesson.subjectCategory.displayName.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = lesson.subjectCategory.color,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
          )
          if (lesson.interactiveProcessId != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = EduBluePrimary.copy(alpha = 0.15f)
            ) {
              Text(
                text = "⚡ SIMULATION",
                style = MaterialTheme.typography.labelSmall,
                color = EduBluePrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = lesson.title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = lesson.subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      if (isCompleted) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Completed",
          tint = EduEmerald,
          modifier = Modifier.size(24.dp)
        )
      } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${lesson.readTimeMinutes}m",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun DailyBrainBoostCard(
  onStart: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .clickable { onStart() }
      .testTag("daily_brain_boost_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(EduPurple.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Psychology,
            contentDescription = "Brain Challenge",
            tint = EduPurple,
            modifier = Modifier.size(26.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = "Daily Knowledge Sprint",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "5 quick questions across all sciences & arts",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      FilledTonalButton(
        onClick = onStart,
        modifier = Modifier.testTag("btn_start_daily_boost")
      ) {
        Text("Start")
      }
    }
  }
}
