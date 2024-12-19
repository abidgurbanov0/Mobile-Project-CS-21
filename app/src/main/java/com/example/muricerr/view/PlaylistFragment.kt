package com.example.muricerr.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muricerr.R
import com.example.muricerr.adapter.PlaylistAdapter
import com.example.muricerr.database.AppDatabase
import com.example.muricerr.database.PlaylistDAO
import com.example.muricerr.database.PlaylistEntity
import com.example.muricerr.database.PlaylistTrackDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistFragment : Fragment() {

    private lateinit var createButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var playlistDao: PlaylistDAO
    private lateinit var playlistTrackDao: PlaylistTrackDAO
    private val playlists = mutableListOf<PlaylistEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_playlist)
        createButton = view.findViewById(R.id.create_button)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext().applicationContext)
        playlistDao = db.playlistDao()
        playlistTrackDao = db.playlistTrackDao()

        playlistAdapter = PlaylistAdapter(playlists, { playlist ->
            navigateToPlaylistTracks(playlist.id)
        }, { playlist ->
            showDeletePlaylistDialog(playlist)
        })
        recyclerView.adapter = playlistAdapter

        createButton.setOnClickListener { showCreatePlaylistDialog() }

        // Fetch playlists when the fragment is created
        fetchPlaylists()

        return view
    }

    private fun navigateToPlaylistTracks(playlistId: Long) {
        lifecycleScope.launch {
            val tracks = withContext(Dispatchers.IO) {
                playlistTrackDao.getTracksForPlaylist(playlistId)
            }
            val trackIds = tracks.map { it.trackId }.toLongArray()
            val fragment = PlaylistTrackFragment().apply {
                arguments = Bundle().apply {
                    putLongArray("TRACK_ID", trackIds)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fetchPlaylists() {
        lifecycleScope.launch {
            val fetchedPlaylists = withContext(Dispatchers.IO) {
                playlistDao.getAllPlaylists()
            }
            playlists.clear()
            playlists.addAll(fetchedPlaylists)

            withContext(Dispatchers.Main) {
                playlistAdapter.notifyDataSetChanged() // Notify adapter of data change
            }
        }
    }

    private fun showCreatePlaylistDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.create_playlist, null)
        val playlistNameInput: EditText = dialogView.findViewById(R.id.playlist_name_input)

        AlertDialog.Builder(requireContext())
            .setTitle("Create Playlist")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val name = playlistNameInput.text.toString().trim()
                if (name.isNotEmpty()) {
                    createPlaylist(name)
                } else {
                    Toast.makeText(requireContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createPlaylist(name: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val playlist = PlaylistEntity(name = name)
            playlistDao.insertPlaylist(playlist)

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Playlist \"$name\" created", Toast.LENGTH_SHORT).show()
                fetchPlaylists() // Refresh playlists after creation
            }
        }
    }

    private fun showDeletePlaylistDialog(playlist: PlaylistEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Playlist")
            .setMessage("Are you sure you want to delete the playlist \"${playlist.name}\"?")
            .setPositiveButton("Delete") { _, _ ->
                deletePlaylist(playlist)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePlaylist(playlist: PlaylistEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                playlistDao.deletePlaylist(playlist)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Playlist deleted", Toast.LENGTH_SHORT).show()
                    fetchPlaylists() // Refresh playlists after deletion
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to delete playlist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
