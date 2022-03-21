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

package com.androidpoet.materialnotes.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidpoet.materialnotes.databinding.FragmentDetailsBinding
import com.androidpoet.materialnotes.ui.addnote.AddNoteViewModel
import com.androidpoet.metaphor.metaphorDestinationFragmentMaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

  private var _binding: FragmentDetailsBinding? = null
  private val note: NoteDetailFragmentArgs by navArgs()
  private val viewModel: AddNoteViewModel by viewModels()

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentDetailsBinding.inflate(inflater, container, false).apply {
      lifecycleOwner = viewLifecycleOwner
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    metaphorDestinationFragmentMaterialContainerTransform(view, note.note.id.toString())
    binding.note = note.note

    binding.delete.setOnClickListener {
      viewModel.delete(note.note)
      findNavController().popBackStack()
    }

    binding.closeIcon.setOnClickListener {
      findNavController().popBackStack()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
