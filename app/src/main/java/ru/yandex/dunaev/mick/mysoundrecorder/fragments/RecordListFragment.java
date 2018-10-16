package ru.yandex.dunaev.mick.mysoundrecorder.fragments;


import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.adapters.MyRecordListAdapter;
import ru.yandex.dunaev.mick.mysoundrecorder.lists.MyObservableArrayList;
import ru.yandex.dunaev.mick.mysoundrecorder.models.MyFileModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordListFragment extends Fragment {

    private FileObserver observer =
            new FileObserver(android.os.Environment.getExternalStorageDirectory().toString()
                    + "/MySoundRecorder", FileObserver.CLOSE_WRITE | FileObserver.DELETE) {
                // set up a file observer to watch this directory on sd card
                @Override
                public void onEvent(int event, String file) {
                    switch (event){
                        case FileObserver.CLOSE_WRITE:
                            updateFileList();
                            fileList.onInsertItem();
                            break;
                        case FileObserver.DELETE:
                            //updateFileList();
                            break;
                    }
                }
            };


    private MyObservableArrayList<MyFileModel> fileList = new MyObservableArrayList<>();
    @BindView(R.id.recycler) RecyclerView mRecyclerView;

    public RecordListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record_list, container, false);
        ButterKnife.bind(this,v);
        updateFileList();
        observer.startWatching();

        mRecyclerView.setAdapter(new MyRecordListAdapter(fileList));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        return v;
    }

    private void updateFileList() {
        fileList.clear();
        String path = Environment.getExternalStorageDirectory().toString()+"/MySoundRecorder";
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file : files)
        {
            fileList.add(new MyFileModel(getActivity(),file.getName(),path + "/" + file.getName()));
        }
        fileList.onChangeList();
    }

}
