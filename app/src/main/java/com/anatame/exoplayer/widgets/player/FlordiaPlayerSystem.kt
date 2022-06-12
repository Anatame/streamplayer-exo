package com.anatame.exoplayer.widgets.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import com.anatame.exoplayer.MainActivity
import com.anatame.exoplayer.R
import com.anatame.exoplayer.databinding.PlayerCustomStubBinding
import com.anatame.exoplayer.widgets.player.more_controls.MoreControlsDialogFragment
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.exoplayer2.ui.PlayerView

class FlordiaPlayerSystem (
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val binding = PlayerCustomStubBinding.inflate(LayoutInflater.from(context), this, false)
    private val flordiaPlayer: FlordiaPlayer = FlordiaPlayer(context)
    private var activity: Activity? = null
    private var dialog: FullScreenDialogFragment? = null

    // controls
    private lateinit var fullScreenBtn: ImageButton
    private lateinit var exitFullScreenBtn: ImageButton
    private lateinit var moreBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var resizeBtn: ImageButton


    init {
        addView(binding.root)
        configureOverlay()
        setUpControls()
        controlsForPortrait()
    }


    fun playVideo(url: String, activity: Activity){
        this.activity = activity
        flordiaPlayer.playVideo(url, binding.vidPlayer)
    }

    fun goLandScape(){
//        dialog = activity?.let { FullScreenDialog(it, flordiaPlayer.player, binding.vidPlayer) }
//        dialog?.show()

        activity?.let{
            dialog = FullScreenDialogFragment(it, flordiaPlayer, binding.vidPlayer)
            dialog?.show((it as MainActivity).supportFragmentManager, "More Controls Dialog")
        }
    }

    fun goPortrait(){
        dialog?.backToPortrait()
    }

    fun getPlayerView(): PlayerView = binding.vidPlayer

    // lifecycle stuff
    fun resume(){
        flordiaPlayer.resume()
    }
    fun stop(){
        flordiaPlayer.stop()
    }
    fun destroy(){
        flordiaPlayer.release()
    }

    private fun configureOverlay() {
        binding.youtubeOverlay.player(flordiaPlayer.player)
        binding.youtubeOverlay
            .performListener(object : YouTubeOverlay.PerformListener {
                override fun onAnimationStart() {
                    // Do UI changes when circle scaling animation starts (e.g. hide controller views)
                    binding.youtubeOverlay.visibility = VISIBLE
                }

                override fun onAnimationEnd() {
                    // Do UI changes when circle scaling animation starts (e.g. show controller views)
                    binding.youtubeOverlay.visibility = GONE
                }
            })
    }
    private fun setUpControls(){
        fullScreenBtn = binding.vidPlayer.findViewById(R.id.fullscreenBtn)
        exitFullScreenBtn = binding.vidPlayer.findViewById(R.id.exitFullScreenBtn)
        moreBtn = binding.vidPlayer.findViewById(R.id.more)
        backBtn = binding.vidPlayer.findViewById(R.id.backArrow)
        resizeBtn = binding.vidPlayer.findViewById(R.id.resize)
    }

    private fun controlsForPortrait(){
        fullScreenBtn.visibility = View.VISIBLE
        exitFullScreenBtn.visibility = View.INVISIBLE
        resizeBtn.visibility = View.INVISIBLE

        fullScreenBtn.setOnClickListener{
            goLandScape()
        }
        moreBtn.setOnClickListener{
            val moreDialog = MoreControlsDialogFragment(flordiaPlayer)
            activity?.let {moreDialog.show((it as MainActivity).supportFragmentManager, "More Controls Dialog")}
        }
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
    tailrec fun Context.getActivity(): Activity? = this as? Activity
        ?: (this as? ContextWrapper)?.baseContext?.getActivity()

}