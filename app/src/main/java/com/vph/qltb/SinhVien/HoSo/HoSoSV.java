package com.vph.qltb.SinhVien.HoSo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.SuaSDT;

import java.util.Map;

public class HoSoSV extends AppCompatActivity {

    TextView mssv, ten, lop, sdt, total_device;
    String MSSV = Menu.login;
    Button btnFixSDT, btnBack, btnReload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_sv);
        Controls();
        HienThiHoSo();
        xuLyButton();
        tinhTongTBDangMuon();
        DemSoThietBiDaMuon();
    }

    private void DemSoThietBiDaMuon() {
        FireBaseHelper.reference
                .child("DanhSachDangKy")
                .child(MSSV)
                .child("Thiết bị")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Đếm tổng số lượng
                        int sumSL = 0;
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object sl = map.get("soluong");
                            int pValue = Integer.parseInt(String.valueOf(sl));
                            sumSL += pValue;
                            total_device.setText(String.valueOf(sumSL));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void xuLyButton() {
        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Update
        btnFixSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoSoSV.this, SuaSDT.class);
                startActivity(intent);

            }
        });
    }

    private void HienThiHoSo() {

            FireBaseHelper.reference
                    .child("Account")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(MSSV)) {
                        final String getSV = snapshot.child(MSSV).child("sinhvien").getValue(String.class);
                        final String getSDT = snapshot.child(MSSV).child("sdt").getValue(String.class);
                        final String getLop = snapshot.child(MSSV).child("lop").getValue(String.class);
                        mssv.setText(MSSV);
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
        mssv = findViewById(R.id.txtStudent_ID);
        ten = findViewById(R.id.txtStudent_Name);
        lop = findViewById(R.id.txtStudent_Class);
        sdt = findViewById(R.id.txtStudent_Phone);
        total_device = findViewById(R.id.txtTotal_Device);

        btnBack = findViewById(R.id.btnBack);
        btnFixSDT = findViewById(R.id.btnFixSDT);
        btnReload = findViewById(R.id.btnReload);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });
    }
    public void Restart(){
        recreate();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
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
    private void tinhTongTBDangMuon() {

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