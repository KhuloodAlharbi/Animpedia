package com.example.animapedia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Invertebrates extends AppCompatActivity {
    EditText searchBar;
    RecyclerView recycler;
    ArrayList<AnimalModel> list = new ArrayList<>();
    AnimalAdapter adapter;
    ImageButton btnback;
    String URL = "https://api.inaturalist.org/v1/taxa?taxon_id=47115&rank=species&per_page=80";
    ArrayList<AnimalModel> fullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invertebrates);

        recycler = findViewById(R.id.InvertebratesRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AnimalAdapter(this, list);
        recycler.setAdapter(adapter);

        loadAnimals();
        btnback = findViewById(R.id.btnback);


        searchBar = findViewById(R.id.searchBar);

        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

    }

    private void loadAnimals() {


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        JSONObject root = new JSONObject(response);
                        JSONArray arr = root.getJSONArray("results");

                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject item = arr.getJSONObject(i);

                            String name = item.optString("preferred_common_name", "Unknown");
                            String scientificName = item.optString("name", "");
                            int id = item.optInt("id", 0);

                            String image = "";
                            JSONObject photo = item.optJSONObject("default_photo");
                            if (photo != null)
                                image = photo.optString("medium_url", "");

                            list.add(new AnimalModel(name, scientificName, image, id));
                            fullList.add(new AnimalModel(name, scientificName, image, id));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(this, "Parsing Error", Toast.LENGTH_SHORT).show();
                        Log.e("API_DEBUG", "Parsing exception: " + e.getMessage());
                    }
                },

                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);

    }

    private void filterList(String text) {
        list.clear();

        if (text.isEmpty()) {
            list.addAll(fullList);
        } else {
            text = text.toLowerCase();
            for (AnimalModel model : fullList) {
                if (model.getName().toLowerCase().contains(text) ||
                        model.getScientificName().toLowerCase().contains(text)) {
                    list.add(model);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
    public void goBack(View view) {
        finish();
    }

}