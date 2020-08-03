package com.codepath.samikshya.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText etitem;
    RecyclerView rvitems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnAdd = findViewById(R.id.btnAdd);
        etitem = findViewById(R.id.etitem);
        rvitems = findViewById(R.id.rvitems);



        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //delete th item from the model
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                makeText(getApplicationContext(),"Item was removed", LENGTH_SHORT).show();
                saveItems();

            }
        };


         itemsAdapter = new ItemsAdapter(items,onLongClickListener);
         rvitems.setAdapter(itemsAdapter);
         rvitems.setLayoutManager(new LinearLayoutManager(this));
         btnAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
              String todoItem = etitem.getText().toString();
              items.add(todoItem);
              itemsAdapter.notifyItemInserted(items.size()-1);
              etitem.setText("");
              makeText(getApplicationContext(),"Items was added", LENGTH_SHORT).show();
              saveItems();
             }
         });
    }
    private File getDataFile() {
        return new File(getFilesDir(),"data.txt");

    }
    private void loadItems(){
       try {
        items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
    } catch(IOException e) {
           Log.e("MainActivity", "Error reading Item",e);
           items = new ArrayList<>();
       }
    }
    private void saveItems (){
        try{
            FileUtils.writeLines(getDataFile(),items);
        } catch(IOException e) {
            Log.e("MainActivity","Error writing Item",e);

        }
    }
}