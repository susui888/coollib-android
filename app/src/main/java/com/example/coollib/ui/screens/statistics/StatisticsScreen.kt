package com.example.coollib.ui.screens.statistics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
            LoginPrompt(onLoginClick = onLogin, modifier = Modifier.align(Alignment.Center))
        } else {
            StatisticsContent(state = state, onSeeMoreLoans = onSeeMoreLoans)
        }
    }
}

@Composable
fun StatisticsContent(
    state: StatisticsState,
    onSeeMoreLoans: () -> Unit
) {
    // Shared height for both the circle container and the wave card for visual symmetry
    val primaryCardHeight = 160.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // --- Section 1: Library Usage Progress (Elevated) ---
        ElevatedCard(
            modifier = Modifier.fillMaxWidth().height(primaryCardHeight),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientProgressChart(
                    currentlyBorrowed = state.currentlyBorrowed,
                    targetLimit = 30,
                    modifier = Modifier.size(110.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text("Library Usage", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Monthly limit: 30 books", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // --- Section 2: Actionable Stats ---
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniStatCard(
                title = "Overdue",
                value = state.overdue.toString(),
                icon = Icons.Default.ErrorOutline,
                highlightColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
            MiniStatCard(
                title = "Due Soon",
                value = state.dueSoon.toString(),
                icon = Icons.Default.EventRepeat,
                highlightColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        // --- Section 3: Trend Analysis (Now matched in height) ---
        ReadingIntensityWaveCard(
            activityData = state.weeklyActivity,
            modifier = Modifier.fillMaxWidth().height(primaryCardHeight)
        )

        // --- Section 4: Lifetime Metrics & Navigation ---
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Life-time Total Borrowed",
                value = state.totalBorrowed.toString(),
                icon = Icons.AutoMirrored.Filled.LibraryBooks
            )

            Button(
                onClick = onSeeMoreLoans,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("See Detailed Loan History")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
        }
    }
}

@Composable
fun ReadingIntensityWaveCard(
    activityData: List<Float>,
    modifier: Modifier = Modifier
) {
    var targetData by remember { mutableStateOf(List(activityData.size) { 0f }) }
    LaunchedEffect(activityData) { targetData = activityData }

    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) { // Reduced padding slightly to fit content better
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("2-Week Activity", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Borrowing trends", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("Today", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Box now occupies the remaining space in the fixed-height card
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                ActivityWaveCanvas(targetData)
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
    ElevatedCard(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Icon(imageVector = icon, contentDescription = null, tint = highlightColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    ElevatedCard(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium)
                Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- Animation Components (GradientProgressChart & ActivityWaveCanvas logic remain consistent) ---

@Composable
fun GradientProgressChart(
    modifier: Modifier = Modifier,
    currentlyBorrowed: Int,
    targetLimit: Int = 30,
    strokeWidth: Dp = 10.dp
) {
    var targetPercent by remember { mutableFloatStateOf(0f) }
    var targetCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentlyBorrowed) {
        targetPercent = currentlyBorrowed.toFloat() / targetLimit
        targetCount = currentlyBorrowed
    }

    val animatedPercent by animateFloatAsState(targetValue = targetPercent, animationSpec = tween(1500, delayMillis = 300, easing = FastOutSlowInEasing), label = "progress")
    val animatedNumber by animateIntAsState(targetValue = targetCount, animationSpec = tween(1500, delayMillis = 300), label = "numberScroll")
    val targetColor = if (targetPercent > 1f) Color(0xFFE53935) else Color(0xFF2E7D32)
    val animatedContentColor by animateColorAsState(targetValue = targetColor, animationSpec = tween(1000), label = "colorChange")

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (targetPercent > 1f) 1.05f else 1f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            drawCircle(color = Color.LightGray.copy(alpha = 0.3f), style = Stroke(width = strokePx))
            if (animatedPercent <= 1f) {
                drawArc(brush = Brush.sweepGradient(listOf(Color(0xFF81C784), Color(0xFF2E7D32))), startAngle = -90f, sweepAngle = 360f * animatedPercent, useCenter = false, style = Stroke(width = strokePx, cap = StrokeCap.Round))
            } else {
                drawCircle(color = Color(0xFF2E7D32), style = Stroke(width = strokePx))
                drawArc(color = animatedContentColor, startAngle = -90f, sweepAngle = (360f * (animatedPercent - 1f)).coerceAtMost(360f), useCenter = false, style = Stroke(width = strokePx * 1.1f, cap = StrokeCap.Round))
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$animatedNumber", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold, fontSize = 36.sp), color = animatedContentColor)
            Text(text = "/ $targetLimit", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ActivityWaveCanvas(data: List<Float>) {
    val waveColor = Color(0xFF81C784)
    val animatedPoints = data.map { animateFloatAsState(targetValue = it, animationSpec = tween(1500, delayMillis = 500, easing = FastOutSlowInEasing), label = "wave") }

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (animatedPoints.size < 2) return@Canvas
        val width = size.width
        val height = size.height
        val pxPerStep = width / (animatedPoints.size - 1)
        fun getY(i: Int): Float = height * (1f - (animatedPoints[i].value * 0.7f + 0.15f))

        val fillPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, height)
            lineTo(0f, getY(0))
        }
        val strokePath = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, getY(0))
        }

        for (i in 0 until animatedPoints.size - 1) {
            val x1 = i * pxPerStep
            val y1 = getY(i)
            val x2 = (i + 1) * pxPerStep
            val y2 = getY(i + 1)
            val cx = x1 + (x2 - x1) / 2f
            fillPath.cubicTo(cx, y1, cx, y2, x2, y2)
            strokePath.cubicTo(cx, y1, cx, y2, x2, y2)
        }
        fillPath.lineTo(width, height)
        fillPath.close()

        drawPath(fillPath, brush = Brush.verticalGradient(listOf(waveColor.copy(alpha = 0.3f), Color.Transparent)))
        drawPath(strokePath, color = waveColor, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
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
                isLoggedIn = true,
                weeklyActivity = listOf(0.2f, 0.5f, 0.3f, 0.8f, 0.1f, 0.4f, 0.6f, 0.2f, 0.5f, 0.7f, 0.4f, 0.9f, 0.3f, 0.5f)
            ),
            onSeeMoreLoans = {}
        )
    }
}