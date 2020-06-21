package Manager;

import com.example.administrator.bkod_androidclient.LoginActivity;
import com.example.administrator.bkod_androidclient.R;
import com.example.administrator.bkod_androidclient.TourTabsActivity;
import com.example.administrator.bkod_androidclient.adapter.TourMemberAdapter;
import com.example.administrator.bkod_androidclient.model.Tour;
import com.example.administrator.bkod_androidclient.model.TourMember;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Classes.UserInfo;

public class LoginManager {
    // TODO: Xu ly khi nhan ket qua dang nhap tu server
    public static void onLoginResult (JSONObject loginResult) {
        try {
            if (loginResult.getString("RESULT").equals("fail")) {
                // Neu dang nhap bi loi
                if (loginResult.getString("REASON").equals("UNKNOWN_ERROR")){
                    // Neu bi loi khong xac dinh
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_unknown));
                } else if (loginResult.getString("REASON").equals("WRONG_EMAIL_PASSWORD")) {
                    // Neu bi loi sai email/mat khau
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_valid_email_password));
                } else if (loginResult.getString("REASON").equals("CONFLICT_ERROR")) {
                    // Neu tai khoan dang duoc dang nhap boi mot nguoi khac
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_conflict_account));
                }
                // Tat thong bao dang dang nhap
                if (ActivityManager.getInstance().getCurrentActivity() instanceof LoginActivity){
                    ((LoginActivity) ActivityManager.getInstance().getCurrentActivity()).progressOff();
                }
            } else if (loginResult.getString("RESULT").equals("success")) {
                // Neu dang nhap thanh cong thi khoi tao user info
                // User id
                int userId = loginResult.getInt("USER_ID");
                // Ten dang nhap (email)
                String userName = loginResult.getString("USERNAME");
                // Ten day du
                String fullName = loginResult.getString("FULLNAME");
                // Ngay sinh
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date birthday = null;
                try {
                    // Convert string sang date
                    birthday = format.parse(loginResult.getString("BIRTHDAY"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Gioi tinh
                int gender = loginResult.getInt("GENDER");
                // Truong trung hoc
                String school = loginResult.getString("SCHOOL");
                // Lop dang hoc
                String classroom = loginResult.getString("CLASS");
                // Lay so dien thoai
                String phoneNumber = loginResult.getString("PHONE_NUMBER");
                // Danh dau co phai tu van vien khong
                boolean isCounselor = loginResult.getBoolean("IS_COUNSELOR");
                // Trang thai dang nhap (0: Offline, 1: Online, 2: Busy, 3: Hidden)
                int state = loginResult.getInt("STATE");
                OnlineManager.getInstance().userInfo = new UserInfo(userId, userName, fullName,
                        birthday, gender, school, classroom, phoneNumber, isCounselor,state);
                // Chuyen sang man hinh home
                if (ActivityManager.getInstance().getCurrentActivity() instanceof LoginActivity){
                    ((LoginActivity) ActivityManager.getInstance().getCurrentActivity()).loginSuccess();
                }
                // In ra man hinh thong bao dang nhap thanh cong
                ActivityManager.getInstance().makeLongToast(fullName + " đăng nhập thành công!");
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }

    // TODO: Xu ly khi nhan ket qua member online tu server
    public static void onMemberOnlineResult (JSONObject memberOnlineResult) {
        try {
            // Tach lay member id
            int memberId = memberOnlineResult.getInt("USER_ID");
            // Kiem tra xem co dang mo activity tour khong
            boolean isOpeningTour = ActivityManager.getInstance().getCurrentActivity() instanceof TourTabsActivity;
            if (OnlineManager.getInstance().mTourList != null) {
                // Neu da lay duoc danh sach tour thi kiem tra danh sach thanh vien
                for (Tour tour : OnlineManager.getInstance().mTourList) {
                    if (tour.getmTourMember() != null) {
                        for (TourMember tourMember : tour.getmTourMember()) {
                            if (tourMember.getUserInfo().getUserId() == memberId) {
                                // Sua trang thai cua member thanh online
                                tourMember.getUserInfo().setState(1);
                            }
                        }
                    }
                }
                // Update trang thai online cua member
                if (isOpeningTour == true) {
                    ((TourTabsActivity)ActivityManager.getInstance().getCurrentActivity()).refreshMemberList();
                }
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }
}
