package com.ks.poc.testaddnumberfromcontacts;

import android.telephony.PhoneNumberUtils;

/**
 * Created by Krit on 6/23/2016.
 */
public class TrueContact {

    private String contactID;
    private String contactName;
    private String contactNumber;
    private String contactImageUri;

    public TrueContact(String contactID, String contactName, String contactNumber, String contactImageUri) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactNumber = contactNumber.replace("-","").replace(" ","");
        this.contactImageUri = contactImageUri;
    }

    public String getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactImage() {
        return contactImageUri;
    }
}
