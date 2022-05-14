package com.vph.qltb.SinhVien.DanhSach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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
import com.vph.qltb.SinhVien.ModuleSV;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryDK extends AppCompatActivity {
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
                Toast.makeText(HistoryDK.this, "Trang đã dược tải lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static class AdapterHistory extends ArrayAdapter<ModuleSV> {
        Context context;
        public AdapterHistory(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
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
            ten.setText(moduleSV.getTenthietbi());
            //Hiển thị
            String key = moduleSV.getId();

            FireBaseHelper.reference.child("LichSuMuonThietBi")
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
            return convertView;
        }
    }
    private void hienthidanhsach(){
        FireBaseHelper.reference.child("LichSuMuonThietBi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsHistory.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsHistory.add(moduleSV);
                }
                AdapterHistory adapter = new AdapterHistory(HistoryDK.this, R.layout.design_history, dsHistory);
                lvhistory.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}