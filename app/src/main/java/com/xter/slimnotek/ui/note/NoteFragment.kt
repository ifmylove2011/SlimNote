package com.xter.slimnotek.ui.note

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
            notesAdapter.setHasStableIds(true)
//            setItemViewCacheSize(20)
            notesAdapter.setItemClickListener(object : OnItemClickListener {
                override fun onItemClick(holderK: ViewHolderK, position: Int) {
                    L.d("pos:$position")
                }

                override fun onItemLongClick(holderK: ViewHolderK, position: Int) {
                    val state = noteViewModel.selected(position)
                    notesAdapter.notifyItemChanged(position)
//                    L.d("view:${holder.itemView.hashCode()}")
//                    state?.let {
//                        if (it.selected) {
//                            holder.itemView.setBackgroundColor(Color.LTGRAY)
////                            当被选中时，避免回收
////                            holder.setIsRecyclable(false)
//                        } else {
//                            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
//                            //未被选中时，可用回收
////                            holder.setIsRecyclable(true)
//                        }
//                    }
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
        noteViewModel.selectedPosition.observe(viewLifecycleOwner, Observer {
            L.i("当前选中:${it}")
            noteViewModel.states.value?.let { states ->
                val size = states.size
                if (size > 0) {
                    activity?.findViewById<Toolbar>(R.id.toolbar)?.let {toolbar ->
                        toolbar.title = "选中$size"
                    }
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

    private fun switchToolbar(scene: Int) {
        when (scene) {
            REMOVE_SCENE -> {
                activity?.findViewById<Toolbar>(R.id.toolbar)?.let {
                    it.title = "选中"
                }
            }
        }

    }

    private fun getScreenOffset(): Int? {
        return activity?.resources?.displayMetrics?.widthPixels
    }
}

const val REMOVE_SCENE: Int = 1
