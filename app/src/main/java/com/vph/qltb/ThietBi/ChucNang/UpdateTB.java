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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vph.qltb.FireBaseHelper;
import com.vph.qltb.R;
import com.vph.qltb.ThietBi.ModuleTB;
import com.vph.qltb.ThietBi.ZoomActivity;

public class UpdateTB extends AppCompatActivity {

    EditText ten, soluong, thongtin, hinhanh, role, type;
    Button btnXacNhan, btnBack, btnXoa, btnUp, btnDown, btnOpenThem, btnCheckImg, btnSelect_Type, btnSelect_Role, btnCheckTen;
    CardView cardView;
    TextView txtThem;
    LinearLayout expand_them;
    ImageView checkImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);
        update();
        addControls();
        addEvents();

    }

    private void update() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Update");
        String key = bundle.getString("UpdateKQ");

        FireBaseHelper.reference.child("DanhSachThietBi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(String.valueOf(key))) {
                    final String getTT = snapshot.child(String.valueOf(key)).child("thongtin").getValue(String.class);
                    final String getSL = snapshot.child(String.valueOf(key)).child("soluong").getValue(String.class);
                    final String getHA = snapshot.child(String.valueOf(key)).child("hinhanh").getValue(String.class);
                    final String getRole = snapshot.child(String.valueOf(key)).child("role").getValue(String.class);
                    final String getType = snapshot.child(String.valueOf(key)).child("loai").getValue(String.class);
                    ten.setText(key);
                    thongtin.setText(getTT);
                    soluong.setText(getSL);
                    hinhanh.setText(getHA);
                    role.setText(getRole);
                    type.setText(getType);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addControls() {
        btnCheckTen = findViewById(R.id.btnCheckTen);
        txtThem = findViewById(R.id.txtThem);
        ten = findViewById(R.id.editDevice_Name);
        soluong = findViewById(R.id.editDeivce_Number);
        thongtin = findViewById(R.id.editDevice_Info);
        hinhanh = findViewById(R.id.editDevice_Link_Image);
        btnCheckImg = findViewById(R.id.btnCheckImg);
        btnXacNhan = findViewById(R.id.btnXacnhan);
        btnBack = findViewById(R.id.btnBack);
        btnXoa = findViewById(R.id.btnClear);
        btnUp = findViewById(R.id.up);
        btnDown = findViewById(R.id.down);
        checkImg = findViewById(R.id.ImgReview);
        btnOpenThem = findViewById(R.id.btnOpenThem);
        cardView = findViewById(R.id.CV_Expand_Them);
        expand_them = findViewById(R.id.expand_them);
        btnSelect_Role = findViewById(R.id.btnSelect_Role);
        btnSelect_Type = findViewById(R.id.btnSelect_Type);
        role = findViewById(R.id.editDeivce_Role);
        type = findViewById(R.id.editDeivce_Type);
    }

    private void addEvents() {
        btnCheckTen.setVisibility(View.GONE);
        btnXacNhan.setVisibility(View.VISIBLE);
        //set tên
        txtThem.setText("Chỉnh sửa thiết bị");
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
        //Pick Type
        btnSelect_Type.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), btnSelect_Type);
                dropDownMenu.getMenuInflater().inflate(R.menu.pick_type, dropDownMenu.getMenu());

                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Chọn ModuleTB
                        if(menuItem.getItemId()==R.id.Module){
                            type.setText("ModuleTB");
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
                        //Chọn ModuleTB
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
        //Chỉnh sửa thiết bị
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
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
                soluong.setText("");
                thongtin.setText("");
                hinhanh.setText("");
                role.setText("");
                type.setText("");
                Glide.with(UpdateTB.this)
                        .load(R.drawable.ic_holder)
                        .into(checkImg);
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
            String Role = role.getText().toString();
            String Type = type.getText().toString();
            ModuleTB moduleTB = new ModuleTB(Ten, SoLuong, ThongTin, HinhAnh, Type, Role);


            if (Ten.isEmpty() || SoLuong.isEmpty() || ThongTin.isEmpty() || HinhAnh.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTB.this)
                        .setIcon(R.drawable.question)
                        .setTitle("Thông báo!")
                        .setMessage("Xác nhận chỉnh sửa " + Ten
                                + "\nSố lượng: "+SoLuong
                                +"\nThông tin: "+ThongTin
                                +"\nLoại: " +Type
                                +"\nThiết bị " + Role)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FireBaseHelper.reference.child("DanhSachThietBi").child(String.valueOf(key)).setValue(moduleTB);
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
                Glide.with(UpdateTB.this)
                        .load(url)
                        .centerCrop()
                        .error(R.drawable.ic_error)
                        .placeholder(R.drawable.ic_error)
                        .into(checkImg);

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