package tow.toproject.mazen.chatappwithfirebase;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.watson.developer_cloud.service.exception.UnauthorizedException;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity {



    private ToneAnalyzer toneAnalyzerService;
    private AnalyzerResultFragment resultFragment;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        BMSClient.getInstance().initialize(getApplicationContext(), BMSClient.REGION_UK);

       // Button btn_Analyaze = (Button)findViewById(R.id.btnVoice);

        final String getMsg = getIntent().getStringExtra("msg");


        fab = (FloatingActionButton)findViewById(R.id.fab);


        toneAnalyzerService = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19,
                "0ed29cff-888b-4534-b01f-1a35bc4c8ff8",
                "ftIKPKjMsO4D");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getMsg.isEmpty()) {
                    // Send the user's input text to the AnalyzeTask.
                    AnalyzeTask analyzeTask = new AnalyzeTask();
                    analyzeTask.execute(getMsg);
                }
            }
        });

//        if (!getMsg.isEmpty()) {
//            // Send the user's input text to the AnalyzeTask.
//            AnalyzeTask analyzeTask = new AnalyzeTask();
//            analyzeTask.execute(getMsg);
//        }







        resultFragment = (AnalyzerResultFragment)getSupportFragmentManager().findFragmentByTag("result");

        if (resultFragment == null) {
            resultFragment = new AnalyzerResultFragment();
            resultFragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, resultFragment, "result").commit();


            AnalyzeTask analyzeTask = new AnalyzeTask();
            analyzeTask.execute(" ");
        }
    }






    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        // Have the fragment save its state for recreation on orientation changes.
        resultFragment.saveData();
        super.onDestroy();
    }


    private void showDialog(int errorTitle, String errorMessage, boolean canContinue) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(errorTitle, errorMessage, canContinue);
        newFragment.show(getFragmentManager(), "dialog");
    }

    /**
     * Enables communication to the Tone Analyzer Service and fetches the service's output.
     */
    private class AnalyzeTask extends AsyncTask<String, Void, ToneAnalysis> {

        @Override
        protected void onPreExecute() {
            EditText entryTextView = (EditText)findViewById(R.id.entryText);
            entryTextView.clearFocus();
            

            // Hide the keyboard so the user can see the full result.
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(entryTextView.getWindowToken(), 0);

            // Turn on the loading spinner for the brief network load.
         //   ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
         //   progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ToneAnalysis doInBackground(String... params) {
            String entryText = params[0];
            ToneAnalysis analysisResult;

            try {
                analysisResult = toneAnalyzerService.getTone(entryText,null).execute();
            } catch (Exception ex) {
                // Here is where we see if the user's credentials are valid or not along with other errors.
                if (ex.getClass().equals(UnauthorizedException.class) ||
                        ex.getClass().equals(IllegalArgumentException.class)) {
                    showDialog(R.string.error_title_invalid_credentials,
                            getString(R.string.error_message_invalid_credentials), false);
                } else if (ex.getCause() != null &&
                        ex.getCause().getClass().equals(UnknownHostException.class)) {
                    showDialog(R.string.error_title_bluemix_connection,
                            getString(R.string.error_message_bluemix_connection), true);
                } else {
                    showDialog(R.string.error_title_default, ex.getMessage(), true);
                }
                return null;
            }

            return analysisResult;
        }

        @Override
        protected void onPostExecute(ToneAnalysis result) {

                AnalyzerResultBuilder resultBuilder = new AnalyzerResultBuilder(Main2Activity.this,result);
              //  Chat_Room main2Activity = new Chat_Room(result);

              //  main2Activity.;
                resultBuilder.buildAnalyzerResultView();

        }
    }
}

