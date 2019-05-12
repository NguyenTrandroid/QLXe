package com.example.giuaki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.giuaki.Database.Database;
import com.example.giuaki.Viewpager.OnPageSelect;
import com.example.giuaki.Viewpager.PagerAdapter;
import com.example.giuaki.Viewpager.ZoomOutPageTransformer;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnPageSelect {
    Database database;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.vp_pager)
    ViewPager vpPager;
    @BindView(R.id.dots_indicator)
    WormDotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        database = new Database(this, "QLXE.sqlite", null, 1);
        init();
        createTable();
        //        database.QueryData("INSERT INTO XE VALUES(null,'Future neo fi','120cc','36.000.000','XM')");
        //        database.QueryData("INSERT INTO XE VALUES(null,'Honda Air Blade 2019','120cc','46.000.000','XM')");
        //        database.QueryData("INSERT INTO XE VALUES(null,'BMW i8','500cc','4.600.000.000','XOT')");
        //        database.QueryData("INSERT INTO XE VALUES(null,'Kawasaki Z1000','400cc','399.000.000','XMT')");
        //        database.QueryData("INSERT INTO XE VALUES(null,'ANBICO 1705','100cc','10.300.000','XD')");

    }

    private void init() {
        PagerAdapter adapter = new PagerAdapter();
        vpPager.setAdapter(adapter);
        vpPager.setPageTransformer(true, new ZoomOutPageTransformer());
        dotsIndicator.setViewPager(vpPager);

    }

    private void createTable() {
        database.QueryData("CREATE TABLE IF NOT EXISTS LOAIXE(MaLoai VARCHAR(10) PRIMARY KEY , TenLoai VARCHAR(100), XuatXu VARCHAR(50))");
        database.QueryData("CREATE TABLE IF NOT EXISTS XE(MaXe INTEGER PRIMARY KEY AUTOINCREMENT , TenXe VARCHAR(100), DungTich VARCHAR(50), DonGia VARCHAR(50),MaLoai VACHAR(10))");
        database.QueryData("CREATE TABLE IF NOT EXISTS DONDATHANG(MADDH INTEGER PRIMARY KEY AUTOINCREMENT  , NgayLap DATETIME )");
        database.QueryData("CREATE TABLE IF NOT EXISTS CTDONDATHANG(MADDH INTEGER, MaXe INTEGER, SoLuong INTEGER,PRIMARY KEY(MADDH,MaXe))");

    }

    @Override
    public void sendPageSelect(int page) {
        switch (page) {
            case 0:
                startActivity(new Intent(MainActivity.this, CatalogActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, BillActivity
                        .class));
                break;
        }

    }
}
