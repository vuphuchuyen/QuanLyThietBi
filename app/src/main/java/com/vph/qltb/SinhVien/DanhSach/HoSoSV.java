package com.vph.qltb.SinhVien.DanhSach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.ChinhSuaHS;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;

public class HoSoSV extends AppCompatActivity {

    TextView mssv, ten, lop, sdt, tbdm;
    ImageButton btnMail, btnNoti, btnBack;
    Button btnUpdate, btnMuon;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_sv);
        Controls();
        HienThiHoSo();
        xuLyButton();
        DemSoThietBiDaMuon();
    }

    private void DemSoThietBiDaMuon() {

    }

    private void xuLyButton() {
        //Muon
        btnMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HoSoSV.this);
                builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                builder.setMessage("Bạn muốn mượn thiết bị?");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent exit = new Intent(HoSoSV.this, PhieuDangKy.class);
                        startActivity(exit);
                        finish();
                    }
                });
                builder.setPositiveButton("NO", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoSoSV.this, ChinhSuaHS.class);
                startActivity(intent);

            }
        });
    }

    private void HienThiHoSo() {
            reference = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference();
            if (MenuLoginScan.scan == null) {
                mssv.setText(MenuLoginMSSV.login.getText().toString());
            } else {
                mssv.setText(MenuLoginScan.scan.getText().toString());
            }
            String HsMSSV = mssv.getText().toString();
            reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(HsMSSV)) {
                        final String getSV = snapshot.child(HsMSSV).child("sinhvien").getValue(String.class);
                        final String getSDT = snapshot.child(HsMSSV).child("sdt").getValue(String.class);
                        final String getLop = snapshot.child(HsMSSV).child("lop").getValue(String.class);
                        ten.setText(getSV);
                        sdt.setText(getSDT);
                        lop.setText(getLop);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    private void Controls() {
        mssv = findViewById(R.id.txtHsMSSV);
        ten = findViewById(R.id.txtHsTenSV);
        lop = findViewById(R.id.txtHsLop);
        sdt = findViewById(R.id.txtHsSDT);
        tbdm = findViewById(R.id.txtHsTBMuon);

        btnMail = findViewById(R.id.btnHsMail);
        btnNoti = findViewById(R.id.btnHsNoti);
        btnBack = findViewById(R.id.btnHsBack);
        btnUpdate = findViewById(R.id.btnHsUpdate);
        btnMuon = findViewById(R.id.btnHsMuon);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(HoSoSV.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(HoSoSV.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            if(networkInfo.isConnected())
                return true;
            else
                return false;
        }else
            return false;
    }
}