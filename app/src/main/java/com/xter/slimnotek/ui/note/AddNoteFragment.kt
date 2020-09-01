package com.xter.slimnotek.ui.note

import android.app.ProgressDialog.show
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.xter.slimnotek.NoteApp

import com.xter.slimnotek.R
import com.xter.slimnotek.databinding.FragmentAddNoteBinding
import com.xter.slimnotek.extension.NodeViewModeFactory
import com.xter.slimnotek.util.L
import kotlinx.coroutines.runBlocking

class AddNoteFragment : Fragment() {

    companion object {
        fun newInstance() = AddNoteFragment()
    }

    private lateinit var addNoteBinding: FragmentAddNoteBinding

    private val addNoteViewModel by viewModels<AddNoteViewModel> {
        NodeViewModeFactory(
            (requireContext().applicationContext as NoteApp).noteSource,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false).apply {
            viewmodel = addNoteViewModel
        }
        return addNoteBinding.root
    }

    //TODO 桌面小工具
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addNoteBinding.lifecycleOwner = this.viewLifecycleOwner
        activity?.findViewById<FloatingActionButton>(R.id.fab_save_note)?.run {
            setOnClickListener {
                addNoteViewModel.saveNote()
            }
        }
        addNoteViewModel.snackbarText.observe(viewLifecycleOwner,
            Observer<String> {
                view?.showSnackbar(it, Snackbar.LENGTH_SHORT)
            })
        addNoteViewModel.completed.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it) {
                navigateToNotes()
            }
        })
        activity?.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToNotes() {
        val action = AddNoteFragmentDirections.addToNotes("所有笔记")
        findNavController().navigate(action)
    }

}
