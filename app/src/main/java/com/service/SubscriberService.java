package com.service;

import rx.Subscriber;

/**
 * Created by ricar on 09/09/2016.
 */
public interface SubscriberService {
    void addSubscribers(int id, Subscriber... subscribers);
    void removeSubscribers(int id);
}
