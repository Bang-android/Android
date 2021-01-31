package org.techtown.bangboard2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBoardFragment extends Fragment {

    MainActivity mainActivity;
    static RecyclerView recyclerView;
    public static ListBoardAdapter adapter;
    static TextView textView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listboard, container, false);

        textView = view.findViewById(R.id.textView);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListBoardAdapter(mainActivity);
        recyclerView.setAdapter(adapter);

        SelectDB();

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(new CreateBoardFragment());  //글쓰기 버튼을 누르면 메인 액티비티를 통해 CreateBoardFragment로 전환
            }
        });

        return view;
    }

    static class ListBoardAdapter extends RecyclerView.Adapter<ListBoardAdapter.ViewHolder> {
        ArrayList<Board> items = new ArrayList<Board>();

        MainActivity mainActivity;

        public ListBoardAdapter (Context context) {
            if (context instanceof MainActivity) {
                mainActivity = (MainActivity) context;
            }
        }
        public void addItemFirst (Board item) {
            items.add(0, item);
        }

        public Board getItem (int position) {
            return items.get(position);
        }

        public void setItems (ArrayList<Board> items) {
            this.items = items;
        }

        public void setItem (int position, Board item) {
            items.set(position, item);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.board_item, parent, false);

            return new ViewHolder(itemView, mainActivity);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Board item = items.get(position);
            holder.setItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView textTitle;
            TextView textDate;
            TextView textUser;

            public ViewHolder(@NonNull View itemView, MainActivity mainActivity) {
                super(itemView);

                textTitle = itemView.findViewById(R.id.textTitle);
                textDate = itemView.findViewById(R.id.textDate);
                textUser = itemView.findViewById(R.id.textUser);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();

                        mainActivity.changeFragment(ViewBoardFragment.newInstance(position, "List"));  // 선택한 itemView 의 position 을 넘겨줌 -> 이 포지션을 이용해 ViewBoardFragment에서 정보들을 띄움
                    }
                });
            }

            public void setItem (Board item) {
                textTitle.setText(item.getTitle());
                textDate.setText(item.getDate());
                textUser.setText(item.getUid());
            }

        }
    }

    public void SelectDB() {
        // Retrofit2 를 이용한 코드로 수정
        Gson gson = new Gson();
        Call<String> call = RetrofitClient.getInstance().getApiService().selectBoard();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                 if (response.isSuccessful()) {
                     Log.i("Bang", response.body());

                     try {
                         JSONArray jsonArray = new JSONArray(response.body());
                         for (int i = 0; i < jsonArray.length(); i++) {
                             Board item = gson.fromJson(jsonArray.get(i).toString(), Board.class);
                             adapter.addItemFirst(item);
                         }
                         adapter.notifyDataSetChanged();

                         if (adapter.getItemCount() > 0) {
                             textView.setText("");
                         }

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Bang", "에러 : " + t.getMessage());
            }
        });
    }
}
