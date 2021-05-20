package com.xter.slimnotek.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.xter.slimnotek.NoteApp
import com.xter.slimnotek.R
import com.xter.slimnotek.databinding.FragmentNoteDetailBinding
import com.xter.slimnotek.extension.NodeViewModeFactory
import com.xter.slimnotek.util.FontLoader
import com.xter.slimnotek.util.L

class NoteDetailFragment : Fragment() {

    companion object {
        fun create(id: String?):NoteDetailFragment{
            val fragment = NoteDetailFragment()
            val bundle = Bundle()
            bundle.putString("noteid",id)
            fragment.arguments = bundle
            return fragment
        }
    }

//    private val args: NoteDetailFragmentArgs by navArgs()

    private lateinit var noteDetailBinding: FragmentNoteDetailBinding

    private val detailViewModel by viewModels<NoteDetailViewModel> {
        NodeViewModeFactory(
            (requireContext().applicationContext as NoteApp).noteSource,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteDetailBinding = FragmentNoteDetailBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = detailViewModel
            }
        arguments?.getString("noteid")?.let {
            detailViewModel.load(it)
        }
        return noteDetailBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        noteDetailBinding.lifecycleOwner = this.viewLifecycleOwner

//        detailViewModel.load(args.noteid)
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_note)?.run {
            setOnClickListener {
                detailViewModel.editable.value?.let { editable ->
                    if (editable) {
                        val title =
                            activity?.findViewById<EditText>(R.id.et_detail_title)?.text.toString()
                        val content =
                            activity?.findViewById<EditText>(R.id.et_detail_content)?.text.toString()
                        detailViewModel.updateAndSave(title, content)
                    } else {
                        detailViewModel.edit()
                    }
                }
            }
            detailViewModel.editable.observe(viewLifecycleOwner, Observer { editable ->
                if (editable) {
                    setImageResource(R.drawable.ic_done)
                } else {
                    setImageResource(R.drawable.ic_edit)
                }
            })
        }
        detailViewModel.snackbarText.observe(viewLifecycleOwner, Observer {
            view?.showSnackbar(it, Snackbar.LENGTH_SHORT)
        })
        activity?.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        context?.let {
            FontLoader.getMNJZY(it)?.let { typeface ->
                noteDetailBinding.etDetailContent.typeface = typeface
                noteDetailBinding.etDetailTitle.typeface = typeface
            }
        }
    }

}
