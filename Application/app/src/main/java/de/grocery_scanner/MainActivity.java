package de.grocery_scanner;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import de.grocery_scanner.helper.scanner.barcodeScanner;
import de.grocery_scanner.home.homeFragment;
import de.grocery_scanner.inventory.FilterBottomSheet;
import de.grocery_scanner.inventory.InventoryFilter;
import de.grocery_scanner.inventory.inventoryFragment;


public class MainActivity extends AppCompatActivity implements FilterBottomSheet.BottomSheetListener {

    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //initialize Objects
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fab = findViewById(R.id.fab);

        //FloatingActionButton OnClickListener
        fabOnClick(1);

        setupViewPager(mViewPager);
        setupTabLayout(mViewPager);
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

    /**
     * Create ViewPageAdapter and Fragments for each Tab
     * @param viewPager - ViewPager of the TabLayout
     * */
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new inventoryFragment(), "Inventar");
        adapter.addFragment(new homeFragment(), "Home");
        viewPager.setAdapter(adapter);

    }
    /**
     * Generate different Tabs
     * @param viewPager - ViewPager of the TabLayout
     * */
    private void setupTabLayout(ViewPager viewPager){
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
                //animate Floating Action Button on tab change
                animateFab(tab.getPosition());
                fabOnClick(tab.getPosition());

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

    /**
     * OnClickListener depends on the current Tab position
     *
     * @param position current Tab position
     */
    private void fabOnClick(final int position){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(position == 0){
                    FilterBottomSheet bottomSheet = new FilterBottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "bottomSheet");

                }else if (position == 1){
                    startActivity(new Intent(MainActivity.this, barcodeScanner.class));
                }

            }
        });
    }


    /**
     * Animate the FloatingActionButton on each Tab change.
     *
     * ColorIntArray is for setting up different colors
     * IconIntArray is for setting up different colors
     * */
    //int[] colorIntArray = {R.color.colorPrimary,R.color.colorPrimaryDark};
    int[] iconIntArray = {R.drawable.ic_baseline_filter_list_24, R.drawable.ic_baseline_add_24};

    protected void animateFab(final int position) {
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
                //fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[position]));
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));

                // Scale up animation
                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    @Override
    public void onSendClicked(InventoryFilter filter) {
        FragmentManager fm = getSupportFragmentManager();
        inventoryFragment page = (inventoryFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mViewPager.getCurrentItem());
        page.applyFilter(filter);
    }
}
