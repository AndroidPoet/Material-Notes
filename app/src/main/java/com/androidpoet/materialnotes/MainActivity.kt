/*
 * Designed and developed by 2022 AndroidPoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.androidpoet.materialnotes

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.FloatingWindow
import com.androidpoet.materialnotes.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    FloatingActionButton


    window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

    if (Build.VERSION.SDK_INT >= 30) {

      // Root ViewGroup of my activity
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decor: View = window.decorView
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
      }
      ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->

        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        // Apply the insets as a margin to the view. Here the system is setting
        // only the bottom, left, and right dimensions, but apply whichever insets are
        // appropriate to your layout. You can also update the view padding
        // if that's more appropriate.

        view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
          leftMargin = insets.left
          bottomMargin = insets.bottom
          rightMargin = insets.right
        }

        // Return CONSUMED if you don't want want the window insets to keep being
        // passed down to descendant views.
        WindowInsetsCompat.CONSUMED
      }
    }
  }
}
