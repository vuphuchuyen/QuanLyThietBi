package com.vph.qltb.ThietBi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.vph.qltb.Login.LoginActivity;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThietBi extends AppCompatActivity {


    SearchView searchView;
    ImageButton btnBack, btnRestart, btnThem, btnChange;
    Button btnLoc;
    ListView listView;
    ArrayList<ModuleTB> dsThietBi;
    public static DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_thiet_bi);
        addControls();
        Role();
        hienthiDanhSach();
        addEvents();

    }
    //Kiểm tra role phân quyền
    private void Role() {
            if (MenuLoginScan.scan == null) {
                String check = MenuLoginMSSV.login.getText().toString();
                DatabaseReference reference = FirebaseDatabase
                        .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("DanhSachSinhVien");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            btnThem.setVisibility(View.VISIBLE);
                        } else {
                            btnThem.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                String check = MenuLoginScan.scan.getText().toString();
                DatabaseReference reference = FirebaseDatabase
                        .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("DanhSachSinhVien");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            btnThem.setVisibility(View.VISIBLE);
                        } else {
                            btnThem.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

    }

    private void addControls() {
        searchView = findViewById(R.id.search);
        btnThem = findViewById(R.id.btnThem);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnRestart = findViewById(R.id.btnRestartTB);
        btnLoc = findViewById(R.id.btnLoc);
        listView = findViewById(R.id.lvthietbi);
        dsThietBi = new ArrayList<>();
        btnChange = findViewById(R.id.btnChange);

    }
    private void hienthiDanhSach() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("DanhSachThietBi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsThietBi.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleTB moduleTB = ds.getValue(ModuleTB.class);
                    dsThietBi.add(moduleTB);
                }
                AdapterTB adapter = new AdapterTB(ThietBi.this,R.layout.design_thietbi,dsThietBi);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addEvents() {
        //Tải lại trang
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });
        //Them
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThietBi.this, ThemTB.class);
                startActivity(intent);
            }
        });

        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        //Tìm kiếm thiết bị theo tên
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ModuleTB> filterTB = new ArrayList<>();
                for(ModuleTB moduleTB: dsThietBi){
                    if(moduleTB.getTen().toLowerCase().contains(s.toLowerCase())){
                        filterTB.add(moduleTB);
                    }
                }
                AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                listView.setAdapter(adapterTB);
                return false;
            }
        });
        //Lọc theo danh sách
        btnLoc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), btnLoc);
                dropDownMenu.getMenuInflater().inflate(R.menu.menu_filter, dropDownMenu.getMenu());

                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                            //Lọc theo Module
                        if(menuItem.getItemId()==R.id.Module){
                            String s = "Module";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for(ModuleTB moduleTB: dsThietBi){
                                if(moduleTB.getThongtin().toLowerCase().contains(s.toLowerCase())){
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ThietBi.this,"Đã lọc theo Module",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listView.setAdapter(adapterTB);
                            return false;

                            //Lọc theo Laptop
                        }else if(menuItem.getItemId()==R.id.Laptop) {
                            String s = "Laptop";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for (ModuleTB thietBi : dsThietBi) {
                                if (thietBi.getTen().toLowerCase().contains(s.toLowerCase())) {

                                    filterTB.add(thietBi);
                                }
                            }
                            Toast.makeText(ThietBi.this, "Đã lọc theo Laptop", Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(), 0, filterTB);
                            listView.setAdapter(adapterTB);
                            return false;
                        }
                            //Lọc theo camera
                            else if(menuItem.getItemId()==R.id.Camera){
                                String s = "Camera";
                                ArrayList<ModuleTB> filterTB = new ArrayList<>();
                                for(ModuleTB thietBi: dsThietBi){
                                    if(thietBi.getTen().toLowerCase().contains(s.toLowerCase())){

                                        filterTB.add(thietBi);
                                    }
                                }
                                Toast.makeText(ThietBi.this,"Đã lọc theo Camera",Toast.LENGTH_SHORT).show();
                                AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                                listView.setAdapter(adapterTB);
                                return false;
                        }else if(menuItem.getItemId()==R.id.Off_Filter){
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for(ModuleTB moduleTB:dsThietBi){
                                filterTB.add(moduleTB);
                            }
                            Toast.makeText(ThietBi.this,"Đã tắt lọc",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listView.setAdapter(adapterTB);
                        }
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });
        //Thay đổi kiểu hiển thị


    }

    public void Restart(){
        Intent intent = new Intent(this,ThietBi.class);
        startActivity(intent);
        finish();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //Kiểm tra kết nối internet
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