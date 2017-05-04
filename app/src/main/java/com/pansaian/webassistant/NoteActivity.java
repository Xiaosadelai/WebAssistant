package com.pansaian.webassistant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pansaian.webassistant.constant.Constant;
import com.pansaian.webassistant.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/1.
 */

public class NoteActivity extends AppCompatActivity{

    private Handler handler=new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case 0x222:
                  Toast.makeText(NoteActivity.this, "添加成功！！", Toast.LENGTH_SHORT).show();
                  Intent intent =new Intent();
                  setResult(1,intent);
                  finish();
                  break;
              case 0x221:
                  Toast.makeText(NoteActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                  break;
          }
      }
    };
    private Toolbar mToolbar;
    private EditText mEtNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);
        mEtNoteContent = (EditText) findViewById(R.id.et_note_content);
        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_note_content);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String content=mEtNoteContent.getText().toString();
                if (TextUtils.isEmpty(content)){
                    Toast.makeText(NoteActivity.this, "无数据输入！", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    String url= Constant.HTTP_ADDNOTES+content;
                    HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message msg=new Message();
                            msg.what=0x221;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message msg=new Message();
                            msg.what=0x222;
                            handler.sendMessage(msg);
                        }
                    });
                }
            default:
                break;
                }
        return  super.onOptionsItemSelected(item);
    }
}
