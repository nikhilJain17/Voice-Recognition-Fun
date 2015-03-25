package com.hello.learning.voicerecognition;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
// API imports
import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends Activity implements AIListener {

    private Button processButton;
    private TextView textView;


    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView = (TextView) findViewById(R.id.textView);
        processButton = (Button) findViewById(R.id.processButton);


        // Setup config for google speech shit
        final AIConfiguration config = new AIConfiguration("0fe4c65a0ef94336b7529efa4f2882a5",
                "5bd76f3bb0e2438fa0930c2e681a92dd", AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.Google);


        // Initialize the aiservice
        aiService = AIService.getService(this, config);
        // Set it as the listener
        aiService.setListener(this);


    }

    // Listener for the button
    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }


    // Callback method when it is finished listening
    public void onResult(final AIResponse response) {

        if (response.isError()) {
            textView.setText("Error: " + response.getStatus().getErrorDetails());
        }

        else {
            Result result = response.getResult();

            // get Parameters
            String parameterString = "";

            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                    parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                }
            }

            // Show results in textview
            textView.setText("Query:" + result.getResolvedQuery() +
                    "\nAction: " + result.getAction() +
                    "\nParameters: " + parameterString);
        }
    }

    // Handles errors
    @Override
    public void onError(final AIError error) {
        textView.setText(error.toString());
    }

    // Satisfy the implement requirements
    @Override
    public void onListeningStarted() {}

    @Override
    public void onListeningFinished() {}

    @Override
    public void onAudioLevel(final float level) {}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
