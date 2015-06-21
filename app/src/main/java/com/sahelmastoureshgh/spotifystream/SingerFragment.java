package com.sahelmastoureshgh.spotifystream;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Encapsulates fetching the Singer and displaying it as a ListView layout
 */
public class SingerFragment extends Fragment{
    ArrayList<Singer> allSingers;
    CustomSingersAdapter singerAdapter;
    SpotifyApi api;
    SpotifyService spotifyService ;
    FetchSingerTask singerTask;
    EditText searchSinger;

    public SingerFragment() {
        api = new SpotifyApi();
        spotifyService = api.getService();
        allSingers = new ArrayList<>();
        singerTask = new FetchSingerTask();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // The singerAdapter will take data from a source and
        // use it to populate the ListView it's attached to.

        singerAdapter =  new CustomSingersAdapter(
                getActivity(), // The current context (this activity)
                R.layout.list_item_singer, // The name of the layout ID.
                R.id.list_item_singer_textview, // The ID of the textview to populate.
                allSingers);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_singer);
        listView.setAdapter(singerAdapter);

        //Listener on Search Text
        searchSinger= (EditText) rootView.findViewById(R.id.search_text_view);
        searchSinger.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String myText = searchSinger.getText().toString().trim();
                if (myText.length() > 1) {
                    singerTask.execute(myText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;

    }


    /**
     * Fetch Artist (Singer) from Spotify and save it to Singer ArrayList
     */

    public class FetchSingerTask extends AsyncTask<String, Void, ArrayList<Singer>> {

        private final String LOG_TAG = FetchSingerTask.class.getSimpleName();
        String singerSearch;

        @Override
        protected ArrayList<Singer> doInBackground(String... params) {
            singerSearch = params[0];
            //Get Artist from Spotify Api and fill Singer model with the results for artist who have images and name
            try {
                ArtistsPager results = spotifyService.searchArtists(singerSearch);
                for(Artist artist : results.artists.items) {
                    if(artist.name!=null && artist.images.size()>0) {
                        allSingers.add(new Singer(artist.name, artist.images.get(0).url));
                    }

                }
                return allSingers;


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the  data, there's no point in attemping
                // to parse it.
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Singer> singers) {
            singerAdapter.clear();
            if(singers!=null) {
                singerAdapter.addAll(singers);
            }

        }
    }


}
