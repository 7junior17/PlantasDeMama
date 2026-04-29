package com.example.plantasdemam

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.plantas.R

class Actividad2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad2)

        val videoId = intent.getStringExtra("videoId") ?: "dQw4w9WgXcQ"
        val titulo = intent.getStringExtra("titulo") ?: "Video de la Planta"

        findViewById<TextView>(R.id.text_titulo_actividad2).text = titulo

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    SoyUnFragmentoFragment.newInstance(videoId, titulo)
                )
                .commit()
        }

        findViewById<BottomNavigationView>(R.id.activity2_bottom_nav)
            .setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_regresar -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
    }
}