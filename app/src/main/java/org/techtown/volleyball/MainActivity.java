package org.techtown.volleyball;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.techtown.volleyball.data.entity.TeamInfo;
import org.techtown.volleyball.news3.NewsFragment;
import org.techtown.volleyball.playerranking5.PlayerFragment;
import org.techtown.volleyball.schedule2.ScheduleFragment;
import org.techtown.volleyball.settings.SettingsFragment;
import org.techtown.volleyball.teamranking4.TeamFragment;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    static final List<TeamInfo> teamList = new ArrayList<>();

    MainFragment mainFragment;
    ScheduleFragment scheduleFragment;
    TeamFragment teamFragment;
    PlayerFragment playerFragment;
    NewsFragment newsFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버전체크하자
        versionCheck();

        /*try {
            //ssl파싱하기위해서
            //setSSL();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/

        //액션바 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.title_v9v9); // 커스텀 사용할 파일 위치

        //하단탭 만들기
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);

        //프래그먼트 설정
        mainFragment = new MainFragment();
        scheduleFragment = new ScheduleFragment();
        teamFragment = new TeamFragment();
        playerFragment = new PlayerFragment();
        newsFragment = new NewsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();

        navView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.tab1:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
                                return true;
                            case R.id.tab2:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, scheduleFragment).commit();
                                return true;
                            case R.id.tab3:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newsFragment).commit();
                                return true;
                            case R.id.tab4:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, teamFragment).commit();
                                return true;
                            case R.id.tab5:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, playerFragment).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );
        //각 팀 url입력!
        putTeamInfo();
    }

    private void putTeamInfo() {
        //각 팀 유튜브, 인스타, 뉴스 링크 정리
        //여자배구
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411607&qvt=0&query=GS%EC%B9%BC%ED%85%8D%EC%8A%A4%20%EC%84%9C%EC%9A%B8KIXX%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","gscaltexkixx", "gs%EC%B9%BC%ED%85%8D%EC%8A%A4"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411611&qvt=0&query=IBK%EA%B8%B0%EC%97%85%EC%9D%80%ED%96%89%20%EC%95%8C%ED%86%A0%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","ibk__altos", "%EA%B8%B0%EC%97%85%EC%9D%80%ED%96%89"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411608&qvt=0&query=%ED%95%9C%EA%B5%AD%EB%8F%84%EB%A1%9C%EA%B3%B5%EC%82%AC%20%ED%95%98%EC%9D%B4%ED%8C%A8%EC%8A%A4%20%EB%B0%B0%EA%B5%AC%EB%8B%A8%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","hipassvolleyclub", "%EB%8F%84%EB%A1%9C%EA%B3%B5%EC%82%AC"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EC%A0%95%EA%B4%80%EC%9E%A5+%EB%A0%88%EB%93%9C%EC%8A%A4%ED%8C%8C%ED%81%AC%EC%8A%A4+%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81&oquery=%EC%A0%95%EA%B4%80%EC%9E%A5+%EB%A0%88%EB%93%9C%EC%8A%A4%ED%8C%8C%ED%81%AC%EC%8A%A4+%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81&tqi=iPeHVwqVOsCssSDf%2F0Vssssss4h-239438","red_sparks", "%EC%9D%B8%EC%82%BC%EA%B3%B5%EC%82%AC"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411604&qvt=0&query=%ED%8E%98%ED%8D%BC%EC%A0%80%EC%B6%95%EC%9D%80%ED%96%89%20AI%20PEPPERS%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","", "%ED%8E%98%ED%8D%BC%EC%A0%80%EC%B6%95%EC%9D%80%ED%96%89"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411609&qvt=0&query=%ED%98%84%EB%8C%80%EA%B1%B4%EC%84%A4%20%ED%9E%90%EC%8A%A4%ED%85%8C%EC%9D%B4%ED%8A%B8%20%EB%B0%B0%EA%B5%AC%EB%8B%A8%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","hdecvolleyball", "%ED%98%84%EB%8C%80%EA%B1%B4%EC%84%A4"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411605&qvt=0&query=%ED%9D%A5%EA%B5%AD%EC%83%9D%EB%AA%85%20%ED%95%91%ED%81%AC%EC%8A%A4%ED%8C%8C%EC%9D%B4%EB%8D%94%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","hkpinkspiders", "%ED%9D%A5%EA%B5%AD%EC%83%9D%EB%AA%85"));
        //남자배구
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411598&qvt=0&query=KB%EC%86%90%ED%95%B4%EB%B3%B4%ED%97%98%20%EC%8A%A4%ED%83%80%EC%A6%88%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","kbstarsvc", "kb%EC%86%90%ED%95%B4%EB%B3%B4%ED%97%98"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411600&qvt=0&query=OK%EA%B8%88%EC%9C%B5%EA%B7%B8%EB%A3%B9%20%EC%9D%8F%EB%A7%A8%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","okman_volleyballclub", "OK%EA%B8%88%EC%9C%B5%EA%B7%B8%EB%A3%B9"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411599&qvt=0&query=%EB%8C%80%ED%95%9C%ED%95%AD%EA%B3%B5%20%EC%A0%90%EB%B3%B4%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","kal_jbos", "%EB%8C%80%ED%95%9C%ED%95%AD%EA%B3%B5"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411603&qvt=0&query=%EC%82%BC%EC%84%B1%ED%99%94%EC%9E%AC%20%EB%B8%94%EB%A3%A8%ED%8C%A1%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","bluefangsvc", "%EC%82%BC%EC%84%B1%ED%99%94%EC%9E%AC"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411601&qvt=0&query=%EC%9A%B0%EB%A6%AC%EC%B9%B4%EB%93%9C%20%EC%9A%B0%EB%A6%ACWON%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","wooricardwibee", "%EC%9A%B0%EB%A6%AC%EC%B9%B4%EB%93%9C"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411602&qvt=0&query=%ED%95%9C%EA%B5%AD%EC%A0%84%EB%A0%A5%20%EB%B9%85%EC%8A%A4%ED%86%B0%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","vixtorm_vbc", "%ED%95%9C%EA%B5%AD%EC%A0%84%EB%A0%A5"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411597&qvt=0&query=%ED%98%84%EB%8C%80%EC%BA%90%ED%94%BC%ED%83%88%20%EC%8A%A4%EC%B9%B4%EC%9D%B4%EC%9B%8C%EC%BB%A4%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","skywalkers_vbc", "%ED%98%84%EB%8C%80%EC%BA%90%ED%94%BC%ED%83%88"));
//        teamList.add(new TeamInfo("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%ED%94%84%EB%A1%9C%EB%B0%B0%EA%B5%AC+%EC%A0%95%EA%B7%9C%EB%A6%AC%EA%B7%B8+%EB%A6%AC%EA%B7%B8%EC%98%81%EC%83%81&oquery=2023-2024+%ED%94%84%EB%A1%9C%EB%B0%B0%EA%B5%AC+%EC%A0%95%EA%B7%9C%EB%A6%AC%EA%B7%B8+%EB%A6%AC%EA%B7%B8%EC%98%81%EC%83%81&tqi=iPeJJlqo15wsshr2Z10ssssssdw-255768","kovopr_official", "%EB%B0%B0%EA%B5%AC"));
    }

    private String whatIsTeam() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        String name = sharedPreferences.getString("favorite_team", "");

        return name;
    }

    //설정메뉴 등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //설정메뉴 클릭시
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item != null){
            settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, settingsFragment)
                    .addToBackStack(null)

                    .commit();

            return super.onOptionsItemSelected(item);
        }
        return false;
    }


    //어플 버전체크하기!
    //버전 바뀌면은 gradle(app) 가서 version code와 version name을 수정한다.
    //그리고 firebase가서 버전 수정한다.
    private void versionCheck() {
        //파이어베이스 원격객체 구성
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        //인앱 매개변수 기본값 설정
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default);
        //값 가져오기 및 활성화
        mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(this, task ->  {
                    String installed_version = "";
                    String firebase_version = "0@0@0";
                    boolean versionPass = true;

                    if (task.isSuccessful()) {
                        mFirebaseRemoteConfig.activate();
                        installed_version = BuildConfig.VERSION_NAME;
                        Log.d("Boo", "installed_version" + installed_version);

                        if(mFirebaseRemoteConfig.getString("android_version").length()>0) {
                            firebase_version = mFirebaseRemoteConfig.getString("android_version");
                            Log.d("Boo", "firebase_version" + firebase_version);
                        }

                        //버전비교하기
                        if(installed_version.equals(firebase_version)) {
                            versionPass = true;
                            Log.d(TAG, "Config params updated: " + versionPass);
                            //Toast.makeText(io.github.antwhale.activity.MainActivity.this, "Fetch and activate succeeded",
                            //        Toast.LENGTH_SHORT).show();
                        } else {
                            versionPass = false;
                            displayWelcomeMessage();
                        }
                    }

                });
    }

    private void displayWelcomeMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("업데이트 안내");
        builder.setMessage("앱 버젼이 다릅니다. 보다 좋은 서비스를 위해 업데이트 해주세요.");
        builder.setPositiveButton("업데이트",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url2 = "https://play.google.com/store/apps/details?id=org.techtown.volleyball";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                        startActivity(i);
                        //System.runFinalization();
                        //System.exit(0);
                    }
                });

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // TODO Auto-generated method stub

                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // TODO Auto-generated method stub
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}