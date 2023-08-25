package com.example.recipeinstructions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.view.View
import com.example.recipeinstructions.base.BaseActivity
import com.example.recipeinstructions.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun setLayout(): View = binding.root

    override fun initView() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 3000)
    }

}