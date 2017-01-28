package com.example.android.cse594project;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.security.KeyStore;

public class noteCursor extends CursorAdapter {
    public noteCursor(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    String KEY_NAME = "my_key";
    KeyStore keyStore;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView noteField = (TextView) view.findViewById(R.id.noteName);
        TextView idField = (TextView) view.findViewById(R.id.noteID);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("Note"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        TokenEncryptor tokenEncryptor = new TokenEncryptor();
        String plaintext = tokenEncryptor.decrypt(body);
        noteField.setText(plaintext);
        idField.setText(String.valueOf(id));
/*
        try {
            String note;
            byte[] decryptedBytes;
            byte[] encryptedBytes;
            GetKey key = new GetKey();
            SecretKey secretKey = key.getKey();
            if (secretKey != null) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                byte[] ivbytes = new byte[ 16 ];
                IvParameterSpec iv = new IvParameterSpec(ivbytes);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
                encryptedBytes = Base64.decode(body, Base64.DEFAULT);
                decryptedBytes = cipher.doFinal(encryptedBytes);
                note = Base64.encodeToString(decryptedBytes, Base64.DEFAULT);
                noteField.setText(note);
                idField.setText(String.valueOf(id));
            }
        } catch (UserNotAuthenticatedException e) {
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
            System.out.println(" Invalid Key " + e);
        }

        catch (NoSuchPaddingException e){
        }
        catch (NoSuchAlgorithmException e){
        }
        catch (InvalidAlgorithmParameterException e){

        }
        catch (InvalidKeyException e) {
            noteField.setText("hello");
    }
*/
    }
}