package com.repofetcher;

import com.service.FetcherCallsHandler;

import java.io.Serializable;

/**
 * Created by ricar on 15/09/2016.
 */
public class SerializableInteger implements Serializable {
    @FetcherCallsHandler.RepoServiceType int service;

    public SerializableInteger(@FetcherCallsHandler.RepoServiceType int service) {
        this.service = service;
    }
}
