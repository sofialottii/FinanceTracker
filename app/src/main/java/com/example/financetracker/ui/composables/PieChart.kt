package com.example.financetracker.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.financetracker.ui.screens.charts.CategoryStat

@Composable
fun PieChart(
    data: List<CategoryStat>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(200.dp)) {
        val total = data.sumOf { it.totalAmount }
        var startAngle = -90f

        data.forEach { stat ->
            val sweepAngle = (stat.percentage * 360f)

            drawArc(
                color = Color(stat.color),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 40.dp.toPx())
            )

            startAngle += sweepAngle
        }
    }
}