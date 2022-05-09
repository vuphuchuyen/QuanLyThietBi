package com.vph.qltb.SinhVien.DanhSach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.vph.qltb.Menu.Menu;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ModuleSV;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DsMuon extends AppCompatActivity {

    ListView lvNguoiMuon;
    ArrayList<ModuleSV> dsMuon;
    ImageButton btnRestartMN, btnBack;
    SearchView searchView;
    TextView DSM;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsach_vn_muon);
        addControls();
        addEvents();
        Hide();
        hienthiDanhSach();
    }
    public class AdapterM extends ArrayAdapter<ModuleSV> {

        Context context;

        public AdapterM(@NonNull Context context, int resource, @NonNull List<ModuleSV> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ModuleSV moduleSV = getItem(position);

            if(convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_ds_muon, parent, false);
            }
            TextView STT = (TextView) convertView.findViewById(R.id.STTNM);

            TextView SVM = (TextView) convertView.findViewById(R.id.SinhVienM);
            TextView SDT = (TextView) convertView.findViewById(R.id.SDTM);
            TextView mssvM = (TextView) convertView.findViewById(R.id.MSSVM);
            TextView slM = (TextView) convertView.findViewById(R.id.SLM);
            TextView tenthietbiM = (TextView) convertView.findViewById(R.id.TenThietBiM);
            TextView ngayMuon = (TextView) convertView.findViewById(R.id.NgayMuon);
            TextView hanTra = (TextView) convertView.findViewById(R.id.HanTra);
            TextView lop = (TextView) convertView.findViewById(R.id.Lop) ;
            ImageButton Accept = convertView.findViewById(R.id.btnAccept);
            //CheckBox Check = (CheckBox) convertView.findViewById(R.id.CheckNM);

            //Tự tăng STT
            int stt = position+1;
            STT.setText(String.valueOf(stt));
            //Hiển thị danh sách
            SVM.setText(moduleSV.getSinhvien());
            SDT.setText(moduleSV.getSdt());
            lop.setText(moduleSV.getLop());
            mssvM.setText(moduleSV.getMssv());
            slM.setText(moduleSV.getSoluong());
            tenthietbiM.setText(moduleSV.getTenthietbi());
            ngayMuon.setText(moduleSV.getNgaymuon());
            hanTra.setText(moduleSV.getHantra());
            ImageButton btnXoa = convertView.findViewById(R.id.btnXoaSinhVienM);
            //Xóa
//
//            DatabaseReference reference = FirebaseDatabase
//                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                    .getReference("DanhSachMuon");
//            reference.child(String.valueOf(moduleSV.getId()))
//                    .orderByChild(String.valueOf(stt))
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            btnXoa.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(DsMuon.this);
//                                    builder.setTitle("Thông báo!").setIcon(R.drawable.question);
//                                    builder.setMessage("Sinh viên "
//                                            + moduleSV.getSinhvien()
//                                            + " đã trả thiết bị "
//                                            + moduleSV.getTenthietbi()
//                                            + " phải không?");
//                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            for (DataSnapshot dsdk : snapshot.getChildren()) {
//                                                dsdk.getRef().removeValue();
//                                                Toast.makeText(DsMuon.this, "Đã xóa sinh viên khỏi danh sách!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                                    builder.setPositiveButton("NO", null);
//                                    AlertDialog dialog = builder.create();
//                                    dialog.show();
//                                }
//                            });
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error){
//                        }
//                    });
            //Kiểm tra role
            //Trả thiết bị -> lịch sử
            String key = moduleSV.getId();
            DatabaseReference reference = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DanhSachMuon");
            DatabaseReference refLichSu = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("LichSuMuonThietBi");

            reference.child(String.valueOf(moduleSV.getId()))
                    .orderByChild(String.valueOf(stt))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            btnXoa.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DsMuon.this);
                                    builder.setTitle("Thông báo!").setIcon(R.drawable.question);
                                    builder.setMessage("Sinh viên  "
                                            + moduleSV.getSinhvien() + " đã trả thiết bị "
                                            + moduleSV.getTenthietbi() + " ?");
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final String getSV = moduleSV.getSinhvien();
                                            final String getSDT = moduleSV.getSdt();
                                            final String getLop = moduleSV.getLop();
                                            final String getMssvM = moduleSV.getMssv();
                                            final String getSL = moduleSV.getSoluong();
                                            final String getTenTB = moduleSV.getTenthietbi();
                                            final String getNgayMuon = moduleSV.getNgaymuon();
                                            final String getHanTra = moduleSV.getHantra();
                                            final String getLydo = moduleSV.getLydo();
                                            final String getTinhTrang = "Đã trả";
                                            ModuleSV moduleSV = new ModuleSV(getSV, getLop, getSDT, getMssvM, getSL, getTenTB, getNgayMuon, getHanTra, getTinhTrang, getLydo, key);
                                            refLichSu.child(key).setValue(moduleSV);
                                            for (DataSnapshot dsdk : snapshot.getChildren()) {
                                                dsdk.getRef().removeValue();
                                            }
                                            Toast.makeText(DsMuon.this, "Sinh viên đã trả thiết bị!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setPositiveButton("NO",null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            DatabaseReference refSV = FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Account");
                String check = Menu.login.getText().toString();
                refSV.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            btnXoa.setVisibility(View.VISIBLE);
                        } else {
                            btnXoa.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            return convertView;
        }

    }
    private void Hide() {
        //Kiểm tra role
        DatabaseReference refSV = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Account");
            String check = Menu.login.getText().toString();
            refSV.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Role = snapshot.child(check).child("role").getValue(String.class);
                    if (Role.equals("admin")) {
                        DSM.setVisibility(View.VISIBLE);
                    } else {
                        DSM.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
    private void hienthiDanhSach() {
        reference = FirebaseDatabase
                .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("DanhSachMuon");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsMuon.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModuleSV moduleSV = ds.getValue(ModuleSV.class);
                    dsMuon.add(moduleSV);
                }
                AdapterM adapter = new AdapterM(DsMuon.this,R.layout.design_ds_muon,dsMuon);
                lvNguoiMuon.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        btnRestartMN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Restart();
            }
        });
        //Quay lại Menu
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
        //Tìm kiếm sinh viên
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ModuleSV> filterMN = new ArrayList<>();
                for(ModuleSV moduleSV : dsMuon){
                    if(moduleSV.getSinhvien().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getMssv().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getTenthietbi().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getSdt().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }else if(moduleSV.getLop().toLowerCase().contains(s.toLowerCase())){
                        filterMN.add(moduleSV);
                    }

                }
                AdapterM adapterM = new AdapterM(getApplicationContext(),0,filterMN);
                lvNguoiMuon.setAdapter(adapterM);
                return false;
            }
        });
    }



    private void addControls() {
        searchView = findViewById(R.id.search);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnRestartMN = findViewById(R.id.btnRestartNM);
        lvNguoiMuon = findViewById(R.id.lvNguoiMuon);
        dsMuon = new ArrayList<>();
        DSM = findViewById(R.id.DSDKXoa);
    }



    public void Restart(){
        recreate();
        Toast.makeText(this,"Trang đã được tải lại",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isConnected()) {

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DsMuon.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(DsMuon.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(DsMuon.this);
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