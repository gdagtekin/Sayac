package com.gd.sayac;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    Button btn, sifirla,ayar;
    private int sayac=0;
    SharedPreferences sharedPreferences,ayarlara;
    SharedPreferences.Editor editor;
    RelativeLayout arkaplan;
    private Boolean ses_durumu, titresim_durumu;
    private MediaPlayer ses;
    private Vibrator titresim;
    private AdView adView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         btn=(Button)findViewById(R.id.button);
        sifirla=(Button)findViewById(R.id.sifirla);
        ayar=(Button)findViewById(R.id.ayar);
        arkaplan=(RelativeLayout)findViewById(R.id.rl);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlara= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        reklamiYukle();
        ayarlariYukle();
        ses=MediaPlayer.create(getApplicationContext(), R.raw.ses);
        titresim=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        //SoundPool ses efekleri için

        sayac=sharedPreferences.getInt("sayac",0);
        btn.setText(String.valueOf(sayac));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ses_durumu){
                    ses.start();
                }
                if(titresim_durumu){
                    titresim.vibrate(250);
                }
                sayac++;
                btn.setText(String.valueOf(sayac));

            }
        });
        sifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Sayaçı sıfırlamaktan emin misiniz?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sayac=0;
                        btn.setText(String.valueOf(sayac));
                        Toast.makeText(MainActivity.this, "Sayaç sıfırlandı.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "Sayaç sıfırlanmadı.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });
        ayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(getApplicationContext(),ayarlar.class);
                startActivity(ıntent);
            }
        });


    }

    private void sifirlaMetod() {

        editor.putInt("sayac",0);
        editor.commit();
    }

    private void reklamiYukle() {
        adView=new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.reklam_kimligi));

        LinearLayout layout=(LinearLayout)findViewById(R.id.reklam);
        layout.addView(adView);

        AdRequest adRequest=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        adView.loadAd(adRequest);

    }

    private void ayarlariYukle() {
        String pos=ayarlara.getString("arkaplan","0");
        switch (Integer.valueOf(pos))
        {
            case 0:
                arkaplan.setBackgroundColor(getResources().getColor(R.color.anarenk));
                btn.setBackgroundResource(R.drawable.buton_selector);
                break;
            case 1:
                arkaplan.setBackgroundColor(getResources().getColor(R.color.gri));
                btn.setBackgroundResource(R.drawable.buton2selector);
                break;
            case 2:
                arkaplan.setBackgroundColor(getResources().getColor(R.color.yesil));
                btn.setBackgroundResource(R.drawable.buton3selector);
                break;
            case 3:
                arkaplan.setBackgroundColor(getResources().getColor(R.color.mor));
                break;
            case 4:
                arkaplan.setBackgroundResource(R.drawable.background);
                break;

        }
        ses_durumu=ayarlara.getBoolean("ses",false);
        titresim_durumu=ayarlara.getBoolean("titresim",false);
        ayarlara.registerOnSharedPreferenceChangeListener(MainActivity.this);

    }

    @Override
    protected void onPause() {
        editor=sharedPreferences.edit();
        editor.putInt("sayac",sayac);
        editor.commit();

        super.onPause();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ayarlariYukle();
    }
}
