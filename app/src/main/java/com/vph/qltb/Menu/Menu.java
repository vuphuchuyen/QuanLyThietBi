package com.vph.qltb.Menu;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Login.LoginActivity;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.DanhSach.DsDangKy;
import com.vph.qltb.SinhVien.DanhSach.HistoryDK;
import com.vph.qltb.SinhVien.HoSo.HoSoSV;
import com.vph.qltb.ThietBi.ChucNang.ThemTB;
import com.vph.qltb.ThietBi.ThietBi;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    public static String login;
    Button btnDSTB, btnDSDK, btnThem, btnRule, btnHistory;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ListView lvSystem;
    ArrayList<ItemMenu> dsSystem;
    MenuAdapter adapterSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnHistory = findViewById(R.id.btnHistory);
        btnDSDK = findViewById(R.id.btnDSDK);
        btnDSTB = findViewById(R.id.btnDSTB);
        btnThem = findViewById(R.id.btnThem);
        toolbar =  findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        lvSystem = findViewById(R.id.lvSystem);
        Login();
        Button();
        actionToolBar();
        //MenuMain();
        System();
    }
    public class MenuAdapter extends BaseAdapter {
        private Context context;
        private int layout;
        private List<ItemMenu> list;
        @Override
        public int getCount() {
            return list.size();
        }

        public MenuAdapter(Context context, int layout, List<ItemMenu> list) {
            this.context = context;
            this.layout = layout;
            this.list = list;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        private class ViewHolder{
            TextView tv;
            ImageView img;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout,null);
                viewHolder = new ViewHolder();

                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv.setText(list.get(position).tenItem);
            viewHolder.img.setImageResource(list.get(position).icon);
            return convertView;
        }
    }
    private void Login(){
        Intent intent = getIntent();
        Bundle bundlescan = intent.getBundleExtra("DataScan");
        Bundle bundlelogin = intent.getBundleExtra("Data");
            if(bundlescan == null && bundlelogin == null && login == ""){
                Intent check = new Intent(Menu.this, LoginActivity.class);
                startActivity(check);
                finish();
            }
            else {
                 if (bundlescan == null) {
                    login = bundlelogin.getString("ketqua");
                } else if (bundlelogin == null) {
                    login = bundlescan.getString("scan");
                }
            }
    }

    private void Button(){
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent history = new Intent(Menu.this, HistoryDK.class);
                startActivity(history);
            }
        });
        btnDSTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dstb = new Intent(Menu.this, ThietBi.class);
                startActivity(dstb);
            }
        });
        btnDSDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsdk = new Intent(Menu.this, DsDangKy.class);
                startActivity(dsdk);
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent them = new Intent(Menu.this, ThemTB.class);
                startActivity(them);
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
                                login = "";
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
        //Hiển thị button
        FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(login)) {
                    final String getRole = snapshot.child(login).child("role").getValue(String.class);
                    if (getRole.equals("admin")) {

                        btnHistory.setVisibility(View.VISIBLE);
                        btnThem.setVisibility(View.VISIBLE);
                    } else{
                        btnHistory.setVisibility(View.GONE);
                        btnThem.setVisibility(View.GONE);
                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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