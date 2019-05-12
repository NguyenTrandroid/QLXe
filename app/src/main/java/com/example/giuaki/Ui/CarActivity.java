package com.example.giuaki;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.giuaki.Adapters.CarAdapter;
import com.example.giuaki.Database.Database;
import com.example.giuaki.Models.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarActivity extends AppCompatActivity {
    CarAdapter carAdapter;
    List<Car> cars;
    Database database;
    String maloai;
    Dialog dialogAddCar;
    Dialog dialogDel;
    Dialog dialogEdit;
    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.cv_Add)
    CardView cvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carr);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        database = new Database(this, "QLXE.sqlite", null, 1);
        cars = new ArrayList<>();
        rvCar.setLayoutManager(new GridLayoutManager(this, 1));
        Intent intent = getIntent();
        maloai = intent.getStringExtra("MaLoai");
        getListCar(maloai);
        carAdapter = new CarAdapter(cars, CarActivity.this);
        rvCar.setAdapter(carAdapter);


    }

    public void getListCar(String maloai) {

        Cursor cursor = database.GetData("SELECT * FROM XE WHERE MaLoai='" + maloai + "'");
        cars.clear();
        while (cursor.moveToNext()) {
            cars.add(new Car(cursor.getString(0), cursor.getString(4), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        Log.d("AAA", cars.size() + "");
        // ok test thử xem sao
        // ok v là dc r
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.cv_Add)
    public void onViewClicked() {
        dialogAddCar = new Dialog(Objects.requireNonNull(CarActivity.this));
        dialogAddCar.setContentView(R.layout.dialog_add_car);
        Objects.requireNonNull(dialogAddCar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button button = dialogAddCar.findViewById(R.id.bt_add);
        Button button1 = dialogAddCar.findViewById(R.id.bt_cc);
        final EditText edName = dialogAddCar.findViewById(R.id.edt_name);
        final EditText edDt = dialogAddCar.findViewById(R.id.edt_Dt);
        final EditText edPrice = dialogAddCar.findViewById(R.id.edt_pirce);
        dialogAddCar.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.QueryData("INSERT INTO XE VALUES(null,'" + edName.getText().toString() + "','" + edDt.getText().toString() + "','" + edPrice.getText().toString() + "','" + maloai + "')");
                getListCar(maloai);
                carAdapter.notifyDataSetChanged();
                dialogAddCar.dismiss();


            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddCar.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                dialogEdit = new Dialog(Objects.requireNonNull(CarActivity.this));
                dialogEdit.setContentView(R.layout.dialog_edit_car);
                Objects.requireNonNull(dialogEdit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button2 = dialogEdit.findViewById(R.id.bt_add);
                Button button3 = dialogEdit.findViewById(R.id.bt_cc);
                final EditText edName = dialogEdit.findViewById(R.id.edt_name);
                final EditText edDt = dialogEdit.findViewById(R.id.edt_Dt);
                final EditText edPrice = dialogEdit.findViewById(R.id.edt_pirce);
                edName.setText(cars.get(item.getGroupId()).getTenXe());
                edDt.setText(cars.get(item.getGroupId()).getDungTich());
                edPrice.setText(cars.get(item.getGroupId()).getDonGia());
                dialogEdit.show();
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("UPDATE XE SET TenXe = '" + edName.getText().toString() + "',DungTich='" + edDt.getText().toString() + "',DonGia='" + edPrice.getText().toString() + "' WHERE MaXe = '" + cars.get(item.getGroupId()).getMaXe() + "'");
                        getListCar(maloai);
                        carAdapter.notifyDataSetChanged();
                        dialogEdit.dismiss();


                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEdit.dismiss();
                    }
                });


                break;
            case 2:
                dialogDel = new Dialog(Objects.requireNonNull(CarActivity.this));
                dialogDel.setContentView(R.layout.dialog_del);
                Objects.requireNonNull(dialogDel.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button button = dialogDel.findViewById(R.id.bt_add);
                Button button1 = dialogDel.findViewById(R.id.bt_cc);
                dialogDel.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.QueryData("DELETE FROM XE WHERE MaXe='" + cars.get(item.getGroupId()).getMaXe() + "'");
                        getListCar(maloai);
                        carAdapter.notifyDataSetChanged();
                        dialogDel.dismiss();


                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDel.dismiss();
                    }
                });


                break;

        }
        return super.onContextItemSelected(item);
    }
}
