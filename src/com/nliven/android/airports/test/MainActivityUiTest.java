package com.nliven.android.airports.test;

import com.nliven.android.airports.R;
import com.nliven.android.airports.biz.model.Airport;
import com.nliven.android.airports.ui.AirportDetailsActivity;
import com.nliven.android.airports.ui.MainActivity;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Adapter;
import android.widget.ListView;

import static org.fest.assertions.api.Assertions.*;


/**
 * Demonstrates Integration Testing (i.e. Automated UI) using the Robotium tool
 * 
 * TODO:
 * - Capture screenshots
 * - Use MockWebServer for REST request?  Or, do we want to call 'real' web servers
 *   during Integration testing??
 *  
 * @author matthew.woolley
 *
 */
public class MainActivityUiTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    
    public MainActivityUiTest() {
        super(MainActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception {     
        super.setUp();
        
        //Initialize Robotium's "Solo" object
        solo = new Solo(getInstrumentation(), getActivity());
    }
    
    @Override
    protected void tearDown() throws Exception {                     
        //Finish all activities that have been opened during the test run
        //Do this BEFORE the super.tearDown!
        solo.finishOpenedActivities();
        
        super.tearDown();
    }
    
    public void testClickCaliforniaButton() throws Exception{  
        
        solo.waitForActivity(MainActivity.class, 6000);        
        solo.clickOnButton(getActivity().getResources().getString(R.string.button_get_cali_airports));
          
        //The 'Loading...' dialog should be up at this point.  
        //TODO: Should we still use the MockWebServer at this point?  Or, do we want to
        //      have our full end-to-end test call the real web server, etc?
        solo.waitForDialogToClose();
        
        //Get ListView and its Adapter and assert
        ListView view = (ListView) solo.getView(R.id.listAirports);
        assertThat(view).isNotNull();
        Adapter adapter = view.getAdapter();
        assertThat(adapter).isNotNull();
        assertThat(adapter.getCount()).isGreaterThan(0);
        
        //Get the FIRST item in the List
        Airport a = (Airport)adapter.getItem(0);
        assertThat(a).isNotNull();
        String ap1Name = a.getName();
        
        //Click a list item and assert the Details view
        solo.clickInList(1);        
        solo.waitForActivity(AirportDetailsActivity.class, 6000);
        solo.assertCurrentActivity("Now showing AirportDetailsActivity ", AirportDetailsActivity.class);
        assertThat(solo.waitForText(ap1Name)).isTrue();
        
        //Go back to List
        solo.goBack();
        solo.waitForActivity(MainActivity.class, 6000);
        solo.assertCurrentActivity("Back to MainActivity", MainActivity.class);
        
        // Get the SECOND item in the List
        a = (Airport)adapter.getItem(1);
        assertThat(a).isNotNull();
        assertThat(a.getName()).isNotEqualTo(ap1Name);
        
        //Click a list item and assert the Details view
        solo.clickInList(2);        
        solo.waitForActivity(AirportDetailsActivity.class, 6000);
        solo.assertCurrentActivity("Now showing AirportDetailsActivity ", AirportDetailsActivity.class);
        assertThat(solo.waitForText(a.getName(), 1, 2000)).isTrue();
        
        //Go back to List
        solo.goBack();
        solo.waitForActivity(MainActivity.class, 6000);
        solo.assertCurrentActivity("Back to MainActivity", MainActivity.class);
        
        //Scroll down
        //solo.scrollToBottom();
        //solo.wait(3000);
        
        
    }
}
