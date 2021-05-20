package com.xter.slimnotek.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xter.slimnotek.NoteApp
import com.xter.slimnotek.R
import com.xter.slimnotek.extension.NodeViewModeFactory
import com.xter.slimnotek.util.L
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @Author XTER
 * @Date 2021/5/13 14:29
 * @Description
 */
class NoteDetailContainerFragment: Fragment() {
    private val noteViewModel by viewModels<NoteViewModel> {
        NodeViewModeFactory(
            (requireContext().applicationContext as NoteApp).noteSource,
            this
        )
    }

    private lateinit var viewPager: ViewPager2

    private val args: NoteDetailContainerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_note_container,container,false)
        viewPager = root.findViewById(R.id.vp_note)
        val ids:String = args.noteids
        val noteIds:List<String> = ids.split(",")
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return NoteDetailFragment.create(noteIds.get(position))
            }

            override fun getItemCount(): Int {
                return noteIds.size
            }
        }
        viewPager.currentItem = args.curPos
        return root
    }

}