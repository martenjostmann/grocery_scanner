package de.grocery_scanner;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import de.grocery_scanner.helper.scanner.barcodeScanner;
import de.grocery_scanner.home.homeFragment;
import de.grocery_scanner.inventory.inventoryFragment;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        addGrocery();


        mViewPager = (ViewPager) findViewById(R.id.pager);

        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        tabLayout.setupWithViewPager(mViewPager);

        //Add icons to TabLayout
        View view1 = getLayoutInflater().inflate(R.layout.fragment_inventory_tab, null);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.fragment_home_tab, null);
        tabLayout.getTabAt(1).setCustomView(view2);


        //Select Home as first Tab
        TabLayout.Tab tab = tabLayout.getTabAt(1);

        tab.select();

        //Tab Action Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            TextView inventoryText = (TextView) findViewById(R.id.inventory_text_tab);
            TextView homeText = (TextView) findViewById(R.id.home_text_tab);


            ImageView inventoryIcon = (ImageView) findViewById(R.id.inventoryIcon);
            ImageView homeIcon = (ImageView) findViewById(R.id.homeIcon);



            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){
                    inventoryText.setHeight(50);
                }else if(tab.getPosition() == 1){
                    homeText.setHeight(50);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    inventoryText.setHeight(0);

                }else if(tab.getPosition() == 1){
                    homeText.setHeight(0);

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    private void addGrocery(){
        FloatingActionButton addGroceryButton = findViewById(R.id.addGrocery);

        addGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, barcodeScanner.class));
            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new inventoryFragment(), "Inventar");
        adapter.addFragment(new homeFragment(), "Home");
        viewPager.setAdapter(adapter);

    }

}
