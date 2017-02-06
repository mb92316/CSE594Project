package com.example.android.cse594project;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    KeyguardManager mKeyguardManager;
    EditText noteField;
    ListView noteList;
    DBHandler dbHandler;
    String KEY_NAME = "my_key";
    String PIN_KEY = "pin_key";
    Boolean showBool = false;
    int pinBool;
    private static final byte[] SECRET_BYTE_ARRAY = new byte[] {1, 2, 3, 4, 5, 6};
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        pinBool = pref.getInt("pinpadInt", 0);
        keyCheck();
        if(pinBool == 1) {
            tryEncrypt();
        }
        else {
            showBool = true;
        }
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

            entry = ks.getEntry(PIN_KEY, null);
            if (entry == null)
            {
                createPinKey();
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


    private void createPinKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(PIN_KEY,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(1)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException | KeyStoreException
                | CertificateException | IOException e) {
            throw new RuntimeException("Failed to create a symmetric key", e);
        }
    }


    private void tryEncrypt() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(PIN_KEY, null);
            Cipher cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cipher.doFinal(SECRET_BYTE_ARRAY);
            showBool = true;
        } catch (UserNotAuthenticatedException e) {
            showAuthenticationScreen();
        } catch (KeyPermanentlyInvalidatedException e) {
        } catch (BadPaddingException | IllegalBlockSizeException | KeyStoreException |
                CertificateException | UnrecoverableKeyException | IOException
                | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }


    private void showAuthenticationScreen() {
        Intent intent = mKeyguardManager.createConfirmDeviceCredentialIntent(null, null);
        if (intent != null) {
            startActivityForResult(intent, 2);
        }
    }

    public void settings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void newNote(View view) {
        Intent intent = new Intent(this, AddNote.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String newText = data.getStringExtra("noteinfo");
            Toast.makeText(this, newText, Toast.LENGTH_LONG).show();
            showNotes();
        }

        if (requestCode == 2){
            showBool = true;
            showNotes();
        }
    }

    public void showNotes() {
        if(showBool == true) {
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
}
