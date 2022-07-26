package com.vph.qltb.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanHelper extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    @Override
    public void handleResult(Result rawResult) {
        String results = rawResult.getText();

        LoginActivity.scanError.setText(results);
        Bundle bundleScan = new Bundle();
        bundleScan.putString("scan", results);

        onBackPressed();
        FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Kiểm tra nếu MSSV tồn tại
                if(snapshot.hasChild(results)){
                    Toast.makeText(ScanHelper.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScanHelper.this, Menu.class);
                    intent.putExtra("DataScan", bundleScan);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ScanHelper.this,"MSSV sai!",Toast.LENGTH_SHORT).show();
                    LoginActivity.scanError.setText("Không thành công!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}