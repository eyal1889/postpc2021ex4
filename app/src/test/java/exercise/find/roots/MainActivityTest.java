package exercise.find.roots;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

  @Test
  public void when_activityIsLaunching_then_theButtonShouldStartDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_activityIsLaunching_then_theEditTextShouldStartEmpty(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "input" edit-text has no text
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    String input = inputEditText.getText().toString();
    assertTrue(input == null || input.isEmpty());
  }

  @Test
  public void when_userIsEnteringNumberInput_and_noCalculationAlreadyHappned_then_theButtonShouldBeEnabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("11");
    assertTrue(button.isEnabled());
  }
  //tests:
  @Test
  public void when_activityLaunches_progressHidden(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
    assertFalse(progressBar.isShown());
    //Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
  }
  @Test
  public void when_userIsEnteringBadNumberInput_then_theButtonShouldBeEnabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("0");
    assertFalse(button.isEnabled());
  }

  // to mock a click on the button:
  //    call `button.performClick()`
  //
  // to mock inserting input to the edit-text:
  //    call `editText.setText("input here")`
  //
  // to mock sending a broadcast:
  //    create the broadcast intent (example: `new Intent("my_action_here")` ) and put extras
  //    call `RuntimeEnvironment.application.sendBroadcast()` to send the broadcast
  //    call `Shadows.shadowOf(Looper.getMainLooper()).idle();` to let the android OS time to process the broadcast the let your activity consume it
}