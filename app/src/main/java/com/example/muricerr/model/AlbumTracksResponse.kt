package com.example.muricerr.model

data class AlbumTracksResponse(
    val id: Long,
    val title: String,
    val cover: String,
    val artist: Artist,
    val tracks: TrackList
)