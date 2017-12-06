package com.example.crystallai.test;

/**
 * Created by johnlcy on 2/28/2017.
 */

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import java.io.IOException;

import bluetoothUtility.ConnectionHandler;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    private String inst;
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        TextView textView = (TextView) view;
        inst = textView.getText().toString();
        switch (inst) {
            case "Move from A to B":
                try {
                    ConnectionHandler.getInstance().sendBytes(("A").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Move from B to A":
                try {
                    ConnectionHandler.getInstance().sendBytes(("B").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Move from C to D":
                try {
                    ConnectionHandler.getInstance().sendBytes(("C").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Move from D to C":
                try {
                    ConnectionHandler.getInstance().sendBytes(("D").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Agitate A":
                try {
                    ConnectionHandler.getInstance().sendBytes(("E").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Agitate B":
                try {
                    ConnectionHandler.getInstance().sendBytes(("F").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Agitate C":
                try {
                    ConnectionHandler.getInstance().sendBytes(("G").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Agitate D":
                try {
                    ConnectionHandler.getInstance().sendBytes(("H").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Demo Motion":
                try {
                    ConnectionHandler.getInstance().sendBytes(("I").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Calibrate Motion":
                try {
                    ConnectionHandler.getInstance().sendBytes(("J").getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
