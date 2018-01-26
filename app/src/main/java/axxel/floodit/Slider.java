package axxel.floodit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Repr�sente le menu anim�.
 */
public class Slider extends LinearLayout {
    /* Est-ce que le menu est ouvert ? */
    protected boolean isOpen = false;
    /* Le menu � dissimuler */
    protected RelativeLayout toHide = null;
    /* Vitesse d�sir�e pour l'animation */
    protected final static int SPEED = 100;

    /* Listener pour l'animation de fermeture du menu */
    Animation.AnimationListener closeListener = new Animation.AnimationListener() {
        public void onAnimationEnd(Animation animation) {
            //On dissimule le menu
            toHide.setVisibility(View.GONE);
        }

        public void onAnimationRepeat(Animation animation) {

        }

        public void onAnimationStart(Animation animation) {

        }
    };

    /* Listener pour l'animation d'ouverture du menu */
    Animation.AnimationListener openListener = new Animation.AnimationListener() {
        public void onAnimationEnd(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            //On affiche le menu
            toHide.setVisibility(View.VISIBLE);
        }
    };

    /**
     * Constructeur utilis� pour l'initialisation en Java.
     * @param context Le contexte de l'activit�.
     */
    public Slider(Context context) {
        super(context);
    }

    /**
     * Constructeur utilis� pour l'initialisation en XML.
     * @param context Le contexte de l'activit�.
     * @param attrs Les attributs d�finis dans le XML.
     */
    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Utilis�e pour ouvrir ou fermer le menu.
     * @return true si le menu est d�sormais ouvert.
     */
    public boolean toggle() {
        //Animation de transition.
        TranslateAnimation animation = null;

        //On passe de ouvert � ferm� (ou vice versa)
        isOpen = !isOpen;

        //Si le menu est d�j� ouvert
        if (isOpen)
        {
            //Animation de translation du bas vers le haut
            animation = new TranslateAnimation(0.0f, 0.0f,
                    -toHide.getHeight(), 0.0f);
            animation.setAnimationListener(openListener);
        } else
        {
            //Sinon, animation de translation du haut vers le bas
            animation = new TranslateAnimation(0.0f, 0.0f,
                    0.0f, -toHide.getHeight());
            animation.setAnimationListener(closeListener);
        }

        //On d�termine la dur�e de l'animation
        animation.setDuration(SPEED);
        //On ajoute un effet d'acc�l�ration
        animation.setInterpolator(new AccelerateInterpolator());
        //Enfin, on lance l'animation
        startAnimation(animation);

        return isOpen;
    }


    public void setToHide(RelativeLayout toHide) {
        this.toHide = toHide;
    }
}