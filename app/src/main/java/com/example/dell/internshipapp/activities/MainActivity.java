package com.example.dell.internshipapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dell.internshipapp.Network.VolleySingleton;
import com.example.dell.internshipapp.pojo.Profile;
import com.example.dell.internshipapp.R;
import com.example.dell.internshipapp.adapters.AdapterProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.dell.internshipapp.extras.Keys.Endpoint.*;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RequestQueue requestQueue;
    private ArrayList<Profile> profileArrayList=new ArrayList<Profile>();
    public AdapterProfile adapterProfile;
    LinearLayout linearLayoutOffset;
    private RecyclerView listprofile;
    private EditText offsetField;
    public  int number=-1;
    public  SwipeRefreshLayout swipeRefreshLayout;
    public AlertDialog.Builder builder;
    public static final  String REQUEST_URL="https://us-central1-cratso-171712.cloudfunctions.net/cratso_internship/leaderboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        offsetField= (EditText) findViewById(R.id.offset);
        Button showButton= (Button) findViewById(R.id.show);
        linearLayoutOffset= (LinearLayout) findViewById(R.id.offsetField);
        listprofile= (RecyclerView) findViewById(R.id.listProfile);
        listprofile.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapterProfile=new AdapterProfile(getApplicationContext());
        listprofile.setAdapter(adapterProfile);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refreshList);
        swipeRefreshLayout.setRefreshing(false);
        builder = new AlertDialog.Builder(MainActivity.this);



        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linearLayoutOffset.getVisibility()==View.VISIBLE) {
                   String n=offsetField.getText().toString();
                    if (n.isEmpty()||n.length()==0) {
                        builder.setTitle("ERROR");
                        displayAlert("Enter Value");
                    } else {
                        number = Integer.parseInt(offsetField.getText().toString());
                        if (number < 50) {
                            linearLayoutOffset.setVisibility(View.INVISIBLE);
                            listprofile.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            sendJSONRequest();
                        } else {
                            builder.setTitle("ERROR");
                            displayAlert("Value should be less than 50");
                        }
                    }
                }
                }


        });


    }
    //JSONREQUEST
        private void sendJSONRequest(){
        requestQueue = VolleySingleton.getsInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(number), null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        profileArrayList= parseJSONResponse(response);
                         adapterProfile.setProfileArrayList(profileArrayList);

                        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        //Toast.makeText(MainActivity.this, "Response" + profileArrayList, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }
    //PARSEJSONRESPONSE
        private ArrayList<Profile> parseJSONResponse(JSONObject response){
            Log.d("*","O");
            ArrayList<Profile> profileArray=new ArrayList<Profile>();
            if(response==null||response.length()==0) {
                Log.d("ISNULL","L");
                return profileArray;
            }
            try {
                Log.d("5","5");
                StringBuilder stringBuilder=new StringBuilder();
                JSONObject jsonObject=response.getJSONObject(METADATA);
                JSONArray jsonArray= response.getJSONArray(DATA);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject data=jsonArray.getJSONObject(i);
                    String id=data.getString(USER_ID);
                    String name=data.getString(NAME);
                    String rank=data.getString(RANK);
                    String profilePic=data.getString(PROFILE_PIC);
                    Profile profile=new Profile();
                    profile.setId(id);
                    profile.setName(name);
                    profile.setProfilePic(profilePic);
                    profile.setRank(rank);
                    profileArray.add(profile);

                }
              // Toast.makeText(MainActivity.this, "Response" + (jsonObject.get(POINTER)).toString(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
         return profileArray;
        }

        public static String getRequestUrl(int offset){
        return REQUEST_URL+"?pointer=0&offset="+offset;
    }


    @Override
    public void onRefresh() {
        number+=5;
        sendJSONRequest();
    }
    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                offsetField.setText("");
            }

        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}

