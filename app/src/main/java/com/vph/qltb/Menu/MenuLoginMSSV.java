package com.vph.qltb.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.Scan.ScanHelper;
import com.vph.qltb.SinhVien.DangKy.SinhVienDK;
import com.vph.qltb.SinhVien.DangKy.PhieuDangKy;
import com.vph.qltb.SinhVien.Muon.SinhVienM;

import com.vph.qltb.ThietBi.ThietBi;
import com.vph.qltb.R;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MenuLoginMSSV extends AppCompatActivity {

    public static TextView login;
    TextView tensinhvien;

    DatabaseReference reference = FirebaseDatabase
            .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference();
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ListView lvMain, lvSub;
    ArrayList<ItemMenu> dsMain, dsSub;
    MenuAdapter adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        login = findViewById(R.id.MSSV);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvSub = (ListView) findViewById(R.id.lvSub);
        tensinhvien = findViewById(R.id.txtTenSinhVien);
        Login();
        actionToolBar();
        MenuMain();
        //MenuSub();

    }

    private void Login() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Data");
        login.setText(bundle.getString("ketqua"));
        String mssv = login.getText().toString();
        reference.child("DanhSachSinhVien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mssv)) {
                    final String getMK = snapshot.child(mssv).child("sinhvien").getValue(String.class);
                    tensinhvien.setText(getMK);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void MenuMain() {
        dsMain= new ArrayList<>();
        dsMain.add(new ItemMenu("Danh sách thiết bị", R.drawable.ic_thietbi));
        dsMain.add(new ItemMenu("Danh sách người mượn", R.drawable.ic_nguoimuon));
        dsMain.add(new ItemMenu("Danh sách đăng ký", R.drawable.ic_dangky));
        dsMain.add(new ItemMenu("Đăng ký mượn", R.drawable.ic_hand));
        adapter2 = new MenuAdapter(this, R.layout.item_list_menu, dsMain);
        lvMain.setAdapter(adapter2);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int main, long id) {
                switch (main){
                    case 0:
                        Intent dstb = new Intent(MenuLoginMSSV.this, ThietBi.class);
                        startActivity(dstb);
                        break;
                    case 1:
                        Intent dsnm = new Intent(MenuLoginMSSV.this, SinhVienM.class);
                        startActivity(dsnm);
                        break;
                    case 2:
                        Intent dsdk = new Intent(MenuLoginMSSV.this, SinhVienDK.class);
                        startActivity(dsdk);
                        break;
                    case 3:
                        Intent dkm = new Intent(MenuLoginMSSV.this, PhieuDangKy.class);
                        startActivity(dkm);
                        break;
                }
            }
        });
    }

    private void MenuSub() {
        dsSub= new ArrayList<>();
        dsSub.add(new ItemMenu("Số ngẫu nhiên", R.drawable.ic_num));
        dsSub.add(new ItemMenu("Bánh quay may mắn", R.drawable.ic_laban));
        adapter1 = new MenuAdapter(this, R.layout.item_list_menu, dsSub);
        lvSub.setAdapter(adapter1);

        lvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int sub, long id) {
                switch (sub){
                    case 0:
                        //Intent dstb = new Intent(MenuLoginMSSV.this, ThietBi.class);
                        //startActivity(dstb);
                        update();
                        break;
                    case 1:
                        //Intent dsnm = new Intent(MenuLoginMSSV.this, SinhVienM.class);
                        //startActivity(dsnm);
                        update();
                        break;

                }
            }
        });
    }
    public  void update(){
        Toast.makeText(this,"Tính năng đang cập nhật!",Toast.LENGTH_SHORT).show();
    }


    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }


}