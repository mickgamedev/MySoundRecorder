package ru.yandex.dunaev.mick.mysoundrecorder;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    private MyRecordController mController = MyRecordController.getInstance();

    @BindView(R.id.chronometer) Chronometer mChronometer;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this,v);

        setControllerListener();
        return v;
    }

    @OnClick(R.id.toogle_record_button)
    public void OnClickTogleRecordButton(View v){
        mController.pressToggleRecordButton();
    }

    private void setControllerListener(){
        mController.setListener(new MyRecordController.IRecordControlListener() {
            @Override
            public void onStart() {
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
            }

            @Override
            public void onStop() {
                mChronometer.stop();
            }
        });
    }
}
