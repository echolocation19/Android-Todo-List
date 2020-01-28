package com.echolocation19.listapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    addPreferencesFromResource(R.xml.preferences);
    PreferenceScreen preferenceScreen = getPreferenceScreen();
    SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
    int count = preferenceScreen.getPreferenceCount();
    for (int i = 0; i < count; i++) {
      Preference preference = preferenceScreen.getPreference(i);
      String value = sharedPreferences.getString(preference.getKey(), "");
      setPreferenceSummary(preference, value);
    }
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference preference = findPreference(key);
    String value = sharedPreferences.getString(preference.getKey(), "");
    setPreferenceSummary(preference, value);
  }


  private void setPreferenceSummary(Preference preference, String value) {
    if (preference instanceof ListPreference) {
      ListPreference listPreference = (ListPreference) preference;
      int index = listPreference.findIndexOfValue(value);
      if (index >= 0) {
        listPreference.setSummary(listPreference.getEntries()[index]);
      }
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }
}
