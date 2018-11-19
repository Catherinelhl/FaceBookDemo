package bcaasc.io.facebookdemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private Button button;
    private TextView tvContent;
    private CallbackManager callbackManager;

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
                System.out.println("onSuccess:" + loginResult);
                // 您可以通过 SDK 从缓存或应用书签中（如果应用冷启动）加载 AccessToken.getCurrentAccessToken。您应在 Activity 的 onCreate 方法中检查它的有效性：
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                tvContent.setText("ApplicationId:" + accessToken.getApplicationId() +
                        ";\nUserId:" + accessToken.getUserId() +
                        ";\ngetDataAccessExpirationTime:" + accessToken.getDataAccessExpirationTime() +
                        ";\ngetDeclinedPermissions:" + accessToken.getDeclinedPermissions() +
                        ";\ngetExpires:" + accessToken.getExpires() +
                        ";\ngetPermissions:" + accessToken.getPermissions() +
                        ";\ngetSource:" + accessToken.getSource());
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
