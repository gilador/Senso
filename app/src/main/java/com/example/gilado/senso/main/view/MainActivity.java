package com.example.gilado.senso.main.view;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gilado.senso.R;
import com.example.gilado.senso.main.model.FileMainModel;
import com.example.gilado.senso.main.presentor.MainPresenter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainView view = setupUi();
        new MainPresenter(new FileMainModel(), view, new Handler(Looper.getMainLooper()), getApplicationContext(), (SensorManager) getSystemService(SENSOR_SERVICE));

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private MainView setupUi() {
        setContentView(R.layout.activity_main);
        MainView mainView = (MainView) getSupportFragmentManager().findFragmentById((R.id.main_view_fragment));

        return mainView;
    }
}
