package github.bewantbe.depressionanalysis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import cz.msebera.android.httpclient.Header;
import github.bewantbe.R;

public class SpectrogramShow extends AppCompatActivity {

    public ConcurrentLinkedQueue<JSONObject> spectrogram = new ConcurrentLinkedQueue<>();
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectrogram_show);
        getCall();
        System.out.println("getCall() called");
        imgView = findViewById(R.id.imageView2);
    }

    String imageDataString = null;

    protected void getImg() {
        while (!spectrogram.isEmpty()) {
            JSONObject jsonArray = spectrogram.poll();
            System.out.println("JSONARRAY: " + jsonArray.toString());
            if (jsonArray != null) {
                System.out.println("Show Spectrogram Called");

                File file = new File("C:\\Users\\kellie\\Documents\\apiServer\\uploads\\" +
                        PredictiveIndex.getUSER() + "\\" + PredictiveIndex.getSESSION_ID() + "\\imgOut\\test.bmp");
                try {
                    // Reading a Image file from file system
                    FileInputStream imageInFile = new FileInputStream(file);
                    byte[] imageData = new byte[(int) file.length()];
                    imageInFile.read(imageData);
                    System.out.println("Image has been read");

                    // Converting Image byte array into Base64 String
                    imageDataString = Base64.encodeToString(imageData, Base64.DEFAULT);
                    //imageInFile.close();

                    byte[] imageByteArray = Base64.decode(imageDataString, Base64.DEFAULT);
                    try {
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                        imgView.setImageBitmap(bmp);
                    } catch (Exception e) {
                        Log.d("tag", e.toString());
                    }
                    System.out.println("Image Successfully Manipulated!");
                } catch (FileNotFoundException e) {
                    System.out.println("Image not found" + e);
                } catch (IOException ioe) {
                    System.out.println("Exception while reading the Image " + ioe);
                }
            }
        }
    }

    private void getCall() {
        RequestParams reqParam = new RequestParams();
//        reqParam.add("username", "aaa");
//        reqParam.add("password", "aaa@123");
//        JSONArray myResponse;

 /*       String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        filePath += "/DepressionAnalysis";
//        filePath += "/S1234567A_26_feb_2019_08:20:00_gmt.pcm";
        filePath = PredictiveIndex.getSAVED_FILE();
        filePath += "/imgOut";
        System.out.println(filePath);

        File theFile = new File(filePath);
        try {
            reqParam.put("file", theFile);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        String url;
        url = "/users/" + PredictiveIndex.getUSER() + "/analysis/" + PredictiveIndex.getSESSION_ID() + "/uploads/imgOut";

//        url = "uploader";

        restClient.get(url, reqParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("API", "Get : " + response);
                try {
                    JSONObject serverResp = new JSONObject(response.toString());
                    spectrogram.add(serverResp);
                    getImg();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                System.out.println("Status: " + statusCode + "\nHeaders: " + headers + "\nResponse: " + timeline);
                getImg();
            }
        });
    }
}
