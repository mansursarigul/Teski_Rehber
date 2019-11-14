package com.example.teski_rehber.teskirehber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import com.example.teski_rehber.BuildConfig;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.teski_rehber.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    DatabaseAdapter databaseadapter = new DatabaseAdapter(MainActivity.this);
/**
    LinearLayout lytSearch1 = (LinearLayout) findViewById(R.id.lytSearch);
    ImageButton imgBtn ;
 imgBtn = (ImageButton) findViewById(R.id.buttonFind);
*/
    DatabaseAdapter myDB;
    ArrayList<ListAdapterUser> listAdapterUserList;
    private ListView listView;
    ListAdapterUser listAdapterUser;

    private static final int REQUEST_CALL = 1;
    public String VersiyonXML;
    public String VersiyonMajor;
    public String VersiyonMinor;
    public int AppVerCode;
    public String AppVerName;
    public Integer VersiyonDB;
    public String XMLRehberURL;
    public String XMLVersiyonURL;
    public String ApkURL;

    private ImageButton imgBtnFind ;
    private ImageButton imgBtnFindCancel ;

    private ConstraintLayout lytSearch1;
    private Uri Download_Uri;

    private static final String APP_DIR = "/storage/emulated/0/data/rehber/";
    File toInstall = new File(APP_DIR, "app-release.apk");

    //private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        imgBtnFind = (ImageButton) findViewById(R.id.buttonFind);
        imgBtnFindCancel = (ImageButton) findViewById(R.id.buttonFindCancel);

        AppVerCode = BuildConfig.VERSION_CODE;
        AppVerName = BuildConfig.VERSION_NAME;
        setTitle("TESKİ Kurumsal Rehber v" + AppVerName  + "." +  AppVerCode);

            Log.i("dikkat",this.getPackageName());
            Log.i("dikkat","Call ParametreAl()")        ;
        ParametreAl();
            Log.i("dikkat","Call XMLLoad()")        ;
        XMLLoad();
            Log.i("dikkat","Call DBLoad()")        ;
        DBLoad(null);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                TextView tvMobil = (TextView) view.findViewById(R.id.Mobil);
                TextView tvDID = (TextView) view.findViewById(R.id.DID);
                String sMobil = tvMobil.getText().toString();
                String sDID = tvDID.getText().toString();
                /*
                Intent callIntent = new Intent("android.intent.action.DIAL");
                callIntent.setData(Uri.parse("tel:" + sMobil));
                MainActivity.this.startActivity(callIntent);

                 */
                alertSimpleListView(sMobil,sDID);
            }
        });
        //ImageButton imgBtn = (ImageButton) findViewById(R.id.buttonFind);
        imgBtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText findText = (EditText) findViewById(R.id.findText);
                String findTextStr = findText.getText().toString();
                DBLoad(findTextStr);
            }
        });

        imgBtnFindCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBLoad(null);
                lytSearch1.setVisibility(View.GONE);
            }
        });
    }
    private void XMLLoad(){
        lytSearch1 = (ConstraintLayout) findViewById(R.id.lytSearch);
        listView = (ListView) findViewById(R.id.user_list);

        try {
            URL url = new URL("http://rehber.teski.gov.tr/bidb/kisiler/rehber2.php");
            //InputStream istream = getAssets().open("userdetails.xml");
            url.openConnection().setReadTimeout(3);
            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(url.openStream()));
            //Document doc = docBuilder.parse(istream);
            doc.getDocumentElement().normalize();
            //ListView lv = (ListView) findViewById(R.id.user_list);
            Log.i("dikkat","XLM rehber listesi alınıyor");

            NodeList nListA = doc.getElementsByTagName("setting");
            for (int i = 0; i < nListA.getLength(); i++) {
                if (nListA.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) nListA.item(i);
                    VersiyonXML = getNodeValue("VersiyonNo", elm);
                }
           //     Log.i("dikkat", "xmlver:" + VersiyonXML);
            }
            try {
                VersiyonDB = databaseadapter.getVersiyonDB();
                //VersiyonDB = 0;

            }catch (Exception e){
                e.printStackTrace();

            }
            Log.i("dikkat", "dbver:" + VersiyonDB);
            // HER İKİ VERSİYON FARKLIYSA DB TEMİZLE YENİ XML YAZMAYA BAŞLA *************************************************************************
            if (VersiyonDB != Integer.parseInt(VersiyonXML)){
                databaseadapter.DeleteTable();
                NodeList nList = doc.getElementsByTagName("rootnode");
                for (int i = 0; i < nList.getLength(); i++) {
                    if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                        //HashMap<String, String> listAdapterUser = new HashMap<>();
                        Element elm = (Element) nList.item(i);
                            String AdDaire= getNodeValue("Daire", elm).trim();
                            String NoMobil= getNodeValue("Mobil", elm).trim();
                            String NoDID= getNodeValue("DID", elm).trim();
                                NoDID = NoDID.replace(" ","");
                        databaseadapter.VeriEkle(getNodeValue("AdSoyad", elm), getNodeValue("Unvan", elm), NoMobil, AdDaire,
                                            getNodeValue("Sube", elm), NoDID,1,null);
                        //Log.i("dikkat", getNodeValue("AdSoyad", elm));
                        /**
                         listAdapterUser.put("Id", getNodeValue("Id", elm));
                         listAdapterUser.put("AdSoyad", getNodeValue("AdSoyad", elm));
                         listAdapterUser.put("Daire", getNodeValue("Daire", elm));
                         listAdapterUser.put("Mobil", getNodeValue("Mobil", elm));
                         listAdapterUserList.add(listAdapterUser);
                         */
                    }
                }
                databaseadapter.VeriEkle(null, null, null, null,null,null,2,Integer.parseInt(VersiyonXML));
            }
        }  catch (IOException err01) {
            //err01.printStackTrace();
            Log.w( "dikkat",err01.getMessage());
        } catch (ParserConfigurationException err01) {
            Log.w( "dikkat",err01.getMessage());
        } catch (SAXException err01) {
            Log.w( "dikkat",err01.getMessage());
        }

    }
    private void DBLoad(String Kriter){
        Log.i("dikkat","DB rehber listesi alınıyor");
    myDB = new DatabaseAdapter(this);

    listAdapterUserList = new ArrayList<>();
    Cursor data = myDB.getListContents(Kriter);
    int numRows = data.getCount();
    if(numRows == 0){
        //Toast.makeText(MainActivity.this,"The Database is empty  :(.",Toast.LENGTH_LONG).show();
        Log.i("dikkat","The Database is empty" + String.valueOf(numRows));
    }else{
        int i=0;
        while(data.moveToNext()){
            listAdapterUser = new ListAdapterUser(data.getString(1),data.getString(2),data.getString(3),data.getString(4),data.getString(5));
            listAdapterUserList.add(i, listAdapterUser);
            i++;
        }
        ListAdapter adapter =  new ListAdapter(this,R.layout.list_row, listAdapterUserList);
        //listView = (ListView) findViewById(R.id.user_list);
        listView.setAdapter(adapter);
    }
}
    private void ParametreAl(){

//    File file = new File("/storage/emulated/0/data/rehber/AppVer.xml");
//    XMLVersiyonURL="/storage/emulated/0/data/rehber/AppVer.xml";

//    if(file != null && file.exists()){
        Log.i("dikkat", "AppVer.xml bulundu ve okunuyor.");
        // code if file exist in external storage
        try {
            URL urlVer = new URL("http://172.16.90.83/tftp/config/AppVer.xml");


            urlVer.openConnection().setReadTimeout(3);
            InputStream istream = getAssets().open("userdetails.xml");
            ArrayList<HashMap<String, String>> userList2 = new ArrayList<>();
            DocumentBuilderFactory builderFactory2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder2 = builderFactory2.newDocumentBuilder();
            //Document doc2 = docBuilder2.parse(new InputSource(urlVer.openStream()));
            Document doc2 = docBuilder2.parse(istream);
            doc2.getDocumentElement().normalize();
            //ListView lv = (ListView) findViewById(R.id.user_list);


            NodeList nListB = doc2.getElementsByTagName("Versiyon");
            for (int i = 0; i < nListB.getLength(); i++) {
                if (nListB.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element elm2 = (Element) nListB.item(i);
                    VersiyonMajor = getNodeValue("Major", elm2);
                    VersiyonMinor = getNodeValue("Minor", elm2);
//                    XMLRehberURL = getNodeValue("XMLRehberURL", elm2);
//                    ApkURL = getNodeValue("ApkURL", elm2);


                    Log.i("dikkat", "AppRemoteVer:" + VersiyonMajor + "." + VersiyonMinor);
                }

            }



            Log.i("dikkat", "AppVer:" + AppVerName  + "." +  AppVerCode);

            if (VersiyonMajor!=AppVerName && AppVerCode!=Integer.parseInt(VersiyonMinor)) {
                alertOneButton();
/*
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    // this will request for permission when permission is not true
                    LaunchRehberVerChk();
                }else{
                    //DownloadFile("http://172.16.90.83/tftp/config/","app-release.apk"); //1.3.3
                    //Log.i("dikkat", "app-release.apk indirildi.");
                    // Download code here
                    //UnInstallApplication(null);
                    //    Log.i("dikkat", "paket uninstall edildi.");
                    //installPack();
                    //    Log.i("dikkat", "paket install edildi.");
                    LaunchRehberVerChk();
                }
                */
             }
        }  catch (IOException err01) {
            err01.printStackTrace();
            Log.w( "dikkat", err01.getMessage());
        } catch (ParserConfigurationException err01) {
            err01.printStackTrace();
            Log.w( "dikkat",err01.getMessage());
        } catch (SAXException err01) {
            err01.printStackTrace();
            Log.w( "dikkat",err01.getMessage());
        }



    //}
    //else {
    //    Log.i("dikkat", "AppVer.xml bulunamadı, indiriliyor...");
    //    DownloadFile("http://172.16.90.83/tftp/config/AppVer.xml","AppVer.xml");   // file not found
    //ParametreAl();
    //}
}
    private void LaunchRehberVerChk(){
    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.rehberverchk");
    if (launchIntent != null) {
        Log.i("dikkat", "com.example.rehberverchk çalıştırıldı");
        startActivity(launchIntent);
    } else {
        Toast.makeText(MainActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
        Log.i("dikkat", "app-release.apk indirildi.");
    }
    finish();
    System.exit(0);
    ActivityCompat.finishAffinity(this);
}
    private void DownloadFile(String URLDownload, String DosyaAdi){
        if (toInstall != null && toInstall.exists()){ toInstall.delete();}
        Download_Uri = Uri.parse(URLDownload);
    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE );
    request.setAllowedOverRoaming(false);
    request.setDestinationInExternalPublicDir(String.valueOf(Environment.getDataDirectory()), "/rehber/"  + "/" + DosyaAdi);
    long refid = downloadManager.enqueue(request);

}
    private void installPack() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            // this will request for permission when permission is not true

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                Log.i("dikkat", "a...");
            } else {
                Uri apkUri = Uri.fromFile(toInstall);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i("dikkat", toInstall.getAbsolutePath() + "--b...");
            }
        }
    }
    public void alertOneButton() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("TESKİ Kurumsal Rehber Uygulaması")
                .setMessage("Uygulamanın versiyon v" + VersiyonMajor + "." + VersiyonMinor + " güncellemesini linkine tıklatarak yükleyebilirsiniz. http://172.16.90.83/tftp/config/AppVer.xml")
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }
    public void alertSimpleListView(String fsMobil, String fsDID) {

        /*
         * WebView is created programatically here.
         *
         * @Here are the list of items to be shown in the list
         */

            final CharSequence[] items = {fsMobil, fsDID};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Arama yapmak istediğiniz numarayı seçiniz.");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                Intent callIntent = new Intent("android.intent.action.DIAL");
                callIntent.setData(Uri.parse("tel:" + items[item]));
                MainActivity.this.startActivity(callIntent);

                // will toast your selection
                //setTitle("No: " + items[item]);
                dialog.dismiss();

            }
        }).show();
    }
    public void UnInstallApplication(String packageName) {
        Uri packageURI = Uri.parse("package:com.example.teski_rehber");
        //Uri packageURI = Uri.parse(packageName.toString());
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);

    }
    protected String getNodeValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if(node!=null){
            if(node.hasChildNodes()){
                Node child = node.getFirstChild();
                while (child!=null){
                    if(child.getNodeType() == Node.TEXT_NODE){
                        return  child.getNodeValue();
                    }
                }
            }
        }
        return "";


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rehber, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuBirimListele) {
            Intent i = new Intent(MainActivity.this, ExpMainActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if (id == R.id.menuArama) {
            //LinearLayout lytSearch1 = (LinearLayout) findViewById(R.id.lytSearch);
                lytSearch1.setVisibility(View.VISIBLE);
        } else if (id == R.id.menuYenile) {
            XMLLoad();
            DBLoad(null);
        } else if (id == R.id.menuExit) {
            finish();
            System.exit(0);
        }


        return super.onOptionsItemSelected(item);
    }

}