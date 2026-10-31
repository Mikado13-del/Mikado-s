package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlashcardItem
import com.example.data.model.SubjectCategory
import com.example.ui.theme.*

@Composable
fun FlashcardsScreen(
  flashcards: List<FlashcardItem>,
  masteryMap: Map<String, Int>,
  onUpdateStatus: (cardId: String, status: Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf<SubjectCategory?>(null) }
  var currentCardIndex by remember { mutableIntStateOf(0) }
  var isFlipped by remember { mutableStateOf(false) }

  val filteredCards = remember(flashcards, selectedCategory) {
    if (selectedCategory == null) flashcards
    else flashcards.filter { it.category == selectedCategory }
  }

  val activeCard = filteredCards.getOrNull(currentCardIndex)
  val totalMastered = flashcards.count { masteryMap[it.id] == 2 }

  // Flip rotation animation
  val rotation by animateFloatAsState(
    targetValue = if (isFlipped) 180f else 0f,
    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
    label = "CardFlipAnimation"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
      .testTag("flashcards_screen"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Title & Mastery Score
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Active Recall",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Spaced repetition deck studio",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = EduEmerald.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, EduEmerald.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = EduEmerald,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "$totalMastered / ${flashcards.size} Mastered",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = EduEmerald
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Subject Filter Chips
    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        FilterChip(
          selected = selectedCategory == null,
          onClick = {
            selectedCategory = null
            currentCardIndex = 0
            isFlipped = false
          },
          label = { Text("All Decks") },
          modifier = Modifier.testTag("deck_chip_all")
        )
      }
      items(SubjectCategory.values()) { category ->
        FilterChip(
          selected = selectedCategory == category,
          onClick = {
            selectedCategory = if (selectedCategory == category) null else category
            currentCardIndex = 0
            isFlipped = false
          },
          label = { Text(category.displayName) },
          modifier = Modifier.testTag("deck_chip_${category.name}")
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    if (activeCard == null) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Text("No flashcards found for this topic.")
      }
    } else {
      // Card Indicator
      Text(
        text = "CARD ${currentCardIndex + 1} OF ${filteredCards.size}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(10.dp))

      // 3D Flip Card Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp)
          .graphicsLayer {
            rotationY = rotation
            cameraDistance = 12f * density
          }
          .clip(RoundedCornerShape(24.dp))
          .background(MaterialTheme.colorScheme.surface)
          .border(
            1.5.dp,
            if (isFlipped) EduAmber.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            RoundedCornerShape(24.dp)
          )
          .clickable { isFlipped = !isFlipped }
          .padding(24.dp)
          .testTag("interactive_flip_card"),
        contentAlignment = Alignment.Center
      ) {
        if (rotation <= 90f) {
          // FRONT OF CARD (Question)
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = activeCard.category.color.copy(alpha = 0.15f)
            ) {
              Text(
                text = activeCard.category.displayName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = activeCard.category.color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Text(
              text = activeCard.question,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
              lineHeight = 26.sp,
              modifier = Modifier.padding(horizontal = 8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Tap to reveal answer",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        } else {
          // BACK OF CARD (Answer & Explanation)
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
              .fillMaxSize()
              .graphicsLayer { rotationY = 180f } // Counter-rotate so text is readable!
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = EduEmerald.copy(alpha = 0.15f)
            ) {
              Text(
                text = "ANSWER & EXPLANATION",
                style = MaterialTheme.typography.labelSmall,
                color = EduEmerald,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = activeCard.answer,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = activeCard.deepNote,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
              )
            }

            Text(
              text = "Tap to flip back",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Rating & Navigation Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Still Learning Button
        OutlinedButton(
          onClick = {
            onUpdateStatus(activeCard.id, 1)
            isFlipped = false
            if (currentCardIndex < filteredCards.size - 1) currentCardIndex++
          },
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("btn_still_learning")
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null, tint = EduRose)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Still Learning", color = EduRose)
        }

        // Mastered Button
        Button(
          onClick = {
            onUpdateStatus(activeCard.id, 2)
            isFlipped = false
            if (currentCardIndex < filteredCards.size - 1) currentCardIndex++
          },
          colors = ButtonDefaults.buttonColors(containerColor = EduEmerald),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("btn_mastered_card")
        ) {
          Icon(Icons.Default.Check, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Mastered (+15 XP)")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Card Swapper Prev / Next
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            if (currentCardIndex > 0) {
              currentCardIndex--
              isFlipped = false
            }
          },
          enabled = currentCardIndex > 0,
          modifier = Modifier.testTag("btn_prev_card")
        ) {
          Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Previous Card")
        }

        LinearProgressIndicator(
          progress = { (currentCardIndex + 1).toFloat() / filteredCards.size },
          modifier = Modifier
            .weight(1f)
            .height(6.dp)
            .clip(CircleShape),
          color = EduBluePrimary,
          trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        IconButton(
          onClick = {
            if (currentCardIndex < filteredCards.size - 1) {
              currentCardIndex++
              isFlipped = false
            }
          },
          enabled = currentCardIndex < filteredCards.size - 1,
          modifier = Modifier.testTag("btn_next_card")
        ) {
          Icon(Icons.Default.ArrowForwardIos, contentDescription = "Next Card")
        }
      }
    }
  }
}
