package jp.techacademy.miyu.oshima.autoslideshowapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    open class Permissions : AppCompatActivity() {}


        private var mTimer: Timer? = null

    //タイマー変数
    private var mTimerSec = 0.0
    private var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {

        repeatOrstop.text = "再生"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        proceed.setOnClickListener {
            if (mTimer == null) {
                mTimer = Timer()

            }
        }
        back.setOnClickListener{
            if(mTimer != null){
                mTimer!!.cancel()
                mTimer = null
            }
        }
        repeatOrstop.setOnClickListener{
            if(mHandler != null) {
                //ハンドラのメソッド呼び出す
                loadingDelay()
                proceed.visibility = View.GONE
                back.visibility = View.GONE
                repeatOrstop.text = "停止"
           }
        }
    }

    //画面の遷移を遅延するためのメソッド
    fun loadingDelay(){

        //ハンドラを生成し、遅延時間を2秒に設定
        mHandler.postDelayed({
            //2秒以降に画面を遷移するためのIntent設定
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }, 2000)
    }




}



