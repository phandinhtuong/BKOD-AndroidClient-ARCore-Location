package Manager;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.LoginActivity;
import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.model.Conversation;
import com.example.administrator.bkod_androidclient.model.Counselor;
import com.example.administrator.bkod_androidclient.model.MemberMessage;
import com.example.administrator.bkod_androidclient.model.News;
import com.example.administrator.bkod_androidclient.model.Tour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Classes.Setting;
import Classes.UserInfo;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class OnlineManager extends Application{
    // Singleton
    private static final OnlineManager ourInstance = new OnlineManager();
    // Singleton getter
    public static OnlineManager getInstance() {return ourInstance;}

    public Socket getmSocket() {
        return mSocket;
    }

    public void setmSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }

    // Websocket
    private Socket mSocket;
    // Thong tin cai dat
    public Setting setting = null;
    // Thong tin user
    public UserInfo userInfo = null;
    // Danh sach tin tuc
    public ArrayList<News> mNewsList = null;
    // Danh sach tour
    public ArrayList<Tour> mTourList = null;
    // Danh sach hoi thoai
    public ArrayList<Conversation> mConversationList = null;
    // Danh sach hoi thoai
    public ArrayList<Counselor> mCounselorsList = null;
    // Duy tri ket noi
    private Handler connectHandler = new Handler();
    // Duong dan file config
    private String configUrl = "http://bkod.herokuapp.com/TuongTest";
    // Constructor
    private OnlineManager() {
        // Load file config dia chi server neu co ket noi mang
        waitInternetConnection ();
    }

    public void waitInternetConnection () {
        if (isNetworkAvailable() == true) {
            // Neu ket noi mang thi tai file config
            new DownloadingTask().execute();
        } else {
            // Neu chua ket noi mang thi doi
            internetHandler.post(internetRunnable);
        }
    }

    // Handler kiem tra ket noi mang
    private Handler internetHandler = new Handler();
    // TODO: Neu ket noi mang duoc thi tai file config
    private Runnable internetRunnable = new Runnable() {
        @Override
        public void run() {
            // Kiem tra xem da ket noi mang chua
            if (isNetworkAvailable() == true){
                // Neu ket noi mang roi thi tai file config
                new DownloadingTask().execute();
            } else {
                // Neu chua ket noi mang thi kiem tra lai sau 1s
                internetHandler.postDelayed(internetRunnable, 1000);
            }
        }
    };
    // Kiem tra ket noi mang
    public boolean isNetworkAvailable() {
        if (ActivityManager.getInstance().getCurrentActivity() == null) {
            return false;
        }
        final ConnectivityManager connectivityManager = ((ConnectivityManager) ActivityManager.getInstance().getCurrentActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    //TODO: Xy ly khi nhan duoc message tu server
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            // Phan tich message ra json neu co
            if (args != null ) {
                JSONObject jsonMessage = JsonManager.toJson((String)args[0]);
                // Kiem tra xem command la gi
                String messageCommand = null;
                try {
                    messageCommand = jsonMessage.getString("COMMAND");
                    if (messageCommand.equals("SETTING")){
                        // Xy ly ket qua cai dat
                        onReceiveSetting(jsonMessage);
                    } else if (messageCommand.equals("LOGIN")){
                        // Xy ly ket qua dang nhap
                        LoginManager.onLoginResult(jsonMessage);
                    } else if (messageCommand.equals("MEMBER_ONLINE")){
                        // Xy ly ket qua member online
                        LoginManager.onMemberOnlineResult(jsonMessage);
                    } else if (messageCommand.equals("LOGOUT")){
                        // Xu ly ket qua dang xuat
                        LogoutManager.onLogoutResult(jsonMessage);
                    } else if (messageCommand.equals("SIGNUP")){
                        // Xu ly ket qua dang ky
                        SignUpManager.onSignUpResult(jsonMessage);
                    } else if (messageCommand.equals("GET_NEWS_LIST")){
                        // Xu ly danh sach tin tuc
                        NewsManager.onNewsResult(jsonMessage);
                    } else if (messageCommand.equals("GET_TOUR_LIST")){
                        // Xu ly danh sach tour
                        HomeManager.onToursResult(jsonMessage);
                    } else if (messageCommand.equals("MEMBER_LOCATION_CHANGE")){
                        // Xu ly co thanh vien vua thay doi toa do
                        LocationManager.onMemberLocationChange(jsonMessage);
                    } else if (messageCommand.equals("CONVERSATIONS_LIST")){
                        // Xu ly khi co danh sach hoi thoai
                        ConversationManager.onRecieveConversationsList(jsonMessage);
                    } else if (messageCommand.equals("COUNSELORS_LIST")){
                        // Xu ly khi nhan duoc danh sach tu van vien
                        ConversationManager.onRecieveCounselorsList(jsonMessage);
                    } else if (messageCommand.equals("MEMBER_MESSAGE")){
                        // Xu ly co tin nhan tu ai do
                        ConversationManager.onRecieveMessageResult(jsonMessage);
                    } else if (messageCommand.equals("RECONNECT")){
                        // Xu ly khi co ket qua ket noi lai tu server
                        onReconnectResult(jsonMessage);
                    } else if (messageCommand.equals("TEST")){
                        // Test
                    }
                } catch (JSONException e) {
                    // Neu convert json bi loi thi in ra loi
                    e.printStackTrace();
                }
            } else {
                // Thong bao loi message tu server
                ActivityManager.getInstance().makeLongToast(getString(R.string.error_server_message));
            }
        }
    };

    // TODO: Ham ket noi socket neu chua ket noi
    public void connectSocket () {
        // Ket noi websocket neu chua ket noi
        if (mSocket.connected() == false) {
            mSocket.connect();
            // Kiem tra ket noi den server
            socketHandler.post(socketRunnable);
        }
    }

    public Handler getSocketHandler() {
        return socketHandler;
    }

    public void onReceiveSetting(JSONObject settingJson) {
        // Lay duong link form
        try {
            String formLink = settingJson.getString("FORM_URL");
            // Khoi tao file cai dat
            setting = new Setting(formLink);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Runnable getSocketRunnable() {
        return socketRunnable;
    }

    public void setSocketRunnable(Runnable socketRunnable) {
        this.socketRunnable = socketRunnable;
    }

    // Handler kiem tra ket noi den server
    private Handler socketHandler = new Handler();
    // TODO: Neu ket noi den server thi tat vong quay o man hinh dang nhap di
    private Runnable socketRunnable = new Runnable() {
        @Override
        public void run() {
            // Kiem tra xem da ket noi den server chua
            Log.v("SOCKET_CHECK", "Start checking");
            if (mSocket.connected() == true){
                if (ActivityManager.getInstance().getCurrentActivity() instanceof LoginActivity){
                    // Tat thong bao dang doi dang nhap
                    ((LoginActivity)ActivityManager.getInstance().getCurrentActivity()).progressOff();
                }
            } else {
                // Neu chua ket noi den server thi check lai sau 1s
                socketHandler.postDelayed(socketRunnable, 1000);
            }
        }
    };
    // TODO: Gui message den server
    public boolean sendMessage(String message) {
        return sendMessage("MESSAGE", message);
    }

    // TODO: Gui message den server
    public boolean sendMessage(String event, String message) {
        if (mSocket.id() == null) {
            // Neu mat ket noi socket thi hien thi loi
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_server_lost_connection));
            return false;
        } else {
            // Neu ket noi on dinh thi gui message len server
            mSocket.emit(event, message);
            return true;
        }
    }

    // TODO: Xu ly khi co ket qua ket noi lai tu server
    public void onReconnectResult (JSONObject reconnectResult) {
        try {
            if (reconnectResult.getString("RESULT").equals("success")) {
                // Neu ket noi lai thanh cong
            } else if (reconnectResult.getString("RESULT").equals("fail")) {
                // Neu ket noi lai that bai thi gui lai yeu cau ket noi
                if (mSocket.connected() == true){
                    if (userInfo != null) {
                        String reconnectMessage = "{\"COMMAND\":\"RECONNECT\", \"USER_ID\":" + userInfo.getUserId() + "}";
                        // Gui message len server
                        OnlineManager.getInstance().sendMessage(reconnectMessage);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // TODO: Gui message noi dung showNewsList den server
    public void test() {
        sendMessage("Test", "Hello!");
    }

    // TODO: Ma hoa mat khau
    public static String sha256(String data){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {
        // Server config
        String str;
        @Override
        protected void onPostExecute(Void result) {
            // Sau khi download xong thi khoi tao websocket
            // Convert sang json
            JSONObject serverConfig = JsonManager.toJson(str);
            try {
                // Lay ip va port
                String ip = serverConfig.getString("IP");
                String port = serverConfig.getString("PORT");
                // Khoi tao socket
                IO.Options opts = new IO.Options();
                opts.reconnection = true;
                mSocket = IO.socket(String.format("%s:%s", ip, port), opts);
                // Set thoi gian ket noi lai
                mSocket.io().reconnection(true).timeout(60000).reconnectionDelay(30000);
                // Xu ly khi nhan message tu server
                mSocket.on("MESSAGE", onNewMessage);
                // Khi socket ket noi lai thi gui message ket noi lai len server
                mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        // Neu da dang nhap roi thi gui message ket noi lai len sever
                        if (userInfo != null) {
                            String reconnectMessage = "{\"COMMAND\":\"RECONNECT\", \"USER_ID\":" + userInfo.getUserId() + "}";
                            // Gui message len server
                            OnlineManager.getInstance().sendMessage(reconnectMessage);
                        }
                    }
                });
                // Duy tri ket noi socket
                connectSocket();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                // Khoi tao duong dan url
                URL url = new URL(configUrl);
                // Mo ket noi url
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                // Thiet lap phuong thuc get
                c.setRequestMethod("GET");
                // Ket noi den url
                c.connect();
                // Neu loi ket noi thi hien thi thong bao loi
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    ActivityManager.getInstance().makeLongToast(
                            String.format("Lỗi lấy file cấu hình: %s. %s", c.getResponseCode(), c.getResponseMessage()));
                }
                // Lay input stream
                InputStream is = c.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }
                // Lay string config server
                str = stringBuilder.toString();
                // Dong ket noi input stream
                is.close();
            } catch (Exception e) {
                // Neu co loi thi in ra loi
                e.printStackTrace();
            }
            return null;
        }
    }
}
