package org.techtown.volleyball;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.techtown.volleyball.data.entity.NaverTVItem;
import org.techtown.volleyball.data.entity.NewsItem;

import java.util.List;

public class ParsingModel extends AndroidViewModel {
    private static final String TAG = "ParsingModel";

    private final NaverTvRepository naverTvRepository;
    private final NewsRepository newsRepository;
    private final ScheduleRepository scheduleRepository;

    MutableLiveData<List<NaverTVItem>> parsingLivedata = new MutableLiveData<List<NaverTVItem>>();
    MutableLiveData<List<NewsItem>> parsingNewsLivedata = new MutableLiveData<>();

    MutableLiveData<String[]> manScheduleLiveData = new MutableLiveData<>();
    MutableLiveData<String[]> womanScheduleLiveData = new MutableLiveData<>();

    public ParsingModel(@NonNull Application application) {
        super(application);
        naverTvRepository = new NaverTvRepository (
                ((MyApplication)application).getExecutorService(),
                ((MyApplication)application).getMainThreadHandler()
        );

        newsRepository = new NewsRepository(
                ((MyApplication)application).getExecutorService(),
                ((MyApplication)application).getMainThreadHandler()
        );

        scheduleRepository = new ScheduleRepository(
                ((MyApplication)application).getExecutorService(),
                ((MyApplication)application).getMainThreadHandler()
        );
    }

    public void makeParsingRequest(String parsingUrl) {
        Log.d(TAG, "첫번째 " + parsingUrl);
        naverTvRepository.makeParsingRequest(parsingUrl, new RepositoryCallback<List<NaverTVItem>>() {
            @Override
            public void onComplete(Result<List<NaverTVItem>> result) {
                if(result instanceof Result.Success) {
                    Log.d(TAG, parsingUrl);

                    parsingLivedata.postValue(((Result.Success<List<NaverTVItem>>)result).data);

                    //Log.d(TAG, ((Result.Success<List<String>>) result).data);
                    Log.d(TAG, "성공이다");

                } else {
                    Log.d(TAG, parsingUrl);
                    Log.d(TAG, String.valueOf(((Result.Error<List<NaverTVItem>>) result).exception));
                    Log.d(TAG, "실패이다");
                }
            }
        });
    }

    public void makeNewsParsingRequest(String parsingUrl) {
        Log.d(TAG, "첫번째 뉴스 " + parsingUrl);
        newsRepository.makeParsingRequest(parsingUrl, new RepositoryCallback<List<NewsItem>>() {
            @Override
            public void onComplete(Result<List<NewsItem>> result) {
                if(result instanceof Result.Success){
                    Log.d(TAG, parsingUrl);

                    parsingNewsLivedata.postValue(((Result.Success<List<NewsItem>>)result).data);

                    //Log.d(TAG, ((Result.Success<List<String>>) result).data);
                    Log.d(TAG, "성공이다");

                }else{
                    Log.d(TAG, parsingUrl);
                    Log.d(TAG, String.valueOf(((Result.Error<List<NewsItem>>) result).exception));
                    Log.d(TAG, "실패이다");
                }
            }
        });
    }

    public void makeScheduleParsingRequest() {
        //남자 스케줄 파싱
        scheduleRepository.makeParsingRequest(1, new RepositoryCallback<String[]>(){
            @Override
            public void onComplete(Result<String[]> result) {
                if(result instanceof Result.Success) {
                    manScheduleLiveData.postValue(((Result.Success<String[]>) result).data);
                    Log.d(TAG, "Manschedule 성공이다");
                } else {
                    Log.d(TAG, String.valueOf(((Result.Error<String[]>) result).exception));
                    Log.d(TAG, "Manschedule 실패이다");
                }
            }
        });

        //여자 스케줄 파싱
        scheduleRepository.makeParsingRequest(0, new RepositoryCallback<String[]>() {
            @Override
            public void onComplete(Result<String[]> result) {
                if(result instanceof Result.Success) {
                    womanScheduleLiveData.postValue(((Result.Success<String[]>) result).data);
                    Log.d(TAG, "Womanschedule 성공이다");
                } else {
                    Log.d(TAG, String.valueOf(((Result.Error<String[]>) result).exception));
                    Log.d(TAG, "Womanschedule 실패이다");
                }
            }
        });
    }
}
