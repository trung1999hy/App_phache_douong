package com.example.recipeinstructions

import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.example.recipeinstructions.base.BaseActivity
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.ActivityMainBinding
import com.example.recipeinstructions.ui.beverage.FragmentBeverage
import com.example.recipeinstructions.ui.category.FragmentCategory
import com.example.recipeinstructions.ui.favorite.FragmentFavorite
import com.example.recipeinstructions.ui.instructions.FragmentInstructions

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun setLayout(): View = binding.root
    private var isCheckClick = 0

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mFragment = supportFragmentManager.findFragmentById(R.id.fragment_layout)
        if (mFragment !is BaseFragment<*>) {
            addFragment(
                R.id.fragment_layout,
                FragmentBeverage.newInstance(),
                FragmentBeverage::class.java.simpleName
            )
        }
    }

    override fun onBackPressed() {
        val mFragment = supportFragmentManager.findFragmentById(R.id.fragment_layout)
        if (mFragment is FragmentBeverage) {
            isCheckClick++
            if (isCheckClick == 2) {
                finish()
            } else {
                Handler().postDelayed({
                    isCheckClick = 0
                }, 3000)
                createCustomToast("Nhấn quay lại lần nữa để thoát ứng dụng")
            }
        } else {
            super.onBackPressed()
        }

    }
}
