package com.example.recycleviewtest

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recycleviewtest.data.Data
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MyAdapter.ViewHolder>? = null
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    var  login = ArrayList<String>()
    var  id = ArrayList<String>()
    var  url_image = ArrayList<String>()
    var  repositories = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readJson()

        val dstFolder = File(filesDir, "jsonFolder")

        try {
            if (!dstFolder.exists()) {
                if (!dstFolder.mkdirs()) {
                    Log.v("CREATE FOLDER", "Failed to create folder.")
                    return
                }
            }
        } catch (e: Exception) {
            Log.d("!!!!!!!!!!!!!!!!!!!!!!", e.message!!)
            return
        }

        swipeRefreshLayout = findViewById(R.id.refreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchUsers()
        }

        fetchUsers()
    }

    private fun readJson() {
        var json : String? = null

        try {
            val inputStream:InputStream = assets.open("users.json")
            json = inputStream.bufferedReader().use { it.readText() }

            var jsonList = JSONArray(json)

            for (i in 0..jsonList.length() - 1){
                var jsnObject = jsonList.getJSONObject(i)
                login.add(jsnObject.getString("login"))
                id.add(jsnObject.getString("id"))
                url_image.add(jsnObject.getString("avatar_url"))
                repositories.add(jsnObject.getString("html_url"))
            }

        }catch (e: IOException){
            Log.d("Read json fail", e.message!!)
        }


    }

    private fun fetchUsers(){
        if (checkForInternet(this)) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
        }
        swipeRefreshLayout.isRefreshing = true

        UsersApi().getUsers().enqueue(object : retrofit2.Callback<List<Data>>{
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                swipeRefreshLayout.isRefreshing = false

                val users = response.body()

                users?.let {
                    showUsers(it)
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG ).show()
                swipeRefreshLayout.isRefreshing = false
            }

        })
    }

    private fun showUsers(users: List<Data>){

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = MyAdapter(users, object : OnClickListener{
            override fun onClicked(html_url: String) {
                newActivity(html_url)
            }
        })
        recyclerView.adapter = adapter


    }

    private fun newActivity(repositoriesUrl : String){
        val activity = Intent (this, Profile_activity::class.java)
        activity.putExtra(Profile_activity.TOTAL_COUNT, repositoriesUrl)
        startActivity(activity)

    }
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}

