package com.example.muricerr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muricerr.R
import com.example.muricerr.database.PlaylistEntity

class PlaylistAdapter(
    private var playlists: List<PlaylistEntity>,
    private val onPlaylistClick: (PlaylistEntity) -> Unit,
    private val onDeleteClick: (PlaylistEntity) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        fun bind(playlist: PlaylistEntity) {
            playlistName.text = playlist.name
            itemView.setOnClickListener { onPlaylistClick(playlist) }
            deleteButton.setOnClickListener { onDeleteClick(playlist) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun updatePlaylists(updatedPlaylists: List<PlaylistEntity>) {
        playlists = updatedPlaylists
        notifyDataSetChanged()
    }
}
