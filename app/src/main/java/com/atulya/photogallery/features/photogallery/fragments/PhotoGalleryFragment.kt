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
import androidx.work.*
import com.atulya.photogallery.R
import com.atulya.photogallery.core.utils.POLL_WORK
import com.atulya.photogallery.core.worker.PollWorker
import com.atulya.photogallery.databinding.FragmentPhotoGalleryBinding
import com.atulya.photogallery.features.photogallery.recyclerView.PhotoListAdapter
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

const val TAG = "#> PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment(), MenuProvider {

    private val viewModel: PhotoGalleryViewModel by viewModels()

    private var searchView: SearchView? = null
    private var pollButton: MenuItem? = null

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is not initialized. Check is the view is visible"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
                viewModel.uiState.collect { uiState ->
                    binding.photoGrid.adapter = PhotoListAdapter(uiState.images)
                    searchView?.setQuery(uiState.query, false)
                    updatePollingState(uiState.isPolling)
                    binding.progressBar.visibility = View.INVISIBLE
                    Log.d(TAG, "onViewCreated: $uiState")
                }
            }
        }
    }


    // App bar menu functions --- START
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu)

        pollButton = menu.findItem(R.id.polling_button)

        // Adding listener to search view
        val searchItem = menu.findItem(R.id.search_view)
        searchView = searchItem.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query ?: "")
                Log.d(TAG, "Query: $query")

                // Dismisses virtual keyboard
                searchView?.clearFocus()

                // Showing progress bar
                binding.progressBar.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Text changed: $newText")
                return true
            }

        })

    }


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
        R.id.search_view -> {
            true
        }
        R.id.clear_search -> {
            viewModel.setQuery("")
            true
        }
        R.id.polling_button -> {
            viewModel.toggleIsPolling()
            true
        }
        else -> false
    }
    // App bar menu functions --- END

    private fun updatePollingState(isPolling: Boolean) {
        val pollButtonTitle = if(isPolling){
            getString(R.string.start_polling)
        }
        else {
            getString(R.string.stop_polling)
        }

        pollButton?.title = pollButtonTitle

        if (isPolling){
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val periodicWorkRequest = PeriodicWorkRequestBuilder<PollWorker>(
                15, // 15 min is that least interval allowed by android
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP, //keep old, discard new
                periodicWorkRequest
            )
        }
        else {
            WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
        }
    }

    override fun onDestroyView() {
        _binding = null
        searchView = null
        pollButton = null
        super.onDestroyView()
    }
}