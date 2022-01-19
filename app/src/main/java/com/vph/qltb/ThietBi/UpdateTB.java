package com.vph.qltb.ThietBi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        addControls();
        addEvents();
        update();
    }

    private void update() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Update");
        ten.setText(bundle.getString("UpdateKQ"));
        String up = ten.getText().toString();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(up)) {
                    final String getTT = snapshot.child(up).child("thongtin").getValue(String.class);
                    final String getSL = snapshot.child(up).child("soluong").getValue(String.class);
                    final String getHA = snapshot.child(up).child("hinhanh").getValue(String.class);
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
        btnUpdate = findViewById(R.id.btnFix);
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
                onBackPressed();
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
        //Get all the values
        String Ten = ten.getText().toString();
        String SoLuong = soluong.getText().toString();
        String ThongTin = thongtin.getText().toString();
        String HinhAnh = hinhanh.getText().toString();

        ModuleTB moduleTB = new ModuleTB(Ten, SoLuong, ThongTin, HinhAnh);
        Toast.makeText(this, "Cập nhật thiết bị thành công!", Toast.LENGTH_SHORT).show();
        reference.child(String.valueOf(Ten)).setValue(moduleTB);
        Glide.with(UpdateTB.this)
                .load(R.drawable.ic_holder)
                .into(checkImg);

    }

    public void CheckInImg() {
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
    }
}