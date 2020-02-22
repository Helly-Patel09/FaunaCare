package com.vahapps.faunacare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GvcEntry extends AppCompatActivity {
    private EditText name,gvc;
    private Button btnGvc;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gvc_entry);
        name= (EditText) findViewById(R.id.vetName);
        //name.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        Log.d("VAH name1->",name.toString());
        Log.d("VAH name2->",name.getText().toString());
        gvc= (EditText) findViewById(R.id.GvcNo);
        //gvc.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        btnGvc= (Button) findViewById(R.id.btnGvcSubmit);
        myref=FirebaseDatabase.getInstance().getReference("Gvc");

        btnGvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = myref.push().getKey();
                myref.child(id).child("GvcNumber").setValue(gvc.getText().toString().trim());

                Log.d("VAH name3->",name.getText().toString());
                myref.child(id).child("VetName").setValue(name.getText().toString().trim());

                Log.d("VAH ngvc->",gvc.getText().toString());
                Toast.makeText(getApplicationContext(),"Gvc-> "+gvc.getText().toString() +"name-> "+name.getText().toString(), Toast.LENGTH_LONG).show();
                name.setText("");
                gvc.setText("");

                myref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });


    }
}

