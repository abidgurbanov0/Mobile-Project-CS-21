package com.example.muricerr.model

data class Track (
    val id: Long,
    val title: String,
    val preview: String,
    val artist: Artist,
    val album: Album
)
