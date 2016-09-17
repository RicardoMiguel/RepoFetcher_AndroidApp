package com.service.request;

import com.model.AccessToken;
import com.model.bitbucket.BitBucketAccessToken;
import com.service.RepoServiceResponse;
import com.service.SubscriberAdapter;

import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by ricar on 17/09/2016.
 */
public class RedefineUiCallbackVisitor {

    public <S extends AccessToken> void swap(ExchangeTokenRequest<S> request, RepoServiceResponse<S> uiCallback){
        removeAndRedefine(request, uiCallback);
    }

    private <S> void removeAndRedefine(BaseRequest<S> request, RepoServiceResponse<S> uiCallback){
        RepoServiceResponse<S> callbackToRemove = request.getUiServiceResponse();
        Map<Integer, List<Subscriber<S>>> subscribersList = request.getServiceResponseList().getSubscribersList();

        for(Map.Entry<Integer, List<Subscriber<S>>> entry : subscribersList.entrySet()){
            Subscriber<S> toRemove = null;

            for(Subscriber<S> subscriber : entry.getValue()){
                if(subscriber instanceof SubscriberAdapter){
                    if(((SubscriberAdapter) subscriber).getResponse() == callbackToRemove){
                        toRemove = subscriber;
                    }
                }
            }

            if(toRemove != null){
                entry.getValue().remove(toRemove);
            }
        }

        request.setUiServiceResponse(uiCallback);
    }
}
