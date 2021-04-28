package com.xter.slimnotek.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xter.slimnotek.R
import com.xter.slimnotek.data.net.RetrofitHelper
import com.xter.slimnotek.extension.Constant
import com.xter.slimnotek.util.L
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(this, Observer {
            textView.text = it
        })
        testData()
        return root
    }

    fun testData(){
        GlobalScope.launch {
            RetrofitHelper.INSTANCE.getJuHe().getNews("top",1,5,appKey = Constant.KEY_JUHE_NEWS).subscribe({ response->
                L.d("news size = ${response.result.data.size}")
            })
        }

    }
}