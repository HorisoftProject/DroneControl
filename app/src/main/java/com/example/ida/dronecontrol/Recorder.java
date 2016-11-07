package com.example.ida.dronecontrol;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Recorder extends AppCompatActivity {

    private MediaRecorder rec;
    private int buffsize = 0;
    AudioRecord ar = null;
    private Thread recordingThread = null;
    boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setClickable(false);

    }

    public void record(View view) {
        Button button = (Button) findViewById(R.id.button);
        button.setClickable(false);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setClickable(true);

        Chronometer chrono = (Chronometer) findViewById(R.id.chronometer1);
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();

        System.out.println("in record\n");

        System.out.println("LOL\n");

        //int rate = this.rateBuff();

       // System.err.println(rate);

        buffsize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        System.err.println(buffsize);

        this.ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffsize);

        //this.ar.release();

        System.err.println(this.ar.getState());


        this.ar.startRecording();


        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudio();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    public void stop(View view) {
        Button button = (Button) findViewById(R.id.button);
        button.setClickable(true);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setClickable(false);

        Chronometer chrono = (Chronometer) findViewById(R.id.chronometer1);
        chrono.stop();

        System.out.println("in  stop\n");

        ar.stop();
        ar.release();
        isRecording = false;
        recordingThread.interrupt();
    }

    public void writeAudio() {
        String path = "/sdcard/test.pcm";

        short sData[] = new short[buffsize/2];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        while (isRecording) {
            // gets the voice output from microphone to byte format

            ar.read(sData, 0, buffsize/2);
            Log.d("eray","Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, buffsize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f1 = new File(path);

        try {
            rawToWave(f1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private int  rateBuff() {

        for (int rate: new int[] {8000, 11025, 16000, 22050, 44100}) {
            buffsize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

            if (buffsize > 0)
                return rate;
        }

        return 0;
    }

    private void rawToWave(final File rawFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            String path = "/sdcard/test.wav";

            output = new DataOutputStream(new FileOutputStream(path));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, 44100 * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

}
