package com.repofetcher;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.service.ServiceUtils;

/**
 * Created by ricar on 07/09/2016.
 */
public class IntroFragment extends BaseFragment{
    private static final String TAG = IntroFragment.class.getName();

    private EditText usernameEditText;

    public IntroFragment() {
        super(R.layout.ask_username);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        usernameEditText = (EditText)view.findViewById(R.id.username_edit_text);
        Button fetchRepoUserListButton = (Button)view.findViewById(R.id.fetch_repo_list_button);
        fetchRepoUserListButton.setOnClickListener( v -> sendQuery(usernameEditText.getText().toString()) );
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void sendQuery(@Nullable String text){
        Bundle bundle = new Bundle();
        bundle.putString( IntroFragment.class.getName(), text);
        ServiceUtils.runIfInstanceNotNull(fragmentTransitionService,
                () -> fragmentTransitionService.switchFragment(MultipleAccountRepositoriesFragment.class, bundle));
    }


}
