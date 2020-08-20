package com.xter.slimnotek.ui.note

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.xter.slimnotek.NoteApp
import com.xter.slimnotek.R
import com.xter.slimnotek.databinding.FragmentNoteBinding
import com.xter.slimnotek.extension.NodeViewModeFactory
import com.xter.slimnotek.util.L
import com.xter.slimnotek.util.QuickItemDecoration


class NoteFragment : Fragment() {

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
            noteFragBinding.notesViewModel?.let { viewModel ->
                notesAdapter = NotesAdapter(viewModel)
            }
            notesAdapter.setItemClickListener(object : OnItemClickListener {
                override fun onItemClick(holderK: ViewHolderK, position: Int) {
                    L.d("pos:$position")
                }

                override fun onItemLongClick(holderK: ViewHolderK, position: Int) {
                    noteViewModel.selected(position)
                    notesAdapter.notifyItemChanged(position)
                }
            })
            adapter = notesAdapter
            addItemDecoration(QuickItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        noteFragBinding.fabAddNote?.run {
            setOnClickListener {
                navigateToAddNote()
            }
        }
        noteViewModel.dataLoading.observe(viewLifecycleOwner, Observer { bool ->
            if (!bool) {
                view?.showSnackbar("刷新成功", Snackbar.LENGTH_SHORT)
                noteFragBinding.rvNotesList.smoothScrollBy(0, -500)
            }
        })
        noteViewModel.selectedNum.observe(viewLifecycleOwner, Observer { num ->
            if (num < 0) {
                view?.showSnackbar("删除成功", Snackbar.LENGTH_SHORT)
                switchToolbar("所有笔记", false)
            } else {
                if (num > 0) {
                    switchToolbar("选中$num", true)
                } else {
                    switchToolbar("所有笔记", false)
                }
            }
        })
        hideKeyboard()
    }

    private fun navigateToAddNote() {
        val action = NoteFragmentDirections.notesToAdd();
        findNavController().navigate(action)
    }

    private fun hideKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0)
        }
    }

    private fun switchToolbar(title: String, showMeun: Boolean) {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            setHasOptionsMenu(showMeun)
            toolbar.title = title
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(context).setTitle("删除笔记")
            .setMessage("确认删除${noteViewModel.selectedNum.value}条笔记？")
            .setPositiveButton(
                "确定"
            ) { dialogInterface, i ->
                noteViewModel.remove()
            }
            .setNegativeButton(
                "取消"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }.also {
                it.create().show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.note_remove -> {
                showDialog()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notes_list, menu)
    }

    private fun getScreenOffset(): Int? {
        return activity?.resources?.displayMetrics?.widthPixels
    }
}

const val REMOVE_SCENE: Int = 1
