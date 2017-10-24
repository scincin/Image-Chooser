package com.example.selahattincincin.son;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView list;
    public static Bitmap gidenImage;
    ArrayList<String> isimler;
    ArrayList<Bitmap> resimler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView)findViewById(R.id.listView);
        isimler=new ArrayList<String>();
        resimler=new ArrayList<Bitmap>();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, isimler);
        list.setAdapter(arrayAdapter);
        try{
            SQLiteDatabase database=this.openOrCreateDatabase("GunduzGezi",MODE_PRIVATE, null);
            database.execSQL("create table if not exists resimler(isim text, resim blob)");
            Cursor cursor=database.rawQuery("Select * from resimler", null);
            cursor.moveToFirst();
            while (cursor!=null){
                isimler.add(cursor.getString(0));
                byte[] byteArray=cursor.getBlob(1);
                Bitmap bitmap= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                resimler.add(bitmap);
                cursor.moveToNext();
                arrayAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("isim",isimler.get(position));
                gidenImage = resimler.get(position);
                startActivity(intent);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.resim_ekle, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.resim_ekle){
            Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
