package com.atulya.photogallery.features.photopage.fragment

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.atulya.photogallery.databinding.FragmentPhotoPageBinding

class PhotoPageFragment: Fragment() {

    private val args: PhotoPageFragmentArgs by navArgs()

    private var _binding: FragmentPhotoPageBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Initialize _binding first"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoPageBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            progressBar.max = 100

            webView.apply {
                // Modern websites need javascript so we enable that
                settings.javaScriptEnabled = true

                /**
                 * We also need to set a webclient otherwise
                 * the [WebView] will ask activity manager for
                 * apps that can open the link.
                 *
                 * If we add a [WebViewClient] then the webview
                 * will ask it to load the URL
                 */
                webViewClient = WebViewClient()

                // At last after configuring everything we will load the URL
                loadUrl(args.photoPageUrl.toString())

                webChromeClient = object: WebChromeClient(){
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if(newProgress == 100) {
                            progressBar.visibility = View.GONE
                        }
                        else {
                            progressBar.visibility = View.VISIBLE
                            progressBar.progress = newProgress
                        }
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        val parent = requireActivity() as AppCompatActivity
                        parent.supportActionBar?.subtitle = title
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}