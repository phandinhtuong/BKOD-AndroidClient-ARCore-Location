package Manager;

import com.example.administrator.bkod_androidclient.HomeTabsActivity;
import com.example.administrator.bkod_androidclient.MessageActivity;
import com.example.administrator.bkod_androidclient.model.Conversation;
import com.example.administrator.bkod_androidclient.model.Counselor;
import com.example.administrator.bkod_androidclient.model.MemberMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Classes.UserInfo;

public class ConversationManager {
    // TODO: Xu ly khi co danh sach hoi thoai
    public static void onRecieveConversationsList(JSONObject conversationsJsonMessage) {
        // Reset danh sach hoi thoai hien tai
        OnlineManager.getInstance().mConversationList = new ArrayList<>();
        try {
            // Lay danh sach hoi thoai
            JSONArray conversationsList = conversationsJsonMessage.getJSONArray("CONVERSATIONS_LIST");
            for (int i = 0; i < conversationsList.length(); i++){
                // Duyet tung hoi thoai trong danh sach hoi thoai
                // Khoi tao id thanh vien
                int partnerId = conversationsList.getJSONObject(i).getInt("PARTNER_ID");
                // Khoi tao ten thanh vien
                String partnerName = conversationsList.getJSONObject(i).getString("PARTNER_NAME");
                // Khoi tao danh sach tin nhan
                ArrayList<MemberMessage> memberMessages = new ArrayList<>();
                // Them cac tin nhan vao danh sach tin nhan
                JSONArray messagesArray = conversationsList.getJSONObject(i).getJSONArray("MESSAGES");
                for (int j = 0; j < messagesArray.length(); j++) {
                    // Khoi tao danh dau nguoi gui
                    Boolean isSender = messagesArray.getJSONObject(j).getBoolean("IS_SENDER");
                    // Khoi tao noi dung tin nhan
                    String content = messagesArray.getJSONObject(j).getString("CONTENT");
                    // Khoi tao ngay gui tin nhan
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    Date date = null;
                    // Convert string sang date
                    date = format.parse(messagesArray.getJSONObject(j).getString("TIME"));
                    // Them 1 tin nhan vao mang tin nhan
                    memberMessages.add(new MemberMessage(isSender, content, date));
                }
                // Them vao mang hoi thoai 1 doan hoi thoai moi
                OnlineManager.getInstance().mConversationList.add(new Conversation(partnerId, partnerName, memberMessages));
            }
            // In ra man hinh thong bao co bao nhieu doan hoi thoai
            ActivityManager.getInstance().makeLongToast("Có " + conversationsList.length() + " đoạn hội thoại");
            // Tat thong bao dang lay danh sach hoi thoai
            if (ActivityManager.getInstance().getCurrentActivity() instanceof HomeTabsActivity){
                if (((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).getTabHomeConversationsBinding() != null) {
                    // Hien thi danh sach hoi thoai
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setConversationsDataListView();
                    // Tat thong bao dang doi hoi thoai
                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).conversationsProgressOff();
                }
            }
        } catch (ParseException | JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }

    // TODO: Xu ly khi nhan duoc tin nhan tu ai do
    public static void onRecieveMessageResult (JSONObject messageResultMessage) {
        // Tin thanh vien nhan tin voi minh trong danh sach hoi thoai va them doan tin nhan moi vao
        try {
            // Lay ket qua tin nhan
            String messageResult = messageResultMessage.getString("RESULT");
            if (messageResult.equals("success")){
                // Neu day la tin nhan gui thanh cong
                // Lay id nguoi gui
                int senderId = messageResultMessage.getInt("SENDER_ID");
                // Lay id nguoi nhan
                int recieverId = messageResultMessage.getInt("RECIEVER_ID");
                // Lay ten nguoi gui
                String senderName = messageResultMessage.getString("SENDER_NAME");
                // Lay ten nguoi nhan
                String recieverName = messageResultMessage.getString("RECIEVER_NAME");
                // Lay noi dung tin nhan
                String content = messageResultMessage.getString("CONTENT");
                // Khoi tao ngay gui tin nhan
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = null;
                // Convert string sang date
                date = format.parse(messageResultMessage.getString("TIME"));
                // Kiem tra xem id nay co phai cua minh khong
                if (senderId == OnlineManager.getInstance().userInfo.getUserId()){
                    // Neu day la tin nhan minh gui
                    // Them tin nhan vao danh sach tin nhan trong danh sach hoi thoai
                    int conversationOrder = findConversationOrderByMemberIdAndName(recieverId, recieverName);
                    // Lay doan hoi thoai vua tim duoc
                    Conversation currentConversation = OnlineManager.getInstance().mConversationList.get(conversationOrder);
                    // Kiem tra xem danh sach message co trong khong
                    if (currentConversation.getMemberMessages() == null) {
                        // Neu khong co danh sach message thi khoi tao danh sach message
                        currentConversation.setMemberMessages(new ArrayList<MemberMessage>());
                    }
                    // Them tin nhan vua gui vao danh sach message
                    currentConversation.getMemberMessages().add(new MemberMessage(true, content, date));
                    // Neu dang nhan tin thi cap nhat danh sach tin nhan
                    if (ActivityManager.getInstance().getCurrentActivity() instanceof MessageActivity) {
                        if (((MessageActivity)ActivityManager.getInstance().getCurrentActivity()).getConversationOrder() == conversationOrder) {
                            // Cap nhat danh sach tin nhan
                            ((MessageActivity)ActivityManager.getInstance().getCurrentActivity()).refreshMessagesListView();
                        }
                    }
                } else {
                    // Neu day la tin nhan minh nhan
                    // Them tin nhan vao danh sach tin nhan trong danh sach hoi thoai
                    int conversationOrder = findConversationOrderByMemberIdAndName(senderId, senderName);
                    // Lay doan hoi thoai vua tim duoc
                    Conversation currentConversation = OnlineManager.getInstance().mConversationList.get(conversationOrder);
                    // Kiem tra xem danh sach message co trong khong
                    if (currentConversation.getMemberMessages() == null) {
                        // Neu khong co danh sach message thi khoi tao danh sach message
                        currentConversation.setMemberMessages(new ArrayList<MemberMessage>());
                    }
                    // Them tin nhan vua nhan vao danh sach message
                    currentConversation.getMemberMessages().add(new MemberMessage(false, content, date));
                    // Neu dang nhan tin thi cap nhat danh sach tin nhan
                    if (ActivityManager.getInstance().getCurrentActivity() instanceof MessageActivity) {
                        if (((MessageActivity)ActivityManager.getInstance().getCurrentActivity()).getConversationOrder() == conversationOrder) {
                            // Cap nhat danh sach tin nhan
                            ((MessageActivity)ActivityManager.getInstance().getCurrentActivity()).refreshMessagesListView();
                        } else {
                            // Neu dang nhan tin voi nguoi khac thi hien thi tin nhan bang toast
                            if (senderId != OnlineManager.getInstance().userInfo.getUserId ()) {
                                // Neu la tin nhan nguoi khac gui cho minh
                                ActivityManager.getInstance().makeLongToast(senderName + ": " + content);
                            }
                        }
                    } else {
                        // Neu dang khong nhan tin thi hien thi tin nhan bang toast
                        if (senderId != OnlineManager.getInstance().userInfo.getUserId ()) {
                            // Neu la tin nhan nguoi khac gui cho minh
                            ActivityManager.getInstance().makeLongToast(senderName + ": " + content);
                        }
                    }
                }
            } else {
                // Neu gui tin nhan bi loi thi in ra loi
                ActivityManager.getInstance().makeLongToast("Lỗi gửi tin nhắn");
            }
        } catch (ParseException | JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }

    // TODO: Xu ly khi co danh sach tu van vien
    public static void onRecieveCounselorsList(JSONObject counselorsJsonMessage) {
        // Reset danh sach tu van vien hien tai
        OnlineManager.getInstance().mCounselorsList = new ArrayList<>();
        try {
            // Lay danh sach tu van vien
            JSONArray counselorsList = counselorsJsonMessage.getJSONArray("COUNSELORS_LIST");
            for (int i = 0; i < counselorsList.length(); i++){
                // Duyet tung tu van vien trong danh sach tu van vien
                // Khoi tao id tu van vien
                int userId = counselorsList.getJSONObject(i).getInt("USER_ID");
                // Khoi tao ten dang nhap tu van vien
                String username = counselorsList.getJSONObject(i).getString("USERNAME");
                // Khoi tao ten day du tu van vien
                String fullname = counselorsList.getJSONObject(i).getString("FULLNAME");
                // Gioi tinh tu van vien
                int gender = counselorsList.getJSONObject(i).getInt("GENDER");
                // Trang thai dang nhap cua tu van vien
                int state = counselorsList.getJSONObject(i).getInt("STATE");
                // Them vao mang tu van vien 1 tu van vien moi
                OnlineManager.getInstance().mCounselorsList.add(new Counselor(userId, username, fullname, gender, state));
            }
            // In ra man hinh thong bao co bao nhieu tu van vien
            ActivityManager.getInstance().makeLongToast("Có " + counselorsList.length() + " tư vấn viên");
//            // Tat thong bao dang lay danh sach tu van vien
//            if (ActivityManager.getInstance().getCurrentActivity() instanceof HomeTabsActivity){
//                if (((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).getTabHomeContactBinding() != null) {
//                    // Hien thi danh sach tu van vien
//                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).setCounselorsDataListView();
//                    // Tat thong bao dang doi danh sach tu van vien
//                    ((HomeTabsActivity)ActivityManager.getInstance().getCurrentActivity()).contactProgressOff();
//                }
//            }
        } catch (JSONException e) {
            // Neu convert json bi loi thi in ra loi
            e.printStackTrace();
        }
    }

    // Ham tim doan hoi thoai dua tren member info
    public static int findConversationOrderByMemberInfo (UserInfo memberInfo){
        for (int i = 0; i < OnlineManager.getInstance().mConversationList.size(); i++) {
            // Duyet danh sach hoi thoai
            if (OnlineManager.getInstance().mConversationList.get(i).getPartnerId() == memberInfo.getUserId()) {
                // Neu tim thay doan hoi thoai thi tra ve so thu tu doan hoi thoai
                return i;
            }
        }
        // Neu khong tim thay doan hoi thoai thi them 1 doan hoi thoai moi
        // Khoi tao danh sach tin nhan
        ArrayList<MemberMessage> memberMessages = new ArrayList<>();
        // Khoi tao 1 conversation
        Conversation conversation = new Conversation(memberInfo.getUserId(), memberInfo.getFullName(), memberMessages);
        // Them conversation nay vao danh sach conversations
        OnlineManager.getInstance().mConversationList.add(conversation);
        // Tra ve conversation order
        return (OnlineManager.getInstance().mConversationList.size() - 1);
    }

    // Ham tin doan hoi thoai dua tren member id va member name
    public static int findConversationOrderByMemberIdAndName (int memberId, String memberName){
        for (int i = 0; i < OnlineManager.getInstance().mConversationList.size(); i++) {
            // Duyet danh sach hoi thoai
            if (OnlineManager.getInstance().mConversationList.get(i).getPartnerId() == memberId) {
                // Neu tim thay doan hoi thoai thi tra ve so thu tu doan hoi thoai
                return i;
            }
        }
        // Neu khong tim thay doan hoi thoai thi them 1 doan hoi thoai moi
        // Khoi tao danh sach tin nhan
        ArrayList<MemberMessage> memberMessages = new ArrayList<>();
        // Khoi tao 1 conversation
        Conversation conversation = new Conversation(memberId, memberName, memberMessages);
        // Them conversation nay vao danh sach conversations
        OnlineManager.getInstance().mConversationList.add(conversation);
        // Refresh danh sach conversation
        if (ActivityManager.getInstance().getCurrentActivity() instanceof HomeTabsActivity) {
            ((HomeTabsActivity) ActivityManager.getInstance().getCurrentActivity()).refreshConversationsList();
        }
        // Tra ve conversation order
        return (OnlineManager.getInstance().mConversationList.size() - 1);
    }
}
