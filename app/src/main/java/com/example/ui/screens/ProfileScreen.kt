package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.local.UserProgressEntity
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
  userProgress: UserProgressEntity,
  completedLessonCount: Int,
  masteredFlashcardsCount: Int,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  val scholarLevel = when {
    userProgress.totalXp >= 600 -> "Master Polymath 🌌"
    userProgress.totalXp >= 250 -> "Curious Scholar 📜"
    else -> "Novice Seeker 🔍"
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(scrollState)
      .testTag("profile_screen"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Avatar & Tier Header
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(EduBluePrimary.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.School,
        contentDescription = "Scholar Avatar",
        tint = EduBluePrimary,
        modifier = Modifier.size(44.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = scholarLevel,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )

    Text(
      text = "Lifelong Learner",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Progress Stats Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // XP Card
      StatCard(
        title = "Total XP",
        value = "${userProgress.totalXp}",
        icon = Icons.Default.Stars,
        iconTint = EduAmber,
        modifier = Modifier.weight(1f)
      )

      // Streak Card
      StatCard(
        title = "Day Streak",
        value = "${userProgress.currentStreakDays} 🔥",
        icon = Icons.Default.LocalFireDepartment,
        iconTint = EduAmber,
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Completed Lessons
      StatCard(
        title = "Lessons Done",
        value = "$completedLessonCount",
        icon = Icons.Default.MenuBook,
        iconTint = EduBlueDarkPrimary,
        modifier = Modifier.weight(1f)
      )

      // Mastered Cards
      StatCard(
        title = "Cards Mastered",
        value = "$masteredFlashcardsCount",
        icon = Icons.Default.FactCheck,
        iconTint = EduEmerald,
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Academic Badges & Milestones
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Earned Academic Badges",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        BadgeItem(
          title = "Water Cycle Pioneer",
          description = "Completed exploration of Earth's thermodynamic hydrological cycle.",
          isUnlocked = true,
          badgeIcon = Icons.Default.WaterDrop,
          badgeColor = EduBlueDarkPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        BadgeItem(
          title = "Consistent Inquirer",
          description = "Maintained a continuous daily study streak.",
          isUnlocked = userProgress.currentStreakDays >= 3,
          badgeIcon = Icons.Default.LocalFireDepartment,
          badgeColor = EduAmber
        )

        Spacer(modifier = Modifier.height(8.dp))

        BadgeItem(
          title = "Polymath in Training",
          description = "Accumulated over 200 XP across diverse curriculum domains.",
          isUnlocked = userProgress.totalXp >= 200,
          badgeIcon = Icons.Default.AutoAwesome,
          badgeColor = EduPurple
        )
      }
    }

    Spacer(modifier = Modifier.height(96.dp))
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun BadgeItem(
  title: String,
  description: String,
  isUnlocked: Boolean,
  badgeIcon: androidx.compose.ui.graphics.vector.ImageVector,
  badgeColor: Color
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(if (isUnlocked) badgeColor.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = badgeIcon,
          contentDescription = null,
          tint = if (isUnlocked) badgeColor else Color.Gray,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
        )
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      }
    }
  }
}
