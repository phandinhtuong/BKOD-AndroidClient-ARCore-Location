package com.example.administrator.bkod_androidclient;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.administrator.bkod_androidclient.adapter.ConversationsAdapter;
import com.example.administrator.bkod_androidclient.adapter.CounselorsAdapter;
import com.example.administrator.bkod_androidclient.adapter.NewsAdapter;
import com.example.administrator.bkod_androidclient.adapter.TourAdapter;
import com.example.administrator.bkod_androidclient.databinding.TabHomeConversationsBinding;
import com.example.administrator.bkod_androidclient.databinding.TabHomeNewsBinding;
import com.example.administrator.bkod_androidclient.databinding.TabHomeContactBinding;
import com.example.administrator.bkod_androidclient.databinding.TabHomeTourBinding;
import com.example.administrator.bkod_androidclient.databinding.TabHomeUserBinding;
import com.example.administrator.bkod_androidclient.model.Counselor;
import com.example.administrator.bkod_androidclient.model.HomeConversationsTab;
import com.example.administrator.bkod_androidclient.model.HomeNewsTab;
import com.example.administrator.bkod_androidclient.model.HomeContactTab;
import com.example.administrator.bkod_androidclient.model.HomeTourTab;
import com.example.administrator.bkod_androidclient.model.HomeUserTab;

import java.util.ArrayList;
import java.util.List;

import Manager.ActivityManager;
import Manager.ConversationManager;
import Manager.OnlineManager;

public class HomeTabsActivity extends BaseActivity {
    // Lop rang buoc tab tour voi binding la TabHomeTourBinding
    private TabHomeTourBinding tabHomeTourBinding;
    // Tour adapter
    private TourAdapter tourAdapter;
    // Lop rang buoc tab news binding la TabHomeNewsBinding
    private TabHomeNewsBinding tabHomeNewsBinding;
    // News adapter
    private NewsAdapter newsAdapter;
    // Lop rang buoc tab conversations voi binding la TabHomeConversationsBinding
    private TabHomeConversationsBinding tabHomeConversationsBinding;
    // Conversations adapter
    private ConversationsAdapter conversationsAdapter;
    // Counselors adapter
    private CounselorsAdapter counselorsAdapter;
    // Lop rang buoc tab user voi binding la TabHomeUserBinding
    private TabHomeUserBinding tabHomeUserBinding;
    // Lop rang buoc tab contact voi binding la TabHomeContactBinding
    private TabHomeContactBinding tabHomeContactBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lien ket layout voi activity
        ActivityManager.getInstance().activityHomeTabsBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_tabs);
        // Gan tour adapter
        tourAdapter = new TourAdapter(this);
        // Gan news adapter
        newsAdapter = new NewsAdapter(this);
        // Gan conversations adapter
        conversationsAdapter = new ConversationsAdapter(this);
        // Gan counselors adapter
        counselorsAdapter = new CounselorsAdapter(this);
        // Lay danh sach hoi thoai
        OnlineManager.getInstance().sendMessage("{\"COMMAND\":\"GET_CONVERSATIONS_LIST\"}");
        // Lay danh sach tu van vien
        OnlineManager.getInstance().sendMessage("{\"COMMAND\":\"GET_COUNSELORS_LIST\"}");
        // Them cac tab vao man hinhp
        addControl();
    }

    private void addControl() {
        // Gan adapter va danh sach tab
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        // Them noi dung cac tab
        adapter.AddFragment(new HomeTourTab());
        adapter.AddFragment(new HomeNewsTab());
        adapter.AddFragment(new HomeUserTab());
        adapter.AddFragment(new HomeConversationsTab());
        adapter.AddFragment(new HomeContactTab());
        // Set adapter danh sach tab
        ViewPager viewPager = ActivityManager.getInstance().activityHomeTabsBinding.viewpagerId;
        TabLayout tabLayout = ActivityManager.getInstance().activityHomeTabsBinding.tablayoutId;
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
//                ActivityManager.getInstance().makeLongToast("onPageScrollStateChanged " + state);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                ActivityManager.getInstance().makeLongToast("onPageScrolled " + position + ", positionOffset " + positionOffset + ", positionOffsetPixels " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                ActivityManager.getInstance().makeLongToast("onPageSelected " + position);
            }
        });
//        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        // Them vach ngan cach cac tab
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorPrimaryDark));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
        // Icon cac tab
        tabLayout.getTabAt(0).setIcon(R.drawable.tour_select);
        tabLayout.getTabAt(1).setIcon(R.drawable.tin_tuc);
        tabLayout.getTabAt(2).setIcon(R.drawable.personal_info);
        tabLayout.getTabAt(3).setIcon(R.mipmap.tour_conversation_icon);
        tabLayout.getTabAt(4).setIcon(R.mipmap.tour_contact_icon);
        // Doi mau tab icon khi click
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                    switch (tab.getPosition()){
                        case 0:
                            tab.setIcon(R.drawable.tour_select);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.tin_tuc_select);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.personal_info_select);
                            break;
                        case 3:
                            tab.setIcon(R.mipmap.tour_conversation_icon_select);
                            break;
                        case 4:
                            tab.setIcon(R.mipmap.tour_contact_icon_select);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    super.onTabUnselected(tab);
                    switch (tab.getPosition()){
                        case 0:
                            tab.setIcon(R.drawable.tour);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.tin_tuc);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.personal_info);
                            break;
                        case 3:
                            tab.setIcon(R.mipmap.tour_conversation_icon);
                            break;
                        case 4:
                            tab.setIcon(R.mipmap.tour_contact_icon);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    super.onTabReselected(tab);
                }
            }
        );
    }

    // Class quan ly danh sach fragment cua home
    public class PagerAdapter extends FragmentPagerAdapter {
        // Danh sach fragment
        private final List<Fragment> fragmentList = new ArrayList<>();
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        // Them fragment
        public void AddFragment (Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    // TODO: Khoi tao fragment tour
    public void initTourFragment (){
        // Thay doi tieu de
        tabHomeTourBinding.actionBar.actionBarTitle.setText(R.string.title_activity_home);
        // Neu co danh sach tour roi thi khong lay danh sach tour nua
        if (OnlineManager.getInstance().mTourList != null){
            // Gan danh sach tour
            setTourDataListView();
            // Tat thong bao dang lay danh sach tour
            tourProgressOff();
        } else if (OnlineManager.getInstance().sendMessage("{\"COMMAND\":\"GET_TOUR_LIST\",\"USER_ID\":" +
                OnlineManager.getInstance().userInfo.getUserId() +"}") == true) {
            // Hien thi thong bao dang lay danh sach tour
            tourProgressOn();
        }
    }

    // TODO: Set danh sach tour
    public void setTourDataListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeTourBinding.listview == null) {
                    return;
                }
                // Gan tour adapter
                tabHomeTourBinding.listview.setAdapter(tourAdapter);
                // Set su kien khi nhan vao tour
                tabHomeTourBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Bat progress len
                        tourProgressOn();
                        // Neu nhan vao tour thi chuyen qua activity tour tabs
                        Intent intent = new Intent(HomeTabsActivity.this, TourTabsActivity.class);
                        // Gan so thu tu tour
                        intent.putExtra("TourOrder", position);
                        // Chuyen activity
                        HomeTabsActivity.this.startActivity(intent);
                        // Neu khong chuyen activity duoc thi tat progress di
                        tourProgressOff();
                    }
                });
            }
        });
    }

    // TODO: Khoi tao fragment news
    public void initNewsFragment (){
        // Thay doi tieu de
        tabHomeNewsBinding.actionBar.actionBarTitle.setText(R.string.info_title);
        // Neu co danh sach tin tuc roi thi khong lay danh sach tin tuc nua
        if (OnlineManager.getInstance().mNewsList != null){
            // Gan danh sach tin tuc
            setNewsDataListView();
            // Tat thong bao dang lay danh sach tin tuc
            newsProgressOff();
        } else if (OnlineManager.getInstance().sendMessage("{\"COMMAND\":\"GET_NEWS_LIST\"}") == true) {
            // Hien thi thong bao dang lay danh sach tin tuc
            newsProgressOn();
        }
    }

    // TODO: Khoi tao fragment conversations
    public void initConversationsFragment (){
        // Thay doi tieu de
        tabHomeConversationsBinding.actionBar.actionBarTitle.setText(R.string.conversations_title);
        // Neu co danh sach tin hoi thoai roi thi khong lay danh sach hoi thoai nua
        if (OnlineManager.getInstance().mConversationList != null){
            // Gan danh sach hoi thoai
            setConversationsDataListView();
            // Tat thong bao dang lay danh sach hoi thoai
            conversationsProgressOff();
        } else if (OnlineManager.getInstance().sendMessage("{\"COMMAND\":\"GET_CONVERSATIONS_LIST\"}") == true) {
            // Hien thi thong bao dang lay danh sach hoi thoai
            conversationsProgressOn();
        }
    }

    // TODO: Set danh sach tin tuc
    public void setNewsDataListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeNewsBinding.listview == null) {
                    return;
                }
                // Gan news adapter
                tabHomeNewsBinding.listview.setAdapter(newsAdapter);
                // Set su kien khi nhan vao tin tuc
                tabHomeNewsBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Goi trinh duyet de mo tin tuc
                        Uri uri = Uri.parse(OnlineManager.getInstance().mNewsList.get(position).getmUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    // TODO: Set danh sach hoi thoai
    public void setConversationsDataListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeConversationsBinding.listview == null) {
                    return;
                }
                // Gan conversations adapter
                tabHomeConversationsBinding.listview.setAdapter(conversationsAdapter);
                // Set su kien khi nhan vao hoi thoai
                tabHomeConversationsBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // TODO: Chuyen sang man hinh nhan tin
                            // Chuyen sang man hinh message neu khong click vao minh
                            int selectedUserId = OnlineManager.getInstance().mConversationList.get(position).getPartnerId();
                            if (OnlineManager.getInstance().userInfo.getUserId() != selectedUserId) {
                                Intent intent = new Intent(HomeTabsActivity.this, MessageActivity.class);
                                // Gan so thu tu hoi thoai
                                intent.putExtra("ConversationOrder", position);
                                // Chuyen activity
                                HomeTabsActivity.this.startActivity(intent);
                            }
                        }
                });
            }
        });
    }

    // TODO: Khoi tao fragment user
    public void initUserFragment (){
        // Thay doi tieu de
        tabHomeUserBinding.actionBar.actionBarTitle.setText(R.string.menu_user_info);
        // Neu nhan vao nut dang xuat thi hien thi thong bao
        tabHomeUserBinding.logoutButton.setOnClickListener(new View.OnClickListener() {
            // TODO: Neu nhan vao nut dang xuat thi hien thi thong bao
            @Override
            public void onClick(View view) {
                // Hien thi dialog hoi co dang xuat khong
                new AlertDialog.Builder(HomeTabsActivity.this)
                    .setTitle(getString(R.string.action_sign_out))
                    .setMessage(getString(R.string.ask_sign_out))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Neu gui message dang xuat thanh cong thi hien thi thong bao
                            if (OnlineManager.getInstance().sendMessage("{\"COMMAND\":\"LOGOUT\"}") == true) {
                                // Hien thi thong bao dang dang xuat
                                userProgressOn();
                            }
                        }
                    }).show();
            }
        });
        // Gan user info
        tabHomeUserBinding.userEmailData.setText(OnlineManager.getInstance().userInfo.getUserName());
        tabHomeUserBinding.userFullnameData.setText(OnlineManager.getInstance().userInfo.getFullName());
        switch (OnlineManager.getInstance().userInfo.getGender()) {
            case 0:
                // Khong tiet lo
                tabHomeUserBinding.userGenderData.setText(getString(R.string.gender_undisclosed));
                break;
            case 1:
                // Nam
                tabHomeUserBinding.userGenderData.setText(getString(R.string.gender_male));
                break;
            case 2:
                // Nu
                tabHomeUserBinding.userGenderData.setText(getString(R.string.gender_female));
                break;
            case 3:
                // Gioi tinh thu 3
                tabHomeUserBinding.userGenderData.setText(getString(R.string.gender_other));
                break;
            default:
                tabHomeUserBinding.userGenderData.setText(getString(R.string.gender_male));
                break;
        }
        tabHomeUserBinding.userSchoolData.setText(OnlineManager.getInstance().userInfo.getSchool());
        tabHomeUserBinding.userClassroomData.setText(OnlineManager.getInstance().userInfo.getClassroom());
        tabHomeUserBinding.userPhoneNumberData.setText(OnlineManager.getInstance().userInfo.getPhoneNumber());
        // Tat quay cua tab user
        userProgressOff();
    }

    // TODO: Dang xuat
    public void logout () {
        // Chuyen sang man hinh login
        Intent intent = new Intent(HomeTabsActivity.this, LoginActivity.class);
        HomeTabsActivity.this.startActivity(intent);
        // Xoa man hinh hien tai
        finish();
        // Thong bao dang xuat thanh cong
        ActivityManager.getInstance().makeLongToast("Đăng xuất thành công!");
    }

    // TODO: Set danh sach tu van vien
    public void setCounselorsDataListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeContactBinding.listview == null) {
                    return;
                }
                // Gan counselors adapter
                tabHomeContactBinding.listview.setAdapter(counselorsAdapter);
                // Set su kien khi nhan vao tu van vien
                tabHomeContactBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO: Chuyen sang man hinh nhan tin
                        // Lay tu van vien o vi tri hien tai
                        Counselor counselor = OnlineManager.getInstance().mCounselorsList.get(position);
                        int counselorUserId = counselor.getUserId();
                        if (OnlineManager.getInstance().userInfo.getUserId() != counselorUserId) {
                            Intent intent = new Intent(HomeTabsActivity.this, MessageActivity.class);
                            // Gan so thu tu hoi thoai
                            // Tim doan hoi thoai cua tu van vien nay
                            int conversationOrder = ConversationManager.findConversationOrderByMemberIdAndName(counselor.getUserId(), counselor.getFullname());
                            intent.putExtra("ConversationOrder", conversationOrder);
                            // Chuyen activity
                            HomeTabsActivity.this.startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    // TODO: Refresh danh sach hoi thoai
    public void refreshConversationsList () {
        conversationsAdapter.notifyDataSetChanged();
    }

    // TODO: Refresh danh sach tu van vien
    public void refreshCounselorsList () {
        counselorsAdapter.notifyDataSetChanged();
    }

    // TODO: Khoi tao fragment contact
    public void initContactFragment() {
        // Thay doi tieu de
        tabHomeContactBinding.actionBar.actionBarTitle.setText(R.string.contact_title);
//        // Set ban quyen
//        tabHomeContactBinding.copyright.setText(R.string.copyright);
    }

    // TODO: Refresh danh sach hoi thoai khi quay tro lai activity
    @Override
    protected void onResume() {
        super.onResume();
        refreshConversationsList ();
        refreshCounselorsList ();
    }

    // TODO: Bat tour progress
    public void tourProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeTourBinding.homeProgress.getVisibility() != View.VISIBLE) {
                    tabHomeTourBinding.homeProgress.setVisibility(View.VISIBLE);
                }
                if (tabHomeTourBinding.homeLayout.getVisibility() != View.GONE) {
                    tabHomeTourBinding.homeLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat tour progress
    public void tourProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeTourBinding.homeProgress.getVisibility() != View.GONE) {
                    tabHomeTourBinding.homeProgress.setVisibility(View.GONE);
                }
                if (tabHomeTourBinding.homeLayout.getVisibility() != View.VISIBLE) {
                    tabHomeTourBinding.homeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //TODO: Bat news progress
    public void newsProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeNewsBinding.newsProgress.getVisibility() != View.VISIBLE) {
                    tabHomeNewsBinding.newsProgress.setVisibility(View.VISIBLE);
                }
                if (tabHomeNewsBinding.newsLayout.getVisibility() != View.GONE) {
                    tabHomeNewsBinding.newsLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat news progress
    public void newsProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeNewsBinding.newsProgress.getVisibility() != View.GONE) {
                    tabHomeNewsBinding.newsProgress.setVisibility(View.GONE);
                }
                if (tabHomeNewsBinding.newsLayout.getVisibility() != View.VISIBLE) {
                    tabHomeNewsBinding.newsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //TODO: Bat conversations progress
    public void conversationsProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeConversationsBinding.conversationsProgress.getVisibility() != View.VISIBLE) {
                    tabHomeConversationsBinding.conversationsProgress.setVisibility(View.VISIBLE);
                }
                if (tabHomeConversationsBinding.conversationsLayout.getVisibility() != View.GONE) {
                    tabHomeConversationsBinding.conversationsLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat conversations progress
    public void conversationsProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeConversationsBinding.conversationsProgress.getVisibility() != View.GONE) {
                    tabHomeConversationsBinding.conversationsProgress.setVisibility(View.GONE);
                }
                if (tabHomeConversationsBinding.conversationsLayout.getVisibility() != View.VISIBLE) {
                    tabHomeConversationsBinding.conversationsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //TODO: Bat user progress
    public void userProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeUserBinding.userInfoProgress.getVisibility() != View.VISIBLE) {
                    tabHomeUserBinding.userInfoProgress.setVisibility(View.VISIBLE);
                }
                if (tabHomeUserBinding.userInfoLayout.getVisibility() != View.GONE) {
                    tabHomeUserBinding.userInfoLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat user progress
    public void userProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeUserBinding.userInfoProgress.getVisibility() != View.GONE) {
                    tabHomeUserBinding.userInfoProgress.setVisibility(View.GONE);
                }
                if (tabHomeUserBinding.userInfoLayout.getVisibility() != View.VISIBLE) {
                    tabHomeUserBinding.userInfoLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //TODO: Bat contact progress
    public void contactProgressOn (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeContactBinding.contactProgress.getVisibility() != View.VISIBLE) {
                    tabHomeContactBinding.contactProgress.setVisibility(View.VISIBLE);
                }
                if (tabHomeContactBinding.contactLayout.getVisibility() != View.GONE) {
                    tabHomeContactBinding.contactLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: Tat contact progress
    public void contactProgressOff (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tabHomeContactBinding.contactProgress.getVisibility() != View.GONE) {
                    tabHomeContactBinding.contactProgress.setVisibility(View.GONE);
                }
                if (tabHomeContactBinding.contactLayout.getVisibility() != View.VISIBLE) {
                    tabHomeContactBinding.contactLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public TabHomeTourBinding getTabHomeTourBinding() {
        return tabHomeTourBinding;
    }

    public void setTabHomeTourBinding(TabHomeTourBinding tabHomeTourBinding) {
        this.tabHomeTourBinding = tabHomeTourBinding;
    }

    public TabHomeNewsBinding getTabHomeNewsBinding() {
        return tabHomeNewsBinding;
    }

    public void setTabHomeNewsBinding(TabHomeNewsBinding tabHomeNewsBinding) {
        this.tabHomeNewsBinding = tabHomeNewsBinding;
    }

    public TabHomeUserBinding getTabHomeUserBinding() {
        return tabHomeUserBinding;
    }

    public void setTabHomeUserBinding(TabHomeUserBinding tabHomeUserBinding) {
        this.tabHomeUserBinding = tabHomeUserBinding;
    }

    public TabHomeConversationsBinding getTabHomeConversationsBinding() {
        return tabHomeConversationsBinding;
    }

    public void setTabHomeConversationsBinding(TabHomeConversationsBinding tabHomeConversationsBinding) {
        this.tabHomeConversationsBinding = tabHomeConversationsBinding;
    }

    public TabHomeContactBinding getTabHomeContactBinding() {
        return tabHomeContactBinding;
    }

    public void setTabHomeContactBinding(TabHomeContactBinding tabHomeContactBinding) {
        this.tabHomeContactBinding = tabHomeContactBinding;
    }
}