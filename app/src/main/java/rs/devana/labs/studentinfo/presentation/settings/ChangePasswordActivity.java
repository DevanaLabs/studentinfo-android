package rs.devana.labs.studentinfo.presentation.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import rs.devana.labs.studentinfo.R;
import rs.devana.labs.studentinfo.domain.api.ApiAccount;
import rs.devana.labs.studentinfo.infrastructure.dagger.Injector;

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

        setOnEditorActions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(password)){
            passwordEditText.setError(getString(R.string.error_field_required));
            passwordEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(newPassword)){
            newPasswordEditText.setError(getString(R.string.error_field_required));
            newPasswordEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            confirmPasswordEditText.setError(getString(R.string.error_field_required));
            confirmPasswordEditText.requestFocus();
            return;
        }
        if (!newPassword.equals(confirmPassword)){
            confirmPasswordEditText.setError(getString(R.string.passwordsDoNotMatch));
        } else {
            progressDialog = ProgressDialog.show(this, getString(R.string.changePassword), getString(R.string.pleaseWait), true);
            new ChangePasswordTask(this).execute(password, newPassword);
        }
    }

    private void setOnEditorActions(){
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    newPasswordEditText.requestFocus();
                    return true;
                }
                return false;
            }
        });

        newPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    confirmPasswordEditText.requestFocus();
                    return true;
                }
                return false;
            }
        });

        confirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    sendChangePasswordRequest();
                    return true;
                }
                return false;
            }
        });
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
                onBackPressed();
//                if (logoutAfterPasswordChange.isChecked()) {
//                    //TODO: Implement logout from all other devices
//                }
            } else {
                passwordEditText.requestFocus();
                Toast.makeText(context, getString(R.string.passwordChangeNotSuccessful), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
