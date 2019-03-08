package com.example.jcmilena.jdainventario;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public class ControllerActivity extends AppCompatActivity implements AddEquipoFragment.OnAddEquipoListener, InventarioFragment.OnInventarioFragmentListener, SearchFragment.OnSearchFragmentListener {

    List<EquipoInformatico> inventario = new ArrayList<>();


    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        //inventario.add(new EquipoInformatico("S","S","S","S"));
        db = new MiBBDD_Helper(this).getWritableDatabase();

        Fragment fragment = new WelcomeFragment();
        cargar_fragment(fragment);
    }

    private void cargar_fragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jdainventario_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
	public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
        case R.id.equipo:
			Fragment fragment = new AddEquipoFragment();
            cargar_fragment(fragment);
            return true;
        case R.id.buscar:
            Fragment fragment2 = new SearchFragment();
            cargar_fragment(fragment2);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}


    @Override
    public void writeSQLite(EquipoInformatico equipo) {

	
	ContentValues values = new ContentValues();
	values.put(MiBBDD_Schema.EntradaBBDD.COLUMNA1, equipo.getFabricante());
	values.put(MiBBDD_Schema.EntradaBBDD.COLUMNA2, equipo.getModelo());
	values.put(MiBBDD_Schema.EntradaBBDD.COLUMNA3, equipo.getMAC());
	values.put(MiBBDD_Schema.EntradaBBDD.COLUMNA4, equipo.getAula());
	
	long newRowId = db.insert(MiBBDD_Schema.EntradaBBDD.TABLE_NAME, null, values);

	    Fragment fragment = new WelcomeFragment();
        cargar_fragment(fragment);

    }

    @Override
    public List<EquipoInformatico> getEquiposList() {

        return inventario;
    }

    @Override
    public void searchSQLite(String columna, String valor) {
        String columna2 = columna.toUpperCase();
        String selection = columna2 + " = ?";
        String[] selectionArgs = { valor };

	Cursor cursor = db.query(
            MiBBDD_Schema.EntradaBBDD.TABLE_NAME,   // The table to query
		null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
		null,                   // don't group the rows
		null,                   // don't filter by row groups
		null               // The sort order
		);
        inventario.removeAll(inventario);
        while(cursor.moveToNext()) {
            String item1 = cursor.getString(cursor.getColumnIndexOrThrow(MiBBDD_Schema.EntradaBBDD.COLUMNA1));
            String item2 = cursor.getString(cursor.getColumnIndexOrThrow(MiBBDD_Schema.EntradaBBDD.COLUMNA2));
            String item3 = cursor.getString(cursor.getColumnIndexOrThrow(MiBBDD_Schema.EntradaBBDD.COLUMNA3));
            String item4 = cursor.getString(cursor.getColumnIndexOrThrow(MiBBDD_Schema.EntradaBBDD.COLUMNA4));
            inventario.add(new EquipoInformatico(item1,item2,item3,item4));
        }
        cursor.close();
        Fragment fragment = new InventarioFragment();
        cargar_fragment(fragment);

    }
}
