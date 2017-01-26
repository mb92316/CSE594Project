package com.example.android.cse594project;

import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AddNote extends AppCompatActivity {
    DBHandler dbHandler;
    EditText noteField;
    ListView noteList;
    KeyStore keyStore;
    String KEY_NAME = "my_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list);
    }


    public void addNote(View view) {
        /*
        WITHOUT ENCRPYTION
        String n = noteField.getText().toString();
        dbHandler.addNote(n);
        finish();
        */
        try {
            String note = noteField.getText().toString();
            String encryptedString;
            byte[] encryptedNote;
            byte[] noteBytes;
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            noteBytes = note.getBytes();
            encryptedNote= cipher.doFinal(noteBytes);
            encryptedString = Base64.encodeToString(encryptedNote, Base64.DEFAULT);
            dbHandler.addNote(encryptedString);
            finish();
        } catch (UserNotAuthenticatedException e) {
            finish();
        } catch (BadPaddingException | IllegalBlockSizeException | KeyStoreException |
                CertificateException | UnrecoverableKeyException | IOException
                | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
