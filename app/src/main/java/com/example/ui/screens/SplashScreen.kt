package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TogetherBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
  val alpha = remember { Animatable(0f) }
  val scale = remember { Animatable(0.94f) }

  LaunchedEffect(Unit) {
    launch {
      alpha.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
      )
    }
    launch {
      scale.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing),
      )
    }
    delay(1600L)
    onSplashFinished()
  }

  Box(
    modifier =
      Modifier.fillMaxSize()
        .background(NavyBackground)
        .background(
          Brush.radialGradient(
            colors =
              listOf(
                TogetherBlue.copy(alpha = 0.14f),
                Color.Transparent,
              ),
          )
        )
        .systemBarsPadding()
        .padding(horizontal = 24.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = stringResource(R.string.brand_wordmark),
      style =
        MaterialTheme.typography.displayLarge.copy(
          fontSize = 40.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 8.sp,
        ),
      color = TextPrimary,
      modifier =
        Modifier.alpha(alpha.value)
          .scale(scale.value)
          .testTag("splash_brand_text"),
    )
  }
}
