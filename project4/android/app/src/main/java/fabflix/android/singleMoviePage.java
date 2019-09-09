package fabflix.android;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class singleMoviePage extends AppCompatActivity {
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie_page);

        Bundle bundle = getIntent().getExtras();
        id = bundle.get("id").toString();


        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.GET, "https://10.0.2.2:8443/project4/api/movie/view?id=" + id, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());

                        try {
                            String title = response.get("title").toString();
                            String year = response.get("year").toString();
                            String director = response.get("director").toString();

                            JSONArray array =  response.getJSONArray("genres");
                            ArrayList<String> genres = new ArrayList<String>();
                            int len = array.length();
                            for (int i=0;i<len;i++){
                                genres.add(array.getJSONObject(i).get("name").toString());
                            }

                            JSONArray array2 =  response.getJSONArray("stars");
                            ArrayList<String> stars = new ArrayList<String>();
                            len = array2.length();
                            for (int i=0;i<len;i++){
                                stars.add(array2.getJSONObject(i).get("name").toString());
                            }

                            String genresStr = String.join(", ", genres);
                            String starsStr = String.join(", ", stars);

                            ((TextView) findViewById(R.id.movie_title)).setText(title);
                            ((TextView) findViewById(R.id.movie_year)).setText(year);
                            ((TextView) findViewById(R.id.movie_director)).setText(director);
                            ((TextView) findViewById(R.id.movie_genres)).setText(genresStr);
                            ((TextView) findViewById(R.id.movie_stars)).setText(starsStr);

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
