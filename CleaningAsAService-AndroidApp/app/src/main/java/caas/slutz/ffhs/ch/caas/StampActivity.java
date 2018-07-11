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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StampActivity extends AppCompatActivity {
    Person person;

    private List<Stamp> TaskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StampAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_times);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.person = (Person) getIntent().getSerializableExtra("Person");

        TextView listView = findViewById(R.id.textView4);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new StampAdapter(TaskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // set the adapter
        recyclerView.setAdapter(mAdapter);


        try {
            String str_result = new StampActivity.HttpAsyncTask().execute("http://lutz.solutions:8080/rest/stamp/my/?token=" + person.getToken()).get();
            JSONArray mArray = new JSONArray(str_result);
            String tmp = null;

            ArrayList arrayList = new ArrayList();

            for (int i = 0; i < mArray.length(); i++) {

                HashMap<String, String> map = new HashMap<String, String>();

                try {
                    tmp = mArray.getJSONObject(i).getString("startTime");
                    map.put("startTime", tmp);
                } catch (JSONException e) {
                    // If id doesn't exist, this exception is thrown
                }
                try {
                    tmp = mArray.getJSONObject(i).getString("endTime");
                    map.put("endTime", tmp);
                } catch (JSONException e) {
                    // If number_of_devices doesn't exist, this exception is thrown
                }

                arrayList.add(map);
            }

            HashMap map = (HashMap) arrayList.get(0);
            String str = map.get("startTime").toString();
            prepareStampList(arrayList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareStampList(ArrayList arr) {

        for (int i = 0; i < arr.size(); i++) {
            HashMap map = (HashMap) arr.get(i);
            String startTime = "Start:" + map.get("startTime").toString();
            String endTime = "Ende:" + map.get("endTime").toString();

            Stamp stamps = new Stamp(startTime, endTime, "4");
            TaskList.add(stamps);
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
