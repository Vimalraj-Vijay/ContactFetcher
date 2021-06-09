package com.vimalvijay.contactfetcher.callreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

abstract class IncomingCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("On BroadcastReceiver Call")
        if (!intent?.action.equals("android.intent.action.NEW_OUTGOING_CALL")) {
            println("On BroadcastReceiver")
            val stateStr = intent!!.extras!!.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            println("Gt Phone nUmnber " + number)
            var state = 0
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING
            }
            onCallStateChanged(context, state, number)
        }
    }

    open fun onIncomingCallStarted(ctx: Context?, number: String?, state: Int) {}

    open fun onCallDismissed(ctx: Context?, number: String?, state: Int) {}

    private fun onCallStateChanged(ctx: Context?, state: Int, number: String?) {
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                onIncomingCallStarted(ctx, number, state)
            }
            else -> {
                onCallDismissed(ctx, number, state)
            }
        }
    }
}