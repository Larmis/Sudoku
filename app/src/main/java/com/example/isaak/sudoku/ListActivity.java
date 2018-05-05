package com.example.isaak.sudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ListActivity extends Activity {

    ListView mListView;
    private ArrayList<String> grille = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mListView = (ListView) findViewById(R.id.listView);

        mListView.setAdapter(null);
        grille.clear();
        Intent intent = getIntent();
        int level= intent.getIntExtra("level", 1);

        Random rand = new Random();

        ArrayList<Item> ItemArray = new ArrayList<>();
        final ArrayList<String> listGrid = new ArrayList<>();
        try {
            InputStream data;
            if(level == 1){
                data = getResources().openRawResource(R.raw.lvl0);
            } else {
                data = getResources().openRawResource(R.raw.lvl1);
            }
            BufferedReader file = new BufferedReader(new InputStreamReader(data));
            int tmp = 0;
            String line = "";
            while ((line = file.readLine()) != null){
                tmp ++;
                int percent = rand.nextInt(99) + 1;
                ItemArray.add(new Item("Grille " + tmp, level, percent));
                listGrid.add(line);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




        GridAdapter adapter = new GridAdapter(ListActivity.this, ItemArray);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListActivity.this, GameActivity.class);
                intent.putExtra("grid", listGrid.get(i));

                startActivity(intent);
            }
        });

    }

    public class GridAdapter extends ArrayAdapter<Item> {

        public GridAdapter(Context context, List<Item> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.affichage,parent, false);
            }

            ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
            if(viewHolder == null){
                viewHolder = new ItemViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.level = (TextView) convertView.findViewById(R.id.level);
                viewHolder.percent = (TextView) convertView.findViewById(R.id.percent);
                convertView.setTag(viewHolder);
            }

            Item item = getItem(position);

            viewHolder.name.setText(item.getName());
            viewHolder.level.setText("Niveau " + String.valueOf(item.getLevel()).toString());
            if(item.getPercent()<50){
                viewHolder.percent.setTextColor(Color.RED);
            } else {
                viewHolder.percent.setTextColor(Color.GREEN);
            }
            viewHolder.percent.setText(item.getPercent()+"%");
            Context tx = getContext();
            return convertView;
        }

        private class ItemViewHolder {
            public TextView name;
            public TextView level;
            public TextView percent;

        }
    }

}


