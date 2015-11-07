# Spotify Streamer

This project will utilize two key Android libraries.

* Picasso - A powerful library that will handle image loading and caching on your behalf.
* Spotify-Web-Api-Wrapper - A clever Java wrapper for the Spotify Web API that provides java methods that map directly to Web API endpoints.

##Stage 1: Search for an Artist, then Return Their Top Tracks

### Task 1: UI to Search for an Artist

Design and build the layout for an Activity that allows the user to search for an artist and then return the results in a ListView.

### Task 2: UI to Display the top 10 tracks for a selected artist

Design and build the layout for an Activity that displays the top tracks for a select artist.

### Task 3: Query the Spotify Web API

Use the Spotify Web Api Wrapper to simplify this task since it handles making the HTTP request and deserializing the JSON response for you. You will be able to extract the artist and track metadata you need directly as java objects. 

##Stage 2: Build a Track Player and Optimize for Tablet 

### Task 1: Build a Simple Player UI

Build a simple track player launched via Intent when a user selects a specific track from the Artist Top Tracks view.

### Task 2: Implement playback for a selected track

Use Android’s MediaPlayer API to stream the track preview of a currently selected track.

### Task 3: Optimize the entire end to end experience for a tablet

Migrate the existing UI flow to use a Master-Detail Structure for tablet. If you haven’t done so already, you will want to implement three Fragments for your tablet UI: one for artist search, one for top track results, and another for Playback.

# Rubric

### User Interface - Layout

* [Phone] UI contains a screen for searching for an artist and displaying a list of artist results.
* Individual artist result layout contains - Artist Thumbnail , Artist name.
* [Phone] UI contains a screen for displaying the top tracks for a selected artist.
* Individual track layout contains - Album art thumbnail, track name, album name.

### User Interface - Function

* App contains a search field that allows the user to enter in the name of an artist to search for.
* When an artist name is entered, app displays list of artist results.
* App displays a message (for example, a toast) if the artist name/top tracks list for an artist is not found (asks to refine search).
* When an artist is selected, app launches the “Top Tracks” View.
* App displays a list of top tracks.

### Network API Implementation

* App implements Artist Search + GetTopTracks API Requests (Using the Spotify wrapper or by making a HTTP request and deserializing the JSON data).
* App stores the most recent top tracks query results and their respective metadata (track name, artist name, album name) locally in list. The queried results are retained on rotation..
