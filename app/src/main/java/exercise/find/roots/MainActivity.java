package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForFail = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);

    // set initial UI:
    progressBar.setVisibility(View.GONE); // hide progress
    editTextUserInput.setText(""); // cleanup text in edit-text
    editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
    buttonCalculateRoots.setEnabled(false); // set button as disabled (user can't click)

    // set listener on the input written by the keyboard to the edit-text
    editTextUserInput.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      public void afterTextChanged(Editable s) {
        // text did change ,was a todo
        String newText = editTextUserInput.getText().toString();
        if (newText.charAt(0) != '0'){
          buttonCalculateRoots.setEnabled(true);
        }
      }
    });

    // set click-listener to the button
    buttonCalculateRoots.setOnClickListener(v -> {
      Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
      String userInputString = editTextUserInput.getText().toString();
      // was a todo
      if (userInputString.charAt(0) == '0' || userInputString.charAt(0) == '-'){
        buttonCalculateRoots.setEnabled(false);
        return;
      }
      long userInputLong=0;
      try{
        buttonCalculateRoots.setEnabled(false);
        userInputLong = Long.parseLong(userInputString);
      }
      catch (Exception e){
        Toast.makeText(MainActivity.this, "number too big",
                Toast.LENGTH_SHORT).show();
        return;
      }
      intentToOpenService.putExtra("number_for_service", userInputLong);
      startService(intentToOpenService);
      //was a todo
      editTextUserInput.setEnabled(false);
      buttonCalculateRoots.setEnabled(false);
      progressBar.setVisibility(View.VISIBLE); // show progress
    });

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForSuccess = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots")) return;
        // success finding roots!
        /*
         was a todo
         */
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(true);
        progressBar.setVisibility(View.GONE); // show progress
        long original_num = incomingIntent.getLongExtra("original_number", 0);
        long root1 = incomingIntent.getLongExtra("root1", 0);
        long root2 = incomingIntent.getLongExtra("root2", 0);
        long time = incomingIntent.getLongExtra("time", 0);
        Intent success_intent = new Intent(MainActivity.this, SuccessActivity.class);
        success_intent.putExtra("original",original_num);
        success_intent.putExtra("root1",root1);
        success_intent.putExtra("root2",root2);
        success_intent.putExtra("time",time);
        startActivity(success_intent);
      }
    };
    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));

    /*
    was a todo
     */
    broadcastReceiverForFail = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("stopped_calculations")) return;
        //aborted
        editTextUserInput.setEnabled(true);
        buttonCalculateRoots.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        long original_num = incomingIntent.getLongExtra("original_number", 0);
        int time = incomingIntent.getIntExtra("time_until_give_up_seconds", 0);
        Toast.makeText(MainActivity.this, "aborted after: "+time+" seconds",
                Toast.LENGTH_SHORT).show();

      }
    };
    registerReceiver(broadcastReceiverForFail, new IntentFilter("stopped_calculations"));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    //was a todo
    this.unregisterReceiver(broadcastReceiverForFail);
    this.unregisterReceiver(broadcastReceiverForSuccess);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    ProgressBar progressBar = findViewById(R.id.progressBar);
    outState.putBoolean("is_calc",progressBar.getVisibility()==View.VISIBLE);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    boolean is_calc = savedInstanceState.getBoolean("is_calc");
    ProgressBar progressBar = findViewById(R.id.progressBar);
    EditText editTextUserInput = findViewById(R.id.editTextInputNumber);
    Button buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);
    if (is_calc){
      progressBar.setVisibility(View.VISIBLE);
      editTextUserInput.setEnabled(false);
      buttonCalculateRoots.setEnabled(false);
    }
    else{
      progressBar.setVisibility(View.GONE);
      editTextUserInput.setEnabled(true);
      String userInputString = editTextUserInput.getText().toString();
      if (userInputString.charAt(0) == '0' || userInputString.charAt(0) == '-'){
        buttonCalculateRoots.setEnabled(false);
        return;
      }
      buttonCalculateRoots.setEnabled(true);
    }
  }
}


/*

TODO:
the spec is:

upon launch, Activity starts out "clean":
* progress-bar is hidden
* "input" edit-text has no input and it is enabled
* "calculate roots" button is disabled

the button behavior is:
* when there is no valid-number as an input in the edit-text, button is disabled
* when we triggered a calculation and still didn't get any result, button is disabled
* otherwise (valid number && not calculating anything in the BG), button is enabled

the edit-text behavior is:
* when there is a calculation in the BG, edit-text is disabled (user can't input anything)
* otherwise (not calculating anything in the BG), edit-text is enabled (user can tap to open the keyboard and add input)

the progress behavior is:
* when there is a calculation in the BG, progress is showing
* otherwise (not calculating anything in the BG), progress is hidden

when "calculate roots" button is clicked:
* change states for the progress, edit-text and button as needed, so user can't interact with the screen

when calculation is complete successfully:
* change states for the progress, edit-text and button as needed, so the screen can accept new input
* open a new "success" screen showing the following data:
  - the original input number
  - 2 roots combining this number (e.g. if the input was 99 then you can show "99=9*11" or "99=3*33"
  - calculation time in seconds

when calculation is aborted as it took too much time:
* change states for the progress, edit-text and button as needed, so the screen can accept new input
* show a toast "calculation aborted after X seconds"


upon screen rotation (saveState && loadState) the new screen should show exactly the same state as the old screen. this means:
* edit-text shows the same input
* edit-text is disabled/enabled based on current "is waiting for calculation?" state
* progress is showing/hidden based on current "is waiting for calculation?" state
* button is enabled/disabled based on current "is waiting for calculation?" state && there is a valid number in the edit-text input


 */