package com.example.android.cse594project;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class AddNote extends AppCompatActivity {

    DBHandler dbHandler;
    SimpleCursorAdapter simpleCursorAdapter;
    //TextView idView;
    //EditText etName;
    EditText noteField;
    ListView noteList;
    TextView textView;
    int generateID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        dbHandler = new DBHandler(this, null, null, 1);
        noteField = (EditText) findViewById(R.id.notetext);
        noteList = (ListView) findViewById(R.id.list2);
        /*
        idView = (TextView) findViewById(R.id.tvID);
        etName = (EditText) findViewById(R.id.etName);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        lvProducts = (ListView) findViewById(R.id.productList);
        */
        //
        //displayProductList();
    }


    public void addNote(View view) {
        Note n = new Note(generateID, noteField.getText().toString());
        dbHandler.addNote(n);
        showNotes();
    }

    private void showNotes() {
        Cursor cursor = dbHandler.getNotes();
        if (cursor!=null) {
            String[] columns = new String[]{
                    DBHandler.COLUMN_ID,
                    DBHandler.COLUMN_NOTE
            };
            int[] fields = new int[]{
                    R.id.list_text3,
                    R.id.list_text2

            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.list_item2,
                    cursor,
                    columns,
                    fields,
                    0);
            noteList.setAdapter(simpleCursorAdapter);
        }
    }




  /*
    public void lookupProduct(View v) {
        Product p = dbHandler.findProduct(etName.getText().toString());
        if (p == null)
        {
            idView.setText("No Match Found!");
            return;
        }
        idView.setText(String.valueOf(p.getID()));
        etQuantity.setText(String.valueOf(p.getQuantity()));
    }

    public void deleteProduct(View v) {
        if (dbHandler.deleteProduct(etName.getText().toString())) {
            idView.setText("Record Deleted!");
            etName.setText("");
            etQuantity.setText("");
            displayProductList();
        } else {
            idView.setText("No Match \nFound!");
        }
    }

    public void updateProduct(View v) {
        try {
            Product p = new Product(Integer.parseInt(idView.getText().toString()),
                    etName.getText().toString(), Integer.parseInt(etQuantity.getText().toString()));
            if (dbHandler.updateProduct(p)) {
                idView.setText("Product updated!");
            } else {
                idView.setText("No product \nfound!");
            }
        } catch (Exception e) {
            idView.setText("Find a product \nfirst. Check \n all fields.");
        }
        etName.setText("");
        etQuantity.setText("");
        displayProductList();
    }

    public void deleteAllProducts(View v) {
        dbHandler.deleteAllProducts();
        idView.setText("All products \ndeleted");
        etName.setText("");
        etQuantity.setText("");
        lvProducts.setAdapter(null);
    }
*/
}
