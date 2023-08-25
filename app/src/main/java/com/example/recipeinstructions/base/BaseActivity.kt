package com.example.recipeinstructions.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recipeinstructions.R
import com.example.recipeinstructions.databinding.ToastCustomBinding

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(setLayout())
    }

    abstract fun setLayout(): View

    abstract fun initView()

    open fun addFragment(id: Int, fragment: Fragment, tag: String, backstack: String? = null) {
        supportFragmentManager.beginTransaction()
            .add(id, fragment, tag)
            .commit()
    }

    open fun openActivity(destinationClass: Class<*>) {
        startActivity(Intent(this@BaseActivity, destinationClass))
//        overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
        finish()
    }

    open fun createCustomToast(message: String) {
        val toast = Toast(this)
        toast.apply {
            val mBinding = ToastCustomBinding.inflate(layoutInflater)
            mBinding.tvMessageCustomToast.text = message
            duration = Toast.LENGTH_SHORT
            view = mBinding.root
            show()
        }
    }

    open fun replaceFragment(id: Int, fragment: Fragment, tag: String, backstack: String? = null) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.dim_in, R.anim.dim_out)
            .replace(id, fragment, tag)
            .addToBackStack(backstack)
            .commit()
    }
}