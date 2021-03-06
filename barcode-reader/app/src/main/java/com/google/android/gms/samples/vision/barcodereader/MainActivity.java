package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int RC_STORE_MAP = 9002;
    private static final String TAG = "BarcodeMain";

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    private Camera mCamera;
    private CameraView mPreview;
    private FrameLayout preview;

    private DelayAutoCompleteTextView DACTV;
    private AutoCompleteAdapter autoCompleteAdapter;
    int THRESHOLD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setupWindowAnimations();

        statusMessage = (TextView) findViewById(R.id.status_message);
        barcodeValue = (TextView) findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Button button = (Button) findViewById(R.id.read_barcode);
        button.setOnClickListener(this);

        findViewById(R.id.add_item).setOnClickListener(this);
        findViewById(R.id.store_map).setOnClickListener(this);
        findViewById(R.id.in_store_map).setOnClickListener(this);
        mDrawerList = (ListView) findViewById(R.id.navList);

        ListFrag fragment = new ListFrag();

        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        Switch focusSwitch = (Switch) findViewById(R.id.auto_focus);
        focusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ImageView imageView = (ImageView) findViewById(R.id.focus);
                    imageView.setImageResource(R.drawable.focus_on);
                } else {
                    ImageView imageView = (ImageView) findViewById(R.id.focus);
                    imageView.setImageResource(R.drawable.focus_off);
                }
            }

        });

        Switch flashSwitch = (Switch) findViewById(R.id.use_flash);
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ImageView imageView = (ImageView) findViewById(R.id.flash);
                    imageView.setImageResource(R.drawable.flash_on);
                } else {
                    ImageView imageView = (ImageView) findViewById(R.id.flash);
                    imageView.setImageResource(R.drawable.flash_off);
                }
            }

        });

        setBackground();

        // set adapter and auto complete text view <-> order is important
        autoCompleteAdapter = new AutoCompleteAdapter(this);
        DACTV = setAdapter();
    }

    private DelayAutoCompleteTextView setAdapter() {
        final DelayAutoCompleteTextView DACTV = (DelayAutoCompleteTextView) findViewById(R.id.et_book_title);
        DACTV.setThreshold(THRESHOLD);
        DACTV.setAdapter(autoCompleteAdapter); // 'this' is Activity instance
        DACTV.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        DACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Code book = (Code) adapterView.getItemAtPosition(position);
                DACTV.setText(book.getTitle());
            }
        });
        return DACTV;
    }

    private void setBackground() {
//        // Create an instance of Camera
//        mCamera = getCameraInstance();
//        mCamera.setDisplayOrientation(90);
//        // Create our Preview view and set it as the content of our activity.
//        mPreview = new CameraView(this, mCamera);
//        preview = (FrameLayout) findViewById(R.id.surfaceView);
//        preview.addView(mPreview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setBackground();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setBackground();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        ListFrag list = (ListFrag) fragmentManager.findFragmentById(R.id.fragment_container);
        System.out.println("LIST STATE BEFORE TERMINATING" + list.getAdapter().getCodes());
        state.putSerializable("codes", list.getAdapter().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        ListFrag list = (ListFrag) fragmentManager.findFragmentById(R.id.fragment_container);
        System.out.println("LIST STATE BEFORE TERMINATING" + list.getAdapter().getCodes());
        super.onDestroy();
    }

    private void addDrawerItems() {
        String[] osArray = {"Android", "iOS", "Windows", "OS X", "Linux"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

        if (v.getId() == R.id.add_item) {

            // Check if there is something in the adapter
            System.out.println("This is the DACTV current text: \"" + DACTV.getText().toString() + "\"");
            System.out.println("This is the DACTV adapter current count: " + DACTV.getAdapter().getCount());
            System.out.println("DACTV adapter before = " + autoCompleteAdapter.toString());

            if (DACTV.getAdapter().getCount() == 0 || DACTV.getText().toString() == "") {
                Toast.makeText(this, "Please search for an item!",
                        Toast.LENGTH_LONG).show();

                if (DACTV.getAdapter().getCount() == 0) {
                    System.out.println("ADAPTER IS EMPTY");
                } else if (DACTV.getText().toString() == "") {
                    System.out.println("AUTOCOMPLETE TEXTVIEW IS EMPTY");
                }
                return;
            }

            //Copy constructor to create a new code item from the first/selected item in adapter
            Code code = new Code(autoCompleteAdapter.getItem(0));
            // add item
            addItem(code);
            System.out.println("This is the X value of the barcode" + code.getX() + "");

            // Check if there is something in the adapter
            System.out.println("This is the DACTV text after addition: \"" + DACTV.getText().toString() + "\"");
            System.out.println("This is the DACTV adapter count after addition: " + DACTV.getAdapter().getCount());
            System.out.println("DACTV adapter before = " + autoCompleteAdapter.toString());
        }

        if (v.getId() == R.id.store_map) {
            // launch barcode activity.
            Intent intent = new Intent(this, MapLocation.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.in_store_map) {
            ListFrag list = (ListFrag) fragmentManager.findFragmentById(R.id.fragment_container);
            System.out.println("LIST STATE BEFORE TERMINATING" + list.getAdapter().getCodes());
            Intent intent = new Intent(this, StoreMap.class);
            intent.putExtra("CodesArrayList", list.getAdapter().getCodes());
            startActivityForResult(intent, RC_STORE_MAP);
        }
    }

    public void addItem(Code code) {
        //Clear Adapter
        System.out.println("HERE WE'RE SUPPOSEDLY CLEARING THE ADAPTER" + DACTV.getAdapter().getCount());
        autoCompleteAdapter.clearAdapter();
        DACTV.clearAdapter();
        System.out.println("HERE THE ADAPTER IS SUPPOSEDLY CLEARED" + DACTV.getAdapter().getCount());

        //Clear search bar
        View bar = this.findViewById(R.id.et_book_title);
        ((AutoCompleteTextView) bar).setText("", false);

        // Get list fragment in view and add the newly created code to it
        ListFrag list = (ListFrag) fragmentManager.findFragmentById(R.id.fragment_container);
        list.addItems(code);

        //update the prices
        updatePrice(list);
    }

    public void updatePrice(ListFrag list) {
        double total = 0;
        TextView tv = (TextView) this.findViewById(R.id.bill_container);
        tv.setText(list.getAdapter().getTotal() + "");
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Code codeAsJson = new Code((Code) data.getSerializableExtra(BarcodeCaptureActivity.CodeObject));
                    statusMessage.setText(R.string.barcode_success);
                    //barcodeValue.setText(codeAsJson.getTitle());

                    addItem(codeAsJson);
                    System.out.println(codeAsJson);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
