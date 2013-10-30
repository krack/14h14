package fr.spiritofborepaire.bertille;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    private static final int CONTACT_PICKER_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.setContentView(R.layout.activity_main);

	Button chooseContact = (Button) this.findViewById(R.id.chooseContact);
	chooseContact.setOnClickListener(this);

	this.initReceiver();
    }

    /**
     * Initialize the receiver configuration part.
     */
    private void initReceiver() {
	// load saved receiver
	Receiver receiver = new Receiver(this);
	if (receiver.isDefine()) {
	    ((TextView) this.findViewById(R.id.name)).setText(receiver.getDisplayName() + " : " + receiver.getPhoneNumber());
	}
	// if no receiver define
	else {
	    ((TextView) this.findViewById(R.id.name)).setText(R.string.receiver_not_saved);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	this.getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    /**
     * Call where click on choose contact.
     */
    public void onClick(View v) {
	// TODO : make possible only the mobile phone
	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
	contactPickerIntent.setType(Phone.CONTENT_TYPE);
	this.startActivityForResult(contactPickerIntent, MainActivity.CONTACT_PICKER_RESULT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == Activity.RESULT_OK) {
	    switch (requestCode) {
	    case CONTACT_PICKER_RESULT:
		Uri result = data.getData();
		this.savePhoneNumber(result);
		break;
	    }
	}
    }

    /**
     * Save the phone number
     * 
     * @param uri uri of phone number
     */
    private void savePhoneNumber(Uri uri) {
	// get phone number info
	Cursor contactPhones = this.getContentResolver().query(uri, null, null, null, null);
	contactPhones.moveToFirst();

	// get information of selected phone
	String id = contactPhones.getString(contactPhones.getColumnIndex(BaseColumns._ID));
	String displayName = contactPhones.getString(contactPhones.getColumnIndex(Contacts.DISPLAY_NAME));
	String number = contactPhones.getString(contactPhones.getColumnIndex(Phone.NUMBER));

	// save the receiver
	Receiver receiver = new Receiver(id, displayName, number, this);
	receiver.save();

	// update the receiver
	this.initReceiver();
    }
}
