package axxel.floodit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Dialog;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tapdaq.sdk.*;
import com.tapdaq.sdk.ads.*;
import com.tapdaq.sdk.common.TMBannerAdSizes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tappx.sdk.android.TappxBanner;

import static axxel.floodit.R.layout.activity_main;


public class MainActivity extends AppCompatActivity {
    TMBannerAdView ad;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Dialog dialog;

    private boolean levelMode;

    private boolean addShowed;

    private int number=1;
    private GridView getGrid;
    private TextView getText;
    private TextView getMode;

    private String[] levelDone = new String[120];

    private Slider slider = null;

    private Button undo = null;
    private Button redo = null;
    private Button settings = null;

    /**
     * Reference to the model of the game
     */
    private GameModel gameModel;

    /**
     * boolean to choose the game mode
     */

    private boolean OrthoPlane = true;
    private boolean OrthoTorus;
    private boolean DiagoPlane;
    private boolean DiagoTorus;

    /**
     * Stack that contains GameModel objects
     */

    private Stack<GameModel> undoStack;
    private Stack<GameModel> redoStack;

    private int bestOrthoPlaneS;
    private int bestOrthoTorusS;
    private int bestDiagoPlaneS;
    private int bestDiagoTorusS;

    private int bestOrthoPlaneM;
    private int bestOrthoTorusM;
    private int bestDiagoPlaneM;
    private int bestDiagoTorusM;

    private int bestOrthoPlaneL;
    private int bestOrthoTorusL;
    private int bestDiagoPlaneL;
    private int bestDiagoTorusL;

    private void instruction() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.instruclayout);

        TextView instruc1 = (TextView) dialog.findViewById(R.id.instructions1);
        instruc1.setText(getString(R.string.instructions1));

        TextView instruc = (TextView) dialog.findViewById(R.id.instructions);
        instruc.setText(getString(R.string.instructions));
        dialog.show();
    }

    private Drawable getDot(int a, int b) {
        gameModel.getColor(a, b);
        Drawable retour = null;
        Resources resources = getResources();

        if (gameModel.getColor(a, b) == 0)
            retour = ResourcesCompat.getDrawable(resources, R.drawable.blue, null);
        else if (gameModel.getColor(a, b) == 1)
            retour = ResourcesCompat.getDrawable(resources, R.drawable.yellow, null);
        else if (gameModel.getColor(a, b) == 2)
            retour = ResourcesCompat.getDrawable(resources, R.drawable.red, null);
        else if (gameModel.getColor(a, b) == 3)
            retour = ResourcesCompat.getDrawable(resources, R.drawable.green, null);
        else if (gameModel.getColor(a, b) == 4)
            retour = ResourcesCompat.getDrawable(resources, R.drawable.violet, null);
        else if (gameModel.getColor(a, b) == 5)
            retour = ResourcesCompat.getDrawable(resources, R.drawable.grey, null);
        return retour;
    }

    private void youWon() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.wonlayout);
        dialog.show();
    }

    private void youLose() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loselayout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button retry = (Button) dialog.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                levelChoice();
            }
        });
        Button realret = (Button) dialog.findViewById(R.id.realretry);
        realret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startLevelMode(getModel().getSize(), getModel().getLevelOfGame(), getModel().getMode());
            }
        });

        Button showad = (Button) dialog.findViewById(R.id.showad);
        if (Tapdaq.getInstance().isRewardedVideoReady(this, "my_rewarded_tag") && !addShowed) {
            showad.setVisibility(View.VISIBLE);
        } else {
            showad.setVisibility(View.INVISIBLE);
        }
        showad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tapdaq.getInstance().showRewardedVideo(MainActivity.this, "my_rewarded_tag", new RewardedVideoListener());
                getModel().setNumberOfSteps();
                addShowed = true;
                dialog.dismiss();
                updateText(getText);
            }
        });
        dialog.show();
    }

    private void levelChoice() {
        setContentView(R.layout.levelchoice);
        final RadioGroup gameMode1 = (RadioGroup) findViewById(R.id.mode1);
        final RelativeLayout l1 = (RelativeLayout) findViewById(R.id.layout1);
        final RelativeLayout l2 = (RelativeLayout) findViewById(R.id.layout2);
        final RelativeLayout l3 = (RelativeLayout) findViewById(R.id.layout3);
        final RelativeLayout l4 = (RelativeLayout) findViewById(R.id.layout4);
        if (OrthoPlane) {
            gameMode1.check(R.id.OrthoPlane);
            l1.setVisibility(RelativeLayout.VISIBLE);
        } else if (OrthoTorus) {
            gameMode1.check(R.id.OrthoTorus);
            l2.setVisibility(RelativeLayout.VISIBLE);
        } else if (DiagoPlane) {
            gameMode1.check(R.id.DiagoPlane);
            l3.setVisibility(RelativeLayout.VISIBLE);
        } else if (DiagoTorus) {
            gameMode1.check(R.id.DiagoTorus);
            l4.setVisibility(RelativeLayout.VISIBLE);
        }
        gameMode1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.OrthoPlane:
                        OrthoPlane = true;
                        OrthoTorus = false;
                        DiagoPlane = false;
                        DiagoTorus = false;
                        l1.setVisibility(RelativeLayout.VISIBLE);
                        l2.setVisibility(RelativeLayout.GONE);
                        l3.setVisibility(RelativeLayout.GONE);
                        l4.setVisibility(RelativeLayout.GONE);
                        break;
                    case R.id.OrthoTorus:
                        OrthoPlane = false;
                        OrthoTorus = true;
                        DiagoPlane = false;
                        DiagoTorus = false;
                        l1.setVisibility(RelativeLayout.GONE);
                        l2.setVisibility(RelativeLayout.VISIBLE);
                        l3.setVisibility(RelativeLayout.GONE);
                        l4.setVisibility(RelativeLayout.GONE);
                        break;
                    case R.id.DiagoPlane:
                        OrthoPlane = false;
                        OrthoTorus = false;
                        DiagoPlane = true;
                        DiagoTorus = false;
                        l1.setVisibility(RelativeLayout.GONE);
                        l2.setVisibility(RelativeLayout.GONE);
                        l3.setVisibility(RelativeLayout.VISIBLE);
                        l4.setVisibility(RelativeLayout.GONE);
                        break;
                    case R.id.DiagoTorus:
                        OrthoPlane = false;
                        OrthoTorus = false;
                        DiagoPlane = false;
                        DiagoTorus = true;
                        l1.setVisibility(RelativeLayout.GONE);
                        l2.setVisibility(RelativeLayout.GONE);
                        l3.setVisibility(RelativeLayout.GONE);
                        l4.setVisibility(RelativeLayout.VISIBLE);
                        break;
                }
            }
        });

        maj();
    }

    private void updateView(GridView g) {
        g.setAdapter(new ImageAdapter(this));
    }

    private void updateText(TextView t) {
        if (OrthoPlane) {getMode.setText(R.string.orthoplane);}
        else if (DiagoPlane) {getMode.setText(R.string.diagoplane);}
        else if (OrthoTorus) {getMode.setText(R.string.orthotorus);}
        else if (DiagoTorus) {getMode.setText(R.string.diagotorus);}
        if (getModel().getNumberOfSteps()==0) {
            t.setText(R.string.start);
        }
        else {
            t.setText(getString(R.string.numstep) + getModel().getNumberOfSteps());
            if (levelMode) {
                t.setText(getString(R.string.numstep) + (getModel().getNumberOfSteps())+"\n"+
                        "MAXIMUM: " + (getModel().getNumbStepsMax()) + getString(R.string.step));
            }
        }
    }

    private void updateLevelT(TextView t, int a, String b) {
        t.setText("/LVL: " + b + "-" + a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstlayout);
        loadStats(getApplicationContext());
        load(getApplicationContext());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //cle();
        homePage();
    }

    private void homePage() {
        Button newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridChoice();
            }
        });
        Button button1 = (Button) findViewById(R.id.levelChoice);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelChoice();
            }
        });
        Button button2 = (Button) findViewById(R.id.instructions);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instruction();
            }
        });
        Button button3 = (Button) findViewById(R.id.discover);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marketIntentBlox = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=axxel.bloxorz"));
                startActivity(marketIntentBlox);
            }
        });
        Button button4 = (Button) findViewById(R.id.rate);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=axxel.floodit"));
                startActivity(marketIntent);
            }
        });
        Button button5 = (Button) findViewById(R.id.QuitB);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     *
     * @param color the newly selected color
     */
    private void selectColor(int color){
        undoStack.push(gameModel.clone());
        undo.setEnabled(canUndo());
        redo.setEnabled(canRedo());
        if(color != gameModel.getCurrentSelectedColor()) {
            gameModel.setCurrentSelectedColor(color);
            if ( OrthoPlane) {planeOrthoMode();}
            else if (DiagoPlane) {planeDiagoMode();}
            else if (OrthoTorus) {torusOrthoMode();}
            else if (DiagoTorus) {torusDiagoMode();}
            gameModel.step();
            updateView(getGrid);
            updateText(getText);
            if(gameModel.gameOver() && !gameModel.isFinished()){
                youLose();
            }
            if(gameModel.isFinished()) {
                if (levelMode) {
                    getCurrentSelectedLevel();
                    save();
                    levelChoice();
                    youWon();
                }
                else {
                    if (OrthoPlane) {
                        if (gameModel.getSize()==10 && gameModel.getNumberOfSteps()<bestOrthoPlaneS){
                            bestOrthoPlaneS=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==20 && gameModel.getNumberOfSteps()<bestOrthoPlaneM){
                            bestOrthoPlaneM=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==40 && gameModel.getNumberOfSteps()<bestOrthoPlaneL){
                            bestOrthoPlaneL=gameModel.getNumberOfSteps();
                        }
                    }
                    if (OrthoTorus) {
                        if (gameModel.getSize()==10 && gameModel.getNumberOfSteps()<bestOrthoTorusS){
                            bestOrthoTorusS=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==20 && gameModel.getNumberOfSteps()<bestOrthoTorusM){
                            bestOrthoTorusM=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==40 && gameModel.getNumberOfSteps()<bestOrthoTorusL){
                            bestOrthoTorusL=gameModel.getNumberOfSteps();
                        }
                    }
                    if (DiagoPlane) {
                        if (gameModel.getSize()==10 && gameModel.getNumberOfSteps()<bestDiagoPlaneS){
                            bestDiagoPlaneS=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==20 && gameModel.getNumberOfSteps()<bestDiagoPlaneM){
                            bestDiagoPlaneM=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==40 && gameModel.getNumberOfSteps()<bestDiagoPlaneL){
                            bestDiagoPlaneL=gameModel.getNumberOfSteps();
                        }
                    }
                    if (DiagoTorus) {
                        if (gameModel.getSize()==10 && gameModel.getNumberOfSteps()<bestDiagoTorusS){
                            bestDiagoTorusS=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==20 && gameModel.getNumberOfSteps()<bestDiagoTorusM){
                            bestDiagoTorusM=gameModel.getNumberOfSteps();
                        }
                        else if (gameModel.getSize()==40 && gameModel.getNumberOfSteps()<bestDiagoTorusL){
                            bestDiagoTorusL=gameModel.getNumberOfSteps();
                        }
                    }
                    saveStats("bestOrthoPlaneS",bestOrthoPlaneS);
                    saveStats("bestOrthoPlaneM",bestOrthoPlaneM);
                    saveStats("bestOrthoPlaneL",bestOrthoPlaneL);

                    saveStats("bestOrthoTorusS",bestOrthoTorusS);
                    saveStats("bestOrthoTorusM",bestOrthoTorusM);
                    saveStats("bestOrthoTorusL",bestOrthoTorusL);

                    saveStats("bestDiagoPlaneS",bestDiagoPlaneS);
                    saveStats("bestDiagoPlaneM",bestDiagoPlaneM);
                    saveStats("bestDiagoPlaneL",bestDiagoPlaneL);

                    saveStats("bestDiagoTorusS",bestDiagoTorusS);
                    saveStats("bestDiagoTorusM",bestDiagoTorusM);
                    saveStats("bestDiagoTorusL",bestDiagoTorusL);
                    number++;
                    if (number%3==0){
                        //Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
                        //interstitial.show();
                        //mInterstitialAd.show();
                        Tapdaq.getInstance().showInterstitial(this, "my_interstitial_tag", new InterstitialListener());
                    }
                    youWonRandom();
                }
            }
        }
    }

    /**
     * <b>shouldBeCaptured</b> is a helper method that decides if the dot
     * located at position (i,j), which is next to a captured dot, should
     * itself be captured
     *
     * @param i row of the dot
     * @param j column of the dot
     */
    private boolean shouldBeCaptured(int i, int j) {
        return (!gameModel.isCaptured(i, j) && (gameModel.getColor(i, j) == gameModel.getCurrentSelectedColor()));
    }

    /**
     * <b>planeOrthoMode</b> is the method that computes which new dots should be ''captured''
     * in plane ortho mode
     * when a new color has been selected. The Model is updated accordingly
     */
    private void planeOrthoMode() {
        Stack<DotInfo> stack = new GenericLinkedStack<>();
        for (int i = 0; i < gameModel.getSize(); i++) {
            for (int j = 0; j < gameModel.getSize(); j++) {
                if (gameModel.isCaptured(i, j)) {
                    stack.push(gameModel.get(i, j));
                }
            }
        }

        while (!stack.isEmpty()) {
            DotInfo DotInfo = stack.pop();
            if ((DotInfo.getX() > 0) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY()));
            }
            if ((DotInfo.getX() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY()));
            }
            if ((DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() - 1));
            }
            if ((DotInfo.getY() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() + 1));
            }
        }
    }

    /**
     * <b>torusOrthoMode</b> is the method that computes which new dots should be ''captured''
     * in torus ortho mode
     * when a new color has been selected. The Model is updated accordingly
     */
    private void torusOrthoMode() {
        Stack<DotInfo> stack = new GenericLinkedStack<>();
        for (int i = 0; i < gameModel.getSize(); i++) {
            for (int j = 0; j < gameModel.getSize(); j++) {
                if (gameModel.isCaptured(i, j)) {
                    stack.push(gameModel.get(i, j));
                }
            }
        }

        while (!stack.isEmpty()) {
            DotInfo DotInfo = stack.pop();
            if ((DotInfo.getX() > 0) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY()));
            }
            if ((DotInfo.getX() == 0) && shouldBeCaptured(gameModel.getSize() - 1, DotInfo.getY())) {
                gameModel.capture(gameModel.getSize() - 1, DotInfo.getY());
                stack.push(gameModel.get(gameModel.getSize() - 1, DotInfo.getY()));
            }

            if ((DotInfo.getX() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY()));
            }

            if ((DotInfo.getX() == gameModel.getSize() - 1) && shouldBeCaptured(0, DotInfo.getY())) {
                gameModel.capture(0, DotInfo.getY());
                stack.push(gameModel.get(0, DotInfo.getY()));
            }

            if ((DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() - 1));
            }
            if ((DotInfo.getY() == 0) && shouldBeCaptured(DotInfo.getX(), gameModel.getSize() - 1)) {
                gameModel.capture(DotInfo.getX(), gameModel.getSize() - 1);
                stack.push(gameModel.get(DotInfo.getX(), gameModel.getSize() - 1));
            }
            if ((DotInfo.getY() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() + 1));
            }
            if ((DotInfo.getY() == gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX(), 0)) {
                gameModel.capture(DotInfo.getX(), 0);
                stack.push(gameModel.get(DotInfo.getX(), 0));
            }
        }
    }

    /**
     * <b>planeDiagoMode</b> is the method that computes which new dots should be ''captured''
     * in plane diago mode
     * when a new color has been selected. The Model is updated accordingly
     */
    private void planeDiagoMode() {
        Stack<DotInfo> stack = new GenericLinkedStack<>();
        for (int i = 0; i < gameModel.getSize(); i++) {
            for (int j = 0; j < gameModel.getSize(); j++) {
                if (gameModel.isCaptured(i, j)) {
                    stack.push(gameModel.get(i, j));
                }
            }
        }
        while (!stack.isEmpty()) {
            DotInfo DotInfo = stack.pop();
            if ((DotInfo.getX() > 0) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY()));
            }
            if ((DotInfo.getX() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY()));
            }
            if ((DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() - 1));
            }
            if ((DotInfo.getY() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() + 1));
            }
            if ((DotInfo.getX() > 0) && (DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY() - 1));
            }
            if ((DotInfo.getY() < gameModel.getSize() - 1) && (DotInfo.getX() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY() + 1));
            }
            if ((DotInfo.getX() > 0) && (DotInfo.getY() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY() + 1));
            }
            if ((DotInfo.getX() < gameModel.getSize() - 1) && (DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY() - 1));
            }
        }
    }

    /**
     * <b>torusDiagoMode</b> is the method that computes which new dots should be ''captured''
     * in torus diago mode
     * when a new color has been selected. The Model is updated accordingly
     */
    private void torusDiagoMode() {

        Stack<DotInfo> stack = new GenericLinkedStack<>();
        for (int i = 0; i < gameModel.getSize(); i++) {
            for (int j = 0; j < gameModel.getSize(); j++) {
                if (gameModel.isCaptured(i, j)) {
                    stack.push(gameModel.get(i, j));
                }
            }
        }
        while (!stack.isEmpty()) {
            DotInfo DotInfo = stack.pop();
            if ((DotInfo.getX() > 0) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY()));
            }
            if ((DotInfo.getX() == 0) && shouldBeCaptured(gameModel.getSize() - 1, DotInfo.getY())) {
                gameModel.capture(gameModel.getSize() - 1, DotInfo.getY());
                stack.push(gameModel.get(gameModel.getSize() - 1, DotInfo.getY()));
            }
            if ((DotInfo.getX() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY()));
            }
            if ((DotInfo.getX() == gameModel.getSize() - 1) && shouldBeCaptured(0, DotInfo.getY())) {
                gameModel.capture(0, DotInfo.getY());
                stack.push(gameModel.get(0, DotInfo.getY()));
            }
            if ((DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() - 1));
            }
            if ((DotInfo.getY() == 0) && shouldBeCaptured(DotInfo.getX(), gameModel.getSize() - 1)) {
                gameModel.capture(DotInfo.getX(), gameModel.getSize() - 1);
                stack.push(gameModel.get(DotInfo.getX(), gameModel.getSize() - 1));
            }
            if ((DotInfo.getY() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX(), DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY() + 1));
            }
            if ((DotInfo.getY() == gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX(), 0)) {
                gameModel.capture(DotInfo.getX(), 0);
                stack.push(gameModel.get(DotInfo.getX(), 0));
            }
            if ((DotInfo.getX() > 0) && (DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY() - 1));
            }
            if ((DotInfo.getX() == 0) && (DotInfo.getY() == 0) && shouldBeCaptured(gameModel.getSize() - 1, gameModel.getSize() - 1)) {
                gameModel.capture(gameModel.getSize() - 1, gameModel.getSize() - 1);
                stack.push(gameModel.get(gameModel.getSize() - 1, gameModel.getSize() - 1));
            }
            if ((DotInfo.getY() < gameModel.getSize() - 1) && (DotInfo.getX() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY() + 1));
            }
            if ((DotInfo.getY() == gameModel.getSize() - 1) && (DotInfo.getX() == gameModel.getSize() - 1) && shouldBeCaptured(0, 0)) {
                gameModel.capture(0, 0);
                stack.push(gameModel.get(0, 0));
            }
            if ((DotInfo.getX() > 0) && (DotInfo.getY() < gameModel.getSize() - 1) && shouldBeCaptured(DotInfo.getX() - 1, DotInfo.getY() + 1)) {
                gameModel.capture(DotInfo.getX() - 1, DotInfo.getY() + 1);
                stack.push(gameModel.get(DotInfo.getX() - 1, DotInfo.getY() + 1));
            }
            if ((DotInfo.getX() == 0) && (DotInfo.getY() == gameModel.getSize() - 1) && shouldBeCaptured(gameModel.getSize() - 1, 0)) {
                gameModel.capture(gameModel.getSize() - 1, 0);
                stack.push(gameModel.get(gameModel.getSize() - 1, 0));
            }
            if ((DotInfo.getX() < gameModel.getSize() - 1) && (DotInfo.getY() > 0) && shouldBeCaptured(DotInfo.getX() + 1, DotInfo.getY() - 1)) {
                gameModel.capture(DotInfo.getX() + 1, DotInfo.getY() - 1);
                stack.push(gameModel.get(DotInfo.getX() + 1, DotInfo.getY() - 1));
            }
            if ((DotInfo.getX() == gameModel.getSize() - 1) && (DotInfo.getY() == 0) && shouldBeCaptured(0, gameModel.getSize() - 1)) {
                gameModel.capture(0, gameModel.getSize() - 1);
                stack.push(gameModel.get(0, gameModel.getSize() - 1));
            }
        }
    }

    /**
     * <b>undo</b> is the method erases the last change done to the Model object and reverting it to an older state
     */
    private void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(gameModel);
            gameModel = undoStack.peek();
            undoStack.pop();
        }
    }

    /**
     * <b>redo</b> is the method erases the last undo done to the Model object and reverting it to a newer state
     */
    private void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(gameModel);
            gameModel = redoStack.peek();
            redoStack.pop();
        }
    }

    /**
     * resets the game
     * */
    private void reset(){
        if (levelMode) {
            levelChoice();
        }
        else {gridChoice();}
        if ( OrthoPlane) {planeOrthoMode();}
        else if (DiagoPlane) {planeDiagoMode();}
        else if (OrthoTorus) {torusOrthoMode();}
        else if (DiagoTorus) {torusDiagoMode();}
        updateView(getGrid);
        updateText(getText);
    }

    private void startLevelMode(int size, int level, int mode) {
        levelMode = true;
        addShowed = false;
        gameModel = new GameModel(size, level, mode);
        if (OrthoPlane) {
            planeOrthoMode();
        } else if (DiagoPlane) {
            planeDiagoMode();
        } else if (OrthoTorus) {
            torusOrthoMode();
        } else if (DiagoTorus) {
            torusDiagoMode();
        }
        undoStack = new GenericLinkedStack<>();
        redoStack = new GenericLinkedStack<>();
        realStart(size, level);
    }

    private void realStart(int size, int level){
        setContentView(activity_main);

        List<TapdaqPlacement> enabledPlacements = new ArrayList<TapdaqPlacement>();
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.INTERSTITIAL_PORTRAIT, CreativeType.INTERSTITIAL_LANDSCAPE), "my_interstitial_tag"));
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.REWARDED_VIDEO_INTERSTITIAL), "my_rewarded_tag"));
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.BANNER), "banner"));

        TapdaqConfig config = new TapdaqConfig(this);
        config.setAutoReloadAds(true);
        config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

        // Required for AdMob
        Tapdaq.getInstance().registerAdapter(this, new TMAdMobAdapter(this));
        // Required for Facebook Audience Network
        Tapdaq.getInstance().registerAdapter(this, new TMFacebookAdapter(this));
        // Required for UnityAds
        Tapdaq.getInstance().registerAdapter(this, new TMUnityAdsAdapter(this));
        // Required for AdColony
        Tapdaq.getInstance().registerAdapter(this, new TMAdColonyAdapter(this));
        // Required for Applovin
        Tapdaq.getInstance().registerAdapter(this, new TMAppLovinAdapter(this));
        // Required for IronSource
        Tapdaq.getInstance().registerAdapter(this, new TMIronSourceAdapter(this));
        // Required for TapJoy
        Tapdaq.getInstance().registerAdapter(this, new TMTapjoyAdapter(this));
        // Required for Chartboost
        Tapdaq.getInstance().registerAdapter(this, new TMChartboostAdapter(this));



        Tapdaq.getInstance().initialize(this, "test", "test", config, new TapdaqInitListener());
        Tapdaq.getInstance().loadInterstitial(this,  "my_interstitial_tag", new InterstitialListener());
        Tapdaq.getInstance().loadRewardedVideo(this,  "my_rewarded_tag", new RewardedVideoListener());

        ad = (TMBannerAdView) findViewById(R.id.adView);

        ad.load(this, TMBannerAdSizes.STANDARD, new TapDaqAdListener());

        LinearLayout bannerContainer = (LinearLayout) findViewById(R.id.tapAd);

        Context context = this;
        TappxBanner banner = new TappxBanner(context, "test");
        banner.setAdSize(TappxBanner.AdSize.SMART_BANNER);
        bannerContainer.addView(banner);
        banner.loadAd();
        banner.setRefreshTimeSeconds(10);

        //Tapdaq.getInstance().startTestActivity(MainActivity.this);


        final TextView textV = (TextView) findViewById(R.id.textV);
        final TextView modeV = (TextView) findViewById(R.id.modeV);
        final GridView gridV = (GridView) findViewById(R.id.gridV);
        final TextView levelV = (TextView) findViewById(R.id.levelV);


        gridV.setAdapter(new ImageAdapter(this));
        gridV.setNumColumns(gameModel.getSize());

        getGrid= gridV;
        getText=textV;
        getMode=modeV;
        updateText(getText);

        if (levelMode) {
            if (size==10) {
                updateLevelT(levelV, level, "S");
            }
            else if (size ==20) {
                updateLevelT(levelV, level, "M");
            }
            else if (size ==40) {
                updateLevelT(levelV, level, "L");
            }
        }

        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                if(slider.toggle()) {
                    settings.setText(R.string.hideMenu);
                }else {
                    settings.setText(R.string.options);
                }
            }
        });


        Button returnBut = (Button) findViewById(R.id.returnB);
        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                MainActivity.this.recreate();
            }
        });

        Button instr = (Button) findViewById(R.id.instruc);
        instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                instruction();
            }
        });

        RelativeLayout toHide = (RelativeLayout) findViewById(R.id.toHide);
        slider = (Slider) findViewById(R.id.slider);
        slider.setToHide(toHide);


        ImageButton blue = (ImageButton) findViewById(R.id.blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectColor(0);}});

        ImageButton yellow = (ImageButton) findViewById(R.id.yellow);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(1);    }
        });

        ImageButton red = (ImageButton) findViewById(R.id.red);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(2);  }
        });

        ImageButton green = (ImageButton) findViewById(R.id.green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(3);    }
        });

        ImageButton violet = (ImageButton) findViewById(R.id.violet);
        violet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(4);    }
        });

        ImageButton grey = (ImageButton) findViewById(R.id.grey);
        grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(5);    }
        });
        undo = (Button) findViewById(R.id.undo);
        undo.setEnabled(canUndo());
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                undo();
                updateView(getGrid);
                updateText(getText);
                undo.setEnabled(canUndo());
                redo.setEnabled(canRedo());
            }
        });

        redo = (Button) findViewById(R.id.redo);
        redo.setEnabled(canRedo());
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                redo();
                updateView(getGrid);
                updateText(getText);
                undo.setEnabled(canUndo());
                redo.setEnabled(canRedo());
            }
        });

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                reset();
                undo.setEnabled(canUndo());
                redo.setEnabled(canRedo());
            }
        });
    }

    private boolean canUndo() {
        return !undoStack.isEmpty();
    }

    private boolean canRedo() {
        return !redoStack.isEmpty();
    }

    private GameModel getModel() {
        return this.gameModel;
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        ImageAdapter(Context c) {
            mContext = c;
            int count = 0;
            for (int i = 0; i < gameModel.getSize(); i++) {
                for (int j = 0; j < gameModel.getSize(); j++) {
                    mThumbIds[count] = getDot(i, j);
                    count++;
                }
            }
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int screenWidth = metrics.widthPixels;
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / gameModel.getSize(), screenWidth / gameModel.getSize()));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageDrawable(mThumbIds[position]);
            return imageView;
        }

        private Drawable[] mThumbIds = new Drawable[gameModel.getSize() * gameModel.getSize()];
    }

    private void getCurrentSelectedLevel() {
        for (int i = 0; i < 120; i++) {
            if (levelDone[i] == null) {
                levelDone[i] = gameModel.getLevel();
                break;
            } else if (levelDone[i] == gameModel.getLevel()) {
                break;
            }
        }
    }

    private void save() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* levelDone is an array */
        mEdit1.putInt("Status_size", levelDone.length);

        for (int i = 0; i < levelDone.length; i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, levelDone[i]);
        }
        mEdit1.apply();
    }

    private void load(Context mContext) {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
        int size = mSharedPreference1.getInt("Status_size", 0);
        for(int i=0;i<size;i++)
        {
            levelDone[i]=mSharedPreference1.getString("Status_" + i, null);
        }

    }

    private void cle() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();
    }
    @Override
    public void onBackPressed() {
        MainActivity.this.recreate();
    }

    public void onButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.returnToMenu:
                MainActivity.this.recreate();
                break;
            case R.id.Level1S:
                startLevelMode(10, 1, 0);
                break;
            case R.id.Level2S:
                startLevelMode(10, 2, 0);
                break;
            case R.id.Level3S:
                startLevelMode(10, 3, 0);
                break;
            case R.id.Level4S:
                startLevelMode(10, 4, 0);
                break;
            case R.id.Level5S:
                startLevelMode(10, 5, 0);
                break;
            case R.id.Level6S:
                startLevelMode(10, 6, 0);
                break;
            case R.id.Level7S:
                startLevelMode(10, 7, 0);
                break;
            case R.id.Level8S:
                startLevelMode(10, 8, 0);
                break;
            case R.id.Level9S:
                startLevelMode(10, 9, 0);
                break;
            case R.id.Level10S:
                startLevelMode(10, 10, 0);
                break;
            case R.id.Level1M:
                startLevelMode(20, 1, 0);
                break;
            case R.id.Level2M:
                startLevelMode(20, 2, 0);
                break;
            case R.id.Level3M:
                startLevelMode(20, 3, 0);
                break;
            case R.id.Level4M:
                startLevelMode(20, 4, 0);
                break;
            case R.id.Level5M:
                startLevelMode(20, 5, 0);
                break;
            case R.id.Level6M:
                startLevelMode(20, 6, 0);
                break;
            case R.id.Level7M:
                startLevelMode(20, 7, 0);
                break;
            case R.id.Level8M:
                startLevelMode(20, 8, 0);
                break;
            case R.id.Level9M:
                startLevelMode(20, 9, 0);
                break;
            case R.id.Level10M:
                startLevelMode(20, 10, 0);
                break;
            case R.id.Level1L:
                startLevelMode(40,1,0);
                break;
            case R.id.Level2L:
                startLevelMode(40,2,0);
                break;
            case R.id.Level3L:
                startLevelMode(40,3,0);
                break;
            case R.id.Level4L:
                startLevelMode(40,4,0);
                break;
            case R.id.Level5L:
                startLevelMode(40,5,0);
                break;
            case R.id.Level6L:
                startLevelMode(40,6,0);
                break;
            case R.id.Level7L:
                startLevelMode(40,7,0);
                break;
            case R.id.Level8L:
                startLevelMode(40,8,0);
                break;
            case R.id.Level9L:
                startLevelMode(40,9,0);
                break;
            case R.id.Level10L:
                startLevelMode(40,10,0);
                break;

            case R.id.Level1Sdeux:
                startLevelMode(10, 1, 1);
                break;
            case R.id.Level2Sdeux:
                startLevelMode(10, 2, 1);
                break;
            case R.id.Level3Sdeux:
                startLevelMode(10, 3, 1);
                break;
            case R.id.Level4Sdeux:
                startLevelMode(10, 4, 1);
                break;
            case R.id.Level5Sdeux:
                startLevelMode(10, 5, 1);
                break;
            case R.id.Level6Sdeux:
                startLevelMode(10, 6, 1);
                break;
            case R.id.Level7Sdeux:
                startLevelMode(10, 7, 1);
                break;
            case R.id.Level8Sdeux:
                startLevelMode(10, 8, 1);
                break;
            case R.id.Level9Sdeux:
                startLevelMode(10, 9, 1);
                break;
            case R.id.Level10Sdeux:
                startLevelMode(10, 10, 1);
                break;
            case R.id.Level1Mdeux:
                startLevelMode(20, 1, 1);
                break;
            case R.id.Level2Mdeux:
                startLevelMode(20, 2, 1);
                break;
            case R.id.Level3Mdeux:
                startLevelMode(20, 3, 1);
                break;
            case R.id.Level4Mdeux:
                startLevelMode(20, 4, 1);
                break;
            case R.id.Level5Mdeux:
                startLevelMode(20, 5, 1);
                break;
            case R.id.Level6Mdeux:
                startLevelMode(20, 6, 1);
                break;
            case R.id.Level7Mdeux:
                startLevelMode(20, 7, 1);
                break;
            case R.id.Level8Mdeux:
                startLevelMode(20, 8, 1);
                break;
            case R.id.Level9Mdeux:
                startLevelMode(20, 9, 1);
                break;
            case R.id.Level10Mdeux:
                startLevelMode(20, 10, 1);
                break;
            case R.id.Level1Ldeux:
                startLevelMode(40,1,1);
                break;
            case R.id.Level2Ldeux:
                startLevelMode(40,2,1);
                break;
            case R.id.Level3Ldeux:
                startLevelMode(40,3,1);
                break;
            case R.id.Level4Ldeux:
                startLevelMode(40,4,1);
                break;
            case R.id.Level5Ldeux:
                startLevelMode(40,5,1);
                break;
            case R.id.Level6Ldeux:
                startLevelMode(40,6,1);
                break;
            case R.id.Level7Ldeux:
                startLevelMode(40,7,1);
                break;
            case R.id.Level8Ldeux:
                startLevelMode(40,8,1);
                break;
            case R.id.Level9Ldeux:
                startLevelMode(40,9,1);
                break;
            case R.id.Level10Ldeux:
                startLevelMode(40,10,1);
                break;


            case R.id.Level1Strois:
                startLevelMode(10, 1, 2);
                break;
            case R.id.Level2Strois:
                startLevelMode(10, 2, 2);
                break;
            case R.id.Level3Strois:
                startLevelMode(10, 3, 2);
                break;
            case R.id.Level4Strois:
                startLevelMode(10, 4, 2);
                break;
            case R.id.Level5Strois:
                startLevelMode(10, 5, 2);
                break;
            case R.id.Level6Strois:
                startLevelMode(10, 6, 2);
                break;
            case R.id.Level7Strois:
                startLevelMode(10, 7, 2);
                break;
            case R.id.Level8Strois:
                startLevelMode(10, 8, 2);
                break;
            case R.id.Level9Strois:
                startLevelMode(10, 9, 2);
                break;
            case R.id.Level10Strois:
                startLevelMode(10, 10, 2);
                break;
            case R.id.Level1Mtrois:
                startLevelMode(20, 1, 2);
                break;
            case R.id.Level2Mtrois:
                startLevelMode(20, 2, 2);
                break;
            case R.id.Level3Mtrois:
                startLevelMode(20, 3, 2);
                break;
            case R.id.Level4Mtrois:
                startLevelMode(20, 4, 2);
                break;
            case R.id.Level5Mtrois:
                startLevelMode(20, 5, 2);
                break;
            case R.id.Level6Mtrois:
                startLevelMode(20, 6, 2);
                break;
            case R.id.Level7Mtrois:
                startLevelMode(20, 7, 2);
                break;
            case R.id.Level8Mtrois:
                startLevelMode(20, 8, 2);
                break;
            case R.id.Level9Mtrois:
                startLevelMode(20, 9, 2);
                break;
            case R.id.Level10Mtrois:
                startLevelMode(20, 10, 2);
                break;
            case R.id.Level1Ltrois:
                startLevelMode(40,1,2);
                break;
            case R.id.Level2Ltrois:
                startLevelMode(40,2,2);
                break;
            case R.id.Level3Ltrois:
                startLevelMode(40,3,2);
                break;
            case R.id.Level4Ltrois:
                startLevelMode(40,4,2);
                break;
            case R.id.Level5Ltrois:
                startLevelMode(40,5,2);
                break;
            case R.id.Level6Ltrois:
                startLevelMode(40,6,2);
                break;
            case R.id.Level7Ltrois:
                startLevelMode(40,7,2);
                break;
            case R.id.Level8Ltrois:
                startLevelMode(40,8,2);
                break;
            case R.id.Level9Ltrois:
                startLevelMode(40,9,2);
                break;
            case R.id.Level10Ltrois:
                startLevelMode(40,10,2);
                break;

            case R.id.Level1Squatre:
                startLevelMode(10, 1, 3);
                break;
            case R.id.Level2Squatre:
                startLevelMode(10, 2, 3);
                break;
            case R.id.Level3Squatre:
                startLevelMode(10, 3, 3);
                break;
            case R.id.Level4Squatre:
                startLevelMode(10, 4, 3);
                break;
            case R.id.Level5Squatre:
                startLevelMode(10, 5, 3);
                break;
            case R.id.Level6Squatre:
                startLevelMode(10, 6, 3);
                break;
            case R.id.Level7Squatre:
                startLevelMode(10, 7, 3);
                break;
            case R.id.Level8Squatre:
                startLevelMode(10, 8, 3);
                break;
            case R.id.Level9Squatre:
                startLevelMode(10, 9, 3);
                break;
            case R.id.Level10Squatre:
                startLevelMode(10, 10, 3);
                break;
            case R.id.Level1Mquatre:
                startLevelMode(20, 1, 3);
                break;
            case R.id.Level2Mquatre:
                startLevelMode(20, 2, 3);
                break;
            case R.id.Level3Mquatre:
                startLevelMode(20, 3, 3);
                break;
            case R.id.Level4Mquatre:
                startLevelMode(20, 4, 3);
                break;
            case R.id.Level5Mquatre:
                startLevelMode(20, 5, 3);
                break;
            case R.id.Level6Mquatre:
                startLevelMode(20, 6, 3);
                break;
            case R.id.Level7Mquatre:
                startLevelMode(20, 7, 3);
                break;
            case R.id.Level8Mquatre:
                startLevelMode(20, 8, 3);
                break;
            case R.id.Level9Mquatre:
                startLevelMode(20, 9, 3);
                break;
            case R.id.Level10Mquatre:
                startLevelMode(20, 10, 3);
                break;
            case R.id.Level1Lquatre:
                startLevelMode(40,1,3);
                break;
            case R.id.Level2Lquatre:
                startLevelMode(40,2,3);
                break;
            case R.id.Level3Lquatre:
                startLevelMode(40,3,3);
                break;
            case R.id.Level4Lquatre:
                startLevelMode(40,4,3);
                break;
            case R.id.Level5Lquatre:
                startLevelMode(40,5,3);
                break;
            case R.id.Level6Lquatre:
                startLevelMode(40,6,3);
                break;
            case R.id.Level7Lquatre:
                startLevelMode(40,7,3);
                break;
            case R.id.Level8Lquatre:
                startLevelMode(40,8,3);
                break;
            case R.id.Level9Lquatre:
                startLevelMode(40,9,3);
                break;
            case R.id.Level10Lquatre:
                startLevelMode(40,10,3);
                break;
            
        }
    }

    private void maj() {

        Button B1 = (Button) findViewById(R.id.Level1S);
        Button B2 = (Button) findViewById(R.id.Level2S);
        Button B3 = (Button) findViewById(R.id.Level3S);
        Button B4 = (Button) findViewById(R.id.Level4S);
        Button B5 = (Button) findViewById(R.id.Level5S);
        Button B6 = (Button) findViewById(R.id.Level6S);
        Button B7 = (Button) findViewById(R.id.Level7S);
        Button B8 = (Button) findViewById(R.id.Level8S);
        Button B9 = (Button) findViewById(R.id.Level9S);
        Button B10 = (Button) findViewById(R.id.Level10S);

        Button LB1 = (Button) findViewById(R.id.Level1L);
        Button LB2 = (Button) findViewById(R.id.Level2L);
        Button LB3 = (Button) findViewById(R.id.Level3L);
        Button LB4 = (Button) findViewById(R.id.Level4L);
        Button LB5 = (Button) findViewById(R.id.Level5L);
        Button LB6 = (Button) findViewById(R.id.Level6L);
        Button LB7 = (Button) findViewById(R.id.Level7L);
        Button LB8 = (Button) findViewById(R.id.Level8L);
        Button LB9 = (Button) findViewById(R.id.Level9L);
        Button LB10 = (Button) findViewById(R.id.Level10L);

        Button MB1 = (Button) findViewById(R.id.Level1M);
        Button MB2 = (Button) findViewById(R.id.Level2M);
        Button MB3 = (Button) findViewById(R.id.Level3M);
        Button MB4 = (Button) findViewById(R.id.Level4M);
        Button MB5 = (Button) findViewById(R.id.Level5M);
        Button MB6 = (Button) findViewById(R.id.Level6M);
        Button MB7 = (Button) findViewById(R.id.Level7M);
        Button MB8 = (Button) findViewById(R.id.Level8M);
        Button MB9 = (Button) findViewById(R.id.Level9M);
        Button MB10 = (Button) findViewById(R.id.Level10M);


        Button bB1 = (Button) findViewById(R.id.Level1Sdeux);
        Button bB2 = (Button) findViewById(R.id.Level2Sdeux);
        Button bB3 = (Button) findViewById(R.id.Level3Sdeux);
        Button bB4 = (Button) findViewById(R.id.Level4Sdeux);
        Button bB5 = (Button) findViewById(R.id.Level5Sdeux);
        Button bB6 = (Button) findViewById(R.id.Level6Sdeux);
        Button bB7 = (Button) findViewById(R.id.Level7Sdeux);
        Button bB8 = (Button) findViewById(R.id.Level8Sdeux);
        Button bB9 = (Button) findViewById(R.id.Level9Sdeux);
        Button bB10 = (Button) findViewById(R.id.Level10Sdeux);

        Button aLB1 = (Button) findViewById(R.id.Level1Ldeux);
        Button aLB2 = (Button) findViewById(R.id.Level2Ldeux);
        Button aLB3 = (Button) findViewById(R.id.Level3Ldeux);
        Button aLB4 = (Button) findViewById(R.id.Level4Ldeux);
        Button aLB5 = (Button) findViewById(R.id.Level5Ldeux);
        Button aLB6 = (Button) findViewById(R.id.Level6Ldeux);
        Button aLB7 = (Button) findViewById(R.id.Level7Ldeux);
        Button aLB8 = (Button) findViewById(R.id.Level8Ldeux);
        Button aLB9 = (Button) findViewById(R.id.Level9Ldeux);
        Button aLB10 = (Button) findViewById(R.id.Level10Ldeux);

        Button aMB1 = (Button) findViewById(R.id.Level1Mdeux);
        Button aMB2 = (Button) findViewById(R.id.Level2Mdeux);
        Button aMB3 = (Button) findViewById(R.id.Level3Mdeux);
        Button aMB4 = (Button) findViewById(R.id.Level4Mdeux);
        Button aMB5 = (Button) findViewById(R.id.Level5Mdeux);
        Button aMB6 = (Button) findViewById(R.id.Level6Mdeux);
        Button aMB7 = (Button) findViewById(R.id.Level7Mdeux);
        Button aMB8 = (Button) findViewById(R.id.Level8Mdeux);
        Button aMB9 = (Button) findViewById(R.id.Level9Mdeux);
        Button aMB10 = (Button) findViewById(R.id.Level10Mdeux);


        Button qB1 = (Button) findViewById(R.id.Level1Strois);
        Button qB2 = (Button) findViewById(R.id.Level2Strois);
        Button qB3 = (Button) findViewById(R.id.Level3Strois);
        Button qB4 = (Button) findViewById(R.id.Level4Strois);
        Button qB5 = (Button) findViewById(R.id.Level5Strois);
        Button qB6 = (Button) findViewById(R.id.Level6Strois);
        Button qB7 = (Button) findViewById(R.id.Level7Strois);
        Button qB8 = (Button) findViewById(R.id.Level8Strois);
        Button qB9 = (Button) findViewById(R.id.Level9Strois);
        Button qB10 = (Button) findViewById(R.id.Level10Strois);

        Button zLB1 = (Button) findViewById(R.id.Level1Ltrois);
        Button zLB2 = (Button) findViewById(R.id.Level2Ltrois);
        Button zLB3 = (Button) findViewById(R.id.Level3Ltrois);
        Button zLB4 = (Button) findViewById(R.id.Level4Ltrois);
        Button zLB5 = (Button) findViewById(R.id.Level5Ltrois);
        Button zLB6 = (Button) findViewById(R.id.Level6Ltrois);
        Button zLB7 = (Button) findViewById(R.id.Level7Ltrois);
        Button zLB8 = (Button) findViewById(R.id.Level8Ltrois);
        Button zLB9 = (Button) findViewById(R.id.Level9Ltrois);
        Button zLB10 = (Button) findViewById(R.id.Level10Ltrois);

        Button zMB1 = (Button) findViewById(R.id.Level1Mtrois);
        Button zMB2 = (Button) findViewById(R.id.Level2Mtrois);
        Button zMB3 = (Button) findViewById(R.id.Level3Mtrois);
        Button zMB4 = (Button) findViewById(R.id.Level4Mtrois);
        Button zMB5 = (Button) findViewById(R.id.Level5Mtrois);
        Button zMB6 = (Button) findViewById(R.id.Level6Mtrois);
        Button zMB7 = (Button) findViewById(R.id.Level7Mtrois);
        Button zMB8 = (Button) findViewById(R.id.Level8Mtrois);
        Button zMB9 = (Button) findViewById(R.id.Level9Mtrois);
        Button zMB10 = (Button) findViewById(R.id.Level10Mtrois);


        Button hB1 = (Button) findViewById(R.id.Level1Squatre);
        Button hB2 = (Button) findViewById(R.id.Level2Squatre);
        Button hB3 = (Button) findViewById(R.id.Level3Squatre);
        Button hB4 = (Button) findViewById(R.id.Level4Squatre);
        Button hB5 = (Button) findViewById(R.id.Level5Squatre);
        Button hB6 = (Button) findViewById(R.id.Level6Squatre);
        Button hB7 = (Button) findViewById(R.id.Level7Squatre);
        Button hB8 = (Button) findViewById(R.id.Level8Squatre);
        Button hB9 = (Button) findViewById(R.id.Level9Squatre);
        Button hB10 = (Button) findViewById(R.id.Level10Squatre);

        Button jLB1 = (Button) findViewById(R.id.Level1Lquatre);
        Button jLB2 = (Button) findViewById(R.id.Level2Lquatre);
        Button jLB3 = (Button) findViewById(R.id.Level3Lquatre);
        Button jLB4 = (Button) findViewById(R.id.Level4Lquatre);
        Button jLB5 = (Button) findViewById(R.id.Level5Lquatre);
        Button jLB6 = (Button) findViewById(R.id.Level6Lquatre);
        Button jLB7 = (Button) findViewById(R.id.Level7Lquatre);
        Button jLB8 = (Button) findViewById(R.id.Level8Lquatre);
        Button jLB9 = (Button) findViewById(R.id.Level9Lquatre);
        Button jLB10 = (Button) findViewById(R.id.Level10Lquatre);

        Button kMB1 = (Button) findViewById(R.id.Level1Mquatre);
        Button kMB2 = (Button) findViewById(R.id.Level2Mquatre);
        Button kMB3 = (Button) findViewById(R.id.Level3Mquatre);
        Button kMB4 = (Button) findViewById(R.id.Level4Mquatre);
        Button kMB5 = (Button) findViewById(R.id.Level5Mquatre);
        Button kMB6 = (Button) findViewById(R.id.Level6Mquatre);
        Button kMB7 = (Button) findViewById(R.id.Level7Mquatre);
        Button kMB8 = (Button) findViewById(R.id.Level8Mquatre);
        Button kMB9 = (Button) findViewById(R.id.Level9Mquatre);
        Button kMB10 = (Button) findViewById(R.id.Level10Mquatre);

        int i = 0;
        while (i < 120) {
            if (!(levelDone[i] == null)) {
                switch (levelDone[i]) {
                    case "1S0":
                        B1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1S1":
                        bB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1S2":
                        qB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1S3":
                        hB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2S0":
                        B2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2S1":
                        bB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2S2":
                        qB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2S3":
                        hB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3S0":
                        B3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3S1":
                        bB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3S2":
                        qB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3S3":
                        hB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4S0":
                        B4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4S1":
                        bB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4S2":
                        qB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4S3":
                        hB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5S0":
                        B5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5S1":
                        bB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5S2":
                        qB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5S3":
                        hB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6S0":
                        B6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6S1":
                        bB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6S2":
                        qB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6S3":
                        hB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7S0":
                        B7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7S1":
                        bB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7S2":
                        qB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7S3":
                        hB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8S0":
                        B8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8S1":
                        bB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8S2":
                        qB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8S3":
                        hB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9S0":
                        B9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9S1":
                        bB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9S2":
                        qB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9S3":
                        hB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10S0":
                        B10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10S1":
                        bB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10S2":
                        qB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10S3":
                        hB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;

                    case "1M0":
                        MB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1M1":
                        aMB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1M2":
                        zMB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1M3":
                        kMB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2M0":
                        MB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2M1":
                        aMB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2M2":
                        zMB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2M3":
                        kMB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3M0":
                        MB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3M1":
                        aMB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3M2":
                        zMB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3M3":
                        kMB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4M0":
                        MB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4M1":
                        aMB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4M2":
                        zMB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4M3":
                        kMB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5M0":
                        MB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5M1":
                        aMB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5M2":
                        zMB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5M3":
                        kMB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6M0":
                        MB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6M1":
                        aMB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6M2":
                        zMB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6M3":
                        kMB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7M0":
                        MB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7M1":
                        aMB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7M2":
                        zMB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7M3":
                        kMB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8M0":
                        MB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8M1":
                        aMB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8M2":
                        zMB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8M3":
                        kMB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9M0":
                        MB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9M1":
                        aMB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9M2":
                        zMB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9M3":
                        kMB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10M0":
                        MB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10M1":
                        aMB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10M2":
                        zMB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10M3":
                        kMB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;

                    case "1L0":
                        LB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1L1":
                        aLB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1L2":
                        zLB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "1L3":
                        jLB1.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2L0":
                        LB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2L1":
                        aLB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2L2":
                        zLB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "2L3":
                        jLB2.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3L0":
                        LB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3L1":
                        aLB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3L2":
                        zLB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "3L3":
                        jLB3.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4L0":
                        LB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4L1":
                        aLB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4L2":
                        zLB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "4L3":
                        jLB4.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5L0":
                        LB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5L1":
                        aLB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5L2":
                        zLB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "5L3":
                        jLB5.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6L0":
                        LB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6L1":
                        aLB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6L2":
                        zLB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "6L3":
                        jLB6.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7L0":
                        LB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7L1":
                        aLB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7L2":
                        zLB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "7L3":
                        jLB7.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8L0":
                        LB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8L1":
                        aLB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8L2":
                        zLB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "8L3":
                        jLB8.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9L0":
                        LB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9L1":
                        aLB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9L2":
                        zLB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "9L3":
                        jLB9.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10L0":
                        LB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10L1":
                        aLB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10L2":
                        zLB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                    case "10L3":
                        jLB10.setBackgroundResource(R.drawable.btnleveldone);
                        break;
                }
            }
            i++;
        }
    }

    private void gridChoice() {
        setContentView(R.layout.boardchoice);
        stats();
        final RadioGroup gameMode1 = (RadioGroup) findViewById(R.id.mode1);
        final RelativeLayout l1 = (RelativeLayout) findViewById(R.id.layout1);
        final RelativeLayout l2 = (RelativeLayout) findViewById(R.id.layout22);
        final RelativeLayout l3 = (RelativeLayout) findViewById(R.id.layout33);
        final RelativeLayout l4 = (RelativeLayout) findViewById(R.id.layout44);
        if (OrthoPlane){gameMode1.check(R.id.OrthoPlane);
            l1.setVisibility(RelativeLayout.VISIBLE);}
        else if (OrthoTorus) {gameMode1.check(R.id.OrthoTorus);
            l2.setVisibility(RelativeLayout.VISIBLE);}
        else if (DiagoPlane) {gameMode1.check(R.id.DiagoPlane);
            l3.setVisibility(RelativeLayout.VISIBLE);}
        else if (DiagoTorus) {gameMode1.check(R.id.DiagoTorus);
            l4.setVisibility(RelativeLayout.VISIBLE);}

        gameMode1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.OrthoPlane:
                        OrthoPlane = true;
                        OrthoTorus = false;
                        DiagoPlane = false;
                        DiagoTorus = false;
                        l1.setVisibility(RelativeLayout.VISIBLE);
                        l2.setVisibility(RelativeLayout.GONE);
                        l3.setVisibility(RelativeLayout.GONE);
                        l4.setVisibility(RelativeLayout.GONE);
                        break;
                    case R.id.OrthoTorus:
                        OrthoPlane = false;
                        OrthoTorus = true;
                        DiagoPlane = false;
                        DiagoTorus = false;
                        l1.setVisibility(RelativeLayout.GONE);
                        l2.setVisibility(RelativeLayout.VISIBLE);
                        l3.setVisibility(RelativeLayout.GONE);
                        l4.setVisibility(RelativeLayout.GONE);
                        break;
                    case R.id.DiagoPlane:
                        OrthoPlane = false;
                        OrthoTorus = false;
                        DiagoPlane = true;
                        DiagoTorus = false;
                        l1.setVisibility(RelativeLayout.GONE);
                        l2.setVisibility(RelativeLayout.GONE);
                        l3.setVisibility(RelativeLayout.VISIBLE);
                        l4.setVisibility(RelativeLayout.GONE);
                        break;
                    case R.id.DiagoTorus:
                        OrthoPlane = false;
                        OrthoTorus = false;
                        DiagoPlane = false;
                        DiagoTorus = true;
                        l1.setVisibility(RelativeLayout.GONE);
                        l2.setVisibility(RelativeLayout.GONE);
                        l3.setVisibility(RelativeLayout.GONE);
                        l4.setVisibility(RelativeLayout.VISIBLE);
                        break;
                }
            }
        });


        Button button = (Button) findViewById(R.id.small);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRandomMode(10);
            }
        });
        Button button1 = (Button) findViewById(R.id.medium);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRandomMode(20);
            }
        });
        Button button2 = (Button) findViewById(R.id.large);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRandomMode(40);
            }
        });
        Button retBut = (Button) findViewById(R.id.returnToM);
        retBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.recreate();
            }
        });
    }

    private void startRandomMode(int size){
        levelMode=false;
        gameModel = new GameModel(size);
        undoStack = new GenericLinkedStack<>();
        redoStack = new GenericLinkedStack<>();
        if (OrthoPlane) {planeOrthoMode();}
        else if (DiagoPlane) {planeDiagoMode();}
        else if (OrthoTorus) {torusOrthoMode();}
        else if (DiagoTorus) {torusDiagoMode();}
        realStart(size, 0);
    }

    private void saveStats(String a, int b){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit2 = sp.edit();
        mEdit2.remove(a);
        mEdit2.putInt(a, b);
        mEdit2.apply();
    }

    private void youWonRandom() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialoglayout);
        TextView textcong = (TextView) dialog.findViewById(R.id.textcong);
        textcong.setText(getString(R.string.youwon1)+gameModel.getNumberOfSteps()+
                getString(R.string.youwon2));

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                reset();
            }
        });

        Button dialBut = (Button) dialog.findViewById(R.id.modegridChoice);
        dialBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startRandomMode(getModel().getSize());
            }
        });
        dialog.show();
    }

    private void stats() {
        setContentView(R.layout.boardchoice);
        TextView stats = (TextView) findViewById(R.id.S1);
        stats.setText(" "+bestOrthoPlaneS);
        TextView stats1 = (TextView) findViewById(R.id.M1);
        stats1.setText(" "+bestOrthoPlaneM);
        TextView stats2 = (TextView) findViewById(R.id.L1);
        stats2.setText(" "+bestOrthoPlaneL);
        TextView stats3 = (TextView) findViewById(R.id.S21);
        stats3.setText(" "+bestOrthoTorusS);
        TextView stats4 = (TextView) findViewById(R.id.M21);
        stats4.setText(" "+bestOrthoTorusM);
        TextView stats5 = (TextView) findViewById(R.id.L21);
        stats5.setText(" "+bestOrthoTorusL);
        TextView stats6 = (TextView) findViewById(R.id.S31);
        stats6.setText(" "+bestDiagoPlaneS);
        TextView stats7 = (TextView) findViewById(R.id.M31);
        stats7.setText(" "+bestDiagoPlaneM);
        TextView stats8 = (TextView) findViewById(R.id.L31);
        stats8.setText(" "+bestDiagoPlaneL);
        TextView stats9 = (TextView) findViewById(R.id.S41);
        stats9.setText(" "+bestDiagoTorusS);
        TextView stats0 = (TextView) findViewById(R.id.M41);
        stats0.setText(" "+bestDiagoTorusM);
        TextView stats11 = (TextView) findViewById(R.id.L41);
        stats11.setText(" "+bestDiagoTorusL);
    }

    private void loadStats(Context mContext){
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);

        bestOrthoPlaneS = mSharedPreference1.getInt("bestOrthoPlaneS", 99);
        bestOrthoTorusS = mSharedPreference1.getInt("bestOrthoTorusS", 99);
        bestDiagoPlaneS = mSharedPreference1.getInt("bestDiagoPlaneS", 99);
        bestDiagoTorusS = mSharedPreference1.getInt("bestDiagoTorusS", 99);

        bestOrthoPlaneM = mSharedPreference1.getInt("bestOrthoPlaneM", 99);
        bestOrthoTorusM = mSharedPreference1.getInt("bestOrthoTorusM", 99);
        bestDiagoPlaneM = mSharedPreference1.getInt("bestDiagoPlaneM", 99);
        bestDiagoTorusM = mSharedPreference1.getInt("bestDiagoTorusM", 99);

        bestOrthoPlaneL = mSharedPreference1.getInt("bestOrthoPlaneL", 99);
        bestOrthoTorusL = mSharedPreference1.getInt("bestOrthoTorusL", 99);
        bestDiagoPlaneL = mSharedPreference1.getInt("bestDiagoPlaneL", 99);
        bestDiagoTorusL = mSharedPreference1.getInt("bestDiagoTorusL", 99);

    }
    @Override
    protected void onDestroy() {
        TMBannerAdView ad = (TMBannerAdView) findViewById(R.id.adView);
        if (ad != null) {
            ad.destroy(this);
        }
        super.onDestroy();
    }
}

