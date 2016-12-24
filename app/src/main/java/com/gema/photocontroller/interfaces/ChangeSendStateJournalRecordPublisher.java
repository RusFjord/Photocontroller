package com.gema.photocontroller.interfaces;

public interface ChangeSendStateJournalRecordPublisher {

    void addListener(ChangeSendStateJournalRecordListener listener);
    void deleteListener(ChangeSendStateJournalRecordListener listener);
    void notifyListeners();

}
