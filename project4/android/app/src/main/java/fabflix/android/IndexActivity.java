package fabflix.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends AppCompatActivity {
    private ListView listView;
    private MovieListViewAdapter adapter;
    private int page;
    private final int limit = 10;
    private String currentKeyword = null;

    final Map<String, String> params = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        params.put("limit", limit + "");
        params.put("offset", "0");
    }

    public void next(final View v) {
        page++;
        params.put("offset", (page * limit) + "");
        search(v);
    }

    public void prev(final View v) {
        page--;
        if (page < 0)
            page = 0;
        params.put("offset", (page * limit) + "");
        search(v);
    }

    public void showMovie(String id) {
        Intent intent = new Intent(this, singleMoviePage.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void search(final View v) {

        String keyword = ((EditText) findViewById(R.id.keyword)).getText().toString();

        if (keyword.equals(currentKeyword)) {
            page = 0;
            currentKeyword = keyword;
        }

        Log.d("Search", keyword);
        Log.d("Params", params.toString());

        final ArrayList<Movie> movies = new ArrayList<Movie>();

        adapter = new MovieListViewAdapter(this, movies);

        params.put("keyword", keyword);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST, "https://10.0.2.2:8443/project4/api/fullsearch", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());

                        try {
                            JSONArray array = response.getJSONArray("data");
                            int len = array.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject obj = array.getJSONObject(i);
                                String id = obj.getString("id");
                                String title = obj.getString("title");
                                int year = obj.getInt("year");
                                String director = obj.getString("director");
                                double rating = obj.getDouble("rating");

                                ArrayList<Star> stars = new ArrayList<Star>();
                                ArrayList<Genre> genres = new ArrayList<Genre>();

                                JSONArray starArray = obj.getJSONArray("stars");

                                for (int j = 0; j < starArray.length(); j++) {
                                    String starId = starArray.getJSONObject(j).getString("id");
                                    String name = starArray.getJSONObject(j).getString("name");

                                    stars.add(new Star(starId, "", name));
                                }

                                JSONArray genreArray = obj.getJSONArray("genres");

                                for (int j = 0; j < genreArray.length(); j++) {
                                    String genreId = genreArray.getJSONObject(j).getString("id");
                                    String name = genreArray.getJSONObject(j).getString("name");

                                    genres.add(new Genre(genreId, name));
                                }

                                movies.add(new Movie(id, title, year, director, stars, genres));
                            }

                            listView = findViewById(R.id.listViewMovies);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    Log.d("Chosen", movies.get(position).getTitle());
                                    showMovie(movies.get(position).getId());


                                }
                            });
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

    /*private ListView lvMovie;
    private MovieListAdapter movieAdapter;
    private List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        lvMovie = (ListView)findViewById(R.id.listViewMovies);
        mMovieList = new ArrayList<>();

        //Sample Data (Insert data from MySQL DB here later)
        ArrayList<String> starsList = new ArrayList<>();
        starsList.add("Star1");
        starsList.add("Star2");
        ArrayList<String> genresList = new ArrayList<>();
        genresList.add("Sci-Fi");
        genresList.add("Action");
        mMovieList.add(new Movie("tt123456","Star Wars", 2018, "George Lucas", "Star1", "Genre1"));
        mMovieList.add(new Movie("tt435433","Infinity War", 2017, "JJ Abrams", "Star2", "Genre2"));
        mMovieList.add(new Movie("tt678768","Black Panther", 2016, "Steven Spielberg", "Star3", "Genre3"));
        //Adapter
        movieAdapter = new MovieListAdapter(IndexActivity.this, android.R.layout.simple_list_item_1, mMovieList);
        lvMovie.setAdapter(movieAdapter);

        //Tap on movie title should jump to single movie page
        lvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }*/


}
