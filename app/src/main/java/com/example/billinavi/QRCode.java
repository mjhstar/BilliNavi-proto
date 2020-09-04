package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRCode extends AppCompatActivity {
    /* private boolean isFlashOn=false;
        private Button btFlash;*/
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        mtoolbar = findViewById(R.id.sub_toolbar);
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        //btFlash=findViewById(R.id.bt_flash);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
        //toolbar설정하기
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*btFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFlashOn){
                    barcodeScannerView.setTorchOff();
                }else {
                    barcodeScannerView.setTorchOn();
                }
            }
        }); 플래시 키고 끄는거, 혹시 필요할까봐^^*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                finish();
                return true; //메니페스트 수정할거있슴다
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*얘도 플래시 키고 끄는거~@Override
        public void onTorchOn() {
            btFlash.setText("플래시끄기");
            isFlashOn=true;
        }

        @Override
        public void onTorchOff() {
            btFlash.setText("플래시켜기");
            isFlashOn=false;
        }*/
    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

}

