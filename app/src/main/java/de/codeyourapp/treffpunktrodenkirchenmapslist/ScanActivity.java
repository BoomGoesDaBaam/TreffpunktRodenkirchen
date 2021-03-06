package de.codeyourapp.treffpunktrodenkirchenmapslist;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.google.zxing.Result;
//https://androidhiro.com/source/android/example/code-scanner/5610
public class ScanActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[] { Manifest.permission.CAMERA }, 1);
        }

        CodeScannerView scannerView = findViewById(R.id.scannerView);

// Use builder

        mCodeScanner = CodeScanner.builder()

                /*camera can be specified by calling .camera(cameraId),

               first back-facing camera on the device by default*/

                /*code formats*/

                .formats(CodeScanner.ALL_FORMATS)/*List<BarcodeFormat>*/

                /*or .formats(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX, ...)*/

                /*or .format(BarcodeFormat.QR_CODE) - only one format*/

                /*auto focus*/

                .autoFocus(true).autoFocusMode(AutoFocusMode.SAFE).autoFocusInterval(2000L)

                /*flash*/

                .flash(false)

                /*decode callback*/

                .onDecoded(new DecodeCallback() {

                               @Override

                               public void onDecoded(@NonNull final Result result) {

                                   runOnUiThread(new Runnable() {

                                                     @Override

                                                     public void run() {
                                                         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
                                                         startActivity(browserIntent);
                                                         //Toast.makeText(ScanActivity.this, result.getText(),Toast.LENGTH_LONG).show();

                                                    }


                                                 }
                                   );

                               }


                           }
                )

                /*error callback*/

                .onError(new ErrorCallback() {

                             @Override

                             public void onError(@NonNull final Exception error) {

                                 runOnUiThread(new Runnable() {

                                                   @Override

                                                   public void run() {

                                                       Toast.makeText(ScanActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();


                                                   }


                                               }
                                 );

                             }


                         }
                ).build(this, scannerView);

// Or use constructor to create scanner with default parameters

// All parameters can be changed after scanner created

// mCodeScanner = new CodeScanner(this, scannerView);



// mCodeScanner.setDecodeCallback(...);



        scannerView.setOnClickListener(new View.OnClickListener() {

                                           @Override

                                           public void onClick(View view) {

                                               mCodeScanner.startPreview();


                                           }

                                       }
        );


    }

    @Override
    protected void onResume() {

        super.onResume();

        mCodeScanner.startPreview();


    }

    @Override
    protected void onPause() {

        mCodeScanner.releaseResources();

        super.onPause();


    }

}