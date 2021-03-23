package com.kuanluntseng.swipify.swipe

import OnSwipeTouchListener
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.playback.Playback
import com.kuanluntseng.swipify.databinding.AppRemoteLayoutBinding
//import com.kuanluntseng.swipify.swipe.SwipeActivity.SpotifySampleContexts.TRACK_URI
import com.kuanluntseng.swipify.utils.Utils
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.ContentApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.*
import com.spotify.sdk.demo.kotlin.TrackProgressBar
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SwipeActivity : AppCompatActivity() {

    object AuthParams {
        const val CLIENT_ID = "YOUR CLIENT ID"
        const val REDIRECT_URI = "comspotifytestsdk://callback"
    }

    companion object {
        const val TAG = "App-Remote Sample"
        const val STEP_MS = 15000L;
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private var playerStateSubscription: Subscription<PlayerState>? = null
    private var playerContextSubscription: Subscription<PlayerContext>? = null
    private var capabilitiesSubscription: Subscription<Capabilities>? = null
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private lateinit var views: List<View>
    private lateinit var trackProgressBar: TrackProgressBar
    private lateinit var binding: AppRemoteLayoutBinding
    private val errorCallback = { throwable: Throwable -> logError(throwable) }
    private lateinit var swipeViewModel: SwipeViewModel
    private lateinit var imageView: ImageView
    var jsonObject = JSONObject()
    lateinit var requestBody: RequestBody
    lateinit var jsonObjectString: String

    private val playerContextEventCallback =
        Subscription.EventCallback<PlayerContext> { playerContext ->
            binding.currentContextLabel.apply {
                text =
                    String.format(Locale.US, "%s\n%s", playerContext.title, playerContext.subtitle)
                tag = playerContext
            }
        }

    private val playerStateEventCallback = Subscription.EventCallback<PlayerState> { playerState ->
        Log.v(TAG, String.format("Player State: %s", gson.toJson(playerState)))

        updateTrackStateButton(playerState)
        updatePlayPauseButton(playerState)
        updateTrackCoverArt(playerState)
        updateSeekbar(playerState)
    }

    private fun updatePlayPauseButton(playerState: PlayerState) {
        // Invalidate play / pause
        if (playerState.isPaused) {
            binding.playPauseButton.setImageResource(R.drawable.btn_play)
        } else {
            binding.playPauseButton.setImageResource(R.drawable.btn_pause)
        }
    }

    private fun updateTrackStateButton(playerState: PlayerState) {
        binding.currentTrackLabel.apply {
            text = String.format(
                Locale.US,
                "%s\n%s",
                playerState.track.name,
                playerState.track.artist.name
            )
            tag = playerState
        }
    }

    private fun AppCompatImageButton.setTint(@ColorInt tint: Int) {
        DrawableCompat.setTint(drawable, Color.WHITE)
    }

    private fun updateSeekbar(playerState: PlayerState) {
        // Update progressbar
        trackProgressBar.apply {
            if (playerState.playbackSpeed > 0) {
                unpause()
            } else {
                pause()
            }
            // Invalidate seekbar length and position
            binding.seekTo.max = playerState.track.duration.toInt()
            binding.seekTo.isEnabled = true
            setDuration(playerState.track.duration)
            update(playerState.playbackPosition)
        }
    }

    private fun updateTrackCoverArt(playerState: PlayerState) {
        // Get image from track
        assertAppRemoteConnected()
            .imagesApi
            .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
            .setResultCallback { bitmap ->
                binding.image.setImageBitmap(bitmap)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        jsonObject.put("Content-Type", "application/json")
        jsonObjectString = jsonObject.toString()
        requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        binding = AppRemoteLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        imageView = findViewById(R.id.image)
        imageView.setOnTouchListener(object : OnSwipeTouchListener() {

            override fun onSwipeBottom() {
                val intent = Intent(this@SwipeActivity, ViewCurrentPlaylistActivity::class.java)
                startActivity(intent)
                Log.d("Hi", "TOPTOPTOTPP")
            }

            override fun onSwipeRight() {
                Log.d("SWIPE", "RIGHT")
                assertAppRemoteConnected()
                    .playerApi
                    .skipNext()
                    .setResultCallback {
                        logMessage(
                            getString(
                                R.string.command_feedback,
                                "skip previous"
                            )
                        )
                    }
                    .setErrorCallback(errorCallback)

                val resultsPlaybackId: Call<Playback> =
                    Utils.spotifyService.getCurrentPlayback(
                        "Bearer " + Utils.token
                    )

                resultsPlaybackId.enqueue(object : Callback<Playback> {
                    override fun onResponse(
                        call: Call<Playback>,
                        response: Response<Playback>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                Utils.currentPlayback = it
                            }
                        }
                    }

                    override fun onFailure(call: Call<Playback>, t: Throwable) {
                        t.printStackTrace()
                    }
                })

                GlobalScope.launch {
                    delay(500)
                    val resultsAddItem: Call<Any> = Utils.spotifyService.addItemsToPlaylist(
                        "Bearer " + Utils.token,
                        Utils.currentPlaylistId,
                        Utils.currentPlayback.item!!.uri!!
                    )
                    resultsAddItem.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {
                                Log.d("TAG", "Add an item to a playlist successfully")
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }

            override fun onSwipeLeft() {
                Log.d("SWIPE", "LEFT")
                assertAppRemoteConnected()
                    .playerApi
                    .skipNext()
                    .setResultCallback {
                        logMessage(
                            getString(
                                R.string.command_feedback,
                                "skip previous"
                            )
                        )
                    }
                    .setErrorCallback(errorCallback)
            }
        })

        swipeViewModel = ViewModelProvider(this).get(SwipeViewModel::class.java)

        binding.seekTo.apply {
            isEnabled = false
            progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        }

        trackProgressBar =
            TrackProgressBar(binding.seekTo) { seekToPosition: Long -> seekTo(seekToPosition) }

        views = listOf(
            binding.subscribeToPlayerContextButton,
            binding.subscribeToPlayerStateButton,
            binding.playPauseButton,
            binding.skipPrevButton,
            binding.skipNextButton,
            binding.seekTo
        )


        SpotifyAppRemote.setDebugMode(true)
        connectRemote()
    }


    fun exitActivity() {
        finish()
    }

    fun connectRemote() {
        //SpotifyAppRemote.disconnect(spotifyAppRemote)
        SpotifyAppRemote.connect(
            application,
            ConnectionParams.Builder(Utils.CLIENT_ID)
                .setRedirectUri(Utils.REDIRECT_URI)
                .showAuthView(true)
                .build(),
            object : Connector.ConnectionListener {
                override fun onConnected(p0: SpotifyAppRemote?) {
                    p0?.let { spotifyAppRemote = it }
                    getSelectedGenres()
                    onConnected()
                }

                override fun onFailure(p0: Throwable?) {
                    p0?.let { it.printStackTrace() }
                }
            }
        )
    }

    fun getSelectedGenres() {

        val token = Utils.token
        val selectedGenres = Utils.selectedGenres
        val genreQuery = selectedGenres.joinToString(separator = "%2C")

        if (!TextUtils.equals("", genreQuery.trim())) {
            swipeViewModel.loadGenreRecResults(genreQuery)
        } else {
            swipeViewModel.loadSongRecResults(Utils.oneSongSeed)
        }
        swipeViewModel.getRecResults().observe(this, {
            it?.let {
                Utils.recommendations = it
                queueRecommendations()
            }
        })
    }

//    fun onPlayTrackButtonClicked(notUsed: View) {
//        playUri(TRACK_URI)
//    }

    private fun seekTo(seekToPosition: Long) {
        assertAppRemoteConnected()
            .playerApi
            .seekTo(seekToPosition)
            .setErrorCallback(errorCallback)
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    private fun onConnected() {
        onSubscribedToPlayerStateButtonClicked(binding.subscribeToPlayerStateButton)
        onSubscribedToPlayerContextButtonClicked(binding.subscribeToPlayerContextButton)
        spotifyAppRemote?.run {
            //playerApi.play("spotify:track:2Iq6HhIquO7JKr0KfTNLzU")
            playerApi.subscribeToPlayerState().setEventCallback {
                val track = it.track
                track?.run {
                    Log.d(TAG, name + "by" + artist.name)
                }
            }
            //queueRecommendations()
        }


    }

    fun getSmartphoneId(): String? {
        Utils.deviceId.devices?.let {
            for (d in it) {
                d?.run {
                    if (type == "Smartphone") {
                        return d.id
                    }
                }
            }
        }
        return null
    }

    fun queueRecommendations() {
        GlobalScope.launch(CoroutineName("RushGun")) {
            runBlocking {
                for (rec in Utils.recommendations.tracks!!) {
                    getSmartphoneId()?.let { phoneId ->
                        rec?.uri?.let { uri ->
                            Utils.spotifyService.queueSong(
                                "Bearer " + Utils.token,
                                phoneId,
                                uri
                            )
                        }
                    }?.enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {
                                if (rec != null) {
                                    response.let { rec.uri?.let { it -> Utils.likedSongs.add(it) } }
                                    Log.d(TAG, "Added " + rec.name + " successfully!")
                                }
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }
        }
    }


    fun playRec() {
        for (r in Utils.recommendations.tracks!!) {
            spotifyAppRemote?.run {
                val uri = r?.run {
                    if (uri != null) {
                        playUri(uri)
                    }
                }
                //playerApi.play()
            }
        }
    }

    private fun playUri(uri: String) {
        assertAppRemoteConnected()
            .playerApi
            .play(uri)
            .setResultCallback { logMessage(getString(R.string.command_feedback, "play")) }
            .setErrorCallback(errorCallback)
    }

    fun showCurrentPlayerContext(view: View) {
        view.tag?.let {
            showDialog("PlayerContext", gson.toJson(it))
        }
    }

    fun showCurrentPlayerState(view: View) {
        view.tag?.let {
            showDialog("PlayerState", gson.toJson(it))
        }
    }

    fun onSkipPreviousButtonClicked(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .skipNext()
            .setResultCallback { logMessage(getString(R.string.command_feedback, "skip previous")) }
            .setErrorCallback(errorCallback)
    }

    fun onPlayPauseButtonClicked(notUsed: View) {
        assertAppRemoteConnected().let {
            it.playerApi
                .playerState
                .setResultCallback { playerState ->
                    if (playerState.isPaused) {
                        it.playerApi
                            .resume()
                            .setResultCallback {
                                logMessage(
                                    getString(
                                        R.string.command_feedback,
                                        "play"
                                    )
                                )
                            }
                            .setErrorCallback(errorCallback)
                    } else {
                        it.playerApi
                            .pause()
                            .setResultCallback {
                                logMessage(
                                    getString(
                                        R.string.command_feedback,
                                        "pause"
                                    )
                                )
                            }
                            .setErrorCallback(errorCallback)
                    }
                }
        }
    }

    fun onSkipNextButtonClicked(notUsed: View) {
        assertAppRemoteConnected()
            .playerApi
            .skipNext()
            .setResultCallback { logMessage(getString(R.string.command_feedback, "skip next")) }
            .setErrorCallback(errorCallback)

        runBlocking {
            launch(Dispatchers.IO) {
                val resultsPlaybackId: Call<Playback> =
                    Utils.spotifyService.getCurrentPlayback(
                        "Bearer " + Utils.token
                    )
                resultsPlaybackId.enqueue(object : Callback<Playback> {
                    override fun onResponse(
                        call: Call<Playback>,
                        response: Response<Playback>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                Utils.currentPlayback = it
                            }
                        }
                    }

                    override fun onFailure(call: Call<Playback>, t: Throwable) {
                        t.printStackTrace()
                    }
                })

                delay(100)

                val resultsAddItem: Call<Any> = Utils.spotifyService.addItemsToPlaylist(
                    "Bearer " + Utils.token,
                    Utils.currentPlaylistId,
                    Utils.currentPlayback.item!!.uri!!
                )
                resultsAddItem.enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful) {
                            Log.d("TAG", "Add an item to a playlist successfully")
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }
    }


    private suspend fun loadRootRecommendations(appRemote: SpotifyAppRemote): ListItems? =
        suspendCoroutine { cont ->
            appRemote.contentApi
                .getRecommendedContentItems(ContentApi.ContentType.FITNESS)
                .setResultCallback { listItems -> cont.resume(listItems) }
                .setErrorCallback { throwable ->
                    errorCallback.invoke(throwable)
                    cont.resumeWithException(throwable)
                }
        }


    fun onSubscribedToPlayerContextButtonClicked(notUsed: View) {
        playerContextSubscription = cancelAndResetSubscription(playerContextSubscription)

        binding.currentContextLabel.visibility = View.VISIBLE
        binding.subscribeToPlayerContextButton.visibility = View.INVISIBLE
        playerContextSubscription = assertAppRemoteConnected()
            .playerApi
            .subscribeToPlayerContext()
            .setEventCallback(playerContextEventCallback)
            .setErrorCallback { throwable ->
                binding.currentContextLabel.visibility = View.INVISIBLE
                binding.subscribeToPlayerContextButton.visibility = View.VISIBLE
                logError(throwable)
            } as Subscription<PlayerContext>
    }

    fun onSubscribedToPlayerStateButtonClicked(notUsed: View) {
        playerStateSubscription = cancelAndResetSubscription(playerStateSubscription)

        binding.currentTrackLabel.visibility = View.VISIBLE
        binding.subscribeToPlayerStateButton.visibility = View.INVISIBLE

        playerStateSubscription = assertAppRemoteConnected()
            .playerApi
            .subscribeToPlayerState()
            .setEventCallback(playerStateEventCallback)
            .setLifecycleCallback(
                object : Subscription.LifecycleCallback {
                    override fun onStart() {
                        logMessage("Event: start")
                    }

                    override fun onStop() {
                        logMessage("Event: end")
                    }
                })
            .setErrorCallback {
                binding.currentTrackLabel.visibility = View.INVISIBLE
                binding.subscribeToPlayerStateButton.visibility = View.VISIBLE
            } as Subscription<PlayerState>
    }


    private fun <T : Any?> cancelAndResetSubscription(subscription: Subscription<T>?): Subscription<T>? {
        return subscription?.let {
            if (!it.isCanceled) {
                it.cancel()
            }
            null
        }
    }

    private fun assertAppRemoteConnected(): SpotifyAppRemote {
        spotifyAppRemote?.let {
            if (it.isConnected) {
                return it
            }
        }
        Log.e(TAG, getString(R.string.err_spotify_disconnected))
        throw SpotifyDisconnectedException()
    }

    private fun logError(throwable: Throwable) {
        Toast.makeText(this, R.string.err_generic_toast, Toast.LENGTH_SHORT).show()
        Log.e(TAG, "", throwable)
    }

    private fun logMessage(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, duration).show()
        Log.d(TAG, msg)
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).create().show()
    }

}
