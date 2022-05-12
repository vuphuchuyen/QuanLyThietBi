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
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Map;

public class ChiTietDangKy extends AppCompatActivity {
    ListView lvChiTietDangKy;
    ArrayList<ModuleSV> dsChiTiet;
    public static String Mssv;
    Button btnBack, btnReload;
    SearchView searchView;

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

        Calendar calendar = Calendar.getInstance(); // Lấy thời gian hiện tại
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy"); //Format hiển thị ngày/tháng/năm
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");

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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_chitiet_dangky, parent, false);
            }
            TextView TenTB = convertView.findViewById(R.id.txtDevice_Name);
            TextView SoLuongTB = convertView.findViewById(R.id.txtTotal_Number);
            TextView date = convertView.findViewById(R.id.txtDevice_Date);
            TextView time = convertView.findViewById(R.id.txtDevice_Time);
            TextView StatusTB = convertView.findViewById(R.id.txtDevice_Status);
            TextView Lydo = convertView.findViewById(R.id.txtReason);
            ImageView imgTB = convertView.findViewById(R.id.Device_Image);
            TextView total_position = convertView.findViewById(R.id.txtDevice_Position_Number);
            Button btnFix = convertView.findViewById(R.id.btnFix);
            Button btnXacNhan = convertView.findViewById(R.id.btnXacnhan);
            Button btnTra = convertView.findViewById(R.id.btnTra);
            Button btnOpenCTDK = convertView.findViewById(R.id.btnOpenCTDK);
            TextView txtTime = convertView.findViewById(R.id.txtTime);
            ConstraintLayout ExpandCTDK = convertView.findViewById(R.id.expand_CTDK);
            CardView cardViewCTDK = convertView.findViewById(R.id.CV_Expand_CTDK);
            ImageView imgAlert = convertView.findViewById(R.id.ImgAlert);
            //Hiển thị danh sách
            int stt = position + 1;
            total_position.setText("Thiết bị " + stt);
            //Thông tin
            FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("Thiết bị").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    TenTB.setText(moduleSV.getTenthietbi());
                                    SoLuongTB.setText(moduleSV.getSoluong());
                                    Lydo.setText(moduleSV.getLydo());
                                    StatusTB.setText(moduleSV.getTinhtrang());
                                    if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 1){
                                        date.setText(moduleSV.getDateDK());
                                        time.setText(moduleSV.getTimeDK());
                                        txtTime.setText("Thời gian đăng ký");
                                        imgAlert.setVisibility(View.VISIBLE);
                                        StatusTB.setText("ĐĂNG KÝ");
                                    }else if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 2){
                                        txtTime.setText("Thời gian mượn");
                                        date.setText(moduleSV.getDateMuon());
                                        time.setText(moduleSV.getTimeMuon());
                                        imgAlert.setVisibility(View.GONE);
                                        StatusTB.setText("ĐANG MƯỢN");
                                    }else if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 3){
                                        date.setText(moduleSV.getDateTra());
                                        time.setText(moduleSV.getTimeTra());
                                        txtTime.setText("Thời gian trả");
                                        imgAlert.setVisibility(View.GONE);
                                        StatusTB.setText("ĐÃ TRẢ");
                                    }else if(Integer.parseInt(String.valueOf(StatusTB.getText())) == 0){
                                        StatusTB.setText("TỪ CHỐI");
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

            //Xác nhận đăng ký
            FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("Thiết bị").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String keygoc = moduleSV.getId();
                            if(StatusTB.getText().toString().equals("ĐĂNG KÝ")){
                                String timeMuon = currentTime.format(calendar.getTime());
                                String dateMuon = currentDate.format(calendar.getTime());
                                String statusxacnhan = "2";
                                String statusTuchoi = "0";
                                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder xacnhan = new AlertDialog.Builder(ChiTietDangKy.this);
                                        xacnhan.setTitle("Thông báo!").setIcon(R.drawable.question);
                                        xacnhan.setMessage("Xác nhận cho mượn thiết bị?");
                                        xacnhan.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FireBaseHelper.reference.child("DanhSachDangKy")
                                                        .child(Mssv)
                                                        .child("Thiết bị")
                                                        .child(keygoc)
                                                        .child("timeMuon")
                                                        .setValue(timeMuon);
                                                FireBaseHelper.reference.child("DanhSachDangKy")
                                                        .child(Mssv)
                                                        .child("Thiết bị")
                                                        .child(keygoc)
                                                        .child("dateMuon")
                                                        .setValue(dateMuon);
                                                FireBaseHelper.reference.child("DanhSachDangKy")
                                                        .child(Mssv)
                                                        .child("Thiết bị")
                                                        .child(keygoc)
                                                        .child("tinhtrang")
                                                        .setValue(statusxacnhan);
                                            }
                                        });
                                        xacnhan.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FireBaseHelper.reference.child("DanhSachDangKy")
                                                        .child(Mssv)
                                                        .child("Thiết bị")
                                                        .child(keygoc)
                                                        .child("tinhtrang")
                                                        .setValue(statusTuchoi);
                                            }
                                        });
                                        AlertDialog dialogxacnhan = xacnhan.create();
                                        dialogxacnhan.show();
                                    }
                                });

                        }
                    else if(StatusTB.getText().toString().equals("ĐANG MƯỢN")){
                        String soluong = moduleSV.getSoluong();
                        String tenthietbi = moduleSV.getTenthietbi();
                        String dateDK = moduleSV.getDateDK();
                        String timeDK = moduleSV.getTimeDK();
                        String lydo = moduleSV.getLydo();
                        String timeTra = currentTime.format(calendar.getTime());
                        String dateMuon = moduleSV.getDateMuon();
                        String dateTra = currentDate.format(calendar.getTime());
                        String timeMuon = moduleSV.getTimeMuon();
                        ModuleSV moduleTra = new ModuleSV(Mssv, soluong, tenthietbi, dateDK, timeDK ,dateMuon, timeMuon, dateTra,timeTra, lydo, keygoc);
                        btnTra.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder xacnhan = new AlertDialog.Builder(ChiTietDangKy.this);
                                xacnhan.setTitle("Thông báo!").setIcon(R.drawable.question);
                                xacnhan.setMessage("Xác nhận trả thiết bị?");
                                xacnhan.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FireBaseHelper.reference.child("DanhSachDangKy")
                                                .child(Mssv).child("Thiết bị").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    long num;
                                                    num = snapshot.getChildrenCount();
                                                    if(num==0){
                                                        FireBaseHelper.reference.child("DanhSachDangKy")
                                                                .child(Mssv)
                                                                .child("Thiết bị")
                                                                .child(keygoc).setValue(null);
                                                        FireBaseHelper.reference.child("LichSuMuonThietBI")
                                                                .child(tenthietbi)
                                                                .child(keygoc)
                                                                .child("tenthietbi")
                                                                .setValue(TenTB);
                                                        FireBaseHelper.reference.child("LichSuMuonThietBI")
                                                                .child(tenthietbi)
                                                                .child(keygoc)
                                                                .child("keymuon")
                                                                .setValue(moduleTra);
                                                        FireBaseHelper.reference.child("DanhSachDangKy")
                                                                .child(Mssv).setValue(null);
                                                        Toast.makeText(ChiTietDangKy.this,"Đã ghi nhận trả thiết bị",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        FireBaseHelper.reference.child("DanhSachDangKy")
                                                                .child(Mssv)
                                                                .child("Thiết bị")
                                                                .child(keygoc).setValue(null);
                                                        FireBaseHelper.reference.child("LichSuMuonThietBI")
                                                                .child(tenthietbi)
                                                                .child(keygoc)
                                                                .child("tenthietbi")
                                                                .setValue(TenTB);
                                                        FireBaseHelper.reference.child("LichSuMuonThietBI")
                                                                .child(tenthietbi)
                                                                .child(keygoc)
                                                                .child("keymuon")
                                                                .setValue(moduleTra);
                                                        Toast.makeText(ChiTietDangKy.this,"Đã ghi nhận trả thiết bị",Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });
                                xacnhan.setPositiveButton("NO",null);
                                AlertDialog dialogxacnhan = xacnhan.create();
                                dialogxacnhan.show();
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //Sửa đăng ký
//            btnFix.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FireBaseHelper.reference.child("DanhSachDangKy").child(Mssv).child("Thiết bị").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("key", moduleSV.getId());
//                            Intent intent = new Intent(ChiTietDangKy.this, SuaDK.class);
//                            intent.putExtra("SuaTT", bundle);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            });
            //Hiển thị button
            String key = Menu.login;
            FireBaseHelper.reference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(key)) {
                        final String getRole = snapshot.child(key).child("role").getValue(String.class);
                        if (getRole.equals("admin")) {
                            if(StatusTB.getText() == "ĐĂNG KÝ") {
                                btnXacNhan.setVisibility(View.VISIBLE);
                            }
                                if (StatusTB.getText() == "ĐANG MƯỢN") {
                                    btnTra.setVisibility(View.VISIBLE);
                                }
                        } else{
                            btnTra.setVisibility(View.GONE);
                            btnXacNhan.setVisibility(View.GONE);
                        }


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Hiển thị button chỉnh sửa + trả
            FireBaseHelper.reference.child("DanhSachDangKy").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(key.equals(Mssv)){
                        if(StatusTB.getText() == "ĐĂNG KÝ") {
                            btnFix.setVisibility(View.VISIBLE);
                        }
                    }else{
                        btnFix.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Expand ChucNang
            ExpandCTDK.setVisibility(View.GONE);
            if(ExpandCTDK.getVisibility() == View.GONE){
                btnOpenCTDK.setBackground(getResources().getDrawable(R.drawable.ic_close));
            }
            btnOpenCTDK.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    if(ExpandCTDK.getVisibility() == View.GONE){
                        btnOpenCTDK.setBackground(getResources().getDrawable(R.drawable.ic_open));
                        TransitionManager.beginDelayedTransition(cardViewCTDK, new AutoTransition());
                        ExpandCTDK.setVisibility(View.VISIBLE);
                    }else{
                        btnOpenCTDK.setBackground(getResources().getDrawable(R.drawable.ic_close));
                        TransitionManager.beginDelayedTransition(cardViewCTDK, new AutoTransition());
                        ExpandCTDK.setVisibility(View.GONE);
                    }
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
        //Tìm kiếm thiết bị theo tên
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ModuleSV> filterTB = new ArrayList<>();
                for (ModuleSV moduleSV : dsChiTiet) {
                    if (moduleSV.getTenthietbi().toLowerCase().contains(s.toLowerCase())) {
                        filterTB.add(moduleSV);
                    }
                }
                AdapterChiTiet adapterChiTiet = new AdapterChiTiet(getApplicationContext(), 0, filterTB);
                lvChiTietDangKy.setAdapter(adapterChiTiet);
                return false;
            }
        });
    }

    private void Controls(){
        lvChiTietDangKy = findViewById(R.id.lvChiTietDangKy);
        dsChiTiet = new ArrayList<>();
        btnReload = findViewById(R.id.btnReload);
        btnBack = findViewById(R.id.btnBack);
        searchView = findViewById(R.id.search);
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