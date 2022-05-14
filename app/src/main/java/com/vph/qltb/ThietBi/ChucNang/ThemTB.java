package com.vph.qltb.ThietBi.ChucNang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.R;
import com.vph.qltb.ThietBi.ModuleTB;
import com.vph.qltb.ThietBi.ZoomActivity;

public class ThemTB extends AppCompatActivity {

    EditText ten, soluong, thongtin, hinhanh, role, type;
    Button btnthem, btnBack, btnXoa, btnUp, btnDown, btnOpenThem, btnCheckImg, btnZoom, btnSelect_Type, btnSelect_Role, btnCheckTen, btnReCheckTen;

    CardView cardView;
    LinearLayout expand_them;
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
        //Check trước mới được nhập
        btnCheckTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkTen();
                }

        });

        btnReCheckTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReCheckTen.setVisibility(View.GONE);
                btnCheckTen.setVisibility(View.VISIBLE);
                btnthem.setVisibility(View.GONE);
                ten.setEnabled(true);
            }
        });
        //Pick Type
        btnSelect_Type.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), btnSelect_Type);
                dropDownMenu.getMenuInflater().inflate(R.menu.pick_type, dropDownMenu.getMenu());

                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Chọn Module
                        if(menuItem.getItemId()==R.id.Module){
                            type.setText("Module");
                        }else if(menuItem.getItemId()==R.id.DienTu){
                            type.setText("Điện tử");
                        }
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });
        //Pick Role
        btnSelect_Role.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), btnSelect_Role);
                dropDownMenu.getMenuInflater().inflate(R.menu.pick_role, dropDownMenu.getMenu());

                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Chọn Module
                        if(menuItem.getItemId()==R.id.Lab){
                            role.setText("Dùng tại Lab");
                        }else if(menuItem.getItemId()==R.id.Home){
                            role.setText("Có thể mượn về");
                        }
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });
        //Zoom thiết bị
        Bundle bundle = new Bundle();
        bundle.putString("ZoomKQ", hinhanh.getText().toString());
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemTB.this, ZoomActivity.class);
                intent.putExtra("ZoomIMG", bundle);
                startActivity(intent);
            }
        });
        btnOpenThem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(expand_them .getVisibility() == View.GONE){
                    btnOpenThem.setBackground(getResources().getDrawable(R.drawable.ic_open));
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expand_them.setVisibility(View.VISIBLE);
                }else{
                    btnOpenThem.setBackground(getResources().getDrawable(R.drawable.ic_close));
                    expand_them.setVisibility(View.GONE);
                }
            }
        });
        //Xóa dữ liệu vừa nhập
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ten.setText("");
                soluong.setText("");
                thongtin.setText("");
                hinhanh.setText("");
                role.setText("");
                type.setText("");
                Glide.with(ThemTB.this)
                        .load(R.drawable.ic_holder)
                        .into(checkImg);
                btnZoom.setVisibility(View.GONE);
                btnthem.setVisibility(View.GONE);
                btnCheckTen.setVisibility(View.VISIBLE);
                btnReCheckTen.setVisibility(View.GONE);
                ten.setEnabled(true);
            }
        });
    }



    private void ThemThietBi(){
        if(isConnected()) {
            //Get all the values
            String Ten = ten.getText().toString();
            String SoLuong = soluong.getText().toString();
            String ThongTin = thongtin.getText().toString();
            String HinhAnh = hinhanh.getText().toString();
            String Role = role.getText().toString();
            String Type = type.getText().toString();
            ModuleTB moduleTB = new ModuleTB(Ten, SoLuong, ThongTin, HinhAnh, Type, Role);

            if (Ten.isEmpty() || SoLuong.isEmpty() || ThongTin.isEmpty() || HinhAnh.isEmpty() || Role.isEmpty() || Type.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                            AlertDialog.Builder builder =new AlertDialog.Builder(ThemTB.this)
                                    .setTitle("Thông báo!")
                                    .setIcon(R.drawable.question)
                                    .setMessage("Đồng ý thêm thiết bị "+Ten
                                            + "\nSố lượng: "+SoLuong
                                            +"\nThông tin: "+ThongTin
                                            +"\nLoại: " +Type
                                            +"\nThiết bị sử dụng tại " + Role)
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(ThemTB.this, "Thêm thiết bị thành công!", Toast.LENGTH_SHORT).show();
                                            FireBaseHelper.reference.child("DanhSachThietBi").child(Ten).setValue(moduleTB);
                                            ten.setText("");
                                            soluong.setText("");
                                            thongtin.setText("");
                                            hinhanh.setText("");
                                            role.setText("");
                                            type.setText("");
                                            btnZoom.setVisibility(View.GONE);
                                            btnthem.setVisibility(View.GONE);
                                            btnCheckTen.setVisibility(View.VISIBLE);
                                            btnReCheckTen.setVisibility(View.GONE);
                                            ten.setEnabled(true);
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
                btnZoom.setVisibility(View.GONE);
            } else {
                Glide.with(ThemTB.this)
                        .load(url)
                        .centerCrop()
                        .error(R.drawable.ic_error)
                        .placeholder(R.drawable.ic_error)
                        .into(checkImg);
                btnZoom.setVisibility(View.VISIBLE);
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
    public void checkTen(){
        String Ten = ten.getText().toString();
        if (Ten.equals("")) {
            Toast.makeText(ThemTB.this,"Tên thiết bị trống!",Toast.LENGTH_SHORT).show();
        } else {
            FireBaseHelper.reference.child("DanhSachThietBi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Ten)) {
                        AlertDialog.Builder tontai = new AlertDialog.Builder(ThemTB.this)
                                .setTitle("Thông báo!")
                                .setIcon(R.drawable.ic_warning)
                                .setMessage("Thiết bị đã tồn tại trong danh sách!")
                                .setNegativeButton("OK", null);
                        AlertDialog dialog = tontai.create();
                        dialog.show();
                    } else {
                        Toast.makeText(ThemTB.this, "Thiết bị có thể thêm vào!",Toast.LENGTH_SHORT).show();
                        btnReCheckTen.setVisibility(View.VISIBLE);
                        btnCheckTen.setVisibility(View.GONE);
                        btnthem.setVisibility(View.VISIBLE);
                        ten.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void addControls(){
        ten = findViewById(R.id.editDevice_Name);
        soluong = findViewById(R.id.editDeivce_Number);
        thongtin = findViewById(R.id.editDevice_Info);
        hinhanh = findViewById(R.id.editDevice_Link_Image);
        btnCheckImg = findViewById(R.id.btnCheckImg);
        btnthem = findViewById(R.id.btnXacnhan);
        btnBack = findViewById(R.id.btnBack);
        btnXoa = findViewById(R.id.btnClear);
        btnUp = findViewById(R.id.up);
        btnDown = findViewById(R.id.down);
        checkImg = findViewById(R.id.ImgReview);
        btnOpenThem = findViewById(R.id.btnOpenThem);
        cardView = findViewById(R.id.CV_Expand_Them);
        expand_them = findViewById(R.id.expand_them);
        btnZoom = findViewById(R.id.btnZoom);
        btnSelect_Role = findViewById(R.id.btnSelect_Role);
        btnSelect_Type = findViewById(R.id.btnSelect_Type);
        role = findViewById(R.id.editDeivce_Role);
        type = findViewById(R.id.editDeivce_Type);
        btnCheckTen = findViewById(R.id.btnCheckTen);
        btnReCheckTen = findViewById(R.id.btnReCheckTen);
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