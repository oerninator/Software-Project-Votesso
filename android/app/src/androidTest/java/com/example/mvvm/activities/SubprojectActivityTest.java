package com.example.mvvm.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.mvvm.R;
import com.example.mvvm.viewmodel.AppViewModel;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
//Die tests sind ein bisschen fehlerhaft, funktionieren nicht jedes mal, gerne mehrmals testen
public class SubprojectActivityTest {

    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recylerViewID)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    //Dieser Test kann nur einmalig benutzt werden, da man nur einmal einen Vote abgeben kann
    @Test
    public void bewertungAbgebenUndÜberprüfenObAngekommen() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recycler_view_subprojects)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        ViewInteraction viewInteraction = onView(withId(R.id.text_view_thumb_up_ID));

        int currentThumsUp = AppViewModel.getVoteUpCount(SubprojectActivity.getSid());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.text_view_thumb_up_ID)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(currentThumsUp + 1, Integer.parseInt(getText(viewInteraction)));
    }

    @Test
    public void kommentareVerfassenUndSichtbarUndTeilprojekteSichtbar() {
        onView(withId(R.id.recycler_view_subprojects)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.editTextTextMultiLine)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.editTextTextMultiLine)).perform(ViewActions.typeText("Kommentar vom UI Test"));

        Espresso.closeSoftKeyboard();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.button_comment_ID)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.button_tostart_comments)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Kommentar vom UI Test")).check(matches(isDisplayed()));
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
