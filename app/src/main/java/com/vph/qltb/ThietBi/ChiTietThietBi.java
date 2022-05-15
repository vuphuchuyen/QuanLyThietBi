package com.vph.qltb.ThietBi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;
import com.vph.qltb.SinhVien.DanhSach.ChiTietDangKy;
import com.vph.qltb.SinhVien.DanhSach.DsDangKy;
import com.vph.qltb.SinhVien.ModuleSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChiTietThietBi extends AppCompatActivity {

    Button btnMuon, btnZoom, btnExit , btnReload;
    TextView TenThietBi, ThongTin, Number_Love, Soluong, LoaiTB, RoleTB;
    ImageView img, Love;
    TabHost tabHost;
    ListView listdanhsach;

    ArrayList<ModuleSV> dsDangMuon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thiet_bi);
        addControls();
        Events();
        createTab();
        hienthidanhsach();
    }



    private void createTab() {
        tabHost.setup();
        //Tab 1
        TabHost.TabSpec tabThongTin;
        tabThongTin = tabHost.newTabSpec("tabThongTin");
        tabThongTin.setContent(R.id.tabThongTin);
        tabThongTin.setIndicator("Thông tin");
        tabHost.addTab(tabThongTin);
        //Tab 2
        TabHost.TabSpec tabDanhSach;
        tabDanhSach = tabHost.newTabSpec("tabDanhSach");
        tabDanhSach.setContent(R.id.tabDanhSachMuon);
        tabDanhSach.setIndicator("Đang mượn");

        tabHost.addTab(tabDanhSach);

        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.color.white);
        tabHost.getTabWidget().setBackgroundResource(R.color.gray);

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
                                        R.color.white);

                    } else {
                        tabHost.getTabWidget()
                                .getChildAt(i)
                                .setBackgroundResource(
                                        R.color.gray);
                    }
                }
            }
        });
    }

    private void addControls() {
        btnMuon = findViewById(R.id.btnMuon);
        btnExit = findViewById(R.id.btnExit);
        btnZoom = findViewById(R.id.btnZoom);
        TenThietBi = findViewById(R.id.txtDevice_Name);
        ThongTin = findViewById(R.id.txtDevice_Info);
        Soluong = findViewById(R.id.txtTotal_Number);
        LoaiTB = findViewById(R.id.txtDevice_Type);
        img = findViewById(R.id.Device_Image);
        tabHost = findViewById(R.id.TabHostTB);
        btnReload = findViewById(R.id.btnRestart);
        Number_Love = findViewById(R.id.Number_Love);
        Love = findViewById(R.id.imgLove);
        RoleTB = findViewById(R.id.txtDevice_Role);
        listdanhsach = findViewById(R.id.lvDanhSachMuon);
        dsDangMuon = new ArrayList<>();
    }

    private void Events() {
        //Hiển thị số yêu thích
        Love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeuthichline();
            }
        });
        Number_Love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeuthichline();
            }
        });
        //Tải lại trang
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restart();
            }
        });
        //Quay lại
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Hiển thị thiết bị
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TB");
        String key = bundle.getString("thietbi");
        String soluong = bundle.getString("soluong");
        FireBaseHelper.reference.child("DanhSachThietBi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    final String getTenTB = snapshot.child(key).child("tenthietbi").getValue(String.class);
                    final String getThongtinTB = snapshot.child(key).child("thongtin").getValue(String.class);
                    final String getHinhAnhTB = snapshot.child(key).child("hinhanh").getValue(String.class);
                    final String getSL = snapshot.child(key).child("soluong").getValue(String.class);
                    final String getType = snapshot.child(key).child("loai").getValue(String.class);
                    final String getRole = snapshot.child(key).child("role").getValue(String.class);
                    TenThietBi.setText(getTenTB);
                    ThongTin.setText(getThongtinTB);
                    Soluong.setText(getSL);
                    LoaiTB.setText(getType);
                    RoleTB.setText(getRole);
                    Picasso.get().load(getHinhAnhTB)
                            .placeholder(R.drawable.ic_holder)
                            .error(R.drawable.ic_error)
                            .into(img);
                    //ZoomTB
                    Bundle bundle = new Bundle();
                    bundle.putString("ZoomKQ", getHinhAnhTB);
                            btnZoom.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChiTietThietBi.this, ZoomActivity.class);
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

        //Tổng yêu thích
        FireBaseHelper.reference.child("DanhSachThietBi")
                .child(key)
                .child("yeuthich")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long num;
                        num = snapshot.getChildrenCount();
                        Number_Love.setText(String.valueOf(num));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Mượn thiết bị
        FireBaseHelper.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                btnMuon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(soluong) == 0) {
                            Toast.makeText(ChiTietThietBi.this,"Thiết bị đã hết!",Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietThietBi.this);
                            builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                            builder.setMessage("Xác nhận mượn thiết bị " + TenThietBi.getText() + " không ?");
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
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void yeuthichline(){
        Toast.makeText(this,"Thiết bị có " + Number_Love.getText() + " lượt yêu thích",Toast.LENGTH_SHORT).show();
    }
    public class AdapterListMuon extends ArrayAdapter<ModuleSV> {

        Context context;

        public AdapterListMuon(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_dangmuon, parent, false);
            }
            TextView SV = convertView.findViewById(R.id.txtStudent_Name);
            TextView ID = convertView.findViewById(R.id.txtStudent_ID);
            TextView timeMuon = convertView.findViewById(R.id.txtDevice_Time);
            TextView dateMuon = convertView.findViewById(R.id.txtDevice_Date);
            TextView SL = convertView.findViewById(R.id.txtTotal_Number);
            TextView Pos = convertView.findViewById(R.id.txtDevice_Position_Number);
            //Hiển thị
            int stt = position + 1;
            Pos.setText("" + stt);
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("TB");
            String ten = bundle.getString("thietbi");
            String key = moduleSV.getId();
            FireBaseHelper.reference.child("DanhSachThietBi").child(ten).child("Đang mượn").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(key)){
                        final String getID = snapshot.child(key).child("mssv").getValue(String.class);
                        final String getTimeMuon = snapshot.child(key).child("timeMuon").getValue(String.class);
                        final String getDateMuon = snapshot.child(key).child("dateMuon").getValue(String.class);
                        final String getSL = snapshot.child(key).child("soluong").getValue(String.class);
                        ID.setText(getID);
                        timeMuon.setText(getTimeMuon);
                        dateMuon.setText(getDateMuon);
                        SL.setText(getSL);
                        FireBaseHelper.reference.child("Account").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(getID)){
                                    final String getSV = snapshot.child(getID).child("sinhvien").getValue(String.class);
                                    SV.setText(getSV);
                                }
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
            return convertView;
        }
    }
    private void hienthidanhsach() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TB");
        String key = bundle.getString("thietbi");
        FireBaseHelper.reference.child("DanhSachThietBi").child(key).child("Đang mượn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsDangMuon.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsDangMuon.add(moduleSV);
                }
                AdapterListMuon adapter = new AdapterListMuon(ChiTietThietBi.this, R.layout.design_dangmuon, dsDangMuon);
                listdanhsach.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void Restart(){
        recreate();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
    }
}