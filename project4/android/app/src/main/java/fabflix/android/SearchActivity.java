package fabflix.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private MovieListViewAdapter adapter;
//    private List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

    }

    /*
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
        movieAdapter = new MovieListViewAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, mMovieList);
        lvMovie.setAdapter(movieAdapter);

        //Tap on movie title should jump to single movie page
        lvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }*/

    // Search box
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Do anything to search results here
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/
}
