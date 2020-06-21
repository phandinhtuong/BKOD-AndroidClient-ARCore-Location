package com.example.administrator.bkod_androidclient;

import android.app.DatePickerDialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import Manager.ActivityManager;
import Manager.OnlineManager;

public class SignUpActivity extends BaseActivity {
    // Bien luu ngay sinh
    private String sBirthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lien ket layout voi activity
        ActivityManager.getInstance().activitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        // Sua tieu de
        ActivityManager.getInstance().activitySignUpBinding.actionBar.actionBarTitle.setText(R.string.title_sign_up);
//        // Gan su kien khi nhan vao nut info
//        ActivityManager.getInstance().activitySignUpBinding.actionBar.actionBarInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Neu bam nut info thi chuyen sang scene thong tin
//                Intent intent = new Intent(SignUpActivity.this, NewsActivity.class);
//                SignUpActivity.this.startActivity(intent);
//            }
//        });
//        // Gan su kien khi nhan vao nut menu
//        ActivityManager.getInstance().activitySignUpBinding.actionBar.actionBarMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Neu bam nut menu thi hien thi menu
//                PopupMenu popupMenu = new PopupMenu(SignUpActivity.this,
//                        ActivityManager.getInstance().activitySignUpBinding.actionBar.actionBarMenu);
//                // Gan menu
//                popupMenu.getMenuInflater().inflate(R.menu.common_menu, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        // Goi su kien khi nhan vao 1 nut trong menu
//                        return true;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
        // Gan su kien khi nhan vao khung ngay sinh
        ActivityManager.getInstance().activitySignUpBinding.signUpBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Neu bam vao khung nhap thi hien thi date picker
                showDatePickerDialog();
            }
        });
        // Khoi tao ngay sinh
        sBirthday = String.format("%d-%02d-%02d", 2004, 1, 1);
        // Khi nhan nut tiep sau khi nhap ho ten thi tien hanh chon ngay sinh
        ActivityManager.getInstance().activitySignUpBinding.signUpFullname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NEXT){
                    // Goi chon ngay sinh
                    ActivityManager.getInstance().activitySignUpBinding.signUpBirthday.performClick();
                }
                return false;
            }
        });
        // Khoi tao danh sach gioi tinh
        String[] items = new String[]{
                getString(R.string.gender_male),
                getString(R.string.gender_female),
                getString(R.string.gender_other),
                getString(R.string.gender_undisclosed)};
        // Lien ket khung va danh sach
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        ActivityManager.getInstance().activitySignUpBinding.signUpGender.setAdapter(adapter);
        // Sau khi chon xong gioi tinh thi tien hanh nhap truong
        ActivityManager.getInstance().activitySignUpBinding.signUpGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // Chuyen sang khung nhap truong
                if (ActivityManager.getInstance().activitySignUpBinding.signUpEmailInput.getText().toString().equals("") == false) {
                    ActivityManager.getInstance().activitySignUpBinding.signUpSchool.requestFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        // Them su kien khi nhan nut dang ky
        ActivityManager.getInstance().activitySignUpBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Neu bam nut dang ky thi tien hanh dang ky
                signUp();
            }
        });
        // Khi nhan nut go sau khi nhap lop thi tien hanh dang ky
        ActivityManager.getInstance().activitySignUpBinding.signUpClassroom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO){
                    // Goi nut dang nhap
                    ActivityManager.getInstance().activitySignUpBinding.signupButton.performClick();
                }
                return false;
            }
        });
        // Focus vao khung nhap email
        ActivityManager.getInstance().activitySignUpBinding.signUpEmailInput.requestFocus();
        // Tat progress
        progressOff ();
    }

    // TODO: Hien thi hop thoai chon ngay sinh
    public void showDatePickerDialog() {
        // Callback khi chon ngay sinh
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Cap nhat ngay sinh moi lan chon
                ActivityManager.getInstance().activitySignUpBinding.signUpBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                // Gan bien ngay sinh de gui message len server
                sBirthday = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                // Khi chon xong ngay sinh thi tien hanh chon gioi tinh
                ActivityManager.getInstance().activitySignUpBinding.signUpGender.performClick();
            }
        };
        // Ngay sinh mac dinh
        int ngay=Integer.parseInt("1");
        int thang=Integer.parseInt("1");
        int nam=Integer.parseInt("2004");
        // Khoi tao hop thoai chon ngay sinh
        DatePickerDialog pic = new DatePickerDialog(this, callback, nam, thang - 1, ngay);
        // Tieu de hop thoai
        pic.setTitle(getString(R.string.birthday));
        // Hien thi hop thoai
        pic.show();
    }

    // TODO: Kiem tra thong tin dang ky va soan message dang ky gui len server
    private void signUp() {
        // Kiem tra email hop le
        if (ActivityManager.isEmailValid(ActivityManager.getInstance().activitySignUpBinding.signUpEmailInput.getText().toString()) == false){
            // In ra man hinh thong bao email khong hop le
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_invalid_email));
        } else if (ActivityManager.isPasswordValid(ActivityManager.getInstance().activitySignUpBinding.signUpPasswordInput.getText().toString()) == false){
            // Kiem tra xem mat khau co hop le khong
            // In ra man hinh thong bao mat khau khong hop le
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_invalid_password));
        } else if (ActivityManager.getInstance().activitySignUpBinding.signUpPasswordInput.getText().toString().equals(
                ActivityManager.getInstance().activitySignUpBinding.signUpRetypePassword.getText().toString()) == false){
            // Kiem tra xem mat khau co khop khong
            // In ra man hinh thong bao mat khau khong khop
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_password_not_match));
        } else if (ActivityManager.isFullnameValid(ActivityManager.getInstance().activitySignUpBinding.signUpFullname.getText().toString()) == false){
            // Kiem tra xem ho ten day du co hop le khong
            // In ra man hinh thong bao ho ten day du khong hop le
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_invalid_fullname));
        } else if (ActivityManager.isBirthdayValid(ActivityManager.getInstance().activitySignUpBinding.signUpBirthday.getText().toString()) == false){
            // Kiem tra xem ngay sinh co hop le khong
            // In ra man hinh thong bao ngay sinh khong hop le
            ActivityManager.getInstance().makeLongToast(getString(R.string.error_invalid_birthday));
        } else {
            // Neu tat ca cac thong tin deu hop le thi gui message dang ky len server
            // Ma hoa mat khau
            String password = OnlineManager.sha256(ActivityManager.getInstance().activitySignUpBinding.signUpPasswordInput.getText().toString() + ActivityManager.getInstance().activitySignUpBinding.signUpEmailInput.getText().toString() + "BKODv1Habvietio");
            // Khoi tao gioi tinh
            // Lien ket khung chon goi tinh
            int iGender = (ActivityManager.getInstance().activitySignUpBinding.signUpGender.getSelectedItemPosition() + 1) % 4;
            // Tao message dang ky
            String signUpMessage =
                    "{\"COMMAND\":\"SIGNUP\"," +
                    "\"USERNAME\":\"" + ActivityManager.getInstance().activitySignUpBinding.signUpEmailInput.getText().toString() + "\"," +
                    "\"PASSWORD\":\"" + password + "\"," +
                    "\"FULLNAME\":\"" + ActivityManager.getInstance().activitySignUpBinding.signUpFullname.getText().toString() + "\"," +
                    "\"BIRTHDAY\":\"" + sBirthday + "\"," +
                    "\"GENDER\":" + iGender + "," +
                    "\"SCHOOL\":\"" + ActivityManager.getInstance().activitySignUpBinding.signUpSchool.getText().toString() + "\"," +
                    "\"CLASSROOM\":\"" + ActivityManager.getInstance().activitySignUpBinding.signUpClassroom.getText().toString() + "\"}";
            // Gui message len server
            if (OnlineManager.getInstance().sendMessage(signUpMessage) == true) {
                // Hien thi thong bao dang dang ky
                progressOn();
            }
        }
    }

    //TODO: Bat progress
    public void progressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activitySignUpBinding.signUpProgress.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activitySignUpBinding.signUpProgress.setVisibility(View.VISIBLE);
                }
                if (ActivityManager.getInstance().activitySignUpBinding.signUpForm.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activitySignUpBinding.signUpForm.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat progress
    public void progressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ActivityManager.getInstance().activitySignUpBinding.signUpProgress.getVisibility() != View.GONE) {
                    ActivityManager.getInstance().activitySignUpBinding.signUpProgress.setVisibility(View.GONE);
                }
                if (ActivityManager.getInstance().activitySignUpBinding.signUpForm.getVisibility() != View.VISIBLE) {
                    ActivityManager.getInstance().activitySignUpBinding.signUpForm.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
