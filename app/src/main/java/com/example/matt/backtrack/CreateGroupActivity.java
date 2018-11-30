package com.example.matt.backtrack;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateGroupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextView txt;
    Button butn;
    Button createGrpButton;
    boolean click = false;
    private Button buttonConfirm;
    private EditText editGroupName;
    Button copy_button;
    int n;
    String s="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //buttonConfirm.setEnabled(false);

        editGroupName = findViewById(R.id.idGroup);
        //editGroupName.setEnabled(false);
        buttonConfirm = (Button)findViewById(R.id.buttonCreate);
        buttonConfirm.setEnabled(false);

        txt = (TextView) findViewById(R.id.genIDtxt);
        butn = (Button) findViewById(R.id.genIdbutton);


        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = true;
                Random r = new Random();
                String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                n = 100000 + r.nextInt(900000);
                String text="";
                s = String.valueOf(n);
                for (int i = 0; i < 10; i++) {
                    text+=(possible.charAt(r.nextInt(possible.length())));
                }
                s = s+text;
                txt.setText(s);
                butn.setEnabled(false);

            }
        });

        EditText edit = findViewById(R.id.idGroup);
        final String result = edit.getText().toString().trim();

        //result.addTextChangedListener(loginTextWatcher);

        createGrpButton = (Button) findViewById(R.id.buttonCreate);
        editGroupName.addTextChangedListener(loginTextWatcher);



        createGrpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(click==true&&result!=""){
                    createGrpButton.setText("Group Created");
                    joinButtonClickedMethod();
                    //Intent i = new Intent(CreateGroupActivity.this, activity_second.class);
                    //startActivity(i);
                    Toast.makeText(CreateGroupActivity.this, "Group Created",
                            Toast.LENGTH_SHORT).show();
                    CreateGroupActivity.this.finish();
                }

                else {
                    createGrpButton.setText("Generate ID first");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createGrpButton.setText("Create Group");//Do something after 100ms
                        }
                    }, 2000);
                }


            }

        });

        copy_button =(Button) findViewById(R.id.copybutton);
       // if (n==0) copy_button.setEnabled(false);
        //else {
            copy_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(click==true) {
                        String copy= txt.getText().toString();
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboardManager.setText(copy);
                        copy_button.setText("id copied");
                    }
                    else {
                        copy_button.setText("Generate ID first");
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                copy_button.setText("Copy id");//Do something after 100ms
                            }
                        }, 2000);
                    }


                }
            });
        //}


    }

    private TextWatcher loginTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = editGroupName.getText().toString().trim();
            //String passwordInput = editGroupName.getText().toString().trim();

            createGrpButton.setEnabled(!usernameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void joinButtonClickedMethod() {


        //EditText usernameEditText = (EditText) findViewById(R.id.idGroup);


        EditText edit = (EditText)findViewById(R.id.idGroup);
        final String result = edit.getText().toString();

        //TextView txt = (TextView) findViewById(R.id.genIDtxt);



        //Log.w("result123456",result);




        Log.w("my clickable", result);


        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRefUsers = database.getReference("users");
        DatabaseReference myRefGroups = database.getReference("groups");

        String g = ""+s;
        myRefUsers.child(mAuth.getCurrentUser().getUid()).child("groups").child(result).setValue(g);
        myRefGroups.child(result).setValue(g);





    }

}
