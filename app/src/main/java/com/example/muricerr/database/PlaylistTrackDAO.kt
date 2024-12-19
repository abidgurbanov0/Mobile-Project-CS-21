package com.example.muricerr.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistTrackDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToPlaylist(playlistTrack: PlaylistTrackEntity)

    @Query(
        "SELECT * FROM playlist_track " +
                "WHERE playlistId = :playlistId"
    )
    fun getTracksForPlaylist(playlistId: Long): List<PlaylistTrackEntity>

    @Delete
    fun removeTrackFromPlaylist(playlistTrack: PlaylistTrackEntity)
}
