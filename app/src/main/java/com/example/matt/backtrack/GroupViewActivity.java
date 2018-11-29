package com.example.matt.backtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupViewActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private Boolean check_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        check_id = false;
        Button button = (Button) findViewById(R.id.groupMapButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textfield = (EditText) findViewById(R.id.groupMapText);
                final String groupID = textfield.getText().toString();
                System.out.println("***************" + groupID + "*************");
                final Intent intent = new Intent(GroupViewActivity.this, MapsActivity.class);


                Query a = FirebaseDatabase.getInstance().getReference("groups");
                a.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                String groupId1 = (String) issue.getValue();
                                String groupId = "" + groupId1;
                                if (groupId.equals(groupID)) {
                                    check_id = true;
                                    break;
                                }
                            }
                            if(!check_id)
                            {
                                Toast.makeText(GroupViewActivity.this, "Group Doesnt Exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (check_id) {
                                intent.putExtra("EXTRA_GROUP_ID", groupID);
                                startActivity(intent);
                                check_id = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Database Error", databaseError.toString());
                    }
                });





            }
        });

        final TextView groups = (TextView) findViewById(R.id.groupsText);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groups.setText("");
                if (dataSnapshot.exists()) {
                    DataSnapshot ds2 = dataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).child("groups");
                    for (DataSnapshot snap : ds2.getChildren()) {
                        String group_name = snap.getKey();
                        String group_uuid = snap.getValue(String.class);
                        groups.setText(groups.getText() + "\n" + group_name + "  :  " + group_uuid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
}
