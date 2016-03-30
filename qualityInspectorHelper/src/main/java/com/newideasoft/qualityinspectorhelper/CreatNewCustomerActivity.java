package com.newideasoft.qualityinspectorhelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreatNewCustomerActivity extends Activity implements OnClickListener {
    private EditText new_account;
    private EditText new_password;
    private EditText repeat_password;
    private Button regist;
    private EditText new_acountName;
    private Spinner company;
    private Spinner organization;
    //private static final String COMPANIES[]={"车轴分公司","铁货分公司","专用分公司","质量部","工艺部","设计部","质检部","锻造分公司","铸造分公司"};
    private static final String COMPANIES[] = {"质检部"};
    private static final String ORGANIZATIONS[] = {"管理室", "技术室", "车辆检验科", "车轴检验科", "原材料检验科", "锻造检验科", "铸造检验科"};
    private ComAdapter comAdapter;
    private OrgAdapter orgAdapter;
    class ComAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return COMPANIES.length;
        }

        @Override
        public Object getItem(int position) {
            return COMPANIES[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = View.inflate(CreatNewCustomerActivity.this,R.layout.spinner_item,null);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.spinnerItem);
            textView.setText(COMPANIES[position]);
            return convertView;
        }
    }
    class OrgAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return ORGANIZATIONS.length;
        }

        @Override
        public Object getItem(int position) {
            return ORGANIZATIONS[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = View.inflate(CreatNewCustomerActivity.this,R.layout.spinner_item,null);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.spinnerItem);
            textView.setText(ORGANIZATIONS[position]);
            return convertView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_new_customer);
        new_account = (EditText) findViewById(R.id.new_account);
        new_password = (EditText) findViewById(R.id.new_password);
        company = (Spinner) findViewById(R.id.company);
        comAdapter = new ComAdapter();
        company.setAdapter(comAdapter);
        organization = (Spinner) findViewById(R.id.organization);
        orgAdapter = new OrgAdapter();
        organization.setAdapter(orgAdapter);
        repeat_password = (EditText) findViewById(R.id.repeat_password);
        regist = (Button) findViewById(R.id.addnewCustomer);
        new_acountName = (EditText) findViewById(R.id.new_accountName);
        regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String account = new_account.getText().toString();
        String password1 = new_password.getText().toString();
        String password2 = repeat_password.getText().toString();
        String name = new_acountName.getText().toString();
        if (checkCustomerInfo(account, name, password1, password2)) {
            //提交请求
            //服务器处理请求
            //		服务器端检查账号，密码信息是否符合要求，向用户发送短信验证码，用户填写验证码完成注册
            //向管理员发送申请消息
            //管理员审核，添加权限，发送反馈
        }
    }

    private boolean checkCustomerInfo(String account, String name, String password1, String password2) {
        //用户名，密码格式是否符合要求，两次输入的密码是否一致
        //用户名为手机号，只包含数字，长度为11位
        //密码为长度不低于6位
        if (!checkNumber(account)) {
            Toast.makeText(this, "电话号码格式不正确！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.equals("")) {
            Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password2.equals(password1)) {
            Toast.makeText(this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
            return false;

        } else if (password1.length() < 6 || password1.length() > 16) {
            Toast.makeText(this, "密码长度应为6-15位！", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }

    }

    private boolean checkNumber(String account) {
        try {
            long number = Long.parseLong(account);
            System.out.println(number + "");
            if (number > 19999999999l || number < 10000000000l) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
