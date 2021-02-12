package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*shareBtn.setOnClickListener {
            Toast.makeText(this, "share", Toast.LENGTH_SHORT).show()
        }*/

        loadMeme()
    }
    private fun loadMeme(){
        // Instantiate the RequestQueue.
        progBar.visibility = View.VISIBLE
        val link = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, link, null,
            { response ->
                currentUrl = response.getString("url")
                Glide.with(this).load(currentUrl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImgView)
            },
            {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        "text/plain".also { intent.type = it }
        intent.putExtra(Intent.EXTRA_TEXT, "Hey! Checkout this hilarious meme, URL: $currentUrl")
        val chooser = Intent.createChooser(intent, "Share using ...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}