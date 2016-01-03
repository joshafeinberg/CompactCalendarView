package sundeepk.github.com.sample;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private CompactCalendarView.CompactCalendarViewListener listener;
    private CompactCalendarView compactCalendarView;
    private MainActivity activity;

    public ApplicationTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
        listener = mock(CompactCalendarView.CompactCalendarViewListener.class);
        compactCalendarView = (CompactCalendarView) activity.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setListener(listener);
    }

    @Test
    public void testOnMonthScrollListenerIsCalled(){
        //Sun, 08 Feb 2015 00:00:00 GMT
        setDate(new Date(1423353600000L));
        onView(ViewMatchers.withId(R.id.compactcalendar_view)).perform(scroll(100, 100, -600, 0));

        //Sun, 01 Mar 2015 00:00:00 GMT - expected
        verify(listener).onMonthScroll(new Date(1425168000000L));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testOnDayClickListenerIsCalled(){
        //Sun, 08 Feb 2015 00:00:00 GMT
        setDate(new Date(1423353600000L));
        onView(ViewMatchers.withId(R.id.compactcalendar_view)).perform(clickXY(300, 300));

        //Tue, 03 Feb 2015 00:00:00 GMT - expected
        verify(listener).onDayClick(new Date(1422921600000L));
        verifyNoMoreInteractions(listener);
    }

    private void setDate(final Date date) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                compactCalendarView.setCurrentDate(date);
            }
        });
    }

    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    public static ViewAction scroll(final int startX, final int startY, final int endX, final int endY){
        return new GeneralSwipeAction(
                Swipe.FAST,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + startX;
                        final float screenY = screenPos[1] + startY;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + endX;
                        final float screenY = screenPos[1] + endY;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

}