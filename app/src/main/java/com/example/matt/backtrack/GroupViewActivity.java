package com.example.matt.backtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
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

    private Button btn;
    private EditText et;
    boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        check_id = false;
        Button button = (Button) findViewById(R.id.groupMapButton);




        btn = (Button) findViewById(R.id.buttonDelete);
        //btn.setEnabled(false);

        et = (EditText) findViewById(R.id.groupMapText);
        et.addTextChangedListener(loginTextWatcher);




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
                groups.setText("Groups \n");
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




    private TextWatcher loginTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String usernameInput = et.getText().toString().trim();
            //String passwordInput = editGroupName.getText().toString().trim();

            btn.setEnabled(!usernameInput.isEmpty());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //btn.setText("Deleted");
                    mAuth = FirebaseAuth.getInstance();

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");


                    Query dummyQuery = myRef.child(mAuth.getCurrentUser().getUid()).child("groups");
                    System.out.println("xoxoxo " + dummyQuery);
                    dummyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dummySnapshot: dataSnapshot.getChildren()) {
                                String deleteSnapshot = dummySnapshot.getValue().toString();

                                if(deleteSnapshot.equals(usernameInput))
                                {
                                    flag=true;
                                    dummySnapshot.getRef().removeValue();
                                    Toast.makeText(GroupViewActivity.this, "Exited Group Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    GroupViewActivity.this.finish();
                                }


                            }
                            if(flag==false) {
                                Toast.makeText(GroupViewActivity.this, "Enter Valid Group ID",
                                        Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
//                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });



//                    database.child("users").orderByKey().equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {


//                    DatabaseReference myRef = database.getReference("users");

//                    System.out.println("xoxoxo" + myRef.child(mAuth.getCurrentUser().getUid()).child("groups").);

//                    myRef.child(mAuth.getCurrentUser().getUid()).child("groups")



                }
            });

            ///////////////To do


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };




}
