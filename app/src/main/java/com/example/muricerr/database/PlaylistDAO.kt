package com.example.muricerr.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface PlaylistDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Delete
    fun deletePlaylist(playlist: PlaylistEntity)
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query("DELETE FROM quizzes WHERE playlistId = :playlistId")
    fun deleteQuizzesByPlaylistId(playlistId: Long)

}
