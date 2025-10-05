package com.example.myapplication.lab3;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class Lab3Activity extends AppCompatActivity {

    private Button btnCheck, btnNewImage;
    private EditText etTextField;
    private ImageView ivPhoto;

    private ArrayList<String> urls = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private int numberOfImage = 0;
    private String urlAddress = "https://kino.mail.ru/stars/";
    private String urlForImages = "https://kino.mail.ru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);

        btnCheck = findViewById(R.id.btnCheck);
        etTextField = findViewById(R.id.etTextField);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnNewImage = findViewById(R.id.btnNewImage);

        // Добавляем стрелочку назад в ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        getContent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected void getContent() {
        DownloadTask task = new DownloadTask();
        try {
            String context = task.execute(urlAddress).get();
            if (context != null) {
                parseContent(context);

                Log.d("Lab3", "Загружено URL: " + urls.size() + ", Названий: " + names.size());

                if (!urls.isEmpty() && !names.isEmpty()) {
                    for (int i = 0; i < Math.min(urls.size(), 3); i++) {
                        Log.d("Lab3", "Image " + i + ": " + urls.get(i));
                        Log.d("Lab3", "Name " + i + ": " + names.get(i));
                    }
                    playGame();
                } else {
                    Toast.makeText(this, "Не удалось загрузить данные с сайта", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Ошибка загрузки контента", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void parseContent(String htmlContent) {
        try {
            urls.clear();
            names.clear();

            // Ищем блоки itemperson
            Pattern patternCard = Pattern.compile("<div[^>]*class=\"[^\"]*itemperson[^\"]*\"[^>]*>(.*?)</div>\\s*</div>", Pattern.DOTALL);
            Matcher matcherCard = patternCard.matcher(htmlContent);

            while (matcherCard.find()) {
                String cardContent = matcherCard.group(1);

                // фото
                Pattern patternImg = Pattern.compile("<img[^>]*src=\"([^\"]+)\"[^>]*>");
                Matcher matcherImg = patternImg.matcher(cardContent);

                // имя
                Pattern patternName = Pattern.compile("<span[^>]*class=\"[^\"]*link__text[^\"]*\"[^>]*>([^<]+)</span>");
                Matcher matcherName = patternName.matcher(cardContent);

                String imgUrl = null;
                String personName = null;

                if (matcherImg.find()) {
                    imgUrl = matcherImg.group(1);
                    if (imgUrl.startsWith("//")) {
                        imgUrl = "https:" + imgUrl;
                    } else if (imgUrl.startsWith("/")) {
                        imgUrl = urlForImages + imgUrl;
                    }
                }

                if (matcherName.find()) {
                    personName = matcherName.group(1).trim();
                }

                if (imgUrl != null && personName != null && !personName.isEmpty()) {
                    urls.add(imgUrl);
                    names.add(personName);
                    Log.d("Lab3", "Нашли: " + personName + " — " + imgUrl);
                }
            }

            if (urls.isEmpty()) {
                Log.d("Lab3", "Ничего не нашли, возможно изменилась структура страницы");
            }

        } catch (Exception e) {
            Log.e("Lab3", "Ошибка парсинга: " + e.getMessage());
        }
    }

    private void playGame() {
        if (urls.isEmpty() || names.isEmpty()) {
            Toast.makeText(this, "Нет доступных изображений", Toast.LENGTH_SHORT).show();
            return;
        }

        numberOfImage = (int) (Math.random() * urls.size());
        Log.d("Lab3", "Показываем изображение " + numberOfImage + ": " + names.get(numberOfImage));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest imageRequest = new ImageRequest(
                urls.get(numberOfImage), // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        ivPhoto.setImageBitmap(response);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        Log.e("Lab3", "Ошибка загрузки изображения: " + error.getMessage());
                        error.printStackTrace();
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);
    }

    public void onClickBtnCheck(View view) {
        String answer = etTextField.getText().toString();
        if (answer.isEmpty()) {
            Toast toast = Toast.makeText(this, "Ошибка! Пустое поле ввода!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (names.isEmpty() || numberOfImage >= names.size()) {
            Toast toast = Toast.makeText(this, "Ошибка! Нет данных для проверки!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String correctName = names.get(numberOfImage);
        String normalizedAnswer = answer.trim().toLowerCase(Locale.ROOT);
        String normalizedCorrect = correctName.toLowerCase(Locale.ROOT);
        if (normalizedAnswer.equals(normalizedCorrect)) {
            Toast toast = Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT);
            toast.show();
            // При правильном ответе показываем следующее фото
            playGame();
            etTextField.setText("");
        } else {
            Toast toast = Toast.makeText(this, "Неверно! Правильный ответ: " + correctName, Toast.LENGTH_LONG);
            toast.show();
            // При неправильном ответе тоже показываем следующее фото
            playGame();
            etTextField.setText("");
        }
    }

    public void onClickBtnNewImage(View view) {
        playGame();
        etTextField.setText("");
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url = null;
            HttpsURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                urlConnection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        result.append(line);
                        line = bufferedReader.readLine();
                    }
                } else {
                    Log.e("DownloadTask", "HTTP Error: " + responseCode);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return result.toString();
        }
    }
}