package com.service;

import java.util.List;

import rx.Subscriber;

/**
 * Created by ricar on 09/09/2016.
 */
public interface SubscriberService {
    void addSubscribers(int id, List<Subscriber> subscribers);
    void removeSubscribers(int id);
}
