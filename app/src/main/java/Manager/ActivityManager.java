package Manager;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import com.example.administrator.bkod_androidclient.databinding.ActivityHomeTabsBinding;
import com.example.administrator.bkod_androidclient.databinding.ActivityLoginBinding;
import com.example.administrator.bkod_androidclient.databinding.ActivityMessageBinding;
import com.example.administrator.bkod_androidclient.databinding.ActivitySignUpBinding;
import com.example.administrator.bkod_androidclient.databinding.ActivityTimesheetInfoBinding;
import com.example.administrator.bkod_androidclient.databinding.ActivityTourTabsBinding;

import java.util.regex.Pattern;

public class ActivityManager extends Application {
    // Singleton
    private static final ActivityManager ourInstance = new ActivityManager();
    // Lop rang buoc login activity voi binding la ActivityLoginBinding
    public ActivityLoginBinding activityLoginBinding;
    // Lop rang buoc sign up activity voi binding la ActivitySignUpBinding
    public ActivitySignUpBinding activitySignUpBinding;
    // Lop rang buoc home tabs activity voi binding la ActivityHomeTabsBinding
    public ActivityHomeTabsBinding activityHomeTabsBinding;
    // Lop rang buoc tour tabs activity voi binding la ActivityTourTabsBinding
    public ActivityTourTabsBinding activityTourTabsBinding;
    // Lop rang buoc timesheet info activity voi binding la ActivityTimesheetInfoBinding
    public ActivityTimesheetInfoBinding activityTimesheetInfoBinding;
    // Lop rang buoc message activity voi binding la ActivityMessageBinding
    public ActivityMessageBinding activityMessageBinding;
    // Activity hien tai
    private Activity mCurrentActivity = null;
    // Singleton getter
    public static ActivityManager getInstance() {
        return ourInstance;
    }
    // Activity hien tai setter va getter
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    // Hien thi thong bao tren man hinh
    public void makeShortToast(final String text) {
        mCurrentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getCurrentActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void makeLongToast(final String text) {
        mCurrentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getCurrentActivity(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO: Kiem tra xem email co hop le khong
    public static boolean isEmailValid(String email) {
        return Pattern.matches( "^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_\\.]+$", email);
    }

    //TODO: Kiem tra xem mat khau co dai hon 6 khong
    public static boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    // TODO: Kiem tra xem ho ten day du co hop le khong
    public static boolean isFullnameValid(String fullname) {
        return Pattern.matches( "^[a-zA-Z0-9_]+ [a-zA-Z0-9_ ]+$", fullname);
    }

    // TODO: Kiem tra xem ngay sinh co hop le khong
    public static boolean isBirthdayValid(String birthday) {
        return Pattern.matches( "^[0-9]+/[0-9]+/[0-9]+$", birthday);
    }
}
