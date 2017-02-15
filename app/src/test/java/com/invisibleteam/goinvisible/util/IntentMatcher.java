package com.invisibleteam.goinvisible.util;

import android.content.Intent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Set;

public class IntentMatcher {

    /**
     * This matcher checks if two intents contains the same Component
     * and the same extra keys with same values under Extras Bundle.
     *
     * @param resultIntent
     * @return
     */
    public static Matcher<Intent> containsSameData(final Intent resultIntent) {
        return new TypeSafeMatcher<Intent>() {

            @Override
            protected boolean matchesSafely(Intent baseIntent) {
                if (!baseIntent.getComponent().equals(resultIntent.getComponent())) {
                    return false;
                }
                if ((baseIntent.getExtras() == null && resultIntent.getExtras() != null)
                        || (baseIntent.getExtras() != null && resultIntent.getExtras() == null)) {
                    return false;
                }
                Set<String> baseIntentExtrasKeys = baseIntent.getExtras().keySet();
                Set<String> resultIntentExtrasKeys = resultIntent.getExtras().keySet();

                for (String baseExtraKey : baseIntentExtrasKeys) {
                    boolean containsKey = false;
                    for (String resultExtraKey : resultIntentExtrasKeys) {
                        if (baseExtraKey.equals(resultExtraKey)) {
                            containsKey = true;
                            Object baseExtraObject = baseIntent.getExtras().get(baseExtraKey);
                            Object resultExtraObject = resultIntent.getExtras().get(resultExtraKey);
                            if (baseExtraObject != resultExtraObject) {
                                return false;
                            }
                            break;
                        }
                    }
                    if (!containsKey) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue("Intents are not the same");
            }
        };
    }
}
