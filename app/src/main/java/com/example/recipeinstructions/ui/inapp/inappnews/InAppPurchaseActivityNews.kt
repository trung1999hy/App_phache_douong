package com.example.recipeinstructions.ui.inapp.inappnews

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.example.recipeinstructions.databinding.ActivityInAppPurchaseBinding
import com.example.recipeinstructions.model.User
import com.example.recipeinstructions.ui.MainApp
import com.example.recipeinstructions.utils.Constants
import com.example.recipeinstructions.utils.DataController
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList

class InAppPurchaseActivityNews : Fragment(), InAppPurchaseAdapterNews.OnClickListener {
    private lateinit var binding: ActivityInAppPurchaseBinding
    private var adapter: InAppPurchaseAdapterNews? = null
    private var billingClient: BillingClient? = null
    private var handler: Handler? = null
    private var productDetailsList: MutableList<ProductDetails>? = null
    private var onPurchaseResponse: OnPurchaseResponse? = null
    private var listData: RecyclerView? = null
    private var imgBack: ImageView? = null
    private var layout: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityInAppPurchaseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        billingClient!!.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(ProductType.INAPP).build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        verifyInAppPurchase(purchase)
                    }
                }
            }
        }
    }

    private fun initViews() {
        listData = binding.listData
        layout = binding.LllNoData
        adapter =
            InAppPurchaseAdapterNews()
        listData?.setHasFixedSize(true)
        listData?.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        listData?.setAdapter(adapter)
        adapter!!.setOnClickListener(this)
        productDetailsList = ArrayList()
        handler = Handler()
        billingClient = BillingClient.newBuilder(requireActivity()).enablePendingPurchases()
            .setListener { billingResult: BillingResult?, list: List<Purchase?>? -> }
            .build()
        establishConnection()
    }

    fun establishConnection() {
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    showProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                establishConnection()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun showProducts() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(inAppProductList)
            .build()
        billingClient!!.queryProductDetailsAsync(
            params
        ) { billingResult: BillingResult?, prodDetailsList: List<ProductDetails> ->
            // Process the result
            productDetailsList!!.clear()
            handler!!.postDelayed({

//                        hideProgressDialog();
                productDetailsList!!.addAll(prodDetailsList)
                adapter?.setData(requireActivity(), productDetailsList)
                if (prodDetailsList.isEmpty()) {
                    layout?.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "prodDetailsList, size = 0",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    layout?.visibility = View.GONE
                }
            }, 2000)
        }
    }

    //Product 1
    //Product 2
    //Product 3
    //Product 4
    //Product 5
    //Product 6
    private val inAppProductList: ImmutableList<Product>
        private get() = ImmutableList.of(
            //Product 1
            Product.newBuilder()
                .setProductId(Constants.KEY_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),
            Product.newBuilder()
                .setProductId(Constants.KEY_10_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),  //Product 2
            Product.newBuilder()
                .setProductId(Constants.KEY_20_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),  //Product 3
            Product.newBuilder()
                .setProductId(Constants.KEY_50_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),  //Product 4
            Product.newBuilder()
                .setProductId(Constants.KEY_100_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),  //Product 5
            Product.newBuilder()
                .setProductId(Constants.KEY_150_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),  //Product 6
            Product.newBuilder()
                .setProductId(Constants.KEY_200_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),
            Product.newBuilder()
                .setProductId(Constants.KEY_500_USD_NEWS)
                .setProductType(ProductType.INAPP)
                .build(),
        )

    fun verifyInAppPurchase(purchases: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()
        billingClient!!.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val proId = purchases.products[0]
                val quantity = purchases.quantity
                setPurchaseResponse(object : OnPurchaseResponse {
                    override fun onResponse(proId: String?, quantity: Int) {
                        proId?.let {
                            setupResult(
                                it,
                                quantity
                            )
                        }
                    }
                })
                onPurchaseResponse!!.onResponse(proId, quantity)
                allowMultiplePurchases(purchases)
//                val coinContain =
//                    MainApp.newInstance()?.preference?.getValueCoin()?.plus(getCoinFromKey(proId))
//                coinContain?.let { MainApp.newInstance()?.preference?.setValueCoin(it) }
//                //                Toast.makeText(PurchaseInAppActivity.this, "verifyInAppPurchase Mua ok--> " + proId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun allowMultiplePurchases(purchase: Purchase) {
        val consumeParams = ConsumeParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient!!.consumeAsync(consumeParams) { billingResult, s ->
            Toast.makeText(
                requireContext(),
                " Resume item ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClickItem(item: ProductDetails) {
        launchPurchaseFlow(item)
    }

    private fun launchPurchaseFlow(productDetails: ProductDetails) {
        // handle item select
//        assert productDetails.getSubscriptionOfferDetails() != null;
        val productDetailsParamsList = ImmutableList.of(
            ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient!!.launchBillingFlow(requireActivity(), billingFlowParams)
    }

    private fun setupResult(proId: String, quantity: Int) {
        val totalCoin = MainApp.newInstanceNews()?.preference?.getValueCoinNews() ?: 0
        val remainCoin = totalCoin + getCoinFromKey(proId) * quantity;
        MainApp.newInstanceNews()?.preference?.setValueCoinNews(remainCoin);

        val dataController = DataController(MainApp.newInstanceNews()?.deviceIdNews ?: "")
        dataController.setOnListenerFirebase(object : DataController.OnListenerFirebase {
            override fun onCompleteGetUser(user: User?) {
            }

            override fun onSuccess() {
                Toast.makeText(
                    this@InAppPurchaseActivityNews.requireContext(),
                    "Xin chúc mừng, bạn đã mua gold thành công!",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure() {
                Toast.makeText(
                    this@InAppPurchaseActivityNews.requireContext(),
                    "Có lỗi kết nối đến server!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        dataController.updateDocument(totalCoin)
    }

    private fun getCoinFromKey(coinId: String): Int {
        return when (coinId) {
            Constants.KEY_USD_NEWS -> 5
            Constants.KEY_10_USD_NEWS -> 10
            Constants.KEY_20_USD_NEWS -> 20
            Constants.KEY_50_USD_NEWS -> 50
            Constants.KEY_100_USD_NEWS -> 100
            Constants.KEY_150_USD_NEWS -> 150
            Constants.KEY_200_USD_NEWS -> 200
            Constants.KEY_500_USD_NEWS -> 500
            else -> 0
        }
    }

    internal interface OnPurchaseResponse {
        fun onResponse(proId: String?, quantity: Int)
    }

    private fun setPurchaseResponse(onPurchaseResponse: OnPurchaseResponse) {
        this.onPurchaseResponse = onPurchaseResponse
    }
}