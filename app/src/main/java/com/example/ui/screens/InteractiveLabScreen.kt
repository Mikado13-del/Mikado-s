package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.InteractiveProcess
import com.example.ui.components.WaterCycleSimulationView
import com.example.ui.theme.EduBluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveLabScreen(
  process: InteractiveProcess,
  onAwardXp: (Int) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Interactive Simulation Lab",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_lab")) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
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
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(scrollState)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Process Overview Header
      Text(
        text = process.title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = process.overview,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      // The Simulation Canvas & Stage Explorer View
      WaterCycleSimulationView(
        process = process,
        onCheckpointPassed = { xp -> onAwardXp(xp) }
      )

      // Visual Infographic Diagram Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Planetary Cross-Section Reference",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Image(
            painter = painterResource(id = R.drawable.img_water_cycle),
            contentDescription = "Water cycle cross section diagram",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
              .clip(RoundedCornerShape(14.dp))
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Diagram illustrates the interplay between incoming solar thermal flux, alpine orographic uplift, and deep subterranean bedrock percolation.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(48.dp))
    }
  }
}
