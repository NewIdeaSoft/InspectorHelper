package com.newideasoft.qualityinspectorhelper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FileDownloadService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static final String FILEURLPATH = "http://192.168.31.189:8080/QualityInspectorHelper/";
    private static final String FILESTORAGEPATH = Environment.getExternalStorageState() + "/InspectorHelper/";
    private NotificationManager manager;
    private Notification.Builder builder;

    // 开始下载通知栏显示通知，点击通知回到界面，下载完成点击通知回到界面，界面显示提示对话框，打开文件
    //通知栏样式包含进度条
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String filePath = intent.getStringExtra("filepath");
        final File file = new File(FILESTORAGEPATH + filePath);
        final String expandedName = InspectStandardActivity.getFileExpandedName(file.getName());
        final RemoteViews notification_view = new RemoteViews(getPackageName(),R.layout.notification_view);
        new AsyncTask<String, Integer, Void>() {
            @Override
            protected void onPreExecute() {
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(FileDownloadService.this,InspectStandardActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(FileDownloadService.this,0,intent,0);
                builder = new Notification.Builder(FileDownloadService.this)
                        .setContent(notification_view)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("文件下载中...")
                        .setContentText("")
                        .setOngoing(true)
                        .setProgress(100, 0, false);
                if (manager != null) {
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                }
                manager.notify(0, builder.build());
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                builder.setProgress(100,values[0],false)
                        .setContentText("下载进度："+values[0]+"%");
                if (manager != null) {
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                }
                manager.notify(0, builder.build());
            }

            @Override
            protected void onPostExecute(Void result) {
                //重新设置pedingIntent,点击通知打开文件，通知消失
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(expandedName);
                if (type != null) {
                    intent.setDataAndType(Uri.fromFile(file), type);
                }
                PendingIntent pendingIntent =PendingIntent.getActivity(FileDownloadService.this,0,intent,0);
                builder.setProgress(0,0,false)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
                if (manager != null) {
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                }
                manager.notify(0, builder.build());
                Intent broadCastIntent = new Intent();
                sendBroadcast(broadCastIntent);
            }

            @Override
            protected Void doInBackground(String... params) {
                HttpURLConnection conn = null;
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    URL url = new URL(FILEURLPATH + params[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(10000);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        fis = (FileInputStream) conn.getInputStream();
                        fos = new FileOutputStream(FILESTORAGEPATH + params[0]);
                        long completedSize = 0;
                        long totleSize = conn.getContentLength();
                        int len = -1;
                        int percent = 0;
                        int i=1;
                        byte[] buffer = new byte[8192];
                        while ((len = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            completedSize += len;
                            //模拟互联网网速
                            SystemClock.sleep(50);
                            percent = (int) (completedSize*100/totleSize);
                            if(percent/(i*5)>0){
                                //每5%更新一次进度
                                publishProgress(percent);
                                i++;
                            }
                        }

                    }else{
                        publishProgress(-1);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    conn.disconnect();
                }
                return null;
            }
        }.execute(filePath);
        return super.onStartCommand(intent, flags, startId);
    }
}
