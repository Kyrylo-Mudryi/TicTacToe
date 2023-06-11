package com.pewenko.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startNewGameButton: Button
    private lateinit var startNewGameButton2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startNewGameButton = findViewById(R.id.startNewGameButton)
        startNewGameButton2 = findViewById(R.id.startNewGameButton2)

        startNewGameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        startNewGameButton2.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("IsFriendPlaying", true)
            startActivity(intent)
        }
    }

    fun openMainActivity(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}
