package ru.yandex.dunaev.mick.mysoundrecorder.adapters;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ru.yandex.dunaev.mick.mysoundrecorder.R;
import ru.yandex.dunaev.mick.mysoundrecorder.fragments.MyPlaybackFragment;
import ru.yandex.dunaev.mick.mysoundrecorder.helpers.MyFileActions;
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

            @Override
            public void onRemoveItem(int position) {
                if(mRecyclerView != null){
                    notifyItemRemoved(position);
                }
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

        ImageView menuImage = (ImageView)cv.findViewById(R.id.more_menu);
        ImageView playImage = (ImageView)cv.findViewById(R.id.image_play);

        textBitrate.setText("bitrate: " + bitRate);
        textSamplerate.setText("samplerate: " + sampleRate);
        textMime.setText(mime);
        textDuration.setText(duration);
        textFileData.setText(dateString);
        textFileSize.setText(fileSize);


        textFileName.setText(fileNames.get(i).getFile());
        setOnClickCard(playImage,fileNames.get(i));
        setOnClickMenu(menuImage,fileNames,i);
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

    private void setOnClickMenu(View v, final MyObservableArrayList<MyFileModel> listFiles, final int positionFile){
        final MyFileModel fm = listFiles.get(positionFile);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popup = new PopupMenu(v.getContext(),v );
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.card_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.id_delete:
                                Log.v("Action","Delete " + fm.getFilePath());
                                if(MyFileActions.deleteFile(fm.getFilePath())){
                                    listFiles.removeItem(positionFile);
                                }
                                break;
                            case R.id.id_share:
                                Log.v("Action","Share " + fm.getFilePath());
                                MyFileActions.shareFile(v.getContext(), fm.getFilePath());
                                break;
                        }

                        return false;
                    }
                });

                setMenuItemsIcon(v.getContext(), popup);

                popup.show();
            }
        });
    }

    private void setMenuItemsIcon(Context context, PopupMenu popupMenu){
        Menu menu = popupMenu.getMenu();

        for(int i = 0; i < menu.size(); i++){
            MenuItem menuItem = menu.getItem(i);
            Drawable icon = menuItem.getIcon();
            if(icon == null) continue;
            ImageSpan imageSpan = new ImageSpan(icon);
            int iconSize = context.getResources().getDimensionPixelSize(R.dimen.menu_item_icon_size);
            icon.setBounds(0, 0, iconSize, iconSize);
            SpannableStringBuilder ssb = new SpannableStringBuilder("       " + menuItem.getTitle());
            ssb.setSpan(imageSpan, 1, 2, 0);
            menuItem.setTitle(ssb);
            menuItem.setIcon(null);
        }
    }
}
