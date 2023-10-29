package com.example.recipeinstructions.ui.news.rss

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeinstructions.R
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.FragmentInstructionsBinding
import com.example.recipeinstructions.databinding.FragmentNewsBinding
import com.example.recipeinstructions.local.Preference
import com.example.recipeinstructions.model.RssItemModel
import com.example.recipeinstructions.model.User
import com.example.recipeinstructions.ui.MainApp
import com.example.recipeinstructions.ui.beverage.FragmentBeverage
import com.example.recipeinstructions.ui.inapp.PurchaseInAppActivity
import com.example.recipeinstructions.ui.inapp.inappnews.InAppPurchaseActivityNews
import com.example.recipeinstructions.ui.news.webview.WebViewActivity
import com.example.recipeinstructions.utils.DataController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    companion object {
        fun newInstance() = NewsFragment()

        fun getInputStream(url: URL): InputStream? {
            return try {
                url.openConnection().getInputStream()
            } catch (e: IOException) {
                null
            }
        }

        var urlData = ""

        @SuppressLint("StaticFieldLeak")
        var adapter: DataNewsAdapter? = null
        val listData = arrayListOf<RssItemModel>()
        val dataList = arrayListOf<RssItemModel>()
    }

    private lateinit var binding: FragmentNewsBinding

    override fun initView(view: View) {
        binding = FragmentNewsBinding.bind(view)
        actionView()
    }

    private fun actionView() {
        getData()
        getUrlData()
        adapter = DataNewsAdapter(requireActivity()) { link ->
            MainApp.newInstanceNews()?.preferenceNews?.apply {
                if (getValueCoinNews() >= 1) {
                    val dialog = Dialog(requireActivity())
                    dialog.setContentView(R.layout.custom_dialog_noti)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.findViewById<Button>(R.id.btnCancel)?.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.findViewById<Button>(R.id.btnOk)?.setOnClickListener {
                        dialog.dismiss()
                        setValueCoinNews(
                            getValueCoinNews().minus(1) ?: 0
                        )
                        val intent = Intent(requireActivity(), WebViewActivity::class.java)
                        updateGold()
                        intent.putExtra(
                            "linkNews",
                            dataList[(adapter?.getListItem()?.indexOf(link)
                                ?: return@setOnClickListener)].link
                        )
                        getData()
                        startActivity(intent)
                    }
                    dialog.show()
                } else {
                    val alertDialog = AlertDialog.Builder(requireActivity())
                    alertDialog.setTitle("You don't have enough gold ")
                        .setMessage("Open shop to buy more gold")
                        .setPositiveButton("Yes") { _, _ ->
                            addFragment(
                                R.id.fragment_layout,
                                InAppPurchaseActivityNews(),
                                InAppPurchaseActivityNews::class.java.simpleName
                            )
                        }
                    alertDialog.setNegativeButton("Cancel") { _, _ ->
                    }
                    alertDialog.show()
                }
            }
        }
        binding.listsNews.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.listsNews.adapter = adapter


        binding.icBack.setOnClickListener {
            addFragment(
                R.id.fragment_layout,
                FragmentBeverage.newInstance(),
                FragmentBeverage::class.java.simpleName
            )
        }

        binding.tvCoin.setOnClickListener {
            addFragment(
                R.id.fragment_layout,
                InAppPurchaseActivityNews(),
                InAppPurchaseActivityNews::class.java.simpleName
            )
        }
    }

    override fun getBinding(): FragmentNewsBinding {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        return binding
    }

    private fun setDataBaseGold() {
        val dataController = DataController(MainApp.newInstanceNews()?.deviceIdNews ?: "")
        dataController.writeNewUser(MainApp.newInstanceNews()?.deviceIdNews ?: "", 20)
    }

    fun getData() {
        val dataController = DataController(MainApp.newInstanceNews()?.deviceIdNews ?: "")
        dataController.setOnListenerFirebase(object : DataController.OnListenerFirebase {
            override fun onCompleteGetUser(user: User?) {
                user?.let {
                    MainApp.newInstanceNews()?.preference?.setValueCoinNews(user.coin)
                } ?: kotlin.run {
                    setDataBaseGold()
                }
                binding.tvCoin.text = String.format(
                    resources.getString(R.string.amount_gold),
                    MainApp.newInstanceNews()?.preference?.getValueCoinNews()
                )

            }

            override fun onSuccess() {

            }

            override fun onFailure() {
                Toast.makeText(
                    this@NewsFragment.requireContext(),
                    "Có lỗi kết nối đến server!",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        dataController.user
    }

    private fun updateGold() {
        val dataController = DataController(MainApp.newInstanceNews()?.deviceIdNews ?: "")
        dataController.updateDocument(MainApp.newInstanceNews()?.preference?.getValueCoinNews() ?: 0)
    }

    private fun getUrlData() {
        val newRef = FirebaseDatabase.getInstance().reference.child("news")
        newRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                urlData = snapshot.value.toString()
                ProcessInBackground(urlData).execute()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    class ProcessInBackground(val url: String) : AsyncTask<Int, Void, java.lang.Exception>() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Int?): java.lang.Exception? {
            var e: Exception? = null
            var title: String? = null
            var link: String? = null
            try {
                val url = URL(url)
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = false
                val xpp = factory.newPullParser()
                xpp.setInput(getInputStream(url), "UTF_8")

                var insideItem = false
                var eventType: Int = xpp.eventType

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.name.equals("item", ignoreCase = true)) {
                            insideItem = true
                        } else if (xpp.name.equals("title", ignoreCase = true)) {
                            if (insideItem) {
                                title = xpp.nextText()
                            }
                        } else if (xpp.name.equals("link", ignoreCase = true)) {
                            link = xpp.nextText()
                            listData.add(RssItemModel(title, link))
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.name.equals(
                            "item",
                            ignoreCase = true
                        )
                    ) {
                        insideItem = false
                    }
                    eventType = xpp.next()
                }
            } catch (m: MalformedURLException) {
                e = m
            } catch (m: XmlPullParserException) {
                e = m
            } catch (m: IOException) {
                e = m
            }

            return e
        }

        override fun onPostExecute(result: java.lang.Exception?) {
            super.onPostExecute(result)
            for (x in listData) {
                if (x.title != null) {
                    dataList.add(x)
                }
            }
            adapter?.setData(dataList)
        }
    }
}