package axxel.floodit;

import android.util.Log;
import android.widget.Toast;

import com.tapdaq.sdk.listeners.TMAdListener;

/**
 * Created by Yassi on 2017-07-07.
 */

public class RewardedVideoListener extends TMAdListener {

    @Override
    public void didLoad() {
        // Rewarded video is loaded, enable your rewarded video button
    }

    @Override
    public void didVerify(String location, String reward, Double value) {
        //Log.i("TAPDAQ-REWARD", "The user received a reward! Name: {}, Amount: {}", reward, value);
    }

}