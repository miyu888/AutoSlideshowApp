package jp.techacademy.miyu.oshima.autoslideshowapp

import android.content.Intent
import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.content.pm.PackageManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //Permissionコード
    private val PERMISSIONS_REQUEST_CODE = 100


    private var mTimer: Timer? = null
    //タイマー変数
    //private var mTimerSec = 0.0
    //private var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {

        proceed.text = "進む"
        back.text = "戻る"
        repeatOrstop.text = "再生"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Android6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //許可されている
                getContentsInfo()
            } else {
                //許可されていないので許可ダイアログを表示する
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            //Android5形以下の場合
        } else {
            getContentsInfo()
        }


        //ボタンを押すとスライドショー操作開始
        proceed.setOnClickListener {
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }
        back.setOnClickListener {
            if (mTimer != null) {
                mTimer!!.cancel()
                mTimer = null
            }
        }
        repeatOrstop.setOnClickListener {
            if (mTimer != null) {
                if (mTimer == null) {
                    mTimer = Timer()
                    mTimer!!.schedule(object: TimerTask(){
                        override fun run(){
                            //2秒以降に画面を遷移するためのIntent設定
                            val nextIntent = Intent()
                            startActivity(nextIntent)
                        }
                    },2000)
                }
                proceed.visibility = View.GONE
                back.visibility = View.GONE
                repeatOrstop.text = "停止"
            }else if(repeatOrstop.text == "停止"){
                return@setOnClickListener
                repeatOrstop.text = "再生"
            }
        }
    }



    //許可
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        //画像の情報を取得
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor!!.moveToFirst()) {
            //IndexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex =
                cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
        }
            cursor.close()
    }


}




