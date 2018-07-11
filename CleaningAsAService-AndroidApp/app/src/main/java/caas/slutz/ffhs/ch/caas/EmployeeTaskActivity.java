package caas.slutz.ffhs.ch.caas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmployeeTaskActivity extends AppCompatActivity {
    Person person;
    private List<EmployeeTask> employeeTaskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EmployeeTaskAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new EmployeeTaskAdapter(employeeTaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // set the adapter
        recyclerView.setAdapter(mAdapter);

        this.person = (Person) getIntent().getSerializableExtra("Person");
        //TextView textView = findViewById(R.id.textView6);

        try {
            //String str_result = new EmployeeTaskActivity.HttpAsyncTask().execute("http://lutz.solutions:8080/rest/work/getAll/?token=da134349-6749-429b-b8b3-8dc3a7d52fcb.1c975589-f211-487d-8633-5066e8c31a30").get();
            String str_result = new EmployeeTaskActivity.HttpAsyncTask().execute("http://lutz.solutions:8080/rest/work/my/?token="+ person.getToken()).get();
            JSONArray mArray = new JSONArray(str_result);
            String tmp = null;

            ArrayList arrayList = new ArrayList();

            for (int i=0 ; i < mArray.length(); i++){
                // get Objects using index
                JSONObject jsonobject= mArray.getJSONObject(i);

                try {
                    JSONObject order = jsonobject.getJSONObject("order");
                    JSONObject customer = order.getJSONObject("customer");
                    JSONObject person = customer.getJSONObject("person");
                    JSONObject account = person.getJSONObject("account");

                    //String email = account.getString("email");
                    //String dateOrdered = order.getString("dateOrdered");email
                    //String description = order.getString("description");
                    //String hours = order.getString("hours");

                    HashMap<String, String> map = new HashMap<String, String>();

                    try {
                        tmp = account.getString("email");
                        map.put("email", tmp);
                    } catch (JSONException e) {
                        // If id doesn't exist, this exception is thrown
                    }
                    try {
                        tmp = order.getString("dateOrdered");
                        map.put("dateOrdered", tmp);
                    } catch (JSONException e) {
                        // If number_of_devices doesn't exist, this exception is thrown
                    } try {
                        tmp = order.getString("description");
                        map.put("description", tmp);
                    } catch (JSONException e) {
                        // If number_of_devices doesn't exist, this exception is thrown
                    } try {
                        tmp = order.getString("hours");
                        map.put("hours", tmp);
                    } catch (JSONException e) {
                        // If number_of_devices doesn't exist, this exception is thrown
                    }
                    arrayList.add(map);
                } catch (Exception e){

                }
                //textView.setText(arrayList.get(1).toString());
                //textView.setText(email + " " + dateOrdered + " " + description + " " + hours);
            }

            //textView.setText("USA");


            HashMap map = (HashMap) arrayList.get(0);
            String email = map.get("email").toString();
            String dateOrdered = map.get("dateOrdered").toString();
            String description = map.get("description").toString();
            String hours = map.get("hours").toString();

            //textView.setText(email + " " + dateOrdered + " " + description + " " + hours);

            prepareEmployTaskList(arrayList);


        }catch (Exception e){
            //textView.setText(e.toString());
            e.printStackTrace();
        }

    }

    private void prepareEmployTaskList(ArrayList arr) {


        for (int i = 0; i < arr.size(); i++) {
            HashMap map = (HashMap) arr.get(i);
            String email = map.get("email").toString();
            String dateOrdered = map.get("dateOrdered").toString();
            String description = map.get("description").toString();
            String hours = map.get("hours").toString();

            EmployeeTask employeeTask = new EmployeeTask(email,dateOrdered,description,hours);
            employeeTaskList.add(employeeTask);
        }


        mAdapter.notifyDataSetChanged();
    }


    /**
     * REST
     */
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            /*
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);
                String token = json.getString("token");
                textView.setText(token);
            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText(e.toString());
            }
            */
        }
    }
    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}
