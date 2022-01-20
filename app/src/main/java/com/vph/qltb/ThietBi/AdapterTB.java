package com.vph.qltb.ThietBi;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vph.qltb.Menu.MenuLoginMSSV;
import com.vph.qltb.Menu.MenuLoginScan;
import com.vph.qltb.R;
import com.vph.qltb.SinhVien.DangKy.PhieuDangKy_Bundle;

import java.util.List;

public class AdapterTB extends ArrayAdapter {


    Context context;

    public AdapterTB(@NonNull Context context, int resource, @NonNull List<ModuleTB> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ModuleTB moduleTB = (ModuleTB) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.design_thietbi, parent, false);
        }

        TextView STT = convertView.findViewById(R.id.stttb);
        TextView Ten = convertView.findViewById(R.id.tentb);
        TextView SL = convertView.findViewById(R.id.soluongtb);
        TextView TT = convertView.findViewById(R.id.thongtintb);
        ImageView img = convertView.findViewById(R.id.HinhAnhtb);

        //Tự tăng STT
        int stt = position + 1;
        STT.setText(String.valueOf(stt));
        //Hiển thị danh sách
        Ten.setText(moduleTB.getTen());
        SL.setText(moduleTB.getSoluong());
        TT.setText(moduleTB.getThongtin());
        Picasso.get().load(moduleTB.getHinhanh())
                .placeholder(R.drawable.ic_holder)
                .error(R.drawable.ic_error)
                .into(img);
        //Xóa
        ImageButton Xoa = convertView.findViewById(R.id.btnXoaTB);
        ThietBi.reference
                .child(String.valueOf(moduleTB.getTen()))
                .orderByChild(String.valueOf(stt))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Xoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Cảnh báo!").setIcon(R.drawable.ic_error);
                                builder.setMessage("Bạn có chắc muốn xóa thiết bị này?");
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (DataSnapshot delst : snapshot.getChildren()) {
                                            delst.getRef().setValue(null);
                                            Toast.makeText(context, "Thiết bị đã được xóa!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
        //Update
        ImageButton Update = convertView.findViewById(R.id.btnFix);

        ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Bundle bundle = new Bundle();
                       bundle.putString("UpdateKQ", moduleTB.getTen());
                       if (snapshot.hasChild(moduleTB.getTen())) {
                           Intent intent = new Intent(context, UpdateTB.class);
                           intent.putExtra("Update", bundle);
                           context.startActivity(intent);
                      }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Mượn Tb trực tiếp
        Button Muon = convertView.findViewById(R.id.btnMuon);
        ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Muon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("KQMuon", moduleTB.getTen());
                        if (snapshot.hasChild(moduleTB.getTen())) {
                            Intent intent = new Intent(context, PhieuDangKy_Bundle.class);
                            intent.putExtra("Muon", bundle);
                            context.startActivity(intent);
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Chức năng Zoom
        ImageButton Zoom = convertView.findViewById(R.id.Big);
        String HinhAnh = moduleTB.getHinhanh();
        Bundle bundle = new Bundle();
        bundle.putString("ZoomKQ", HinhAnh);
        ThietBi.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Kiểm tra nếu MSSV tồn tại

                Zoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                Intent intent = new Intent(context, ZoomActivity.class);
                                intent.putExtra("ZoomIMG", bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Kiểm tra Role
        if (MenuLoginScan.scan == null) {
            String check = MenuLoginMSSV.login.getText().toString();
            DatabaseReference reference= FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DanhSachSinhVien");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Role = snapshot.child(check).child("role").getValue(String.class);
                        if (Role.equals("admin")) {
                            Xoa.setVisibility(View.VISIBLE);
                            Update.setVisibility(View.VISIBLE);
                        }
                        else{
                            Xoa.setVisibility(View.GONE);
                            Update.setVisibility(View.GONE);
                        }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            String check = MenuLoginScan.scan.getText().toString();
            DatabaseReference reference= FirebaseDatabase
                    .getInstance("https://quanlythietbi-b258e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DanhSachSinhVien");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Role = snapshot.child(check).child("role").getValue(String.class);
                    if (Role.equals("admin")) {
                        Xoa.setVisibility(View.VISIBLE);
                        Update.setVisibility(View.VISIBLE);
                    }
                    else{
                        Xoa.setVisibility(View.GONE);
                        Update.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }


        return convertView;
    }



}





