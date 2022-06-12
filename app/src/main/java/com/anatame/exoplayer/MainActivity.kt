package com.anatame.exoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anatame.exoplayer.databinding.ActivityMainBinding
import com.anatame.exoplayer.widgets.player.FlordiaPlayerSystem

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var player: FlordiaPlayerSystem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // dataNetWork val url = "https://ajye.mcloud.to/1aa38422f90b4f42a6d1671bcee5a045d52fe8044abab3bb554172f3a6369a877e43d210103e63b3aa76d45da94aec8ef384cb1a847cd2fed3e5f16b0527b4826f9040eb0105bc454191abab6d4e563926388a220700339a89c2d65980cd66c52b4df80c06/r/list.m3u8"
        val url = "https://ajye.vizcloud.cloud/simple/EqPFI_gQBAro1HhYl67rC5surVwCurfvTRh7rqk+wYMnU94US2El/br/list.m3u8#.mp4"
        player = binding.flordiaPlayer
        player?.playVideo(url, this)

        binding.loadBtn.setOnClickListener {
            player?.playVideo(url, this)
        }
    }

    override fun onResume() {
        super.onResume()
        player?.resume()
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.destroy()
    }
}