package akashsarkar188.expensedaroga.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log


class SMSBroadcastReceiver : BroadcastReceiver() {

    private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"

    override fun onReceive(p0: Context?, intent: Intent?) {
        intent?.let { intent ->
            Log.e("XXX", "onReceive: " + intent )
            Log.e("XXX", "onReceive: " + intent.extras )
            if (intent.action === SMS_RECEIVED) {
                val bundle = intent.extras
                if (bundle != null) {
                    val pdus = bundle["pdus"] as Array<Any>?
                    val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus!!.size)
                    for (i in pdus!!.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus!![i] as ByteArray)
                    }
                    if (messages.size > -1) {
                        Log.i("XXX", "Message recieved: " + messages[0]?.getMessageBody())
                    }
                }
            }
        }
    }
}