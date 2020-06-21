package Manager;

import com.example.administrator.bkod_androidclient.TourTabsActivity;
import com.example.administrator.bkod_androidclient.model.Tour;
import com.example.administrator.bkod_androidclient.model.TourMember;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationManager {
    // TODO: Xu ly khi nhan duoc toa do cua 1 member vua thay doi
    public static void onMemberLocationChange (JSONObject locationResult) {
        try {
            int userid = locationResult.getInt("USER_ID");
            if (userid != OnlineManager.getInstance().userInfo.getUserId()) {
                // Neu la toa do thanh vien khac thi moi cap nhat
                double latitude = locationResult.getDouble("LATITUDE");
                double longitude = locationResult.getDouble("LONGITUDE");
                for (Tour tour : OnlineManager.getInstance().mTourList) {
                    for (TourMember member : tour.getmTourMember()) {
                        if (member.getUserInfo().getUserId() == userid) {
                            // Cap nhat vi tri cua thanh vien
                            member.setmLocation(new LatLng(latitude, longitude));
                        }
                    }
                }
                // Neu dang o trong man hinh thong tin tour thi refresh danh sach thanh vien
                if (ActivityManager.getInstance().getCurrentActivity() instanceof TourTabsActivity) {
                    ((TourTabsActivity)(ActivityManager.getInstance().getCurrentActivity())).refreshMemberList();
                }
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }
}
