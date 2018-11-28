package com.example.matt.backtrack;

import org.junit.Test;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class LoginUITest {

    public static final String LOGIN = "0";
    public static final String PASS = "0";


    @Test
    public void Login() {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(LOGIN), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(PASS), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

    }
}