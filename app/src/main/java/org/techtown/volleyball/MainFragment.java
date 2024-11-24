package org.techtown.volleyball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.volleyball.data.entity.NewsItem;
import org.techtown.volleyball.databinding.FragmentMainBinding;
import org.techtown.volleyball.recyclerviewadapter.InstaRecyclerAdapter;
import org.techtown.volleyball.recyclerviewadapter.InstaRecyclerItem;
import org.techtown.volleyball.slideradapter.NaverTVSliderAdapter;
import org.techtown.volleyball.slideradapter.SliderItem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

//1번 프래그먼트 커스텀 화면
@AndroidEntryPoint
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private NaverTVSliderAdapter sliderAdapter;

    private InstaRecyclerAdapter recyclerAdapter;
    private List<InstaRecyclerItem> mRecyclerItems = new ArrayList<>();

    private FragmentMainBinding binding;
    private ParsingModel parsingModel;

    WebView webView;
    private String myTeam;

    String myNaverTvUrl;
    String myInstaUrl;
    String myNewsUrl;
    String currentInsta="";

    private MutableLiveData<List<InstaRecyclerItem>> instaLivedata = new MutableLiveData<>();

    //firebase - realtime database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("Insta");


    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView() start!");
        super.onDestroyView();
        //화면 가로 세로 전환할 때는 mRecyclerItems size가 0이 되어버리네;;

        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(TAG, "onCreateView start!");

        checkMyTeam();
        //뷰바인딩
        binding = FragmentMainBinding.inflate(getLayoutInflater());
//        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        parsingModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()))
                .get(ParsingModel.class);

        webView = rootView.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);


        parseInsta();
        //인스타는 가로리사이클러뷰에

        binding.kovoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkPage("https://www.kovomarket.co.kr/main/index.php");
            }
        });

        binding.cafeBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/v9v9"));
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    private void setUpSliderView() {
            Log.d(TAG,"sliderView 세팅!");
            sliderAdapter = new NaverTVSliderAdapter(Collections.emptyList());
            binding.imageSlider.setAdapter(sliderAdapter);
//            binding.imageSlider.setSliderAdapter(sliderAdapter);
//            binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
//            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//            binding.imageSlider.startAutoCycle();
    }

    private void checkMyTeam() {
        myTeam = whatIsTeam();
        Log.d("gaja",myTeam + " checkMyTeam() Start!");

        switch (myTeam){
            case "GS칼텍스" :
                myNaverTvUrl = MainActivity.teamList.get(0).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(0).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(0).getNewsUrl();
                break;
            case "기업은행" :
                myNaverTvUrl =  MainActivity.teamList.get(1).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(1).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(1).getNewsUrl();
                break;
            case "도로공사" :
                myNaverTvUrl = MainActivity.teamList.get(2).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(2).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(2).getNewsUrl();
                break;
            case "인삼공사" :
                myNaverTvUrl = MainActivity.teamList.get(3).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(3).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(3).getNewsUrl();
                break;
            case "페퍼저축은행" :
                myNaverTvUrl =  MainActivity.teamList.get(4).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(4).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(4).getNewsUrl();
                break;
            case "현대건설" :
                myNaverTvUrl =  MainActivity.teamList.get(5).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(5).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(5).getNewsUrl();
                break;
            case "흥국생명" :
                myNaverTvUrl =  MainActivity.teamList.get(6).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(6).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(6).getNewsUrl();
                break;
            case "KB손해보험" :
                myNaverTvUrl =  MainActivity.teamList.get(7).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(7).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(7).getNewsUrl();
                break;
            case "OK금융그룹" :
                myNaverTvUrl = MainActivity.teamList.get(8).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(8).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(8).getNewsUrl();
                break;
            case "대한항공" :
                myNaverTvUrl =  MainActivity.teamList.get(9).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(9).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(9).getNewsUrl();
                break;
            case "삼성화재" :
                myNaverTvUrl = MainActivity.teamList.get(10).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(10).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(10).getNewsUrl();
                break;
            case "우리카드" :
                myNaverTvUrl =  MainActivity.teamList.get(11).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(11).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(11).getNewsUrl();
                break;
            case "한국전력" :
                myNaverTvUrl =  MainActivity.teamList.get(12).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(12).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(12).getNewsUrl();
                break;
            case "현대캐피탈" :
                myNaverTvUrl =  MainActivity.teamList.get(13).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(13).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(13).getNewsUrl();
                break;
            default:
                myNaverTvUrl =  MainActivity.teamList.get(14).getNaverTVUrl();
                myInstaUrl = MainActivity.teamList.get(14).getInstaUrl();
                myNewsUrl = MainActivity.teamList.get(14).getNewsUrl();
                break;
        }

        Log.d("gaja", myNaverTvUrl + " " + myInstaUrl + "  " + myNewsUrl);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach start!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume start!");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart start!!");
        //recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause start!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy start!!");
    }

    private void parseInsta() {
        Log.d(TAG, "parseInsta() Start!");
        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> resultMap = (Map<String, Object>) snapshot.getValue();
                if(resultMap != null) {
                    Log.d(TAG, "onDataChange: " + resultMap);

                    String keyValue = "";
                    switch(myTeam) {
                        case "GS칼텍스" :
                            keyValue = "GS";
                            break;
                        case "기업은행" :
                            keyValue = "IBK";
                            break;
                        case "도로공사" :
                            keyValue = "Hypass";
                            break;
                        case "인삼공사" :
                            keyValue = "KGC";
                            break;
                        case "페퍼저축은행" :
                            keyValue = "Pepper";
                            break;
                        case "현대건설" :
                            keyValue = "Hillstate";
                            break;
                        case "흥국생명" :
                            keyValue = "PinkSpiders";
                            break;
                        case "KB손해보험" :
                            keyValue = "KB";
                            break;
                        case "OK금융그룹" :
                            keyValue = "OK";
                            break;
                        case "대한항공" :
                            keyValue = "Jumbos";
                            break;
                        case "삼성화재" :
                            keyValue = "Samsung";
                            break;
                        case "우리카드" :
                            keyValue = "Woori";
                            break;
                        case "한국전력" :
                            keyValue = "Kepco";
                            break;
                        case "현대캐피탈" :
                            keyValue = "SkyWalkers";
                            break;
                        default:
                            keyValue = "KOVO";
                            break;
                    }

                    ArrayList<Map<String, String>> instaDataArray = (ArrayList<Map<String, String>>) resultMap.get(keyValue);
                    for(int i = 0; i < 5; i++) {
                        try {
                            if(mRecyclerItems.size() == 5) recyclerAdapter.deleteItems();
//                            String imgUrl = ((JSONObject) instaDataArray.get(i)).getString("imgUrl");
                            String imgUrl = instaDataArray.get(i).get("imgUrl");
                            String link = instaDataArray.get(i).get("link");
                            mRecyclerItems.add(new InstaRecyclerItem(imgUrl, link));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    instaLivedata.postValue(mRecyclerItems);
                    Log.d(TAG, "mRecyclerItems count = " + mRecyclerItems.size());
                } else {
                    Log.d(TAG, "onDataChange: resultMap is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //예전 웹뷰로 크롤링
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
                // 자바스크립트 기본 메소드로 html 소스를 통째로 지정해서 인자로 넘김
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
                Log.d(TAG, "onPageFinished!!");
            }
        });

        if(!currentInsta.equals("https://www.instagram.com/"+myInstaUrl)){
            //지정한 URL을 웹 뷰로 접근하기
            if(mRecyclerItems.size() == 5){
                recyclerAdapter.deleteItems();
                recyclerAdapter.notifyDataSetChanged();
                Log.d(TAG, "recyclerItems 삭제");
            }

            webView.loadUrl("https://www.instagram.com/" + myInstaUrl);
            currentInsta = "https://www.instagram.com/" + myInstaUrl;
            Log.d(TAG, "webView 열기");
        }

        Log.d(TAG, "currentInsta = " + currentInsta);*/
    }



    private void parseNews(){
        String parsingUrl = "http://thespike.co.kr/news/search.php?q=" + myNewsUrl + "&x=0&y=0";
        parsingModel.makeNewsParsingRequest(parsingUrl);

        parsingModel.parsingNewsLivedata.observe(getViewLifecycleOwner(), new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> NewsItems) {
                if(NewsItems.size() != 0){
                    Glide.with(binding.newsImage.getContext()).load("http://thespike.co.kr/"+NewsItems.get(0).getThumbUrl()).into(binding.newsImage);
                    //뉴스제목 보여주기
                    binding.newsTitle1.setText(NewsItems.get(0).getTitle());
                    binding.newsTitle2.setText(NewsItems.get(1).getTitle());
                    binding.newsTitle3.setText(NewsItems.get(2).getTitle());
                    binding.newsTitle4.setText(NewsItems.get(3).getTitle());
                    //뉴스제목 링크 붙여주기
                    binding.newsImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLinkPage("http://thespike.co.kr/"+NewsItems.get(0).getNewsUrl());
                        }
                    });
                    binding.newsTitle1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLinkPage("http://thespike.co.kr/"+NewsItems.get(0).getNewsUrl());
                        }
                    });
                    binding.newsTitle2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLinkPage("http://thespike.co.kr/"+NewsItems.get(1).getNewsUrl());
                        }
                    });
                    binding.newsTitle3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLinkPage("http://thespike.co.kr/"+NewsItems.get(2).getNewsUrl());
                        }
                    });
                    binding.newsTitle4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLinkPage("http://thespike.co.kr/"+NewsItems.get(3).getNewsUrl());
                        }
                    });
                }

            }
        });
    }

    private void parseNaverTv() {
        //슬라이더뷰 만들기
        setUpSliderView();
        Log.d(TAG, "setUpSliderView Started!");

        parsingModel.parsingLivedata.observe(getViewLifecycleOwner(), new Observer<List<SliderItem>>() {
            @Override
            public void onChanged(List<SliderItem> sliderItems) {
                sliderAdapter.renewItems(sliderItems);   //슬라이더뷰에 넣어
                binding.imageSlider.setAdapter(sliderAdapter);
            }
        });
        parsingModel.makeParsingRequest(myNaverTvUrl);
    }

    private void parseSchedule() {
        String parsingUrl = "https://www.kovo.co.kr/game/v-league/11110_schedule_list.asp";
        //테스트용url
        //리그 url = "https://www.kovo.co.kr/game/v-league/11110_schedule_list.asp"
        //컵대회 url = "https://www.kovo.co.kr/game/kovocup/13110_schedule_list.asp"
        //String parsingUrl = "https://www.kovo.co.kr/game/v-league/11110_schedule_list.asp?season=017&team=&yymm=2021-02&r_round=";
        binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        parsingModel.makeScheduleParsingRequest();
//        parsingModel.parsingScheduleLivedata.observe(getViewLifecycleOwner(), new Observer<List<String[]>>() {
//            @Override
//            public void onChanged(List<String[]> scheduleItems) {
//                Log.d(TAG, "scheduleItems.size() = " + scheduleItems.size());
//
//                //코보 일정페이지 업데이트 차이로 어플 오류생기는거 대비
//                if(scheduleItems.size() == 0){
//                    binding.wLayout.setVisibility(View.GONE);
//                    binding.manTextView.setText("no match");   //경기가 없습니다.
//                    binding.matchContentTextView.setVisibility(View.GONE);
//                    binding.matchTimePlaceTextView.setVisibility(View.GONE);
//                    binding.matchCameraTextView.setVisibility(View.GONE);
//                    binding.progressBar.setVisibility(ProgressBar.GONE);
//
//                }else if(scheduleItems.size() == 1){
//                    if(scheduleItems.get(0)[3].matches("")){
//                        Log.d(TAG, "경기가 없다!");
//                        //경기 없는 날
//                        binding.wLayout.setVisibility(View.GONE);
//                        binding.manTextView.setText(scheduleItems.get(0)[2]);   //경기가 없습니다.
//                        binding.matchContentTextView.setVisibility(View.GONE);
//                        binding.matchTimePlaceTextView.setVisibility(View.GONE);
//                        binding.matchCameraTextView.setVisibility(View.GONE);
//                        binding.progressBar.setVisibility(ProgressBar.GONE);
//
//                    }else{
//                        Log.d(TAG, "경기 1개 뿐");
//                        //하루에 경기 1개인 경우
//                        binding.wLayout.setVisibility(View.GONE);
//                        binding.manTextView.setText(scheduleItems.get(0)[2]);
//                        binding.manTextView.append(" "+scheduleItems.get(0)[8]);//남자부   V리그 6Round
//                        binding.matchContentTextView.setText(scheduleItems.get(0)[3] + " ");
//                        binding.matchContentTextView.append(scheduleItems.get(0)[4]);
//                        binding.matchTimePlaceTextView.setText(scheduleItems.get(0)[5]+" ");
//                        binding.matchTimePlaceTextView.append(scheduleItems.get(0)[6] + "체육관");
//                        binding.matchCameraTextView.setText(scheduleItems.get(0)[7]);
//                        binding.progressBar.setVisibility(ProgressBar.GONE);
//
//                    }
//                }else{
//                    Log.d(TAG, "경기 2개");
//
//                    //하루에 경기 2개인 경우 => secondGame[0] = ""  or scheduleItems.size() == 0일 때;;;
//                    binding.manTextView.setText(scheduleItems.get(0)[2]);
//                    binding.manTextView.append(" "+scheduleItems.get(0)[8]);    //남자부   V리그 6Round
//                    binding.matchContentTextView.setText(scheduleItems.get(0)[3] + " ");  //현대캐피탈 3
//                    binding.matchContentTextView.append(scheduleItems.get(0)[4]);   // : 2 한국전력
//                    binding.matchTimePlaceTextView.setText(scheduleItems.get(0)[5]+" ");    // 14:00
//                    binding.matchTimePlaceTextView.append(scheduleItems.get(0)[6] + "체육관");        //   천안유관순체육관
//                    binding.matchCameraTextView.setText(scheduleItems.get(0)[7]);       //   KBSNSports
//
//                    binding.womanTextView.setText(scheduleItems.get(1)[2]);
//                    binding.womanTextView.append(" "+scheduleItems.get(1)[8]);
//                    binding.matchContentTextView2.setText(scheduleItems.get(1)[3] + " ");
//                    binding.matchContentTextView2.append(scheduleItems.get(1)[4]);
//                    binding.matchTimePlaceTextView2.setText(scheduleItems.get(1)[5]+" ");
//                    binding.matchTimePlaceTextView2.append(scheduleItems.get(1)[6] + "체육관");
//                    binding.matchCameraTextView2.setText(scheduleItems.get(1)[7]);
//                    binding.progressBar.setVisibility(ProgressBar.GONE);
//
//                }
//            }
//        });
//ToDo: schedule data observe
        parsingModel.manScheduleLiveData.observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                binding.progressBar.setVisibility(ProgressBar.GONE);

                if(strings != null) {
                        String homeTeam = strings[0];
                        String awayTeam = strings[1];
                        String place = strings[2];
                        String round = strings[3];
                        String time = strings[4];

                        Date nowDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("M월 d일 (E)", Locale.KOREA);
                        String todayString = sdf.format(nowDate);
                        binding.manTextView.setText(todayString + " " + time);

                        binding.matchTimePlaceTextView.setText(place);
                        binding.matchRoundTextView.setText(round + "ound");
                        binding.matchContentTextView.setText(homeTeam + " vs " + awayTeam);

                        binding.matchTimePlaceTextView.setVisibility(View.VISIBLE);
                        binding.matchRoundTextView.setVisibility(View.VISIBLE);
                        binding.matchContentTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.manTextView.setText("남자부 경기가 없습니다.");

                    binding.matchTimePlaceTextView.setVisibility(View.GONE);
                    binding.matchRoundTextView.setVisibility(View.GONE);
                    binding.matchContentTextView.setVisibility(View.GONE);
                }
            }
        });

        parsingModel.womanScheduleLiveData.observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                binding.progressBar.setVisibility(ProgressBar.GONE);

                if(strings != null) {
                    String homeTeam = strings[0];
                    String awayTeam = strings[1];
                    String place = strings[2];
                    String round = strings[3];
                    String time = strings[4];

                    Date nowDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("M월 d일 (E)", Locale.KOREA);
                    String todayString = sdf.format(nowDate);
                    binding.womanTextView.setText(todayString + " " + time);

                    binding.matchTimePlaceTextView2.setText(place);
                    binding.matchRoundTextView2.setText(round + "ound");
                    binding.matchContentTextView2.setText(homeTeam + " vs " + awayTeam);
                    binding.matchTimePlaceTextView2.setVisibility(View.VISIBLE);
                    binding.matchRoundTextView2.setVisibility(View.VISIBLE);
                    binding.matchContentTextView2.setVisibility(View.VISIBLE);
                } else {
                    binding.womanTextView.setText("여자부 경기가 없습니다.");
                    binding.matchTimePlaceTextView2.setVisibility(View.GONE);
                    binding.matchRoundTextView2.setVisibility(View.GONE);
                    binding.matchContentTextView2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"onViewCreated 시작");
        parseNaverTv();
        parseNews();
        parseSchedule();
        setUpRecyclerView();
        //observe insta데이터
        observeRecyclerView();
        Log.d(TAG, "observeRecyclerView start!!");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate!!");
    }

    private String whatIsTeam() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext() /* Activity context */);
        String name = sharedPreferences.getString("favorite_team", "");

        return name;
    }

    private void findImage_url(String source) throws IOException {
        Log.d(TAG, "findImage_url() Start!");
        //Log.d(TAG, "findImage_url() 의 source = " + source);

        Document doc = Jsoup.parse(source);
        //Log.d(TAG, "Document doc = " + doc.toString());

        Elements elements = doc.select("div[class=v1Nh3 kIKUG  _bz0w]");
        //Log.d(TAG, "elements = " + elements.toString());

        String img_url;
        String page_url;
        int count = 0;
        for(Element element:elements){
            if(count == 5) break;
            img_url = element.select("img").attr("src");
            page_url = element.select("a").attr("href");
            Log.d(TAG, "img_url = " + img_url);
            mRecyclerItems.add(new InstaRecyclerItem(img_url, page_url));
            count++;
        }
        try{
            //LiveData활용해서 insta 파싱 데이터 보관!!
            instaLivedata.postValue(mRecyclerItems);
            Log.d(TAG, "라이브 데이터에 데이터넣었어!!");
            Log.d(TAG, "mRecyclerItems count = " + mRecyclerItems.size());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            //위 자바스크립트가 호출되면 여기로 html이 반환됨
            Log.d(TAG, "getHtml() start!!");
            try{

                findImage_url(html);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }*/

    private void observeRecyclerView() {
        instaLivedata.observe(getViewLifecycleOwner(), new Observer<List<InstaRecyclerItem>>() {
            @Override
            public void onChanged(List<InstaRecyclerItem> recyclerItems) {
                    Log.d(TAG,"observeRecyclerView Started & onChanged() Started");
                    recyclerAdapter.renewItems(recyclerItems);   //슬라이더뷰에 넣어
                    binding.recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    //코보마켓페이지 열기(배너 이미지 클릭시)
    public void showLinkPage(String link){
        Intent intent = new Intent(getContext(), NewsActivity.class);
        intent.putExtra("Link", link);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        Log.d(TAG, "setUpRecyclerView() Start!");

        if(recyclerAdapter == null) recyclerAdapter = new InstaRecyclerAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
    }
}