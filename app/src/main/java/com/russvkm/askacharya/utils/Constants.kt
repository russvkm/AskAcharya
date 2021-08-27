package com.russvkm.askacharya.utils

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.russvkm.askacharya.models.Wisdom

object Constants {
    const val CAMERA_REQUEST_CODE_ONE=3
    const val GALLERY_REQUEST_CODE_ONE=4
    const val CAMERA_REQUEST_CODE_TWO=6
    const val GALLERY_REQUEST_CODE_TWO=8
    const val CAMERA_REQUEST_CODE_THREE=56
    const val GALLERY_REQUEST_CODE_THREE=32
    const val PERMISSION_REQUEST_CODE=9
    const val EVENT_DATA:String="events"
    const val PANDIT_DATA:String="pandits"
    const val ARTICLE_DATA:String="articles"
    const val IMAGES:String="Images"
    const val PANDIT_CAMERA_REQUEST_CODE=7
    const val PANDIT_GALLERY_REQUEST_CODE=9
    const val ARTICLE_CAMERA_REQUEST_CODE=2
    const val ARTICLE_GALLERY_REQUEST_CODE=5
    lateinit var DIALOG:BottomSheetDialog
    var imageUrl:String=""
    lateinit var imageOne:String
    lateinit var imageTwo:String
    lateinit var imageThree:String
    const val pathOne:String="Images/imageBitmapOne.jpg"
    const val pathTwo:String="Images/imageBitmapTwo.jpg"
    const val pathThree:String="Images/imageBitmapThree.jpg"
    const val ARG_OBJECT = "object"
    lateinit var ARTICLE_ARRAY_LIST:ArrayList<Wisdom>
    const val QUESTION:String="Question"
    const val FCM_QUESTION="fcm"
    const val NUMBER_OF_CURRENT_ARTICLE="current_article_number"
    const val PREF_KEY_ARTICLE="pref_key_article"
    const val ALL_QUESTION_FOR_USER="totalQuestion"
    const val TITLE="title"
    var ANSWER="answer"
    const val QUESTION_TO_UPDATE="question"
    const val ADMIN_UID:String="Admin_uid"
    const val SIGN_UP_REQUEST_CODE_CAMERA=12
    const val SIGN_UP_REQUEST_CODE_GALLERY=13
    const val BASE_URL="https://www.googleapis.com/youtube/v3/search?part=snippet&channelId="
    const val CHANNEL_ID="***********************"
    const val API="&maxResults=50&key=AIzaSyDnx17QH39quCxRajwF56W3Q4itAmPMeV4"
    const val BASE_VIDEO_URL="https://www.youtube.com/watch?v="
    const val BASE_CHANNEL_URL="https://www.youtube.com/channel/"
    const val FCM_TOKEN_KEY="*********************************************************************************************************************************************"
    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"
    const val VIDEO_NOTIFICATION_PREF_NAME="video_notification"
    const val VIDEO_NOTIFICATION_PREF_KEY="video_pref_key"
    const val UPDATE_USER_IMAGE_REQUEST_CODE_CAMERA=57
    const val UPDATE_USER_IMAGE_REQUEST_CODE_GALLERY=61
    var askQuestionSwitch:Boolean=false
}
