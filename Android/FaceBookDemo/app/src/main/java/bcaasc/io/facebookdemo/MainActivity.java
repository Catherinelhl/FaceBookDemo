package bcaasc.io.facebookdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private Button button;
    private TextView tvContent;
    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        button = (Button) findViewById(R.id.btn_login);
        tvContent = (TextView) findViewById(R.id.tv_result);
        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
//         If using in a fragment
//        loginButton.setFragment(this);

        initListener();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 您可以通过 SDK 从缓存或应用书签中（如果应用冷启动）加载 AccessToken.getCurrentAccessToken。您应在 Activity 的 onCreate 方法中检查它的有效性：
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (!isLoggedIn) {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                }
            }
        });

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                System.out.println("onSuccess:" + loginResult.getAccessToken());
                final AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = null;
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            System.out.println("facebook - profile:" + profile2.getFirstName());
                            profile = profile2;
//                            ";\ngetDataAccessExpirationTime:\t" + accessToken.getDataAccessExpirationTime() +
//                                    ";\ngetDeclinedPermissions:\t" + accessToken.getDeclinedPermissions() +
                            tvContent.setText("ApplicationId:\t" + accessToken.getApplicationId() +
                                    ";\nUserId:\t" + accessToken.getUserId() +
                                    ";\nToken:\t" + accessToken.getToken() +
                                    ";\ngetExpires:\t" + accessToken.getExpires() +
                                    ";\ngetPermissions:\t" + accessToken.getPermissions() +
                                    ";\ngetSource:\t" + accessToken.getSource() +
                                    ";\ngetFirstName:\t" + profile.getFirstName() +
                                    ";\ngetMiddleName:\t" + profile.getMiddleName() +
                                    ";\ngetLastName:\t" + profile.getLastName() +
                                    ";\ngetName:\t" + profile.getName() +
                                    ";\ngetLinkUri:\t" + profile.getLinkUri() +
                                    ";\ngetId:\t" + profile.getId());
                            if (mProfileTracker != null) {
                                mProfileTracker.stopTracking();
                            }

                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    profile = Profile.getCurrentProfile();
                    System.out.println("facebook - profile:" + profile.getFirstName());
//                    ";\ngetDataAccessExpirationTime:\t" + accessToken.getDataAccessExpirationTime() +
//                            ";\ngetDeclinedPermissions:\t" + accessToken.getDeclinedPermissions() +
                    tvContent.setText("ApplicationId:\t" + accessToken.getApplicationId() +
                            ";\nUserId:\t" + accessToken.getUserId() +
                            ";\nToken:\t" + accessToken.getToken() +
                            ";\ngetExpires:\t" + accessToken.getExpires() +
                            ";\ngetPermissions:\t" + accessToken.getPermissions() +
                            ";\ngetSource:\t" + accessToken.getSource() +
                            ";\ngetFirstName:\t" + profile.getFirstName() +
                            ";\ngetMiddleName:\t" + profile.getMiddleName() +
                            ";\ngetLastName:\t" + profile.getLastName() +
                            ";\ngetName:\t" + profile.getName() +
                            ";\ngetLinkUri:\t" + profile.getLinkUri() +
                            ";\ngetId:\t" + profile.getId());
                }


            }

            @Override
            public void onCancel() {
                // App code
                tvContent.setText("onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                tvContent.setText("onError:" + error);

            }
        });
    }

}
