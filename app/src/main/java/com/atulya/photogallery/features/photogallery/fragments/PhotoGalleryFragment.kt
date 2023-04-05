package com.atulya.photogallery.features.photogallery.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.atulya.photogallery.R
import com.atulya.photogallery.databinding.FragmentPhotoGalleryBinding
import com.atulya.photogallery.features.photogallery.recyclerView.PhotoListAdapter
import kotlinx.coroutines.launch

const val TAG = "#> PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment(), MenuProvider {

    private val viewModel: PhotoGalleryViewModel by viewModels()

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

        // To add items in app bar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)

        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gallery.collect { photos ->
                    binding.photoGrid.adapter = PhotoListAdapter(photos)
                    Log.d(TAG, "onViewCreated: $photos")
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // App bar menu functions --- START
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu)

        // Adding listener to search view
        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query ?: "")
                Log.d(TAG, "Query: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Text changed: $newText")
                return true
            }

        })

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when(menuItem.itemId) {
        R.id.menu_item_search -> {
            true
        }
        R.id.menu_item_clear -> {
            viewModel.setQuery("")
            true
        }
        else -> false
    }
    // App bar menu functions --- END
}