package com.russvkm.askacharya.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.russvkm.askacharya.R
import com.russvkm.askacharya.adapter.VideoAdapter
import com.russvkm.askacharya.models.Video
import com.russvkm.askacharya.models.VideoModel
import com.russvkm.askacharya.utils.BaseClass
import com.russvkm.askacharya.utils.Constants
import com.russvkm.askacharya.utils.NotificationClass
import com.squareup.picasso.Picasso
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL


class VideoFragment : BaseClass() {
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var channelImageView: ImageView
    private lateinit var channelTitleTextView: TextView
    private lateinit var channelButton: AppCompatButton
    private lateinit var unableToConnect: ConstraintLayout
    private lateinit var channelCardView: CardView
    private lateinit var refreshButton: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_video, container, false)
        videoRecyclerView = root.findViewById(R.id.videoRecyclerView)
        channelImageView = root.findViewById(R.id.channelImageView)
        channelTitleTextView = root.findViewById(R.id.channelTitleTextView)
        unableToConnect = root.findViewById(R.id.unableToConnect)
        channelButton = root.findViewById(R.id.channelButton)
        channelCardView = root.findViewById(R.id.channelCardView)
        refreshButton = root.findViewById(R.id.refreshButton)
        if (isNetworkAvailable()) {
            unableToConnect.visibility = View.GONE
            videoRecyclerView.visibility = View.VISIBLE
            channelCardView.visibility = View.VISIBLE
            FetchVideo().execute()
        } else {
            unableToConnect.visibility = View.VISIBLE
            videoRecyclerView.visibility = View.GONE
            channelCardView.visibility = View.GONE
        }
        channelButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("${Constants.BASE_CHANNEL_URL}${Constants.CHANNEL_ID}")
            ContextCompat.startActivity(requireContext(), openURL, null)
        }

        refreshButton.setOnClickListener {
            recreate(requireActivity())
        }
        return root
    }

    @SuppressLint("StaticFieldLeak")
    private inner class FetchVideo() : AsyncTask<Any, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showDialog(requireContext().getString(R.string.pls_wait))
        }

        override fun doInBackground(vararg params: Any?): String {
            var result: String
            val connection: HttpURLConnection?
            try {
                val url = URL("${Constants.BASE_URL}${Constants.CHANNEL_ID}${Constants.API}")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                val connectionCode: Int = connection.responseCode
                if (connectionCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    result = stringBuilder.toString()
                } else {
                    result = connection.responseMessage
                }
            } catch (e: SocketTimeoutException) {
                result = "Connection TimeOut"
            } catch (e: Exception) {
                result = "Error : ${e.message}"
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            dismissDialog()
            if (!(result.isNullOrEmpty())) {
                val finalVideoArrayList: ArrayList<Video> = ArrayList()
                val allVideo = Gson().fromJson(result, VideoModel::class.java)
                for (items in allVideo.items.subList(1, allVideo.items.size)) {
                    val value = Video(
                        items.snippet.title,
                        items.snippet.description,
                        "${Constants.BASE_VIDEO_URL}${items.id.videoId}",
                        items.snippet.thumbnails.default.url,
                        items.snippet.publishTime
                    )
                    finalVideoArrayList.add(value)
                }
                videoRecyclerView.layoutManager = LinearLayoutManager(context)
                videoRecyclerView.setHasFixedSize(true)
                val adapter = context?.let { VideoAdapter(it, finalVideoArrayList) }
                videoRecyclerView.adapter = adapter
                Picasso.get()
                    .load(allVideo.items[0].snippet.thumbnails.medium.url)
                    .placeholder(R.drawable.youtube)
                    .into(channelImageView)
                channelTitleTextView.text = allVideo.items[0].snippet.channelTitle


                if (NotificationClass(
                        requireContext(),
                        requireActivity()
                    ).fetchAllNotificationPreference(
                        Constants.VIDEO_NOTIFICATION_PREF_NAME,
                        Constants.VIDEO_NOTIFICATION_PREF_KEY
                    ) != adapter!!.itemCount &&
                    NotificationClass(
                        requireContext(),
                        requireActivity()
                    ).fetchAllNotificationPreference("video_pref", "video_key") != 1 &&
                    NotificationClass(
                        requireContext(),
                        requireActivity()
                    ).fetchAllNotificationPreference("all_notification", "notification") != 1
                ) {
                    NotificationClass(
                        requireContext(),
                        requireActivity()
                    ).configuringNotificationChannel()
                    NotificationClass(requireContext(), requireActivity())
                        .displayingNotification(
                            requireContext()
                                .getString(R.string.new_video_title),
                            requireContext()
                                .getString(R.string.new_video_notification_body),
                            R.drawable.app_icon
                        )
                    NotificationClass(
                        requireContext(),
                        requireActivity()
                    ).saveAllNotificationPreference(
                        adapter.itemCount,
                        Constants.VIDEO_NOTIFICATION_PREF_NAME,
                        Constants.VIDEO_NOTIFICATION_PREF_KEY
                    )
                }
            } else {
                unableToConnect.visibility = View.VISIBLE
                videoRecyclerView.visibility = View.GONE
                channelCardView.visibility = View.GONE
            }
        }
    }

}