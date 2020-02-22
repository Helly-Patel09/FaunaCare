package com.vahapps.faunacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class Tips extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private NavigationView navigationView;
    private TextView navmail, navname;
    private ListView listView;
    private TipsAdapter tp;
    private ArrayList<String> arrayList, injuryTypes;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        spinner = (Spinner) findViewById(R.id.spinner);
        injuryTypes = new ArrayList<>();
        injuryTypes.add("Search For Tips");
        injuryTypes.add("Broken Beaks");
        injuryTypes.add("BRoken wings");
        injuryTypes.add("Broken and Blood Feather");
        injuryTypes.add(" Burns");
        injuryTypes.add("Chilling(Affected by Cold Temperature)");
        injuryTypes.add("Broken leg of Dog/Cat/Goat/Cow");
        injuryTypes.add("Cuts (lacerations) ");
        injuryTypes.add("scrapes (abrasions)");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, injuryTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayList = new ArrayList<>();
                switch (position) {
                    case 1:
                        arrayList.clear();
                        arrayList.add("On broken beaks the tissues tend to dry out so keep pouring water till bird is attended by vet or serve bird soft food");
                        arrayList.add("The wound can be rinsed with sterile saline (preservative-free contact lens solution is perfect for situations such as this) to flush out any debris and to help keep the tissue moist until your bird can be evaluated by your veterinarian. ");
                        arrayList.add("Do not be aggressive with flushing, and do not remove the beak if it is partially attached.");
                        getList();
                        break;
                    case 2:
                        arrayList.clear();
                        arrayList.add("On broken wings the tissues tend to dry out so keep pouring water till bird is attended by vet or serve bird soft food");
                        arrayList.add("Carefully fold the wing into its natural position and wrap it with gauze in a figure 8 pattern. This will avoid further injury to the wind. Don’t wrap it too tight as you can restrict breathing");
                        getList();
                        break;
                    case 3:
                        arrayList.clear();
                        arrayList.add("The bleeding is easily controlled with flour");
                        arrayList.add("blood");
                        getList();
                        break;
                    case 4:
                        arrayList.clear();
                        arrayList.add("treat it by running cold water over the affected area");
                        arrayList.add("heat burns");
                        getList();
                        break;
                    case 5:
                        arrayList.clear();
                        arrayList.add("It needs to be warmed up, but carefully so it is not burned. Some possible ways to accomplish this are by putting your bird under a heat lamp or sitting it next to a hot water bottle wrapped in a towel. Caution needs to be taken not to replace the chilling with a burn injury.");
                        arrayList.add("Cold ");
                        getList();
                        break;
                    case 6:
                        arrayList.clear();
                        arrayList.add(" Flush the wound thoroughly with clean water. Do not use any other antiseptic.");
                        arrayList.add("Cover the wound with a clean cloth, sanitary napkin  to minimize the movement of the broken bones..");
                        getList();
                        break;
                    case 7:
                        arrayList.clear();
                        arrayList.add(" Flush the ares around cut  thoroughly with clean water.");
                        arrayList.add("The fur around the edges of the injured area will need to be clipped, followed by cleaning of the entire area with an antibacterial cleanser.");
                        getList();
                        break;
                    case 8:
                        arrayList.clear();
                        arrayList.add(" Flush the scraped skin thoroughly with clean water.Remove dirt and hair.");
                        arrayList.add("Bandages can be treated with a material that tastes bad (eg, bitter apple) to discourage animal from licking or chewing the area..");
                        getList();
                        break;

                    default:
                        arrayList.add("Default");
                        break;

                }
            }

            private void getList() {

                listView = (ListView) findViewById(R.id.list_view);
                tp = new TipsAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(tp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View hView = navigationView.getHeaderView(0);
        navmail = hView.findViewById(R.id.textViewmail);
        navname = hView.findViewById(R.id.textViewname);
        navmail.setText(user.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.tips, menu);
        return false;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_upload) {
            Intent i = new Intent(Tips.this, UploadFaunaDetails.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_fauna) {
            Intent i = new Intent(Tips.this, FaunasInNeed.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(Tips.this, Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent i = new Intent(Tips.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_allVets) {
            Intent i = new Intent(Tips.this, AllVet.class);
            startActivity(i);
        } else if (id == R.id.nav_myProfile) {
            final UserType objUser= (UserType) getApplicationContext();
            String userTyp=objUser.getUserType();
            if(userTyp.equals("vet")){
                Intent i =new Intent(Tips.this,VetProfile.class);
                startActivity(i);
            }
            else if(userTyp.equals("vol")){
                Intent i =new Intent(Tips.this,VolProfile.class);
                startActivity(i);
            }else if(userTyp.equals("finder")){
                Intent i =new Intent(Tips.this,FinderProfile.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_tips) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
