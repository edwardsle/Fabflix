package fabflix.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void sendMessage(final View v)
    {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
    }

    public void login(final View view)
    {
        Log.d("Login", "Begin log in");
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        final Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final JsonObjectRequest afterLoginRequest = new JsonObjectRequest(
                Request.Method.GET, "https://10.0.2.2:8443/project4/api/me", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response2", response.toString());
                        try {
                            ((TextView) findViewById(R.id.errorMessage)).setText("Welcome " + response.getString("firstName"));
                            sendMessage(view);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("security.error", error.toString());
                    }
                });

        final JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST, "https://10.0.2.2:8443/project4/api/login", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equals("success"))
                                queue.add(afterLoginRequest);
                            else
                                ((TextView) findViewById(R.id.errorMessage)).setText(response.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("security.error", error.toString());
                    }
                });

        queue.add(loginRequest);
    }

}
