package ru.yandex.dunaev.mick.mysoundrecorder.adapters;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.fragments.MyPlaybackFragment;
import ru.yandex.dunaev.mick.mysoundrecorder.lists.MyObservableArrayList;
import ru.yandex.dunaev.mick.mysoundrecorder.models.MyFileModel;

public class MyRecordListAdapter extends RecyclerView.Adapter{
    private MyObservableArrayList<MyFileModel> fileNames;
    private RecyclerView mRecyclerView;

    public MyRecordListAdapter(final MyObservableArrayList<MyFileModel> fileNames) {
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
        fileNames.get(i).generateMediaInfo();
        String bitRate = fileNames.get(i).getBitRate();
        String sampleRate = fileNames.get(i).getSampleRate();
        String mime = fileNames.get(i).getMime();
        String duration = fileNames.get(i).getDuration();
        String fileData = fileNames.get(i).getDateCreated();
        String fileSize = fileNames.get(i).getSizeOfFile();
        Date date = fileNames.get(i).getDateFile();

        String dateString = DateUtils.formatDateTime(
                cv.getContext(),
                date.getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
        );

        TextView textBitrate = (TextView)cv.findViewById(R.id.textBitrate);
        TextView textSamplerate = (TextView) cv.findViewById(R.id.textSamplerate);
        TextView textMime = (TextView)cv.findViewById(R.id.textMime);
        TextView textDuration = (TextView)cv.findViewById(R.id.textDuration);
        TextView textFileData = (TextView)cv.findViewById(R.id.textFileDate);
        TextView textFileSize = (TextView)cv.findViewById(R.id.textFileSize);

        textBitrate.setText("bitrate: " + bitRate);
        textSamplerate.setText("samplerate: " + sampleRate);
        textMime.setText(mime);
        textDuration.setText(duration);
        textFileData.setText(dateString);
        textFileSize.setText(fileSize);


        textFileName.setText(fileNames.get(i).getFile());
        setOnClickCard(cv,fileNames.get(i));
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    private void setOnClickCard(View v, final MyFileModel fm){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPlaybackFragment pf = MyPlaybackFragment.newInstance(fm);
                FragmentTransaction transaction = ((FragmentActivity) v.getContext())
                        .getSupportFragmentManager()
                        .beginTransaction();
                pf.show(transaction,"dialog_playback");
            }
        });
    }
}
