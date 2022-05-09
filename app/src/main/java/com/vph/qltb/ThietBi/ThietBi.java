package com.vph.qltb.ThietBi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;

import java.util.ArrayList;
import java.util.List;

public class ThietBi extends AppCompatActivity {
    public static DatabaseReference reference;
    Button btnSelect;
    TabHost tabHost;
    SearchView searchView;
    ImageButton btnBack;
    ListView listALL, listMODULE, listDIENTU;
    ArrayList<ModuleTB> dsThietBi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_danh_sach_thiet_bi);
        addControls();
        createTab();
        hienthiDanhSach();
        addEvents();
    }


    private void addControls() {
        tabHost = findViewById(R.id.TabThietBi);
        searchView = findViewById(R.id.search);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        listALL = findViewById(R.id.lvtoanbothietbi);
        listMODULE = findViewById(R.id.lvmodule);
        listDIENTU = findViewById(R.id.lvdodientu);
        dsThietBi = new ArrayList<>();

    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTab() {
        tabHost.setup();
        //Tab 1
        TabHost.TabSpec tabALL;
        tabALL = tabHost.newTabSpec("tabALL");
        tabALL.setContent(R.id.tabALL);
        tabALL.setIndicator("ALL");
        tabHost.addTab(tabALL);
        //Tab 2
        TabHost.TabSpec tabModule;
        tabModule = tabHost.newTabSpec("tabModule");
        tabModule.setContent(R.id.tabModule);
        tabModule.setIndicator("MODULE");
        tabHost.addTab(tabModule);
        //Tab 3
        TabHost.TabSpec tabDientu;
        tabDientu = tabHost.newTabSpec("tabDientu");
        tabDientu.setContent(R.id.tabDientu);
        tabDientu.setIndicator("ĐỒ ĐIỆN TỬ");
        tabHost.addTab(tabDientu);
        //Đổi màu khi pick

        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.color.yellow);
        tabHost.getTabWidget().setBackgroundResource(R.color.blue);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    if (tabHost.getTabWidget().getChildAt(i).isSelected()) {
                        tabHost.getTabWidget()
                                .getChildAt(i)
                                .setBackgroundResource(
                                        R.color.yellow);
                    } else {
                        tabHost.getTabWidget()
                                .getChildAt(i)
                                .setBackgroundResource(
                                        R.color.blue);

                    }
                }
            }
        });

    }
    private void hienthiDanhSach() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("DanhSachThietBi");
        //listALL
        reference.addValueEventListener(new ValueEventListener() {
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
        //listModule
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsThietBi.clear();
                String s = "Module";
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModuleTB moduleTB = ds.getValue(ModuleTB.class);
                    dsThietBi.add(moduleTB);
                }
                ArrayList<ModuleTB> filterTB = new ArrayList<>();
                for (ModuleTB moduleTB : dsThietBi) {
                    if (moduleTB.getLoai().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleTB);
                    }
                }
                AdapterTB adapterTB = new AdapterTB(getApplicationContext(), 0, filterTB);
                listMODULE.setAdapter(adapterTB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //listDienTu
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsThietBi.clear();
                String s = "Điện tử";
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModuleTB moduleTB = ds.getValue(ModuleTB.class);
                    dsThietBi.add(moduleTB);
                }
                ArrayList<ModuleTB> filterTB = new ArrayList<>();
                for (ModuleTB moduleTB : dsThietBi) {
                    if (moduleTB.getLoai().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleTB);
                    }
                }
                AdapterTB adapterTB = new AdapterTB(getApplicationContext(), 0, filterTB);
                listDIENTU.setAdapter(adapterTB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void addEvents() {

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
            TextView Ten = convertView.findViewById(R.id.txtTenThietBi);
            TextView SL = convertView.findViewById(R.id.txtSoluongThietBi);
            ImageView img = convertView.findViewById(R.id.imgThietBi);
            Button btnLove = convertView.findViewById(R.id.btnLove);
            Button btnUnLove = convertView.findViewById(R.id.btnUnLove);
            //Hiển thị danh sách
            Ten.setText(moduleTB.getTen());
            SL.setText(moduleTB.getSoluong());
            String key = moduleTB.getId();
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
                                bundle.putString("thietbi", key);
                                Intent intent = new Intent(ThietBi.this, ChiTietThietBi.class);
                                intent.putExtra("TB", bundle);
                                startActivity(intent);

                                return false;

                            } else if (menuItem.getItemId() == R.id.Muon) {
                                ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ThietBi.this);
                                        builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                                        builder.setMessage("Bạn có chắc muốn mượn thiết bị " + moduleTB.getTen() + " không ?");
                                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("KQMuon", moduleTB.getTen());
                                                if (snapshot.hasChild(String.valueOf(key))) {
                                                    Intent intent = new Intent(context, PhieuDangKy.class);
                                                    intent.putExtra("Muon", bundle);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                        builder.setPositiveButton("NO", null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                return false;
                            }
                            return true;
                        }
                    });
                    dropDownMenu.show();
                }
            });
            //Hiện trạng thái yêu thích
            DatabaseReference refYT = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference();
            refYT.child("DanhSachThietBi").child(key).child("yeuthich").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(String.valueOf(Menu.login.getText()))){
                        btnLove.setVisibility(View.VISIBLE);
                        btnUnLove.setVisibility(View.INVISIBLE);
                    }else {
                        btnLove.setVisibility(View.INVISIBLE);
                        btnUnLove.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        //Thả Love/ Hủy Love
            String MSSV = Menu.login.getText().toString();

                   refYT.child("DanhSachThietBi").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Thêm vào yêu thích
                           String tentblove = Ten.getText().toString();
                                btnUnLove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (snapshot.hasChild(key)) {
                                            ModuleTB moduleTBlove = new ModuleTB(key, tentblove);
                                            refYT.child("DanhSachThietBi").child(key).child("yeuthich").child(MSSV).setValue(MSSV);
                                            refYT.child("Account").child(MSSV).child("yeuthich").child(key).setValue(moduleTBlove);
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
                                        if (snapshot.hasChild(key)) {
                                            refYT.child("DanhSachThietBi").child(key).child("yeuthich").child(MSSV).setValue(null);
                                            refYT.child("Account").child(MSSV).child("yeuthich").child(key).setValue(null);
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



            return convertView;
        }

    }
}