package org.techtown.volleyball;

import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.volleyball.data.entity.NaverTVItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class NaverTvRepository {
    private static final String TAG = "NaverTvRepository";

    private final Executor executor;
    private final Handler resultHandler;

    public NaverTvRepository(Executor executor, Handler resultHandler) {
        this.executor = executor;
        this.resultHandler = resultHandler;
    }

    public void makeParsingRequest(
            final String parseUrl,
            final RepositoryCallback<List<NaverTVItem>> callback
    ){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, parseUrl);
                    Result<List<NaverTVItem>> resultResponse = makeSynchronousParsingRequest(parseUrl);
                    notifyResult(resultResponse, callback);
                    //callback.onComplete(resultResponse);
                } catch(Exception e) {
                    Log.d(TAG, parseUrl);
                    Result<List<NaverTVItem>> errorResult = new Result.Error<>(e);
                    notifyResult(errorResult, callback);
                    //callback.onComplete(errorResult);
                }
            }
        });
    }

    public Result<List<NaverTVItem>> makeSynchronousParsingRequest(String parseUrl){
        try{
            Log.d(TAG, "parsing start!!");

            List<NaverTVItem> parsingResponse = new ArrayList<>();
            String imgUrl = null;
            String naverUrl = null;

            Document doc = Jsoup.connect(parseUrl).get();
            //Log.d(TAG, doc.toString());

            Elements links = doc.select("div.video_thumbnail");
            //Log.d(TAG, links.toString());
            int count = 0;
            for (Element link : links) {
                if(count == 5) break;
                naverUrl = link.select("a.thumb_link").attr("href");
                imgUrl = link.select("img").attr("src");
                parsingResponse.add(new NaverTVItem(imgUrl, naverUrl));

                count++;
                //Log.d(TAG, "parsingResult = "+parsingResult);
            }

            return new Result.Success<List<NaverTVItem>>(parsingResponse);

        }catch(Exception e){
            return new Result.Error<List<NaverTVItem>>(e);
        }
    }

    private void notifyResult(
            final Result<List<NaverTVItem>> result,
            final RepositoryCallback<List<NaverTVItem>> callback
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "notifyResult Started!!");
                callback.onComplete(result);
            }
        });
    }

}
