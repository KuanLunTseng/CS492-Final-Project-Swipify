package com.kuanluntseng.swipify

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.kuanluntseng.swipify.addplaylist.AddPlaylistActivity
import com.kuanluntseng.swipify.data.devices.Devices
import com.kuanluntseng.swipify.data.genreseeds.GenreSeeds
import com.kuanluntseng.swipify.data.userprofile.UserProfile
import com.kuanluntseng.swipify.genre.GenreActivity
import com.kuanluntseng.swipify.genre.GenreItem
import com.kuanluntseng.swipify.settings.SettingsActivity
import com.kuanluntseng.swipify.song.SongActivity
import com.kuanluntseng.swipify.swipe.SwipeActivity
import com.kuanluntseng.swipify.swipe.ViewCurrentPlaylistActivity
import com.kuanluntseng.swipify.utils.Utils
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val TAG = MainActivity::class.java.simpleName
    val CLIENT_ID = "2baa0fc179d94869b28342364bd5ef98"
    val AUTH_TOKEN_REQUEST_CODE = 0x10

    private lateinit var drawerLayout: DrawerLayout
    private var userId: String? = null
    private lateinit var accessToken: String
    private lateinit var sharedPreferences: SharedPreferences

    private val okHttpClient = OkHttpClient()

    private lateinit var userNameTextView: TextView
    private lateinit var userAvatarImageView: ImageView
    private lateinit var navigationView: NavigationView
    private lateinit var userProfileImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestAccessToken()

        navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val navHeader = navigationView.getHeaderView(0)
        userNameTextView = navHeader.findViewById(R.id.user_name_textview)
        userAvatarImageView = navHeader.findViewById(R.id.user_avatar_imageview)
        userProfileImageView = findViewById(R.id.iv_profile_image)

        navigationView.setNavigationItemSelectedListener(this)
        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        setNightMode(
            this.sharedPreferences.getBoolean(
                getString(R.string.pref_dark_mode_key),
                false
            )
        );
    }

    override fun onDestroy() {
        super.onDestroy();
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun requestAccessToken() {
        val request = AuthorizationRequest
            .Builder(CLIENT_ID, TOKEN, getRedirectUri())
            .setShowDialog(false)
            .setScopes(
                arrayOf(
                    getString(R.string.user_read_email),
                    "user-read-playback-state",
                    "user-modify-playback-state",
                    "playlist-modify-public",
                    "playlist-modify-private"
                )
            )
            .build()
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    fun updateUserProfile() {
        val results: Call<UserProfile> =
            Utils.spotifyService.getUserProfile(getString(R.string.token_type) + " " + accessToken)
        results.enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    userNameTextView.text = responseBody?.displayName ?: "User Name"
                    userId = responseBody?.id
                    Utils.userId = responseBody!!.id!!
                    responseBody?.run {
                        val navHeader = navigationView.getHeaderView(0)
                        Glide.with(navHeader.context)
                            .load(images?.get(0)?.url)
                            .into(userAvatarImageView)

                        Glide.with(userProfileImageView)
                            .load(images?.get(0)?.url)
                            .into(userProfileImageView)
                    }
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getGenreSeeds() {
        val results: Call<GenreSeeds> = Utils.spotifyService.getGenreSeeds("Bearer " + Utils.token)
        results.enqueue((object : Callback<GenreSeeds> {
            override fun onResponse(call: Call<GenreSeeds>, response: Response<GenreSeeds>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        Utils.genreSeeds =
                            it.genres!!.map { GenreItem(it!!, false) } as ArrayList<GenreItem>
                    }
                }
            }

            override fun onFailure(call: Call<GenreSeeds>, t: Throwable) {
                t.printStackTrace()
            }
        }))
    }

    fun getDeviceId() {
        val results: Call<Devices> = Utils.spotifyService.getDeviceId("Bearer " + Utils.token)
        results.enqueue(object : Callback<Devices> {
            override fun onResponse(call: Call<Devices>, response: Response<Devices>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        Utils.deviceId = it
                    }
                }
            }

            override fun onFailure(call: Call<Devices>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

//    fun getDeviceId() {
//        val results: Call<DeviceId> = Utils.spotifyService.getDeviceId("Bearer " + Utils.token)
//        results.enqueue((object : Callback<DeviceId> {
//            override fun onResponse(call: Call<DeviceId>, response: Response<DeviceId>) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    responseBody?.let {
//                        Utils.deviceId = it.devices?.get(0)?.id.toString()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<DeviceId>, t: Throwable) {
//                t.printStackTrace()
//            }
//        }))
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val response = AuthorizationClient.getResponse(resultCode, intent)
        if (response.accessToken != null) {
            when (requestCode) {
                AUTH_TOKEN_REQUEST_CODE -> {
                    accessToken = response.accessToken
                    Utils.token = accessToken
                    updateUserProfile()
                    getGenreSeeds()
                    getDeviceId()
                }
                else -> {
                    Log.d(TAG, "onActivityResult: Error: Check your connection...")
                }
            }
        }
    }

    private fun getRedirectUri(): String {
        return Uri.Builder()
            .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(getString(R.string.com_spotify_sdk_redirect_host))
            .build()
            .toString()
    }

    fun startGenreActivity(view: View) {
        Utils.clearSeeds()
        startActivity(Intent(this, GenreActivity::class.java))
    }

    fun startSongActivity(view: View) {
        Log.d(TAG, "SongActivity: Starting activity")
        val intent = Intent(this, SongActivity::class.java)
        intent.putExtra(SongActivity.Companion.EXTRA_ACCESS_TOKEN, accessToken)
        intent.putExtra(SongActivity.Companion.EXTRA_USER_ID, userId)
        startActivity(intent)
    }

    fun startAddPlaylistActivity(view: View) {
        Log.d(TAG, "startAddPlaylistActivity: Starting activity")
        val intent = Intent(this, AddPlaylistActivity::class.java)
        intent.putExtra(AddPlaylistActivity.Companion.EXTRA_ACCESS_TOKEN, accessToken)
        startActivity(intent)
    }

    fun startSwipeActivity(view: View) {
        if (Utils.isOneSongSeedInit() || !Utils.selectedGenres.isNullOrEmpty()) {
            startActivity(Intent(this, SwipeActivity::class.java))
        }else {
            Toast.makeText(this, "Sorry, seeds are not initialized. Please use buttons above...", Toast.LENGTH_SHORT).show()
        }
//        when (Utils.selectedGenres.isNotEmpty()) {
//            true -> startActivity(Intent(this, SwipeActivity::class.java))
//            else -> Toast.makeText(this, "Select genre types before swiping...", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        return when (item.itemId) {
            R.id.nav_select_genre -> {
                val intent = Intent(this, GenreActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_select_song -> {
                startActivity(Intent(this, SongActivity::class.java))
                true
            }
            R.id.nav_add_playlist -> {
                val intent = Intent(this, AddPlaylistActivity::class.java)
                intent.putExtra(AddPlaylistActivity.Companion.EXTRA_ACCESS_TOKEN, accessToken)
                startActivity(intent)
                true
            }
            else -> {
                false
            }
        }
    }


    /**
     * Sets the app's night mode to the given [Boolean].
     */
    private fun setNightMode(isNightMode: Boolean) {
        Log.d(TAG, isNightMode.toString());
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Sets the night mode to the value in the given [SharedPreferences].
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setNightMode(
            this.sharedPreferences.getBoolean(
                getString(R.string.pref_dark_mode_key),
                false
            )
        );
    }
}
