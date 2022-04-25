package com.example.a91;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.MenuItem;

import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    Context context = null;
    TheaterRepo theaterRepo = TheaterRepo.getInstance();
    Spinner spinner;
    ArrayList<String> stringTheaters;
    ArrayList<String> movies;
    int choice;
    ListView listView;
    EditText textViewDate;
    EditText textViewAfter;
    EditText textViewBefore;
    public static final String TAG = "YOUR-TAG-NAME";
    EditText textVewNimi;
    Button nappi;
    FirebaseAuth firebaseAuth;

    //variables for sidemenu
    private androidx.drawerlayout.widget.DrawerLayout DrawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listview);
        textViewAfter = findViewById(R.id.editTextAfter);
        textViewBefore = findViewById(R.id.editTextBefore);
        textViewDate = findViewById(R.id.editTextDate);
        textVewNimi = findViewById(R.id.editTextNimi);
        nappi = findViewById(R.id.button);
        theaterRepo.readTheaters();
        stringTheaters = theaterRepo.getStringTheaters();
        firebaseAuth = FirebaseAuth.getInstance();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users1")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Create a new user with a first, middle, and last name
        Map<String, Object> user2 = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        // Add a new document with a generated ID
        db.collection("users2")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("users2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        theaterRepo.readTheaters();
        stringTheaters = theaterRepo.getStringTheaters();

        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringTheaters);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                choice = position;
                theaterRepo.readMovies(textViewDate.getText().toString(), choice);
                SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");


                if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
                    movies = theaterRepo.getTheaters().get(choice).getMovies();
                    ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, movies);
                    listView.setAdapter(aa2);
                } else {
                    movies = theaterRepo.getTheaters().get(choice).getMovies();
                    ArrayList<String> searched = new ArrayList<>();
                    Date after = null;
                    Date before = null;
                    try {
                        after = formatter3.parse(textViewAfter.getText().toString());
                        before = formatter3.parse(textViewBefore.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < movies.size(); i++) {
                        String movie = movies.get(i);
                        String stringTime = movie.substring(movie.length() - 5);
                        System.out.println(stringTime);
                        try {
                            Date aika = formatter3.parse(stringTime);
                            if (aika.after(after) && aika.before(before)) {
                                searched.add(movie);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, searched);
                    listView.setAdapter(aa3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        //this part is for the navigation menu
        findViewById(R.id.nav_settings);
        findViewById(R.id.nav_account);

        NavigationView navigationView = findViewById(R.id.navigationView);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, DrawerLayout, R.string.open, R.string.close);

        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //this makes the menu items start activities when clicked
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //get item id for comparing with menu item ids
                int id = item.getItemId();

                //takes to SettingsActivity when Settings is pressed
                if(id == R.id.nav_settings){
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                //takes to AccountActivity when My Account is pressed
                else if(id == R.id.nav_account){
                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                }
                //logs out and takes to log in activity when Log Out is pressed
                else if(id == R.id.nav_log){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                }
                return true;
            }
        });




    }

    //this allows the hamburger menu to open
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void haeKaikki(View v){
        theaterRepo.readMovies(textViewDate.getText().toString(), choice);
        SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");
        String nimi = textVewNimi.getText().toString();


        if (nimi != "") {
            ArrayList<String> kaikki = theaterRepo.readAll(nimi);

            if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
                ArrayAdapter aa4 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, kaikki);
                listView.setAdapter(aa4);
            }else{
                ArrayList<String> searched = new ArrayList<>();
                Date after = null;
                Date before = null;
                try {
                    after = formatter3.parse(textViewAfter.getText().toString());
                    before = formatter3.parse(textViewBefore.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < kaikki.size(); i++) {
                    String movie = kaikki.get(i);
                    String stringTime = movie.substring(movie.length() - 5);
                    System.out.println(stringTime);
                    try {
                        Date aika = formatter3.parse(stringTime);
                        if (aika.after(after) && aika.before(before)) {
                            searched.add(movie);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, searched);
                listView.setAdapter(aa3);
            }
        } else if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
            movies = theaterRepo.getTheaters().get(choice).getMovies();
            ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, movies);
            listView.setAdapter(aa2);
        } else {
            movies = theaterRepo.getTheaters().get(choice).getMovies();
            ArrayList<String> searched = new ArrayList<>();
            Date after = null;
            Date before = null;
            try {
                after = formatter3.parse(textViewAfter.getText().toString());
                before = formatter3.parse(textViewBefore.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < movies.size(); i++) {
                String movie = movies.get(i);
                String stringTime = movie.substring(movie.length() - 5);
                System.out.println(stringTime);
                try {
                    Date aika = formatter3.parse(stringTime);
                    if (aika.after(after) && aika.before(before)) {
                        searched.add(movie);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, searched);
            listView.setAdapter(aa3);
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringTheaters);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


    }

}