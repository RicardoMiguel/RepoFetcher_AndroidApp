package com.service;

import java.util.List;

import rx.Subscriber;

public interface SubscriberService {
    void addSubscribers(int id, List<Subscriber> subscribers);
    void removeSubscribers(int id);
}
