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
    private static final String AndroidKeyStore = "AndroidKeyStore";
    SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list);
       // test();
        showNotes();
    }

    public void test() {
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

    public SecretKey getKey()
    {
        return secretKey;
    }



    private void createKey() {
        // Generate a key to decrypt payment credentials, tokens, etc.
        // This will most likely be a registration step for the user when they are setting up your app.
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
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
            String[] columns = new String[]{
                    DBHandler.COLUMN_ID,
                    DBHandler.COLUMN_NOTE
            };
            int[] fields = new int[]{
                    R.id.noteID,
                    R.id.noteName
            };

            /* swapped from simplecursoradapter to cursoradapter
            //simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, columns, fields, 0);
            // noteList.setAdapter(simpleCursorAdapter);
            */

            noteCursor c = new noteCursor(this, cursor);
            noteList.setAdapter(c);
/*
            try {
                TextView noteText = (TextView) noteList.getParent();
                int j;
                int w;
                String encryptedString = noteText.getText().toString();
                String note;
                byte[] decryptedNote;
                byte[] noteBytes;
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
                Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);

                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                noteBytes = encryptedString.getBytes();
                decryptedNote = cipher.doFinal(noteBytes);
                note = Base64.encodeToString(decryptedNote, Base64.DEFAULT);
                noteText.setText(note);
            } catch (UserNotAuthenticatedException e) {
                finish();
            } catch (BadPaddingException | IllegalBlockSizeException | KeyStoreException |
                    CertificateException | UnrecoverableKeyException | IOException
                    | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
*/


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
