package com.vph.qltb.SinhVien.HoSo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChiTietHistory extends AppCompatActivity {
    Button btnBack, btnReload, btnSearch, btnCalen;
    SearchView searchDateTra;
    ListView listHistory;
    ArrayList<ModuleSV> dsHistory;
    Calendar calendar = Calendar.getInstance(); // Lấy thời gian hiện tại
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat Date = new SimpleDateFormat("dd/MM/yyyy"); //Format hiển thị ngày/tháng/năm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_theo_mssv);
        addControls();
        addEvents();
        hienthidanhsach();
        timkiem();
    }



    private void addControls() {
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        btnReload = findViewById(R.id.btnReload);
        btnCalen = findViewById(R.id.btnCalen);

        listHistory = findViewById(R.id.lvChiTietDangKy);
        dsHistory = new ArrayList<>();

        searchDateTra = findViewById(R.id.searchDateTra);
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

    }
    public void reload(){
        recreate();
        Toast.makeText(this, "Trang đã được tải lại", Toast.LENGTH_SHORT).show();
    }

    public class AdapterChiTiet extends ArrayAdapter<ModuleSV> {

        Context context;

        public AdapterChiTiet(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_chitiet_history, parent, false);
            }
            TextView SV = convertView.findViewById(R.id.txtStudent_Name);
            TextView ID = convertView.findViewById(R.id.txtStudent_ID);
            TextView Total_Numer = convertView.findViewById(R.id.txtTotal_Number);
            TextView DateDK = convertView.findViewById(R.id.txtDevice_DateDK);
            TextView TimeDK = convertView.findViewById(R.id.txtDevice_TimeDK);
            TextView DateMuon = convertView.findViewById(R.id.txtDevice_DateMuon);
            TextView TimeMuon = convertView.findViewById(R.id.txtDevice_TimeMuon);
            TextView DateTra = convertView.findViewById(R.id.txtDevice_DateTra);
            TextView TimeTra = convertView.findViewById(R.id.txtDevice_TimeTra);
            TextView Reason = convertView.findViewById(R.id.txtReason);
            //Hiển thị danh sách
            int stt = position + 1;
            SV.setText(""+stt);

            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("History");
            String tentb = bundle.getString("Ten");
            String key = moduleSV.getId();
            FireBaseHelper.reference.child("Account").child(Menu.login).child("LichSuMuon").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(tentb)){
                        final String getTotal = snapshot.child(tentb).child("keymuon").child(key).child("soluong").getValue(String.class);
                        final String getDateDK = snapshot.child(tentb).child("keymuon").child(key).child("dateDK").getValue(String.class);
                        final String getTimeDK = snapshot.child(tentb).child("keymuon").child(key).child("timeDK").getValue(String.class);
                        final String getDateMuon = snapshot.child(tentb).child("keymuon").child(key).child("dateMuon").getValue(String.class);
                        final String getTimeMuon = snapshot.child(tentb).child("keymuon").child(key).child("timeMuon").getValue(String.class);
                        final String getDateTra = snapshot.child(tentb).child("keymuon").child(key).child("dateTra").getValue(String.class);
                        final String getTimeTra = snapshot.child(tentb).child("keymuon").child(key).child("timeTra").getValue(String.class);
                        final String getReason = snapshot.child(tentb).child("keymuon").child(key).child("lydo").getValue(String.class);

                        ID.setText(null);
                        Total_Numer.setText(getTotal);
                        DateDK.setText(getDateDK);
                        TimeDK.setText(getTimeDK);
                        DateMuon.setText(getDateMuon);
                        TimeMuon.setText(getTimeMuon);
                        DateTra.setText(getDateTra);
                        TimeTra.setText(getTimeTra);
                        Reason.setText(getReason);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return convertView;
        }
    }
    private void hienthidanhsach(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("History");
        String tentb = bundle.getString("Ten");
        FireBaseHelper.reference.child("Account").child(Menu.login).child("LichSuMuon").child(tentb).child("keymuon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsHistory.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsHistory.add(moduleSV);
                }
                AdapterChiTiet adapterChiTiet = new AdapterChiTiet(ChiTietHistory.this, R.layout.design_view_thietbi, dsHistory);
                listHistory.setAdapter(adapterChiTiet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void timkiem() {

        //Pick ngày
        btnCalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener callback2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int M, int d) {
                        calendar.set(Calendar.YEAR, y);
                        calendar.set(Calendar.MONTH, M);
                        calendar.set(Calendar.DATE, d);
                        String date = Date.format(calendar.getTime());
                        searchDateTra.setQuery(date,false);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ChiTietHistory.this,
                        callback2,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)
                );
                datePickerDialog.show();
            }
        });


        //Tìm kiếm theo Date
        searchDateTra.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<ModuleSV> filterTB = new ArrayList<>();
                for (ModuleSV moduleSV : dsHistory) {
                    if (moduleSV.getDateTra().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleSV);
                    }
                }
                AdapterChiTiet adapterChiTiet = new AdapterChiTiet(getApplicationContext(), 0, filterTB);
                listHistory.setAdapter(adapterChiTiet);
                return false;
            }
        });
    }
}