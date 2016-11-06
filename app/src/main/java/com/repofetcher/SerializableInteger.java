package com.repofetcher;

import com.service.holder.RepoServiceType;

import java.io.Serializable;

public class SerializableInteger implements Serializable {
    @RepoServiceType
    int service;

    public SerializableInteger(@RepoServiceType int service) {
        this.service = service;
    }
}
