package com.example.matt.backtrack;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextView txt;
    Button butn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt = (TextView) findViewById(R.id.genIDtxt);
        butn = (Button) findViewById(R.id.genIdbutton);

        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("Some hashed ID");
            }
        });

        final Button createGrpButton = (Button) findViewById(R.id.buttonCreate);
        createGrpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGrpButton.setText("Group Created");
                joinButtonClickedMethod();
            }
        });
    }

    private void joinButtonClickedMethod() {

        EditText edit = (EditText)findViewById(R.id.idGroup);
        String result = edit.getText().toString();

        Log.w("my clickable", result);


        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(mAuth.getCurrentUser().getUid()).child("groups").child(result).setValue("true");




    }

}
