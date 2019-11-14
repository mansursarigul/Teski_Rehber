package com.example.teski_rehber.teskirehber;

/**
 * Created by Mitch on 2016-05-13.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.teski_rehber.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 2016-05-06.
 */
public class ListAdapter extends ArrayAdapter<ListAdapterUser> {

    private LayoutInflater mInflater;
    private ArrayList<ListAdapterUser> listAdapterUsers;
    private int mViewResourceId;


    public ListAdapter(Context context, int textViewResourceId, ArrayList<ListAdapterUser> listAdapterUsers) {
        super(context, textViewResourceId, listAdapterUsers);
        this.listAdapterUsers = listAdapterUsers;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        ListAdapterUser listAdapterUser = listAdapterUsers.get(position);

        if (listAdapterUser != null) {
            TextView xAdSoyad = (TextView) convertView.findViewById(R.id.AdSoyad);
            TextView xUnvan = (TextView) convertView.findViewById(R.id.Unvan);
            TextView xMobil = (TextView) convertView.findViewById(R.id.Mobil);
            TextView xDaire = (TextView) convertView.findViewById(R.id.Daire);
            TextView xDID = (TextView) convertView.findViewById(R.id.DID);
            if (xAdSoyad != null) {
                xAdSoyad.setText(listAdapterUser.getAdSoyad());
            }
            if (xUnvan != null) {
                xUnvan.setText((listAdapterUser.getUnvan()));
            }
            if (xMobil != null) {
                xMobil.setText((listAdapterUser.getMobil()));
            }
            if (xDaire != null) {
                xDaire.setText((listAdapterUser.getDaire()));
            }
            if (xDID != null) {
                xDID.setText((listAdapterUser.getDID()));
            }
        }
/*
        ImageButton btns = (ImageButton) convertView.findViewById(R.id.buttonDial);

        btns.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView lastName = (TextView) v.findViewById(R.id.Mobil);
                String s = lastName.getText().toString();
                Log.i("dikkat", s);

            }
        });

*/
        return convertView;

    }


}