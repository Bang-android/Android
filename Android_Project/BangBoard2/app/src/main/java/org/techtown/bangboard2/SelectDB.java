package org.techtown.bangboard2;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SelectDB {
    MainActivity mainActivity;

    public SelectDB (Context context) {
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    public void task() {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                selectDB();
            }
        }).start();
    }

    public void selectDB() {
        String urlStr = MainActivity.DB_URL + "select.php";

        try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                String line = null;
                StringBuffer sb = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();

                showResult (sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showResult (String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("test");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString("title");
                String content = item.getString("content");
                String date = item.getString("date");
                String uid = item.getString("uid");

                Board boardItem = new Board(title, content, date, uid);

                ListBoardFragment.adapter.addItemFirst(boardItem);

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListBoardFragment.adapter.notifyDataSetChanged();

                        if (ListBoardFragment.adapter.getItemCount() > 0) {
                            ListBoardFragment.textView.setText("");  // RecyclerView에 아이템이 하나라도 있으면 아이템을 추가해달라는 TextView를 지움
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
