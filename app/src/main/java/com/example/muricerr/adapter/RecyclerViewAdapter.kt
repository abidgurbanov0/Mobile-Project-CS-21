package com.example.muricerr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muricerr.R
import com.example.muricerr.model.Track

class RecyclerViewAdapter(
    private var tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.TrackViewHolder>() {

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.track_name)
        val artistTextView: TextView = itemView.findViewById(R.id.artist_name)
        val albumImageView: ImageView = itemView.findViewById(R.id.album_cover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        holder.titleTextView.text = track.title
        holder.artistTextView.text = track.artist.name
        Glide.with(holder.itemView.context).load(track.album.cover).into(holder.albumImageView)

        holder.itemView.setOnClickListener {
            onItemClick(track)
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun updateTracks(updatedTracks: List<Track>) {
        tracks = updatedTracks
        notifyDataSetChanged()
    }
}
