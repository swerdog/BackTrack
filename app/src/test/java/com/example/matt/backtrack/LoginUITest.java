package com.example.matt.backtrack;

import org.junit.Test;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*

public class LoginUITest {

    public static final String LOGIN = "0";
    public static final String PASS = "0";

    Button btn_Login;
    EditText user_Name;
    EditText password;


    private val username="Monil"
    private val correct_password ="pass_word"
    private val wrong_password = "passme456"

    @Test
    public void Login() {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(LOGIN), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(PASS), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn_Login=(Button)findViewById(R.id.btnLogin);
        user_Name=(EditText)findViewById(R.id.edtUsername);
        password=(EditText)findViewById(R.id.edtPassword);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(user_Name.getText().toString(),password.getText().toString());
            }
        });
    }
    public String validate(String userName, String password)
    {
        if(userName.equals("user") && password.equals("user"))
            return "Login successful";
        else
            return "login failed!";
    }


    @Test
    fun login_failure(){
        Log.e("@Test","login failure test")
        Espresso.onView((withId(R.id.user_name)))
                .perform(ViewActions.typeText(username_tobe_typed))

        Espresso.onView(withId(R.id.password))
                .perform(ViewActions.typeText(wrong_password))

        Espresso.onView(withId(R.id.login_button))
                .perform(ViewActions.click())

        Espresso.onView(withId(R.id.login_result))
                .check(matches(withText(R.string.login_failed)))
    }


    @Test
    fun login_success(){
        Log.e("@Test","login success test")
        Espresso.onView((withId(R.id.user_name)))
                .perform(ViewActions.typeText(username))

        Espresso.onView(withId(R.id.password))
                .perform(ViewActions.typeText(correct_password))

        Espresso.onView(withId(R.id.login_button))
                .perform(ViewActions.click())

        Espresso.onView(withId(R.id.login_result))
                .check(matches(withText(R.string.login_success)))
    }




}