package com.example.asusrog.papajohns;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PizzaActivity extends AppCompatActivity {

    private static String url = "http://tootbh.com/a_papa/getMenueById.php?id=";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "menus";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE = "_date";
    private static final String TAG_PICTURE = "picture";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_RATING = "rating";
    private static final String TAG_PRICE  = "price";

    JSONArray menus = null;
    ImageView pizzaPic;
    public String picture;
    RatingBar rate;
    TextView Description,price,name,pidtv;
    int pid;
    String[] myM1;
    SQLiteDatabase db;


    Button btnOpenDialog;
    TextView priceP;
    TextView pizzaID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        new LoadAllMenus().execute();

        // int p=0;
        pizzaPic = (ImageView) findViewById(R.id.imageView3);
        Description = (TextView) findViewById(R.id.desTV);
        rate =(RatingBar) findViewById(R.id.ratingBar);
        pidtv = (TextView) findViewById(R.id.pidtv);
        name =(TextView) findViewById(R.id.nameTv);
        price=(TextView)findViewById(R.id.priceTv);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            pid =(int)bundle.getLong("d");
        }

        Log.d("id-----:",pid+"");
        //  pizzaPic.setDrawableResource();


        btnOpenDialog = (Button)findViewById(R.id.orderBT);
        pizzaID = (TextView)findViewById(R.id.pidtv);
        priceP = (TextView)findViewById(R.id.priceTv);

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pizza, menu);
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
            Intent i = new Intent(PizzaActivity.this,OrderActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private ProgressDialog pDialog;
    JSONParser2 jParser = new JSONParser2();
    class LoadAllMenus extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PizzaActivity.this);
            pDialog.setMessage("Loading Pizza. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        /**
         * getting All products from url
         * */

        JSONObject c;
        protected String doInBackground(String... args) {



            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url+pid);

            try {
                // Getting Array of Contacts
                menus = json.getJSONArray(TAG_PRODUCTS);


                JSONObject c = menus.getJSONObject(0);
                myM1 = new String[7];

                myM1[0]=  c.getString(TAG_PID);
                myM1[1]= c.getString(TAG_NAME);
                Log.d("text", myM1[1]);
                myM1[2]= c.getString(TAG_PICTURE);
                myM1[3]= c.getString(TAG_DATE);
                myM1[4]= c.getString(TAG_RATING);
                myM1[5]= c.getString(TAG_DESCRIPTION);
                myM1[6]= c.getString(TAG_PRICE);






            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
// dismiss the dialog after getting all products
            pDialog.dismiss();
// updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
/**
 * Updating parsed JSON data into ListView
 * */


// updating listview


                    pidtv.setText( myM1[0]);
                    name.setText( myM1[1]);
                    rate.setRating( Float.parseFloat(myM1[4]));
                    Description.setText(Description.getText()+ myM1[5]);

                    price.setText(price.getText() + myM1[6]);

                    Picasso.with(PizzaActivity.this)
                            .load("http://192.168.1.112:8080/a7/" +  myM1[2])
                            .placeholder(R.drawable.common_google_signin_btn_icon_dark_pressed)
                            .error(R.drawable.cast_ic_notification_connecting)
                            .into(pizzaPic);

                }
            });
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*  private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(PizzaActivity.this);
        final View subView = inflater.inflate(R.layout.dialog_signin, null);
        final EditText subEditText = (EditText)subView.findViewById(R.id.quantityET);
        final RadioGroup rg = (RadioGroup)subView.findViewById(R.id.radioGroup);
        final RadioButton[] r1 = new RadioButton[1];
        final RadioButton[] r2 = new RadioButton[1]; //= (RadioButton) subView.findViewById(R.id.largeRB);
        final RadioGroup rg1 = (RadioGroup)subView.findViewById(R.id.radioGroup2);


        //final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        //subImageView.setImageDrawable(drawable);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setMessage("AlertDialog Message");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //id.setText(subEditText.getText().toString());
                int selectedId = rg.getCheckedRadioButtonId();
                r1[0] = (RadioButton) subView.findViewById(selectedId);
                int selectedId2 = rg1.getCheckedRadioButtonId();
                r2[0] = (RadioButton) subView.findViewById(selectedId2);
                Toast.makeText(PizzaActivity.this, pizzaID.getText().toString(), Toast.LENGTH_SHORT).show();

                Toast.makeText(PizzaActivity.this, r1[0].getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(PizzaActivity.this, r2[0].getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(PizzaActivity.this, subEditText.getText().toString(), Toast.LENGTH_SHORT).show();

                float p = Float.valueOf(priceP.getText().toString());
                Integer pid = Integer.valueOf(pizzaID.getText().toString());
                Integer q = Integer.valueOf(subEditText.getText().toString());

                if (r1[0].getText().toString().equals("Medium")) {
                    Float i = new Float(1.5);
                    p = p * i;
                } else if (r1[0].getText().toString().equals("Large")) {
                    Float i = new Float(2.5);
                    p = p * i;
                }
                p = p * q;

                Toast.makeText(PizzaActivity.this, String.valueOf(p), Toast.LENGTH_SHORT).show();

                db = openOrCreateDatabase("papaJ", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS orders(" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " pizzaID INT(11) NOT NULL," +
                        " size VARCHAR(10) NOT NULL," +
                        " type VARCHAR(10) NOT NULL," +
                        " quan INT(2) NOT NULL," +
                        " price FLOAT(10) NOT NULL);");
                //db.execSQL("INSERT INTO users VALUES(1,'Abbas Salman','33213188','abas.bh@gmail.com');");
                db.execSQL("INSERT INTO orders VALUES(null,"
                        + pid
                        + ",'" + r1[0].getText().toString() + "'," +
                        "'"    + r2[0].getText().toString() + "',"
                        + q + ","
                        + p +
                        ");");


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PizzaActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }*/
}
