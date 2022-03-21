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

package com.androidpoet.materialnotes.ui.addnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.androidpoet.materialnotes.R
import com.androidpoet.materialnotes.databinding.AddNoteFragmentBinding
import com.androidpoet.materialnotes.model.Note
import com.androidpoet.metaphor.metaphorMaterialContainerTransformViewIntoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

  private var _binding: AddNoteFragmentBinding? = null
  private val viewModel: AddNoteViewModel by viewModels()

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = AddNoteFragmentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    metaphorMaterialContainerTransformViewIntoFragment(
      requireActivity().findViewById(R.id.fab),
      binding.root
    )

    binding.closeIcon.setOnClickListener {
      findNavController().popBackStack()
    }

    binding.save.setOnClickListener {
      val note = Note()
      if (binding.title.text.isNullOrEmpty()) {
        Toast.makeText(context, "Please Enter Title", Toast.LENGTH_SHORT).show()
      } else {
        note.title = binding.title.text.toString()
        if (binding.body.text.isNullOrEmpty()) {
          Toast.makeText(context, "Please Enter Content", Toast.LENGTH_SHORT).show()
        } else {
          note.content = binding.body.text.toString()
          viewModel.addNote(note)
          findNavController().popBackStack()
          Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
