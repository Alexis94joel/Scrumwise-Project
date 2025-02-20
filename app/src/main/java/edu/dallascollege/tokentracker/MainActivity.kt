package edu.dallascollege.tokentracker

import TokenTracker
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tokenTracker: TokenTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the token tracker
        tokenTracker = TokenTracker()

        // Example: Move the token by 5 spaces
        val newPosition = tokenTracker.moveToken(5)
        println("Token moved to position: $newPosition")

        // Example: Get the current location of the token
        val currentLocation = tokenTracker.getCurrentLocation()
        println("Token is currently at position: $currentLocation")
    }
}