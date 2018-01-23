package com.uphero.sadda.myrecipe;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView searchResultList;
    TextView tvDesc;
    SearchView searchView = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 2);
            }
        }

        searchResultList = findViewById(R.id.listView);
        tvDesc = findViewById(R.id.tvDesc);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching");
        progressDialog.setCancelable(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchResultList.setLayoutManager(mLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        final MenuItem searchBar = menu.findItem(R.id.menuSearch);

        searchView = (SearchView) searchBar.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                progressDialog.show();
                if (isNetworkAvailable()) {
                    tvDesc.setVisibility(View.INVISIBLE);
                    searchResultList.setVisibility(View.INVISIBLE);
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    query = query.replaceAll("\\s+", "%20");

                    //converting first letter to upper case
                    String temp = query.substring(0, 1).toUpperCase() + query.substring(1);
                    final String actionBarTitle = temp.replaceAll("%20", "\\ ");
                    String API_URL = "http://www.recipepuppy.com/api/?q=";
                    String jsonURL = API_URL + query;

                    JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, jsonURL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                final List<RecipeModel> recipeModelList = new ArrayList<>();

                                JSONArray jsonArray = response.getJSONArray("results");

                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject childJSONObject = jsonArray.getJSONObject(i);

                                        RecipeModel recipeModel = new RecipeModel();

                                        recipeModel.setTitle(childJSONObject.getString("title"));
                                        recipeModel.setIngredients(childJSONObject.getString("ingredients"));
                                        recipeModel.setUrl(childJSONObject.getString("href"));
                                        recipeModel.setPoster(childJSONObject.getString("thumbnail"));


                                        recipeModelList.add(recipeModel);

                                        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(MainActivity.this, recipeModelList);
                                        searchResultList.setAdapter(recipeListAdapter);
                                        searchResultList.setVisibility(View.VISIBLE);
                                        getSupportActionBar().setTitle(actionBarTitle + " Recipes");
                                        searchView.setIconified(true);
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    tvDesc.setVisibility(View.VISIBLE);
                                    tvDesc.setText("No Recipe Found");
                                    progressDialog.dismiss();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                    requestQueue.add(objectRequest);
                } else {
                    Toast.makeText(MainActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
