package com.kuanluntseng.swipify.song

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.SpotifyService
import com.kuanluntseng.swipify.data.newplaylist.NewPlaylist
import com.kuanluntseng.swipify.genre.GenreActivity
import com.kuanluntseng.swipify.swipe.SwipeActivity
import com.kuanluntseng.swipify.utils.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

class NewPlaylistInfoDialogFragment: DialogFragment() {
    var playlistName: String? = null
    var playlistDescription: String? = null
    var playlistNameED: EditText? = null
    var playlistDescriptionED: EditText? = null
    var jsonObject = JSONObject()
    lateinit var requestBody: RequestBody
    lateinit var jsonObjectString: String
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            var dialogView = inflater.inflate(R.layout.dialog_new_playlist, null)
            playlistDescriptionED = dialogView.findViewById(R.id.dialog_playlist_description)
            playlistNameED = dialogView.findViewById(R.id.dialog_playlist_name)
            builder
                .setView(dialogView)
                .setPositiveButton("Done",
                    DialogInterface.OnClickListener { dialog, id ->
                        //check if name and description is not null, if yes then prompt error, do nothing
                        // else make api call
                        Log.d("DIALOG_LOG", "DONE clicked")
                        Log.d("DIALOG_LOG", "Playlist name: " + playlistNameED!!.text.toString())
                        Log.d("DIALOG_LOG", "Playlist description: " + playlistDescriptionED!!.text.toString())
                        jsonObject.put("name", playlistNameED!!.text.toString())
                        jsonObject.put("description", playlistDescriptionED!!.text.toString())
                        jsonObject.put("public", "true")

                        jsonObjectString = jsonObject.toString()
                        requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                        createNewPlaylist()
                        val intent = Intent(activity, SwipeActivity::class.java)
                        startActivity(intent)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        Log.d("DIALOG_LOG","Cancel clicked")
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun createNewPlaylist() {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.spotify_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val spotifyService = retrofit.create(SpotifyService::class.java)

        val results: Call<NewPlaylist> =
            spotifyService.createNewPlaylist(getString(R.string.token_type) + " " + Utils.token,Utils.userId, requestBody)
        results.enqueue(object : Callback<NewPlaylist> {
            override fun onResponse(
                call: Call<NewPlaylist>,
                response: Response<NewPlaylist>
            ) {
                if (response.isSuccessful) {
                    val spotifyResult = response.body()
                    Log.d("PLAYLIST_LOG", "onResponse: " + spotifyResult)

                    spotifyResult?.run {
                        Utils.currentPlaylistId = spotifyResult.id!!
                    }
                } else {
                    Log.d("PLAYLIST_LOG", "onResponse: The request was not successful")
                    Log.d("PLAYLIST_LOG", "onResponse Else: " + response.code())
                }

            }

            override fun onFailure(call: Call<NewPlaylist>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}