package com.example.user.medical6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    EditText etxtContent;
    TextView txvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnLogout = (Button)findViewById(R.id.LogoutBtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnSubscribe = (Button)findViewById(R.id.SubscribeBtn);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ProfileActivity.this,SubscribeActivity.class);
                startActivity(intent);
            }
        });

        etxtContent = (EditText)findViewById(R.id.etxtContent);
        txvInfo = (TextView) findViewById(R.id.txvInfo);

        Button btnMyID = (Button)findViewById(R.id.MyIDBtn);
        btnMyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScanQRCode(); //掃描QR Code的函數
            }
        });
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //取得intent回傳結果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents() == null)
                txvInfo.setText("取消QR Code讀取作業");
            else{
                String resultStr=result.getContents();
                String UrlLocation = "https://dev.cims.tw/csis/createPatient.do?token=5C3i49C0g1M55O9l5cFNg5lm58lI"; //API位置
                String[] arrayData = resultStr.split(",");
                String PostData = "{\"protocolId\" : \"1032\" ,\"subjectId\" : \"A" +arrayData[0]+"\" ,\"lastName\" : \""+arrayData[1]+"\" , \"guid\" : \""+arrayData[2]+"\"";
                etxtContent.setText(PostData);

//                HttpURLConnection conn = null;
//                StringBuilder sb = new StringBuilder();
//                try
//                {
//                    URL Url = new URL(UrlLocation);
//                    conn = (HttpURLConnection)Url.openConnection();
//                    conn.setRequestMethod("POST"); //要呼意的方式 Get Or Post
//                    conn.setDoInput(true);
//                    conn.setDoOutput(true);
//                    conn.connect();
//                    //開始傳輸資料過去給API
//                    OutputStream Output = conn.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(
//                            new OutputStreamWriter(Output, "UTF-8"));
//                    writer.write(PostData);
//                    writer.flush();
//                    writer.close();
//                    Output.close();
//
////                    //讀取API回傳的值
////                    BufferedReader br = new BufferedReader(new InputStreamReader(
////                            conn.getInputStream(),"utf-8"));
////
////                    String line;
////                    while ((line=br.readLine())!=null)
////                    {
////                        sb.append(line);
////                    }
//                }
//                catch(Exception ex)
//                {
//                    Log.e("API_Post",ex.getMessage());
//                }
//                finally
//                {
//                    if (conn != null)
//                        conn.disconnect();
//                }
//
//                //return sb.toString();
//                txvInfo.setText(sb.toString());

                txvInfo.setText("QR Code讀取完成!");
                }
        }
        else
            super.onActivityResult(resultCode,resultCode,data); //預設父親類別執行指令
    }

    private void doScanQRCode() //產生QR Code的函數
    {
        IntentIntegrator integrator = new IntentIntegrator(this); //產生intent整合物件
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES); //設定條碼格式
        integrator.setPrompt("掃描QR Code"); //設定掃描時提示文字
        integrator.setCameraId(0);//設定使用後相機讀取QR Code
        integrator.setBeepEnabled(true);//提示音
        integrator.setBarcodeImageEnabled(false);//設定掃描圖檔不要存檔，掃完即丟掉
        integrator.setOrientationLocked(false);
        integrator.initiateScan();//起到掃描功能
    }
}
