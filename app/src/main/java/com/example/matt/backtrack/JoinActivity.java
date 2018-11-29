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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase;

    private Button joinButton;

    Boolean checker = true;

    private String ggg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        joinButton = (Button) findViewById(R.id.join_button);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinButtonClickedMethod();
            }
        });

    }

    private void joinButtonClickedMethod() {

        EditText edit = (EditText) findViewById(R.id.join_id_text_input);
        final String result = edit.getText().toString();

        Log.w("myclickable", result);

//        Query firebaseSearchQuery = mUserDatabase.orderByChild("groups").startAt(result).endAt(result + "\uf8ff");
//
//        String a = firebaseSearchQuery.toString();

        joinButton = (Button) findViewById(R.id.join_button);


        Query a = FirebaseDatabase.getInstance().getReference("groups");

        a.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("myclickable2", result);
                if (dataSnapshot.exists()) {
                    Log.w("myclickable3", result);

                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"

                        Log.w("myclickable45", issue.toString());

                        Log.w("value", issue.getValue().toString());
                        String toCheck = issue.getValue().toString();
                        Log.w("mycontainable", toCheck);
                        Log.w("mycontainable", result);

                        String groupName = issue.getKey();
                        long groupId1 = (long) issue.getValue();
                        String groupId = "" + groupId1;

//                        Toast.makeText(JoinActivity.this, groupId + " " + result,
//                                Toast.LENGTH_SHORT).show();

                        if (groupId.contains(result)) {
                            checker = false;
                            Log.w("mycontainable1", toCheck);
                            mAuth = FirebaseAuth.getInstance();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            myRef.child(mAuth.getCurrentUser().getUid()).child("groups").child(groupName).setValue(result);
                            Log.w("mycontainable12", toCheck);

                            Toast.makeText(JoinActivity.this, "Group Joined",
                                    Toast.LENGTH_SHORT).show();

                            joinButton.setEnabled(false);

                            JoinActivity.this.finish();


                        }


                    }


                    if (checker) {
                        Toast.makeText(JoinActivity.this, "No Such Group Exists, Please Try Again",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Database Error", databaseError.toString());
            }
        });

//        Log.w("showmesomething",a);

//        mAuth = FirebaseAuth.getInstance();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("users");
//                myRef.child(mAuth.getCurrentUser().getUid()).child("groups").child(result).setValue("true");


    }

}
