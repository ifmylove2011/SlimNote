package com.xter.slimnotek.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xter.slimnotek.NoteApp
import com.xter.slimnotek.databinding.FragmentNewsBinding
import com.xter.slimnotek.extension.NodeViewModeFactory
import com.xter.slimnotek.ui.note.NotesAdapter
import com.xter.slimnotek.util.L
import com.xter.slimnotek.util.QuickItemDecoration

class NewsFragment : Fragment() {

    private val newViewModel by viewModels<NewsViewModel> {
        NodeViewModeFactory(
            (requireContext().applicationContext as NoteApp).noteSource,
            this
        )
    }

    private lateinit var newsBinding:FragmentNewsBinding

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsBinding = FragmentNewsBinding.inflate(inflater,container,false).apply {
            newsViewModel = newViewModel
        }
        return newsBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        newsBinding.lifecycleOwner = this.viewLifecycleOwner
        newsBinding.rvNewsList.apply {
            newsAdapter = NewsAdapter(newViewModel)
            newsAdapter.setItemClickListener(object:OnItemClickListener{
                override fun onItemClick(holderK: ViewHolderK, position: Int) {
                    L.i("---")
                }

                override fun onItemLongClick(holderK: ViewHolderK, position: Int) {
                    L.i("---")
                }

            })
            adapter = newsAdapter
            addItemDecoration(QuickItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }
}