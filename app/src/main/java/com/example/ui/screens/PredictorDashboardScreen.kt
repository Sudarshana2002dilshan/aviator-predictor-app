package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.PredictionHistoryEntity
import com.example.ui.theme.AviatorCardBg
import com.example.ui.theme.AviatorCardBorder
import com.example.ui.theme.AviatorDarkBg
import com.example.ui.theme.AviatorGold
import com.example.ui.theme.AviatorGreen
import com.example.ui.theme.AviatorOrange
import com.example.ui.theme.AviatorRedLight
import com.example.ui.theme.AviatorRedPrimary
import com.example.ui.theme.AviatorSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PredictorDashboardScreen(
  userId: String,
  site: String,
  isCalculating: Boolean,
  calculationProgress: Float,
  currentSignal: PredictionHistoryEntity?,
  predictionHistory: List<PredictionHistoryEntity>,
  autoPredictEnabled: Boolean,
  onlineUsers: Int = 84,
  onGetSignalClick: () -> Unit,
  onToggleAutoPredict: () -> Unit,
  onOpenAdmin: (() -> Unit)? = null,
  onLogout: (() -> Unit)? = null
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(AviatorDarkBg)
      .testTag("predictor_dashboard_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(16.dp)
    ) {
      // Top Bar: Site Name + User ID + Live Server Sync
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(AviatorRedPrimary)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = site.uppercase(),
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Column {
            Text(
              text = "ID: $userId",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(AviatorGreen)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "APPROVED (LIVE ALGO V4.2)",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.sp,
                  color = AviatorGreen,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AviatorGreen.copy(alpha = 0.15f))
            .border(1.dp, AviatorGreen, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(AviatorGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "⚡ $onlineUsers ONLINE",
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              color = AviatorGreen
            )
          }
        }
      }

      // CENTERPIECE RADAR CANVAS & MULTIPLIER DISPLAY CARD
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, AviatorRedPrimary.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = AviatorSurface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Radar Sweep Canvas Visual
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(AviatorDarkBg)
              .border(1.dp, AviatorCardBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
          ) {
            AviatorRadarCanvas(isCalculating = isCalculating)

            // Center Floating Multiplier Text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              if (isCalculating) {
                Text(
                  text = "SCANNING ALGORITHM...",
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = AviatorGold,
                    letterSpacing = 1.sp
                  )
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                  progress = { calculationProgress },
                  modifier = Modifier
                    .width(180.dp)
                    .height(6.dp)
                    .clip(CircleShape),
                  color = AviatorRedPrimary,
                  trackColor = AviatorCardBg
                )
              } else {
                val multiplierText = currentSignal?.predictedMultiplier?.let { "%.2fx".format(it) } ?: "2.85x"

                Text(
                  text = multiplierText,
                  style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = AviatorGold,
                    fontSize = 52.sp,
                    letterSpacing = (-1).sp
                  ),
                  modifier = Modifier.testTag("multiplier_display")
                )

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AviatorGreen.copy(alpha = 0.15f))
                    .border(1.dp, AviatorGreen, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Shield,
                      contentDescription = null,
                      tint = AviatorGreen,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${currentSignal?.accuracy ?: 99}% ACCURACY CONFIRMED",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AviatorGreen,
                        fontSize = 11.sp
                      )
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Safe Cashout Advice Box
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(AviatorCardBg)
              .border(1.dp, AviatorCardBorder, RoundedCornerShape(12.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "RECOMMENDED SAFE CASH-OUT",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp)
              )
              val safeCashoutText = currentSignal?.safeCashout?.let { "%.2fx".format(it) } ?: "2.10x"
              Text(
                text = safeCashoutText,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = AviatorGreen
                )
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "ROUND ALGORITHM CODE",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp)
              )
              Text(
                text = currentSignal?.roundCode ?: "#7839",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // GET SIGNAL / PREDICT NEXT ROUND BUTTON
          Button(
            onClick = onGetSignalClick,
            enabled = !isCalculating,
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .testTag("get_signal_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = AviatorRedPrimary,
              contentColor = Color.White
            )
          ) {
            if (isCalculating) {
              CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
              Spacer(modifier = Modifier.width(8.dp))
              Text("CALCULATING SIGNAL...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            } else {
              Icon(Icons.Default.FlightTakeoff, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
              Text("PREDICT NEXT MULTIPLIER", fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Auto-Predict Switch Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AviatorGold, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Auto-Signal Mode (Continuous)",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
              )
            }

            Switch(
              checked = autoPredictEnabled,
              onCheckedChange = { onToggleAutoPredict() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AviatorRedPrimary,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = AviatorCardBg
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // STRATEGY CALCULATOR CARDS
      Text(
        text = "CASHOUT STRATEGY MATRIX",
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
          letterSpacing = 1.sp
        ),
        modifier = Modifier.padding(bottom = 8.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        StrategyCard(
          title = "Safe Bet",
          range = "1.50x - 2.00x",
          winRate = "98%",
          accentColor = AviatorGreen,
          modifier = Modifier.weight(1f)
        )
        StrategyCard(
          title = "Balanced",
          range = "2.00x - 5.00x",
          winRate = "92%",
          accentColor = AviatorGold,
          modifier = Modifier.weight(1f)
        )
        StrategyCard(
          title = "High Flyer",
          range = "5.00x+",
          winRate = "84%",
          accentColor = AviatorRedLight,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Bottom Server Status & Fluctuating Online Users Display
      Card(
        colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AviatorGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "ALGORITHM V4.2 ACTIVE",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(AviatorGreen.copy(alpha = 0.15f))
              .border(1.dp, AviatorGreen, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "⚡ $onlineUsers USERS ONLINE",
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              color = AviatorGreen
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}

@Composable
private fun AviatorRadarCanvas(isCalculating: Boolean) {
  val infiniteTransition = rememberInfiniteTransition(label = "radar")
  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotationAngle"
  )

  Canvas(modifier = Modifier.fillMaxSize()) {
    val center = Offset(size.width / 2, size.height / 2)
    val maxRadius = kotlin.math.min(size.width, size.height) / 2.2f

    // Draw Radar Grid Circles
    drawCircle(
      color = Color(0xFF382952),
      radius = maxRadius,
      center = center,
      style = Stroke(width = 1.dp.toPx())
    )
    drawCircle(
      color = Color(0xFF382952),
      radius = maxRadius * 0.65f,
      center = center,
      style = Stroke(width = 1.dp.toPx())
    )
    drawCircle(
      color = Color(0xFF382952),
      radius = maxRadius * 0.35f,
      center = center,
      style = Stroke(width = 1.dp.toPx())
    )

    // Crosshairs
    drawLine(
      color = Color(0xFF2B1F40),
      start = Offset(center.x, center.y - maxRadius),
      end = Offset(center.x, center.y + maxRadius),
      strokeWidth = 1.dp.toPx()
    )
    drawLine(
      color = Color(0xFF2B1F40),
      start = Offset(center.x - maxRadius, center.y),
      end = Offset(center.x + maxRadius, center.y),
      strokeWidth = 1.dp.toPx()
    )

    // Aviator Flight Path Curve
    val path = Path().apply {
      moveTo(30f, size.height - 20f)
      quadraticTo(
        size.width * 0.4f, size.height * 0.7f,
        size.width - 40f, 30f
      )
    }

    drawPath(
      path = path,
      color = Color(0xFFE50914),
      style = Stroke(width = 3.dp.toPx())
    )

    // Glowing Airplane Dot at peak
    drawCircle(
      color = Color(0xFFFFD700),
      radius = 6.dp.toPx(),
      center = Offset(size.width - 40f, 30f)
    )
  }
}

@Composable
private fun StrategyCard(
  title: String,
  range: String,
  winRate: String,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = accentColor
        )
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = range,
        style = MaterialTheme.typography.bodySmall.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "$winRate Win",
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 10.sp,
          color = TextMuted
        )
      )
    }
  }
}

@Composable
private fun PredictionHistoryRow(item: PredictionHistoryEntity) {
  Card(
    colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.TrendingUp,
          contentDescription = null,
          tint = AviatorGreen,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = item.roundCode,
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
        )
      }

      Text(
        text = "%.2fx".format(item.predictedMultiplier),
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Black,
          color = AviatorGold
        )
      )

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(AviatorGreen.copy(alpha = 0.2f))
          .padding(horizontal = 8.dp, vertical = 2.dp)
      ) {
        Text(
          text = "${item.accuracy}% SUCCESS",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = AviatorGreen
        )
      }
    }
  }
}
