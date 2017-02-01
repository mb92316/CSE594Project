package com.example.android.cse594project;

import android.app.KeyguardManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    KeyguardManager mKeyguardManager;
    KeyStore keyStore;
    EditText noteField;
    ListView noteList;
    DBHandler dbHandler;
    SimpleCursorAdapter simpleCursorAdapter;
    String KEY_NAME = "my_key";
    String PIN_KEY = "pin_key";
    private static final String AndroidKeyStore = "AndroidKeyStore";
    SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list);
        keyCheck();
        showNotes();
    }

    public void keyCheck() {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(KEY_NAME, null);
            if (entry == null)
            {
                createKey();
            }
        } catch ( KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        }
    }


    private void createKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setRandomizedEncryptionRequired(false)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException | KeyStoreException
                | CertificateException | IOException e) {
            throw new RuntimeException("Failed to create a symmetric key", e);
        }
    }

    public void newNote(View view) {
        Intent intent = new Intent(this, AddNote.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            showNotes();
        }
    }

    public void showNotes() {
        Cursor cursor = dbHandler.getNotes();
        if (cursor != null) {

            noteCursor c = new noteCursor(this, cursor);
            noteList.setAdapter(c);
            noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adaptView, View view, int newInt,
                                        long newLong) {
                    LinearLayout parent = (LinearLayout) view;
                    LinearLayout child = (LinearLayout) parent.getChildAt(0);
                    TextView m = (TextView) child.getChildAt(1);
                    TextView k = (TextView) child.getChildAt(0);
                    String noteText = k.getText().toString();
                    Bundle bundle = new Bundle();
                    int id = Integer.parseInt(m.getText().toString());
                    bundle.putInt("id", id);
                    bundle.putString("notetext", noteText);
                    Intent intent = new Intent(getApplicationContext(), NoteHelper.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }
}
