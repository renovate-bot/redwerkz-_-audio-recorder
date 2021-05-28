package com.wirehall.audiorecorder;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.wirehall.audiorecorder.AppRater.KEY_PREF_RATE_DIALOG_DO_NOT_SHOW;
import static com.wirehall.audiorecorder.AppRater.KEY_PREF_RATE_DIALOG_FIRST_LAUNCH_TIME;
import static com.wirehall.audiorecorder.AppRater.KEY_PREF_RATE_DIALOG_LAUNCH_COUNT;

public class AppRaterTest {
  @Rule
  public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE);

  private final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

  @Test
  public void testLaunch_rate_dialog() {
    setSharedPrefsForTest(context);

    ActivityScenario.launch(MainActivity.class);
    onView(withId(R.id.btn_rate_dialog_rate)).check(matches(isDisplayed()));
    onView(withId(R.id.btn_rate_dialog_remind_me_later)).check(matches(isDisplayed()));
    onView(withId(R.id.btn_rate_dialog_no_thx)).check(matches(isDisplayed()));

    resetSharedPrefs(context);
  }

  @Test
  @Ignore("There is no play store app on emulator. So rate action causes failure")
  public void testClick_rate_button() {
    setSharedPrefsForTest(context);

    ActivityScenario.launch(MainActivity.class);
    onView(withId(R.id.btn_rate_dialog_rate)).perform(click());
    onView(withId(R.id.btn_rate_dialog_no_thx)).check(doesNotExist());

    resetSharedPrefs(context);
  }

  @Test
  public void testClick_nt_button() {
    setSharedPrefsForTest(context);

    ActivityScenario.launch(MainActivity.class);
    onView(withId(R.id.btn_rate_dialog_no_thx)).perform(click());
    onView(withId(R.id.btn_rate_dialog_no_thx)).check(doesNotExist());

    resetSharedPrefs(context);
  }

  @Test
  public void testClick_rml_button() {
    setSharedPrefsForTest(context);

    ActivityScenario.launch(MainActivity.class);
    onView(withId(R.id.btn_rate_dialog_remind_me_later)).perform(click());
    onView(withId(R.id.btn_rate_dialog_no_thx)).check(doesNotExist());

    resetSharedPrefs(context);
  }

  private void setSharedPrefsForTest(Context context) {
    Date date = Date.from(Instant.now().minus(Duration.ofDays(4)));

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong(KEY_PREF_RATE_DIALOG_LAUNCH_COUNT, 6);
    editor.putLong(KEY_PREF_RATE_DIALOG_FIRST_LAUNCH_TIME, date.getTime());
    editor.putBoolean(KEY_PREF_RATE_DIALOG_DO_NOT_SHOW, false);
    editor.apply();
    editor.commit();
  }

  private void resetSharedPrefs(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong(KEY_PREF_RATE_DIALOG_LAUNCH_COUNT, 0);
    editor.putLong(KEY_PREF_RATE_DIALOG_FIRST_LAUNCH_TIME, Date.from(Instant.now()).getTime());
    editor.putBoolean(KEY_PREF_RATE_DIALOG_DO_NOT_SHOW, false);
    editor.apply();
    editor.commit();
  }
}
