package com.vph.qltb.ThietBi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.vph.qltb.R;

public class ZoomActivity extends AppCompatActivity {
    ImageView Zoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom);
        Zoom = findViewById(R.id.Zoom);
        xuly();
    }

    private void xuly() {
       Intent intent = getIntent();
       Bundle bundle = intent.getBundleExtra("ZoomIMG");
        String url = bundle.getString("ZoomKQ").toString();
        Picasso.get().load(String.valueOf(url))
                .placeholder(R.drawable.ic_holder)
                .error(R.drawable.ic_error)
                .into(Zoom);
    }

}