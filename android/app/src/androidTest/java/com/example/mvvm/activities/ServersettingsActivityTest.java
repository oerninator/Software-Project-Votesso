package com.example.mvvm.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.mvvm.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

//@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class ServersettingsActivityTest {

    private ActivityScenario<ServersettingsActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(ServersettingsActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void testeSpeicherFunktion() {

        onView(withId(R.id.textInputAdressID)).perform(click());

        onView(withId(R.id.textInputAdressID)).perform(ViewActions.typeText("http://mocky"));
        onView(withId(R.id.speichernButtonID)).perform(click());

        ViewInteraction viewInteraction1 = onView(withId(R.id.currentBackendAdressID));
        assertEquals("Die aktuell gespeicherte Adresse ist: \nhttp://mocky", getText(viewInteraction1));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.defaultButtonID)).perform(click());

        ViewInteraction viewInteraction2 = onView(withId(R.id.currentBackendAdressID));
        assertEquals("Die aktuell gespeicherte Adresse ist: \nhttp://134.245.1.240:1100/", getText(viewInteraction2));

    }

    public String getText(ViewInteraction matcher) {
        final String[] text = {""};

        matcher.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Gibt den textinhalt einer textview zurück.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                text[0] = (String) textView.getText();
            }
        });

        return text[0];
    }

}