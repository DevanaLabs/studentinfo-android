package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiFeedback;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

public class FeedbackFragment extends Fragment {

    @Inject
    ApiFeedback apiFeedback;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.feedback_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((item.getItemId() == R.id.action_send) && (getView() != null)) {

            final EditText feedbackContent = (EditText) getView().findViewById(R.id.feedbackContent);
            final String content = feedbackContent.getText().toString();

            if (TextUtils.isEmpty(content)) {
                feedbackContent.setError(getString(R.string.error_field_required));
                feedbackContent.requestFocus();
            } else {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), R.string.thankyou, Toast.LENGTH_SHORT).show();
                feedbackContent.setError(null);
                feedbackContent.setText("");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!apiFeedback.sendFeedback(content)) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), R.string.somethingHappened, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
