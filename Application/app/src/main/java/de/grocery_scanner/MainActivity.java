package de.grocery_scanner;

import android.content.Intent;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.grocery_scanner.helper.scanner.BarcodeScanner;
import de.grocery_scanner.home.HomeFragment;
import de.grocery_scanner.inventory.filter.FilterBottomSheet;
import de.grocery_scanner.inventory.InventoryFilter;
import de.grocery_scanner.inventory.InventoryFragment;
import de.grocery_scanner.inventory.filter.Group;
import de.grocery_scanner.inventory.filter.Sort;


public class MainActivity extends AppCompatActivity implements FilterBottomSheet.BottomSheetListener {

    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private LinearLayout fabLayout1;
    private LinearLayout fabLayout2;
    private InventoryFilter filter;
    private boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //initialize Objects
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fabLayout1 = findViewById(R.id.FABLayout1);
        fabLayout2 = findViewById(R.id.FABLayout2);

        //FloatingActionButton OnClickListener
        fabOnClick(1);
        fab1OnClick();

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
        adapter.addFragment(new InventoryFragment(), "Inventar");
        adapter.addFragment(new HomeFragment(), "Home");
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

                //close the fab menu on tab change
                closeFABMenu();

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

                    if (filter == null) {
                        filter = new InventoryFilter(Sort.dateDESC, Group.none);
                    }

                    FilterBottomSheet bottomSheet = new FilterBottomSheet(filter);
                    bottomSheet.show(getSupportFragmentManager(), "bottomSheet");

                }else if (position == 1){
                    if (!isFABOpen) {
                        showFABMenu();
                    }else {
                        closeFABMenu();
                    }

                }

            }
        });
    }

    /**
     * OnClickListener for the first unfolded fab
     */
    private void fab1OnClick(){
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BarcodeScanner.class));
            }
        });
    }

    /**
     * When the back button was pressed the fab menu should be closed
     */
    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method to open the FAB Menu with animations
     */
    public void showFABMenu(){

        isFABOpen=true;

        //animation for fab 1
        fab.animate().rotationBy(180);
        fab1.animate().rotationBy(180);
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout1.animate().alpha(1);

        //animation for fab 2
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout2.setAlpha(1);
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_55));


    }

    /**
     * Method to close the FAB Menu with animations
     */
    public void closeFABMenu(){
        isFABOpen=false;

        //animation for fab 1
        fab.animate().rotation(0);
        fab1.animate().rotation(180);
        fabLayout1.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                fabLayout1.setVisibility(View.GONE);
            }
        });

        //animation for fab 2
        fabLayout2.animate().translationY(0);
        fabLayout2.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                fabLayout2.setVisibility(View.GONE);
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

    /**
     * Method to retrieve filter settings from FilterBottomSheet
     *
     * @param filter InventoryFilter with necessary information to apply filters
     * */
    @Override
    public void onSendClicked(InventoryFilter filter) {

        // Make filter settings available to other methods, so that it can be send back to FilterBottomSheet
        this.filter = filter;

        FragmentManager fm = getSupportFragmentManager();
        InventoryFragment page = (InventoryFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mViewPager.getCurrentItem());
        page.applyFilter(filter);
    }
}
