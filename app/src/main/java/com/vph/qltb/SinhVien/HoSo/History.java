package com.vph.qltb.SinhVien.HoSo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.DanhSach.ChiTietHistoryALL;
import com.vph.qltb.SinhVien.DanhSach.HistoryALL;
import com.vph.qltb.SinhVien.ModuleSV;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    Button btnBack, btnReload;
    SearchView searchView;
    ListView lvhistory;

    ArrayList<ModuleSV> dsHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_dk);
        Controls();
        Events();
        hienthidanhsach();
    }

    private void Controls() {
        btnBack = findViewById(R.id.btnBack);
        btnReload = findViewById(R.id.btnReload);
        searchView = findViewById(R.id.search);
        lvhistory = findViewById(R.id.lvHistory);
        dsHistory = new ArrayList<>();
    }
    private void Events(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                Toast.makeText(History.this, "Trang đã dược tải lại!", Toast.LENGTH_SHORT).show();
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
                ArrayList<ModuleSV> filterTB = new ArrayList<>();
                for (ModuleSV moduleSV : dsHistory) {
                    if (moduleSV.getTenthietbi().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleSV);
                    }
                }
                AdapterHistory adapterChiTiet = new AdapterHistory(getApplicationContext(), 0, filterTB);
                lvhistory.setAdapter(adapterChiTiet);
                return false;
            }
        });
    }
    public class AdapterHistory extends ArrayAdapter<ModuleSV> {
        Context context;
        public AdapterHistory(@NonNull Context context, int resource, @NonNull ArrayList<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_history, parent, false);
            }
            Button btnChiTiet = convertView.findViewById(R.id.btnChiTiet);
            TextView ten  = convertView.findViewById(R.id.txtDevice_Name);
            TextView total = convertView.findViewById(R.id.txtTotal_Number);
            ImageView img = convertView.findViewById(R.id.Device_Image);
            ten.setText(moduleSV.getTenthietbi());
            //Hiển thị
            String tentb = ten.getText().toString();
            FireBaseHelper.reference.child("DanhSachThietBi").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(tentb)){
                        final String getIMG = snapshot.child(tentb).child("hinhanh").getValue(String.class);
                        Picasso.get().load(getIMG)
                                .placeholder(R.drawable.ic_holder)
                                .error(R.drawable.ic_error)
                                .into(img);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FireBaseHelper.reference.child("Account")
                    .child(Menu.login)
                    .child("LichSuMuon")
                    .child(moduleSV.getTenthietbi())
                    .child("keymuon")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long num;
                            num = snapshot.getChildrenCount();
                            total.setText(String.valueOf(num));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            //Chi tiết
            btnChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Ten", moduleSV.getTenthietbi());
                    Intent intent = new Intent(context, ChiTietHistory.class);
                    intent.putExtra("History", bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    private void hienthidanhsach(){
        FireBaseHelper.reference.child("Account").child(Menu.login).child("LichSuMuon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsHistory.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsHistory.add(moduleSV);
                }
                AdapterHistory adapter = new AdapterHistory(History.this, R.layout.design_history, dsHistory);
                lvhistory.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}