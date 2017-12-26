package com.example.yychiu.PictureExampleApp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.content.CursorLoader;

import java.util.ArrayList;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recycler;
    RecyclerAdapter adapter;
    static final String[] projection = { MediaStore.Images.Thumbnails.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME };

    Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final int REQUEST_READ_STORAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permission = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        }else {
            readThumbnails();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_READ_STORAGE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readThumbnails();
                }else{
                    new AlertDialog.Builder(this)
                            .setMessage("need permission to do next!")
                            .setPositiveButton("OK",null)
                            .show();
                }
        }
    }
    private void readThumbnails() {
        recycler = findViewById(R.id.rview);
        adapter = new RecyclerAdapter(getAllShownImagesPath());
        recycler.setAdapter(adapter);

        recycler.setLayoutManager(new GridLayoutManager(this,3));

        recycler.addOnItemTouchListener(new ItemClickListener(recycler,
                new ItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                        intent.putExtra("POSITION",position);
                        startActivity(intent);
                    }
                }));
        getLoaderManager().initLoader(0,null,this);
    }

    private ArrayList<ItemData> getAllShownImagesPath() {
        String item_image,item_name;
        ArrayList<ItemData> listOfAllImages = new ArrayList<>();

        Cursor cursor = getContentResolver().query(uri, projection, null,
                null, null);

        while (cursor.moveToNext()) {
            item_image = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            item_name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

            ItemData data = new ItemData(item_name,item_image);

            listOfAllImages.add(data);
        }
        return listOfAllImages;
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(this,uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}