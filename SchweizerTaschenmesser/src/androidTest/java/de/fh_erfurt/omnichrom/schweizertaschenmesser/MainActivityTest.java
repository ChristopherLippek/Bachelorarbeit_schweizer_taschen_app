package de.fh_erfurt.omnichrom.schweizertaschenmesser;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MainActivityTest
{
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(Compass.class.getName(), null, false);

    @Before
    public void setUp() throws Exception
    {
        mActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch()
    {
        View view = mActivity.findViewById(R.id.textView);

        assertNotNull(view);
    }

    @Test
    public void secondTest()
    {
        assertNotNull(mActivity.findViewById(R.id.compassImageView));

        onView(withId(R.id.compassImageView)).perform(click());
        Activity compassActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(compassActivity);
        compassActivity.finish();
    }

    @After
    public void tearDown() throws Exception
    {
        mActivity = null;
    }
}