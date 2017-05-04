package com.pansaian.webassistant.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pansaian.webassistant.R;
import com.pansaian.webassistant.constant.Constant;
import com.pansaian.webassistant.entity.Notes;
import com.pansaian.webassistant.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/30.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    final Handler handler;

    public List<Notes> notesList;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView numberText,contentText,timeText;
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            numberText= (TextView) itemView.findViewById(R.id.number);
            contentText= (TextView) itemView.findViewById(R.id.content);
            timeText= (TextView) itemView.findViewById(R.id.time);
            button= (Button) itemView.findViewById(R.id.button);

        }
    }
    //用到主线程的handler,需要在Adapter构造时候传入handler参数
    public NotesAdapter(Handler handler, List<Notes> mnotesList){
        this.handler = handler;
        notesList=mnotesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Notes notes=notesList.get(position);
        holder.numberText.setText(String.valueOf(position+1));
        holder.contentText.setText(notes.getContent());
        holder.timeText.setText(notes.getTime());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int id=notesList.get(position).getId();

                String url= Constant.HTTP_DELETE+id;
                HttpUtil.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg=new Message();
                        msg.what=0x88;
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg=new Message();
                        msg.what=0x89;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
