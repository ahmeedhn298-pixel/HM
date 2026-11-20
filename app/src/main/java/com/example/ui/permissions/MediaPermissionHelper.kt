package com.example.ui.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

enum class MediaAccessStatus {
  FULL,
  PARTIAL,
  DENIED;

  val isAllowed: Boolean
    get() = this == FULL || this == PARTIAL
}

object MediaPermissionHelper {

  fun requiredMediaPermissions(sdkInt: Int = Build.VERSION.SDK_INT): Array<String> =
    when {
      sdkInt >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ->
        arrayOf(
          Manifest.permission.READ_MEDIA_IMAGES,
          Manifest.permission.READ_MEDIA_VIDEO,
          Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        )
      sdkInt == Build.VERSION_CODES.TIRAMISU ->
        arrayOf(
          Manifest.permission.READ_MEDIA_IMAGES,
          Manifest.permission.READ_MEDIA_VIDEO,
        )
      else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

  fun evaluatePermissionResult(
    grantResults: Map<String, Boolean>,
    sdkInt: Int = Build.VERSION.SDK_INT,
  ): MediaAccessStatus {
    return when {
      sdkInt >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
        val imagesGranted = grantResults[Manifest.permission.READ_MEDIA_IMAGES] == true
        val videoGranted = grantResults[Manifest.permission.READ_MEDIA_VIDEO] == true
        val partialGranted =
          grantResults[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == true
        when {
          imagesGranted && videoGranted -> MediaAccessStatus.FULL
          imagesGranted || videoGranted || partialGranted -> MediaAccessStatus.PARTIAL
          else -> MediaAccessStatus.DENIED
        }
      }
      sdkInt == Build.VERSION_CODES.TIRAMISU -> {
        val imagesGranted = grantResults[Manifest.permission.READ_MEDIA_IMAGES] == true
        val videoGranted = grantResults[Manifest.permission.READ_MEDIA_VIDEO] == true
        when {
          imagesGranted && videoGranted -> MediaAccessStatus.FULL
          imagesGranted || videoGranted -> MediaAccessStatus.PARTIAL
          else -> MediaAccessStatus.DENIED
        }
      }
      else -> {
        val storageGranted = grantResults[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        if (storageGranted) MediaAccessStatus.FULL else MediaAccessStatus.DENIED
      }
    }
  }

  fun checkCurrentAccessStatus(
    context: Context,
    sdkInt: Int = Build.VERSION.SDK_INT,
  ): MediaAccessStatus {
    val permissions = requiredMediaPermissions(sdkInt)
    val resultMap =
      permissions.associateWith { permission ->
        ContextCompat.checkSelfPermission(context, permission) ==
          PackageManager.PERMISSION_GRANTED
      }
    return evaluatePermissionResult(resultMap, sdkInt)
  }
}
