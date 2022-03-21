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



package com.androidpoet.materialnotes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.androidpoet.materialnotes.databinding.NoteItemBinding
import com.androidpoet.materialnotes.model.Note

class NoteAdapter : PagingDataAdapter<Note, NoteViewHolder>(SHOW_COMPARATOR) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
    return NoteViewHolder(
      NoteItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent, false
      )
    ).apply {
      binding.root.setOnClickListener {
        getItem(bindingAdapterPosition)?.let { it1 ->
          callback?.onClick(
            binding.cardView,
            it1
          )
        }
      }

      binding.delete.setOnClickListener {
        getItem(bindingAdapterPosition)?.let { it1 ->
          deletecallback?.onClick(
            it1
          )
        }
      }
    }
  }

  override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    holder.binding.cardView.transitionName = getItem(position)?.id.toString()
    holder.binding.note = getItem(position)
    holder.binding.executePendingBindings()
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == itemCount) SHOW_ITEM else LOADING_ITEM
  }

  companion object {
    private val SHOW_COMPARATOR = object : DiffUtil.ItemCallback<Note>() {
      override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem == newItem
    }
    private const val SHOW_ITEM = 0
    const val LOADING_ITEM = 1
  }

  var callback: Callback? = null

  interface Callback {
    fun onClick(view: CardView, item: Note)
  }

  interface DeleteCallback {
    fun onClick(item: Note)
  }

  var deletecallback: DeleteCallback? = null
}
