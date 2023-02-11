package com.atulya.photogallery.features.photogallery.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.atulya.photogallery.core.api.FlickerApi
import com.atulya.photogallery.databinding.FragmentPhotoGalleryBinding
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

const val TAG = "# PhotoGalleryFragment"
class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is not initialized. Check is the view is visible"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)

        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Retrofit's default response type is [okhttp3.ResponseBody],
         * To deserialize it into usable data we need to use
         * converter to change it into desired type.
         *
         * Ex: we can specify a converter such as
         * [ScalarsConverterFactory.create], which among other
         * type will let retrofit to deserialize data to string
         */

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        /**
         *  When we call `retrofit.create`, retrofit
         *  generate code using the interface we passed to it.
         *  It generates an Anonymous class containing
         *  all the methods we need.
         *
         *  This code generation happens at runtime.
         */
        val flickerApi = retrofit.create<FlickerApi>()

        viewLifecycleOwner.lifecycleScope.launch {
            val response = flickerApi.fetchContents()
            Log.d(TAG, "onViewCreated: $response")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}