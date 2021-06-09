package com.vimalvijay.contactfetcher.callreceivers

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.vimalvijay.contactfetcher.R
import com.vimalvijay.contactfetcher.helpers.SharedDatas


class GetCallReceiver : IncomingCallReceiver() {

    lateinit var sharedDatas: SharedDatas
    lateinit var callerName: String

    companion object {
        var windowManager2: WindowManager? = null
        var view: View? = null
    }

    override fun onIncomingCallStarted(ctx: Context?, number: String?, state: Int) {
        println("On Incoming Call")
        sharedDatas = SharedDatas(ctx!!)
        if (!number.isNullOrEmpty() && isContactedSelected(number)) {
            Handler().postDelayed(Runnable { showDialog(number, ctx, state) }, 1000)
        }

    }

    override fun onCallDismissed(ctx: Context?, number: String?, state: Int) {
        if (windowManager2 != null) {
            try {
                windowManager2?.removeView(view)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                windowManager2 = null
                view = null
            }
        }
    }

    private fun isContactedSelected(number: String): Boolean {
        if (sharedDatas.getData("ContactsSaved").isNullOrEmpty()) {
            return false
        } else {
            val array = sharedDatas.getData("ContactsSaved")?.split(",")
            for (i in array?.indices!!) {
                if (array[i].split("_")[1].equals(number, true)) {
                    callerName = array[i].split("_")[0]
                    return true
                }
            }
            return false
        }
    }

    private fun showDialog(number: String?, context: Context?, state: Int) {
        val LAYOUT_FLAG: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        if (windowManager2 == null) {
            windowManager2 = context?.getSystemService(WINDOW_SERVICE) as WindowManager?
            val layoutInflater =
                context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            view = layoutInflater!!.inflate(R.layout.caller_dialog, null)
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

            params.gravity = Gravity.CENTER or Gravity.CENTER
            params.x = 0
            params.y = 0


            val tvName = view?.findViewById(R.id.tvName) as TextView
            val tvNumber = view?.findViewById(R.id.tvNumber) as TextView
            val ivClose = view?.findViewById(R.id.iv_close) as ImageView
            tvName.text = callerName
            tvNumber.text = number
            ivClose.setOnClickListener {
                try {
                    windowManager2?.removeView(view)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                windowManager2?.addView(view, params)
            }
        }
    }
}