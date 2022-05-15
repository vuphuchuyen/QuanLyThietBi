package com.vph.qltb.SinhVien.HoSo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.DanhSach.ChiTietDangKy;

import java.util.Map;

public class HoSoSV extends AppCompatActivity {

    TextView mssv, ten, lop, sdt, total_device;
    String MSSV = Menu.login;
    Button btnFixSDT, btnBack, btnReload, btnListDangMuon, btnLove, btnHistory, btnOpenHS, btnOpenCN, btnOpenDM, btnPassWord;
    LinearLayout ExpandHS, ExpandCN, ExpandDM;
    CardView cardViewHS, cardViewCN, cardViewDM;
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

        //List yêu thích
        btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoSoSV.this, ListYeuThich.class);
                startActivity(intent);
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
        btnFixSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdt = new Intent(HoSoSV.this, SuaSDT.class);
                startActivity(sdt);

            }
        });
        //Đổi MK
        btnPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mk = new Intent(HoSoSV.this, DoiMK.class);
                startActivity(mk);
            }
        });
        //Hiển thị chi tiết mượn
        btnListDangMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoSoSV.this, ChiTietDangKy.class);
                startActivity(intent);
            }
        });
        //Hiển thị lịch sử
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("MSSV", Menu.login);
                Intent intent = new Intent(HoSoSV.this, History.class);
                intent.putExtra("History", bundle);
                startActivity(intent);
            }
        });
        //Expand HoSo
        btnOpenHS.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(ExpandHS.getVisibility() == View.GONE){
                    btnOpenHS.setBackground(getResources().getDrawable(R.drawable.ic_open));
                    TransitionManager.beginDelayedTransition(cardViewHS, new AutoTransition());
                    ExpandHS.setVisibility(View.VISIBLE);
                }else{
                    btnOpenHS.setBackground(getResources().getDrawable(R.drawable.ic_close));
                    ExpandHS.setVisibility(View.GONE);
                }
            }
        });
        //Expand DanhMuc
        ExpandDM.setVisibility(View.GONE);
        btnOpenDM.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(ExpandDM.getVisibility() == View.GONE){
                    btnOpenDM.setBackground(getResources().getDrawable(R.drawable.ic_open));
                    TransitionManager.beginDelayedTransition(cardViewDM, new AutoTransition());
                    ExpandDM.setVisibility(View.VISIBLE);
                }else{
                    btnOpenDM.setBackground(getResources().getDrawable(R.drawable.ic_close));
                    ExpandDM.setVisibility(View.GONE);
                }
            }
        });
        //Expand ChucNang
        ExpandCN.setVisibility(View.GONE);
        btnOpenCN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(ExpandCN.getVisibility() == View.GONE){
                    btnOpenCN.setBackground(getResources().getDrawable(R.drawable.ic_open));
                    TransitionManager.beginDelayedTransition(cardViewCN, new AutoTransition());
                    ExpandCN.setVisibility(View.VISIBLE);
                }else{
                    btnOpenCN.setBackground(getResources().getDrawable(R.drawable.ic_close));
                    ExpandCN.setVisibility(View.GONE);
                }
            }
        });
    }
    private void HienThiHoSo() {

            FireBaseHelper.reference
                    .child("Account")
                    .addValueEventListener(new ValueEventListener() {
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
        btnHistory = findViewById(R.id.btnHistory);
        btnListDangMuon = findViewById(R.id.btnlistTBMuon);
        mssv = findViewById(R.id.txtStudent_ID);
        ten = findViewById(R.id.txtStudent_Name);
        lop = findViewById(R.id.txtStudent_Class);
        sdt = findViewById(R.id.txtStudent_Phone);
        total_device = findViewById(R.id.txtTotal_Device);

        ExpandHS = findViewById(R.id.expand_HoSo);
        btnOpenHS = findViewById(R.id.btnOpenHS);
        cardViewHS = findViewById(R.id.CV_Expand_HoSo);
        ExpandCN = findViewById(R.id.expand_ChucNang);
        btnOpenCN = findViewById(R.id.btnOpenCN);
        cardViewCN = findViewById(R.id.CV_Expand_CN);
        ExpandDM = findViewById(R.id.expand_DanhMuc);
        btnOpenDM = findViewById(R.id.btnOpenDM);
        cardViewDM = findViewById(R.id.CV_Expand_DM);

        btnLove = findViewById(R.id.btnListLove);
        btnBack = findViewById(R.id.btnBack);
        btnPassWord = findViewById(R.id.btnDoiMatKhau);
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