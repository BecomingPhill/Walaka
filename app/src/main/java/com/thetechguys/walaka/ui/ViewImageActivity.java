package com.thetechguys.walaka.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thetechguys.walaka.R;


public class ViewImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();

        Picasso.with(this).load(imageUri.toString()).into(imageView);

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                finish();
//            }
//        },10*1000);

    }







    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
          //  return true;
       // }

       // return super.onOptionsItemSelected(item);
    }




