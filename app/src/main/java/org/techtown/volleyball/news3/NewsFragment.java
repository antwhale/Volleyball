package org.techtown.volleyball.news3;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.volleyball.R;
import org.techtown.volleyball.RecyclerClickListener;
import org.techtown.volleyball.recyclerviewadapter.InstaRecyclerAdapter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class NewsFragment extends Fragment {
    private static final String TAG = "News";

    private static String rssUrl = "http://thespike.co.kr/news/rss.php";

    RecyclerView recyclerView;
    RSSNewsAdapter adapter;
    ArrayList<RSSNewsItem> newsItemList;

    //RXJava
    Disposable backgroundTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);

        newsItemList = new ArrayList<RSSNewsItem>();
        //뉴스 긁어와
        readRSS();

        recyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RSSNewsAdapter(newsItemList, getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerClickListener() {
            @Override
            public void onItemClick(RSSNewsAdapter.ViewHolder holder, View view, int position) {
                //링크가져오기
                String link = newsItemList.get(position).getLink();
                //인텐트만들기
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                getContext().startActivity(intent);
            }

            @Override
            public void onItemClick(InstaRecyclerAdapter.RecyclerViewViewHolder holder, View view, int position) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() Started!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() Started!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() Started!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() Started!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() Started!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() Started!!");
    }

    private void readRSS() {
        try {
            URL url=new URL(rssUrl);

            //스트림역할하여 데이터 읽어오기 : 인터넷 작업은 반드시 퍼미션 작성해야함.
            //Network작업은 반드시 별도의 Thread만 할 수 있다.
            //별도의 Thread 객체 생성

            newsItemList = parsingNewsMethod(url);

            //RssFeedTask task= new RssFeedTask();
            //task.execute(url); //doInBackground()메소드가 발동[thread의 start()와 같은 역할]
        } catch (MalformedURLException e) { e.printStackTrace();}

    }

    private ArrayList<RSSNewsItem> parsingNewsMethod(URL url){
        ArrayList<RSSNewsItem> list = new ArrayList<RSSNewsItem>();

        backgroundTask = Observable.fromCallable(()->{
            try {
                InputStream is= url.openStream();

                //읽어온 xml를 파싱(분석)해주는 객체 생성
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();

                //utf-8은 한글도 읽어오기 위한 인코딩 방식
                xpp.setInput(is, "utf-8");
                int eventType= xpp.getEventType();

                RSSNewsItem item= null;
                String tagName= null;

                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tagName = xpp.getName();

                            if(tagName.equals("item")){
                                item = new RSSNewsItem();
                            }else if(tagName.equals("title")){
                                xpp.next();
                                if(item!=null) item.setTitle(xpp.getText());
                            }else if(tagName.equals("link")){
                                xpp.next();
                                if(item!=null) item.setLink(xpp.getText());
                            }else if(tagName.equals("description")){
                                xpp.next();
                                if(item!=null) item.setDescription(xpp.getText());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tagName=xpp.getName();
                            if(tagName.equals("item")){
                                //기사 하나 파싱했으니 읽어온 기사 한개를 대량의 데이터에 추가
                                list.add(item);
                                //img 주소 따오기

                                String description = item.getDescription();
                                if(description.indexOf("img") != -1) { //이미지 있다면
                                    String imgSrc = item.getDescription().split("<img")[1].split("src=\"")[1].split("thum")[0].concat("thum");
                                    item.setImgUrl(imgSrc);
                                }
                                item=null;

                            }
                            break;
                    }
                    eventType= xpp.next();
                }//while

                //파싱 작업이 완료되었다!!
                //테스트로 Toastㄹ 보여주기, 단 별도 스레드는
                //UI작업이 불가! 그래서 runOnUiThread()를 이용했었음.
                //이 UI작업을 하는 별도의 메소드로
                //결과를 리턴하는 방식을 사용

            } catch (IOException e) {e.printStackTrace();} catch (XmlPullParserException e) {e.printStackTrace();}
            return false;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((result) -> {
            adapter.notifyItemInserted(list.size());
            backgroundTask.dispose();
        });
        return list;
    }

    /*class RssFeedTask extends AsyncTask<URL, Void, String> {

        //Thread의 run()메소드와 같은 역할
        @Override
        protected String doInBackground(URL... urls) {
            //...는 여러개를 받는 의미, 만약 task.execute(url, url2, url3); 보내면 urls[3]로 받는다.
            //전달받은 URL 객체
            URL url= urls[0];

            //해임달(URL)에게 무지개로드(Stream) 열도록..
            try {
                InputStream is= url.openStream();

                //읽어온 xml를 파싱(분석)해주는 객체 생성
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();

                //utf-8은 한글도 읽어오기 위한 인코딩 방식
                xpp.setInput(is, "utf-8");
                int eventType= xpp.getEventType();

                RSSNewsItem item= null;
                String tagName= null;

                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tagName = xpp.getName();

                            if(tagName.equals("item")){
                                item = new RSSNewsItem();
                            }else if(tagName.equals("title")){
                                xpp.next();
                                if(item!=null) item.setTitle(xpp.getText());
                            }else if(tagName.equals("link")){
                                xpp.next();
                                if(item!=null) item.setLink(xpp.getText());
                            }else if(tagName.equals("description")){
                                xpp.next();
                                if(item!=null) item.setDescription(xpp.getText());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tagName=xpp.getName();
                            if(tagName.equals("item")){
                                //읽어온 기사 한개를 대량의 데이터에 추가
                                newsItemList.add(item);
                                //img 주소 따오기
                                String description = item.getDescription();
                                if(description.indexOf("img") != -1) {
                                    String imgSrc = item.getDescription().split("<img")[1].split("src=\"")[1].split("thum")[0].concat("thum");
                                    item.setImgUrl(imgSrc);
                                }
                                item=null;
                                //리사이클러의 아답터에게 데이터가
                                //변경되었다는 것을 통지
                                publishProgress();
                            }
                            break;
                    }
                    eventType= xpp.next();
                }//while

                //파싱 작업이 완료되었다!!
                //테스트로 Toastㄹ 보여주기, 단 별도 스레드는
                //UI작업이 불가! 그래서 runOnUiThread()를 이용했었음.
                //이 UI작업을 하는 별도의 메소드로
                //결과를 리턴하는 방식을 사용

            } catch (IOException e) {e.printStackTrace();} catch (XmlPullParserException e) {e.printStackTrace();}

            return "파싱종료"; // 리턴 값은 onPostExecute(String s) s에 전달된다.
        }//doIBackground()

        //doInBackground메소드가 종료된 후
        //UI작업을 위해 자동 실행되는 메소드
        //runOnUiThread()와 비슷한 역할

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            adapter.notifyItemInserted(newsItemList.size());
        }

        @Override
        protected void onPostExecute(String s) { //매개 변수 s에 들어오는 값음 doIBackground()의 return 값이다.
            super.onPostExecute(s);

            //adapter.notifyDataSetChanged();
            //이 메소드 안에서는 UI변경 작업 가능
            //Toast.makeText(getContext(), s+":"+newsItemList.size(), Toast.LENGTH_SHORT).show();
        }
    }*/
}