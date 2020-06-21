package com.example.administrator.bkod_androidclient;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.administrator.bkod_androidclient.databinding.ActivityLoginBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import Manager.ActivityManager;
import Manager.OnlineManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
        start();

        // Kiem tra neu da dang nhap thi chuyen qua man hinh dang nhap
        if (OnlineManager.getInstance().userInfo != null) {
            loginSuccess();
        }
        // Lien ket layout voi activity
        ActivityManager.getInstance().activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // Thay doi tieu de
        ActivityManager.getInstance().activityLoginBinding.actionBar.actionBarTitle.setText(getString(R.string.app_full_name).toUpperCase());
        // Gan su kien cho nut dang nhap
        ActivityManager.getInstance().activityLoginBinding.loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Neu bam nut dang nhap thi tien hanh dang nhap
                login();
            }
        });
        // Khi nhan nut go sau khi nhap mat khau thi tien hanh dang nhap
        ActivityManager.getInstance().activityLoginBinding.loginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO){
                    // Goi nut dang nhap
                    ActivityManager.getInstance().activityLoginBinding.loginButton.performClick();
                }
                return false;
            }
        });
        // Goi su kien khi nhan vao nut nut dang ky tu do
        ActivityManager.getInstance().activityLoginBinding.freeSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Neu bam nut dang ky tu do thi chuyen sang man hinh dang ky tu do
                signUp();
            }
        });
        // Goi su kien khi nhan vao nut nut dang ky voi nha truong
        ActivityManager.getInstance().activityLoginBinding.signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Neu bam nut dang ky voi nha truong thi mo trang web form url
                if (OnlineManager.getInstance() != null && OnlineManager.getInstance().setting != null) {
                    // Neu da co setting roi thi mo link dang ky
                    Uri uri = Uri.parse(OnlineManager.getInstance().setting.getFormUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    // Neu chua co setting thi thong bao
                    ActivityManager.getInstance().makeLongToast(getString(R.string.server_waiting_connection));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OnlineManager.getInstance().getmSocket() != null) {
            if (OnlineManager.getInstance().getmSocket().connected() == true) {
                // Neu ket noi den server roi thi khong hien thi progress nua
                progressOff();
            } else {
                // Neu chua ket noi den server thi doi den khi ket noi duoc
                OnlineManager.getInstance().getSocketHandler().post(OnlineManager.getInstance().getSocketRunnable());
            }
        }
    }

    public void onBackPressed() {
        // TODO: Thoat chuong trinh khi nhan nut back tren dien thoai
        super.onBackPressed();
        finish();
    }

    // TODO: Chuyen sang activity dang ky
    public void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        this.startActivity(intent);
    }
    // TODO: Chuyen sang activity home khi dang nhap thanh cong
    public void loginSuccess() {
        Intent intent = new Intent(this, HomeTabsActivity.class);
        this.startActivity(intent);
        finish();
    }

    // TODO: Tien hanh gui message dang nhap len server
    public void login() {
        // Kiem tra xem email co hop le khong
        if (ActivityManager.isEmailValid(ActivityManager.getInstance().activityLoginBinding.loginEmail.getText().toString()) == false){
            // In ra man hinh thong bao email khong hop le
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_invalid_email));
        } else if (ActivityManager.isPasswordValid(ActivityManager.getInstance().activityLoginBinding.loginPassword.getText().toString()) == false){
            // Kiem tra xem mat khau co hop le khong
            // In ra man hinh thong bao mat khau khong hop le
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_invalid_password));
        } else {
            // Hien thi thong bao dang dang nhap
            progressOn();
            // Ma hoa mat khau
            String password = OnlineManager.sha256(ActivityManager.getInstance().activityLoginBinding.loginPassword.getText().toString() + ActivityManager.getInstance().activityLoginBinding.loginEmail.getText().toString() + "BKODv1Habvietio");
            // Tao message dang nhap
            String loginMessage =
                    "{\"COMMAND\":\"LOGIN\"," +
                            "\"USERNAME\":\"" + ActivityManager.getInstance().activityLoginBinding.loginEmail.getText().toString() + "\"," +
                            "\"PASSWORD\":\"" + password + "\"}";
            // Gui message len server
            if (OnlineManager.getInstance().sendMessage(loginMessage) == false) {
                // Neu khong dang nhap duoc thi tat progress di
                progressOff();
            }
        }
    }

    //TODO: Bat progress
    public void progressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activityLoginBinding.loginProgress.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activityLoginBinding.loginProgress.setVisibility(View.VISIBLE);
                }
                if (ActivityManager.getInstance().activityLoginBinding.loginLayout.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activityLoginBinding.loginLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat progress
    public void progressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activityLoginBinding.loginProgress.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activityLoginBinding.loginProgress.setVisibility(View.GONE);
                }
                if (ActivityManager.getInstance().activityLoginBinding.loginLayout.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activityLoginBinding.loginLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void start() {
        Request request = new Request.Builder().url("http://192.168.31.19:28146/").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }
    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel !");
            webSocket.send("What's up ?");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ActivityManager.getInstance().makeLongToast(txt);
            }
        });
        ActivityManager.getInstance().activityLoginBinding.tvTest.setText(txt);
    }

    // Websocket
    private final class EchoWebsocketListener2 extends WebSocketListener {
        private static final int NORMAL_STATUS = 1000;

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            webSocket.send("Hello");
            webSocket.close(NORMAL_STATUS, "Bye");
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            ActivityManager.getInstance().makeLongToast("onMessage " + text);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            ActivityManager.getInstance().makeLongToast("onMessage " + bytes.hex());
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
            ActivityManager.getInstance().makeLongToast("onClosed " + reason);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
            ActivityManager.getInstance().makeLongToast("onClosing " + reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            ActivityManager.getInstance().makeLongToast("onFailure " + t.getMessage());
        }
    }
}

