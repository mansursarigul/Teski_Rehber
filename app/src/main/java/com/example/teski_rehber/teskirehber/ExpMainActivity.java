
package com.example.teski_rehber.teskirehber;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.androidtutorialshub.expandablelistview.adapter.ExpandableListViewAdapter;
import com.example.teski_rehber.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpMainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;

    private ExpandableListAdapter expandableListAdapter;

    private List<String> listDataGroup;

    private HashMap<String, List<AdSoyadMobil>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exp_activity_main);

        // initializing the views
        initViews();

        // initializing the listeners
        initListeners();

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();

    }


    /**
     * method to initialize the views
     */
    private void initViews() {

        expandableListView = findViewById(R.id.expandableListView);

    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                TextView textViewChild2 = (TextView) v.findViewById(R.id.textViewChild2);

                Toast.makeText(
                        getApplicationContext(),
                        listDataGroup.get(groupPosition)
                                + " : " + textViewChild2.getText().toString() , Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // ExpandableListView Group expanded listener genişletmek
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener daraltmak
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
     /**
     * method to initialize the objects
     */
    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();

        // initializing the adapter object
        expandableListAdapter = new ExpandableListAdapter(this, listDataGroup, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(expandableListAdapter);

    }

    /*
     * Preparing the list data
     *
     * Dummy Items
     */
    private void initListData() {
        DatabaseAdapter myDB2;

        myDB2 = new DatabaseAdapter(this);

        List<AdSoyadMobil> lists[]=new ArrayList[45];

        // DAİRELERİ LİSTELE *******************************************************
        Cursor data = myDB2.getListDaireler();
        int numRows = data.getCount();
        if(numRows > 0){
            int i=0;
            while(i<numRows){
                lists[i]=new ArrayList<AdSoyadMobil>();
                listDataGroup.add(data.getString(0).trim()); //Daire adına kategori aç
                // DAİRELERE GÖRE KİŞİLERİ LİSTELE *******************************************************
                Cursor data2 = myDB2.getListDairelerinKisileri(listDataGroup.get(i));
                data2.moveToFirst();
                int numRows2 = data2.getCount();
                if (numRows2 > 0) {
                    int si = 0;
                    while (si<numRows2) {
                        AdSoyadMobil adsoyadmobil = new AdSoyadMobil(data2.getString(0).trim(),data2.getString(2).trim());
                        lists[i].add(adsoyadmobil); // ad soyadı ve mobili arrayliste ekle
                        data2.moveToNext();
                        si++;
                    }
                    data2.close();
                }
                //Log.i("dikkat",String.valueOf(i));
                //Log.i("dikkat",String.valueOf(listDataGroup.get(i))+ "......." + String.valueOf(lists[i]));
//                listDataChild.put(String.valueOf(listDataGroup.get(i)), lists[i]); //expviewliste daire adı, adsoyad arraylarını put et
//                Log.i("dikkat",String.valueOf(i)+ "......." + String.valueOf(listDataChild));
                i++;
                data.moveToNext();
            }
            data.close();
            for(int ArrayPut=0;ArrayPut<numRows;ArrayPut++){
                listDataChild.put(listDataGroup.get(ArrayPut), lists[ArrayPut]); //expviewliste daire adı, adsoyad arraylarını put et
                Log.i("dikkat",String.valueOf(ArrayPut)+ "......." + String.valueOf(listDataChild));

            }
        }

        // Adding child data
        //listDataChild.put(listDataGroup.get(0), pc0);

        //listDataChild.put(listDataGroup.get(2), pc2);
//        listDataChild.put(listDataGroup.get(3), pc3);


        // notify the adapter
        expandableListAdapter.notifyDataSetChanged();
    }


}