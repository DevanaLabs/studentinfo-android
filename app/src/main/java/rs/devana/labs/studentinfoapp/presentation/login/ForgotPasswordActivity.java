package rs.devana.labs.studentinfoapp.presentation.login;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import rs.devana.labs.studentinfoapp.R;
import rs.devana.labs.studentinfoapp.domain.api.ApiAccount;
import rs.devana.labs.studentinfoapp.infrastructure.dagger.Injector;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Inject
    ApiAccount apiAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Injector.INSTANCE.getApplicationComponent().inject(this);

        final EditText recoveryEmailEditText = (EditText) findViewById(R.id.recoveryEmailEditText);

        Button sendRecoveryEmailButton = (Button) findViewById(R.id.sendRecoveryEmailButton);
        sendRecoveryEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailEntered = recoveryEmailEditText.getText().toString();
                if (TextUtils.isEmpty(emailEntered)){
                    recoveryEmailEditText.setError(getString(R.string.error_field_required));
                    recoveryEmailEditText.requestFocus();
                } else
                if (!emailIsValid(emailEntered)){
                    recoveryEmailEditText.setError(getString(R.string.error_invalid_email));
                    recoveryEmailEditText.requestFocus();
                } else {
                    recoveryEmailEditText.setError(null);
                    sendRecoveryEmail(emailEntered);
                }
            }
        });
    }

    private boolean emailIsValid(String emailEntered){
        return Patterns.EMAIL_ADDRESS.matcher(emailEntered).matches();
    }

    private void sendRecoveryEmail(String email){
        new GroupFetchTask(this).execute(email);
    }

    public class GroupFetchTask extends AsyncTask<String, Void, Boolean> {

        private Context context;

        public GroupFetchTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return apiAccount.sendRecoveryEmail(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                Toast.makeText(context, context.getString(R.string.emailSent), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, context.getString(R.string.emailNotSent), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
