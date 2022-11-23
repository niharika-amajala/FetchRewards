package com.example.fetchrewards;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ArrayList<JsonItem> jsonArrayList;
    private ArrayList<String> listIdArray;
    private String jsonString;
    private Spinner spinner;
    private JSONObject jsonObject;
    private RecyclerView jsonRecyclerView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                populateView();
            } catch (IllegalArgumentException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        spinner = findViewById(R.id.spinner_ids);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listIdArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String listid = (String) adapterView.getItemAtPosition(i);
                displayByGroupId(listid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter = new ListAdapter(jsonArrayList, this);
        jsonRecyclerView = findViewById(R.id.json_recycler_view);
        jsonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jsonRecyclerView.setAdapter(adapter);


    }

    private void displayByGroupId(String listid) {
        Log.d("Fetch Rewards", listid);
        if (!("None").equals(listid)) {
            ArrayList<JsonItem> displayList = jsonArrayList.stream().filter(item -> item.getListId() == Integer.parseInt(listid)).collect(Collectors.toCollection(ArrayList::new));
            adapter = new ListAdapter(displayList, this);
            jsonRecyclerView.setAdapter(adapter);
        } else {
            adapter = new ListAdapter(jsonArrayList, this);
            jsonRecyclerView.setAdapter(adapter);
        }

    }


    private void populateView() throws IllegalArgumentException {
        try {
            jsonArrayList = new ArrayList<>();
            listIdArray = new ArrayList<>();
            readJSON(new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json"));
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Malformed URL");
        }
    }

    public void readJSON(URL url) throws IllegalArgumentException {
        try (InputStream input = url.openStream()) {
            InputStreamReader inStreamReader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inStreamReader);
            StringBuilder json = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                json.append((char) character);
            }
            jsonString = json.toString();
            Log.d("FetchRewards", jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            listIdArray.add("None");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                int id = (int) explrObject.get("id");
                int listId = (int) explrObject.get("listId");
                String name = explrObject.get("name").toString();
                //Log.d("Fetch Rewards", item.toString());
                if (name != null && !"".equals(name) && !"null".equals(name)) {
                    jsonArrayList.add(new JsonItem(id, name, listId));
                    if (!listIdArray.contains(String.valueOf(listId))) {
                        listIdArray.add(String.valueOf(listId));

                    }
                }
            }

            Collections.sort(listIdArray);
            jsonArrayList.sort((item1, item2) -> {
                int value = item1.getListId() - item2.getListId();
                return value == 0 ? item1.getName().compareTo(item2.getName()) : value;
            });

        } catch (IOException | JSONException e) {
            throw new IllegalArgumentException(" JSON Parse failed");
        }

    }

}