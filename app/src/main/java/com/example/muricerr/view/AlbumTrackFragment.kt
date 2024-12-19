package com.example.muricerr.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muricerr.R
import com.example.muricerr.adapter.RecyclerViewAdapter
import com.example.muricerr.model.Track
import com.example.muricerr.viewmodel.TrackListViewModel

class AlbumTrackFragment : Fragment() {

    private lateinit var trackAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: TrackListViewModel
    private lateinit var backButton: ImageView
    private lateinit var playerLayout: View
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerAlbumCover: ImageView
    private lateinit var playerButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[TrackListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_album_track, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_track)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        playerLayout = view.findViewById(R.id.player_layout_album)
        playerTrackName = view.findViewById(R.id.track_name)
        playerArtistName = view.findViewById(R.id.artist_name)
        playerAlbumCover = view.findViewById(R.id.album_cover)
        playerButton = view.findViewById(R.id.player_button)
        backButton = view.findViewById(R.id.backButtonAlbum)

        trackAdapter = RecyclerViewAdapter(
            emptyList(),
            onItemClick = { track -> displayPlayer(track) }
        )

        recyclerView.adapter = trackAdapter
        observeViewModel()

        val albumId = arguments?.getLong("ALBUM_ID") ?: return view
        viewModel.getAlbumTracks(albumId)

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        playerButton.setOnClickListener {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
                playerButton.setBackgroundResource(R.drawable.play_ic)
            } else {
                exoPlayer.play()
                playerButton.setBackgroundResource(R.drawable.stop_ic)
            }
        }

        setupPlayerListeners()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.stop()
        exoPlayer.release()
    }

    private fun observeViewModel() {
        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateTracks(tracks)
        }
    }

    private fun setupPlayerListeners() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    exoPlayer.seekTo(0)
                    exoPlayer.pause()
                }
            }
        })
    }

    private fun displayPlayer(track: Track) {
        exoPlayer.pause()
        exoPlayer.clearMediaItems()

        playerTrackName.text = track.title
        playerArtistName.text = track.artist.name
        Glide.with(requireContext()).load(track.album.cover).into(playerAlbumCover)
        playerButton.setBackgroundResource(R.drawable.play_ic)

        val mediaItem = MediaItem.fromUri(track.preview)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        playerLayout.visibility = View.VISIBLE

        playerLayout.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("TRACK_ID", track.id)
                putString("TRACK_TITLE", track.title)
                putString("ARTIST_NAME", track.artist.name)
                putString("ALBUM_COVER", track.album.cover)
                putString("ALBUM_COVER_XL", track.album.cover_xl)
                putString("TRACK_PREVIEW", track.preview)
            }
            val songDetailsFragment = DetailFragment().apply {
                arguments = bundle
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, songDetailsFragment)
                .addToBackStack("")
                .commit()
        }
    }
}
