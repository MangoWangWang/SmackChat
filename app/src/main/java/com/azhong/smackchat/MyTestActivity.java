package com.azhong.smackchat;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Set;

public class MyTestActivity extends AppCompatActivity {
    private EditText ed1, ed2, ed3, ed4;
    private String user1, user2, password1, password2;
    private Button login, register;
    private XMPPTCPConnection mConnection;

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
        ed3 = (EditText) findViewById(R.id.editView3);
        ed4 = (EditText) findViewById(R.id.editView4);
        login = (Button) findViewById(R.id.denglu);
        register = (Button) findViewById(R.id.zhuche);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
                }else
                {
                    Log.d("error",",登录失败");
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user2 = ed3.getText().toString();
                password2 = ed4.getText().toString();
                boolean result = registerUser(user2,password2);
                if (result)
                {
                    Log.d("success",",注册成功");
                }else
                {
                    Log.d("error","注册失败");
                }

            }
        });

        mConnection = connect();



    }


    private XMPPTCPConnection connect() {
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost("120.79.62.147")//服务器IP地址
                    //服务器端口
                    .setPort(5222)
                    //服务器名称
                    .setServiceName("iz4qvlxllh87cdz")
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
    }

