package com.example.androidactivitylifecycle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;
import okhttp3.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityOpenai extends AppCompatActivity {
    private EditText userPromptEditText;
    private TextView responseTextView;
    private Button sendButton;

    private static final String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openai);

        userPromptEditText = findViewById(R.id.userPromptEditText);
        responseTextView = findViewById(R.id.responseTextView);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPrompt = userPromptEditText.getText().toString();
                new OpenAIRequestTask().execute(userPrompt);
            }
        });
    }

    private class OpenAIRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userPrompt = params[0];
            String response = null;

            try {
                OkHttpClient client = new OkHttpClient();
                //String json = "{\"prompt\":\"" + userPrompt + "\"}";
                String json = "{\"prompt\":\"" + userPrompt + "\",\"max_tokens\":50}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

                Request request = new Request.Builder()
                        .url("https://api.openai.com/v1/engines/davinci/completions")
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .post(requestBody)
                        .build();

                Response apiResponse = client.newCall(request).execute();
                response = apiResponse.body().string();
                Log.d("OpenAIResponse", response);
                for (int i = 0; i < params.length; i++) {
                    Log.d("ArrayValues", "Index " + i + ": " + params[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray choices = jsonObject.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    String generatedText = choice.getString("text");
                    responseTextView.setText(generatedText);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                responseTextView.setText("Error parsing response");
            }
        }
    }
}
