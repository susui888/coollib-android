package com.example.coollib.ui.screens.statistics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    onSeeMoreLoans: () -> Unit = {},
    onLogin: () -> Unit = {},
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (!state.isLoggedIn) {
            LoginPrompt(
                onLoginClick = onLogin,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            StatisticsContent(
                state = state,
                onSeeMoreLoans = onSeeMoreLoans
            )
        }
    }
}

@Composable
fun StatisticsContent(
    state: StatisticsState,
    onSeeMoreLoans: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.medium),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                GradientProgressChart(
                    currentlyBorrowed = state.currentlyBorrowed,
                    targetLimit = 30,
                    modifier = Modifier.size(110.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        "Library Usage",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Based on 30-book monthly limit",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }


        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Quick Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                MiniStatCard(
                    title = "Overdue",
                    value = state.overdue.toString(),
                    icon = Icons.Default.Warning,
                    highlightColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )

                MiniStatCard(
                    title = "Due Soon",
                    value = state.dueSoon.toString(),
                    icon = Icons.Default.Schedule,
                    highlightColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }


            StatCard(
                title = "Life-time Total Borrowed",
                value = state.totalBorrowed.toString(),
                icon = Icons.AutoMirrored.Filled.LibraryBooks
            )
        }


        Button(
            onClick = onSeeMoreLoans,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("See Detailed Loan History")
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MiniStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    highlightColor: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = highlightColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
@Composable
fun GradientProgressChart(
    modifier: Modifier = Modifier,
    currentlyBorrowed: Int,
    targetLimit: Int = 30,
    strokeWidth: Dp = 10.dp
) {
    // 关键点 1：使用 remember 保持一个本地进度状态
    var targetPercent by remember { mutableFloatStateOf(0f) }
    var targetCount by remember { mutableIntStateOf(0) }

    // 关键点 2：当组件加载时，将目标值设为实际值，触发动画
    LaunchedEffect(currentlyBorrowed) {
        targetPercent = currentlyBorrowed.toFloat() / targetLimit
        targetCount = currentlyBorrowed
    }

    // 核心进度动画（应用了你要求的 300ms 延迟）
    val animatedPercent by animateFloatAsState(
        targetValue = targetPercent, // 绑定到本地状态
        animationSpec = tween(
            durationMillis = 1500,
            delayMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )

    // 数字滚动动画
    val animatedNumber by animateIntAsState(
        targetValue = targetCount,
        animationSpec = tween(durationMillis = 1500, delayMillis = 300),
        label = "numberScroll"
    )

    // 颜色过渡动画
    val targetColor = if (targetPercent > 1f) Color(0xFFE53935) else Color(0xFF2E7D32)
    val animatedContentColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(1000),
        label = "colorChange"
    )

    // 超限时的脉冲动画
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (targetPercent > 1f) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val gradientGreen = listOf(Color(0xFF81C784), Color(0xFF2E7D32))
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            drawCircle(color = trackColor, style = Stroke(width = strokePx))

            if (animatedPercent <= 1f) {
                drawArc(
                    brush = Brush.sweepGradient(gradientGreen),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedPercent,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            } else {
                drawCircle(color = Color(0xFF2E7D32), style = Stroke(width = strokePx))
                drawArc(
                    color = animatedContentColor,
                    startAngle = -90f,
                    sweepAngle = (360f * (animatedPercent - 1f)).coerceAtMost(360f),
                    useCenter = false,
                    style = Stroke(width = strokePx * 1.1f, cap = StrokeCap.Round)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$animatedNumber",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp
                ),
                color = animatedContentColor
            )
            Text(
                text = "/ $targetLimit Books",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    CoolLibTheme {
        StatisticsContent(
            state = StatisticsState(
                currentlyBorrowed = 3,
                dueSoon = 1,
                overdue = 0,
                totalBorrowed = 27,
                isLoggedIn = true
            ),
            onSeeMoreLoans = {}
        )
    }
}
