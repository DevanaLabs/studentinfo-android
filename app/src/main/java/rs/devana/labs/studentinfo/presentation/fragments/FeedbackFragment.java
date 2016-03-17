package rs.devana.labs.studentinfo.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        FeedbackFragment fragment = new FeedbackFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View)inflater.inflate(R.layout.fragment_feedback, container, false);
        final EditText feedbackContent = (EditText)view.findViewById(R.id.feedbackContent);
        Button sendButton = (Button)view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = feedbackContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    feedbackContent.setError(getString(R.string.error_field_required));
                    feedbackContent.requestFocus();
                } else {
                    feedbackContent.setError(null);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (apiFeedback.sendFeedback(content)) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), R.string.thankyou, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), R.string.somethingHappened, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

        return view;
    }
}
