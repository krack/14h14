package fr.spiritofborepaire.bertille;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;
import fr.spiritofborepaire.bertille.exceptions.FailedActionException;
import fr.spiritofborepaire.bertille.exceptions.NoSavedReceiverException;

/**
 * Broadcast make to launch SMS.
 * 
 */
public class SmsWidgetReceiver extends BroadcastReceiver {
    /**
     * The custom message for specifics hours.
     */
    private final Map<Integer, String> customMessages;

    /**
     * 
     */
    public SmsWidgetReceiver() {

	// initilise the custom message for hour.
	this.customMessages = new HashMap<Integer, String>();

	this.customMessages.put(6, "Alice The Storm");
	this.customMessages.put(11, "Vadrouille !!");
	this.customMessages.put(14, "toi<3 Léchou");
	this.customMessages.put(22, "à poil !! Léchou !!");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

	Calendar currentDate = Calendar.getInstance();

	// check if call is make at the good time
	if (this.isTheTime(currentDate)) {

	    try {
		// get the message
		String message = this.getMessage(currentDate);
		// send the message
		this.sendSms(message, context);
		// save the message
		this.saveSms(message, context);
		// prevent user sms send
		this.showSimpleMessage(R.string.send_sms_send, context);
	    } catch (FailedActionException e) {
		// error : prevent the user.
		this.showSimpleMessage(R.string.send_sms_failed, context);
	    } catch (NoSavedReceiverException e) {
		// no saved user : prevent the user
		this.showSimpleMessage(R.string.send_sms_no_receiver, context);
	    }
	} else {
	    // prevent the user : is not the good time.
	    this.showSimpleMessage(R.string.send_sms_not_the_time, context);
	}
    }

    /**
     * Define if it's the time for work.
     * 
     * @param currentDate the current date.
     * @return true if is the time, false else.
     */
    private boolean isTheTime(Calendar currentDate) {
	int hour = currentDate.get(Calendar.HOUR_OF_DAY);
	int minutes = currentDate.get(Calendar.MINUTE);
	return hour == minutes;
    }

    /**
     * Return the message of the current date.
     * 
     * @param currentDate the current date.
     * @return the message for the current date.
     */
    private String getMessage(Calendar currentDate) {
	int hour = currentDate.get(Calendar.HOUR_OF_DAY);
	String number = null;
	if(hour >= 10){
	    number = ""+hour;
	}else{
	    number = "0"+hour;
	}
	
	// complete the message with the good hour and minutes
	String message = this.getFormatString(currentDate).replaceAll("%02d", number);
	return message;
    }

    /**
     * Return the format of string for the current date.
     * 
     * @param currentDate the current date.
     * @return the format of the date.
     */
    private String getFormatString(Calendar currentDate) {
	int hour = currentDate.get(Calendar.HOUR_OF_DAY);
	// get custom message of the hour
	String message = this.customMessages.get(hour);

	// if no custom message, message
	if (message == null) {
	    message = "";
	}
	//if custom message, add before 
	else{
	    message = " "+message;

	}

	message = getHourString(currentDate)+message;

	return message;
    }

    /**
     * Create the string of hour with all identique element of the date.
     * @param currentDate the current date
     * @return the string of hour with %02d for hours.
     */
    private String getHourString(Calendar currentDate){
	int hour = currentDate.get(Calendar.HOUR_OF_DAY);
	String hours = "%02dh";
	//if minutes is the same of hour, add minutes string
	if(hour == currentDate.get(Calendar.MINUTE)){
	    hours+="%02d";
	    //if second is the same of hour, add second string
	    if(hour == currentDate.get(Calendar.SECOND)){
		hours+="min%02ds";
	    }
	}

	//if the days is the same of hour, add day string
	if(hour == currentDate.get(Calendar.DAY_OF_MONTH)){
	    hours+=" le %02d";
	    //if the month is the same of hour, add month string
	    if(hour == currentDate.get(Calendar.MONTH)+1){
		hours+="/%02d";
		//if last 2 number of year is the same of hour, add year string
		if(hour == (currentDate.get(Calendar.YEAR)%100)){
		    hours+="/%02d";
		}
	    }
	}
	return hours;
    }

    /**
     * Send SMS.
     * 
     * @param message the content of message.
     * @param context the context
     */
    private void sendSms(String message, Context context) {
	try {
	    SmsManager smsManager = SmsManager.getDefault();
	    Receiver receiver = this.getReceiver(context);
	    // if no receiver saved
	    if (!receiver.isDefine())
		throw new NoSavedReceiverException();
	    smsManager.sendTextMessage(receiver.getPhoneNumber(), null, message, null, null);
	} catch (Exception e) {
	    throw new FailedActionException();
	}
    }

    /**
     * Return the current receiver saved.
     * 
     * @param context the context
     * @return the receiver saved.
     */
    private Receiver getReceiver(Context context) {
	Receiver receiver = new Receiver(context);
	return receiver;
    }

    /**
     * Save SMS for have this in history.
     * 
     * @param message the message content.
     * @param context the context.
     */
    private void saveSms(String message, Context context) {
	ContentValues values = new ContentValues();
	Receiver receiver = this.getReceiver(context);
	if (receiver.isDefine()) {
	    values.put("address", receiver.getPhoneNumber().replace(" ", ""));
	    values.put("body", message);
	}
	context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    }

    /**
     * Show a simple message for the user.
     * 
     * @param message the message to display at user.
     * @param context the context
     */
    private void showSimpleMessage(int messageId, Context context) {
	String message = context.getResources().getString(messageId);
	Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}
