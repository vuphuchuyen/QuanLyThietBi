package com.vph.qltb.ThietBi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;

import java.util.HashMap;
import java.util.Map;

public class UpdateTB extends AppCompatActivity {

    EditText ten, soluong, thongtin, hinhanh;
    ImageButton btnthem, btnBack, btnUpdate, btnUp, btnDown;
    Button btnCheckImg;
    ImageView checkImg;
    FirebaseDatabase rootNode;
    DatabaseReference reference = FirebaseDatabase
            .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("DanhSachThietBi");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tb);
        update();
        addControls();
        addEvents();

    }

    private void update() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Update");
        String key = bundle.getString("UpdateKQ");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(String.valueOf(key))) {
                    final String getTen = snapshot.child(String.valueOf(key)).child("ten").getValue(String.class);
                    final String getTT = snapshot.child(String.valueOf(key)).child("thongtin").getValue(String.class);
                    final String getSL = snapshot.child(String.valueOf(key)).child("soluong").getValue(String.class);
                    final String getHA = snapshot.child(String.valueOf(key)).child("hinhanh").getValue(String.class);
                    ten.setText(getTen);
                    thongtin.setText(getTT);
                    soluong.setText(getSL);
                    hinhanh.setText(getHA);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addControls() {


        ten = findViewById(R.id.edtTen);
        soluong = findViewById(R.id.edtSoluong);
        thongtin = findViewById(R.id.edtThongtin);
        hinhanh = findViewById(R.id.edtNgayMuon);
        btnCheckImg = findViewById(R.id.btnCheckImg);
        btnthem = findViewById(R.id.btnXacnhan);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnUp = findViewById(R.id.up);
        btnDown = findViewById(R.id.down);
        checkImg = findViewById(R.id.ImgReview);
    }

    private void addEvents() {
        //Tăng số lượng
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = soluong.getText().toString();
                if (num.isEmpty()) {
                    soluong.setText("1");
                } else {
                    int plus = Integer.parseInt(num);
                    plus += 1;
                    soluong.setText(String.valueOf(plus));
                }
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = soluong.getText().toString();
                if (num.isEmpty()) {
                    soluong.setText("1");
                } else {
                    int down = Integer.parseInt(num);
                    down -= 1;
                    soluong.setText(String.valueOf(down));
                }
            }
        });
        //Quay lại List thiet bi
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Chỉnh sửa thiết bị
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateThietBi();
            }
        });
        //Hiện sẵn hình ảnh
        btnCheckImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInImg();
            }
        });
    }

    private void UpdateThietBi() {
        if(isConnected()) {
            //Get all the values
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("Update");
            String key = bundle.getString("UpdateKQ");
            String Ten = ten.getText().toString();
            String SoLuong = soluong.getText().toString();
            String ThongTin = thongtin.getText().toString();
            String HinhAnh = hinhanh.getText().toString();
            String Loai = "Điện tử";
            ModuleTB moduleTB = new ModuleTB(Ten, SoLuong, ThongTin, HinhAnh, Loai, key);

            if (Ten.isEmpty() || SoLuong.isEmpty() || ThongTin.isEmpty() || HinhAnh.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTB.this)
                        .setIcon(R.drawable.question)
                        .setTitle("Thông báo!")
                        .setMessage("Xác nhận chỉnh sửa"
                                    +"Tên thiết bị: "+Ten
                                    +"Số lượng: "+SoLuong
                                    +"Thông tin: " +ThongTin)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference.child(String.valueOf(key)).setValue(moduleTB);
                                Toast.makeText(UpdateTB.this, "Cập nhật thiết bị thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setPositiveButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTB.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void CheckInImg() {
        if (isConnected()) {
            String url = hinhanh.getText().toString();
            if (url.isEmpty()) {
                Toast.makeText(this, "Địa chỉ trống!", Toast.LENGTH_SHORT).show();
            } else {
                Glide.with(UpdateTB.this)
                        .load(url)
                        .centerCrop()
                        .error(R.drawable.ic_error)
                        .placeholder(R.drawable.ic_error)
                        .into(checkImg);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTB.this);
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