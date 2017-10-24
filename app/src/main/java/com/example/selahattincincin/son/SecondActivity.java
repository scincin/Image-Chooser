package com.example.selahattincincin.son;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;

public class SecondActivity extends AppCompatActivity {
    ImageView resim;
    EditText yazi;
    Button button;
    SQLiteDatabase database;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        resim=(ImageView)findViewById(R.id.imageView);
        yazi=(EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.button);
        Intent intent=getIntent();
        String info=intent.getStringExtra("info");
        if(info.equalsIgnoreCase("new")){
            Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.back);
            resim.setImageBitmap(bitmap);
            button.setVisibility(View.VISIBLE);
            yazi.setText("");
        }else {
            button.setVisibility(View.INVISIBLE);
            yazi.setText(intent.getStringExtra("isim"));
            resim.setImageBitmap(MainActivity.gidenImage);
        }
    }
    protected void kaydet(View view){
        String resimIsim=yazi.getText().toString();
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray=outputStream.toByteArray();
        try{
            database=this.openOrCreateDatabase("GunduzGezi", MODE_PRIVATE, null);
            database.execSQL("create table if not exists resimler(isim text, resim blob)");
            String sql="insert into resimler(isim,resim) values (?,?)";
            SQLiteStatement statement=database.compileStatement(sql);
            statement.bindString(1,resimIsim);
            statement.bindBlob(2, byteArray);
            statement.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    protected void resimEkle(View view){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }
        else{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            Uri image=data.getData();
            try{
                selectedImage=MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                resim.setImageBitmap(selectedImage);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

