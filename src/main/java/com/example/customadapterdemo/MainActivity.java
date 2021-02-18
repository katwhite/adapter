package com.example.customadapterdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    UserListAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);

        ArrayList<User> users = new ArrayList<>();


        try {
            users = (ArrayList<User>) readJsonStream(getAssets().open("ppl.json"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        final ArrayList<User> usersAdapter = new ArrayList<>(users);
        adapter = new UserListAdapter(this, users);

        listView.setAdapter(adapter);


       Button btnRestart = findViewById(R.id.goinggreen);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.users = new ArrayList<>(usersAdapter);
                adapter.notifyDataSetChanged();
            }
        });

        Button women = findViewById(R.id.two);
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> usersnew = new ArrayList<>();
                for (int i = 0; i < usersAdapter.size(); i++) {
                    if (usersAdapter.get(i).sex == Sex.WOMAN) {
                        usersnew.add(usersAdapter.get(i));
                    }
                }
                adapter.users = new ArrayList<>(usersnew);
                adapter.notifyDataSetChanged();
            }
        });

        Button cats = findViewById(R.id.three);
        cats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> usersnew = new ArrayList<>();
                for (int i = 0; i < usersAdapter.size(); i++) {
                    if (usersAdapter.get(i).sex == Sex.UNKNOWN) {
                        usersnew.add(usersAdapter.get(i));
                    }
                }
                adapter.users = new ArrayList<>(usersnew);
                adapter.notifyDataSetChanged();
            }
        });


        Button men = findViewById(R.id.one);
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> usersnew = new ArrayList<>();
                for (int i = 0; i < usersAdapter.size(); i++) {
                    if (usersAdapter.get(i).sex == Sex.MAN) {
                        usersnew.add(usersAdapter.get(i));
                    }
                }
                adapter.users = new ArrayList<>(usersnew);
                adapter.notifyDataSetChanged();
            }
        });

        Button name = findViewById(R.id.surname);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(adapter.users, new Comparator<User>() {
                            public int compare(User o1, User o2) {
                                return o1.name.compareTo(o2.name);

                            }
                });

                adapter.notifyDataSetChanged();
            }
        });

        Button phone = findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(adapter.users, new Comparator<User>() {
                    public int compare(User o1, User o2) {
                        return o1.phoneNumber.compareTo(o2.phoneNumber);

                    }
                });

                adapter.notifyDataSetChanged();
            }
        });

    }

    public List<User> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readUsersArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<User> readUsersArray(JsonReader reader) throws IOException {
        List<User> users = new ArrayList<User>();

        reader.beginArray();
        while (reader.hasNext()) {
            users.add(readUser(reader));
        }
        reader.endArray();
        return users;
    }

    public User readUser(JsonReader reader) throws IOException {
        String name = null;
        String phoneNumber = null;
        Sex sex = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String field = reader.nextName();
            if (field.equals("Name")) {
                name = reader.nextString();
            } else if (field.equals("Phone")) {
                phoneNumber = reader.nextString();
            } else if (field.equals("Sex")){
                switch (reader.nextString()) {
                    case "MAN": sex = Sex.MAN; break;
                    case "WOMAN": sex = Sex.WOMAN; break;
                    case "UNKNOWN": sex = Sex.UNKNOWN; break;
                }
            }
        }
        reader.endObject();
        return new User(name, phoneNumber, sex);
    }

}
