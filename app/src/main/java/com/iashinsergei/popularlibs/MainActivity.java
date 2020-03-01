package com.iashinsergei.popularlibs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView textView;
    Long counter = 60L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1)
                .take(61);

        observable.safeSubscribe(observer);
    }

    Observer<Long> observer = new Observer<Long>() {
        @Override
        public void onSubscribe(Disposable d) { }
        @SuppressLint("SetTextI18n")
        @Override
        public void onNext(Long l) {
            Log.i(TAG, "onNext: " + l);
            textView.setText(String.valueOf(counter - l));
        }
        @Override
        public void onError(Throwable e) {
            Log.i(TAG,"onError: " + e);
        }
        @Override
        public void onComplete() {
            Log.i(TAG,"onCompleted");
            textView.setText("complete");
        }
    };

    List<String> getStrings(){
        return Arrays.asList("a", "b", "c", "d");
    }

    Function<Long, Long> mapFunction = new Function<Long, Long>() {
        @Override
        public Long apply(Long num) throws Exception {
            return num * 2;
        }
    };

    Function<Long, Boolean> filterFunction = new Function<Long, Boolean>() {
        @Override
        public Boolean apply(Long num) throws Exception {
            return num % 2 == 0;
        }
    };
}
