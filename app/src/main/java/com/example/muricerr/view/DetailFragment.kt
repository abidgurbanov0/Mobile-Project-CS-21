package com.example.muricerr.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muricerr.R
import com.example.muricerr.adapter.PlaylistSelectionAdapter
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.database.PlaylistEntity
import com.example.muricerr.database.PlaylistTrackEntity
import com.example.muricerr.model.Album
import com.example.muricerr.model.Artist
import com.example.muricerr.model.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {

    private lateinit var database: AppDatabase
    private lateinit var albumImage: ImageView
    private lateinit var songTitle: TextView
    private lateinit var artistTitle: TextView
    private lateinit var playIcon: ImageButton
    private lateinit var player: ExoPlayer
    private lateinit var bookmarkIcon: ImageButton
    private lateinit var addToFavoritesIcon: ImageButton
    private lateinit var backArrowIcon: ImageView
    private var isSongPlaying = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_detail, container, false)

        // Bind views using findViewById
        albumImage = fragmentView.findViewById(R.id.album_cover)
        songTitle = fragmentView.findViewById(R.id.song_name)
        artistTitle = fragmentView.findViewById(R.id.artist_name)
        playIcon = fragmentView.findViewById(R.id.play_button)
        backArrowIcon = fragmentView.findViewById(R.id.backButton)
        bookmarkIcon = fragmentView.findViewById(R.id.bookmark)
        addToFavoritesIcon = fragmentView.findViewById(R.id.add_to_favorites)
        player = ExoPlayer.Builder(requireContext()).build()

        // Initialize database
        database = AppDatabase.getDatabase(requireContext())

        // Retrieve and display track information
        arguments?.let {
            val track = Track(
                id = it.getLong("TRACK_ID"),
                title = it.getString("TRACK_TITLE")!!,
                preview = it.getString("TRACK_PREVIEW")!!,
                artist = Artist(0, it.getString("ARTIST_NAME")!!),
                album = Album(
                    id = 0,
                    title = "",
                    cover = it.getString("ALBUM_COVER")!!,
                    cover_xl = it.getString("ALBUM_COVER_XL")!!
                )
            )
            songTitle.text = track.title
            artistTitle.text = track.artist.name
            Glide.with(requireContext()).load(track.album.cover_xl).into(albumImage)

            val mediaItem = MediaItem.fromUri(track.preview)
            player.setMediaItem(mediaItem)
            player.prepare()

            // Set up listeners
            setUpPlayerListeners()
            configureSaveButton(track)
            configureFavoritesButton(track)
        }

        // Back button
        backArrowIcon.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Set play button to play initially
        playIcon.setBackgroundResource(R.drawable.play_ic)

        // Play/Pause button
        playIcon.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                playIcon.setBackgroundResource(R.drawable.play_ic)
                isSongPlaying = false
            } else {
                player.play()
                playIcon.setBackgroundResource(R.drawable.stop_ic)
                isSongPlaying = true
            }
        }

        return fragmentView
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
    }

    private fun setUpPlayerListeners() {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    player.seekTo(0)
                    player.pause()
                    playIcon.setBackgroundResource(R.drawable.play_ic)
                }
            }
        })
    }

    private fun configureSaveButton(track: Track) {
        bookmarkIcon.setOnClickListener {
            lifecycleScope.launch {
                val allPlaylists = withContext(Dispatchers.IO) {
                    database.playlistDao().getAllPlaylists()
                }
                if (allPlaylists.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "No playlists available. Please create one first.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    displayPlaylistSelectionDialog(allPlaylists, track)
                }
            }
        }
    }

    private fun configureFavoritesButton(track: Track) {
        addToFavoritesIcon.setOnClickListener {
            val preferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val editor = preferences.edit()

            val likedTracks = preferences.getStringSet("likedTracks", mutableSetOf()) ?: mutableSetOf()

            val trackJson = Gson().toJson(track)
            if (likedTracks.contains(trackJson)) {
                Toast.makeText(requireContext(), "Track is already in your favorites", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            likedTracks.add(trackJson)
            editor.putStringSet("likedTracks", likedTracks)
            editor.apply()

            Toast.makeText(requireContext(), "Track added to Favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayPlaylistSelectionDialog(playlists: List<PlaylistEntity>, track: Track) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.select_playlist, null)
        val playlistRecyclerView: RecyclerView = dialogView.findViewById(R.id.recycler_view_playlist_select)
        playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = PlaylistSelectionAdapter(playlists) { selectedPlaylist ->
            addTrackToPlaylist(selectedPlaylist, track)
        }
        playlistRecyclerView.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle("Choose a Playlist")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addTrackToPlaylist(playlist: PlaylistEntity, track: Track) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val playlistTrack = PlaylistTrackEntity(playlistId = playlist.id, trackId = track.id)
                database.playlistTrackDao().addTrackToPlaylist(playlistTrack)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Track successfully added to ${playlist.name}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to add track to playlist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
