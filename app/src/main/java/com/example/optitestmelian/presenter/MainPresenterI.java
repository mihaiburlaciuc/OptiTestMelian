package com.example.optitestmelian.presenter;

public interface MainPresenterI {
    // From View
    boolean checkWebsiteData();
    // From Model
    void receiveWebsiteResponse(String response);
}
