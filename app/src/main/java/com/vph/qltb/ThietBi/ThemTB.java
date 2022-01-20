package com.vph.qltb.ThietBi;

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
import com.vph.qltb.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThemTB extends AppCompatActivity {

    EditText ten, soluong, thongtin, hinhanh;
    ImageButton btnthem, btnBack, btnXoa,  btnUpdate, btnUp, btnDown;
    Button btnCheckImg;
    ImageView checkImg;
    FirebaseDatabase rootNode;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);
        addControls();
        addEvents();

    }


    private void addEvents() {
        //Hiện sẵn hình ảnh
        btnCheckImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CheckInImg();
            }
        });
        //Thêm thiết bị
        btnthem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ThemThietBi();
            }
        });
        //Nhập lại
        btnXoa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ten.setText("");
                soluong.setText("");
                thongtin.setText("");
                hinhanh.setText("");
                Glide.with(ThemTB.this)
                        .load(R.drawable.ic_holder)
                        .into(checkImg);
            }
        });
        //Quay lại List thiet bi
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });


        //Tăng số lượng nhấn đơn
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = soluong.getText().toString();
                if(num.isEmpty()){
                    soluong.setText("1");
                }else{
                   int plus = Integer.parseInt(num);
                   plus +=1;
                   soluong.setText(String.valueOf(plus));
                }
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = soluong.getText().toString();
                if(num.isEmpty()){
                    soluong.setText("1");
                }else{
                    int down = Integer.parseInt(num);
                    down -=1;
                    soluong.setText(String.valueOf(down));
                }
            }
        });

    }



    private void ThemThietBi(){
        reference = rootNode.getReference("DanhSachThietBi");
        //Get all the values
        String Ten = ten.getText().toString();
        String SoLuong = soluong.getText().toString();
        String ThongTin = thongtin.getText().toString();
        String HinhAnh = hinhanh.getText().toString();

        ModuleTB moduleTB = new ModuleTB(Ten, SoLuong, ThongTin,HinhAnh);


        if( Ten.isEmpty() || SoLuong.isEmpty() || ThongTin.isEmpty() ||HinhAnh.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Thêm thiết bị thành công!", Toast.LENGTH_SHORT).show();
            reference.child(String.valueOf(Ten)).setValue(moduleTB);
            ten.setText("");
            soluong.setText("");
            thongtin.setText("");
            hinhanh.setText("");
            Glide.with(ThemTB.this)
                    .load(R.drawable.ic_holder)
                    .into(checkImg);
        }
    }
    public void CheckInImg(){
        String url = hinhanh.getText().toString();
        if(url.isEmpty()){
            Toast.makeText(this,"Địa chỉ trống!",Toast.LENGTH_SHORT).show();
        }else{
            Glide.with(ThemTB.this)
                    .load(url)
                    .centerCrop()
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_error)
                    .into(checkImg);
        }
    }

    private void addControls(){
        rootNode = FirebaseDatabase.getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/");

        ten = findViewById(R.id.edtTen);
        soluong = findViewById(R.id.edtSoluong);
        thongtin = findViewById(R.id.edtThongtin);
        hinhanh = findViewById(R.id.edtNgayMuon);
        btnUpdate = findViewById(R.id.btnFix);
        btnCheckImg = findViewById(R.id.btnCheckImg);
        btnthem = findViewById(R.id.btnXacnhan);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnXoa = findViewById(R.id.btnClear);
        btnUp = findViewById(R.id.up);
        btnDown = findViewById(R.id.down);
        checkImg = findViewById(R.id.ImgReview);


    }
}