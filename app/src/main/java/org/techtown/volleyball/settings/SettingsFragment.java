package org.techtown.volleyball.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.techtown.volleyball.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    //SharedPreferences 리스너
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        ListPreference teamPreference = findPreference("favorite_team");

        //응원하는 팀 요약표시
        if (teamPreference != null) {
            teamPreference.setSummaryProvider(new Preference.SummaryProvider<ListPreference>() {
                @Override
                public CharSequence provideSummary(ListPreference preference) {
                    String text = String.valueOf(preference.getEntry());
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }

                    return text;
                }
            });
        }

        //리스너 설정
        prefListener =
                new SharedPreferences.OnSharedPreferenceChangeListener(){
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        if(key.equals("favorite_team")){

                            String name = sharedPreferences.getString("favorite_team", "");

                            Log.d("mystar", name);                        }
                    }
                };
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("mystar","onAttach!!");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("mystar","onResume!!");
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(prefListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("mystar","onPause!!");
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(prefListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("mystar","onStop!!");

    }
}