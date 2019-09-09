package fabflix.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class MovieListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMovieList;

    public MovieListAdapter(Context mContext, int simple_list_item_1, List<Movie> mMovieList) {
        this.mContext = mContext;
        this.mMovieList = mMovieList;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
    }
}
