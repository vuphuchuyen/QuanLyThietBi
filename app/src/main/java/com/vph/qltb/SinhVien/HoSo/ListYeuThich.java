package com.vph.qltb.SinhVien.HoSo;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;
import com.vph.qltb.ThietBi.ChiTietThietBi;
import com.vph.qltb.ThietBi.ChucNang.UpdateTB;
import com.vph.qltb.ThietBi.ModuleTB;
import com.vph.qltb.ThietBi.ThietBi;
import com.vph.qltb.ThietBi.ZoomActivity;

import java.util.ArrayList;
import java.util.List;

public class ListYeuThich extends AppCompatActivity {
    Button btnSelect;
    Button btnBack;
    Button btnReload;
    public static Button btnLoc;
    SearchView searchView;
    ListView lvYeuThich;
    ArrayList<ModuleTB> dsThietBi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_yeu_thich);
        Controls();
        hienthidanhsach();
        Events();
    }

    public class AdapterYeuThich extends ArrayAdapter {


        Context context;

        public AdapterYeuThich(@NonNull Context context, int resource, @NonNull List<ModuleTB> objects) {
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
            Button btnXoa = convertView.findViewById(R.id.btnXoa);
            Button btnZoom = convertView.findViewById(R.id.btnZoom);
            Button btnLove = convertView.findViewById(R.id.btnLove);
            Button btnUnLove = convertView.findViewById(R.id.btnUnLove);
            //Hi???n th??? danh s??ch
            Ten.setText(moduleTB.getTenthietbi());
            Role.setText(moduleTB.getRole());
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
                                bundle.putString("thietbi", moduleTB.getTenthietbi());
                                bundle.putString("soluong", moduleTB.getSoluong());
                                Intent intent = new Intent(ListYeuThich.this, ChiTietThietBi.class);
                                intent.putExtra("TB", bundle);
                                startActivity(intent);
                                return false;

                            } else if (menuItem.getItemId() == R.id.Muon) {
                                if(Integer.parseInt(String.valueOf(SL.getText())) == 0){
                                    Toast.makeText(ListYeuThich.this,"Thi???t b??? ???? h???t!",Toast.LENGTH_SHORT).show();
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("KQMuon", moduleTB.getTenthietbi());
                                    Intent intent = new Intent(context, PhieuDangKy.class);intent.putExtra("Muon", bundle);
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
            //X??a thi???t b???
            btnXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FireBaseHelper.reference.child("DanhSachThietBi").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String Tentb = Ten.getText().toString();
                            if(snapshot.hasChild(Tentb)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListYeuThich.this);
                                builder.setTitle("C???nh b??o!").setIcon(R.drawable.ic_warning);
                                builder.setMessage("X??c nh???n x??a thi???t b??? " + Tentb);
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FireBaseHelper.reference.child("DanhSachThietBi").child(Tentb).setValue(null);
                                        Toast.makeText(ListYeuThich.this,"Thi???t b??? " + Tentb + " ???? b??? x??a kh???i danh s??ch", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setPositiveButton("NO", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            //Hi???n tr???ng th??i y??u th??ch
            FireBaseHelper.reference.child("DanhSachThietBi").child(moduleTB.getTenthietbi()).child("yeuthich").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(String.valueOf(Menu.login))){
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
            //Th??? Love/ H???y Love
            String MSSV = Menu.login;

            FireBaseHelper.reference.child("DanhSachThietBi").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //H???y y??u th??ch
                    btnLove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (snapshot.hasChild(moduleTB.getTenthietbi())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListYeuThich.this);
                                builder.setTitle("Th??ng b??o!").setIcon(R.drawable.question);
                                builder.setMessage("X??c nh???n h???y y??u th??ch thi???t b??? " + moduleTB.getTenthietbi()+" ?");
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FireBaseHelper.reference.child("DanhSachThietBi").child(moduleTB.getTenthietbi()).child("yeuthich").child(MSSV).setValue(null);
                                        FireBaseHelper.reference.child("Account").child(MSSV).child("yeuthich").child(moduleTB.getTenthietbi()).setValue(null);
                                        Toast.makeText(ListYeuThich.this,"???? b??? " + Ten.getText() + " kh???i danh s??ch y??u th??ch!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setPositiveButton("NO", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        }
                    });
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //S???a TB
            btnFix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("UpdateKQ", String.valueOf(moduleTB.getTenthietbi()));
                    Intent intent = new Intent(context, UpdateTB.class);
                    intent.putExtra("Update", bundle);
                    context.startActivity(intent);

                }
            });
            //Hi???n th??? chi ti???t
            String key = Ten.getText().toString();
            FireBaseHelper.reference.child("DanhSachThietBi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(key)){
                        final String getSL = snapshot.child(key).child("soluong").getValue(String.class);
                        final String getHinhAnh = snapshot.child(key).child("hinhanh").getValue(String.class);
                        SL.setText(getSL);
                        Picasso.get().load(getHinhAnh)
                                .placeholder(R.drawable.ic_holder)
                                .error(R.drawable.ic_error)
                                .into(img);

                        //ZoomTB
                        Bundle bundle = new Bundle();
                        bundle.putString("ZoomKQ", getHinhAnh);
                        btnZoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ListYeuThich.this, ZoomActivity.class);
                                intent.putExtra("ZoomIMG", bundle);
                                startActivity(intent);
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //Hi???n th??? button
            FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(Menu.login)) {
                        final String getRole = snapshot.child(Menu.login).child("role").getValue(String.class);
                        if (getRole.equals("admin")) {
                            btnXoa.setVisibility(View.VISIBLE);
                            btnFix.setVisibility(View.VISIBLE);
                        } else{
                            btnXoa.setVisibility(View.INVISIBLE);
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

    private void Events() {
        //Reload
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });
        //Quay l???i Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //T??m ki???m thi???t b??? theo t??n
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ModuleTB> filterTB = new ArrayList<>();
                for (ModuleTB moduleTB : dsThietBi) {
                    if (moduleTB.getTenthietbi().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleTB);
                    }
                }
                AdapterYeuThich adapterYeuThich = new AdapterYeuThich(getApplicationContext(), 0, filterTB);
                lvYeuThich.setAdapter(adapterYeuThich);
                return false;
            }
        });
        //L???c thi???t b???
        ListYeuThich.btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), btnLoc);
                dropDownMenu.getMenuInflater().inflate(R.menu.menu_filter, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //L???c theo Module
                        if (menuItem.getItemId() == R.id.Module) {
                            String s = "Module";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for (ModuleTB moduleTB : dsThietBi) {
                                if (moduleTB.getLoai().toLowerCase().contains(s.toLowerCase())) {
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ListYeuThich.this, "???? l???c theo Module", Toast.LENGTH_SHORT).show();
                            AdapterYeuThich adapterYeuThich = new AdapterYeuThich(getApplicationContext(), 0, filterTB);
                            lvYeuThich.setAdapter(adapterYeuThich);
                            return false;

                            //L???c theo Laptop
                        } else if (menuItem.getItemId() == R.id.DienTu) {
                            String s = "??i???n t???";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for (ModuleTB moduleTB : dsThietBi) {
                                if (moduleTB.getLoai().toLowerCase().contains(s.toLowerCase())) {
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ListYeuThich.this, "???? l???c theo Thi???t b??? ??i???n t???", Toast.LENGTH_SHORT).show();
                            AdapterYeuThich adapterTB = new AdapterYeuThich(getApplicationContext(), 0, filterTB);
                            lvYeuThich.setAdapter(adapterTB);
                            return false;
                        } else if (menuItem.getItemId() == R.id.Lab) {
                            String s = "D??ng t???i Lab";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for (ModuleTB moduleTB : dsThietBi) {
                                if (moduleTB.getRole().toLowerCase().contains(s.toLowerCase())) {
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ListYeuThich.this, "???? l???c theo Thi???t b??? ch??? s??? d???ng t???i ph??ng Lab", Toast.LENGTH_SHORT).show();
                            AdapterYeuThich adapterYeuThich = new AdapterYeuThich(getApplicationContext(), 0, filterTB);
                            lvYeuThich.setAdapter(adapterYeuThich);
                            return false;
                        } else if (menuItem.getItemId() == R.id.Home) {
                            String s = "C?? th??? m?????n v???";
                            ArrayList<ModuleTB> filterTB = new ArrayList<>();
                            for (ModuleTB moduleTB : dsThietBi) {
                                if (moduleTB.getRole().toLowerCase().contains(s.toLowerCase())) {
                                    filterTB.add(moduleTB);
                                }
                            }
                            Toast.makeText(ListYeuThich.this, "???? l???c theo Thi???t b??? c?? th??? m?????n v??? nh??", Toast.LENGTH_SHORT).show();
                            AdapterYeuThich adapterYeuThich = new AdapterYeuThich(getApplicationContext(), 0, filterTB);
                            lvYeuThich.setAdapter(adapterYeuThich);
                            return false;
                        } else if (menuItem.getItemId() == R.id.Off) {
                            ArrayList<ModuleTB> filterTB = new ArrayList<>(dsThietBi);
                            Toast.makeText(ListYeuThich.this, "???? t???t l???c", Toast.LENGTH_SHORT).show();
                            AdapterYeuThich adapterYeuThich = new AdapterYeuThich(getApplicationContext(), 0, filterTB);
                            lvYeuThich.setAdapter(adapterYeuThich);
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
        Toast.makeText(this, "Trang ???? ???????c t???i l???i", Toast.LENGTH_SHORT).show();
    }
    private void hienthidanhsach() {
        FireBaseHelper.reference.child("Account").child(Menu.login).child("yeuthich").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsThietBi.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModuleTB moduleTB = ds.getValue(ModuleTB.class);
                    dsThietBi.add(moduleTB);
                }
                AdapterYeuThich adapter = new AdapterYeuThich(ListYeuThich.this, R.layout.design_view_thietbi, dsThietBi);
                lvYeuThich.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void Controls() {
        btnLoc = findViewById(R.id.btnLoc);
        searchView = findViewById(R.id.search);
        btnBack = findViewById(R.id.btnBack);
        lvYeuThich = findViewById(R.id.lvthietbi);
        dsThietBi = new ArrayList<>();
        btnReload = findViewById(R.id.btnReload);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ListYeuThich.this);
            builder.setTitle("C???nh b??o!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui l??ng ki???m tra l???i k???t n???i Internet c???a b???n!");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ListYeuThich.this);
            builder.setTitle("C???nh b??o!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui l??ng ki???m tra l???i k???t n???i Internet c???a b???n!");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ListYeuThich.this);
            builder.setTitle("C???nh b??o!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui l??ng ki???m tra l???i k???t n???i Internet c???a b???n!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    //Ki???m tra k???t n???i internet
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