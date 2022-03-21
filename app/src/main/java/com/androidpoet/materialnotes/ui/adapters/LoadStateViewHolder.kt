/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidpoet.materialnotes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.androidpoet.materialnotes.R
import com.androidpoet.materialnotes.databinding.LoadStateFooterViewItemBinding

class LoadStateViewHolder(
  private val binding: LoadStateFooterViewItemBinding,
  retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

  init {
    binding.retryButton.setOnClickListener { retry.invoke() }
  }

  fun bind(loadState: LoadState) {
    if (loadState is LoadState.Error) {
      binding.errorMsg.text = loadState.error.localizedMessage
    }
    binding.progressBar.isVisible = loadState is LoadState.Loading
    binding.retryButton.isVisible = loadState is LoadState.Error
    binding.errorMsg.isVisible = loadState is LoadState.Error
  }

  companion object {
    fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.load_state_footer_view_item, parent, false)
      val binding = LoadStateFooterViewItemBinding.bind(view)
      return LoadStateViewHolder(binding, retry)
    }
  }
}
