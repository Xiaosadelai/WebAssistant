package com.pansaian.webassistant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pansaian.webassistant.entity.Notes;
import com.pansaian.webassistant.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity" ;
    public  TextView text;


    public Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if (msg.what==0x123){
                String data=msg.getData().getString("data");
                List<Notes> notesList=parseJsonData(data);
                for (Notes notes:notesList){
                    System.out.println(notes.getId()+notes.getContent()+notes.getTime()+notes.getStatus());
                }
            }
            if(msg.what==0x122){
                Toast.makeText(MainActivity.this, "网络出差了，稍后再试！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text= (TextView) findViewById(R.id.text);
        initToolbar();
        initData();

    }

    private void initData() {
        HttpUtil.sendOkHttpRequest("http://192.168.0.103:8080/myweb/json.html", new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what=0x122;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                System.out.println(responseData);
                Bundle bundle=new Bundle();
                bundle.putString("data",responseData);
                Message message= new Message();
                message.what=0x123;
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    private List<Notes> parseJsonData(String responseData) {
        Gson gson=new Gson();
        List<Notes> notesList=gson.fromJson(responseData,new TypeToken<List<Notes>>(){}.getType());
        return  notesList;
    }

    private void initToolbar() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notes");
    }
}
