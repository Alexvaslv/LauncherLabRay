package com.example.ui.launcher

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

/**
 * Ultra-smooth zero-lag Analog Clock Widget.
 * Uses drawWithCache to prevent unnecessary recompositions and layout passes.
 */
@Composable
fun AnalogClockWidget(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(8.dp, RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFF1E1F24))
                .clickable { onClick() }
                .padding(10.dp)
                .testTag("clock_widget"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        val textPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = size.width * 0.12f
                            isAntiAlias = true
                            textAlign = android.graphics.Paint.Align.CENTER
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }

                        onDrawBehind {
                            val cal = Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
                            val hours = cal.get(Calendar.HOUR)
                            val minutes = cal.get(Calendar.MINUTE)
                            val seconds = cal.get(Calendar.SECOND)

                            val center = Offset(size.width / 2, size.height / 2)
                            val radius = size.width / 2 - 4.dp.toPx()

                            // Dial Background (White circle)
                            drawCircle(
                                color = Color.White,
                                radius = radius,
                                center = center
                            )

                            // Outer subtle rim
                            drawCircle(
                                color = Color(0xFF333333),
                                radius = radius,
                                center = center,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
                            )

                            // Numbers 1..12
                            for (i in 1..12) {
                                val angle = Math.toRadians((i * 30 - 90).toDouble())
                                val numRadius = radius * 0.76f
                                val numX = (center.x + numRadius * cos(angle)).toFloat()
                                val numY = (center.y + numRadius * sin(angle) - (textPaint.descent() + textPaint.ascent()) / 2).toFloat()

                                drawIntoCanvas {
                                    it.nativeCanvas.drawText(i.toString(), numX, numY, textPaint)
                                }
                            }

                            // Small tick marks
                            for (min in 0 until 60) {
                                val angle = Math.toRadians((min * 6 - 90).toDouble())
                                val outer = radius * 0.94f
                                val inner = if (min % 5 == 0) radius * 0.86f else radius * 0.90f
                                val strokeW = if (min % 5 == 0) 2.dp.toPx() else 1.dp.toPx()

                                drawLine(
                                    color = Color(0xFF444444),
                                    start = Offset((center.x + inner * cos(angle)).toFloat(), (center.y + inner * sin(angle)).toFloat()),
                                    end = Offset((center.x + outer * cos(angle)).toFloat(), (center.y + outer * sin(angle)).toFloat()),
                                    strokeWidth = strokeW,
                                    cap = StrokeCap.Round
                                )
                            }

                            // Hour Hand
                            val hourAngle = Math.toRadians(((hours + minutes / 60f) * 30 - 90).toDouble())
                            val hourLength = radius * 0.52f
                            drawLine(
                                color = Color(0xFF1A1A1A),
                                start = center - Offset((10.dp.toPx() * cos(hourAngle)).toFloat(), (10.dp.toPx() * sin(hourAngle)).toFloat()),
                                end = center + Offset((hourLength * cos(hourAngle)).toFloat(), (hourLength * sin(hourAngle)).toFloat()),
                                strokeWidth = 4.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )

                            // Minute Hand
                            val minAngle = Math.toRadians(((minutes + seconds / 60f) * 6 - 90).toDouble())
                            val minLength = radius * 0.72f
                            drawLine(
                                color = Color(0xFF1A1A1A),
                                start = center - Offset((12.dp.toPx() * cos(minAngle)).toFloat(), (12.dp.toPx() * sin(minAngle)).toFloat()),
                                end = center + Offset((minLength * cos(minAngle)).toFloat(), (minLength * sin(minAngle)).toFloat()),
                                strokeWidth = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )

                            // Second Hand (Orange)
                            val secAngle = Math.toRadians((seconds * 6 - 90).toDouble())
                            val secLength = radius * 0.82f
                            drawLine(
                                color = Color(0xFFFF9100),
                                start = center - Offset((14.dp.toPx() * cos(secAngle)).toFloat(), (14.dp.toPx() * sin(secAngle)).toFloat()),
                                end = center + Offset((secLength * cos(secAngle)).toFloat(), (secLength * sin(secAngle)).toFloat()),
                                strokeWidth = 1.8.dp.toPx(),
                                cap = StrokeCap.Round
                            )

                            // Center Pin
                            drawCircle(
                                color = Color(0xFFFFB300),
                                radius = 4.dp.toPx(),
                                center = center
                            )
                            drawCircle(
                                color = Color(0xFF3E2723),
                                radius = 2.dp.toPx(),
                                center = center
                            )
                        }
                    }
            ) {}
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Clock",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.8f),
                    offset = Offset(0f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}
