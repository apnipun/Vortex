package com.example.apn.vortex;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatForEvent extends AppCompatActivity {

    private EditText inputMSG;
    ChatAdapter adapter;

    ListView listView;
    List<ChatModel> list_chat = new ArrayList<>();

    FloatingActionButton btnSend;
    private String chatMsg,chatUserName;
    private String userName,groupName,temp_key,chatUserEmail,chatDateAndTime;
    private DatabaseReference root;
    ChatModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_for_event);

        listView = (ListView) findViewById(R.id.messageList);
        btnSend = (FloatingActionButton) findViewById(R.id.fab);
        inputMSG = (EditText) findViewById(R.id.message);


        groupName = getIntent().getExtras().get("eventName").toString();
        userName = getIntent().getExtras().get("UserName").toString();
        setTitle("Group Chat for "+groupName+ " Event");

        root = FirebaseDatabase.getInstance().getReference(groupName);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference messageRoot = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String ,Object>();
                map2.put("name",userName);
                map2.put("message",inputMSG.getText().toString());
                map2.put("oemail","abc@gmail.com");
                map2.put("timeSent", DateFormat.getDateTimeInstance().format(new Date()));

                messageRoot.updateChildren(map2);
                inputMSG.setText("");

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void appendChatConversation(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){
            chatMsg = (String) ((DataSnapshot)i.next()).getValue();
            chatUserName = (String) ((DataSnapshot)i.next()).getValue();
            chatUserEmail = (String) ((DataSnapshot)i.next()).getValue();
            chatDateAndTime = (String) ((DataSnapshot)i.next()).getValue();


            String message = chatUserName+" ->> "+chatMsg;

            if(userName.toString().equals(chatUserName.toString())){
                chatModel  = new ChatModel(chatMsg,true);
                list_chat.add(chatModel);
            }else {
                chatModel = new ChatModel(message,false);
                list_chat.add(chatModel);
            }

        }

        adapter = new ChatAdapter(list_chat,ChatForEvent.this);
        listView.setAdapter(adapter);
    }
}
