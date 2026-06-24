package com.example.principal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PrincipalActivityInstrumentedTest {

    @Before
    public void setUp() {
        // Setea el rol y el usuario en SharedPreferences antes de lanzar la Activity
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("rol", "Administrador")
                .putString("user", "Diany")
                .apply();

        Intents.init();

    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testActivityLaunchesAndViewsVisible() {
        ActivityScenario.launch(PrincipalActivity.class);
        onView(withId(R.id.recordatorio)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.AddBluetooth)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.Moduser)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.AddParticipante)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.idconfiguracion)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.descargarPdf)).check(matches(ViewMatchers.isDisplayed()));
    }
}
