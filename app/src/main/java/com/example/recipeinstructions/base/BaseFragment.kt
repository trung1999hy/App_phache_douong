package com.example.recipeinstructions.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.recipeinstructions.R

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var mBinding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getBinding()
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    protected abstract fun initView(view: View)
    abstract fun getBinding(): T

    open fun openActivity(destinationClass: Class<*>) {
        startActivity(Intent(activity, destinationClass))
//        activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
    }

    open fun addFragment(id: Int, fragment: Fragment, tag: String, backstack: String? = null) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(id, fragment, tag)
            ?.commit()
    }

    open fun replaceFragment(id: Int, fragment: Fragment, backstack: String? = null) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.dim_in, R.anim.dim_out)
            ?.replace(id, fragment, fragment.tag)
            ?.addToBackStack(backstack)
            ?.commit()
    }

//    open fun createCustomToast(image: Int, message: String) {
//        val toast = Toast(requireActivity())
//        toast.apply {
//            val mBinding = ToastCustomBinding.inflate(layoutInflater)
//            mBinding.tvMessageCustomToast.text = message
//            mBinding.imgWarningToast.setImageResource(image)
//            duration = Toast.LENGTH_SHORT
//            view = mBinding.root
//            show()
//        }
//    }
}