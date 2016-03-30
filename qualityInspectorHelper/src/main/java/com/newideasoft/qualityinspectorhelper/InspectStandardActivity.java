package com.newideasoft.qualityinspectorhelper;

import java.io.File;
import java.net.HttpURLConnection;

import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author NewIdeaSoft 根据用户提交的信息，显示可以查看的文件列表，点击加载，加载完成后打开文件 多级列表的显示 列表样式图片+文本
 */
public class InspectStandardActivity extends BaseActivity implements OnChildClickListener {
    private ExpandableListView sourceFilesList;
    private TextView selectedProduct;
    private TextView selectedStation;
    private ExpandAdapter adapter;
    private static final String[] PARENT_NAMES = {"安全知识", "机械检查基础", "技术标准", "操作流程及方法"};
    private static final int[] PARENT_ICONS = {R.drawable.safe2, R.drawable.standard, R.drawable.standard,
            R.drawable.logo};
    private static final String[][] CHILD_NAMES = {{"教学视频", "岗位危险源", "应急处置措施"},
            {"教学视频", "岗位基础知识", "检验工作流程", "质量问题处理流程"}, {"产品图样", "工艺文件", "技术通知"},
            {"教学视频", "作业指导书", "操作流程", "操作方法", "图例"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_standard_files);
        sourceFilesList = (ExpandableListView) findViewById(R.id.sourceFilesList);
        selectedProduct = (TextView) findViewById(R.id.selectedProduct);
        selectedStation = (TextView) findViewById(R.id.selectedStation);
        Intent intent = getIntent();
        selectedProduct.setText(intent.getStringExtra("product"));
        selectedStation.setText(intent.getStringExtra("station"));
        adapter = new ExpandAdapter();
        sourceFilesList.setAdapter(adapter);
        getFileNames(selectedProduct.getText().toString(), selectedStation.getText().toString(), FILES_SERVLET_PATH);
        sourceFilesList.setOnChildClickListener(this);
    }

    class ExpandAdapter implements ExpandableListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return PARENT_NAMES.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return CHILD_NAMES[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return PARENT_NAMES[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return CHILD_NAMES[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(InspectStandardActivity.this, R.layout.parent_list_item, null);
            }
            ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
            TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
            itemIcon.setImageResource(PARENT_ICONS[groupPosition]);
            itemName.setText(getGroup(groupPosition).toString());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(InspectStandardActivity.this, R.layout.spinner_item, null);
            }
            TextView itemName = (TextView) convertView.findViewById(R.id.spinnerItem);
            itemName.setText(getChild(groupPosition, childPosition).toString());
            itemName.setTextSize(20);
            itemName.setPadding(20, 5, 20, 5);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }
    }

    /**
     * 当点击子项后，打开文件，如文件不存在，开始下载，完成后打开
     */
    // 文件名
    private String[][] filesName;
    // 指定的文件存储路径不含文件名
    private static final String FILES_DIR = "";
    // 指定的文件下载路径，不含文件名
    private static final String FILES_SERVLET_PATH = "http://192.168.31.189:8080/QualityInspectorHelper/GetFilesNameServlet";
    private FileInfo fileInfo;

    private ProgressDialog dialog;

    private void getFileNames(String product, String station, String urlPath) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                if (result != null) {
                    Gson gson = new Gson();
                    fileInfo = gson.fromJson(result, FileInfo.class);
                }
            }

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(InspectStandardActivity.this);
                dialog.setTitle("正在加载...");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String canlogin = null;
                try {
                    StringBuffer sb = LoginActivity.loginInfoToPm(params[0], params[1]);
                    byte[] data = sb.toString().getBytes();
                    HttpURLConnection connection = NetWorkTools.getConnection(params[2], data);
                    canlogin = LoginActivity.canLogin(connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return canlogin;
            }
        }.execute(product, station, urlPath);
    }

    public static String getFileExpandedName(String name) {
        String expandedName = "";
        int index = -1;
        index = name.lastIndexOf(".");
        if (index != -1) {
            expandedName = name.substring(index + 1, name.length()).toLowerCase();
        }
        return expandedName;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String path = fileInfo.getProductId() + "/" + fileInfo.getStationId() + "/" + fileInfo.getFilesName()[groupPosition][childPosition];
        File file = new File(FILES_DIR, path);
        String expandedName = getFileExpandedName(file.getName());
        if (fileInfo != null) {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.mkdirs();
                }
                Intent intent = new Intent(InspectStandardActivity.this, FileDownloadService.class);
                intent.putExtra("filepath", path);
                startService(intent);
            } else {
                // 打开文件
                openFile(file, expandedName);
            }
        } else {
            getFileNames(selectedProduct.getText().toString(), selectedStation.getText().toString(),
                    FILES_SERVLET_PATH);
        }
        // 1.从网络下载文件，通过服务实现
        // 服务器返回一个josn字符串，解析为与名称对应的2维数组，代表于岗位对应的文件的路径，匹配到列表的子项中，当子项被点击
        // 2.点击打开本地文件

        return true;
    }

    private void openFile(File file, String expandedName) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件的MimeType
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(expandedName);
        if (type != null) {
            intent.setDataAndType(Uri.fromFile(file), type);
            startActivity(intent);
        }
    }
}
