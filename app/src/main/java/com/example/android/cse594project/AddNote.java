package com.example.android.cse594project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.security.KeyStore;

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


        String n = noteField.getText().toString();
        TokenEncryptor tokenEncryptor = new TokenEncryptor();
        String encryptedNote = tokenEncryptor.encrypt(n);
        dbHandler.addNote(encryptedNote);
        finish();

        /*
        try {
            String note = noteField.getText().toString();
            String encryptedString;
            byte[] encryptedNote;
            byte[] noteBytes;
            GetKey key = new GetKey();
            SecretKey secretKey = key.getKey();
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            byte[] ivbytes = new byte[ 16 ];
            IvParameterSpec iv = new IvParameterSpec(ivbytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            noteBytes = note.getBytes("utf-8");
            encryptedNote = cipher.doFinal(noteBytes);
            byte[] ivAndCipherText = getCombinedArray(ivbytes, encryptedNote);
           //encryptedString = Base64.encodeToString(ivAndCipherText, Base64.NO_WRAP);
             encryptedString = Base64.encodeToString(encryptedNote, Base64.DEFAULT);
            dbHandler.addNote(encryptedString);
            finish();
        } catch (UserNotAuthenticatedException e) {
            finish();
        } catch (BadPaddingException | UnsupportedEncodingException| IllegalBlockSizeException| InvalidAlgorithmParameterException
                | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
*/
    }

        /*
    private static byte[] getCombinedArray(byte[] one, byte[] two) {
        byte[] combined = new byte[one.length + two.length];
        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }
    */

}
