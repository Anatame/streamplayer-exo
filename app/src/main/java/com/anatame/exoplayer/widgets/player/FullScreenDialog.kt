package com.anatame.exoplayer.widgets.player

import android.app.Activity
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import com.anatame.exoplayer.R
import com.anatame.exoplayer.databinding.PlayerCustomStubLandscapeBinding
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView

class FullScreenDialog(
    private val activity: Activity,
    private val player: ExoPlayer,
    private val playerView: PlayerView,
): Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

    private lateinit var binding: PlayerCustomStubLandscapeBinding
    private lateinit var newPlayerView: PlayerView
    // controls
    private lateinit var fullScreenBtn: ImageButton
    private lateinit var exitFullScreenBtn: ImageButton
    private lateinit var moreBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var resizeBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleGoingFullScreen()
        binding = PlayerCustomStubLandscapeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newPlayerView = binding.vidPlayer
        PlayerView.switchTargetView(player, playerView, newPlayerView)

        configureOverlay()
        setUpControls()
    }

    override fun onStart() {
        super.onStart()
        handleGoingFullScreen()
    }

    override fun onBackPressed() {
        backToPortrait()
    }

    fun backToPortrait(){
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        PlayerView.switchTargetView(player, newPlayerView, playerView)
        notFullScreen(this.window!!)
        notFullScreen(activity.window!!)
        this.dismiss()
    }

    fun handleGoingFullScreen(){
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fullScreenActivity(this.window!!)
        fullScreenActivity(activity.window!!)
    }

    private fun setUpControls(){
        fullScreenBtn = binding.vidPlayer.findViewById(R.id.fullscreenBtn)
        exitFullScreenBtn = binding.vidPlayer.findViewById(R.id.exitFullScreenBtn)
        moreBtn = binding.vidPlayer.findViewById(R.id.more)
        backBtn = binding.vidPlayer.findViewById(R.id.backArrow)
        resizeBtn = binding.vidPlayer.findViewById(R.id.resize)

        controlsForLandscape()
    }
    private fun controlsForLandscape(){
        fullScreenBtn.visibility = View.INVISIBLE
        exitFullScreenBtn.visibility = View.VISIBLE
        resizeBtn.visibility = View.VISIBLE

        exitFullScreenBtn.setOnClickListener{
            backToPortrait()
        }
        backBtn.setOnClickListener{
            backToPortrait()
        }
        resizeBtn.setOnClickListener{
            resize()
        }

    }

    private fun resize(){
        when(newPlayerView.resizeMode){
            AspectRatioFrameLayout.RESIZE_MODE_FIT -> {
                newPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> {
                newPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
        }

    }

    private fun configureOverlay() {
        binding.youtubeOverlay.player(player)
        binding.youtubeOverlay
            .performListener(object : YouTubeOverlay.PerformListener {
                override fun onAnimationStart() {
                    // Do UI changes when circle scaling animation starts (e.g. hide controller views)
                    binding.youtubeOverlay.visibility = FrameLayout.VISIBLE
                }

                override fun onAnimationEnd() {
                    // Do UI changes when circle scaling animation starts (e.g. show controller views)
                    binding.youtubeOverlay.visibility = FrameLayout.GONE
                }
            })
    }

    private fun fullScreenActivity(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val controller =  window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                };
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            };
        }
    }
    private fun notFullScreen(window: Window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
            val controller =  window.insetsController
            if (controller != null) {
                controller.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_DEFAULT
                window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            };
        }

    }

}
