package com.vph.qltb.ThietBi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;

import java.util.ArrayList;
import java.util.List;

public class ThietBi extends AppCompatActivity {
    Button btnSelect, btnBack, btnReload, btnLoc;
    SearchView searchView;
    ListView listALL;
    ArrayList<ModuleTB> dsThietBi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_danh_sach_thiet_bi);
        addControls();
        hienthiDanhSach();
        addEvents();
    }


    private void addControls() {
        btnLoc = findViewById(R.id.btnLoc);
        searchView = findViewById(R.id.search);
        btnBack = findViewById(R.id.btnBack);
        listALL = findViewById(R.id.lvthietbi);
        dsThietBi = new ArrayList<>();
        btnReload = findViewById(R.id.btnReload);
    }
    private void hienthiDanhSach() {
        //listALL
        FireBaseHelper.reference.child("DanhSachThietBi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsThietBi.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModuleTB moduleTB = ds.getValue(ModuleTB.class);
                    dsThietBi.add(moduleTB);
                }
                AdapterTB adapter = new AdapterTB(ThietBi.this, R.layout.design_view_thietbi, dsThietBi);
                listALL.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void addEvents() {
        //Reload
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });
        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                for (ModuleTB moduleTB : dsThietBi) {
                    if (moduleTB.getTen().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleTB);
                    }
                }
                AdapterTB adapterTB = new AdapterTB(getApplicationContext(), 0, filterTB);
                listALL.setAdapter(adapterTB);
                return false;
            }
        });
        //Lọc thiết bị
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
                                if(moduleTB.getLoai().toLowerCase().contains(s.toLowerCase())){
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ThietBi.this,"Đã lọc theo Module",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listALL.setAdapter(adapterTB);
                            return false;

                            //Lọc theo Laptop
                        }else if(menuItem.getItemId()==R.id.DienTu) {
                            String s = "Điện tử";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for(ModuleTB moduleTB: dsThietBi){
                                if(moduleTB.getLoai().toLowerCase().contains(s.toLowerCase())){
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ThietBi.this,"Đã lọc theo Thiết bị điện tử",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listALL.setAdapter(adapterTB);
                            return false;
                        }else if(menuItem.getItemId()==R.id.Lab) {
                            String s = "Dùng tại Lab";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for(ModuleTB moduleTB: dsThietBi){
                                if(moduleTB.getRole().toLowerCase().contains(s.toLowerCase())){
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ThietBi.this,"Đã lọc theo Thiết bị chỉ sử dụng tại phòng Lab",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listALL.setAdapter(adapterTB);
                            return false;
                        }else if(menuItem.getItemId()==R.id.Home) {
                            String s = "Có thể mượn về";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for(ModuleTB moduleTB: dsThietBi){
                                if(moduleTB.getRole().toLowerCase().contains(s.toLowerCase())){
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ThietBi.this,"Đã lọc theo Thiết bị có thể mượn về nhà",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listALL.setAdapter(adapterTB);
                            return false;
                        }
                        else if(menuItem.getItemId()==R.id.Off){
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for(ModuleTB moduleTB:dsThietBi){
                                filterTB.add(moduleTB);
                            }
                            Toast.makeText(ThietBi.this,"Đã tắt lọc",Toast.LENGTH_SHORT).show();
                            AdapterTB adapterTB = new AdapterTB(getApplicationContext(),0,filterTB);
                            listALL.setAdapter(adapterTB);
                        }
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

    }

    public void Restart() {
        recreate();
        Toast.makeText(this, "Trang đã được tải lại", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        } else {
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

        } else {
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

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //Kiểm tra kết nối internet
    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        } else
            return false;
    }

    public class AdapterTB extends ArrayAdapter {


        Context context;

        public AdapterTB(@NonNull Context context, int resource, @NonNull List<ModuleTB> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleTB moduleTB = (ModuleTB) getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_view_thietbi, parent, false);
            }
            TextView Ten = convertView.findViewById(R.id.txtDevice_Name);
            TextView SL = convertView.findViewById(R.id.txtTotal_Number);
            TextView Role = convertView.findViewById(R.id.txtDevice_Role);
            ImageView img = convertView.findViewById(R.id.Device_Image);

            Button btnFix = convertView.findViewById(R.id.btnFix);
            Button btnZoom = convertView.findViewById(R.id.btnZoom);
            Button btnLove = convertView.findViewById(R.id.btnLove);
            Button btnUnLove = convertView.findViewById(R.id.btnUnLove);
            //Hiển thị danh sách
            Ten.setText(moduleTB.getTen());
            Role.setText(moduleTB.getRole());
            SL.setText(moduleTB.getSoluong());
            Picasso.get().load(moduleTB.getHinhanh())
                    .placeholder(R.drawable.ic_holder)
                    .error(R.drawable.ic_error)
                    .into(img);
            //Menu
            btnSelect = convertView.findViewById(R.id.btnSelect);
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), view);
                    dropDownMenu.getMenuInflater().inflate(R.menu.menu_thietbi, dropDownMenu.getMenu());

                    dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.Thongtin) {
                                Bundle bundle = new Bundle();
                                bundle.putString("thietbi", moduleTB.getTen());
                                bundle.putString("soluong", moduleTB.getSoluong());
                                Intent intent = new Intent(ThietBi.this, ChiTietThietBi.class);
                                intent.putExtra("TB", bundle);
                                startActivity(intent);
                                return false;

                            } else if (menuItem.getItemId() == R.id.Muon) {
                                        if(Integer.parseInt(String.valueOf(SL.getText())) == 0){
                                            Toast.makeText(ThietBi.this,"Thiết bị đã hết!",Toast.LENGTH_SHORT).show();
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("KQMuon", moduleTB.getTen());
                                            Intent intent = new Intent(context, PhieuDangKy.class);
                                            intent.putExtra("Muon", bundle);
                                            startActivity(intent);
                                                }

                                return false;
                            }
                            return true;
                        }
                    });
                    dropDownMenu.show();
                }
            });
            //Hiện trạng thái yêu thích
            FireBaseHelper.reference.child("DanhSachThietBi").child(moduleTB.getTen()).child("yeuthich").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(String.valueOf(Menu.login))){
                        btnLove.setVisibility(View.VISIBLE);
                        btnUnLove.setVisibility(View.GONE);
                    }else {
                        btnLove.setVisibility(View.GONE);
                        btnUnLove.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        //Thả Love/ Hủy Love
            String MSSV = Menu.login;

            FireBaseHelper.reference.child("DanhSachThietBi").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Thêm vào yêu thích
                                btnUnLove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (snapshot.hasChild(moduleTB.getTen())) {
                                            String ten = moduleTB.getTen();
                                            String loai = moduleTB.getLoai();
                                            String role = moduleTB.getRole();
                                            ModuleTB moduleTBlove = new ModuleTB(ten, loai, role);
                                            FireBaseHelper.reference.child("DanhSachThietBi").child(moduleTB.getTen()).child("yeuthich").child(MSSV).setValue(MSSV);
                                            FireBaseHelper.reference.child("Account").child(MSSV).child("yeuthich").child(moduleTB.getTen()).setValue(moduleTBlove);
                                            btnLove.setVisibility(View.VISIBLE);
                                            btnUnLove.setVisibility(View.INVISIBLE);
                                            Toast.makeText(ThietBi.this, "Đã thêm " + Ten.getText() + " vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                //Hủy yêu thích
                                btnLove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (snapshot.hasChild(moduleTB.getTen())) {
                                            FireBaseHelper.reference.child("DanhSachThietBi").child(moduleTB.getTen()).child("yeuthich").child(MSSV).setValue(null);
                                            FireBaseHelper.reference.child("Account").child(MSSV).child("yeuthich").child(moduleTB.getTen()).setValue(null);
                                            btnLove.setVisibility(View.INVISIBLE);
                                            btnUnLove.setVisibility(View.VISIBLE);
                                            Toast.makeText(ThietBi.this,"Đã bỏ " + Ten.getText() + " khỏi danh sách yêu thích!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            //ZoomTB
            Bundle bundle = new Bundle();
            bundle.putString("ZoomKQ", moduleTB.getHinhanh());
            btnZoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ThietBi.this, ZoomActivity.class);
                    intent.putExtra("ZoomIMG", bundle);
                    startActivity(intent);
                }
            });
            //Sửa TB
            btnFix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("UpdateKQ", String.valueOf(moduleTB.getTen()));
                        Intent intent = new Intent(context, UpdateTB.class);
                        intent.putExtra("Update", bundle);
                        context.startActivity(intent);

                }
            });

            //Hiển thị button
            FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(Menu.login)) {
                        final String getRole = snapshot.child(Menu.login).child("role").getValue(String.class);
                        if (getRole.equals("admin")) {

                            btnFix.setVisibility(View.VISIBLE);
                        } else{
                            btnFix.setVisibility(View.GONE);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return convertView;
        }

    }
}