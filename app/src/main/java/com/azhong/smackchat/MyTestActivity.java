package com.azhong.smackchat;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MyTestActivity extends AppCompatActivity {
    private EditText ed1, ed2;
    private String user1, password1;
    private Button login, sendMessage, zhuxiao, setChatListener,getOutUserInfo,huoquzhanghuoxin,addfriend;
    private XMPPTCPConnection mConnection;
    private Set<String> userInfo;

    private ChatManagerListener chatManagerListener = new ChatManagerListener() {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    if (!TextUtils.isEmpty(message.getBody())) {
                        //message为用户所收到的消息
                        //接收到消息Message之后进行消息展示处理，这个地方可以处理所有人的消息
                        Log.d("message", "processMessage: "+message.getBody().toString());
                    }

                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ed1 = (EditText) findViewById(R.id.editView1);
        ed2 = (EditText) findViewById(R.id.editView2);
        login = (Button) findViewById(R.id.denglu);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        zhuxiao = (Button) findViewById(R.id.zhuxiao);
        setChatListener = (Button) findViewById(R.id.setChatListener);
        getOutUserInfo = (Button) findViewById(R.id.getLianXinxi);
        huoquzhanghuoxin = (Button) findViewById(R.id.huoquyonghuxinxi);
        addfriend = (Button)findViewById(R.id.addfriends); 

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mConnection = connect();
                user1 = ed1.getText().toString();
                password1 = ed2.getText().toString();
                boolean result = false;
                try {
                    result = login(user1,password1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("error",",登录异常");
                }
                if (result)
                {
                    Log.d("success",",登录成功");
                    Toast.makeText(MyTestActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }else
                {
                    Log.d("error",",登录失败");
                }
            }
        });


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = createChat("1032@iz4qvlxllh87cdz/smack");
                try {
                    chat.sendMessage("hello");
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

            }
        });

        zhuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logout())
                {
                    Toast.makeText(MyTestActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(MyTestActivity.this, "注销失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setChatListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getChatManager().addChatListener(chatManagerListener);
            }
        });

        huoquzhanghuoxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> accountAttributes  =  getAccountAttributes();

                Iterator<String> iterator = accountAttributes.iterator();
                while(iterator.hasNext()) {
                    String trim = iterator.next().toString().trim();
                    try {
                        trim = AccountManager.getInstance(mConnection).getAccountAttribute(trim);
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                    Log.e("Account", "获取账号信息成功===" + trim);
                }
            }
        });
        
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend("1031");
            }
        });

        getOutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOfflineMessage();
            }
        });

        //mConnection = connect();



    }


    /**
     * 连接方法
     * @return
     */
    private XMPPTCPConnection connect() {
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost("120.79.62.147")//服务器IP地址
                    //服务器端口
                    .setPort(5222)
                    //服务器名称
                    .setServiceName("iz4qvlxllh87cdz")
                    .setSendPresence(false)
                    //是否开启安全模式
                    .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                    //是否开启压缩
                    .setCompressionEnabled(false)
                    //开启调试模式
                    .setDebuggerEnabled(true).build();

            XMPPTCPConnection connection = new XMPPTCPConnection(config);
            connection.connect();
            Log.d("success", "连接服务器连接成功");
            return connection;
        } catch (Exception e) {
            Log.d("error", "连接服务器连接失败");
            return null;
        }

    }


    /**
     * 是否连接成功
     * @return
     */
    private boolean isConnected() {
        if(mConnection == null) {
            return false;
        }
        if(!mConnection.isConnected()) {
            try {
                mConnection.connect();
                return true;
            } catch (SmackException | IOException | XMPPException e) {
                Log.d("error","isconnecd连接异常");
                return false;
            }
        }
        return true;
    }


    /**
     * 注册用户信息
     * @param user        账号，是用来登陆用的，不是用户昵称
     * @param password    账号密码
     * @return
     */
    public boolean registerUser(String user, String password) {
        if(!isConnected()) {
            return false;
        }
        try {
            SASLAuthentication.registerSASLMechanism(new SASLPlainMechanism());
            AccountManager.getInstance(mConnection).createAccount(user, password);
            return true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            Log.e("error", "注册失败", e);
            return false;
        }
    }


    /**
     * 登陆
     * @param user		用户账号
     * @param password		用户密码
     * @return
     * @throws Exception
     */
    public boolean login(String user, String password) throws Exception {
        if(!isConnected()) {
            return false;
        }
        try {
            SASLAuthentication.registerSASLMechanism(new SASLPlainMechanism());
            mConnection.login(user, password);
            return  true;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 注销
     * @return
     */
    public boolean logout() {
        if(!isConnected()) {
            return false;
        }
        try {
            mConnection.instantShutdown();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }


    /**
     * 修改密码
     * @param newpassword    新密码
     * @return
     */
    public boolean changePassword(String newpassword) {
        if(!isConnected()) {
            return false;
        }
        try {
            AccountManager.getInstance(mConnection).changePassword(newpassword);
            return true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            Log.e("error", "密码修改失败", e);
            return false;
        }
    }

    /**
     * 删除当前登录的用户信息(从服务器上删除当前用户账号)
     * @return
     */
    public boolean deleteUser() {
        if (!isConnected()) {
            return false;
        }
        try {
            AccountManager.getInstance(mConnection).deleteAccount();//删除该账号
            return true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            return false;
        }
    }

        /**
         * 获取账户所有属性信息
         * @return
         */
        public Set getAccountAttributes()
        {
            if(isConnected()) {
                try {
                    return AccountManager.getInstance(mConnection).getAccountAttributes();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            throw new NullPointerException("服务器连接失败，请先连接服务器");
        }


    /**
     * 添加好友，可根据账号，昵称，组名
     */
    private void addFriend(String user) {
        if (mConnection.isConnected()) {
            try {
                Roster.getInstanceFor(mConnection).createEntry(user, null, null);
                Log.e("addFriend", "添加好友成功" + user);
                Toast.makeText(this, "添加好友成功", Toast.LENGTH_SHORT).show();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
                Log.e("addFriend", "NotLoggedInException" + e);
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
                Log.e("addFriend", "NoResponseException" + e);
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
                Log.e("addFriend", "XMPPErrorException" + e);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                Log.e("addFriend", "NotConnectedException" + e);
            }
        }
    }


    /**
     * 创建聊天窗口
     * @param jid   好友的JID
     * @return
     */
    public Chat createChat(String jid) {
        if(isConnected()) {
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            return chatManager.createChat(jid);
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }


    /**
     * 获取聊天对象管理器
     * @return
     */
    public ChatManager getChatManager() {
        if(isConnected()) {
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            return chatManager;
        }
        throw new NullPointerException("服务器连接失败，请先连接服务器");
    }

    /**
     * 一上线获取离线消息
     * 设置登录状态为在线
     */
    private void getOfflineMessage() {
        OfflineMessageManager offlineManager = new OfflineMessageManager(mConnection);
        try {
            List<Message> list = offlineManager.getMessages();
            for (Message message : list)
            {
                Log.e("error", "getOfflineMessage: "+message.getBody().toString() );
            }
            //删除离线消息
            offlineManager.deleteMessages();
            //将状态设置成在线
            Presence presence = new Presence(Presence.Type.available);
            mConnection.sendStanza(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}