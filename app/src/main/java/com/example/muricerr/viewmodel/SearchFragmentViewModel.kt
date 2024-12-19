package com.example.muricerr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.muricerr.model.Album
import com.example.muricerr.model.DeezerResponseAlbum
import com.example.muricerr.model.DeezerResponseTrack
import com.example.muricerr.model.Track
import com.example.muricerr.network.DeezerApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private val _isLoading = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String?>()

    fun searchTracks(query: String) {
        _isLoading.value = true
        DeezerApiInstance.api.searchTracks(query).enqueue(object : Callback<DeezerResponseTrack> {
            override fun onResponse(call: Call<DeezerResponseTrack>, response: Response<DeezerResponseTrack>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _tracks.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<DeezerResponseTrack>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed to fetch tracks"
            }
        })
    }

    fun searchAlbums(query: String) {
        _isLoading.value = true
        DeezerApiInstance.api.searchAlbums(query).enqueue(object : Callback<DeezerResponseAlbum> {
            override fun onResponse(call: Call<DeezerResponseAlbum>, response: Response<DeezerResponseAlbum>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _albums.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<DeezerResponseAlbum>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed to fetch albums"
            }
        })
    }

    fun clearTracks() {
        _tracks.value = emptyList()
    }

    fun clearAlbums() {
        _albums.value = emptyList()
    }
}
