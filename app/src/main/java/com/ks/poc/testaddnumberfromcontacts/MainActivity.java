package com.ks.poc.testaddnumberfromcontacts;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Declare Variables
    ListView list;
    EditText searchTxt;
    String[] contactNames;
    String[] contactNumbers;
    int[] contactImages;
    ArrayList<TrueContact> trueContacts = new ArrayList<TrueContact>();
    boolean hasContacts = false;

    // Declare class variable for context
    public static Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(MainActivity.this, "MainActivity onCreate...", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // processing start here
        if (checkPermission()) {
            loadTrueContacts();
        }

        // Create fragment

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            FragmentManager fragmentManager = getFragmentManager();

            ListFragment lstFragment = new ListFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, lstFragment);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }

        // Locate the ListView in listview_main.xml
//        list = (ListView) findViewById(R.id.listSearchResult);
        searchTxt = (EditText) findViewById(R.id.search);

        thisContext = this;

        // Capture Text in EditText
        searchTxt.addTextChangedListener(new TextWatcher() {

            // pattern for all number or +
            private Pattern sPattern = Pattern.compile("^[0-9+]+$");
            // pattern for mobile number in Thailand
            private Pattern mobilePattern = Pattern.compile("^([+][6][6][6,8,9][0-9]{8}|[0][6,8,9][0-9]{8})$");

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = searchTxt.getText().toString().toLowerCase(Locale.getDefault());
                // Search for result
                ArrayList<TrueContact> searchResult;
                int searchType = searchType(text);
                searchResult = searchContacts(text, searchType, trueContacts);
                if (searchResult.size() == 0) {
                    if (searchType == 1) {
//                        Toast.makeText(MainActivity.this, "Not found in Contacts", Toast.LENGTH_SHORT).show();
                        showErrorFragment("Not found in Contacts");
                    }
                    if (searchType == 2) {
                        // Check that the entered number is the mobile number (e.g., length correct, prefix correct.
                        // Then, search whether entered number is True number or not. If so, show Add button. Otherwise, display error.
                        // Thai mobile phone has prefix of 6, 8, 9 followed by 8 digits number
                        // Format can be +66pxxxxxxxx or 0pxxxxxxxx
                        // Regex "^([+][6][6][6,8,9][0-9]{8}|[0][6,8,9][0-9]{8})$"
                        if (isMobileNumber(text)) {
                            // Search whether this is true number or not. If so, display add button. Otherwise, display error message.
                            if (text.startsWith("081")) {
                                //Toast.makeText(MainActivity.this, "True Number... ADD", Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new OkFragment());
                                fragmentTransaction.commit();
                                fragmentManager.executePendingTransactions();
                            } else {
                                //Toast.makeText(MainActivity.this, "Not TRUE number", Toast.LENGTH_SHORT).show();
                                showErrorFragment("Not TRUE number");
                            }
                        } else {
                            //Toast.makeText(MainActivity.this, "Not TRUE number", Toast.LENGTH_SHORT).show();
                            showErrorFragment("Not TRUE number");
                        }
                    }
                } else {
                    //Toast.makeText(MainActivity.this, "Found in Contacts", Toast.LENGTH_SHORT).show();
                    // Pass results to ListViewAdapter Class
                    ListViewAdapter adapter = new ListViewAdapter(MainActivity.thisContext, searchResult, searchType, text);
                    // Load Fragment containing ListView
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ListFragment lstFragment = new ListFragment();
                    fragmentTransaction.replace(R.id.fragment_container, lstFragment);
                    fragmentTransaction.commit();
                    fragmentManager.executePendingTransactions();
                    // Binds the Adapter to the ListView
                    lstFragment.lvResult.setAdapter(adapter);
                }
            }

            private void showErrorFragment(String errMsg) {
//                Bundle bundle = new Bundle();
//                bundle.putString("errmsg", errMsg);
//                errFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ErrorFragment errFragment = new ErrorFragment();
                fragmentTransaction.replace(R.id.fragment_container, errFragment);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                errFragment.tv.setText(errMsg);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }

            private int searchType(String text) {
                if (sPattern.matcher(text).matches()) {
                    return 2;
                } else
                    return 1;
            }

            private boolean isMobileNumber(String text) {
                if (mobilePattern.matcher(text).matches()) {
                    return true;
                } else
                    return false;
            }

            private ArrayList<TrueContact> searchContacts(String text, int searchType, ArrayList<TrueContact> trueContacts) {
                ArrayList<TrueContact> result = new ArrayList<TrueContact>();
                text = text.toLowerCase(Locale.getDefault());
                if (text.length() > 0) {
                    if (searchType == 1) {
                        for (TrueContact contact : trueContacts) {
                            if (contact.getContactName().toLowerCase(Locale.getDefault()).contains(text)) {
                                result.add(contact);
                            }
                        }
                    }
                    if (searchType == 2) {
                        for (TrueContact contact : trueContacts) {
                            if (contact.getContactNumber().toLowerCase(Locale.getDefault()).contains(text)) {
                                result.add(contact);
                            }
                        }
                    }
                }
                return result;
            }
        });

    }

    private void loadTrueContacts() {
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();
            cursor = contentResolver.query(uri, projection, null, null, sortOrder);
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI));
                TrueContact contactData = new TrueContact(contactId, name, phone, photoUri);
                trueContacts.add(contactData);
            }
            if (trueContacts.size() > 0) {
                hasContacts = true;
                Log.d("MainActivity", "TrueContacts Loaded (" + trueContacts.size() + ")");
            } else {
                hasContacts = false;
                Log.d("MainActivity", "TrueContacts NOT Loaded");
            }
        } catch (NullPointerException npe) {
            Log.e(getClass().getSimpleName(), "Error trying to get Contacts.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", " Does not have READ_CONTACTS permission");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    999);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 999: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", " READ_CONTACTS permission granted");
                    loadTrueContacts();
                } else {
                    Log.d("MainActivity", " User not granting READ_CONTACTS permission");
                }
                return;
            }
        }
    }
}
