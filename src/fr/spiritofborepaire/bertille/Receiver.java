/**
 * 
 */
package fr.spiritofborepaire.bertille;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * The saved receiver representation
 */
public class Receiver {

    private static final String SAVED_PHONE_ID = "receiver.phoneid";
    private static final String SAVED_DISPLAY_NAME = "receiver.displayname";
    private static final String SAVED_PHONE_NUMBER = "receiver.phonenumber";

    private String id;
    private String displayName;
    private String phoneNumber;

    private final Context context;

    public Receiver(Context context) {
	this.context = context;
	SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	try {
	    this.id = defaultPreferences.getString(Receiver.SAVED_PHONE_ID, null);
	    this.displayName = defaultPreferences.getString(Receiver.SAVED_DISPLAY_NAME, null);
	    this.phoneNumber = defaultPreferences.getString(Receiver.SAVED_PHONE_NUMBER, null);
	} catch (Exception e) {
	}
    }

    /**
     * @param id
     * @param displayName
     * @param phoneNumber
     * @param context
     */
    public Receiver(String id, String displayName, String phoneNumber, Context context) {
	super();
	this.id = id;
	this.displayName = displayName;
	this.phoneNumber = phoneNumber;
	this.context = context;
    }

    /**
     * Check if the receiver is define.
     * 
     * @return true if the receiver is define, false else.
     */
    public boolean isDefine() {
	return this.displayName != null;
    }

    /**
     * Save the current information of receiver.
     */
    public void save() {
	SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
	Editor edit = defaultPreferences.edit();
	edit.putString(Receiver.SAVED_PHONE_ID, this.id);
	edit.putString(Receiver.SAVED_DISPLAY_NAME, this.displayName);
	edit.putString(Receiver.SAVED_PHONE_NUMBER, this.phoneNumber);
	edit.commit();
    }

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getDisplayName() {
	return this.displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getPhoneNumber() {
	return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

}
