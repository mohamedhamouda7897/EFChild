package com.example.efinancechild;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readButton = findViewById(R.id.readButton);
        readButton.setOnClickListener(view -> {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.efinancemaster", "com.example.efinancemaster.MainActivity"));
                intent.putExtra("CALL_FUNCTION", "ReadFileData");
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                Log.d("ERROR ERROR", "Hena Error " + e.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            Log.d("EF CHECK", result);

            String json = convertStringToJson(result);

            RequestModel requestBody = new RequestModel(json);
            Services service = APiManager.getRetrofitInstance().create(Services.class);
            Call<Void> call = service.sendResult(requestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("EF Resonse", "Response: " + response.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("API Configuration Required");
                    String requestBodyStr = json;
                    builder.setMessage("Please change your API base URL and endpoint. To Return Success Result\n\nRequest Body:\n" + requestBodyStr);
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("EF", "Error: " + t.getMessage());
                }
            });
        }

    }

    public static String convertStringToJson(String data) {

        String[] lines = data.split("\n");

        List<String> jsonObjects = new ArrayList<>();


        for (String line : lines) {

            String[] parts = line.split(" ");
            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];
                jsonObjects.add(String.format("{\"%s\": %s}", key, value));
            }
        }

        return "[" + String.join(", ", jsonObjects) + "]";
    }
}