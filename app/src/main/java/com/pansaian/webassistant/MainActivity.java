package com.pansaian.webassistant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pansaian.webassistant.adapter.NotesAdapter;
import com.pansaian.webassistant.entity.Notes;
import com.pansaian.webassistant.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity" ;
    public  TextView text;
    FloatingActionButton fab;
    public RecyclerView recyclerView;
    private   Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if (msg.what==0x123){
                String data=msg.getData().getString("data");
                List<Notes> notesList=parseJsonData(data);
                NotesAdapter adapter=new NotesAdapter(handler, notesList);
                recyclerView.setAdapter(adapter);
            }
            if(msg.what==0x122){
                Toast.makeText(MainActivity.this, "网络出差了，稍后再试！", Toast.LENGTH_SHORT).show();
            }
            if(msg.what==0x89){
                Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                initData();
            }
            if(msg.what==0x88){
                Toast.makeText(MainActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                initData();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        text= (TextView) findViewById(R.id.text);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        fab= (FloatingActionButton) findViewById(R.id.button_add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NoteActivity.class);
                startActivityForResult(intent,1);
            }
        });
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            System.out.println("11111111");
            initData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fresh_a:
                Toast.makeText(this, "dddd", Toast.LENGTH_SHORT).show();
                initData();
                break;
            default:
        }
        return false;
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
