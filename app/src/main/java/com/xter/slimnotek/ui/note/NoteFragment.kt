package com.xter.slimnotek.ui.note

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xter.slimnotek.NoteApp
import com.xter.slimnotek.R
import com.xter.slimnotek.databinding.FragmentNoteBinding
import com.xter.slimnotek.extension.SimpleLinearSnapHelper
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
                    L.d("pos${position}")
//                    noteViewModel.items.value?.get(position)?.let { note ->
//                        navigateToDetail(note.id, note.title)
//                    }
                    navigateToContainer(position)
                }

                override fun onItemLongClick(holderK: ViewHolderK, position: Int) {
                    noteViewModel.selected(position)
                    notesAdapter.notifyItemChanged(position)
                }
            })
            adapter = notesAdapter
            addItemDecoration(QuickItemDecoration(context, LinearLayoutManager.VERTICAL))
            val hls = SimpleLinearSnapHelper(
                context,
                150f,
                RecyclerView.VERTICAL
            )
            hls.attachToRecyclerView(this)
//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    when (newState) {
//                        RecyclerView.SCROLL_STATE_IDLE -> {
//                            L.d("idle")
////                            (layoutManager as LinearLayoutManager).let {
////                                val firstVisibleItemPos = it.findFirstVisibleItemPosition()
////                                val lastCompleteItemPos = it.findLastCompletelyVisibleItemPosition()
////                                val midItemPos = (lastCompleteItemPos - firstVisibleItemPos) / 2
////                                it.getChildAt(midItemPos).let { view ->
////                                    view?.setBackgroundColor(Color.RED)
////                                }
////                            }
//                        }
//                        RecyclerView.SCROLL_STATE_SETTLING -> {
//                            L.d("set")
//                        }
//                        RecyclerView.SCROLL_STATE_DRAGGING -> {
//                            L.d("drag")
//                        }
//                    }
//                }
//            })
        }
        noteFragBinding.fabAddNote?.run {
            setOnClickListener {
                navigateToAddNote()
            }
        }
        noteViewModel.selectedNum.observe(viewLifecycleOwner, Observer { num ->
            if (num > 0) {
                switchToolbar("选中$num", true)
            } else {
                switchToolbar("所有笔记", false)
            }
        })
        noteViewModel.operationState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                REMOVE_SUCCESS -> {
                    view?.showSnackbar("删除成功", Snackbar.LENGTH_SHORT)
                }
                REFRESH_SUCCESS -> {
                    noteFragBinding.rvNotesList.smoothScrollBy(0, -500)
                    view?.showSnackbar("刷新成功", Snackbar.LENGTH_SHORT)
                }
            }
        })
        hideKeyboard()
    }

    private fun navigateToAddNote() {
        val action = NoteFragmentDirections.notesToAdd()
        findNavController().navigate(action)
    }

    private fun navigateToDetail(noteid: String, title: String) {
//        val action = NoteFragmentDirections.notesToDetail(noteid, title)
//        findNavController().navigate(action)
    }

    private fun navigateToContainer(pos:Int){
        val action = NoteFragmentDirections.notesToContainer(noteViewModel.getItemIds(),pos)
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
            if (showMeun) {
                toolbar.setNavigationOnClickListener {
                    noteViewModel.clearFocus()
                    notesAdapter.notifyDataSetChanged()
                }
            }
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

const val REMOVE_SUCCESS: Int = 1
const val REFRESH_SUCCESS: Int = 2
