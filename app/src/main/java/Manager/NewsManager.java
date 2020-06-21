package Manager;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsManager {
    // TODO: Xu ly khi nhan danh sach tin tuc tu server
    public static void onNewsResult (JSONObject newsResult) {
        // Reset danh sach tin tuc hien tai
        OnlineManager.getInstance().mNewsList = new ArrayList<>();
        try {
            JSONArray newsList = newsResult.getJSONArray("NEWS");
            for (int i = 0; i < newsList.length(); i++){
                // Gan tung tin tuc
                News mNew = new News(newsList.getJSONObject(i).getString("URL"),
                        newsList.getJSONObject(i).getString("TITLE"),
                        newsList.getJSONObject(i).getString("SUMMARY"),
                        newsList.getJSONObject(i).getString("IMAGE_URL"));
                OnlineManager.getInstance().mNewsList.add(mNew);
            }
            // In ra man hinh thong bao co bao nhieu tin tuc
            ActivityManager.getInstance().makeLongToast("Có " + newsList.length() + " tin tức");
            if (ActivityManager.getInstance().getCurrentActivity() instanceof HomeTabsActivity){
                if (((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).getTabHomeNewsBinding() != null) {
                    // Hien thi danh sach tin tuc
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setNewsDataListView();
                    // Tat thong bao dang doi tin tuc
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).newsProgressOff();
                }
            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }
}