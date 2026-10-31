package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FormulaItem
import com.example.ui.theme.EduAmber
import com.example.ui.theme.EduBlueDarkPrimary
import com.example.ui.theme.EduEmerald

@Composable
fun FormulaHubScreen(
  formulas: List<FormulaItem>,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val filtered = remember(formulas, searchQuery) {
    if (searchQuery.isBlank()) formulas
    else formulas.filter {
      it.title.contains(searchQuery, ignoreCase = true) ||
        it.formula.contains(searchQuery, ignoreCase = true) ||
        it.explanation.contains(searchQuery, ignoreCase = true)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
      .testTag("formula_hub_screen")
  ) {
    Text(
      text = "Formula & Theory Hub",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = "Essential equations, laws & principles across disciplines",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("search_formulas_input"),
      placeholder = { Text("Search equations (e.g. latent heat, Bayes, Euler)...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
      singleLine = true,
      shape = RoundedCornerShape(14.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(filtered) { item ->
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("formula_card_${item.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = item.category.color.copy(alpha = 0.15f)
              ) {
                Text(
                  text = item.category.displayName.uppercase(),
                  style = MaterialTheme.typography.labelSmall,
                  color = item.category.color,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Formula equation display block
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFF1E293B),
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .padding(14.dp)
                  .fillMaxWidth(),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = item.formula,
                  style = MaterialTheme.typography.titleMedium,
                  color = EduBlueDarkPrimary,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace,
                  fontSize = 18.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = item.explanation,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Top
              ) {
                Icon(
                  imageVector = Icons.Default.Science,
                  contentDescription = null,
                  tint = EduAmber,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Application: ${item.application}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
