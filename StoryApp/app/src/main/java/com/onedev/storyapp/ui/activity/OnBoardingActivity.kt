package com.onedev.storyapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.onedev.storyapp.databinding.ActivityOnBoardingBinding
import com.onedev.storyapp.utils.Constant.USER_TOKEN
import com.onedev.storyapp.utils.getPreference
import com.onedev.storyapp.utils.putPreference

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = getPreference(this, USER_TOKEN)
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                if (token.isNotEmpty()) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, AuthActivity::class.java))
                }
                finish()
            }, 100)
        }
    }
}