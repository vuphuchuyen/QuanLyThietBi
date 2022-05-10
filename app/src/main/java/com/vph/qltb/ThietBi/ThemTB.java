package com.vph.qltb.ThietBi;

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
import com.squareup.picasso.Picasso;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.ChucNang.PhieuDangKy;

public class ThemTB extends AppCompatActivity {

    EditText ten, soluong, thongtin, hinhanh;
    ImageButton btnthem, btnBack, btnXoa,  btnUpdate, btnUp, btnDown;
    Button btnCheckImg;
    ImageView checkImg;




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
        if(isConnected()) {
            //Get all the values
            String key = FireBaseHelper.reference.push().getKey();
            String Ten = ten.getText().toString();
            String SoLuong = soluong.getText().toString();
            String ThongTin = thongtin.getText().toString();
            String HinhAnh = hinhanh.getText().toString();
            String Loai = "";
            ModuleTB moduleTB = new ModuleTB(Ten, SoLuong, ThongTin, HinhAnh, Loai, key);

            if (Ten.isEmpty() || SoLuong.isEmpty() || ThongTin.isEmpty() || HinhAnh.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder =new AlertDialog.Builder(ThemTB.this)
                        .setTitle("Thông báo!")
                        .setIcon(R.drawable.question)
                        .setMessage("Đồng ý thêm thiết bị "+Ten
                            + "\nSố lượng: "+SoLuong
                            +"\nThông tin: "+ThongTin)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ThemTB.this, "Thêm thiết bị thành công!", Toast.LENGTH_SHORT).show();
                                FireBaseHelper.reference.child(key).setValue(moduleTB);
                                ten.setText("");
                                soluong.setText("");
                                thongtin.setText("");
                                hinhanh.setText("");
                                Glide.with(ThemTB.this)
                                        .load(R.drawable.ic_holder)
                                        .into(checkImg);
                            }
                        }).setPositiveButton("NO", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemTB.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    public void CheckInImg(){
        if(isConnected()) {
            String url = hinhanh.getText().toString();
            if (url.isEmpty()) {
                Toast.makeText(this, "Địa chỉ trống!", Toast.LENGTH_SHORT).show();
            } else {
                Glide.with(ThemTB.this)
                        .load(url)
                        .centerCrop()
                        .error(R.drawable.ic_error)
                        .placeholder(R.drawable.ic_error)
                        .into(checkImg);
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemTB.this);
            builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_wifiout);
            builder.setMessage("Vui lòng kiểm tra lại kết nối Internet của bạn!");
            builder.setNegativeButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void addControls(){
        ten = findViewById(R.id.edtTen);
        soluong = findViewById(R.id.edtSoluong);
        thongtin = findViewById(R.id.edtThongtin);
        hinhanh = findViewById(R.id.edtNgayMuon);
        btnCheckImg = findViewById(R.id.btnCheckImg);
        btnthem = findViewById(R.id.btnXacnhan);
        btnBack = findViewById(R.id.btnQuaylaiMenu);
        btnXoa = findViewById(R.id.btnClear);
        btnUp = findViewById(R.id.up);
        btnDown = findViewById(R.id.down);
        checkImg = findViewById(R.id.ImgReview);
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