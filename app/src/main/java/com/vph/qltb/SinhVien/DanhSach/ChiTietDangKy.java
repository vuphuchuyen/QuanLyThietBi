package com.vph.qltb.SinhVien.DanhSach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;

import java.util.ArrayList;
import java.util.List;

public class ChiTietDangKy extends AppCompatActivity {
    ListView lvChiTietDangKy;
    ArrayList<ModuleSV> dsChiTiet;
    public static String Mssv;
    Button btnBack, btnReload;
    SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_dang_ky);
        //Lấy list theo MSSV

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ThongTin");
        Mssv = bundle.getString("ChiTiet");

        Controls();
        hienthiDanhSach();
        Events();
    }
    public class AdapterChiTiet extends ArrayAdapter<ModuleSV> {

        Context context;

        public AdapterChiTiet(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_chitiet_dangky, parent, false);
            }
            TextView TenTB = convertView.findViewById(R.id.txtDevice_Name);
            TextView SoLuongTB = convertView.findViewById(R.id.txtTotal_Number);
            TextView date = convertView.findViewById(R.id.txtDevice_Date);
            TextView time = convertView.findViewById(R.id.txtDevice_Time);
            TextView StatusTB = convertView.findViewById(R.id.txtDevice_Status);
            TextView Lydo = convertView.findViewById(R.id.txtReason);
            ImageView imgTB = convertView.findViewById(R.id.Device_Image);

            Button btnXacNhan = convertView.findViewById(R.id.btnXacnhan);
            //Hiển thị danh sách
            FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("Thiết bị").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    TenTB.setText(moduleSV.getTenthietbi());
                                    SoLuongTB.setText(moduleSV.getSoluong());
                                    date.setText(moduleSV.getNgaymuon());
                                    time.setText(moduleSV.getThoigian());
                                    Lydo.setText(moduleSV.getLydo());
                                    //Hiển thị Status
                                    StatusTB.setText(moduleSV.getTinhtrang());
                                    if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 1){
                                        btnXacNhan.setVisibility(View.VISIBLE);
                                        StatusTB.setText("ĐĂNG KÝ");
                                    }else if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 2){
                                        btnXacNhan.setVisibility(View.INVISIBLE);
                                        StatusTB.setText("ĐANG MƯỢN");
                                    }else if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 3){
                                        btnXacNhan.setVisibility(View.VISIBLE);
                                        StatusTB.setText("ĐANG TRẢ");
                                    }else{
                                        btnXacNhan.setVisibility(View.INVISIBLE);
                                        StatusTB.setText("ĐÃ TRẢ");
                                    }
                     }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            FireBaseHelper.reference.child("DanhSachThietBi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(TenTB.getText().toString())){
                            final String getHinhAnh = snapshot.child(TenTB.getText().toString()).child("hinhanh").getValue(String.class);
                            Picasso.get().load(getHinhAnh)
                                    .placeholder(R.drawable.ic_holder)
                                    .error(R.drawable.ic_error)
                                    .into(imgTB);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return convertView;
        }

    }
    private void hienthiDanhSach() {

        FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("Thiết bị").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsChiTiet.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsChiTiet.add(moduleSV);
                }
                AdapterChiTiet adapter = new AdapterChiTiet(ChiTietDangKy.this, R.layout.design_chitiet_dangky, dsChiTiet);
                lvChiTietDangKy.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                Restart();
            }
        });
    }

    private void Controls(){
        lvChiTietDangKy = findViewById(R.id.lvChiTietDangKy);
        dsChiTiet = new ArrayList<>();
        btnReload = findViewById(R.id.btnReload);
        btnBack = findViewById(R.id.btnBack);
        search = findViewById(R.id.search);
    }
    public void Restart() {
        recreate();
        Toast.makeText(this, "Trang đã được tải lại", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietDangKy.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietDangKy.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietDangKy.this);
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