package com.ecarriers.drivers.view.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecarriers.drivers.R;
import com.ecarriers.drivers.data.preferences.Preferences;
import com.ecarriers.drivers.data.remote.SyncUtils;
import com.ecarriers.drivers.data.remote.listeners.ILoginListener;
import com.ecarriers.drivers.data.remote.responses.LoginResponse;
import com.ecarriers.drivers.utils.Connectivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILoginListener {

    private static final int MIN_PASSWORD_LENGHT = 8;

    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.progress_view) View progressView;
    @BindView(R.id.login_view) View loginView;
    @BindView(R.id.btn_sign_in) Button btnSignIn;

    private String mEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        progressView = findViewById(R.id.progress_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(LoginActivity.this, PreferenceActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_sign_in)
    void onBtnSignInClick(View view){
        attemptLogin();
    }

    private void attemptLogin() {
        // Reset errors.
        etEmail.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            closeKeyboard();
            if(Connectivity.isConnected(getApplicationContext())){
                showProgress(true);
                mEmail = email;
                login(email, password);
            }else{
                Snackbar.make(loginView, R.string.msg_no_connection, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void login(String email, String password){
        SyncUtils syncUtils = new SyncUtils(LoginActivity.this);
        syncUtils.login(LoginActivity.this, email, password);
    }

    @Override
    public void onResponse(boolean success, LoginResponse response) {
        showProgress(false);

        if (success && response != null && response.getToken() != null && !response.getToken().isEmpty()) {

            Preferences.setSessionToken(getApplicationContext(), response.getToken());
            if(mEmail != null && !mEmail.isEmpty()) {
                Preferences.setCurrentUserEmail(getApplicationContext(), mEmail);
            }

            Intent i = new Intent(getApplicationContext(), TripsActivity.class);
            startActivity(i);
        } else {
            mEmail = "";
            etPassword.setError(getString(R.string.error_incorrect_password));
            etPassword.requestFocus();
        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= MIN_PASSWORD_LENGHT;
    }

    private void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginView.setVisibility(show ? View.GONE : View.VISIBLE);
        loginView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

