package com.example.muricerr.view

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muricerr.R
import com.example.muricerr.adapter.AlbumRecyclerViewAdapter
import com.example.muricerr.adapter.RecyclerViewAdapter
import com.example.muricerr.model.Track
import com.example.muricerr.viewmodel.SearchFragmentViewModel

class SearchFragment : Fragment() {

    private lateinit var trackAdapter: RecyclerViewAdapter
    private lateinit var albumAdapter: AlbumRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var playerLayout: View
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerAlbumCover: ImageView
    private lateinit var playerButton: ImageButton
    private lateinit var trackButton: Button
    private lateinit var albumButton: Button

    private lateinit var viewModel: SearchFragmentViewModel
    private var searchMode: String = "track"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[SearchFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        trackAdapter = RecyclerViewAdapter(
            emptyList(),
            onItemClick = { track -> displayPlayer(track) }
        )

        albumAdapter = AlbumRecyclerViewAdapter(
            emptyList(),
            onAlbumClick = { album ->
                val bundle = Bundle().apply {
                    putLong("ALBUM_ID", album.id)
                }
                val albumTrackFragment = AlbumTrackFragment().apply { arguments = bundle }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_layout, albumTrackFragment)
                    .addToBackStack(null)
                    .commit()
            }
        )

        recyclerView.adapter = trackAdapter

        searchView = view.findViewById(R.id.search_view)
        searchView.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (searchMode == "track") {
                        viewModel.searchTracks(it)
                    } else {
                        viewModel.searchAlbums(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        playerLayout = view.findViewById(R.id.player_layout)
        playerTrackName = view.findViewById(R.id.track_name)
        playerArtistName = view.findViewById(R.id.artist_name)
        playerAlbumCover = view.findViewById(R.id.album_cover)
        playerButton = view.findViewById(R.id.player_button)
        trackButton = view.findViewById(R.id.track_button)
        albumButton = view.findViewById(R.id.album_button)
        exoPlayer = ExoPlayer.Builder(requireContext()).build()

        trackButton.setOnClickListener { selectSearchMode("track") }
        albumButton.setOnClickListener { selectSearchMode("album") }
        selectSearchMode(searchMode)

        playerButton.setOnClickListener {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
        }

        setupPlayerListeners()
        observeViewModel()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.stop()
        exoPlayer.release()
    }

    private fun selectSearchMode(mode: String) {
        searchMode = mode
        recyclerView.adapter = if (mode == "track") {
            viewModel.clearAlbums()
            trackAdapter
        } else {
            viewModel.clearTracks()
            albumAdapter
        }
        searchView.setQuery("", false)
    }

    private fun observeViewModel() {
        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            if (searchMode == "track") {
                trackAdapter.updateTracks(tracks)
            }
        }

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            if (searchMode == "album") {
                albumAdapter.updateAlbums(albums)
            }
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
            val songDetailsFragment = DetailFragment().apply { arguments = bundle }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, songDetailsFragment)
                .addToBackStack("")
                .commit()
        }
    }
}
