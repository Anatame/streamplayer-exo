package com.anatame.exoplayer.widgets.player.more_controls

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anatame.exoplayer.databinding.PlayerControlsMoreBinding
import com.anatame.exoplayer.databinding.PlayerControlsMoreQualityBinding
import com.anatame.exoplayer.widgets.player.TrackData

class MoreControlsAdapter( val context: Context
) : RecyclerView.Adapter<MoreControlsAdapter.MoreControlItemsViewHolder>() {

    inner class MoreControlItemsViewHolder(val binding: PlayerControlsMoreQualityBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TrackData>() {
        override fun areItemsTheSame(oldItem: TrackData, newItem: TrackData): Boolean {
            return oldItem.bitrate == newItem.bitrate
        }

        override fun areContentsTheSame(oldItem: TrackData, newItem: TrackData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreControlItemsViewHolder {
        val binding = PlayerControlsMoreQualityBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoreControlItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((TrackData) -> Unit)? = null

    override fun onBindViewHolder(holder: MoreControlItemsViewHolder, position: Int) {
        val trackData = differ.currentList[position]
        holder.binding.apply {
            tvQuality.text = trackData.quality
            container.setOnClickListener {
                onItemClickListener?.let{it(trackData)}
            }
        }
    }

    fun setOnItemClickListener(listener: (TrackData) -> Unit) {
        onItemClickListener = listener
    }

}


