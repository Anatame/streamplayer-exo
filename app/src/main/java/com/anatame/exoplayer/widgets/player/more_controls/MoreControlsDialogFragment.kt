package com.anatame.exoplayer.widgets.player.more_controls

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.exoplayer.databinding.PlayerControlsMoreBinding
import com.anatame.exoplayer.widgets.player.FlordiaPlayer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class MoreControlsDialogFragment(
    private val flordiaPlayer: FlordiaPlayer
) : BottomSheetDialogFragment() {
    private lateinit var binding: PlayerControlsMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerControlsMoreBinding.inflate(layoutInflater)
        Timber.tag("tracksList").d(flordiaPlayer.currentTracksDataList.toString())

        val moreControlsAdapter = MoreControlsAdapter(requireContext())

        binding.recyclerView.apply {
            adapter = moreControlsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        moreControlsAdapter.differ.submitList(flordiaPlayer.currentTracksDataList)

        moreControlsAdapter.setOnItemClickListener{
            Toast.makeText(requireContext(), it.quality, Toast.LENGTH_SHORT).show()
            flordiaPlayer.setVideoQuality(it.bitrate)
            this.dismiss()
        }

        return binding.root
    }

}