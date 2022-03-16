package com.vph.qltb.ThietBi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.squareup.picasso.Picasso;
import com.vph.qltb.Login.LoginActivity;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy_Bundle;

import java.util.ArrayList;
import java.util.List;

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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_thietbi, parent, false);
            }

            TextView STT = convertView.findViewById(R.id.txtHsMSSV);
            TextView Ten = convertView.findViewById(R.id.txtHsTenSV);
            TextView SL = convertView.findViewById(R.id.txtHsLop);
            TextView TT = convertView.findViewById(R.id.txtHsSDT);
            ImageView img = convertView.findViewById(R.id.HinhAnhtb);
            //Tự tăng STT
            int stt = position + 1;
            STT.setText(String.valueOf(stt));
            //Hiển thị danh sách
            Ten.setText(moduleTB.getTen());
            SL.setText(moduleTB.getSoluong());
            TT.setText(moduleTB.getThongtin());
            String key = moduleTB.getId();
            Picasso.get().load(moduleTB.getHinhanh())
                    .placeholder(R.drawable.ic_holder)
                    .error(R.drawable.ic_error)
                    .into(img);
            //Xóa
            ImageButton Xoa = convertView.findViewById(R.id.btnXoaTB);
            ThietBi.reference
                    .child(String.valueOf(key))
                    .orderByChild(String.valueOf(stt))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Xoa.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
                                    builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_error);
                                    builder.setMessage("Bạn có chắc muốn xóa thiết bị này?");
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (DataSnapshot delst : snapshot.getChildren()) {
                                                delst.getRef().setValue(null);
                                                Toast.makeText(context, "Thiết bị đã được xóa!", Toast.LENGTH_SHORT).show();
                                            }
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
            //Update
            ImageButton Update = convertView.findViewById(R.id.btnFix);

            ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("UpdateKQ", String.valueOf(key));
                            if (snapshot.hasChild(String.valueOf(key))) {
                                Intent intent = new Intent(context, UpdateTB.class);
                                intent.putExtra("Update", bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Mượn Tb trực tiếp
            Button Muon = convertView.findViewById(R.id.btnMuon);
            ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Muon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
                            builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                            builder.setMessage("Bạn có chắc muốn mượn thiết bị " + moduleTB.getTen() + " không ?");
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("KQMuon", moduleTB.getTen());
                                    if (snapshot.hasChild(String.valueOf(key))) {
                                        Intent intent = new Intent(context, PhieuDangKy_Bundle.class);
                                        intent.putExtra("Muon", bundle);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
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
            //Chức năng Zoom
            ImageButton Zoom = convertView.findViewById(R.id.Big);
            String HinhAnh = moduleTB.getHinhanh();
            Bundle bundle = new Bundle();
            bundle.putString("ZoomKQ", HinhAnh);
            ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Kiểm tra nếu MSSV tồn tại

                    Zoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ZoomActivity.class);
                            intent.putExtra("ZoomIMG", bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Kiểm tra Role
            if (MenuLoginScan.scan == null) {
                String check = MenuLoginMSSV.login.getText().toString();
                DatabaseReference reference = FirebaseDatabase
                        .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Account");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            Xoa.setVisibility(View.VISIBLE);
                            Update.setVisibility(View.VISIBLE);
                            Muon.setVisibility(View.GONE);
                        } else {
                            Xoa.setVisibility(View.GONE);
                            Update.setVisibility(View.GONE);
                            Muon.setVisibility(View.VISIBLE);
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
                        .getReference("Account");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            Xoa.setVisibility(View.VISIBLE);
                            Update.setVisibility(View.VISIBLE);
                            Muon.setVisibility(View.GONE);
                        } else {
                            Xoa.setVisibility(View.GONE);
                            Update.setVisibility(View.GONE);
                            Muon.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
            return convertView;
        }


    }

    //Kiểm tra role phân quyền
    private void Role() {
            if (MenuLoginScan.scan == null) {
                String check = MenuLoginMSSV.login.getText().toString();
                DatabaseReference reference = FirebaseDatabase
                        .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Account");
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
                        .getReference("Account");
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