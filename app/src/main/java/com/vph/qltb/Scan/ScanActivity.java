package com.vph.qltb.Scan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vph.qltb.R;


public class ScanActivity extends AppCompatActivity {

    ImageView btnQuestion;
    Button btnScan;
    public static TextView scanError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanError= (TextView) findViewById(R.id.ScanError);
        btnScan=(Button) findViewById(R.id.btnScan);
        //btnQuestion = (ImageView) findViewById(R.id.btnQuestion);

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtext();
            }
        });
    }

    private void showtext() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hướng dẫn");
        builder.setMessage("Bấm nút 'Quét mã vạch' và sử dụng thẻ sinh viên của bạn");
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create() ;
        dialog.show();
    }

    public void scanner(View view) {
        Intent intent = new Intent(ScanActivity.this, Scan.class);
        startActivity(intent);
    }
}