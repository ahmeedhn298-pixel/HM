package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.permissions.MediaPermissionHelper
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TogetherBlue

private enum class ConsentFeedbackState {
  NONE,
  PERMISSION_DENIED,
  DECLINED,
}

@Composable
fun ConsentScreen(onPermissionAllowed: () -> Unit) {
  var feedbackState by rememberSaveable { mutableStateOf(ConsentFeedbackState.NONE) }

  val permissionLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
      val status = MediaPermissionHelper.evaluatePermissionResult(grantResults)
      if (status.isAllowed) {
        onPermissionAllowed()
      } else {
        // Permission denied: access nothing and remain on Consent.
        feedbackState = ConsentFeedbackState.PERMISSION_DENIED
      }
    }

  Box(
    modifier =
      Modifier.fillMaxSize()
        .background(NavyBackground)
        .background(
          Brush.verticalGradient(
            colors =
              listOf(
                TogetherBlue.copy(alpha = 0.08f),
                Color.Transparent,
                NavyBackground,
              )
          )
        )
        .systemBarsPadding()
        .padding(horizontal = 24.dp, vertical = 32.dp),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier =
        Modifier.fillMaxWidth()
          .widthIn(max = 520.dp),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
          text = stringResource(R.string.brand_wordmark),
          style =
            MaterialTheme.typography.labelMedium.copy(
              letterSpacing = 4.sp,
              fontWeight = FontWeight.Bold,
            ),
          color = TextMuted,
        )

        Text(
          text = stringResource(R.string.consent_title),
          style = MaterialTheme.typography.headlineLarge,
          color = TextPrimary,
          modifier = Modifier.testTag("consent_title"),
        )

        Text(
          text = stringResource(R.string.consent_body),
          style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
          color = TextSecondary,
          modifier = Modifier.testTag("consent_body"),
        )

        if (feedbackState != ConsentFeedbackState.NONE) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text =
              when (feedbackState) {
                ConsentFeedbackState.PERMISSION_DENIED ->
                  stringResource(R.string.consent_denied_notice)
                ConsentFeedbackState.DECLINED ->
                  stringResource(R.string.consent_declined_notice)
                ConsentFeedbackState.NONE -> ""
              },
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.testTag("consent_feedback_text"),
          )
        }
      }

      Spacer(modifier = Modifier.height(40.dp))

      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Button(
          onClick = {
            feedbackState = ConsentFeedbackState.NONE
            permissionLauncher.launch(MediaPermissionHelper.requiredMediaPermissions())
          },
          shape = RoundedCornerShape(12.dp),
          colors =
            ButtonDefaults.buttonColors(
              containerColor = TogetherBlue,
              contentColor = TextPrimary,
            ),
          modifier =
            Modifier.fillMaxWidth()
              .height(54.dp)
              .testTag("consent_accept_button"),
        ) {
          Text(
            text = stringResource(R.string.consent_accept),
            style = MaterialTheme.typography.labelLarge,
          )
        }

        OutlinedButton(
          onClick = {
            // Ablehnen does not request any permission and accesses nothing.
            feedbackState = ConsentFeedbackState.DECLINED
          },
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, NavyBorder),
          colors =
            ButtonDefaults.outlinedButtonColors(
              containerColor = NavySurfaceElevated,
              contentColor = TextPrimary,
            ),
          modifier =
            Modifier.fillMaxWidth()
              .height(54.dp)
              .testTag("consent_decline_button"),
        ) {
          Text(
            text = stringResource(R.string.consent_decline),
            style = MaterialTheme.typography.labelLarge,
          )
        }
      }
    }
  }
}
