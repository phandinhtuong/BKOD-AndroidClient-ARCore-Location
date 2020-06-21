package Manager;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutManager {
    // TODO: Xu ly khi nhan ket qua dang xuat tu server
    public static void onLogoutResult (JSONObject logoutResult) {
        try {
            if (logoutResult.getString("RESULT").equals("fail")) {
                // Neu dang xuat bi loi
                if (logoutResult.getString("REASON").equals("UNKNOWN_ERROR")){
                    // Neu bi loi khong xac dinh
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_unknown));
                } else if (logoutResult.getString("REASON").equals("DATABASE_ERROR")) {
                    // Neu bi loi csdl
                    ActivityManager.getInstance().makeLongToast(
                            ActivityManager.getInstance().getCurrentActivity().getString(
                                    R.string.error_database));
                }
            } else if (logoutResult.getString("RESULT").equals("success")) {
                // Xoa user info
                OnlineManager.getInstance().userInfo = null;
                // Xoa tour list
                OnlineManager.getInstance().mTourList = null;
                // Neu dang xuat thanh cong thi thong bao
                if (ActivityManager.getInstance().getCurrentActivity() instanceof HomeTabsActivity){
                    // Chuyen sang man hinh login
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).logout();
                }
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }
}
