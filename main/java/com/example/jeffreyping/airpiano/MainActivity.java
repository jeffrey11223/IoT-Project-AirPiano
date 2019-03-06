package com.example.jeffreyping.airpiano;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button a,b,c,d,e, left, right, left1, right1, expand1, expand2, expand3, expand4;

    String address = null, name = null;
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-0805F9B34FB");

    SeekBar seekBar;
    int pcut = 0;
    int exp = 0;
    float div = 100/33;
    String[] all = new String[38];

    String na[] = {"C","D", "E", "F", "G", "A", "B"};

    private SoundPool soundPool;
    //private int sound_a, sound_b, sound_c, sound_d, sound_e;
    int sounds[] = new int[38];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i < 38; i++){
            all[i] = na[i%7] + Integer.toString(i/7);
        }

        a = (Button) findViewById(R.id.a);
        b = (Button) findViewById(R.id.b);
        c = (Button) findViewById(R.id.c);
        d = (Button) findViewById(R.id.d);
        e = (Button) findViewById(R.id.e);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        left1 = (Button) findViewById(R.id.left1);
        right1 = (Button) findViewById(R.id.right1);
        expand1 = (Button) findViewById(R.id.expand1);
        expand2 = (Button) findViewById(R.id.expand2);
        expand3 = (Button) findViewById(R.id.expand3);
        expand4 = (Button) findViewById(R.id.expand4);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        for(int i = 0; i < 38; i++){
            int file = getResources().getIdentifier( "note" + Integer.toString(i + 1), "raw", getPackageName());
            sounds[i] = soundPool.load(this, file, 1);
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sounds[pcut], 1, 1, 0, 0, 1);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sounds[pcut+1], 1, 1, 0, 0, 1);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sounds[pcut+2], 1, 1, 0, 0, 1);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sounds[pcut+3], 1, 1, 0, 0, 1);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sounds[pcut+ 4 + exp], 1, 1, 0, 0, 1);
            }
        });


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pcut = (int) (progress/div);
                Log.i("MainActivity", Integer.toString(pcut));
                a.setText(all[pcut]);
                b.setText(all[pcut + 1]);
                c.setText(all[pcut + 2]);
                d.setText(all[pcut + 3]);
                e.setText(all[pcut + 4]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int after = seekBar.getProgress() - 15;
                seekBar.setProgress(after>0?after:0);
            }
        });
        right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int after = seekBar.getProgress() + 15;
                seekBar.setProgress(after<100?after:100);
            }
        });

        left1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int after = seekBar.getProgress() - 3;
                seekBar.setProgress(after>0?after:0);
            }
        });
        right1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int after = seekBar.getProgress() + 3;
                seekBar.setProgress(after<100?after:100);
            }
        });

        expand1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    exp = 1;
                    e.setText(all[pcut + 4 + exp]);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    exp = 0;
                    e.setText(all[pcut + 4]);
                }
                return false;
            }
        });

        expand2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    exp = 2;
                    e.setText(all[pcut + 4 + exp]);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    exp = 0;
                    e.setText(all[pcut + 4]);
                }
                return false;
            }
        });
        expand3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    exp = 3;
                    e.setText(all[pcut + 4 + exp]);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    exp = 0;
                    e.setText(all[pcut + 4]);
                }
                return false;
            }
        });
        expand4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    exp = 4;
                    e.setText(all[pcut + 4 + exp]);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    exp = 0;
                    e.setText(all[pcut + 4]);
                }
                return false;
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
        }

        pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice bt : pairedDevices){
                address = bt.getAddress().toString();
                name = bt.getName().toString();
            }
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            TextView t2 = findViewById(R.id.textView2);
            t2.setText("Name: " + name + ", Address: " + address);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            bluetoothSocket.connect();
        } catch (IOException e1) {
            Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
        }

        update u = new update(bluetoothSocket);
        u.start();

    }

    public class update extends Thread{

        private final BluetoothSocket mbs;
        private final InputStream mis;
        private String temp;

        public update (BluetoothSocket bluetoothSocket){
            mbs = bluetoothSocket;
            InputStream tis = null;
            try{
                tis = mbs.getInputStream();
            }catch(IOException e){}
            mis = tis;

            temp = "";
        }

        public void run(){
            boolean[] flag = {true,true,true,true,true};
            while(true) {
                byte[] buffer = new byte[1024];
                int bytes = 1024;
                int cut = 0;
                final TextView t = findViewById(R.id.textView);
                try {
                    bytes = mis.read(buffer);
                } catch (IOException e) {}
                final String readMessage = new String(buffer, 0, bytes);
                temp += readMessage;
                //Log.i("MainActivity", temp);
                for(int i = 0; i < temp.length(); i++){
                    if(temp.charAt(i) == ';'){
                        final String value = temp.substring(cut,i);
                        cut = i + 1;

                        String[] v = value.split(",");
                        for(int j = 0; j < 5; j++){
                            int force = Integer.parseInt(v[j]);

                            if(force > 50 && flag[j]){
                                flag[j] = false;
                                float vol = (float) ((force - 20)/ 200.0);
                                if(vol > 1) vol = 1;
                                int play = j;
                                if(j == 4) play += exp;
                                soundPool.play(sounds[pcut + play], vol, vol, 0, 0, 1);
                                Log.i("MainActivity", Float.toString(vol));
                            }else if(v[j].length() <= 2) flag[j] = true;

                        }
                        continue;
                    }
                }
                temp = temp.substring(cut);

            }
        }
    }
}
