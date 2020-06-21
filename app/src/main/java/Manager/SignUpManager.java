package Manager;

import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpManager {
    // TODO: Xu ly khi nhan ket qua dang nhap tu server
    public static void onSignUpResult (JSONObject signUpResult) {
        try {
            if (signUpResult.getString("RESULT").equals("fail")) {
                // Neu dang ky bi loi
                if (signUpResult.getString("REASON").equals("UNKNOWN_ERROR")){
                    // Neu bi loi khong xac dinh
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_unknown));
                } else if (signUpResult.getString("REASON").equals("ERROR_EMAIL_EXIST")) {
                    // Neu bi loi trung email
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_email_exist));
                }
            } else if (signUpResult.getString("RESULT").equals("success")) {
                // Neu dang ky thanh cong thi thong bao
                // Ten dang nhap (email)
                String userName = signUpResult.getString("USERNAME");
                // Ten day du
                String fullName = signUpResult.getString("FULLNAME");
                // In ra man hinh thong bao dang nhap thanh cong
                ActivityManager.getInstance().makeLongToast(fullName + " đăng ký thành công với Username: " + userName + "!");
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
        // Tat thong bao dang dang ky
        if (ActivityManager.getInstance().getCurrentActivity() instanceof SignUpActivity){
            ((SignUpActivity)ActivityManager.getInstance().getCurrentActivity()).progressOff();
        }
    }
}
