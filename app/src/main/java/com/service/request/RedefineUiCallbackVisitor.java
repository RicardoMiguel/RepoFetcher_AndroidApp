package com.service.request;

import android.util.SparseArray;

import com.model.AccessToken;
import com.service.RepoServiceResponse;
import com.service.SubscriberAdapter;

import java.util.List;

import rx.Subscriber;

public class RedefineUiCallbackVisitor {

    public <S extends AccessToken> void swap(ExchangeTokenRequest<S> request, RepoServiceResponse<S> uiCallback){
        removeAndRedefine(request, uiCallback);
    }

    private <S> void removeAndRedefine(BaseRequest<S> request, RepoServiceResponse<S> uiCallback){
        RepoServiceResponse<S> callbackToRemove = request.getUiServiceResponse();
        SparseArray<List<Subscriber<S>>> subscribersList = request.getServiceResponseList().getSubscribersList();

        for (int i = 0; i < subscribersList.size(); i++) {
            int key = subscribersList.keyAt(i);
            List<Subscriber<S>> value = subscribersList.get(key);
            Subscriber<S> toRemove = null;

            for(Subscriber<S> subscriber : value){
                if(subscriber instanceof SubscriberAdapter){
                    if(((SubscriberAdapter) subscriber).getResponse() == callbackToRemove){
                        toRemove = subscriber;
                    }
                }
            }

            if(toRemove != null){
                value.remove(toRemove);
            }
        }

        request.setUiServiceResponse(uiCallback);
    }
}
