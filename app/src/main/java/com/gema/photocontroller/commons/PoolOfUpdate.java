package com.gema.photocontroller.commons;

import com.gema.photocontroller.interfaces.UpdateRefsListener;
import com.gema.photocontroller.interfaces.UpdateRefsPublisher;

import java.util.ArrayList;
import java.util.List;

public class PoolOfUpdate implements UpdateRefsPublisher{

    private List<UpdateRefsListener> listeners;

    public PoolOfUpdate() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addListener(UpdateRefsListener listener) {
        int index = this.listeners.indexOf(listener);
        if (index < 0) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void deleteListener(UpdateRefsListener listener) {
        int index = this.listeners.indexOf(listener);
        if (index >= 0 ) {
            this.listeners.remove(index);
        }
    }

    @Override
    public void notifyListeners() {
        for (UpdateRefsListener listener : this.listeners) {
            listener.update();
        }
    }
}
