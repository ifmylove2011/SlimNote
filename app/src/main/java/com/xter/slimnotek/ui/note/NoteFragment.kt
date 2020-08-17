package com.xter.slimnotek.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xter.slimnotek.util.L
import com.xter.slimnotek.NoteApp

import com.xter.slimnotek.R
import com.xter.slimnotek.databinding.FragmentNoteBinding
import com.xter.slimnotek.extension.NodeViewModeFactory

class NoteFragment : Fragment() {

    companion object {
        fun newInstance() = NoteFragment()
    }

    private val noteViewModel by viewModels<NoteViewModel> {
        NodeViewModeFactory(
            (requireContext().applicationContext as NoteApp).noteSource,
            this
        )
    }

    private lateinit var noteFragBinding: FragmentNoteBinding

    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteFragBinding = FragmentNoteBinding.inflate(inflater, container, false).apply {
            notesViewModel = noteViewModel
        }
        return noteFragBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noteFragBinding.lifecycleOwner = this.viewLifecycleOwner
        noteFragBinding.notesViewModel?.let {
            notesAdapter = NotesAdapter(it)
            noteFragBinding.rvNotesList.adapter = notesAdapter
        }
        activity?.findViewById<FloatingActionButton>(R.id.fab_add_note)?.run {
            setOnClickListener {
                navigateToAddNote()
            }
        }

    }

    private fun navigateToAddNote() {
        val action = NoteFragmentDirections.notesToAdd();
        findNavController().navigate(action)
    }

}
