package pk.encodersolutions.myrecipebook

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import pk.encodersolutions.myrecipebook.myrecipe.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var searchResultList: RecyclerView
    private lateinit var tvDesc: TextView
    private var searchView: SearchView? = null
    private var progressBar: ProgressBar? = null

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        requestPermissions()
    }

    private fun initViews() {
        progressBar = findViewById(R.id.progressBar)
        searchResultList = findViewById(R.id.listView)
        tvDesc = findViewById(R.id.tvDesc)
        searchResultList.layoutManager = LinearLayoutManager(this)
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), 2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchBar = menu.findItem(R.id.menuSearch)

        searchView = searchBar.actionView as SearchView

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                var query = query
                progressBar!!.visibility = View.VISIBLE
                if (isNetworkAvailable) {
                    tvDesc.visibility = View.INVISIBLE
                    searchResultList.visibility = View.INVISIBLE
                    val requestQueue = Volley.newRequestQueue(this@MainActivity)
                    query = query.replace("\\s+".toRegex(), "%20")

                    val temp = query.substring(0, 1).toUpperCase() + query.substring(1)
                    val actionBarTitle = temp.replace("%20".toRegex(), "\\ ")
                    val API_URL = "http://www.recipepuppy.com/api/?q="
                    val jsonURL = API_URL + query

                    val objectRequest = JsonObjectRequest(com.android.volley.Request.Method.GET, jsonURL, null, Response.Listener { response ->
                        try {

                            val recipeModelList = ArrayList<RecipeModel>()

                            val jsonArray = response.getJSONArray("results")

                            if (jsonArray.length() > 0) {
                                for (i in 0 until jsonArray.length()) {

                                    val childJSONObject = jsonArray.getJSONObject(i)

                                    val recipeModel = RecipeModel()

                                    recipeModel.title = childJSONObject.getString("title")
                                    recipeModel.ingredients = childJSONObject.getString("ingredients")
                                    recipeModel.url = childJSONObject.getString("href")
                                    recipeModel.poster = childJSONObject.getString("thumbnail")


                                    recipeModelList.add(recipeModel)

                                    val recipeListAdapter = RecipeListAdapter(this@MainActivity, recipeModelList)
                                    searchResultList.adapter = recipeListAdapter
                                    searchResultList.visibility = View.VISIBLE
                                    supportActionBar!!.setTitle(actionBarTitle + " Recipes")
                                    searchView!!.isIconified = true
                                    progressBar!!.visibility = View.GONE
                                }
                            } else {
                                tvDesc.visibility = View.VISIBLE
                                tvDesc.text = "No Recipe Found"
                                progressBar!!.visibility = View.GONE

                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { error ->
                        Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
                        progressBar!!.visibility = View.GONE
                    })
                    requestQueue.add(objectRequest)
                } else {
                    Toast.makeText(this@MainActivity, "Internet is required!", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE

                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)

    }
}
