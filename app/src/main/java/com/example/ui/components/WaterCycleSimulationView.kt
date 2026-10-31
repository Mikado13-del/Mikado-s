package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InteractiveProcess
import com.example.data.model.ProcessStage
import com.example.data.model.QuizQuestion
import com.example.ui.theme.*
import kotlin.math.*
import kotlin.random.Random

@Composable
fun WaterCycleSimulationView(
  process: InteractiveProcess,
  onCheckpointPassed: (xp: Int) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedStageIndex by remember { mutableIntStateOf(0) }
  var isAutoPlaying by remember { mutableStateOf(false) }
  var solarIntensity by remember { mutableFloatStateOf(0.7f) } // 0.2f to 1.0f
  var windSpeed by remember { mutableFloatStateOf(0.5f) } // 0.1f to 1.0f
  var isSnowMode by remember { mutableStateOf(false) }
  var showTelemetry by remember { mutableStateOf(true) }

  val stages = process.stages
  val currentStage = stages.getOrElse(selectedStageIndex) { stages.first() }

  // Auto-play timer
  LaunchedEffect(isAutoPlaying, selectedStageIndex) {
    if (isAutoPlaying) {
      kotlinx.coroutines.delay(4500)
      selectedStageIndex = (selectedStageIndex + 1) % stages.size
    }
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("water_cycle_simulation_card"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header with Title & Auto-Play Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(EduBluePrimary)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "INTERACTIVE SIMULATION LAB",
              style = MaterialTheme.typography.labelMedium,
              color = EduBluePrimary,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = process.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        // Auto Tour / Play Button
        FilledTonalButton(
          onClick = { isAutoPlaying = !isAutoPlaying },
          modifier = Modifier.testTag("btn_auto_tour"),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = if (isAutoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isAutoPlaying) "Pause Tour" else "Auto Tour",
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isAutoPlaying) "Pause" else "Auto Tour",
            style = MaterialTheme.typography.labelMedium
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Stage Stepper Chips
      ScrollableStageTabs(
        stages = stages,
        selectedIndex = selectedStageIndex,
        onSelectStage = { index ->
          isAutoPlaying = false
          selectedStageIndex = index
        }
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Interactive Animated Canvas
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
          .clip(RoundedCornerShape(18.dp))
          .background(Color(0xFF0F172A))
          .border(1.dp, Color(0xFF334155), RoundedCornerShape(18.dp))
          .pointerInput(Unit) {
            detectTapGestures { offset ->
              // Map tap coordinates to stages
              val relX = offset.x / size.width
              val relY = offset.y / size.height
              isAutoPlaying = false
              when {
                relX < 0.35f && relY > 0.6f -> selectedStageIndex = 0 // Ocean / Evaporation
                relX in 0.45f..0.85f && relY in 0.5f..0.75f -> selectedStageIndex = 1 // Transpiration / Forest
                relX in 0.25f..0.7f && relY < 0.45f -> selectedStageIndex = 2 // Condensation / Clouds
                relX in 0.05f..0.5f && relY < 0.3f -> selectedStageIndex = 3 // Atmospheric Transport
                relX in 0.6f..0.95f && relY in 0.2f..0.6f -> selectedStageIndex = 4 // Precipitation / Mountain
                relY > 0.75f -> selectedStageIndex = 5 // Runoff / Aquifer
              }
            }
          }
          .testTag("water_cycle_canvas_container")
      ) {
        WaterCycleAnimatedCanvas(
          selectedStage = currentStage,
          solarIntensity = solarIntensity,
          windSpeed = windSpeed,
          isSnowMode = isSnowMode
        )

        // Telemetry Floating Overlay
        if (showTelemetry) {
          Surface(
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xCC0B1120)
          ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
              Text(
                text = "Phase: ${currentStage.name}",
                style = MaterialTheme.typography.labelSmall,
                color = EduAmber,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Insolation: ${(solarIntensity * 100).toInt()}% • Wind: ${(windSpeed * 80).toInt()} km/h",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8)
              )
            }
          }
        }

        // Tap hint badge
        Surface(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp),
          shape = RoundedCornerShape(6.dp),
          color = Color(0xAA0284C7)
        ) {
          Text(
            text = "Tap zones to inspect",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Simulation Parameter Sliders & Toggles
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "ENVIRONMENTAL PARAMETERS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(6.dp))

          // Solar Radiation Slider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.WbSunny,
              contentDescription = "Solar Insolation",
              tint = EduAmber,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Solar Heat",
              style = MaterialTheme.typography.labelMedium,
              modifier = Modifier.width(80.dp)
            )
            Slider(
              value = solarIntensity,
              onValueChange = { solarIntensity = it },
              valueRange = 0.2f..1.0f,
              modifier = Modifier
                .weight(1f)
                .testTag("slider_solar_heat")
            )
          }

          // Wind Speed Slider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Air,
              contentDescription = "Wind Advection",
              tint = EduBlueDarkPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Wind Jet",
              style = MaterialTheme.typography.labelMedium,
              modifier = Modifier.width(80.dp)
            )
            Slider(
              value = windSpeed,
              onValueChange = { windSpeed = it },
              valueRange = 0.1f..1.0f,
              modifier = Modifier
                .weight(1f)
                .testTag("slider_wind_speed")
            )
          }

          // Toggle Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { isSnowMode = !isSnowMode }
                .padding(vertical = 4.dp, horizontal = 6.dp)
            ) {
              Checkbox(
                checked = isSnowMode,
                onCheckedChange = { isSnowMode = it },
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isSnowMode) "Snow/Glacier Mode ❄️" else "Liquid Rain Mode 🌧️",
                style = MaterialTheme.typography.labelMedium
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showTelemetry = !showTelemetry }
                .padding(vertical = 4.dp, horizontal = 6.dp)
            ) {
              Checkbox(
                checked = showTelemetry,
                onCheckedChange = { showTelemetry = it },
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Telemetry HUD",
                style = MaterialTheme.typography.labelMedium
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Stage Deep Explanation Card
      AnimatedContent(
        targetState = currentStage,
        transitionSpec = {
          fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
        },
        label = "StageExplanationTransition"
      ) { stage ->
        StageExplanationCard(
          stage = stage,
          totalStages = stages.size,
          onPrevious = {
            if (selectedStageIndex > 0) selectedStageIndex--
          },
          onNext = {
            if (selectedStageIndex < stages.size - 1) selectedStageIndex++
          },
          canGoBack = selectedStageIndex > 0,
          canGoForward = selectedStageIndex < stages.size - 1,
          onCheckpointAnswered = onCheckpointPassed
        )
      }
    }
  }
}

@Composable
fun ScrollableStageTabs(
  stages: List<ProcessStage>,
  selectedIndex: Int,
  onSelectStage: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    stages.forEachIndexed { index, stage ->
      val isSelected = index == selectedIndex
      val chipColor = if (isSelected) EduBluePrimary else MaterialTheme.colorScheme.surfaceVariant
      val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(chipColor)
          .clickable { onSelectStage(index) }
          .padding(vertical = 8.dp)
          .testTag("tab_stage_${stage.id}"),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "STEP ${stage.stepNumber}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor.copy(alpha = 0.8f),
            fontSize = 9.sp
          )
          Text(
            text = stage.name.substringBefore(" ").take(7),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = contentColor,
            maxLines = 1
          )
        }
      }
    }
  }
}

@Composable
fun WaterCycleAnimatedCanvas(
  selectedStage: ProcessStage,
  solarIntensity: Float,
  windSpeed: Float,
  isSnowMode: Boolean
) {
  val infiniteTransition = rememberInfiniteTransition(label = "SimulationAnimation")

  // Animated phase timer
  val phaseAnimation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 2f * PI.toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "WavePhase"
  )

  // Floating vapor drift
  val vaporProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween((2800 / solarIntensity).toInt().coerceAtLeast(1000), easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "VaporProgress"
  )

  // Rain drop fall progress
  val rainProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (isSnowMode) 2200 else 900, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "RainProgress"
  )

  // Cloud horizontal drift
  val cloudDrift by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 40f,
    animationSpec = infiniteRepeatable(
      animation = tween((5000 / windSpeed).toInt().coerceAtLeast(1500), easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "CloudDrift"
  )

  Canvas(
    modifier = Modifier.fillMaxSize()
  ) {
    val width = size.width
    val height = size.height

    // 1. Sky Gradient Background (Atmosphere)
    val skyBrush = Brush.verticalGradient(
      colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E293B),
        Color(0xFF0F3460)
      )
    )
    drawRect(brush = skyBrush)

    // 2. The Radiant Sun (Upper Left)
    val sunCenter = Offset(width * 0.16f, height * 0.20f)
    val sunRadius = 24.dp.toPx() * (0.8f + solarIntensity * 0.3f)

    // Sun Corona Pulsing
    val coronaRadius = sunRadius * (1.3f + 0.15f * sin(phaseAnimation))
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          EduAmber.copy(alpha = 0.5f * solarIntensity),
          Color.Transparent
        ),
        center = sunCenter,
        radius = coronaRadius
      ),
      radius = coronaRadius,
      center = sunCenter
    )

    // Core Sun
    drawCircle(
      color = Color(0xFFFBBF24),
      radius = sunRadius,
      center = sunCenter
    )

    // Solar Radiation Waves downwards towards ocean
    if (solarIntensity > 0.3f) {
      for (i in 0..3) {
        val angle = (PI / 4 + i * (PI / 10)).toFloat()
        val rayStart = Offset(
          sunCenter.x + cos(angle) * (sunRadius + 4f),
          sunCenter.y + sin(angle) * (sunRadius + 4f)
        )
        val rayLength = 35.dp.toPx() * solarIntensity
        val rayEnd = Offset(
          rayStart.x + cos(angle) * rayLength,
          rayStart.y + sin(angle) * rayLength
        )
        drawLine(
          color = Color(0xFFFDE68A).copy(alpha = 0.6f * solarIntensity),
          start = rayStart,
          end = rayEnd,
          strokeWidth = 2.dp.toPx()
        )
      }
    }

    // 3. Mountain Terrain & Slopes (Right half)
    val mountainPath = Path().apply {
      moveTo(width * 0.40f, height * 0.85f)
      lineTo(width * 0.68f, height * 0.36f) // Alpine peak
      lineTo(width * 0.85f, height * 0.48f) // Saddle
      lineTo(width, height * 0.40f) // Far ridge
      lineTo(width, height)
      lineTo(width * 0.40f, height)
      close()
    }

    drawPath(
      path = mountainPath,
      brush = Brush.verticalGradient(
        colors = listOf(
          Color(0xFF334155),
          Color(0xFF1E293B),
          Color(0xFF0F172A)
        ),
        startY = height * 0.35f,
        endY = height
      )
    )

    // Snow Cap on Mountain
    val snowPeakPath = Path().apply {
      moveTo(width * 0.62f, height * 0.45f)
      lineTo(width * 0.68f, height * 0.36f)
      lineTo(width * 0.74f, height * 0.44f)
      lineTo(width * 0.70f, height * 0.48f)
      close()
    }
    drawPath(snowPeakPath, color = Color(0xFFE2E8F0))

    // Forest Canopy & Vegetation on Lower Slopes
    val canopyCenter = Offset(width * 0.54f, height * 0.70f)
    drawCircle(
      color = Color(0xFF15803D),
      radius = 22.dp.toPx(),
      center = canopyCenter
    )
    drawCircle(
      color = Color(0xFF16A34A),
      radius = 16.dp.toPx(),
      center = Offset(width * 0.50f, height * 0.74f)
    )

    // 4. Subterranean Aquifer / Groundwater Layer
    val aquiferTop = height * 0.88f
    drawRect(
      color = Color(0xFF1E293B),
      topLeft = Offset(width * 0.35f, aquiferTop),
      size = Size(width * 0.65f, height - aquiferTop)
    )

    // Aquifer Water saturation
    drawRect(
      brush = Brush.horizontalGradient(
        colors = listOf(
          Color(0xFF0284C7).copy(alpha = 0.6f),
          Color(0xFF0369A1).copy(alpha = 0.8f)
        ),
        startX = width * 0.35f,
        endX = width
      ),
      topLeft = Offset(width * 0.35f, height * 0.92f),
      size = Size(width * 0.65f, height * 0.08f)
    )

    // 5. Ocean / Body of Water (Left half)
    val oceanTop = height * 0.82f
    val wavePath = Path().apply {
      moveTo(0f, oceanTop)
      var x = 0f
      while (x <= width * 0.42f) {
        val y = oceanTop + sin((x / 30f) + phaseAnimation) * 4f
        lineTo(x, y)
        x += 8f
      }
      lineTo(width * 0.42f, height)
      lineTo(0f, height)
      close()
    }

    drawPath(
      path = wavePath,
      brush = Brush.verticalGradient(
        colors = listOf(
          Color(0xFF0284C7),
          Color(0xFF0369A1),
          Color(0xFF082F49)
        )
      )
    )

    // 6. Surface Runoff Stream down mountain into ocean
    val streamPath = Path().apply {
      moveTo(width * 0.66f, height * 0.55f)
      quadraticTo(
        width * 0.52f, height * 0.78f,
        width * 0.38f, oceanTop + 6.dp.toPx()
      )
    }
    drawPath(
      path = streamPath,
      color = Color(0xFF38BDF8),
      style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
    )

    // 7. Clouds (Drifting overhead)
    val cloudBaseX = width * 0.50f + cloudDrift
    val cloudBaseY = height * 0.28f

    fun drawFluffyCloud(cx: Float, cy: Float, scale: Float = 1f) {
      drawCircle(Color(0xFFE2E8F0), radius = 24.dp.toPx() * scale, center = Offset(cx, cy))
      drawCircle(Color(0xFFCBD5E1), radius = 20.dp.toPx() * scale, center = Offset(cx - 18.dp.toPx() * scale, cy + 4.dp.toPx() * scale))
      drawCircle(Color(0xFFCBD5E1), radius = 22.dp.toPx() * scale, center = Offset(cx + 20.dp.toPx() * scale, cy + 3.dp.toPx() * scale))
      drawCircle(Color(0xFF94A3B8), radius = 16.dp.toPx() * scale, center = Offset(cx + 6.dp.toPx() * scale, cy + 10.dp.toPx() * scale))
    }

    drawFluffyCloud(cloudBaseX, cloudBaseY, 1.0f)
    drawFluffyCloud(cloudBaseX + width * 0.22f, cloudBaseY - 14.dp.toPx(), 0.85f)

    // 8. Dynamic Particles Based on Parameters
    // A. Evaporation Vapor Particles (Rising from Ocean)
    val vaporCount = (6 * solarIntensity).toInt().coerceAtLeast(3)
    for (i in 0 until vaporCount) {
      val startX = width * (0.08f + i * 0.05f)
      val cycleProgress = (vaporProgress + (i * 0.22f)) % 1f
      val pY = (oceanTop - 4.dp.toPx()) - (cycleProgress * (height * 0.50f))
      val pX = startX + sin(cycleProgress * 4f * PI.toFloat()) * 12.dp.toPx()
      val alpha = (sin(cycleProgress * PI.toFloat()) * solarIntensity).coerceIn(0f, 0.9f)

      drawCircle(
        color = Color(0xFF7DD3FC).copy(alpha = alpha),
        radius = (3.5.dp.toPx() + cycleProgress * 2.dp.toPx()),
        center = Offset(pX, pY)
      )
    }

    // B. Plant Transpiration Particles (Rising from Canopy)
    for (i in 0..3) {
      val tCycle = (vaporProgress + i * 0.3f) % 1f
      val tY = canopyCenter.y - (tCycle * height * 0.35f)
      val tX = canopyCenter.x + (i - 1.5f) * 10.dp.toPx()
      val tAlpha = (sin(tCycle * PI.toFloat()) * 0.8f).coerceIn(0f, 0.8f)

      drawCircle(
        color = Color(0xFF86EFAC).copy(alpha = tAlpha),
        radius = 2.5.dp.toPx(),
        center = Offset(tX, tY)
      )
    }

    // C. Wind Advection Vectors (Sky arrows)
    val windY = height * 0.16f
    for (i in 0..2) {
      val wOffset = ((cloudDrift * 2.5f) + i * width * 0.25f) % (width * 0.85f)
      val wStart = Offset(width * 0.15f + wOffset, windY + (i * 12.dp.toPx()))
      drawLine(
        color = Color(0xFFBAE6FD).copy(alpha = 0.45f * windSpeed),
        start = wStart,
        end = Offset(wStart.x + 22.dp.toPx() * windSpeed, wStart.y),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
      )
    }

    // D. Precipitation Droplets / Snow Flakes (Falling from Clouds over Mountains)
    val rainDropCount = 14
    for (i in 0 until rainDropCount) {
      val rProg = (rainProgress + i * (1f / rainDropCount)) % 1f
      val rx = cloudBaseX - 10.dp.toPx() + (i * 7.dp.toPx())
      val ry = cloudBaseY + 18.dp.toPx() + (rProg * height * 0.42f)

      if (isSnowMode) {
        // Snow flake particle
        drawCircle(
          color = Color(0xFFF1F5F9).copy(alpha = 0.85f),
          radius = 2.dp.toPx(),
          center = Offset(rx, ry)
        )
      } else {
        // Rain streak
        drawLine(
          color = Color(0xFF67E8F9).copy(alpha = 0.75f),
          start = Offset(rx, ry),
          end = Offset(rx - 2.dp.toPx() * windSpeed, ry + 7.dp.toPx()),
          strokeWidth = 1.8.dp.toPx()
        )
      }
    }

    // 9. Stage Highlight Ring Overlay
    val highlightTarget = when (selectedStage.visualHighlightArea) {
      "ocean" -> Offset(width * 0.22f, height * 0.72f)
      "canopy" -> canopyCenter
      "clouds" -> Offset(cloudBaseX, cloudBaseY)
      "winds" -> Offset(width * 0.40f, height * 0.18f)
      "mountains" -> Offset(width * 0.70f, height * 0.50f)
      "subsurface" -> Offset(width * 0.60f, height * 0.93f)
      else -> Offset(width * 0.5f, height * 0.5f)
    }

    // Draw active pulsing halo around current stage focus
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          EduAmber.copy(alpha = 0.35f + 0.15f * sin(phaseAnimation * 2f)),
          Color.Transparent
        ),
        center = highlightTarget,
        radius = 48.dp.toPx()
      ),
      radius = 48.dp.toPx(),
      center = highlightTarget
    )
    drawCircle(
      color = EduAmber,
      radius = 6.dp.toPx(),
      center = highlightTarget
    )
  }
}

@Composable
fun StageExplanationCard(
  stage: ProcessStage,
  totalStages: Int,
  onPrevious: () -> Unit,
  onNext: () -> Unit,
  canGoBack: Boolean,
  canGoForward: Boolean,
  onCheckpointAnswered: (xp: Int) -> Unit
) {
  var selectedAnswerIndex by remember(stage.id) { mutableStateOf<Int?>(null) }
  var hasAnswered by remember(stage.id) { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("stage_explanation_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Step badge & Stage Name
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = EduBluePrimary.copy(alpha = 0.15f)
        ) {
          Text(
            text = "STAGE ${stage.stepNumber} OF $totalStages",
            style = MaterialTheme.typography.labelSmall,
            color = EduBluePrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        // Stepper Navigation Buttons
        Row {
          IconButton(
            onClick = onPrevious,
            enabled = canGoBack,
            modifier = Modifier
              .size(36.dp)
              .testTag("btn_stage_prev")
          ) {
            Icon(
              imageVector = Icons.Default.ChevronLeft,
              contentDescription = "Previous Stage"
            )
          }
          IconButton(
            onClick = onNext,
            enabled = canGoForward,
            modifier = Modifier
              .size(36.dp)
              .testTag("btn_stage_next")
          ) {
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = "Next Stage"
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = stage.name,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = stage.shortDescription,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Thermodynamic Metric Chip
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E293B),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "Energy Metric",
            tint = EduAmber,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = stage.thermodynamicMetric,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFDE68A)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Deep Science Detail
      Text(
        text = "Physical Mechanism:",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = stage.detailedScience,
        style = MaterialTheme.typography.bodyMedium,
        lineHeight = 22.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Atmospheric & Biosphere impact
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(10.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Public,
            contentDescription = "Biosphere Impact",
            tint = EduEmerald,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Planetary Climate Impact",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = EduEmerald
            )
            Text(
              text = stage.atmosphericImpact,
              style = MaterialTheme.typography.bodySmall
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Interactive Checkpoint Quiz
      CheckpointQuizBlock(
        question = stage.keyCheckpointQuestion,
        selectedAnswerIndex = selectedAnswerIndex,
        hasAnswered = hasAnswered,
        onSelectAnswer = { index ->
          selectedAnswerIndex = index
          hasAnswered = true
          if (index == stage.keyCheckpointQuestion.correctOptionIndex) {
            onCheckpointAnswered(25)
          }
        }
      )
    }
  }
}

@Composable
fun CheckpointQuizBlock(
  question: QuizQuestion,
  selectedAnswerIndex: Int?,
  hasAnswered: Boolean,
  onSelectAnswer: (Int) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("checkpoint_quiz_block"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.HelpOutline,
          contentDescription = "Checkpoint",
          tint = EduAmber,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "CONCEPT CHECKPOINT (+25 XP)",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = EduAmber
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = question.question,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Answer choices
      question.options.forEachIndexed { index, option ->
        val isSelected = selectedAnswerIndex == index
        val isCorrect = index == question.correctOptionIndex

        val (btnColor, textColor) = when {
          !hasAnswered -> {
            MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
          }
          isCorrect -> {
            EduEmerald.copy(alpha = 0.2f) to EduEmerald
          }
          isSelected && !isCorrect -> {
            EduRose.copy(alpha = 0.2f) to EduRose
          }
          else -> {
            MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
          }
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = btnColor,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = !hasAnswered) { onSelectAnswer(index) }
            .testTag("checkpoint_option_$index")
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                  if (hasAnswered && isCorrect) EduEmerald
                  else if (hasAnswered && isSelected) EduRose
                  else MaterialTheme.colorScheme.surface
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = ('A' + index).toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (hasAnswered && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurface
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
              text = option,
              style = MaterialTheme.typography.bodyMedium,
              color = textColor,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              modifier = Modifier.weight(1f)
            )

            if (hasAnswered) {
              if (isCorrect) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Correct",
                  tint = EduEmerald,
                  modifier = Modifier.size(20.dp)
                )
              } else if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Cancel,
                  contentDescription = "Incorrect",
                  tint = EduRose,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }
      }

      // Explanation reveal
      if (hasAnswered) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (selectedAnswerIndex == question.correctOptionIndex)
            EduEmerald.copy(alpha = 0.1f)
          else EduAmber.copy(alpha = 0.1f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = question.explanation,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}
