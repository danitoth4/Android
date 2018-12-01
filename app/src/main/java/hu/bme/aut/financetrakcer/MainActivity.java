package hu.bme.aut.financetrakcer;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hu.bme.aut.financetrakcer.adapter.FinanceAdapter;
import hu.bme.aut.financetrakcer.fragments.DatePickerDialogFragment;
import hu.bme.aut.financetrakcer.fragments.NewFinanceItemDialogFragment;
import hu.bme.aut.financetrakcer.model.Category;
import hu.bme.aut.financetrakcer.model.DataManager;
import hu.bme.aut.financetrakcer.model.Finance;
import hu.bme.aut.financetrakcer.model.FinanceTrackerDatabase;

public class MainActivity extends AppCompatActivity implements FinanceAdapter.FinanceItemClickListener,  NewFinanceItemDialogFragment.NewFinanceItemDialogListener, DatePickerDialogFragment.OnDateSelectedListener {


    private RecyclerView recyclerView;
    private FinanceAdapter adapter;

    private FinanceTrackerDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewFinanceItemDialogFragment().show(getSupportFragmentManager(), NewFinanceItemDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(
                getApplicationContext(),
                FinanceTrackerDatabase.class,
                "finance-list"
        ).fallbackToDestructiveMigration().build();

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.diagram_action) {
            Intent launchNewIntent = new Intent(MainActivity.this,DiagramActivity.class);
            startActivityForResult(launchNewIntent, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new FinanceAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        try{
        new  AsyncTask<Void, Void, List<Finance>>() {

            @Override
            protected List<Finance> doInBackground(Void... voids) {
                return database.FinanceDao().getAll();
            }


            @Override
            protected void onPostExecute(List<Finance> finances) {
                DataManager.getInstance().addItems(finances);
                adapter.update(finances);
            }
        }.execute(); }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    @Override
    public void onItemChanged(final Finance item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.FinanceDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MainActivity", "ShoppingItem update was successful");
            }
        }.execute();
    }

    @Override
    public void onFinanceItemCreated(final Finance newItem) {
        new AsyncTask<Void, Void, Finance>() {

            @Override
            protected Finance doInBackground(Void... voids) {
                newItem.id = database.FinanceDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(Finance finance) {
                DataManager.getInstance().addItem(finance);
                adapter.addItem(finance);
            }
        }.execute();
    }

    @Override
    public void onItemRemoved(final Finance item) {
        new AsyncTask<Void, Void, Finance>() {

            @Override
            protected Finance doInBackground(Void... voids) {
                database.FinanceDao().deleteItem(item);
                return item;
            }

            @Override
            protected void onPostExecute(Finance item) {
                DataManager.removeItem(item);
                adapter.deleteItem(item);
            }
        }.execute();
    }

    @Override
    public void onDateSelected(int year, int month, int day, boolean start) {
       NewFinanceItemDialogFragment newFinanceFrag = (NewFinanceItemDialogFragment) getSupportFragmentManager().findFragmentByTag("NewFinanceItemDialogFragment");
       newFinanceFrag.onDateSelected(year, month, day, start);
    }
}

