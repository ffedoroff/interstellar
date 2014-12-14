package ru.rfedorov.wfinterstellar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via Google+ sign in.
 */
public class LoginActivity extends PlusBaseActivity {
    private static final String TAG = "LoginActivity";

    // UI references.
    private View mProgressView;
    private SignInButton mPlusSignInButton;
    private View mSignOutButtons;
    private View mLoginFormView;
//    private Switch mToggleWearNotifier;
    private Button mToggleWearNotifier;
//    private ToggleButton mToggleButtonServer;
    private EditText mMorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Controller.getInstance().mainActivity = this;
        setContentView(R.layout.activity_login);

        // Find the Google+ sign in button.
//        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
//        if (supportsGooglePlayServices()) {
//            // Set a listener to connect the user when the G+ button is clicked.
//            mPlusSignInButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    signIn();
//                }
//            });
//        } else {
//            // Don't offer G+ sign in if the app's version is too low to support Google Play
//            // Services.
//            mPlusSignInButton.setVisibility(View.GONE);
//            return;
//        }

//        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
//        mSignOutButtons = findViewById(R.id.plus_sign_out_buttons);
        mMorse = (EditText)findViewById(R.id.morse);

//        mToggleButtonServer = (ToggleButton)findViewById(R.id.mToggleServer);
//        mToggleButtonServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Controller.getInstance().ServerEnabled = !Controller.getInstance().ServerEnabled;
//                Controller.getInstance().onModelChanged();
//            }
//        });

        mToggleWearNotifier = (Button)findViewById(R.id.mToggle);
        mToggleWearNotifier.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.getInstance().getModel().setMessage(mMorse.getText().toString());
                Controller.getInstance().getModel().setEnabled(!Controller.getInstance().getModel().getEnabled());
                Controller.getInstance().onModelChanged();
            }
        });

//        mToggleWearNotifier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Controller.getInstance().getModel().setMessage(mMorse.getText().toString());
//                Controller.getInstance().getModel().setEnabled(mToggleWearNotifier.isChecked());
//                Controller.getInstance().onModelChanged();
//            }
//        });
        reCreateUnits();
    }

    public void reCreateUnits() {
        mMorse.setText(Controller.getInstance().getModel().getMessage());
//        mToggleButtonServer.setChecked(Controller.getInstance().ServerEnabled);
//        mToggleWearNotifier.setChecked(Controller.getInstance().getModel().getEnabled());
        if (Controller.getInstance().getModel().getEnabled()) {
            mToggleWearNotifier.setBackgroundColor(Color.parseColor("#F44336"));
            mToggleWearNotifier.setText("STOP");
        } else {
            mToggleWearNotifier.setBackgroundColor(Color.parseColor("#2196f3"));
            mToggleWearNotifier.setText("Send Message");
        }
        Log.v(TAG, "reCreateUnits enabled=" + Controller.getInstance().getModel().getEnabled());
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onPlusClientSignIn() {
        //Set up sign out and disconnect buttons.
//        Button signOutButton = (Button) findViewById(R.id.plus_sign_out_button);
//        signOutButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });
//        Button disconnectButton = (Button) findViewById(R.id.plus_disconnect_button);
//        disconnectButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                revokeAccess();
//            }
//        });
    }

    @Override
    protected void onPlusClientBlockingUI(boolean show) {
        showProgress(show);
    }

    @Override
    protected void updateConnectButtonState() {
        //TODO: Update this logic to also handle the user logged in by email.
        boolean connected = getPlusClient().isConnected();

        if (mSignOutButtons != null)
            mSignOutButtons.setVisibility(connected ? View.VISIBLE : View.GONE);
        if (mPlusSignInButton != null)
            mPlusSignInButton.setVisibility(connected ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onPlusClientRevokeAccess() {
        // TODO: Access to the user's G+ account has been revoked.  Per the developer terms, delete
        // any stored user data here.
    }

    @Override
    protected void onPlusClientSignOut() {

    }

    /**
     * Check if the device supports Google Play Services.  It's best
     * practice to check first rather than handling this as an error case.
     *
     * @return whether the device supports Google Play Services
     */
    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }
}



