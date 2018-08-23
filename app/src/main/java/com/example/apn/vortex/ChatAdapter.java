package com.example.apn.vortex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

/**
 * Created by APN on 1/21/2018.
 */

public class ChatAdapter extends BaseAdapter{
    private List<ChatModel> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public ChatAdapter(List<ChatModel> list, Context context){
        this.list = list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2 = view;
        if(view2 == null){
            if(list.get(i).isSend){
                view2 = layoutInflater.inflate(R.layout.list_item_message_send,null);
            }
            else {
                view2 = layoutInflater.inflate(R.layout.list_item_message_recieve,null);
            }

            BubbleTextView text_message = (BubbleTextView) view2.findViewById(R.id.text_message);
            text_message.setText(list.get(i).message);
        }

        return view2;
    }
}
