package com.example.muricerr.adapterimport

import android.view.LayoutInflater
import com.example.muricerr.R
import com.example.muricerr.model.Track

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackAdapterForFauvarits(
    private var tracks: MutableList<Track>,
    private val onTrackClick: (Track) -> Unit,
    private val onRemoveClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapterForFauvarits.TrackViewHolder>() {

    class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.trackTitle)
        val artist: TextView = view.findViewById(R.id.artistName)
        val albumCover: ImageView = view.findViewById(R.id.albumCover)
        val removeIcon: ImageView = view.findViewById(R.id.removeIcon)  // Remove icon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tracker_fauvarits, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.title.text = track.title
        holder.artist.text = track.artist?.name

        // Load album cover using Glide
        Glide.with(holder.albumCover.context)
            .load(track.album?.cover)
            .into(holder.albumCover)

        // Handle track item click
        holder.itemView.setOnClickListener { onTrackClick(track) }

        // Handle remove icon click (remove from favorites)
        holder.removeIcon.setOnClickListener { onRemoveClick(track) }
    }

    // Remove track from the list and notify the adapter to update
    fun removeTrack(track: Track) {
        tracks.remove(track)
        notifyDataSetChanged()  // Notify the adapter that the data set has changed
    }

    override fun getItemCount(): Int = tracks.size
}
