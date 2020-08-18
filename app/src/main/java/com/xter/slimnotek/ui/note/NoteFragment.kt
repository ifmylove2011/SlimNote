package com.xter.slimnotek.ui.note

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.xter.slimnotek.NoteApp
import com.xter.slimnotek.R
import com.xter.slimnotek.data.Note
import com.xter.slimnotek.databinding.FragmentNoteBinding
import com.xter.slimnotek.extension.NodeViewModeFactory
import com.xter.slimnotek.util.L
import com.xter.slimnotek.util.QuickItemDecoration


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
        noteFragBinding.rvNotesList.apply {
            noteFragBinding.notesViewModel?.let {
                notesAdapter = NotesAdapter(it)
            }
            notesAdapter.setItemClickListener(object:OnItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    L.d("pos:$position")
                }

                override fun onItemLongClick(view: View, position: Int) {
                    L.d("pos:$position")
                    noteViewModel.items.value?.drop(position)
                }
            })
            adapter = notesAdapter
            addItemDecoration(QuickItemDecoration(context,LinearLayoutManager.VERTICAL))
        }
        noteFragBinding.fabAddNote?.run {
            setOnClickListener {
                navigateToAddNote()
            }
        }
        noteViewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if(!it){
//                view?.showSnackbar("刷新成功", Snackbar.LENGTH_SHORT)
                noteFragBinding.rvNotesList.smoothScrollBy(0, -500)
            }
        })
        hideKeyboard()
    }

    private fun navigateToAddNote() {
        val action = NoteFragmentDirections.notesToAdd();
        findNavController().navigate(action)
    }

    private fun hideKeyboard(){
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken,0)
        }
    }

    private fun getScreenOffset():Int?{
        return activity?.resources?.displayMetrics?.widthPixels
    }
}
