package com.newideasoft.qualityinspectorhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends Activity implements View.OnClickListener {
    private ViewPager mViewPager;
    private PagerAdapter pagerAdapter;
//    private LinearLayout pager_organization;
//    private LinearLayout pager_files;
//    private LinearLayout pager_prosess;
//    private LinearLayout pager_yourself;
    private List<View> listViews;
    private ImageButton ib_files;
    private ImageButton ib_prosess;
    private ImageButton ib_organization;
    private ImageButton ib_yourself;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
//        pager_organization = (LinearLayout)findViewById(R.id.pager_organization);
//        pager_files = (LinearLayout)findViewById(R.id.pager_files);
//        pager_prosess = (LinearLayout)findViewById(R.id.pager_prosess);
//        pager_yourself = (LinearLayout)findViewById(R.id.pager_yourself);
        ib_files = (ImageButton)findViewById(R.id.ib_files);
        ib_prosess = (ImageButton)findViewById(R.id.ib_prosess);
        ib_organization = (ImageButton)findViewById(R.id.ib_organization);
        ib_yourself = (ImageButton)findViewById(R.id.ib_yourself);
        listViews = new ArrayList<View>();
        listViews.add(View.inflate(this,R.layout.pager_files,null));
        listViews.add(View.inflate(this,R.layout.pager_prosess,null));
        listViews.add(View.inflate(this,R.layout.pager_organization,null));
        listViews.add(View.inflate(this,R.layout.pager_yourself,null));
        ib_files.setOnClickListener(this);
        ib_prosess.setOnClickListener(this);
        ib_organization.setOnClickListener(this);
        ib_yourself.setOnClickListener(this);
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return listViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(listViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = listViews.get(position);
                container.addView(view);
                return view;
            }
        };
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_files :
                mViewPager.setCurrentItem(0);
                //设置按钮点击后的图标和字体颜色
                break;
            case R.id.ib_prosess :
                mViewPager.setCurrentItem(1);

                break;
            case R.id.ib_organization :
                mViewPager.setCurrentItem(2);

                break;
            case R.id.ib_yourself :
                mViewPager.setCurrentItem(3);

                break;
        }
    }
    private View sttItemFiles(){
        View view = View.inflate(this,R.layout.pager_files,null);
        return view;
    }
    private View sttItemProsess(){
        View view = View.inflate(this,R.layout.pager_prosess,null);
        return view;
    }
    private View setItemOganization(){
        View view = View.inflate(this,R.layout.pager_organization,null);
        ListView lv_company = (ListView) view.findViewById(R.id.lv_company);
        ListView lv_organization = (ListView) view.findViewById(R.id.lv_organization);
        CompanyContactsAdapter adapter = new CompanyContactsAdapter();
        lv_company.setAdapter(adapter);
        lv_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("Tel:"+CONTACTSNUMBER));
                startActivity(intent);
            }
        });
        return view;
    }
    private View sttItemYourself(){
        View view = View.inflate(this,R.layout.pager_yourself,null);
        return view;
    }
    class CompanyContactsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = View.inflate(Main2Activity.this,R.layout.contact_item,null);
            }
            TextView contactsName = (TextView) convertView.findViewById(R.id.contactsName);
            TextView contactsStation = (TextView) convertView.findViewById(R.id.contactsStation);
            contactsName.setText(CONTACTSNAME[position]);
            contactsStation.setText(CONTACTSSTATION[position]);
            return convertView;
        }
    }
    class OrganizationContactsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
    private static final String CONTACTSNAME[]={"王瑞","陕军峰","宁迎新"};
    private static final String CONTACTSNUMBER[]={"13834511400","15935148990","13835142436"};
    private static final String CONTACTSSTATION[]={"部长","技术科科长","管理科科长"};


}
