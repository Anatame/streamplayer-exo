package com.anatame.exoplayer.widgets.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anatame.exoplayer.databinding.PlayerControlsMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MoreControlsDialog() : BottomSheetDialogFragment() {
    private lateinit var binding: PlayerControlsMoreBinding

    companion object {
        fun newInstance(): MoreControlsDialog {
            return MoreControlsDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlayerControlsMoreBinding.inflate(layoutInflater)
        return binding.root
    }
}