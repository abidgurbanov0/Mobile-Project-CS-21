package com.example.muricerr.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muricerr.R

import com.example.muricerr.adapterimport.TrackAdapterForFauvarits
import com.example.muricerr.model.Track
import com.google.gson.Gson

class FouvaritsFragment : Fragment() {

    private lateinit var adapter: TrackAdapterForFauvarits
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fouvarits, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        loadFavorites(recyclerView)

        return view
    }

    private fun loadFavorites(recyclerView: RecyclerView) {
        val sharedPrefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val likedTracksJson = sharedPrefs.getStringSet("likedTracks", mutableSetOf()) ?: return

        // Deserialize the tracks
        val likedTracks = likedTracksJson.map { Gson().fromJson(it, Track::class.java) }.toMutableList()

        // Initialize the adapter
        adapter = TrackAdapterForFauvarits(likedTracks, { track ->
            playTrack(track)
        }, { track ->
            removeTrackFromFavorites(track)
        })

        recyclerView.adapter = adapter
    }

    private fun removeTrackFromFavorites(track: Track) {
        val sharedPrefs = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        // Get the existing favorites
        val existingFavorites = sharedPrefs.getStringSet("likedTracks", mutableSetOf()) ?: mutableSetOf()

        // Remove the track from the favorites set
        val updatedFavorites = existingFavorites.filter { it != Gson().toJson(track) }.toMutableSet()

        // Update the shared preferences
        editor.putStringSet("likedTracks", updatedFavorites)
        editor.apply()

        // Remove the track from the adapter and update the UI
        adapter.removeTrack(track)

        // Show a Toast message
        Toast.makeText(requireContext(), "Removed from Playlist", Toast.LENGTH_SHORT).show()
    }

    private fun playTrack(track: Track) {
        val mediaItem = MediaItem.fromUri(track.preview)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.release()
    }
}
