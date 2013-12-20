/**
 * 
 */
package fr.spiritofborepaire.bertille;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * SMS broadcast to intercept and notify specific sms.
 */
public class SmsNotifierReceiver extends BroadcastReceiver {

    public final static String VIEW = "1";
    public final static String YOU_SAVE_ME = "2";
    public final static String FAILED = "3";

    public final static String ID = "notificationID";
    public final static String CONTACT = "contactNumber";

    @Override
    public void onReceive(Context context, Intent intent) {

	List<SmsMessage> sms = this.getSms(intent);
	if (this.isSmsForMe(sms.get(0).getMessageBody())) {
	    // create notification
	    String title = sms.get(0).getOriginatingAddress() + " : " + sms.get(0).getMessageBody().substring(0, 5);
	    String ticker = sms.get(0).getMessageBody().substring(0, 5);
	    this.notify(context, sms.get(0).getOriginatingAddress(), title, sms.get(0).getMessageBody(), ticker);
	}
    }

    /**
     * Notify for application.
     * 
     * @param context the context of application.
     * @param contactNumber the contact number of sender
     * @param title the title of notification.
     * @param content the content of notification.
     * @param ticker the message display in top bar.
     */
    private void notify(Context context, String contactNumber, String title, String content, String ticker) {
	NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

	int notificationId = (int) new Date().getTime();

	Builder builder = new Notification.Builder(context);
	builder.setSmallIcon(R.drawable.ic_launcher);
	builder.setTicker(ticker);
	builder.setWhen(System.currentTimeMillis());
	builder.setAutoCancel(true);
	builder.setContentTitle(title);
	builder.setContentText(content);

	this.addButton(builder, context, contactNumber, notificationId, SmsNotifierReceiver.VIEW, R.drawable.vue, "");
	this.addButton(builder, context, contactNumber, notificationId, SmsNotifierReceiver.YOU_SAVE_ME, R.drawable.vue_grace_a_toi, "");
	this.addButton(builder, context, contactNumber, notificationId, SmsNotifierReceiver.FAILED, R.drawable.loupe, "");
	Notification notification = builder.build();

	mNotificationManager.notify(notificationId, notification);
    }

    private void addButton(Builder builder, Context context, String contactNumber, int notificationId, String action, int logo, String text) {
	Intent defineIntent = new Intent(context, SmsResponseReceiver.class);
	defineIntent.setAction(action);
	defineIntent.putExtra(SmsNotifierReceiver.ID, notificationId);

	defineIntent.putExtra(SmsNotifierReceiver.CONTACT, contactNumber);
	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, defineIntent, Intent.FILL_IN_DATA);
	builder.addAction(logo, text, pendingIntent);
    }

    /**
     * Return the list of SMS messages.
     * 
     * @param intent the intent of SMS broadcast.
     * @return the list with all part of message.
     */
    private List<SmsMessage> getSms(Intent intent) {
	Bundle bundle = intent.getExtras();
	Object[] messages = (Object[]) bundle.get("pdus");
	List<SmsMessage> sms = new ArrayList<SmsMessage>();
	for (Object message : messages) {
	    sms.add(SmsMessage.createFromPdu((byte[]) message));
	}
	return sms;
    }

    /**
     * Check if SMS is a SMS manage by this application.
     * 
     * @param smsContent the content of SMS
     * @return true if the SMS must be manage by this application.
     */
    private boolean isSmsForMe(String smsContent) {
	// if SMS don't have possibility content hour
	if (smsContent == null || "".equals(smsContent) || smsContent.length() < 5) {
	    return false;
	}
	// compare hour and minutes. True if is equals
	String hours = smsContent.substring(0, 2);
	String label = smsContent.substring(2, 3);
	String minutes = smsContent.substring(3, 5);

	if (hours.equals(minutes) && label.equals("h")) {
	    return true;
	} else {
	    return false;
	}
    }
}
