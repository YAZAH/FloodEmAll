package axxel.floodit;

import android.util.Log;

import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

import java.util.Locale;

/**
 * Created by dominicroberts on 10/11/2016.
 */

public class TapDaqAdListener extends TMAdListener {

    @Override
    public void didRewardFail(TMAdError error) {
        super.didRewardFail(error);
        Log.i("MEDIATION-SAMPLE", "didRewardFail " + error.getErrorMessage());
    }

    @Override
    public void onUserDeclined() {
        Log.i("MEDIATION-SAMPLE", "onUserDeclined");
    }

    @Override
    public void didVerify(String s, String s1, Double aDouble) {
        Log.i("MEDIATION-SAMPLE", String.format(Locale.ENGLISH, "didVerify %s %s %.2f", s, s1, aDouble));
    }

    @Override
    public void didComplete() {
        Log.i("MEDIATION-SAMPLE", "didComplete");
    }

    @Override
    public void didEngagement() {
        Log.i("MEDIATION-SAMPLE", "didEngagement");
    }

    @Override
    public void didLoad() {
        Log.i("MEDIATION-SAMPLE", "didLoad");
    }

    @Override
    public void willDisplay() {
        Log.i("MEDIATION-SAMPLE", "willDisplay");
    }

    @Override
    public void didDisplay() {
        Log.i("MEDIATION-SAMPLE", "didDisplay");
    }

    @Override
    public void didClick() {
        Log.i("MEDIATION-SAMPLE", "didClick");
    }

    @Override
    public void didClose() {
        Log.i("MEDIATION-SAMPLE", "didClose");
    }

    @Override
    public void didFailToLoad(TMAdError tmAdError) {
        Log.i("MEDIATION-SAMPLE", "didFailToLoad " + tmAdError.getErrorMessage());
    }
}
