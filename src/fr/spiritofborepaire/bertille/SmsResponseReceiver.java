package fr.spiritofborepaire.bertille;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;
import fr.spiritofborepaire.bertille.exceptions.FailedActionException;

public class SmsResponseReceiver extends BroadcastReceiver {

    public final static String VIEW = "1";
    public final static String YOU_SAVE_ME = "2";
    public final static String FAILED = "3";

    public final static String ID = "notificationID";
    public final static String CONTACT = "contactNumber";

    @Override
    public void onReceive(Context context, Intent intent) {

	// hide status bar
	this.hideStatusBar(context);

	// close notification
	this.hideCurrentNotification(context, intent);

	// reponse
	this.sendMessage(context, intent);
    }

    /**
     * Hide the status bar.
     * 
     * @param context the contect
     */
    private void hideStatusBar(Context context) {
	try {
	    Object sbservice = context.getSystemService("statusbar");
	    Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
	    Method hidesb = statusbarManager.getMethod("collapse");
	    hidesb.invoke(sbservice);
	} catch (ClassNotFoundException e) {
	} catch (IllegalArgumentException e) {
	} catch (IllegalAccessException e) {
	} catch (InvocationTargetException e) {
	} catch (NoSuchMethodException e) {
	}
    }

    /**
     * Hide the notification send this code.
     * 
     * @param context the context
     * @param intent the intent
     */
    private void hideCurrentNotification(Context context, Intent intent) {
	int notificationId = Integer.parseInt(intent.getExtras().get(SmsResponseReceiver.ID).toString());
	NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	mNotificationManager.cancel(notificationId);
    }

    /**
     * Send the message of response.
     * 
     * @param context the context.
     * @param intent the intent.
     */
    private void sendMessage(Context context, Intent intent) {
	String contactNumber = intent.getExtras().get(SmsResponseReceiver.CONTACT).toString();
	if (intent.getAction().equals(SmsResponseReceiver.VIEW)) {
	    this.response(contactNumber, "Vu", context);
	} else if (intent.getAction().equals(SmsResponseReceiver.YOU_SAVE_ME)) {
	    this.response(contactNumber, "Vu grâce à toi :)", context);
	} else if (intent.getAction().equals(SmsResponseReceiver.FAILED)) {
	    this.response(contactNumber, "loupé", context);
	}
    }

    private void response(String receiverPhoneNumber, String message, Context context) {
	this.sendSms(receiverPhoneNumber, message, context);
	this.saveSms(receiverPhoneNumber, message, context);
	this.showSimpleMessage(message, context);
    }

    /**
     * Send SMS.
     * 
     * @param message the content of message.
     * @param context the context
     */
    private void sendSms(String phoneNumber, String message, Context context) {
	try {
	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
	} catch (Exception e) {
	    throw new FailedActionException();
	}
    }

    /**
     * Save SMS for have this in history.
     * 
     * @param message the message content.
     * @param context the context.
     */
    private void saveSms(String phoneNumber, String message, Context context) {
	ContentValues values = new ContentValues();
	values.put("address", phoneNumber.replace(" ", ""));
	values.put("body", message);
	context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    }

    /**
     * Show a simple message for the user.
     * 
     * @param message the message to display at user.
     * @param context the context
     */
    private void showSimpleMessage(String message, Context context) {
	// String message = context.getResources().getString(messageId);
	Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}
