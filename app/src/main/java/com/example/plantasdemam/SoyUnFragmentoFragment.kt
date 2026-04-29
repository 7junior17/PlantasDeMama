package com.example.plantasdemam

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plantas.R

class SoyUnFragmentoFragment : Fragment() {

    private var webView: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_soy_un_fragmento, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoId = arguments?.getString("videoId") ?: "jNQXAC9IVRw"
        val titulo = arguments?.getString("titulo") ?: "Video de la planta"

        view.findViewById<TextView>(R.id.txtTitulo).text = titulo

        webView = view.findViewById(R.id.webViewVideo)

        webView?.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36"
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            loadUrl("https://m.youtube.com/watch?v=$videoId")
        }
    }

    override fun onDestroyView() {
        webView?.destroy()
        webView = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(videoId: String, titulo: String): SoyUnFragmentoFragment {
            val fragment = SoyUnFragmentoFragment()
            val args = Bundle()
            args.putString("videoId", videoId)
            args.putString("titulo", titulo)
            fragment.arguments = args
            return fragment
        }
    }
}