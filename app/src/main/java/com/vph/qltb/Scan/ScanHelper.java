package com.vph.qltb.Scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vph.qltb.Menu.MenuLoginScan;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanHelper extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView scannerView;
    DatabaseReference reference = FirebaseDatabase
            .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference();

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
        Bundle bundle = new Bundle();
        bundle.putString("scan", results);

        onBackPressed();
//        if (results.length()==9) {
//                Intent intent = new Intent(ScanHelper.this, MenuLoginScan.class);
//                intent.putExtra("Data", bundle);
//                startActivity(intent);
//                finish();
//        }
        reference.child("DanhSachSinhVien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Kiểm tra nếu MSSV tồn tại
                if(snapshot.hasChild(results)){
                    Toast.makeText(ScanHelper.this,"ScanHelper thành công!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScanHelper.this, MenuLoginScan.class);
                    intent.putExtra("Data", bundle);
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