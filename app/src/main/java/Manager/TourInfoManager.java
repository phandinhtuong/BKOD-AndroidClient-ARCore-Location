package Manager;

import com.example.administrator.bkod_androidclient.model.TourTimesheet;
import com.example.administrator.bkod_androidclient.model.TourMember;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.Comparator;

public class TourInfoManager {
    // TODO: Sap xep danh sach thanh vien
    public static void sortTour (int tourIndex) {
        // Sap xep danh sach thanh vien
        Collections.sort(OnlineManager.getInstance().mTourList.get(tourIndex).getmTourMember(), new Comparator<TourMember>() {
            public int compare(TourMember o1, TourMember o2) {
                if (o1.getmFunction() == 1){
                    return -1;
                } else if (o2.getmFunction() == 1){
                    return 1;
                } else if (o1.getmFunction() == 2){
                    return -1;
                } else if (o2.getmFunction() == 2){
                    return 1;
                } else if (o1.getmFunction() == 0){
                    return -1;
                } else if (o2.getmFunction() == 0){
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        // Sap xep danh sach chang
        Collections.sort(OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet(), new Comparator<TourTimesheet>() {
            public int compare(TourTimesheet o1, TourTimesheet o2) {
                return o1.getmStartTime().compareTo(o2.getmStartTime());
            }
        });
        // Kiem tra xem co 2 chang nao co vi tri trung nhau khong
        for (int i = 0; i < OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().size() - 1; i++) {
            for (int j = i + 1; j < OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().size(); j++) {
                int fLat1 = (int) (OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(i).getmBuildingLocation().latitude * 1000000.0f);
                int fLong1 = (int) (OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(i).getmBuildingLocation().longitude * 1000000.0f);
                int fLat2 = (int) (OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(j).getmBuildingLocation().latitude * 1000000.0f);
                int fLong2 = (int) (OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(j).getmBuildingLocation().longitude * 1000000.0f);
                // Neu trung dia diem thi tang khoang cach 1 chut
                if (fLat1 == fLat2 && fLong1 == fLong2){
                    OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(j).setmBuildingLocation(new LatLng(OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(j).getmBuildingLocation().latitude + 0.00005f, OnlineManager.getInstance().mTourList.get(tourIndex).getmTourTimesheet().get(j).getmBuildingLocation().longitude + 0.00005f));
                }
            }
        }
    }
}
