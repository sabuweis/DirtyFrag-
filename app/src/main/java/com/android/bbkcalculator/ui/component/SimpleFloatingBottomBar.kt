package com.android.bbkcalculator.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home as FilledHome
import androidx.compose.material.icons.filled.Settings as FilledSettings
import androidx.compose.material.icons.rounded.Home as RoundedHome
import androidx.compose.material.icons.rounded.Settings as RoundedSettings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawScope.drawRoundRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.tap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.android.bbkcalculator.ui.theme.LocalCalculatorColors

data class BottomBarItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun SimpleFloatingBottomBar(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    selectedIndex: Int,
    onDestinationClick: (Int) -> Unit,
    items: List<BottomBarItem>
) {
    val colors = LocalCalculatorColors.current
    val isDark = colors === com.android.bbkcalculator.ui.theme.CalculatorColors.Dark
    
    val backgroundColor = if (isDark) colors.surfaceContainer.copy(alpha = 0.8f) else colors.surfaceContainer.copy(alpha = 0.9f)
    val borderColor = colors.outlineVariant.copy(alpha = 0.5f)
    val indicatorColor = colors.primary
    val contentColor = colors.onSurface
    val selectedContentColor = colors.onPrimary
    
    val pillShape = RoundedCornerShape(28.dp)
    val containerShape = RoundedCornerShape(28.dp)
    
    val selectedIndexState = remember { mutableIntStateOf(selectedIndex) }
    val animProgress = remember { mutableStateOf(0f) }
    
    // 液态玻璃效果容器
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.Transparent)
            .drawBehind {
                // 绘制背景色
                drawRoundRect(
                    color = backgroundColor,
                    topLeft = containerShape.topLeft.size,
                    topRight = containerShape.topRight.size,
                    bottomRight = containerShape.bottomRight.size,
                    bottomLeft = containerShape.bottomLeft.size,
                    style = androidx.compose.ui.draw.Stroke(width = 1.dp.toPx()),
                    color = borderColor
                )
            }
            .clip(containerShape)
            .shadow(elevation = 20.dp, shape = containerShape, clip = true)
    ) {
        Row(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndexState.value == index
                val progress = animProgress.value
                
                val itemWidth = (1f / items.size)
                val targetX = (index + 0.5f) * itemWidth - 0.5f
                val currentX = if (isSelected) targetX else selectedIndexState.value.toFloat() * itemWidth - 0.5f + (targetX - (selectedIndexState.value.toFloat() * itemWidth - 0.5f)) * progress
                
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .pointerInput(Unit) {
                            tap {
                                if (selectedIndexState.value != index) {
                                    selectedIndexState.value = index
                                    animProgress.value = 1f
                                    androidx.compose.animation.core.animateFloatAsState(
                                        targetValue = 0f,
                                        animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f)
                                    ).value
                                    onDestinationClick(index)
                                }
                            }
                        }
                        .graphicsLayer {
                            // 选中项放大效果
                            val scale = if (isSelected) 1.1f else 1f
                            scaleX = scale
                            scaleY = scale
                        }
                ) {
                    Column(
                        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 图标
                        val iconToShow = if (isSelected) item.selectedIcon else item.icon
                        androidx.compose.foundation.Icon(
                            imageVector = iconToShow,
                            contentDescription = item.label,
                            tint = if (isSelected) selectedContentColor else contentColor,
                            modifier = androidx.compose.ui.Modifier.size(24.dp)
                        )
                        
                        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(2.dp))
                        
                        // 标签
                        androidx.compose.material3.Text(
                            text = item.label,
                            color = if (isSelected) selectedContentColor else contentColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Medium else androidx.compose.ui.text.font.FontWeight.Normal,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        
        // 液态指示器
        if (items.isNotEmpty()) {
            val selectedItem = items[selectedIndexState.value]
            val itemWidth = 1f / items.size
            val indicatorWidth = itemWidth * 0.8f
            
            Box(
                modifier = androidx.compose.ui.Modifier
                    .width(indicatorWidth * 0.8f)
                    .height(56.dp)
                    .align(Alignment.CenterStart)
                    .graphicsLayer {
                        translationX = selectedIndexState.value.toFloat() * (1f / items.size) * size.width + (1f / items.size) * size.width * 0.1f
                    }
                    .background(indicatorColor, pillShape)
                    .clip(pillShape)
                    .shadow(elevation = 8.dp, shape = pillShape, clip = true),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Icon(
                    imageVector = selectedItem.selectedIcon,
                    contentDescription = "",
                    tint = selectedContentColor,
                    modifier = androidx.compose.ui.Modifier.size(24.dp)
                )
            }
        }
    }
}