package com.anatame.exoplayer.widgets.player

import android.content.Context
import android.media.MediaFormat
import android.util.Log
import com.anatame.exoplayer.network.AppNetworkClient
import com.github.vkay94.dtpv.DoubleTapPlayerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.video.VideoFrameMetadataListener
import timber.log.Timber

class FlordiaPlayer(private val context: Context) {
    val player: ExoPlayer = makePlayer(context)
    private var trackListener: PlayerTrackListener? = null

    fun playVideo(url: String, playerView: PlayerView){
        playerView.player = player
        player.setMediaSource(createMediaSource(url))
        player.prepare()
        player.playWhenReady = true
        listen()
    }

    private fun listen(){
        player.addListener(object: Player.Listener{
            override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
                super.onTracksInfoChanged(tracksInfo)

                val trackDataList = ArrayList<TrackData>()

                val tracks = tracksInfo.trackGroupInfos.first()

                for(i in 0 until tracks.trackGroup.length){
                    trackDataList.add(
                        TrackData(
                            "${tracks.trackGroup.getFormat(i).width} x ${tracks.trackGroup.getFormat(i).height}",
                            tracks.trackGroup.getFormat(i).bitrate
                        )
                    )
                }

                trackListener?.handleTracksLoaded(trackDataList)
            }
        })
    }

    fun setVideoQuality(bitrate: Int){
        player.let{
            it.trackSelectionParameters = player.trackSelectionParameters
                .buildUpon()
                .setMaxVideoBitrate(bitrate)
                .setMinVideoBitrate(bitrate)
                .build()
            Timber.tag("setVidQual").d(bitrate.toString())

            it.setVideoFrameMetadataListener(object : VideoFrameMetadataListener {
                override fun onVideoFrameAboutToBeRendered(
                    presentationTimeUs: Long,
                    releaseTimeNs: Long,
                    format: Format,
                    mediaFormat: MediaFormat?
                ) {
                    Timber.d("""
                                 ${format.height} x ${format.width}
                                 ${format.bitrate}
                                 """.trimIndent())
                }
            })

        }
    }

    fun resume(){
        player.prepare()
        player.play()
    }
    fun stop(){
        player.stop()
    }
    fun release(){
        player.stop()
        player.release()
    }

    private fun createMediaSource(hls: String): MediaSource {

        val dataSourceFactory = OkHttpDataSource.Factory(AppNetworkClient.getClient())

        return HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(false)
            .createMediaSource(MediaItem.fromUri(hls))
    }
    private fun makePlayer(context: Context): ExoPlayer {
        Timber.d("shit this got called again bruh")
        return ExoPlayer.Builder(context).build()
    }
}

data class TrackData(
    val quality: String,
    val bitrate: Int
)

interface PlayerTrackListener {
    fun handleTracksLoaded(tracks: ArrayList<TrackData>)
}