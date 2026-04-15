package com.example.coollib.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

val randomBookName = (1..10).map { ('a'..'z').random() }.joinToString("")

@Composable
fun paintBookCover(title: String = randomBookName, author: String = randomBookName): BookCoverPainter {
    val textMeasurer = rememberTextMeasurer()
    return remember(title, author, textMeasurer) {
        BookCoverPainter(title, author, textMeasurer)
    }
}

class BookCoverPainter(
    private val title: String,
    private val author: String,
    private val textMeasurer: TextMeasurer
) : Painter() {

    private val colorPalette = listOf(
        Color(0xFF5DADE2), Color(0xFF48C9B0), Color(0xFFF4D03F),
        Color(0xFFEB984E), Color(0xFFAF7AC5), Color(0xFF52BE80)
    )

    private val baseColor: Color = if (title.isEmpty()) {
        colorPalette[0]
    } else {
        val index = abs(title.hashCode()) % colorPalette.size
        colorPalette[index]
    }

    override val intrinsicSize: Size = Size.Unspecified

    override fun DrawScope.onDraw() {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return

        // --- 1. 绘制背景：上下分层 ---
        // 顶部 40% 为黑色色块，底部 60% 为彩色色块
        val topSectionHeight = h * 0.4f
        drawRect(color = Color(0xFF1A1A1A), size = Size(w, topSectionHeight))
        drawRect(
            color = baseColor,
            topLeft = Offset(0f, topSectionHeight),
            size = Size(w, h - topSectionHeight)
        )

        // --- 2. 绘制白色细线外框 ---
        val padding = w * 0.05f
        drawRect(
            color = Color.White.copy(alpha = 0.8f),
            topLeft = Offset(padding, padding),
            size = Size(w - 2 * padding, h - 2 * padding),
            style = Stroke(width = 1.dp.toPx())
        )

        // --- 3. 绘制书名 (位于顶部黑色区域居中) ---

        var titleFontSize = (w * 0.04f).coerceIn(10f, 20f)
        if (title.length < 16)
            titleFontSize = (w * 0.07f).coerceIn(12f, 24f)

        val titleLayout = textMeasurer.measure(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = titleFontSize.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            constraints = Constraints(maxWidth = (w * 0.8f).toInt())
        )

        drawText(
            textLayoutResult = titleLayout,
            topLeft = Offset(
                (w - titleLayout.size.width) / 2,
                (topSectionHeight / 2) - (titleLayout.size.height / 2) + (padding / 2)
            )
        )

        // --- 4. 绘制作者名 (位于底部彩色区域下方居中) ---
        val authorFontSize = (w * 0.02f).coerceIn(4f, 10f)

        val authorLayout = textMeasurer.measure(
            text = author.uppercase(), // 使用全大写增加设计感
            style = TextStyle(
                color = Color.Black, // 进一步调淡颜色，视觉上会显得更小
                fontSize = authorFontSize.sp,
                fontWeight = FontWeight.Normal, // 使用细体
                letterSpacing = 0.5.sp, // 关键：增加字间距，小字才不拥挤
                textAlign = TextAlign.Center
            ),
            constraints = Constraints(maxWidth = (w * 0.7f).toInt())
        )

        drawText(
            textLayoutResult = authorLayout,
            topLeft = Offset(
                (w - authorLayout.size.width) / 2,
                h * 0.82f // 放在底部彩色区域的中间偏下位置
            )
        )
    }
}