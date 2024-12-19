package com.example.muricerr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.muricerr.model.AlbumTracksResponse
import com.example.muricerr.model.Track
import com.example.muricerr.network.DeezerApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackListViewModel(application: Application) : AndroidViewModel(application) {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    // Fetch tracks for a given album
    fun getAlbumTracks(albumId: Long) {
        DeezerApiInstance.api.getAlbumTracks(albumId).enqueue(object : Callback<AlbumTracksResponse> {
            override fun onResponse(call: Call<AlbumTracksResponse>, response: Response<AlbumTracksResponse>) {
                if (response.isSuccessful) {
                    _tracks.value = response.body()?.tracks?.data ?: emptyList()
                }
            }

            override fun onFailure(call: Call<AlbumTracksResponse>, t: Throwable) {
                _tracks.value = emptyList()
            }
        })
    }

    // Fetch tracks by a list of track IDs
    fun getTracksByIds(trackIds: List<Long>) {
        val fetchedTracks = mutableListOf<Track>()

        trackIds.forEach { id ->
            DeezerApiInstance.api.getTrackById(id).enqueue(object : Callback<Track> {
                override fun onResponse(call: Call<Track>, response: Response<Track>) {
                    if (response.isSuccessful) {
                        response.body()?.let { fetchedTracks.add(it) }
                        if (fetchedTracks.size == trackIds.size) {
                            _tracks.value = fetchedTracks
                        }
                    }
                }

                override fun onFailure(call: Call<Track>, t: Throwable) {
                    if (fetchedTracks.size == trackIds.size) {
                        _tracks.value = fetchedTracks
                    }
                }
            })
        }
    }

    // Remove a track from the list of tracks
    fun removeTrack(track: Track) {
        val updatedTracks = _tracks.value?.toMutableList() ?: mutableListOf()
        updatedTracks.remove(track) // Remove the track from the list
        _tracks.value = updatedTracks // Update the LiveData with the modified list
    }
}
