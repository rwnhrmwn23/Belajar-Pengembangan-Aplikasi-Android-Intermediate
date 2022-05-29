package com.onedev.storyapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onedev.storyapp.R
import com.onedev.storyapp.databinding.ActivityAuthBinding
import com.onedev.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}