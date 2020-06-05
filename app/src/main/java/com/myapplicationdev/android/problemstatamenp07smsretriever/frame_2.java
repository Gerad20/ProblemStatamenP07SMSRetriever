package com.myapplicationdev.android.problemstatamenp07smsretriever;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;



/**
 * A simple {@link Fragment} subclass.
 */
public class frame_2 extends Fragment {

    EditText words;
    TextView displaySmsFromWord;
    Button Reteieve;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frame_2, container, false);
        words = view.findViewById(R.id.word);
        displaySmsFromWord = view.findViewById(R.id.smsDisplays);
        Reteieve = view.findViewById(R.id.btnGet1);

        Reteieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String ChosenWord = words.getText().toString();
                int permissionCheck = PermissionChecker.checkSelfPermission
                        (getActivity(), Manifest.permission.READ_SMS);

                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_SMS}, 0);
                    // stops the action from proceeding further as permission not
                    //  granted yet
                    return;
                }

                // Create all messages URI
                Uri uri = Uri.parse("content://sms");
                // The columns we want
                //  date is when the message took place
                //  address is the number of the other party
                //  body is the message content
                //  type 1 is received, type 2 sent
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                // Get Content Resolver object from which to
                //  query the content provider
                ContentResolver cr = getActivity().getContentResolver();
                // Fetch SMS Message from Built-in Content Provider
                String filter="body LIKE ?";
                // The matches for the ?
                String[] filterArgs = {"%" + ChosenWord + "%"};

                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                displaySmsFromWord.setText(smsBody);
            }
        });


        return view;
    }
}
