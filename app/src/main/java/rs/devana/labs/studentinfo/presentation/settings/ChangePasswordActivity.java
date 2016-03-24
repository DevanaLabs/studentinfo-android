package rs.devana.labs.studentinfo.presentation.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiAccount;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;
import rs.devana.labs.studentinfo.presentation.login.LoginActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText passwordEditText;
    EditText newPasswordEditText;
    EditText confirmPasswordEditText;
    CheckBox logoutAfterPasswordChange;
    ProgressDialog progressDialog;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    ApiAccount apiAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Injector.INSTANCE.getApplicationComponent().inject(this);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        logoutAfterPasswordChange = (CheckBox) findViewById(R.id.checkBoxLogout);

        Button changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChangePasswordRequest();
            }
        });
    }

    private void sendChangePasswordRequest(){
        String password = passwordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!newPassword.equals(confirmPassword)){
            confirmPasswordEditText.setError(getString(R.string.passwordsDoNotMatch));
        } else {
            progressDialog = ProgressDialog.show(this, getString(R.string.changePassword), getString(R.string.pleaseWait), true);
           new ChangePasswordTask(this).execute(password, newPassword);
        }
    }

    public class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {

        Context context;

        public ChangePasswordTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return apiAccount.changePassword(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressDialog.dismiss();
            if (success){
                Toast.makeText(context, getString(R.string.passwordChangeSuccessful), Toast.LENGTH_SHORT).show();
                if (logoutAfterPasswordChange.isChecked()) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("accessToken", "");
                    editor.putString("slug", "");
                    editor.putInt("userId", 0);
                    editor.apply();

                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                }
            } else {
                Toast.makeText(context, getString(R.string.passwordChangeNotSuccessful), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
