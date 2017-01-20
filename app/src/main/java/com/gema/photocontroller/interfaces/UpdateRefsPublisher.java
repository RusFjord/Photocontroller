package com.gema.photocontroller.interfaces;

public interface UpdateRefsPublisher {
    void addListener(UpdateRefsListener listener);
    void deleteListener(UpdateRefsListener listener);
    void notifyListeners();
}
