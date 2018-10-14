package ru.yandex.dunaev.mick.mysoundrecorder.adapters;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.lists.MyObservableArrayList;

public class MyRecordListAdapter extends RecyclerView.Adapter{
    private MyObservableArrayList<String> fileNames;
    private RecyclerView mRecyclerView;

    public MyRecordListAdapter(final MyObservableArrayList<String> fileNames) {
        this.fileNames = fileNames;
        this.fileNames.setChangeListener(new MyObservableArrayList.IChangeListener() {
            @Override
            public void onChangeList() {
                notifyDataSetChanged();
            }

            @Override
            public void OnInsertItem(int size) {
                if(mRecyclerView != null) mRecyclerView.getLayoutManager().scrollToPosition(size - 1);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cv = (CardView)LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_file,viewGroup,false);
        return new RecyclerView.ViewHolder(cv) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CardView cv = (CardView)viewHolder.itemView;
        TextView textFileName = (TextView)cv.findViewById(R.id.file_name);
        textFileName.setText(fileNames.get(i));
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }
}
