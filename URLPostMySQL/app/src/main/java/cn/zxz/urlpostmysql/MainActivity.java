package cn.zxz.urlpostmysql;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText name1;                    //申明一个输入姓名的编辑框对象
    private EditText sex1;                   //申明一个输入性别的编辑框对象
    private EditText age1;                   //申明一个输入年龄的编辑框对象
    private Button btn1;                    //申明一个“保存”按钮对象
    private Handler handler;                //申明一个Handler对象
    private String result = "";                //申明一个代表显示内容的字符串
    private TextView resultTV;                //申明一个显示结果的文本框对象

    //	public void reset(View view){
//		;
//	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name1 = (EditText) findViewById(R.id.name);            //获取输入姓名的EditText组件
        sex1 = (EditText) findViewById(R.id.sex);            //获取输入性别的EditText组件
        age1 = (EditText) findViewById(R.id.age);                //获取输入年龄的EditText组件
        btn1 = (Button) findViewById(R.id.btn);               //获取“保存”按钮组件
        resultTV = (TextView) findViewById(R.id.resultTV);
        //为按钮添加点击事件监听器
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(name1.getText().toString())) {
                    Toast.makeText(MainActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //创建一个新线程，用于发送并读取个人信息
                new Thread(new Runnable() {
                    public void run() {
                        send();
                        Message m = handler.obtainMessage();    //获取一个Message对象
                        handler.sendMessage(m);                //向UI线程发送消息
                    }
                }).start();                                    //开启线程
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (result != null) {
                    Toast.makeText(MainActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();

                    name1.setText("");                            //清空姓名编辑框
                    sex1.setText("");                            //清空性别编辑框
                    age1.setText("");                           //清空年龄编辑框
                    resultTV.setText(result); //输出添加的信息
                }
                super.handleMessage(msg);
            }
        };

    }

    public void send() {
        String target = "http://192.168.31.84:8080/index.jsp";//要提交的目标地址 ip为主机的ip地址
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();                        //创建一个HTTP连接
            urlConn.setRequestMethod("POST");                //指定使用POST请求方式
            urlConn.setDoInput(true);                        //向连接中写入数据
            urlConn.setDoOutput(true);                        //从连接中读取数据
            urlConn.setUseCaches(false);                    //禁止缓存
            urlConn.setInstanceFollowRedirects(true);        //自动执行HTTP重定向
            urlConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");    //设置内容类型
            DataOutputStream out = new DataOutputStream(
                    urlConn.getOutputStream());            //获取输出流
            //连接要提交的数据
            String param = "name="
                    + URLEncoder.encode(name1.getText().toString(), "utf-8")
                    + "&sex=" + URLEncoder.encode(sex1.getText().toString(), "utf-8")
                    + "&age=" + URLEncoder.encode(age1.getText().toString(), "utf-8");
            out.writeBytes(param);                            //将要传递的数据写入数据输出流
            out.flush();                                    //输出缓存
            out.close();                                    //关闭数据输出流
            //判断是否响应成功
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(
                        urlConn.getInputStream());                //获得读取的内容
                BufferedReader buffer = new BufferedReader(in); //获取输入流对象
                String inputLine = null;
                while ((inputLine = buffer.readLine()) != null) {
                    result += inputLine + "\n";
                }
                in.close();                                        //关闭字符输入流
            }
            urlConn.disconnect();                                //断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
