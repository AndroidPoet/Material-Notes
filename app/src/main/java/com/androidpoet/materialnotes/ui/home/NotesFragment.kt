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



package com.androidpoet.materialnotes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.androidpoet.materialnotes.R
import com.androidpoet.materialnotes.databinding.FragmentHomeBinding
import com.androidpoet.materialnotes.model.Note
import com.androidpoet.materialnotes.ui.adapters.LoadingStateAdapter
import com.androidpoet.materialnotes.ui.adapters.NoteAdapter
import com.androidpoet.metaphor.metaphorStartFragmentWithoutAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NotesFragment : Fragment() {
  private var _binding: FragmentHomeBinding? = null
  private val viewModel: NotesViewModel by viewModels()
  private lateinit var noteAdapter: NoteAdapter

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    noteAdapter = NoteAdapter()

    noteAdapter.callback = object : NoteAdapter.Callback {
      override fun onClick(view: CardView, item: Note) {
        val extras = FragmentNavigatorExtras(view to item.id.toString())
        val action = NotesFragmentDirections.actionNavigationHomeToDetailFragment(item)
        findNavController().navigate(action, extras)
      }
    }

    noteAdapter.deletecallback = object : NoteAdapter.DeleteCallback {
      override fun onClick(item: Note) {
        viewModel.delete(item)
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    metaphorStartFragmentWithoutAnimation(binding.recyclerView)

    binding.recyclerView.apply {
      val layoutManager = layoutManager as GridLayoutManager
      layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
          return if (noteAdapter.getItemViewType(position) == NoteAdapter.LOADING_ITEM)
            1 else 2
        }
      }
      adapter = noteAdapter.withLoadStateHeaderAndFooter(
        header = LoadingStateAdapter { noteAdapter.retry() },
        footer = LoadingStateAdapter { noteAdapter.retry() }
      )
    }

    binding.recyclerView.apply {
      layoutManager = GridLayoutManager(requireContext(), 2)
      adapter = noteAdapter.apply {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
          viewModel.getAllNotes.collectLatest(noteAdapter::submitData)
        }
      }
    }

    binding.fab.apply {
      setShowMotionSpecResource(R.animator.fab_show)
      setHideMotionSpecResource(R.animator.fab_hide)
      binding.fab.setOnClickListener {
        val action = NotesFragmentDirections.actionNavigationHomeToAddnoteFragment()
        findNavController().navigate(action)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
