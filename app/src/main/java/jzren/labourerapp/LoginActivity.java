package jzren.labourerapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jzren.labourerapp.jzren.labourerapp.bean.User;
import jzren.labourerapp.jzren.labourerapp.bean.jzren.labourerapp.utils.Utils;


public class LoginActivity extends ActionBarActivity implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener {
    private LinearLayout mLoginLinearLayout; // 登录内容的容器
    private LinearLayout mUserIdLinearLayout; // 将下拉弹出窗口在此容器下方显示
    private EditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private ImageView mMoreUser; // 下拉图标
    private String mIdString;
    private String mPwdString;
    private ArrayList<User> mUsers; // 用户列表
    private ListView mUserIdListView; // 下拉弹出窗显示的ListView对象
    private MyAapter mAdapter; // ListView的监听器
    private PopupWindow mPop; // 下拉弹出窗


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

		/* 获取已经保存好的用户密码 */
        mUsers = Utils.getUserList(LoginActivity.this);

        if (mUsers.size() > 0) {
			/* 将列表中的第一个user显示在编辑框 */
            mIdEditText.setText(mUsers.get(0).getId());
            mPwdEditText.setText(mUsers.get(0).getPwd());
        }

        LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.userifo_listview, null);
        mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
        parent.removeView(mUserIdListView); // 必须脱离父子关系,不然会报错
        mUserIdListView.setOnItemClickListener(this); // 设置点击事
        mAdapter = new MyAapter(mUsers);
        mUserIdListView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLogin(View view) {
        if (mIdString == null || mIdString.equals("")) { // 账号为空时
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT)
                    .show();
        } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                    .show();
        } else {// 账号和密码都不为空时
            boolean mIsSave = true;
            try {
                for (User user : mUsers) { // 判断本地文档是否有此ID用户
                    if (user.getId().equals(mIdString)) {
                        mIsSave = false;
                        break;
                    }
                }
                if (mIsSave) { // 将新用户加入users
                    User user = new User(mIdString, mPwdString);
                    mUsers.add(user);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        }
        Intent it = new Intent();
        it.setClass(LoginActivity.this, MenuActivity.class);
        startActivity(it);
        LoginActivity.this.finish();
    }

    public void onSignUp(View view) {
        Intent it = new Intent();
        it.setClass(LoginActivity.this, SignupActivity.class);
        startActivity(it);
        LoginActivity.this.finish();
    }

    public void onForgetPsw(View view) {
        Toast.makeText(getApplicationContext(), "密码找回中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss() {
        // Log.i(TAG, "切换为角向下图标");
        mMoreUser.setImageResource(R.mipmap.login_more_up);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mIdEditText.setText(mUsers.get(position).getId());
        mPwdEditText.setText(mUsers.get(position).getPwd());
        mPop.dismiss();
    }

    /**
     * ListView的适配器
     */
    class MyAapter extends ArrayAdapter<User> {

        public MyAapter(ArrayList<User> users) {
            super(LoginActivity.this, 0, users);
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.listview_item, null);
            }

            TextView userIdText = (TextView) convertView
                    .findViewById(R.id.listview_userid);
            userIdText.setText(getItem(position).getId());

            ImageView deleteUser = (ImageView) convertView
                    .findViewById(R.id.login_delete_user);
            deleteUser.setOnClickListener(new View.OnClickListener() {
                // 点击删除deleteUser时,在mUsers中删除选中的元素
                @Override
                public void onClick(View v) {

                    if (getItem(position).getId().equals(mIdString)) {
                        // 如果要删除的用户Id和Id编辑框当前值相等，则清空
                        mIdString = "";
                        mPwdString = "";
                        mIdEditText.setText(mIdString);
                        mPwdEditText.setText(mPwdString);
                    }
                    mUsers.remove(getItem(position));
                    mAdapter.notifyDataSetChanged(); // 更新ListView
                }
            });
            return convertView;
        }
    }

    private void initView() {
        mIdEditText = (EditText) findViewById(R.id.login_edtId);
        mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
        mMoreUser = (ImageView) findViewById(R.id.login_more_user);
        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
    }

    public void initPop() {
        int width = mUserIdLinearLayout.getWidth() - 4;
        int height = RadioGroup.LayoutParams.WRAP_CONTENT;
        mPop = new PopupWindow(mUserIdListView, width, height, true);
        mPop.setOnDismissListener(this);// 设置弹出窗口消失时监听器

        // 注意要加这句代码，点击弹出窗口其它区域才会让窗口消失
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));

    }

    public void userListClick(View view) {
        if (mPop == null) {
           initPop();
        }
        if (!mPop.isShowing() && mUsers.size() > 0) {
            mMoreUser.setImageResource(R.mipmap.login_more_down); // 切换图标
            mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // 显示弹出窗口
        }
    }

}
