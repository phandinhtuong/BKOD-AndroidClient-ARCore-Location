package Manager;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.model.ClassroomManager;
import com.example.administrator.bkod_androidclient.model.TourTimesheet;
import com.example.administrator.bkod_androidclient.model.Tour;
import com.example.administrator.bkod_androidclient.model.TourMember;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Classes.UserInfo;

public class HomeManager {
    // TODO: Xu ly khi nhan danh sach tour tu server
    public static void onToursResult (JSONObject toursResult) {
        // Reset danh sach tour hien tai
        OnlineManager.getInstance().mTourList = new ArrayList<>();
        try {
            JSONArray toursList = toursResult.getJSONArray("TOURS");
            for (int i = 0; i < toursList.length(); i++){
                // Lay danh sach thanh vien
                ArrayList<TourMember> tourMembers = getMemberList(toursList.getJSONObject(i).getJSONArray("MEMBERS"));
                // Khoi tao danh sach chang
                ArrayList<TourTimesheet> tourTimesheets = getTimesheetList(toursList.getJSONObject(i).getJSONArray("TIMESHEETS"));
                // Ngay dien ra tour
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                // Convert string sang date
                date = format.parse(toursList.getJSONObject(i).getString("DATE"));
                // Gan tung tour
                Tour mTour = new Tour(toursList.getJSONObject(i).getInt("TOUR_ID"),
                    toursList.getJSONObject(i).getString("NAME"),
                    toursList.getJSONObject(i).getString("IMAGE_URL"),
                    toursList.getJSONObject(i).getInt("FUNCTION"),
                    date,
                    toursList.getJSONObject(i).getString("MAP_IMAGE_URL"),
                    tourMembers, tourTimesheets);
                OnlineManager.getInstance().mTourList.add(mTour);
            }
            // In ra man hinh thong bao co bao nhieu tour
            ActivityManager.getInstance().makeLongToast("Có " + toursList.length() + " tour");
            if (ActivityManager.getInstance().getCurrentActivity() instanceof HomeTabsActivity){
                if (((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).getTabHomeTourBinding() != null) {
                    // Hien thi danh sach tour
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setTourDataListView();
                    // Tat thong bao dang doi danh sach tour
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).tourProgressOff();
                }
            }
        } catch (ParseException | JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }

    // TODO: Lay danh sach thanh vien trong 1 tour
    public static ArrayList<TourMember> getMemberList (JSONArray memberList) {
        // Khoi tao danh sach thanh vien
        ArrayList<TourMember> tourMembers = new ArrayList<>();
        try{
            for (int i = 0; i < memberList.length(); i++){
                // Lay member hien tai
                JSONObject tourMember = memberList.getJSONObject(i);
                // Vi tri thanh vien
                Object x = tourMember.get("LATITUDE");
                Object y = tourMember.get("LONGITUDE");
                LatLng location = null;
                // Toa do
                if (!x.equals(null) && !y.equals(null)){
                    // Neu co toa do thi gan toa do
                    location = new LatLng((double)x, (double)y);
                }
                // Ngay sinh thanh vien
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date birthday = null;
                // Convert string sang date
                birthday = format.parse(tourMember.getString("BIRTHDAY"));
                // Khoi tao user info
                UserInfo memberInfo = new UserInfo(tourMember.getInt("USER_ID"),
                        tourMember.getString("USERNAME"),
                        tourMember.getString("FULLNAME"),
                        birthday,
                        tourMember.getInt("GENDER"),
                        tourMember.getString("SCHOOL"),
                        tourMember.getString("CLASS"),
                        tourMember.getString("PHONE_NUMBER"),
                        tourMember.getBoolean("IS_COUNSELOR"),
                        tourMember.getInt("STATE"));
                // Duyet tung thanh vien
                TourMember member = new TourMember(
                        memberInfo,
                        tourMember.getInt("FUNCTION"),
                        location,
                        tourMember.getString("NOTE"));
                tourMembers.add(member);
            }
        } catch (ParseException | JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
        return tourMembers;
    }

    // TODO: Lay danh sach chang trong 1 tour
    public static ArrayList<TourTimesheet> getTimesheetList (JSONArray timesheetList) {
        // Khoi tao danh sach chang
        ArrayList<TourTimesheet> tourTimesheets = new ArrayList<>();
        try{
            for (int i = 0; i < timesheetList.length(); i++){
                // Lay danh sach nguoi quan ly
                ArrayList<ClassroomManager> classroomManagers = getManagerList(timesheetList.getJSONObject(i).getJSONArray("MANAGERS"));
                // Vi tri toa nha
                Object x = timesheetList.getJSONObject(i).get("LATITUDE");
                Object y = timesheetList.getJSONObject(i).get("LONGITUDE");
                LatLng location = null;
                // Toa do
                if (!x.equals(null) && !y.equals(null)){
                    // Neu co toa do thi gan toa do
                    location = new LatLng((double)x, (double)y);
                }
                // Duyet tung chang
                TourTimesheet timesheet = new TourTimesheet(
                        timesheetList.getJSONObject(i).getInt("TIMESHEET_ID"),
                        Time.valueOf(timesheetList.getJSONObject(i).getString("START_TIME")),
                        Time.valueOf(timesheetList.getJSONObject(i).getString("END_TIME")),
                        timesheetList.getJSONObject(i).getInt("CLASSROOMS_ID"),
                        timesheetList.getJSONObject(i).getInt("CLASSROOM_FLOOR"),
                        timesheetList.getJSONObject(i).getString("CLASSROOMS_NAME"),
                        timesheetList.getJSONObject(i).getString("CLASSROOM_SUB_NAME"),
                        timesheetList.getJSONObject(i).getString("CLASSROOMS_NOTE"),
                        timesheetList.getJSONObject(i).getString("BUILDING_NAME"),
                        timesheetList.getJSONObject(i).getString("BUILDING_SUB_NAME"),
                        location,
                        timesheetList.getJSONObject(i).getString("BUILDING_NOTE"),
                        classroomManagers);
                tourTimesheets.add(timesheet);
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
        return tourTimesheets;
    }

    // TODO: Lay danh sach nguoi quan ly trong 1 lop
    public static ArrayList<ClassroomManager> getManagerList (JSONArray managerList) {
        // Khoi tao danh sach nguoi quan ly
        ArrayList<ClassroomManager> classroomManagers = new ArrayList<>();
        try{
            for (int i = 0; i < managerList.length(); i++){
                // Ngay sinh nguoi quan ly
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date birthday = null;
                try {
                    // Convert string sang date
                    birthday = format.parse(managerList.getJSONObject(i).getString("BIRTHDAY"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Duyet tung nguoi quan ly
                ClassroomManager manager = new ClassroomManager(
                        managerList.getJSONObject(i).getInt("MANAGER_ID"),
                        managerList.getJSONObject(i).getString("NAME"),
                        managerList.getJSONObject(i).getInt("GENDER"),
                        birthday,
                        managerList.getJSONObject(i).getString("EMAIL"),
                        managerList.getJSONObject(i).getString("PHONE_NUMBER"));
                classroomManagers.add(manager);
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
        return classroomManagers;
    }
}