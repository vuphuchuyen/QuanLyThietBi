package com.vph.qltb.Menu;


import static com.vph.qltb.Login.LoginActivity.chkSaveLogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vph.qltb.Login.LoginActivity;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.DanhSach.DsDangKy;
import com.vph.qltb.SinhVien.DanhSach.DsMuon;
import com.vph.qltb.SinhVien.DanhSach.HoSoSV;
import com.vph.qltb.ThietBi.ThietBi;
import java.util.ArrayList;

public class Menu extends AppCompatActivity {

    public static TextView login;

    DatabaseReference reference = FirebaseDatabase
            .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference();
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ListView lvMain, lvSub, lvSystem;
    ArrayList<ItemMenu> dsMain, dsSub, dsSystem;
    MenuAdapter adapterMain, adapterSub, adapterSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        login = findViewById(R.id.MSSV);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvSystem = findViewById(R.id.lvSystem);
        Login();
        actionToolBar();
        MenuMain();
        //MenuSub();
        System();
    }
    private void Login(){
        Intent intent = getIntent();
        Bundle bundlescan = intent.getBundleExtra("DataScan");
        Bundle bundlelogin = intent.getBundleExtra("Data");
            if(bundlescan == null && bundlelogin == null && login.getText() == ""){
                Intent check = new Intent(Menu.this, LoginActivity.class);
                startActivity(check);
                finish();
            }
            else {
                 if (bundlescan == null) {
                    login.setText(bundlelogin.getString("ketqua"));
                } else if (bundlelogin == null) {
                    login.setText(bundlescan.getString("scan"));
                }
            }
    }


    private void MenuMain() {
        dsMain = new ArrayList<>();
        dsMain.add(new ItemMenu("Danh sách thiết bị", R.drawable.ic_thietbi));
        dsMain.add(new ItemMenu("Danh sách mượn", R.drawable.ic_list));
        dsMain.add(new ItemMenu("Danh sách đăng ký", R.drawable.ic_dangky));
        adapterMain = new MenuAdapter(this, R.layout.item_list_menu, dsMain);
        lvMain.setAdapter(adapterMain);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int main, long id) {
                switch (main) {
                    case 0:
                        Intent dstb = new Intent(Menu.this, ThietBi.class);
                        startActivity(dstb);
                        break;
                    case 1:
                        Intent dsnm = new Intent(Menu.this, DsMuon.class);
                        startActivity(dsnm);
                        break;
                    case 2:
                        Intent dsdk = new Intent(Menu.this, DsDangKy.class);
                        startActivity(dsdk);
                        break;
                }
            }
        });
    }


    public void update() {
        Toast.makeText(this, "Tính năng đang cập nhật!", Toast.LENGTH_SHORT).show();
    }

    private void System() {
        dsSystem = new ArrayList<>();
        dsSystem.add(new ItemMenu("Hồ sơ sinh viên", R.drawable.ic_nguoimuon));
        dsSystem.add(new ItemMenu("Hệ thống", R.drawable.ic_setting));
        dsSystem.add(new ItemMenu("Đăng xuất", R.drawable.ic_exit));
        adapterSystem = new MenuAdapter(this, R.layout.item_list_menu, dsSystem);
        lvSystem.setAdapter(adapterSystem);

        lvSystem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int system, long id) {
                switch (system) {
                    case 0:
                        Intent hssv = new Intent(Menu.this, HoSoSV.class);
                        startActivity(hssv);
                        break;
                    case 1:
                        update();
                        break;
                    case 2:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                        builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                        builder.setMessage("Bạn muốn đăng xuất?");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                login.setText("");
                                Bundle bundle = new Bundle();
                                Intent exit = new Intent(Menu.this, LoginActivity.class);
                                exit.putExtra("False",bundle);
                                startActivity(exit);
                                finish();
                            }
                        });
                        builder.setPositiveButton("NO", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
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