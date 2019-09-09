package fabflix.android;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {

    private Context context;
    private ArrayList<Movie> movies;

    public MovieListViewAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.layout_listview_row, movies);
        this.movies = movies;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    /*public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_movie_list, null);
        TextView movieTitle = (TextView)v.findViewById(R.id.movie_title);
        TextView movieYear = (TextView)v.findViewById(R.id.movie_year);
        TextView movieDirector = (TextView)v.findViewById(R.id.movie_director);
        TextView movieGenres = (TextView)v.findViewById(R.id.movie_genres);
        TextView movieStars = (TextView)v.findViewById(R.id.movie_stars);

        // Set Text for TextView
        movieTitle.setText(mMovieList.get(position).getTitle());
        movieYear.setText(mMovieList.get(position).getYear());
        movieDirector.setText(mMovieList.get(position).getDirector());
        movieGenres.setText((CharSequence) mMovieList.get(position).getGenres());
        movieStars.setText((CharSequence) mMovieList.get(position).getStars());

        // Save Movie Id to tag
        v.setTag(mMovieList.get(position).getId());
        return v;
    }*/
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_listview_row, parent, false);

        String title = movies.get(position).getTitle();
        String year = movies.get(position).getYear() + "";
        String director = movies.get(position).getDirector();
        ArrayList<String> genres = new ArrayList<String>();
        ArrayList<String> stars = new ArrayList<String>();

        for (Genre genre : movies.get(position).getGenres())
        {
            genres.add(genre.getName());
        }

        for (Star star : movies.get(position).getStars())
        {
            stars.add(star.getName());
        }

        String genresStr = String.join(", ", genres);
        String starsStr = String.join(", ", stars);

        ((TextView) view.findViewById(R.id.movie_title)).setText(title);
        ((TextView) view.findViewById(R.id.movie_year)).setText(year);
        ((TextView) view.findViewById(R.id.movie_director)).setText(director);
        ((TextView) view.findViewById(R.id.movie_genres)).setText(genresStr);
        ((TextView) view.findViewById(R.id.movie_stars)).setText(starsStr);

        return view;
    }
}
