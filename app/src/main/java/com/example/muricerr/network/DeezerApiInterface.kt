package com.example.muricerr.network

import com.example.muricerr.model.AlbumTracksResponse
import com.example.muricerr.model.DeezerResponseAlbum
import com.example.muricerr.model.DeezerResponseTrack
import com.example.muricerr.model.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerApiInterface {
    @GET("search/track")
    fun searchTracks(
        @Query("q") query: String
    ): Call<DeezerResponseTrack>

    @GET("search/album")
    fun searchAlbums(
        @Query("q") query: String
    ): Call<DeezerResponseAlbum>

    @GET("album/{id}")
    fun getAlbumTracks(
        @Path("id") albumId: Long
    ): Call<AlbumTracksResponse>

    @GET("track/{id}")
    fun getTrackById(@Path("id") trackId: Long): Call<Track>
}
