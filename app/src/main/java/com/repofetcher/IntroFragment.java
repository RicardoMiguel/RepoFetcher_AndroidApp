package com.repofetcher;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.service.ServiceUtils;

/**
 * Created by ricar on 07/09/2016.
 */
public class IntroFragment extends BaseFragment{

    private EditText usernameEditText;

    public IntroFragment() {
        super(R.layout.ask_username);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameEditText = (EditText)view.findViewById(R.id.username_edit_text);
        Button fetchRepoUserListButton = (Button)view.findViewById(R.id.fetch_repo_list_button);
        fetchRepoUserListButton.setOnClickListener( v -> sendQuery(usernameEditText.getText().toString()) );
    }

    private void sendQuery(@Nullable String text){
        Bundle bundle = new Bundle();
        bundle.putString( IntroFragment.class.getName(),text);
        ServiceUtils.runIfInstanceNotNull(fragmentTransitionService,
                () -> fragmentTransitionService.switchFragment(MultipleAccountRepositoriesFragment.class, bundle));
    }
}
