package ru.yandex.dunaev.mick.mysoundrecorder.lists;

import java.util.ArrayList;

public class MyObservableArrayList<E> extends ArrayList<E> {
    public interface IChangeListener{
        void onChangeList();
        void OnInsertItem(int size);
        void onRemoveItem(int position);
    }

    private IChangeListener listener;

    public void onChangeList() {
        if(listener != null) listener.onChangeList();
    }

    public void onInsertItem(){
        if(listener != null) listener.OnInsertItem(size());
    }

    public void setChangeListener(IChangeListener listener){
        this.listener = listener;
    }

    public void removeItem(int position){
        remove(position);
        if(listener != null) listener.onRemoveItem(position);
    }
}
