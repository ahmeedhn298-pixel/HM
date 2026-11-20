package com.example

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.ui.permissions.MediaAccessStatus
import com.example.ui.permissions.MediaPermissionHelper
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Together", appName)
  }

  @Test
  fun `required permissions match android 13 and 14 plus`() {
    assertArrayEquals(
      arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
      ),
      MediaPermissionHelper.requiredMediaPermissions(Build.VERSION_CODES.TIRAMISU),
    )

    assertArrayEquals(
      arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
      ),
      MediaPermissionHelper.requiredMediaPermissions(Build.VERSION_CODES.UPSIDE_DOWN_CAKE),
    )
  }

  @Test
  fun `android 14 partial access counts as allowed`() {
    val partial =
      MediaPermissionHelper.evaluatePermissionResult(
        grantResults =
          mapOf(
            Manifest.permission.READ_MEDIA_IMAGES to false,
            Manifest.permission.READ_MEDIA_VIDEO to false,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED to true,
          ),
        sdkInt = Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
      )
    assertEquals(MediaAccessStatus.PARTIAL, partial)
    assertTrue(partial.isAllowed)

    val denied =
      MediaPermissionHelper.evaluatePermissionResult(
        grantResults =
          mapOf(
            Manifest.permission.READ_MEDIA_IMAGES to false,
            Manifest.permission.READ_MEDIA_VIDEO to false,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED to false,
          ),
        sdkInt = Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
      )
    assertEquals(MediaAccessStatus.DENIED, denied)
    assertFalse(denied.isAllowed)
  }
}
