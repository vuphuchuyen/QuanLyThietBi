package com.vph.qltb.ThietBi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;

import io.reactivex.rxjava3.internal.operators.completable.CompletableHide;

public class ChiTietThietBi extends AppCompatActivity {

    Button btnMuon, btnZoom;
    ImageView btnExit;
    TextView TenThietBi, ThongTin;
    DatabaseReference reference;
    ImageView img;
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thiet_bi);
        addControls();
        Events();
        createTab();
    }

    private void createTab() {
        tabHost.setup();
        //Tab 1
        TabHost.TabSpec tabThongTin;
        tabThongTin = tabHost.newTabSpec("tabThongTin");
        tabThongTin.setContent(R.id.tabThongTin);
        tabThongTin.setIndicator("Thông tin thiết bị");
        tabHost.addTab(tabThongTin);
        //Tab 2
        TabHost.TabSpec tabDanhSach;
        tabDanhSach = tabHost.newTabSpec("tabDanhSach");
        tabDanhSach.setContent(R.id.tabDanhSachMuon);
        tabDanhSach.setIndicator("Danh sách đang mượn");

        tabHost.addTab(tabDanhSach);

        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.color.yellow);
        tabHost.getTabWidget().setBackgroundResource(R.color.blue);

        //Đổi màu khi pick
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    if (tabHost.getTabWidget().getChildAt(i).isSelected()) {
                        tabHost.getTabWidget()
                                .getChildAt(i)
                                .setBackgroundResource(
                                        R.color.yellow);
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                        tv.setTextColor(R.color.black);
                    } else {
                        tabHost.getTabWidget()
                                .getChildAt(i)
                                .setBackgroundResource(
                                        R.color.blue);
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                        tv.setTextColor(R.color.white);
                    }
                }
            }
        });
    }

    private void addControls() {
        btnMuon = findViewById(R.id.btnMuon);
        btnExit = findViewById(R.id.btnExit);
        btnZoom = findViewById(R.id.btnZoom);
        TenThietBi = findViewById(R.id.txtTenThietBi);
        ThongTin = findViewById(R.id.txtThongTinThietBi);
        img = findViewById(R.id.imgThietBi);
        tabHost = findViewById(R.id.TabHostTB);
    }

    private void Events() {
        //Quay lại
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Hiển thị thiết bị
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TB");
        String key = bundle.getString("thietbi");
        reference.child("DanhSachThietBi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    final String getTenTB = snapshot.child(key).child("ten").getValue(String.class);
                    final String getThongtinTB = snapshot.child(key).child("thongtin").getValue(String.class);
                    final String getHinhAnhTB = snapshot.child(key).child("hinhanh").getValue(String.class);
                    TenThietBi.setText(getTenTB);
                    ThongTin.setText(getThongtinTB);
                    Picasso.get().load(getHinhAnhTB)
                            .placeholder(R.drawable.ic_holder)
                            .error(R.drawable.ic_error)
                            .into(img);
                    //ZoomTB
                    Bundle bundle = new Bundle();
                    bundle.putString("ZoomKQ", getHinhAnhTB);
                    ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            btnZoom.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChiTietThietBi.this, ZoomActivity.class);
                                    intent.putExtra("ZoomIMG", bundle);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Mượn thiết bị
        ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                btnMuon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietThietBi.this);
                        builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                        builder.setMessage("Bạn có chắc muốn mượn thiết bị " + TenThietBi.getText() + " không ?");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                bundle.putString("KQMuon", String.valueOf(TenThietBi.getText()));
                                    Intent intent = new Intent(ChiTietThietBi.this, PhieuDangKy.class);
                                    intent.putExtra("Muon", bundle);
                                    startActivity(intent);
                            }
                        });
                        builder.setPositiveButton("NO", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}