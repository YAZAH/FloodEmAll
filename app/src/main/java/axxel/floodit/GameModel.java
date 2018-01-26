package axxel.floodit;

import java.util.Random;
import java.io.*;

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the followiung information:
 * - the state of all the ``dots'' on the board (color, captured or not)
 * - the size of the board
 * - the number of steps since the last reset
 * - the current color of selection
 *
 * The model provides all of this informations to the other classes trough 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Yassine Zahoui, University of Ottawa
 */
public class GameModel implements Serializable, Cloneable {


    /**
     * predefined values to capture the color of a DotInfo
     */
    //public static final int Bleu     = 0;
    //public static final int Jaune    = 1;
    //public static final int Vert     = 2;
    //public static final int Violet   = 3;
    //public static final int Rouge    = 4;
    //public static final int Gris     = 5;
    public static final int NUMBER_OF_COLORS  = 6;

    /**
     * The current selection color
     */
	private int currentSelectedColor;

    private String currentSelectedLevel;

    /**
     * The size of the game.
     */
    private int sizeOfGame;
    private int levelOfGame;
    private int modeOfGame; // 0: OrthoPlane, 1: DiagoPlane, 2: OrthoTorus, 3: DiagoTorus


    /**
     * A 2 dimentionnal array of sizeOfGame*sizeOfGame recording the state of each dot
     */
	private DotInfo[][] model;

    private int numbStepsMax;

   /**
     * The number of steps played since the last reset
     */
	private int numberOfSteps;
 
   /**
     * The number of captered dots
     */
    private int numberCaptured;

   /**
     * Random generator
     */
	private Random generator;

    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {
        generator = new Random();
        sizeOfGame = size;
        reset();
    }

    public GameModel(int size, int level, int mode) {
        sizeOfGame=size;
        levelOfGame=level;
        modeOfGame=mode;
        gameSelect();
    }
    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){

    	model = new DotInfo[sizeOfGame][sizeOfGame];

    	for(int i = 0; i < sizeOfGame; i++){
		   	for(int j = 0; j < sizeOfGame; j++){
    			model[i][j] = new DotInfo(i,j,generator.nextInt(NUMBER_OF_COLORS));
    		}
    	}
        start();
    }

    /**
     * Getter method for the size of the game
     * 
     * @return the value of the attribute sizeOfGame
     */   
    public int getSize(){
        return sizeOfGame;
    }

    public int getLevelOfGame(){
        return levelOfGame;
    }

    public int getMode(){
        return modeOfGame;
    }

    /**
     * returns the color  of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public int getColor(int i, int j){
        if(isCaptured(i, j)) {
            return currentSelectedColor;
        } else {
    	   return model[i][j].getColor();
        }
    }

    /**
     * returns true is the dot is captured, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isCaptured(int i, int j){
        return model[i][j].isCaptured();
    }

    /**
     * Sets the status of the dot at coordinate (i,j) to captured
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void capture(int i, int j){
 		model[i][j].setCaptured(true);
        numberCaptured++;
    }

    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){
    	return numberOfSteps;
    }

    public void setNumberOfSteps() {numberOfSteps=numberOfSteps-1;}

    public int getNumbStepsMax() {return numbStepsMax;}

    /**
     * Setter method for currentSelectedColor
     * 
     * @param val
     *            the new value for currentSelectedColor
    */   
    public void setCurrentSelectedColor(int val) {
        currentSelectedColor = val;
    }

     /**
     * Getter method for currentSelectedColor
     * 
     * @return currentSelectedColor
     */   
    public int getCurrentSelectedColor() {
        return currentSelectedColor ;
    }

    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
      * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {
        return model[i][j];
    }

   /**
     * The metod <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the payer selected a new color.
     */
     public void step(){
        numberOfSteps++;
    }

   /**
     * The metod <b>isFinished</b> returns true iff the game is finished, that
     * is, all the dats are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){
        return numberCaptured == sizeOfGame*sizeOfGame;
    }

    public boolean gameOver() {
        return numbStepsMax==numberOfSteps;
    }

   /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){
        StringBuffer b = new StringBuffer();
        for(int i = 0; i < sizeOfGame; i++){
            for(int j = 0; j < sizeOfGame; j++){
                b.append(getColor(i, j) + " ");
            }
            b.append("\n");
        }
        return b.toString();
    }
	
	/**
     * The metod <b>clone</b> returns a deepcopy of the actual object GameModel.
     *
     * @return An object GameModel (the clone of the actual object GameModel)
     */
	@Override
	public GameModel clone() {
		try {
			GameModel clone = (GameModel) super.clone();
			clone.model = new DotInfo[sizeOfGame][sizeOfGame];
			for (int i = 0; i <sizeOfGame; i++) {
				for (int j=0; j<sizeOfGame; j++) {
					clone.model[i][j]=model[i][j].clone();
				}
			}
			return clone;
			} catch (CloneNotSupportedException e) {
				throw new AssertionError();
			}
	}

	private void gameSelect() {
        if (sizeOfGame == 10) {
            setCurrentSelectedLevel(levelOfGame+"S"+modeOfGame);
            if (levelOfGame == 1) {
                level1S();
            }
            if (levelOfGame == 2) {
                level2S();
            }
            if (levelOfGame == 3) {
                level3S();
            }
            if (levelOfGame == 4) {
                level4S();
            }
            if (levelOfGame == 5) {
                level5S();
            }
            if (levelOfGame == 6) {
                level6S();
            }
            if (levelOfGame == 7) {
                level7S();
            }
            if (levelOfGame == 8) {
                level8S();
            }
            if (levelOfGame == 9) {
                level9S();
            }
            if (levelOfGame == 10) {
                level10S();
            }
        }

        if (sizeOfGame == 20) {
            setCurrentSelectedLevel(levelOfGame+"M"+modeOfGame);
            if (levelOfGame == 1) {
                level1M();
            }
            if (levelOfGame == 2) {
                level2M();
            }
            if (levelOfGame == 3) {
                level3M();
            }
            if (levelOfGame == 4) {
                level4M();
            }
            if (levelOfGame == 5) {
                level5M();
            }
            if (levelOfGame == 6) {
                level6M();
            }
            if (levelOfGame == 7) {
                level7M();
            }
            if (levelOfGame == 8) {
                level8M();
            }
            if (levelOfGame == 9) {
                level9M();
            }
            if (levelOfGame == 10) {
                level10M();
            }
        }
        if (sizeOfGame == 40) {
            setCurrentSelectedLevel(levelOfGame+"L"+modeOfGame);
            if (levelOfGame == 1) {
                level1L();
            }
            if (levelOfGame == 2) {
                level2L();
            }
            if (levelOfGame == 3) {
                level3L();
            }
            if (levelOfGame == 4) {
                level4L();
            }
            if (levelOfGame == 5) {
                level5L();
            }
            if (levelOfGame == 6) {
                level6L();
            }
            if (levelOfGame == 7) {
                level7L();
            }
            if (levelOfGame == 8) {
                level8L();
            }
            if (levelOfGame == 9) {
                level9L();
            }
            if (levelOfGame == 10) {
                level10L();
            }
        }
    }

    private void level1S(){
        model = new DotInfo[sizeOfGame][sizeOfGame];
        int count=0;
        int[] Tab0 = {0,5,2,1,1,5,3,2,2,3,
                0,5,2,5,2,0,1,3,0,3,
                1,5,1,2,5,2,1,0,4,5,
                4,5,3,2,4,2,2,4,3,2,
                2,4,5,0,4,4,5,2,5,3,
                2,4,3,5,0,1,3,3,0,2,
                3,5,3,4,5,3,2,1,0,5,
                2,4,1,5,0,0,5,0,4,4,
                5,1,2,2,3,5,0,5,4,5,
                2,1,0,5,2,5,5,4,2,3};
        int[] Tab1 ={4,1,5,1,4,0,5,1,1,3,
                5,3,0,1,2,4,5,5,4,0,
                4,1,4,1,3,5,3,0,4,3,
                2,2,2,1,0,3,1,2,1,2,
                2,1,5,4,2,0,1,3,4,0,
                3,3,2,5,5,2,5,2,5,4,
                2,2,4,4,5,4,5,4,0,2,
                1,3,4,2,3,0,2,4,5,3,
                4,3,3,1,3,0,4,5,0,4,
                4,1,2,5,5,4,3,4,4,1};
        int[] Tab2 ={3,0,3,5,0,3,1,1,2,0,
                1,4,0,1,1,1,2,4,4,0,
                3,4,5,1,1,4,5,0,1,5,
                3,4,0,1,1,1,2,0,5,2,
                4,2,4,5,1,1,5,5,5,1,
                1,0,0,4,1,0,5,3,4,2,
                0,5,5,5,1,2,5,0,3,0,
                0,4,0,2,2,5,0,1,1,1,
                5,2,3,2,1,1,3,3,4,2,
                3,4,1,3,0,2,3,0,3,3
        };
        int[] Tab3 ={2,5,1,2,2,5,2,0,3,4,
                3,4,2,0,0,2,2,0,2,3,
                3,5,4,2,2,3,3,4,3,2,
                3,3,5,3,0,4,5,3,0,2,
                0,4,5,3,0,2,5,1,4,5,
                2,1,3,2,4,5,2,3,5,4,
                2,5,2,2,0,3,1,1,0,2,
                1,4,1,4,5,2,0,2,3,1,
                0,2,0,2,0,4,0,4,5,1,
                5,4,4,0,4,1,2,1,1,5
        };

        if (modeOfGame==0){
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=15;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level2S(){
        int[] Tab0 = {5,2,5,2,5,2,1,0,0,3,
                4,4,1,3,2,0,1,2,1,4,
                2,4,2,0,5,5,0,4,4,0,
                3,0,1,0,3,4,1,3,4,0,
                5,3,4,0,0,3,5,4,2,5,
                5,4,3,0,2,3,2,5,2,1,
                0,3,5,3,4,3,4,3,1,3,
                5,5,0,0,1,3,1,3,2,3,
                1,4,3,0,1,5,5,3,5,4,
                3,1,2,3,5,3,3,0,1,3};
        int[] Tab1 ={5,4,0,2,4,5,0,4,1,1,
                                                        5,4,2,2,5,3,1,0,3,2,
                                                        4,4,0,4,5,4,1,1,4,5,
                                                        0,0,0,1,5,0,5,3,2,1,
                                                        4,0,0,3,5,4,4,4,3,4,
                                                        3,4,5,2,1,3,5,3,3,0,
                                                        5,5,2,3,2,5,4,5,2,1,
                                                        1,1,2,0,4,1,0,0,1,1,
                                                        1,4,5,4,3,3,3,2,4,0,
                                                        0,3,2,5,3,0,0,5,1,3,
                                                        };
        int[] Tab2 ={2,0,2,3,0,2,1,2,2,0,
                                                        0,2,4,4,1,3,4,2,2,1,
                                                        4,3,1,5,3,2,2,2,4,2,
                                                        5,2,0,2,5,4,5,0,1,4,
                                                        3,2,0,4,5,3,3,2,2,4,
                                                        2,0,3,5,1,0,4,5,5,2,
                                                        1,0,2,5,2,2,4,4,5,0,
                                                        4,5,3,0,4,2,3,4,5,3,
                                                        2,2,1,3,3,4,0,5,2,5,
                                                        0,2,1,1,2,3,5,1,5,3,
                                                        };
        int[] Tab3 ={2,5,0,1,3,3,0,0,3,1,
                                                        1,2,0,4,4,3,1,4,5,2,
                                                        4,4,1,2,0,0,5,1,1,5,
                                                        5,0,2,0,0,4,1,3,0,5,
                                                        3,0,3,5,4,1,5,5,5,1,
                                                        4,5,1,0,2,0,1,4,3,5,
                                                        3,1,4,5,3,5,5,1,0,3,
                                                        2,1,2,2,5,5,4,1,1,0,
                                                        5,0,4,1,5,1,5,3,0,0,
                                                        1,4,4,2,3,2,2,1,2,0,
                                                        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=18; // Réussi en 16 coups (Best?)
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=16;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=13;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=11;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level3S(){
        int[] Tab0 = {5,5,3,1,1,3,1,4,3,5,
                1,4,4,3,2,4,2,0,4,2,
                2,4,4,2,0,1,3,3,1,4,
                4,2,2,4,0,4,4,0,0,3,
                2,3,3,0,0,5,1,5,2,5,
                5,0,1,5,0,2,3,0,2,5,
                3,3,1,5,0,0,2,1,5,2,
                3,5,4,0,5,2,2,5,3,0,
                3,4,5,2,5,3,1,0,2,4,
                0,3,1,3,5,3,5,5,5,2};
        int[] Tab1 ={2,2,0,2,5,0,5,5,5,5,
                                                        3,0,5,5,5,3,2,0,3,2,
                                                        0,2,2,4,1,1,2,5,4,0,
                                                        4,0,3,4,5,5,1,5,3,4,
                                                        1,2,2,5,1,0,2,1,3,1,
                                                        3,0,1,5,0,5,2,1,3,3,
                                                        3,3,0,1,3,0,0,1,3,0,
                                                        1,2,2,2,2,0,0,0,1,1,
                                                        4,4,4,2,4,3,1,5,0,4,
                                                        2,5,5,0,2,5,0,3,3,3,
                                                        };
        int[] Tab2 ={4,0,2,3,5,2,5,1,1,5,
                                                        3,3,3,0,1,3,3,0,2,5,
                                                        0,1,4,5,5,3,4,0,0,2,
                                                        1,0,2,3,4,3,2,5,5,5,
                                                        2,1,0,0,3,1,1,3,1,1,
                                                        4,1,5,2,5,0,5,1,5,1,
                                                        0,0,5,3,4,5,0,1,3,2,
                                                        3,0,1,5,2,0,4,3,2,2,
                                                        2,5,1,2,1,4,5,2,1,1,
                                                        3,4,1,1,3,4,1,0,1,1,
                                                        };
        int[] Tab3 ={3,3,0,2,0,5,0,4,1,4,
                                                        1,5,1,3,2,2,3,2,2,0,
                                                        2,5,2,0,2,2,4,4,3,3,
                                                        0,3,3,0,1,4,1,1,0,0,
                                                        0,1,3,2,0,2,5,3,4,4,
                                                        1,2,1,3,4,4,3,1,4,0,
                                                        2,2,3,2,5,4,1,3,3,3,
                                                        0,0,0,2,4,3,1,2,1,4,
                                                        3,4,3,0,4,4,0,3,5,4,
                                                        3,5,1,4,3,3,2,0,2,0,
                                                        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=16;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=13;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=10;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();

    }
    private void level4S(){
        int[] Tab0 = {0,5,2,2,2,3,1,1,5,5,
                4,3,0,4,0,2,5,2,0,0,
                5,2,4,0,3,1,1,1,2,4,
                2,5,5,4,1,0,0,0,5,5,
                5,5,1,1,3,1,4,2,1,1,
                0,3,3,1,3,2,0,2,1,3,
                5,5,0,5,3,1,3,0,3,3,
                0,4,2,0,1,1,4,2,5,4,
                1,1,1,2,3,1,1,0,1,1,
                4,3,2,0,1,4,1,2,4,5};
        int[] Tab1 ={1,3,4,0,5,2,0,1,4,1,
                 4,1,5,3,1,5,5,4,4,2,
                 3,5,0,3,1,5,4,0,4,5,
                 0,4,0,0,2,4,0,1,5,4,
                 0,2,0,0,4,3,5,0,4,0,
                 5,4,3,4,3,4,4,5,5,3,
                 2,1,0,2,0,3,5,5,4,1,
                 2,1,2,5,4,4,3,3,5,3,
                 4,3,5,0,4,5,3,4,2,4,
                 2,3,1,5,5,1,4,3,3,3,
         };

        int[] Tab2 ={3,5,1,0,5,5,3,4,1,2,
                2,1,2,1,2,2,3,4,0,1,
                0,3,4,1,1,3,1,4,2,0,
                5,0,0,4,1,1,3,3,2,2,
                4,0,3,2,4,3,5,4,3,4,
                2,2,0,4,5,5,4,4,5,3,
                2,0,2,0,1,4,0,5,3,4,
                5,1,3,1,3,4,2,1,2,2,
                3,1,2,3,4,4,1,5,3,4,
                2,2,5,5,4,2,3,1,3,5,
        };
        int[] Tab3 ={2,2,0,4,2,1,3,1,1,4,
                2,4,2,1,4,5,2,3,2,3,
                1,5,3,0,4,4,2,3,2,2,
                0,0,4,1,1,5,1,5,1,1,
                1,3,5,2,1,1,1,2,4,1,
                4,2,1,2,5,1,4,2,2,2,
                5,4,2,2,5,5,3,0,4,2,
                2,5,5,5,1,1,0,0,4,1,
                2,5,1,2,3,3,3,5,2,5,
                2,0,4,2,5,5,2,3,2,2
        };

        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=17;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=10;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=10;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level5S(){
        int[] Tab0 = {0,5,3,4,0,2,0,3,4,3
                ,5,3,2,0,5,5,0,1,2,1
                ,4,0,1,1,5,2,2,5,0,4
                ,2,2,3,5,5,3,3,2,0,3
                ,0,2,4,4,3,4,1,3,1,4
                ,4,1,0,4,2,4,3,5,5,3
                ,1,5,1,4,2,5,1,2,4,3
                ,4,5,5,4,1,5,0,5,3,0
                ,5,5,3,2,3,4,1,2,3,4
                ,2,0,0,5,1,3,5,2,5,4};

        int[] Tab1 ={2,2,0,1,2,5,0,2,4,0,
                0,2,5,0,0,3,1,0,0,2,
                2,2,2,4,1,1,0,0,5,5,
                2,2,1,5,5,3,2,5,0,1,
                3,0,2,5,2,3,3,2,1,1,
                2,5,4,1,0,1,0,4,1,4,
                1,2,4,5,3,5,0,3,5,3,
                4,1,2,4,1,2,4,1,4,0,
                1,0,3,1,0,0,3,1,4,1,
                5,1,5,5,2,0,3,2,5,1,
        };
        int[] Tab2 ={5,0,1,0,2,4,3,3,2,5,
                2,2,5,4,0,5,0,4,2,1,
                3,0,2,5,3,3,5,1,4,3,
                2,3,2,0,3,5,5,5,2,1,
                4,5,2,5,2,4,0,1,4,0,
                5,1,0,0,1,4,2,5,1,1,
                5,0,2,1,1,5,3,3,2,3,
                5,2,0,2,1,3,5,4,0,4,
                4,4,4,4,5,1,3,3,0,2,
                5,1,1,3,4,4,2,5,2,5,
        };
        int[] Tab3 ={2,2,2,1,2,5,1,5,5,2,
                3,0,1,1,1,2,0,2,5,4,
                5,4,1,1,0,1,0,2,3,5,
                4,3,2,5,1,0,2,2,4,0,
                2,0,0,1,2,0,3,1,2,0,
                5,4,1,4,4,5,5,2,2,0,
                3,3,4,3,3,5,1,1,3,4,
                0,0,3,1,0,4,4,3,5,3,
                1,5,3,2,4,5,2,0,3,1,
                1,2,4,3,5,0,2,2,3,5,
        };


        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=19;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=13;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=9;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();

    }
    private void level6S(){
        int[] Tab0 = {0,4,0,0,4,3,4,1,4,5,
                3,0,4,5,1,4,1,0,1,3,
                4,4,5,5,4,2,1,4,5,1,
                0,4,3,3,0,0,0,0,2,5,
                2,1,4,0,3,2,4,3,2,4,
                0,2,3,1,0,4,3,0,0,3,
                5,3,4,0,5,2,1,1,0,1,
                3,5,0,3,5,2,1,1,4,4,
                1,0,5,4,1,2,4,1,4,3,
                1,5,4,2,0,5,5,3,2,1};
        int[] Tab1 ={1,1,2,3,5,4,0,2,2,5,
                4,5,3,1,0,5,1,1,0,1,
                0,0,5,0,1,2,0,5,4,4,
                3,1,5,5,4,2,0,0,4,2,
                2,5,2,3,4,3,3,2,4,3,
                1,4,2,1,4,3,0,4,5,0,
                3,0,5,2,2,0,1,0,4,1,
                2,1,3,0,5,3,4,4,5,2,
                3,3,4,2,5,3,2,3,1,5,
                5,2,5,2,5,1,2,0,4,5,
        };
        int[] Tab2 ={1,1,2,3,5,4,0,2,2,5,
                4,5,3,1,0,5,1,1,0,1,
                0,0,5,0,1,2,0,5,4,4,
                3,1,5,5,4,2,0,0,4,2,
                2,5,2,3,4,3,3,2,4,3,
                1,4,2,1,4,3,0,4,5,0,
                3,0,5,2,2,0,1,0,4,1,
                2,1,3,0,5,3,4,4,5,2,
                3,3,4,2,5,3,2,3,1,5,
                5,2,5,2,5,1,2,0,4,5,
        };
        int[] Tab3 ={4,4,2,0,1,2,3,5,0,5,
                5,4,0,3,4,4,2,0,0,3,
                5,4,0,1,5,5,4,1,3,3,
                3,2,4,2,5,5,4,2,5,5,
                5,3,4,1,2,1,1,4,1,4,
                0,1,0,4,1,0,5,0,1,2,
                3,4,0,2,4,0,3,1,0,0,
                1,3,5,1,1,2,1,3,0,4,
                4,5,0,0,0,4,3,3,1,4,
                1,1,2,4,3,4,3,4,3,4,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];
        int count=0;
        if (modeOfGame==0){
            numbStepsMax=18;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=13;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=8;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }start();
    }
    private void level7S(){
        int[] Tab0 = {4,5,2,2,3,4,4,1,4,0,
                3,4,0,2,2,2,5,1,1,5,
                1,4,3,1,2,1,0,0,1,3,
                2,3,1,1,0,5,2,4,4,5,
                5,5,1,1,3,2,2,0,4,0,
                5,1,0,4,3,3,2,2,0,5,
                5,4,0,4,2,4,5,1,5,5,
                2,5,1,1,5,4,0,0,1,2,
                0,4,5,1,1,1,5,3,1,1,
                0,4,0,0,1,0,1,2,2,1};
        int[] Tab1 ={0,4,3,4,4,5,1,0,1,5,
                3,2,2,4,3,3,4,0,4,2,
                0,3,0,5,5,0,5,5,5,4,
                4,3,0,2,3,0,1,5,4,4,
                2,5,0,1,5,4,3,5,5,1,
                5,1,0,5,1,5,3,3,1,4,
                5,3,4,3,3,1,2,2,4,5,
                2,1,3,2,1,3,2,2,2,0,
                1,3,0,2,3,2,4,3,4,5,
                2,5,0,4,4,5,1,0,4,3,
        };
        int[] Tab2 ={0,3,3,1,0,2,2,0,1,3,
                5,4,1,5,2,2,4,5,0,2,
                4,4,4,4,3,5,4,3,1,0,
                0,1,5,4,1,0,4,4,5,1,
                5,4,0,0,2,4,4,3,2,1,
                5,3,4,1,0,0,1,0,2,1,
                0,5,2,1,1,0,4,0,0,3,
                3,1,3,2,2,4,5,3,1,1,
                3,4,1,1,1,0,0,2,5,2,
                3,4,1,5,1,5,1,1,4,5,
        };
        int[] Tab3 ={5,2,5,2,5,3,3,1,1,1,
                4,0,0,1,2,3,1,2,1,3,
                0,4,5,0,4,0,4,0,3,3,
                4,5,3,4,5,4,0,2,4,1,
                4,2,0,1,5,2,0,4,3,3,
                3,3,0,1,4,3,2,5,4,2,
                3,1,3,4,4,3,0,0,2,5,
                5,5,5,2,0,2,1,1,1,4,
                0,4,1,3,4,0,4,2,3,4,
                0,2,1,5,0,1,0,1,4,1,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=13;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=13;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=9;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level8S(){
        int[] Tab0 = {5,0,2,1,1,3,2,3,0,3,
                3,5,2,3,2,2,0,0,3,2,
                1,2,0,4,5,5,5,5,4,3,
                0,2,5,2,3,0,0,3,5,3,
                4,1,3,5,0,3,2,1,0,1,
                0,1,2,1,4,2,1,0,0,4,
                0,3,4,5,5,1,0,0,5,3,
                4,1,2,5,2,1,0,0,5,2,
                1,5,3,2,0,2,2,1,1,1,
                1,0,0,4,2,2,4,0,0,5};
        int[] Tab1 ={4,0,3,3,2,5,1,3,4,1,
                3,2,2,2,1,3,1,3,3,2,
                4,2,4,2,3,1,1,0,4,0,
                5,1,1,5,2,1,3,1,4,3,
                5,5,1,1,0,2,4,2,0,3,
                5,1,3,2,2,2,0,3,1,3,
                0,5,3,3,3,0,0,3,5,5,
                3,4,3,4,5,0,2,4,3,1,
                4,4,2,2,2,2,1,4,2,4,
                4,5,5,3,3,4,3,5,5,4,
        };
        int[] Tab2 ={5,0,1,3,2,4,4,1,4,0,
                2,3,3,1,1,5,3,4,3,4,
                5,0,0,0,2,4,0,1,5,0,
                0,2,0,1,5,3,4,0,2,0,
                4,2,3,0,1,2,4,0,2,2,
                5,4,4,3,2,0,0,0,3,2,
                0,1,5,3,1,4,4,3,2,2,
                1,5,1,2,1,5,1,4,3,0,
                4,4,3,5,5,3,5,2,2,5,
                1,1,3,3,3,3,2,0,3,5,
        };
        int[] Tab3 ={2,0,4,0,0,3,3,2,2,3,
                0,2,1,3,4,2,0,2,3,3,
                0,3,0,0,4,0,1,5,2,4,
                0,3,2,3,3,4,3,0,1,0,
                3,1,1,4,4,1,4,4,0,3,
                4,3,4,2,5,4,5,3,2,2,
                3,2,2,3,0,3,2,3,0,2,
                2,1,3,3,4,3,2,3,4,3,
                1,1,3,0,4,4,3,5,1,5,
                1,5,3,3,1,3,2,2,4,1,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=18;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=11;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=11;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=8;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }start();
    }
    private void level9S(){
        int[] Tab0 = {2,1,1,0,0,5,3,0,2,3,
                4,0,5,2,4,1,2,2,4,4,
                5,3,0,3,3,5,4,2,4,3,
                2,2,5,0,3,4,5,0,0,0,
                3,5,2,3,1,0,0,3,5,1,
                0,5,5,1,4,3,5,3,4,5,
                4,1,0,1,2,1,0,3,1,2,
                2,5,5,1,5,3,4,2,0,0,
                5,4,1,5,5,4,2,5,0,2,
                2,1,1,3,0,1,1,3,0,2};
        int[] Tab1 ={1,3,5,1,4,3,5,3,4,1,
                1,4,2,0,1,4,5,3,2,3,
                4,1,5,2,4,3,0,5,0,3,
                5,0,4,2,5,0,3,2,1,2,
                5,2,3,0,2,3,3,1,1,2,
                4,2,3,3,0,4,3,4,0,3,
                0,0,1,3,3,3,4,2,5,1,
                4,4,5,4,3,1,4,2,2,4,
                3,0,4,4,5,0,4,1,5,0,
                5,0,3,0,1,3,0,1,3,0,
        };
        int[] Tab2 ={5,0,5,4,5,0,5,5,5,3,
                2,2,0,2,3,4,0,2,2,2,
                3,5,1,1,1,1,1,3,3,1,
                2,1,1,3,2,4,1,2,4,5,
                2,1,3,4,2,2,2,1,4,4,
                4,1,3,3,4,4,2,4,3,1,
                5,5,1,0,0,4,3,4,5,3,
                2,1,1,1,5,2,1,2,4,4,
                2,5,1,0,1,4,0,4,1,0,
                2,4,3,3,1,5,5,3,0,3,
        };
        int[] Tab3 ={1,5,2,4,4,3,2,2,3,2,
                4,0,2,3,0,0,5,1,1,1,
                4,4,0,5,4,4,2,1,2,0,
                4,5,5,3,3,3,0,2,5,0,
                3,4,5,3,0,0,4,3,3,4,
                4,1,2,0,0,4,2,3,2,4,
                0,2,3,0,4,2,2,1,3,0,
                3,1,0,4,4,4,2,2,0,2,
                3,3,1,0,5,0,0,3,1,3,
                1,4,2,4,3,3,0,5,0,2,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=17;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=9;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=9;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }start();
    }
    private void level10S(){
        int[] Tab0 = {4,0,3,5,3,5,3,0,2,3,
                1,2,3,1,4,1,3,2,4,2,
                0,0,4,2,4,5,4,5,3,4,
                5,5,1,0,3,1,3,5,2,3,
                3,0,4,1,3,3,1,4,0,1,
                1,0,0,4,5,4,2,1,4,3,
                1,1,0,4,5,2,3,4,1,0,
                3,4,2,2,4,2,2,1,2,4,
                2,3,3,1,3,4,2,3,1,3,
                3,1,4,5,0,0,3,4,1,2};
        int[] Tab1 ={2,4,2,2,2,0,1,1,0,1,
                3,4,2,1,4,5,2,5,2,0,
                0,2,3,0,3,2,4,0,2,2,
                2,1,1,2,1,2,3,5,0,5,
                3,5,5,0,1,1,0,1,1,2,
                0,4,0,4,3,2,5,2,1,2,
                0,1,5,4,0,1,5,0,3,3,
                1,3,3,5,0,2,2,5,5,4,
                2,2,3,2,2,2,0,1,5,4,
                5,0,0,5,2,2,4,0,5,3,
        };
        int[] Tab2 ={0,4,0,1,2,0,3,0,4,3,
                0,5,2,2,0,0,1,4,5,3,
                1,3,1,4,4,3,4,3,5,2,
                2,4,0,2,5,1,1,4,2,3,
                1,3,3,0,3,0,2,2,0,2,
                0,1,2,4,3,5,2,4,5,5,
                1,3,1,0,1,4,4,5,1,1,
                0,5,4,4,0,1,1,3,3,3,
                4,1,2,4,0,2,0,4,2,2,
                0,2,2,3,5,5,2,4,5,3,
        };
        int[] Tab3 ={0,5,1,3,4,5,4,1,3,2,
                5,0,4,5,1,4,3,5,4,3,
                2,3,2,1,1,5,0,2,0,1,
                0,5,2,5,2,0,0,0,3,5,
                3,0,4,0,0,2,2,0,2,4,
                4,3,5,1,4,0,5,1,3,3,
                0,5,1,0,3,1,1,2,5,0,
                1,5,1,1,1,1,4,1,3,4,
                2,1,5,3,3,1,1,5,0,0,
                3,1,2,4,1,3,2,4,4,4,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=19;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=12;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=9;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }

    private void level1M(){
        int[] Tab0 = {5,2,2,4,3,4,1,2,1,3,5,2,0,4,4,3,0,1,3,2,
                0,0,5,4,1,0,3,0,2,0,0,0,4,4,3,3,2,0,0,3,
                2,4,0,3,5,5,4,1,0,5,0,1,3,5,1,1,1,2,5,3,
                4,4,2,4,1,2,4,2,0,0,4,4,1,5,5,2,4,0,3,3,
                5,4,1,3,4,0,2,3,3,0,4,0,5,4,3,4,4,5,5,1,
                4,2,1,3,3,4,0,5,3,4,5,0,3,5,4,3,1,2,0,2,
                0,1,5,0,3,4,5,1,0,3,4,5,2,0,5,0,2,5,2,3,
                0,0,2,2,2,0,0,5,0,2,5,1,5,3,4,1,4,3,4,1,
                0,1,0,3,4,0,3,5,1,2,0,3,5,2,0,5,3,2,0,2,
                2,3,0,5,5,2,2,0,5,0,0,4,0,0,0,0,1,2,1,5,
                3,3,5,4,3,0,0,2,0,4,0,0,0,0,1,4,5,5,0,1,
                0,3,2,5,5,3,5,5,0,4,5,2,3,2,1,3,0,5,3,3,
                2,2,1,5,2,4,2,5,1,1,4,3,3,0,3,0,1,1,4,1,
                5,2,4,1,4,2,0,4,0,5,0,5,0,5,0,0,1,1,0,0,
                2,0,5,0,0,3,1,4,5,3,3,3,5,2,0,1,3,5,0,5,
                3,4,5,4,2,2,4,5,1,2,3,3,3,3,2,1,3,3,3,3,
                1,0,4,3,4,2,5,2,5,0,0,3,3,5,0,4,1,5,3,4,
                4,3,3,4,1,0,4,5,3,0,2,1,5,1,3,2,5,2,5,4,
                0,2,4,1,3,1,3,1,2,4,1,2,3,4,1,0,1,5,1,3,
                4,4,3,2,0,0,5,2,4,3,5,3,1,5,4,4,3,2,4,3};
        int[] Tab1 ={5,0,0,3,0,4,2,0,4,1,4,4,4,0,1,2,0,0,4,0,
                4,5,2,5,1,2,2,5,2,4,2,4,3,4,1,1,1,5,5,2,
                0,4,0,1,2,1,4,0,4,5,5,4,3,5,0,4,2,1,2,5,
                0,0,3,1,4,2,4,1,5,5,2,3,1,4,5,1,0,1,2,5,
                3,4,4,4,1,1,5,1,0,0,5,4,5,4,3,3,2,3,0,5,
                5,1,2,4,0,0,0,5,5,4,3,4,3,3,5,5,5,3,1,2,
                3,4,5,3,1,4,2,0,3,0,2,2,2,1,3,0,5,2,3,0,
                0,2,5,5,3,3,4,4,1,4,4,2,1,4,5,0,1,0,4,4,
                0,0,2,3,3,3,2,4,4,4,4,0,2,1,4,3,5,3,2,5,
                0,3,1,3,4,1,2,1,5,0,1,4,5,1,4,3,3,3,4,3,
                5,5,2,4,2,3,1,0,0,0,2,3,0,5,3,5,2,5,5,3,
                2,4,1,2,1,2,5,0,3,3,2,4,4,4,0,1,3,5,4,4,
                2,5,1,3,4,5,4,4,2,5,4,5,0,1,4,0,2,0,4,1,
                4,2,1,2,0,3,1,3,4,5,3,2,3,4,1,1,2,1,4,0,
                3,2,5,4,4,5,1,2,2,2,5,1,5,2,4,2,5,0,3,5,
                4,1,1,1,2,0,4,4,5,2,4,2,5,0,3,1,5,2,0,3,
                4,3,3,2,2,5,2,2,4,1,0,2,5,5,2,4,5,0,0,0,
                4,2,2,5,4,4,1,2,2,0,3,2,0,2,1,3,2,3,2,2,
                1,4,2,1,2,5,0,0,3,1,3,4,2,1,2,1,2,2,3,3,
                2,1,5,2,5,5,3,5,5,2,1,5,4,1,2,2,5,2,4,0
        };
        int[] Tab2 ={5,3,3,0,0,0,1,3,0,0,0,2,5,0,2,2,5,3,0,5,
                2,5,1,2,4,4,1,0,0,5,2,4,2,0,2,3,5,5,5,4,
                2,3,5,0,5,4,0,0,1,4,0,5,3,4,5,0,5,0,5,1,
                3,3,1,3,1,5,0,5,5,4,1,2,4,2,3,4,3,2,4,1,
                5,2,1,0,2,3,2,1,2,3,5,0,4,1,5,0,0,4,1,1,
                1,3,0,4,1,2,3,1,5,4,0,4,3,5,0,4,2,1,4,5,
                1,1,0,3,5,3,5,0,1,1,4,3,1,4,4,2,2,4,4,1,
                2,2,5,0,0,2,2,1,5,5,2,0,1,2,4,1,1,0,5,3,
                2,3,2,1,0,2,5,5,5,2,5,0,4,1,5,0,0,0,0,3,
                5,5,4,2,5,1,2,0,1,4,3,4,3,5,0,2,4,3,1,0,
                0,4,3,2,0,5,2,2,4,5,3,4,5,5,3,4,1,1,0,4,
                5,1,2,2,0,3,3,2,5,1,0,3,2,2,1,3,3,5,0,5,
                1,2,1,1,0,3,4,0,3,1,1,0,1,2,2,5,2,4,2,4,
                0,1,4,1,4,4,3,5,5,1,2,4,1,5,1,2,2,0,3,5,
                2,2,4,0,0,1,4,4,5,3,3,5,5,3,5,0,2,3,2,3,
                4,5,1,0,5,1,3,1,5,2,3,4,5,2,2,3,5,2,4,3,
                4,5,2,5,0,1,3,1,5,0,0,3,0,0,1,2,5,1,2,2,
                2,5,2,3,2,4,4,4,4,2,2,0,0,0,5,0,2,1,1,2,
                4,5,2,1,0,2,0,4,4,4,0,5,4,2,4,3,3,5,1,2,
                3,3,4,0,5,1,2,3,4,5,3,2,5,3,4,4,0,3,2,5
        };
        int[] Tab3 ={3,1,0,0,4,3,1,5,1,4,4,1,2,1,5,5,1,4,1,3,
                2,1,5,3,4,3,2,3,5,5,5,1,2,3,2,2,4,2,4,1,
                3,5,2,3,4,1,0,5,5,0,2,4,1,2,1,3,1,2,5,3,
                2,2,1,0,2,4,1,3,5,4,5,5,3,5,0,5,0,0,1,4,
                4,4,4,4,1,4,4,1,3,3,4,0,5,5,2,2,4,3,1,3,
                1,1,3,4,4,3,5,4,1,5,4,3,0,5,1,4,1,2,0,5,
                1,3,1,4,2,0,2,5,4,4,1,4,0,2,0,1,2,1,5,0,
                2,2,2,3,2,2,4,3,2,2,1,1,0,3,2,1,1,4,1,2,
                4,4,3,5,1,4,1,3,2,4,0,1,5,1,2,4,2,2,5,0,
                1,4,2,3,3,5,5,3,3,0,2,5,4,0,3,5,0,5,1,0,
                3,2,1,2,0,2,3,3,4,0,2,0,2,2,1,4,4,3,2,0,
                4,2,1,3,2,3,5,4,1,0,5,4,0,0,0,0,3,1,4,2,
                2,2,3,3,5,1,5,5,0,2,0,1,2,2,1,1,0,4,5,4,
                5,4,0,5,5,1,2,5,2,2,2,1,1,3,2,0,1,1,0,0,
                4,4,3,2,4,1,5,5,0,0,2,4,2,1,0,4,1,0,1,1,
                0,5,3,5,0,3,0,0,2,5,0,1,4,4,3,3,4,4,2,3,
                5,4,2,5,0,3,2,4,5,2,5,5,5,1,0,1,2,5,5,2,
                1,1,3,4,1,4,2,3,0,5,3,0,1,1,5,3,4,0,0,4,
                3,4,4,3,3,3,2,2,1,5,1,4,4,5,2,1,0,1,4,2,
                2,2,4,3,0,0,3,4,0,2,0,4,4,3,5,3,1,3,0,0,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=33;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=27;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=18;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=17;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }start();
    }
    private void level2M(){
        int[] Tab0 = {3,2,4,1,5,2,0,0,5,4,5,2,4,3,0,3,4,1,1,4,
                5,4,2,4,2,3,4,2,1,4,0,0,0,4,2,4,3,2,5,0,
                2,1,4,3,5,0,5,0,2,0,5,1,4,0,0,0,1,4,4,2,
                5,5,4,1,2,5,2,3,2,4,1,0,3,1,2,0,4,1,0,5,
                5,5,2,4,0,5,4,4,1,0,1,1,1,1,4,0,3,0,4,3,
                3,1,2,3,2,4,5,3,4,5,2,1,0,1,2,2,5,2,2,3,
                3,2,2,2,3,4,3,1,4,5,4,2,0,3,4,5,4,4,0,4,
                3,4,5,3,2,0,0,1,1,3,1,5,3,2,3,3,5,5,1,2,
                1,2,1,5,2,5,3,0,0,5,3,5,2,5,1,1,4,0,1,3,
                3,3,3,1,3,2,2,4,2,1,5,2,3,0,2,1,4,4,5,1,
                5,3,0,2,2,4,5,1,5,4,3,3,1,2,3,0,0,2,0,0,
                0,1,2,0,4,5,5,4,0,2,4,5,4,2,4,2,3,2,2,4,
                0,0,2,1,1,5,4,0,4,3,5,1,0,0,2,1,5,4,3,5,
                1,3,3,3,4,4,4,3,2,5,5,4,2,2,3,5,5,4,4,4,
                2,3,4,1,2,4,3,4,2,4,4,1,2,0,3,2,0,4,0,0,
                3,3,2,3,4,2,0,1,2,5,2,3,3,5,3,5,3,4,4,3,
                2,1,2,1,3,5,4,0,0,0,2,3,3,1,5,3,1,4,0,2,
                1,3,4,4,5,1,0,2,5,1,0,3,1,1,2,3,2,3,2,4,
                3,2,3,2,0,3,3,5,1,4,3,0,2,4,3,5,2,5,4,5,
                2,0,3,5,0,3,3,3,3,3,4,4,3,5,1,4,3,0,5,2};
        int[] Tab1 ={1,5,5,0,2,0,4,1,4,3,4,4,1,0,5,3,3,2,4,2,
                3,4,0,2,3,1,1,2,1,0,1,5,0,0,0,4,3,2,1,5,
                0,3,5,0,2,3,0,4,2,4,0,4,3,5,3,5,3,1,4,4,
                2,4,2,0,0,1,1,4,5,0,2,1,3,5,2,5,0,5,0,0,
                4,1,4,4,1,0,0,5,0,0,0,4,1,3,0,1,3,5,2,1,
                2,4,0,1,4,1,4,0,2,1,5,1,5,4,0,2,4,0,2,3,
                5,5,2,3,1,2,5,2,3,0,5,0,2,0,0,5,0,5,3,0,
                1,2,3,0,0,5,0,1,3,4,2,0,1,1,4,1,2,3,2,5,
                2,0,4,1,5,2,1,1,1,5,5,4,5,1,3,4,3,4,4,4,
                0,0,4,5,0,2,0,5,0,1,5,3,3,3,1,3,5,3,3,1,
                1,3,4,3,5,4,0,1,1,4,4,1,2,3,5,2,5,0,2,5,
                2,3,2,4,2,1,4,3,3,4,4,0,5,2,3,1,0,2,5,0,
                5,3,0,5,2,4,5,2,0,5,0,3,5,1,4,0,3,3,1,2,
                4,1,3,2,2,3,3,5,4,4,3,1,3,5,2,1,4,2,1,1,
                1,3,2,1,5,2,1,1,5,2,0,3,2,3,3,3,1,2,2,2,
                5,4,3,1,5,0,5,2,0,3,3,1,5,0,2,3,5,5,2,1,
                0,4,4,4,3,0,2,1,1,3,2,3,2,5,4,0,5,0,4,5,
                5,2,0,0,1,5,0,3,0,4,2,5,0,4,0,0,1,1,4,0,
                3,2,2,3,4,4,1,1,0,2,1,1,0,2,2,4,5,3,4,2,
                0,0,0,1,5,1,2,0,2,5,2,2,3,1,5,2,3,1,1,1,
        };
        int[] Tab2 ={2,3,5,5,5,4,3,2,0,0,4,5,2,3,4,5,5,4,5,0,
                4,1,4,2,2,5,4,5,4,5,4,1,0,1,2,5,4,5,0,3,
                4,2,4,1,2,0,5,3,5,4,5,2,1,3,5,3,0,3,4,5,
                4,1,3,5,0,4,3,1,4,2,3,0,0,4,2,4,3,3,3,1,
                0,4,5,5,1,1,4,5,5,4,4,5,2,0,0,5,1,2,0,3,
                0,3,2,5,1,5,5,1,4,4,0,4,3,0,2,0,2,1,2,4,
                5,2,5,4,3,4,5,4,0,1,5,0,2,0,0,2,4,0,0,3,
                1,1,4,0,3,1,1,0,1,1,1,2,1,0,3,0,3,2,3,2,
                5,3,4,1,4,2,1,1,0,3,3,3,2,3,0,4,1,0,5,0,
                4,0,5,1,1,4,0,0,4,2,2,3,1,2,1,5,0,5,1,0,
                0,2,1,0,5,1,2,4,4,2,2,0,3,4,5,4,5,1,2,0,
                3,1,0,0,3,3,2,5,0,3,3,4,2,4,5,1,3,3,1,3,
                2,1,0,3,2,0,1,5,5,2,5,5,3,0,0,5,1,4,5,4,
                2,1,1,5,5,4,5,5,4,1,5,1,2,5,4,1,3,3,3,5,
                3,3,1,3,0,5,4,5,0,0,4,3,4,2,1,2,4,5,4,1,
                2,4,0,4,5,0,3,1,3,1,5,0,4,1,4,1,3,0,0,4,
                0,0,5,3,3,4,0,5,1,3,5,0,3,4,0,0,2,2,4,3,
                2,0,5,3,2,4,3,3,3,5,5,4,3,0,4,3,3,5,2,3,
                2,4,0,3,5,1,5,1,4,3,2,4,0,2,0,0,4,4,4,2,
                4,1,5,5,4,4,1,3,0,0,5,0,2,1,4,3,3,4,2,3,
        };
        int[] Tab3 ={3,4,2,0,5,4,2,4,1,5,0,3,2,3,0,5,2,2,1,5,
                5,0,5,2,5,1,0,4,2,0,0,5,4,5,4,2,3,2,2,1,
                1,4,3,3,5,2,3,3,0,0,0,2,5,0,3,1,1,2,1,2,
                4,1,2,0,4,2,1,1,5,1,0,2,2,5,0,3,0,2,1,1,
                4,0,0,3,4,5,4,4,3,0,2,4,0,1,5,3,5,0,5,0,
                2,5,3,3,3,2,4,0,4,4,4,2,4,4,4,2,2,3,2,2,
                3,3,3,2,0,0,5,3,0,2,4,4,1,2,5,1,1,3,1,3,
                2,4,4,2,1,1,3,1,2,0,1,5,1,0,3,3,3,5,2,0,
                1,0,1,4,1,3,1,3,4,5,3,0,1,2,3,2,0,0,5,0,
                0,1,1,0,0,5,5,1,3,3,4,2,0,5,3,3,1,2,5,1,
                5,3,5,2,2,4,1,5,3,2,1,3,1,2,5,5,2,4,5,5,
                1,0,5,2,3,4,4,3,3,5,3,4,4,1,3,2,5,0,4,3,
                4,0,5,4,1,3,2,2,2,0,5,0,2,4,2,2,2,0,0,1,
                4,2,4,3,3,5,2,4,0,4,0,0,5,4,4,5,0,5,4,3,
                1,2,1,2,1,4,0,2,5,0,2,4,4,3,4,1,0,5,1,3,
                2,1,4,5,4,5,1,0,1,1,1,4,5,2,5,3,2,2,4,0,
                0,5,3,4,2,3,3,4,3,4,4,0,5,5,0,5,5,0,2,1,
                5,5,4,2,0,0,5,1,3,2,3,5,1,5,2,1,4,2,0,2,
                0,5,2,5,2,2,0,2,4,2,1,3,1,2,2,2,4,1,0,4,
                4,4,0,1,0,5,1,2,1,2,2,5,5,0,3,2,5,0,1,2,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=37;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=25;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=18;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=16;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }start();
    }
    private void level3M(){
        int[] Tab0 ={1,2,5,0,1,2,3,5,2,1,1,2,2,5,4,0,1,3,0,3,
                2,2,0,0,1,5,2,5,5,1,3,2,1,5,0,5,3,4,5,3,
                4,4,1,0,5,5,1,1,4,0,3,2,2,4,5,1,5,5,5,3,
                4,3,4,3,2,4,1,1,5,4,4,1,1,1,5,2,5,0,2,3,
                5,2,5,5,4,3,2,3,0,3,2,0,4,5,2,5,0,3,2,4,
                2,1,2,5,5,2,4,1,3,1,5,0,1,2,1,0,3,3,5,4,
                0,1,2,2,2,4,3,1,4,4,5,3,2,5,3,1,2,2,1,4,
                5,2,5,3,4,5,4,2,0,3,0,4,0,0,5,0,4,0,1,1,
                3,4,2,5,0,0,4,5,5,0,1,2,1,2,1,0,5,1,0,0,
                4,5,3,0,0,0,0,1,1,1,4,3,5,0,2,2,5,0,5,0,
                2,2,4,1,4,3,1,5,2,5,3,3,5,2,3,0,1,4,5,2,
                2,5,3,5,5,4,0,3,0,5,4,2,2,3,4,2,2,1,3,3,
                3,2,2,4,4,4,1,4,3,1,2,2,4,0,1,5,2,5,5,3,
                2,0,4,5,1,1,2,0,5,0,0,2,2,5,3,5,1,4,1,3,
                5,4,0,5,0,0,2,2,2,2,1,3,2,3,3,2,4,4,1,3,
                1,2,4,2,2,5,3,1,1,2,2,1,5,5,4,1,1,5,2,1,
                2,4,0,5,1,3,0,1,1,1,1,4,3,2,2,1,3,4,5,3,
                2,3,0,0,5,3,5,1,2,0,2,2,2,4,2,1,2,5,2,4,
                0,5,0,2,1,4,2,0,4,5,5,1,3,0,3,3,5,5,3,2,
                3,1,5,4,4,3,0,4,0,0,0,0,3,2,0,2,3,0,0,3,
        };

        int[] Tab1 ={4,3,1,1,0,2,5,5,4,5,2,0,5,2,2,0,2,1,0,0,
                1,0,5,3,3,0,1,0,0,0,3,1,2,1,5,2,5,4,1,5,
                5,1,4,5,0,5,4,5,4,4,0,1,5,4,0,4,3,3,2,1,
                2,2,5,3,3,2,0,4,5,5,4,2,2,0,1,4,1,0,1,5,
                5,0,4,0,5,5,4,4,4,3,3,5,5,4,4,2,3,2,1,1,
                3,1,2,0,2,2,2,0,3,4,3,3,0,4,2,5,4,0,1,1,
                5,2,1,1,1,0,4,5,3,2,1,0,3,4,0,3,1,5,0,2,
                0,0,0,1,5,3,5,0,3,5,2,5,5,4,5,4,5,1,5,5,
                3,0,0,0,2,5,2,2,3,3,2,0,5,2,0,5,3,1,1,0,
                2,3,1,2,1,1,2,2,1,2,2,0,3,2,1,5,3,5,1,1,
                1,2,0,2,5,3,4,2,3,2,0,2,0,0,0,2,2,5,5,3,
                4,4,1,4,5,2,4,2,5,5,4,5,3,3,1,3,1,5,2,1,
                0,2,2,3,2,1,3,5,2,4,3,2,1,0,0,2,3,0,5,4,
                1,4,2,0,4,2,4,1,3,3,4,2,2,1,0,5,3,2,2,3,
                5,0,3,2,2,3,5,0,0,0,1,3,5,3,1,4,2,4,2,2,
                3,1,4,5,5,1,3,0,0,0,2,2,0,5,1,4,3,2,2,4,
                0,4,3,0,3,1,4,4,3,5,4,2,3,0,0,1,5,4,0,3,
                3,3,5,0,4,0,4,4,0,5,4,3,0,2,3,3,4,4,1,5,
                3,5,1,2,2,5,5,2,5,4,2,4,4,4,1,2,3,3,5,4,
                3,1,1,2,2,1,1,0,5,5,4,5,0,5,1,4,2,3,5,1,
        };
        int[] Tab2 ={4,3,3,0,4,2,1,4,4,3,5,1,2,1,0,0,1,0,3,0,
                3,4,0,3,2,0,2,1,3,4,5,5,3,2,0,2,0,3,4,4,
                4,4,4,0,5,4,1,0,1,2,0,3,3,5,2,5,3,2,3,4,
                5,5,3,3,0,4,2,4,4,0,4,1,0,1,1,4,3,5,4,5,
                4,5,2,0,3,2,0,0,0,5,5,1,3,5,5,2,1,1,5,2,
                2,1,3,1,3,3,5,5,2,4,3,3,1,3,0,5,5,1,0,0,
                2,1,4,2,2,0,5,0,1,0,2,1,3,2,0,2,5,4,5,4,
                4,3,4,4,0,5,5,1,1,0,0,4,2,2,3,2,3,4,5,2,
                5,2,1,2,3,2,2,5,1,0,4,3,5,0,1,4,0,5,4,5,
                3,0,3,3,4,2,0,3,4,4,4,0,1,3,1,5,1,5,5,2,
                2,1,5,4,5,4,2,4,5,1,5,1,3,5,3,0,3,2,2,3,
                3,4,1,2,5,0,5,5,2,3,5,2,4,1,5,5,1,5,2,4,
                3,3,1,2,0,4,3,2,3,1,1,0,5,0,4,5,0,0,0,2,
                4,5,0,5,3,5,3,2,3,1,0,3,4,2,4,4,0,5,3,5,
                0,0,2,0,1,1,3,1,3,4,5,0,3,5,0,0,0,4,2,1,
                3,5,2,0,0,5,0,5,3,4,0,4,1,3,3,0,3,5,4,1,
                4,4,4,5,5,3,1,0,0,3,1,2,0,5,5,0,3,1,0,4,
                0,4,4,2,5,1,3,4,5,0,4,1,0,2,2,2,1,4,1,3,
                3,4,5,2,1,0,0,1,1,4,0,5,1,3,0,2,3,3,0,0,
                1,5,5,4,4,0,5,1,2,0,5,0,3,4,5,3,0,2,5,2,
        };
        int[] Tab3 ={4,3,2,2,3,2,5,4,0,4,2,3,0,0,1,0,5,5,0,3,
                4,4,3,5,4,1,0,1,0,3,3,1,5,4,1,3,2,3,1,3,
                1,0,3,4,3,3,5,3,2,5,4,0,1,4,3,2,0,1,2,2,
                0,5,0,0,4,3,0,2,4,2,0,3,5,1,4,5,4,4,0,1,
                1,3,2,4,1,4,5,4,3,5,1,3,5,1,4,3,4,3,4,0,
                2,0,2,5,2,0,2,5,2,4,0,1,0,3,2,5,0,3,0,1,
                5,1,4,0,5,1,4,4,1,1,5,0,2,3,5,4,5,2,2,5,
                3,5,3,5,2,5,2,4,4,1,0,0,5,0,0,4,3,4,5,2,
                1,5,2,5,1,3,5,4,3,5,2,2,2,3,3,1,0,2,4,1,
                0,5,3,1,5,1,4,3,0,2,5,2,5,3,3,2,4,5,0,2,
                3,2,3,1,1,4,2,3,5,5,4,3,4,5,4,3,2,0,4,4,
                4,5,0,2,1,4,3,0,1,0,5,0,4,0,5,5,5,0,3,5,
                4,0,2,5,2,5,0,0,2,1,5,5,1,2,3,2,4,1,5,2,
                2,0,0,0,0,1,3,0,2,3,5,1,5,0,4,0,2,2,2,5,
                1,4,5,0,2,3,4,2,1,2,1,2,1,5,1,0,4,3,2,0,
                5,2,1,1,3,4,1,2,0,5,0,5,4,1,5,2,5,3,0,0,
                4,0,4,1,0,5,5,0,3,3,0,4,1,3,5,5,5,4,4,1,
                1,0,5,0,5,0,4,2,2,5,3,5,0,3,1,1,2,5,0,2,
                2,2,5,0,0,4,1,1,2,2,5,1,3,3,1,2,1,2,4,1,
                5,1,3,4,3,3,5,5,1,0,5,2,2,0,5,0,2,2,5,3,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=34;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=25;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=18;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }start();
    }
    private void level4M(){
        int[] Tab0 = {3,1,1,5,4,0,1,2,0,0,5,1,1,2,5,1,5,3,3,0,
                3,5,5,2,5,1,3,4,5,0,4,3,3,0,5,3,1,1,1,3,
                0,1,0,1,2,0,1,0,5,3,3,3,5,0,3,4,0,3,0,2,
                0,5,3,1,1,4,3,2,3,4,4,2,2,5,0,2,4,2,2,0,
                5,4,5,0,5,5,3,0,2,0,1,5,4,3,3,2,4,3,5,3,
                5,3,5,2,2,1,3,4,3,2,4,3,3,1,2,3,1,4,2,3,
                3,3,4,5,0,4,1,3,5,4,2,3,2,4,5,3,2,5,5,1,
                4,2,4,3,3,0,2,0,0,2,5,4,1,1,2,1,3,5,0,0,
                1,4,0,3,0,3,5,4,0,4,5,2,3,1,2,1,5,5,3,1,
                5,0,3,3,1,5,3,1,2,1,3,2,3,3,3,0,0,2,1,4,
                3,5,2,5,3,2,2,2,0,3,1,2,2,5,0,4,1,2,1,4,
                3,4,0,4,3,5,4,0,1,1,3,2,1,4,0,5,1,4,4,1,
                4,1,5,5,4,1,3,4,5,5,1,0,1,3,2,5,4,4,5,1,
                3,3,2,3,4,3,4,0,0,3,2,4,5,5,2,5,0,0,5,4,
                3,1,3,2,3,0,2,4,0,3,0,1,3,5,5,0,3,2,3,5,
                1,5,4,1,1,5,5,0,5,1,0,1,5,3,1,2,2,3,2,2,
                3,1,4,3,5,0,1,4,5,0,0,0,0,1,0,1,0,4,0,5,
                2,5,5,3,0,3,4,3,3,5,1,4,4,2,1,3,2,4,1,2,
                2,5,1,0,4,4,0,4,4,4,5,0,4,0,3,5,2,4,4,2,
                1,5,2,1,1,0,3,0,0,1,3,1,3,3,0,2,4,5,0,3};

        int[] Tab1 ={0,1,0,1,4,3,2,0,5,1,5,4,0,3,5,3,1,0,4,4,
                5,2,4,3,3,5,0,5,2,1,5,3,3,2,1,4,0,4,3,2,
                4,0,4,3,4,5,4,1,4,3,3,0,2,4,4,1,3,0,1,4,
                3,1,5,4,1,1,1,2,0,2,4,5,5,3,3,5,2,3,0,0,
                3,3,3,1,0,4,1,2,0,4,5,5,5,4,0,4,5,1,0,5,
                1,5,3,2,0,2,0,1,2,2,4,1,1,2,5,3,5,3,5,1,
                2,2,5,2,4,3,5,3,4,0,2,1,3,5,1,0,4,4,2,1,
                1,2,0,4,1,2,4,0,3,0,1,3,3,4,2,4,3,4,0,0,
                4,1,2,3,3,0,0,3,0,2,1,0,1,1,0,0,2,0,5,5,
                5,3,1,1,3,4,5,0,5,1,5,2,5,5,0,1,5,5,3,4,
                4,0,2,1,2,0,0,4,2,1,2,0,4,3,3,0,1,5,5,5,
                1,3,4,1,3,1,4,5,3,2,3,4,5,4,0,5,5,5,2,3,
                1,0,3,5,2,5,4,2,0,1,4,4,2,0,5,3,0,1,3,4,
                4,1,2,0,1,5,0,4,3,0,0,2,1,4,5,5,3,2,0,2,
                1,2,3,5,5,0,4,4,0,3,0,3,5,3,4,1,5,5,3,5,
                4,5,4,2,0,1,2,3,5,4,0,0,5,0,0,1,0,4,3,5,
                0,3,4,4,0,2,5,1,1,4,2,2,2,1,2,4,0,4,4,3,
                3,5,2,2,0,0,4,2,3,0,0,5,5,4,0,2,1,4,5,5,
                0,2,4,5,2,5,2,4,0,4,2,0,2,1,2,0,4,1,5,2,
                3,0,3,2,2,2,3,5,5,1,4,0,1,3,0,0,2,2,1,1,
        };
        int[] Tab2 ={1,1,0,1,5,1,0,5,5,1,4,4,2,2,0,2,2,2,0,3,
                2,0,2,3,0,3,2,3,0,3,2,0,1,1,0,3,4,3,2,2,
                5,5,5,4,5,3,2,1,3,4,1,2,2,2,2,0,4,3,5,3,
                1,3,2,4,5,2,2,3,5,5,1,2,0,2,0,5,3,1,1,0,
                1,2,5,3,1,5,4,0,0,2,1,0,5,1,5,5,1,5,0,0,
                0,5,2,4,5,3,3,3,4,4,1,5,1,5,5,0,5,3,5,3,
                4,0,5,2,4,1,1,2,4,5,1,3,1,0,1,1,1,0,4,2,
                5,2,1,2,1,1,2,4,2,2,2,3,2,4,2,1,3,2,1,4,
                3,1,5,0,4,0,3,1,1,4,3,2,0,0,4,3,0,1,2,3,
                3,5,2,2,2,3,1,0,0,0,1,4,2,2,3,1,5,1,1,2,
                4,5,3,0,5,2,4,0,3,3,4,5,2,2,1,4,4,2,2,0,
                5,4,3,0,0,5,4,3,1,5,4,4,0,4,0,5,3,3,2,1,
                1,0,2,5,3,4,2,5,4,2,0,1,5,4,1,1,0,5,4,1,
                5,4,5,3,1,5,0,3,2,5,1,2,2,2,0,2,1,3,1,4,
                0,1,3,3,2,3,5,5,5,2,3,5,5,4,5,5,2,3,1,1,
                0,3,0,3,3,0,2,4,0,1,2,0,5,5,3,5,4,3,2,1,
                3,0,2,1,1,3,1,5,1,5,1,5,3,5,0,2,3,1,3,1,
                2,4,5,4,0,5,0,5,3,2,2,2,3,2,1,1,3,1,1,5,
                4,2,4,3,3,3,4,0,2,0,1,2,4,0,5,2,5,1,1,3,
                2,4,4,0,4,2,2,0,0,2,0,2,1,1,4,4,1,5,5,5,
        };
        int[] Tab3 ={5,1,1,1,1,2,1,1,4,2,2,3,5,5,2,3,1,5,5,2,
                5,0,1,1,3,5,5,5,3,3,1,1,0,2,3,5,2,1,5,4,
                2,5,3,0,3,0,4,2,1,2,1,1,1,1,4,0,1,1,5,1,
                4,3,0,3,2,0,1,4,1,2,1,2,3,2,5,2,4,4,2,5,
                0,5,1,4,5,4,4,5,3,2,1,5,4,2,1,2,3,5,0,5,
                4,4,1,4,2,2,3,2,2,0,0,3,5,4,1,1,5,4,5,5,
                5,3,1,1,1,5,5,5,2,1,3,4,1,0,4,5,5,2,5,0,
                0,5,0,0,1,3,0,5,1,3,3,0,3,0,1,3,2,5,0,5,
                0,2,4,1,0,5,5,2,3,4,5,0,4,5,3,3,2,3,1,2,
                3,0,2,5,2,5,2,0,4,1,2,4,2,1,5,4,5,2,2,2,
                0,4,5,1,2,5,2,3,5,2,1,4,1,4,0,1,3,5,4,3,
                5,0,3,0,1,2,5,1,2,0,4,3,5,3,3,3,2,0,3,3,
                4,1,1,0,3,2,0,5,5,2,0,1,5,2,4,3,2,3,4,1,
                1,5,0,0,3,1,3,2,0,1,4,5,2,3,0,4,5,3,5,5,
                1,2,5,1,4,4,4,3,0,0,0,0,2,0,2,0,3,0,4,4,
                2,5,1,4,3,1,3,2,4,0,1,2,2,5,5,2,0,3,2,1,
                4,3,3,0,5,3,4,1,3,0,0,3,2,1,2,5,4,4,3,2,
                5,0,2,1,1,3,4,5,3,2,2,2,3,4,5,4,5,0,3,5,
                2,3,5,0,2,1,5,4,3,2,1,1,0,1,0,2,5,0,0,0,
                5,2,4,5,0,3,5,0,4,3,2,1,5,3,2,4,4,4,3,4,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=33;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=26;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=16;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level5M(){
        int[] Tab0 = {2,3,2,0,1,1,2,1,1,1,4,1,3,0,3,5,2,5,5,3,
                1,2,0,4,0,2,2,4,5,5,5,3,2,1,0,1,1,2,3,1,
                5,1,3,3,2,5,4,5,3,1,4,2,3,0,4,4,2,0,4,1,
                5,3,0,0,5,1,0,1,5,3,1,5,1,3,1,1,0,3,2,0,
                0,3,1,1,2,5,2,3,0,3,5,3,5,2,5,5,2,0,2,3,
                0,3,4,2,2,3,3,3,3,1,4,3,3,2,2,5,5,3,4,4,
                2,3,1,5,1,0,4,1,2,2,0,2,3,2,2,5,0,3,1,1,
                3,3,2,5,0,4,2,4,2,2,0,2,1,1,0,4,0,1,3,5,
                2,5,5,2,3,3,0,3,4,3,2,3,4,1,3,5,2,3,1,5,
                2,3,5,1,1,1,5,3,3,0,2,0,0,2,3,3,0,4,1,1,
                0,2,2,5,5,2,4,3,3,0,5,2,2,3,1,0,2,1,2,2,
                3,0,2,1,1,4,2,3,3,4,4,4,2,3,5,4,4,1,1,2,
                3,0,2,1,5,2,2,3,0,0,3,5,3,5,3,1,3,1,1,0,
                5,2,5,5,2,0,4,3,0,1,0,4,2,3,4,0,1,3,4,1,
                1,2,1,2,5,3,5,1,2,1,4,4,3,0,5,3,0,4,1,0,
                2,2,3,0,2,3,4,5,1,5,5,4,2,1,2,4,0,0,3,2,
                1,4,3,5,0,5,3,5,5,1,4,4,2,2,4,4,1,3,5,2,
                0,0,1,1,5,0,0,2,5,4,2,3,0,3,1,5,0,3,3,2,
                1,1,5,0,1,4,4,5,4,2,3,4,1,0,2,5,4,0,2,4,
                3,3,3,3,4,3,0,4,4,4,2,2,2,1,4,0,1,3,2,2};
        int[] Tab1 ={0,1,5,3,0,0,1,5,1,3,3,4,2,3,0,3,0,5,2,3,
                1,2,1,1,2,2,0,4,2,3,3,4,1,1,4,2,2,4,0,4,
                5,0,0,1,4,5,0,4,2,0,0,5,0,0,4,2,3,1,2,5,
                4,4,5,1,1,5,3,2,4,3,1,0,3,4,4,5,4,5,3,2,
                0,5,0,3,5,5,1,3,2,0,3,1,3,4,5,0,4,5,5,4,
                4,3,5,5,5,1,3,2,3,3,4,1,0,4,5,2,5,3,4,4,
                4,4,3,2,1,3,0,1,3,2,0,0,0,4,4,1,0,4,3,1,
                0,2,2,2,0,2,5,5,5,0,1,0,3,2,3,5,2,1,2,2,
                0,1,5,1,4,4,1,2,1,1,2,3,2,0,0,0,1,5,5,4,
                4,2,5,0,4,4,5,5,1,5,2,1,5,1,0,3,4,1,1,1,
                4,4,2,2,2,0,3,0,2,3,2,1,4,3,4,2,4,3,2,2,
                4,4,5,4,1,4,5,2,1,5,1,3,2,4,4,2,5,0,1,4,
                2,0,0,0,3,3,3,3,1,5,3,0,3,0,5,3,1,3,2,1,
                2,4,0,1,0,2,0,5,2,0,1,4,1,4,1,4,5,2,2,4,
                2,1,4,4,1,1,5,5,4,2,3,0,5,4,0,2,5,4,2,4,
                5,4,2,5,1,0,4,2,3,4,1,1,4,5,1,3,4,3,3,2,
                4,1,2,2,2,0,4,0,5,0,5,2,4,0,0,0,0,4,3,0,
                2,1,3,0,4,2,3,1,2,4,5,3,4,3,0,2,3,2,1,2,
                3,3,1,1,1,5,0,2,3,0,2,0,5,2,2,2,3,3,2,0,
                4,1,0,3,3,0,3,0,2,1,2,0,4,3,3,5,0,1,2,5,
        };
        int[] Tab2 ={3,5,4,2,3,3,4,0,3,5,1,2,1,1,0,2,5,2,5,1,
                5,5,0,2,3,2,4,2,4,5,0,4,4,5,1,2,3,5,3,5,
                5,5,4,2,2,0,3,2,4,4,0,5,2,0,4,3,5,1,0,5,
                2,5,1,2,4,2,4,1,0,1,3,3,4,3,5,3,4,3,3,4,
                5,3,1,5,2,1,2,5,5,0,5,3,2,4,5,3,3,2,2,2,
                4,2,5,5,1,1,4,2,4,5,2,2,4,3,0,0,2,0,1,1,
                1,1,3,5,5,2,3,5,1,2,2,3,5,2,3,3,5,3,5,0,
                1,3,3,4,0,3,5,4,0,5,3,3,2,4,1,2,1,3,5,5,
                5,2,4,4,3,4,1,5,2,5,4,3,3,1,4,5,1,4,2,4,
                3,0,3,1,2,3,2,5,4,1,1,4,2,0,2,5,0,0,4,5,
                0,5,4,0,4,2,0,1,3,5,1,5,2,5,1,4,3,4,0,3,
                2,5,4,2,0,0,3,5,2,1,2,5,5,2,1,4,5,0,2,5,
                1,1,0,0,1,0,4,4,4,2,1,4,4,4,1,4,1,1,0,2,
                1,4,3,2,2,4,0,2,1,4,5,0,0,3,1,0,0,4,5,0,
                1,0,1,1,5,5,1,4,0,3,3,5,4,1,0,4,4,1,1,3,
                2,5,3,3,0,2,0,0,2,3,3,1,4,0,0,5,2,2,1,2,
                5,0,4,3,3,0,5,5,1,2,0,3,3,3,1,3,3,1,1,5,
                2,3,1,4,0,0,2,3,4,4,2,3,2,3,1,5,3,1,1,1,
                1,4,3,1,1,4,4,2,2,3,1,1,4,5,3,0,4,4,5,2,
                5,0,1,1,0,2,1,0,3,2,5,1,3,2,5,4,4,4,5,3,
        };
        int[] Tab3 ={4,3,4,1,4,4,4,1,5,3,0,4,5,1,1,0,5,1,3,0,
                2,0,1,1,5,4,5,4,0,2,4,4,5,4,3,0,4,2,5,4,
                0,5,2,1,5,0,2,3,1,5,4,1,1,3,2,5,4,0,3,4,
                2,4,0,4,5,4,0,4,3,5,5,4,3,3,2,4,3,2,4,3,
                1,2,2,5,5,4,2,3,3,4,0,5,1,0,0,3,4,2,1,0,
                2,2,2,2,5,2,5,4,3,5,1,0,2,2,5,3,2,5,3,0,
                3,5,3,4,4,4,2,5,1,5,0,3,1,3,1,0,4,2,5,5,
                4,0,1,2,0,4,4,4,1,5,0,4,3,1,5,2,1,1,4,2,
                3,3,5,4,1,5,4,2,1,3,4,3,2,5,5,3,3,4,4,3,
                4,0,0,3,2,4,2,1,2,1,5,0,1,0,1,0,1,3,1,2,
                3,2,4,0,1,1,5,5,1,1,1,4,1,2,4,5,3,1,5,2,
                1,1,2,0,4,2,5,0,5,0,3,4,3,2,0,0,3,2,0,2,
                5,1,0,4,2,4,4,4,1,5,3,3,4,4,4,4,4,5,2,3,
                1,1,0,4,2,3,4,2,0,2,2,0,4,1,0,0,1,5,3,2,
                2,1,0,2,4,1,3,1,0,5,0,2,0,5,2,4,1,5,2,0,
                1,4,5,0,0,4,4,3,5,1,4,5,5,1,1,0,1,5,5,5,
                1,5,4,0,5,4,1,4,3,5,3,4,2,1,0,4,3,2,0,5,
                3,4,1,3,1,5,3,3,5,1,0,2,4,3,1,4,0,2,0,3,
                2,4,4,5,3,5,4,1,2,0,3,4,3,4,4,0,1,2,4,3,
                2,3,5,5,0,4,1,0,4,2,1,3,1,4,2,4,0,0,4,0,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=32;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=26;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=21;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=16;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level6M(){
        int[] Tab0 = {3,2,5,0,5,1,0,0,1,0,1,2,3,1,2,0,1,4,1,4,
                2,4,5,1,0,3,1,3,0,0,5,2,1,3,3,0,2,0,1,3,
                4,4,4,3,5,5,4,0,2,5,0,4,1,4,4,2,4,4,1,3,
                5,0,5,4,0,4,3,2,3,3,3,0,0,4,5,3,5,3,5,3,
                4,3,2,4,0,4,2,2,5,2,0,2,1,0,2,5,2,3,2,5,
                4,2,0,1,4,5,5,0,0,2,5,2,4,5,0,3,1,4,3,2,
                4,0,2,5,4,4,4,4,3,2,1,3,3,3,1,0,5,1,3,5,
                2,4,1,4,2,4,4,2,5,5,0,1,4,2,2,0,5,0,1,5,
                1,5,5,0,5,2,4,1,5,0,4,3,5,5,1,2,5,5,2,1,
                0,5,5,3,1,3,2,1,1,0,0,5,3,4,1,0,0,0,4,2,
                0,4,1,1,0,2,0,0,0,1,5,5,2,0,4,3,1,4,4,1,
                2,3,5,3,2,1,2,1,3,5,1,4,5,4,5,4,5,1,0,1,
                5,0,2,1,4,2,1,0,3,2,1,4,2,2,0,0,5,4,5,3,
                1,2,3,5,3,4,4,4,0,4,5,3,2,0,4,3,3,3,0,4,
                4,1,4,1,0,4,3,1,0,2,4,2,1,5,2,4,2,2,3,2,
                2,0,1,4,0,4,0,2,1,2,1,0,3,2,2,3,0,3,5,4,
                3,5,0,5,0,2,0,0,2,3,2,2,4,5,5,0,0,4,3,1,
                0,2,4,0,2,4,2,4,5,5,0,2,4,5,1,0,1,2,5,2,
                2,4,4,4,2,1,3,1,0,4,0,2,4,5,5,2,5,5,5,4,
                3,4,2,4,4,3,0,5,2,1,1,2,1,0,5,3,0,1,4,0};
        int[] Tab1 ={3,5,3,0,5,5,1,0,1,2,2,5,4,4,2,0,2,1,4,0,
                4,4,5,3,0,4,5,5,2,0,1,1,1,5,1,3,3,1,0,2,
                4,5,3,0,3,0,4,2,4,2,0,5,2,4,1,2,0,0,4,2,
                5,3,4,1,0,3,4,5,5,2,3,3,2,1,1,4,5,5,5,0,
                1,4,4,0,0,4,4,2,2,4,2,2,0,0,5,4,3,1,4,1,
                0,0,0,1,0,3,0,1,1,4,1,3,0,3,3,2,5,5,3,4,
                3,2,1,5,1,0,4,2,3,3,1,1,5,1,2,1,0,1,1,5,
                1,3,2,2,4,3,1,2,4,5,3,5,5,3,4,3,2,2,5,3,
                2,5,5,0,0,3,4,2,1,1,3,4,0,4,5,3,2,0,1,0,
                3,3,4,1,4,4,5,5,4,0,2,5,3,1,2,0,1,3,0,5,
                2,5,1,5,2,2,3,5,0,0,1,2,4,4,4,1,3,4,0,4,
                3,4,2,5,2,3,2,5,0,3,5,4,0,5,0,5,4,0,0,5,
                0,0,3,0,3,5,5,5,1,4,5,5,0,4,0,0,4,3,4,3,
                3,0,4,1,2,2,5,0,3,4,5,1,3,4,1,4,4,3,3,0,
                1,0,4,0,3,1,4,5,2,2,2,0,0,3,3,2,3,5,5,0,
                1,0,1,5,0,2,2,3,1,4,2,5,1,1,1,4,4,4,4,0,
                1,0,0,5,1,3,0,2,0,4,3,1,4,1,0,0,2,0,5,1,
                4,1,5,4,4,3,1,0,1,5,2,4,1,1,1,1,5,1,2,5,
                4,4,4,3,4,3,0,2,4,4,0,4,2,5,4,2,0,0,2,4,
                4,4,0,0,1,4,5,4,1,4,2,1,4,1,0,1,3,3,0,1,
        };
        int[] Tab2 ={2,2,0,4,3,3,2,2,0,3,4,0,0,0,4,3,5,4,3,3,
                3,2,5,1,3,1,0,1,0,3,5,2,3,5,5,4,1,5,3,0,
                1,5,0,5,3,2,4,0,3,1,5,5,5,4,4,0,4,4,5,1,
                1,0,0,4,1,5,1,0,3,0,1,3,0,5,5,0,5,3,2,5,
                5,1,0,1,3,0,0,3,3,5,4,2,3,2,3,2,2,5,3,4,
                2,4,4,2,1,2,4,5,3,5,2,4,0,0,4,3,5,5,2,5,
                0,5,5,2,1,1,3,2,2,2,3,4,2,2,5,4,0,0,0,0,
                5,4,5,4,2,0,5,3,4,2,3,3,3,5,4,2,3,0,4,5,
                4,2,2,4,5,1,5,2,4,4,3,5,4,1,0,4,0,2,4,1,
                1,3,0,0,5,1,0,1,5,2,0,4,2,5,4,0,0,0,5,2,
                1,0,1,2,5,3,1,5,3,1,5,0,1,2,5,5,2,1,0,3,
                0,5,3,0,4,0,1,5,5,1,0,3,5,0,4,3,0,2,5,5,
                0,1,0,3,5,0,0,1,4,0,2,4,4,1,1,4,4,3,2,4,
                5,0,0,1,2,3,1,2,5,3,3,3,2,0,4,4,5,0,3,3,
                1,2,0,4,3,5,4,2,1,4,3,5,1,0,2,2,4,0,5,3,
                5,1,3,2,5,5,5,4,0,2,3,2,5,1,2,5,2,4,0,4,
                5,0,0,1,2,2,5,5,1,1,5,0,4,0,2,1,4,2,5,4,
                3,5,0,1,5,0,4,4,3,2,5,1,1,0,1,1,0,2,1,2,
                5,2,3,3,4,2,3,2,3,2,5,0,5,4,4,0,5,4,5,4,
                3,1,1,4,0,0,2,5,0,4,3,5,5,2,4,0,1,2,2,5,
        };
        int[] Tab3 ={0,3,0,2,3,4,0,4,5,2,0,0,1,1,3,1,2,0,2,3,
                0,4,1,5,4,2,2,4,1,1,2,2,3,4,2,0,1,3,5,3,
                4,3,0,3,0,5,1,4,3,2,1,2,1,4,1,0,2,5,5,3,
                5,4,3,4,1,5,4,5,1,2,3,3,3,2,1,5,3,3,3,4,
                2,2,1,3,1,5,3,3,5,0,3,0,1,4,0,0,4,0,2,5,
                3,4,1,1,4,2,4,5,4,1,5,5,4,5,1,3,0,5,4,3,
                2,1,4,4,3,2,0,0,0,4,2,4,5,4,2,3,4,3,0,5,
                5,1,1,4,4,1,0,1,3,5,0,3,2,2,0,3,3,2,4,2,
                2,3,4,4,3,4,2,3,3,3,1,3,0,2,4,0,5,4,2,4,
                5,4,1,5,4,1,0,1,0,3,5,1,4,1,3,2,5,4,0,2,
                0,4,1,5,0,0,3,1,4,2,5,3,3,5,2,0,3,0,3,5,
                3,0,1,0,5,0,2,0,1,3,1,2,3,4,3,2,2,0,4,4,
                4,3,1,4,4,1,2,1,0,2,1,1,2,1,2,2,2,0,4,2,
                3,3,3,1,1,2,5,5,2,0,2,2,2,2,3,1,2,1,1,4,
                4,5,4,1,4,1,3,5,0,4,2,5,0,3,4,1,2,2,0,0,
                3,1,4,2,5,3,0,5,3,5,4,0,2,3,1,2,4,4,3,2,
                0,0,1,3,0,0,3,4,5,4,0,1,3,4,0,1,2,5,4,0,
                3,0,4,0,1,4,4,4,0,1,1,0,3,2,3,4,4,0,5,3,
                3,2,5,2,4,1,2,3,3,5,3,5,3,3,5,4,1,4,0,3,
                5,2,1,5,5,2,3,3,4,0,2,2,3,5,4,1,1,3,5,2,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=33;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=21;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level7M(){
        int[] Tab0 = {3,5,0,4,3,4,3,1,4,2,0,5,3,1,0,0,3,1,3,5,
                1,1,2,1,3,4,1,2,0,2,1,4,5,1,5,4,1,0,3,4,
                0,2,2,4,1,4,2,5,5,5,5,0,5,0,5,4,1,2,5,2,
                2,2,0,0,4,0,0,1,2,5,0,1,2,5,1,5,0,5,0,1,
                0,1,5,3,3,1,4,4,3,5,3,3,0,4,2,0,3,5,0,3,
                4,0,5,0,0,4,5,4,0,4,2,1,4,1,4,0,1,3,5,5,
                3,2,4,0,2,2,4,3,2,2,1,5,3,2,4,1,2,4,1,1,
                1,1,2,4,2,4,5,2,1,0,0,4,0,3,0,3,1,3,2,5,
                4,4,0,1,0,2,2,1,5,3,3,5,0,2,3,2,1,0,0,2,
                1,1,1,0,5,2,3,4,1,2,2,4,4,1,2,5,4,3,0,1,
                1,1,3,4,1,2,2,0,2,1,5,3,3,5,2,4,1,2,2,1,
                1,4,4,5,1,1,2,2,1,3,0,4,4,1,4,0,5,5,3,1,
                1,1,3,5,4,1,3,0,0,1,1,2,5,2,1,3,2,3,5,0,
                4,3,1,1,5,5,5,4,1,3,4,5,3,1,0,4,0,2,1,2,
                5,0,4,0,3,3,2,1,4,3,2,1,4,0,3,4,5,1,4,4,
                1,0,2,5,5,3,3,4,4,0,5,5,1,2,2,4,1,3,4,4,
                4,4,2,0,3,1,5,2,4,1,1,4,5,2,0,2,2,4,4,0,
                4,0,5,0,5,3,4,5,2,3,5,4,4,5,4,1,2,1,0,3,
                5,3,0,1,5,0,0,0,0,3,1,0,3,3,3,2,5,3,4,5,
                3,0,1,3,2,2,3,0,1,4,2,4,0,3,4,1,1,2,5,2};
        int[] Tab1 ={0,4,4,3,1,2,1,3,0,1,0,3,3,0,4,4,1,0,0,5,
                0,4,1,3,3,1,4,1,3,5,3,1,3,3,3,3,2,2,5,3,
                5,4,3,3,4,1,0,0,1,2,5,4,1,2,0,1,3,3,0,5,
                1,0,1,3,4,5,1,4,4,0,4,0,1,4,3,2,1,4,0,1,
                5,4,0,0,3,0,5,4,4,3,5,0,1,3,2,2,0,1,0,1,
                5,5,0,5,0,1,2,0,2,0,0,0,1,4,5,2,1,2,5,5,
                4,0,5,5,4,5,4,2,2,5,3,4,0,3,0,3,2,2,2,4,
                0,1,0,3,0,0,4,2,2,3,1,4,3,2,5,2,2,5,5,1,
                4,3,0,0,0,4,0,5,0,1,0,1,2,1,2,3,4,0,0,2,
                2,0,5,5,4,2,0,4,1,1,5,5,2,5,2,0,4,1,4,2,
                3,4,4,1,0,1,4,1,1,2,5,1,4,4,3,5,4,1,5,3,
                3,3,1,4,4,3,4,0,3,3,4,2,0,3,3,0,4,5,5,4,
                3,4,2,3,5,2,2,3,5,5,3,0,5,4,5,4,2,5,0,5,
                5,3,2,2,1,2,5,3,5,1,3,5,2,2,5,1,1,1,0,5,
                3,4,0,1,0,0,1,3,5,1,1,2,5,1,5,4,4,5,3,3,
                1,0,0,5,1,4,0,2,3,4,5,1,4,2,1,5,5,3,5,0,
                1,5,1,3,0,2,1,0,5,0,0,2,4,0,0,1,5,0,2,0,
                0,4,1,2,0,3,1,1,0,3,1,5,0,5,0,2,0,2,0,1,
                5,1,2,1,4,4,1,2,2,1,5,4,1,2,4,2,4,3,3,3,
                4,2,5,0,1,0,0,4,4,2,4,0,5,4,4,1,1,3,4,1,
        };
        int[] Tab2 ={5,0,1,0,4,4,2,3,4,2,2,1,5,0,4,4,3,3,5,3,
                4,1,4,1,0,5,2,0,1,3,1,0,2,1,4,2,4,3,1,5,
                5,4,2,2,5,5,3,0,4,2,5,3,2,0,2,2,1,0,2,4,
                3,5,0,5,4,3,4,5,2,3,0,1,3,0,4,4,4,1,1,1,
                0,4,0,3,3,4,5,0,0,4,0,0,3,0,2,5,2,1,1,5,
                0,3,0,4,3,2,2,4,2,2,4,1,0,3,4,1,2,4,4,1,
                0,1,2,1,1,3,4,5,1,1,1,0,5,4,4,5,3,3,0,2,
                2,1,4,4,4,1,1,5,5,4,4,2,4,0,1,5,5,2,0,3,
                0,1,2,3,3,1,0,2,4,4,5,4,1,0,2,4,1,2,3,0,
                1,1,2,2,3,1,5,4,1,1,2,0,0,5,0,4,5,4,2,0,
                4,1,4,4,3,3,5,1,2,0,4,5,0,0,2,1,4,0,2,1,
                3,5,3,5,2,4,5,0,1,4,1,1,0,2,4,1,5,2,4,3,
                4,4,0,4,4,3,2,2,4,2,3,0,2,4,0,3,1,2,1,5,
                0,2,4,4,2,2,4,2,1,3,5,0,3,2,0,5,1,1,3,4,
                4,0,5,1,2,1,4,4,2,3,2,5,4,1,5,3,3,5,5,2,
                0,3,4,4,0,0,4,0,5,2,5,5,5,0,0,5,0,3,1,2,
                0,3,2,3,5,0,5,4,3,5,1,3,2,2,4,0,2,4,4,3,
                2,3,1,4,2,3,4,4,3,0,5,5,4,0,0,4,5,2,4,5,
                1,2,3,3,0,5,4,2,4,0,5,3,0,4,2,2,4,4,0,0,
                5,1,5,2,3,3,0,5,0,4,0,3,1,4,3,2,2,5,3,4,
        };
        int[] Tab3 ={3,3,5,5,2,5,5,1,0,0,0,4,5,4,5,5,3,3,1,3,
                1,5,2,3,3,1,2,5,4,4,2,4,3,5,4,0,1,0,1,1,
                4,0,2,5,4,3,0,1,3,4,1,0,4,5,5,1,1,5,2,4,
                4,1,4,5,0,2,4,3,2,1,0,0,1,2,3,1,1,1,4,3,
                1,5,4,1,2,2,5,3,1,5,3,4,4,3,5,3,4,4,5,2,
                5,5,1,0,2,5,5,1,3,4,5,2,0,2,5,3,2,0,3,1,
                3,3,2,3,3,3,3,0,1,2,4,2,1,3,0,3,0,0,0,0,
                2,1,4,2,4,1,3,4,1,0,4,0,0,1,5,2,4,2,0,4,
                1,2,4,5,5,0,1,5,0,3,5,3,3,2,5,4,5,1,4,0,
                3,1,4,0,4,2,1,0,4,5,5,0,0,5,0,0,4,1,0,2,
                5,5,1,0,3,0,5,0,4,4,3,2,3,2,5,3,1,1,5,1,
                1,3,2,3,1,3,1,0,5,1,5,0,2,1,4,3,0,2,1,2,
                2,5,5,3,4,1,1,2,0,2,4,3,0,3,3,1,1,1,5,1,
                0,3,2,1,4,3,0,3,5,5,2,1,2,5,5,1,5,4,4,5,
                2,1,1,5,2,3,5,0,1,5,1,1,4,4,3,2,4,1,5,2,
                1,5,5,4,4,4,0,1,2,0,1,5,1,0,1,4,1,0,1,2,
                3,5,2,5,0,4,1,3,4,4,0,1,2,2,4,3,3,0,4,2,
                0,0,2,1,2,3,3,4,5,0,5,0,1,1,5,2,4,2,4,0,
                2,5,4,3,1,1,4,4,0,1,4,2,2,5,3,4,2,1,3,2,
                5,2,2,1,1,3,1,4,1,4,4,2,2,0,3,0,3,3,3,4,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=35;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=23;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level8M(){
        int[] Tab0 = {3,2,1,1,3,3,5,0,0,5,3,4,4,3,3,3,2,4,5,2,
                5,0,1,1,2,0,1,0,5,1,0,3,1,1,1,4,5,3,0,3,
                2,2,0,3,5,3,1,2,1,2,0,5,5,0,4,1,2,3,4,5,
                1,1,2,2,1,2,0,1,5,0,5,1,0,4,1,1,2,3,0,0,
                1,0,3,0,1,0,1,5,5,0,4,1,1,5,3,0,4,3,0,5,
                4,5,0,0,1,1,2,5,3,5,1,5,0,5,4,3,3,0,4,5,
                4,3,1,1,5,3,4,2,0,5,5,2,4,3,0,2,0,0,3,4,
                0,2,3,3,3,0,5,0,1,2,1,3,1,4,5,0,0,5,3,3,
                4,1,4,3,2,1,0,2,1,5,3,0,4,3,5,5,2,2,0,0,
                0,4,0,3,0,2,0,2,1,5,2,2,5,3,2,5,5,0,4,2,
                4,1,5,1,1,1,4,1,5,5,2,1,1,2,0,4,5,3,2,0,
                3,3,0,0,5,5,5,5,4,0,0,0,5,3,1,2,5,5,2,3,
                4,2,2,3,1,5,4,5,4,2,3,5,4,5,0,5,5,5,4,3,
                2,2,3,3,4,3,1,4,1,1,2,2,3,2,0,2,2,1,4,2,
                5,3,0,0,4,5,4,1,4,3,4,4,4,2,1,1,5,2,2,0,
                5,4,5,2,0,0,2,0,1,2,2,5,0,0,4,3,0,2,0,2,
                0,1,1,3,5,4,0,0,4,4,1,3,2,4,0,0,5,3,4,5,
                4,2,3,0,4,4,1,2,5,2,5,2,5,4,5,0,0,2,2,2,
                4,3,3,5,4,1,1,2,2,4,2,5,0,3,2,1,0,3,1,3,
                1,1,3,0,5,1,1,0,0,0,5,3,0,3,4,5,0,5,4,2};
        int[] Tab1 ={2,5,0,1,0,4,3,5,2,1,0,4,4,1,4,1,1,1,5,2,
                0,4,5,3,0,4,1,0,1,3,1,2,5,0,4,2,0,5,5,1,
                4,4,4,5,4,1,5,1,1,0,2,1,0,2,5,0,1,5,2,5,
                4,2,3,3,5,2,1,1,0,1,0,2,4,2,2,2,2,2,5,2,
                2,5,3,5,2,1,3,0,3,3,4,4,4,5,4,4,3,0,0,5,
                3,4,0,5,1,3,1,4,0,0,4,5,3,1,2,0,0,2,1,0,
                3,3,1,5,2,0,4,1,2,5,4,3,0,3,5,3,4,4,4,4,
                2,2,2,5,1,4,4,4,3,3,4,0,2,3,3,2,2,4,1,1,
                0,5,1,4,4,5,4,2,4,1,3,4,0,2,1,4,4,5,0,4,
                2,2,2,4,4,0,4,0,3,2,3,4,1,1,2,0,2,3,2,2,
                5,2,5,3,4,3,1,0,0,0,4,2,2,1,0,2,3,5,2,0,
                2,4,4,2,1,3,2,5,2,4,5,4,2,1,3,5,2,5,3,1,
                1,4,0,2,4,5,1,5,3,4,2,1,1,4,3,2,0,0,2,5,
                5,2,3,0,2,3,4,4,2,3,5,2,0,5,2,2,4,2,0,0,
                4,3,2,3,1,1,4,4,5,4,3,1,4,3,0,1,1,1,3,4,
                0,1,3,0,3,2,5,5,5,0,0,1,1,4,5,2,1,1,5,4,
                4,0,5,4,5,2,5,5,3,3,0,3,1,0,5,1,0,0,2,3,
                4,0,3,4,0,1,0,3,3,2,2,4,5,4,5,3,4,5,2,2,
                5,3,2,1,5,5,5,1,0,3,3,1,3,5,4,0,2,0,3,1,
                0,3,3,2,3,0,2,3,0,2,2,5,0,0,4,3,0,5,2,5,
        };
        int[] Tab2 ={2,3,1,5,3,1,0,3,2,1,5,3,5,1,2,3,1,5,5,3,
                2,3,4,3,2,5,2,5,2,1,1,5,1,4,1,1,3,1,1,0,
                0,0,0,5,2,5,1,3,2,3,2,0,1,4,5,0,3,3,1,5,
                0,3,1,0,5,1,2,5,1,4,4,4,2,1,5,5,2,3,2,5,
                2,0,1,4,2,5,4,5,1,5,2,5,0,1,3,3,4,4,1,1,
                0,5,2,1,4,3,2,1,5,0,4,3,5,2,4,1,1,1,2,3,
                4,3,0,4,5,4,0,2,0,3,1,1,4,2,5,0,3,1,4,5,
                2,4,4,5,3,3,3,4,4,1,0,4,3,3,1,4,3,5,5,2,
                2,3,2,5,3,4,2,3,1,2,1,1,3,2,5,0,5,4,5,3,
                3,4,3,1,1,5,3,2,5,1,4,0,2,5,3,5,4,4,3,5,
                4,4,1,0,1,5,4,1,2,4,4,2,0,2,2,3,5,4,0,5,
                1,0,2,4,0,0,4,2,2,0,5,4,3,1,2,2,3,5,4,4,
                0,4,1,3,2,5,1,0,4,1,0,5,5,2,2,5,1,2,5,2,
                3,3,4,2,1,3,4,3,5,5,0,2,3,4,5,2,3,5,2,4,
                3,0,5,4,2,2,1,3,4,3,5,4,5,2,1,3,4,4,5,4,
                3,4,5,1,5,4,0,4,2,3,4,2,2,1,3,3,3,5,4,4,
                3,1,4,1,3,4,1,0,4,0,5,0,2,2,0,2,1,4,5,3,
                4,3,0,0,2,0,0,2,1,2,2,5,4,3,5,1,4,5,1,1,
                0,1,2,3,5,0,3,5,2,3,1,0,3,1,3,3,1,4,2,0,
                2,2,3,4,2,3,0,1,3,4,2,5,0,4,2,5,0,3,5,3,
        };
        int[] Tab3 ={4,4,1,5,2,2,3,0,1,0,1,3,0,1,2,5,0,2,4,5,
                2,3,4,1,0,2,1,0,4,4,4,3,3,0,2,4,0,2,3,1,
                0,4,5,5,0,0,1,3,3,4,5,1,4,3,5,5,5,3,1,2,
                3,2,1,4,0,5,5,4,2,5,2,5,2,1,4,4,1,3,5,4,
                2,3,1,2,3,4,2,0,1,5,0,2,5,5,1,0,2,1,4,5,
                4,2,0,1,3,4,3,1,5,2,5,1,2,0,0,2,2,1,3,3,
                5,3,3,0,0,2,3,5,3,1,1,1,3,5,0,0,1,1,5,0,
                5,5,1,0,4,2,4,2,3,0,3,3,2,3,0,4,5,4,5,1,
                3,0,1,3,4,1,2,4,1,2,5,1,0,5,1,2,0,2,4,4,
                4,2,5,1,1,1,3,0,2,3,0,5,3,0,4,2,1,0,5,0,
                1,3,4,4,0,3,3,2,5,2,2,0,3,0,5,0,2,0,4,2,
                1,0,1,2,5,4,4,1,4,1,2,1,1,4,5,1,3,3,1,4,
                5,3,1,4,2,4,0,4,0,5,3,3,4,4,0,1,5,1,2,2,
                4,4,5,1,2,2,3,3,5,3,5,1,5,3,2,5,2,1,1,2,
                1,4,4,1,5,4,2,2,2,1,1,5,0,5,0,1,3,3,3,5,
                5,5,4,2,4,2,4,5,2,1,2,3,2,4,5,2,0,5,1,4,
                2,2,3,0,2,1,2,4,1,3,2,3,4,0,1,5,2,4,2,2,
                4,4,5,1,1,3,3,3,1,2,3,2,4,5,2,3,1,2,2,1,
                3,4,0,5,5,5,2,2,5,2,1,1,2,0,3,1,1,0,1,5,
                4,1,3,2,0,3,3,3,4,1,2,0,1,0,4,3,3,2,3,5,
        };

        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=30;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=22;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=26;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level9M(){
        int[] Tab0 = {4,5,3,2,0,5,3,1,1,0,0,1,1,2,4,3,0,2,3,3,
                0,2,4,2,3,2,1,5,2,3,0,0,4,4,2,2,4,4,5,2,
                2,4,0,2,1,5,1,5,3,3,0,2,1,0,0,1,0,0,4,5,
                0,2,4,5,3,1,3,0,0,0,3,0,0,1,5,4,4,0,4,5,
                3,0,2,1,4,2,4,5,3,1,1,0,1,4,3,0,5,1,4,1,
                2,3,4,5,2,5,0,2,5,5,1,1,0,1,2,3,5,2,3,5,
                0,5,5,5,4,1,3,3,3,5,2,0,0,5,3,5,1,1,5,1,
                0,5,0,5,0,0,0,2,2,4,5,1,4,5,3,2,4,0,5,2,
                5,3,3,2,1,1,5,0,1,4,1,0,1,0,0,3,0,2,2,0,
                2,2,2,3,2,0,5,0,5,4,3,1,1,1,0,0,5,0,5,1,
                5,4,1,2,4,4,5,1,2,2,4,2,5,2,0,1,4,0,5,3,
                5,3,1,2,0,0,2,1,2,4,0,5,0,5,3,2,3,4,3,4,
                5,1,1,0,1,1,2,3,0,4,0,2,4,3,1,4,4,1,5,2,
                0,3,3,1,5,2,2,5,4,3,5,1,0,3,0,1,2,2,2,5,
                1,3,2,3,0,3,4,2,2,0,2,0,3,1,4,2,5,0,1,4,
                4,1,3,0,1,4,2,2,4,2,0,3,5,1,1,1,4,3,5,2,
                4,2,2,0,2,3,3,4,1,5,2,4,1,2,5,4,3,3,5,2,
                2,2,3,4,3,4,5,4,4,2,2,3,1,0,2,5,3,2,1,1,
                1,4,2,0,3,3,0,4,2,3,2,2,4,5,3,4,0,1,4,3,
                4,2,1,3,4,4,3,0,3,4,2,3,2,4,0,1,0,4,0,4};
        int[] Tab1 ={1,5,1,3,4,2,3,0,0,3,4,0,5,4,0,3,1,4,0,3,
                1,1,3,2,1,0,2,1,5,3,5,4,0,3,0,1,1,0,5,2,
                5,1,3,1,5,2,3,5,5,2,5,1,5,0,4,2,3,4,0,3,
                5,5,2,5,4,2,2,1,5,4,1,4,2,4,4,2,5,1,5,5,
                2,0,4,1,4,4,2,4,3,5,5,2,0,2,4,5,1,0,1,5,
                1,3,0,5,5,4,0,2,2,1,2,3,3,3,5,5,5,2,4,0,
                0,4,5,5,4,1,2,3,4,1,4,5,5,3,0,3,0,0,1,1,
                0,0,2,0,2,2,3,0,2,0,2,4,1,0,1,4,4,2,4,1,
                4,4,3,2,5,0,1,2,4,0,4,1,1,4,3,5,2,4,3,2,
                5,2,3,2,4,0,0,0,4,2,3,2,1,3,1,1,2,4,3,5,
                4,5,0,3,1,5,3,0,0,2,1,2,5,0,3,2,2,3,3,3,
                0,5,1,5,4,1,5,2,0,5,1,3,5,5,3,2,2,1,5,1,
                5,1,1,2,2,4,4,4,4,2,5,1,1,3,0,0,4,1,4,1,
                3,1,1,0,1,5,0,5,1,1,1,1,1,2,5,2,4,0,0,2,
                3,0,4,5,3,0,4,3,2,0,3,3,5,4,5,5,3,2,0,5,
                0,5,0,5,1,0,5,5,4,3,4,0,0,4,3,2,0,5,5,5,
                2,2,4,2,4,5,5,3,2,3,5,5,4,0,5,1,4,4,2,0,
                2,1,2,1,1,1,2,0,1,2,5,3,3,4,4,1,3,1,5,0,
                0,2,5,5,1,0,3,0,2,4,2,5,4,1,0,5,3,1,1,5,
                2,4,5,5,2,4,3,1,5,0,4,3,4,3,0,5,1,4,2,1,
        };
        int[] Tab2 ={0,4,3,3,3,3,0,4,5,1,5,2,0,1,3,0,2,1,2,0,
                2,1,5,2,3,4,3,2,5,5,3,3,4,5,0,3,0,3,0,3,
                2,5,3,4,5,1,2,2,2,5,0,3,4,0,0,0,1,0,1,1,
                0,1,5,3,3,2,2,1,1,2,5,5,2,5,4,1,2,4,1,2,
                4,3,0,3,0,5,5,4,2,1,0,4,4,2,0,2,5,1,2,0,
                3,3,4,5,2,3,3,2,0,0,2,4,5,3,1,5,3,1,5,2,
                4,2,1,3,5,3,2,0,4,4,0,4,0,5,5,4,2,4,4,2,
                3,5,3,4,0,1,4,2,4,0,1,4,0,4,5,0,5,5,1,5,
                1,5,3,5,2,3,2,3,4,4,3,5,4,4,5,5,2,3,5,5,
                1,4,0,5,3,5,3,1,4,1,2,1,1,2,2,3,0,3,2,5,
                1,2,0,3,2,1,4,0,2,5,3,5,1,2,3,4,1,2,4,4,
                5,2,5,4,0,1,3,4,0,2,2,3,5,2,4,5,4,4,3,3,
                0,5,5,1,4,1,4,5,4,2,0,3,3,3,0,2,5,0,3,4,
                2,4,2,3,2,2,4,4,2,2,3,0,4,1,4,0,3,2,4,4,
                4,5,0,3,3,1,3,4,1,5,3,4,4,0,1,4,2,4,2,2,
                1,5,5,4,4,2,3,1,3,2,1,2,1,1,5,5,3,1,2,2,
                3,0,0,2,1,4,3,4,4,2,0,4,4,2,4,0,4,5,4,4,
                4,0,1,5,0,0,0,3,0,4,5,4,1,4,1,3,0,5,3,5,
                4,0,5,1,3,4,4,4,1,4,4,1,5,5,3,3,0,2,0,3,
                5,5,1,5,3,0,5,0,0,3,3,2,0,2,3,5,1,0,2,2,
        };
        int[] Tab3 ={3,1,1,1,4,2,0,0,4,4,2,3,2,5,1,2,1,5,4,5,
                3,2,3,5,0,3,3,2,1,0,2,0,3,1,3,5,1,4,1,4,
                5,3,3,2,0,0,4,1,1,5,1,5,2,3,2,0,0,5,1,1,
                1,2,0,0,2,2,3,5,4,2,1,0,4,3,2,2,1,5,3,5,
                1,3,1,3,2,5,2,0,5,0,2,4,1,0,2,2,0,1,2,4,
                5,5,2,2,4,2,5,5,4,4,5,1,1,0,5,5,0,5,2,2,
                2,1,3,4,3,4,1,4,3,2,1,1,1,3,1,5,4,1,1,3,
                0,0,0,0,1,5,4,0,2,0,0,3,0,1,3,0,3,2,3,2,
                2,2,3,4,2,2,2,5,0,1,0,4,1,1,1,5,3,0,0,2,
                0,2,5,4,2,4,1,4,4,5,3,2,0,1,3,0,2,1,5,5,
                2,4,3,4,3,5,4,1,2,4,4,4,0,4,4,3,4,0,2,1,
                5,0,1,3,0,2,0,3,4,4,2,1,1,1,0,2,4,4,4,0,
                5,3,1,1,3,1,5,3,4,4,1,4,2,5,4,4,0,0,3,4,
                4,2,0,3,0,1,0,4,3,3,1,1,2,4,4,0,0,0,2,0,
                1,3,3,1,3,2,4,5,2,5,5,4,3,1,2,4,1,2,1,2,
                1,4,0,3,5,2,1,4,3,1,3,0,2,2,5,2,1,1,2,2,
                1,4,1,1,5,1,0,4,1,1,1,2,2,5,2,5,0,5,4,5,
                4,0,4,5,5,2,5,5,4,4,0,5,2,4,2,0,4,2,2,2,
                5,5,1,2,0,3,5,0,5,1,4,0,3,4,5,5,1,3,4,2,
                3,0,2,1,5,4,2,4,3,2,5,2,1,5,2,3,4,2,5,0,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=35;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=23;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=18;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=14;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level10M(){
        int[] Tab0 = {5,2,1,2,5,2,0,3,4,1,1,4,1,0,3,4,5,0,5,5,
                0,1,3,3,5,0,1,5,4,5,1,3,3,3,5,5,4,0,5,1,
                3,2,3,4,2,4,0,3,5,3,1,3,4,3,1,5,4,1,4,3,
                2,5,2,3,3,1,4,0,2,3,5,5,3,5,0,2,2,1,3,0,
                2,5,2,1,1,2,5,1,5,1,0,1,5,0,4,3,5,3,0,1,
                4,0,5,5,2,4,0,1,1,4,1,2,3,1,2,2,3,2,4,2,
                0,4,5,3,4,5,0,5,3,3,0,2,4,3,3,2,4,5,0,1,
                5,4,1,1,5,3,0,1,5,4,5,4,2,4,3,2,1,1,1,4,
                0,1,4,2,1,2,1,0,4,2,4,5,2,1,4,1,0,3,3,1,
                4,2,1,5,5,2,4,1,0,3,5,0,2,0,1,5,1,2,0,5,
                1,5,3,4,1,2,3,0,0,2,1,5,1,4,0,3,3,5,0,2,
                4,2,1,3,5,4,3,0,0,0,2,3,3,5,3,5,1,4,3,4,
                0,3,3,3,4,5,1,0,4,0,5,2,2,2,1,3,0,4,3,2,
                1,0,3,1,2,4,4,3,5,1,3,1,2,4,2,2,5,4,0,1,
                2,4,1,2,5,2,2,4,0,0,3,3,1,5,2,5,1,2,2,5,
                5,4,5,3,2,4,0,2,3,3,4,2,5,4,4,3,1,0,5,3,
                3,2,1,4,2,4,2,0,0,0,5,3,5,2,2,5,3,4,1,3,
                3,5,3,5,1,3,2,2,4,3,1,0,1,2,5,5,1,4,0,1,
                4,2,0,2,0,2,0,3,4,5,1,3,2,3,2,0,0,3,1,0,
                3,0,1,3,1,0,5,2,1,2,1,5,1,1,5,3,4,4,4,5};
        int[] Tab1 ={1,2,0,2,3,1,5,0,5,1,2,3,2,0,4,5,1,2,2,1,
                0,0,3,3,4,2,5,1,5,3,5,0,3,4,2,1,2,5,5,5,
                2,3,4,4,2,3,4,4,1,0,5,5,3,5,0,0,5,5,2,2,
                2,5,2,2,3,3,4,1,3,5,5,5,3,0,1,4,5,1,1,4,
                2,1,1,4,0,2,0,0,2,0,5,3,0,3,3,0,3,0,1,1,
                0,3,0,1,3,0,4,4,2,5,1,2,4,0,2,3,2,2,0,0,
                3,2,5,4,3,0,4,1,4,3,0,3,3,5,2,2,5,2,0,5,
                1,5,4,4,5,2,1,2,1,2,1,5,4,1,4,3,3,4,2,4,
                1,5,4,2,0,1,0,2,5,1,0,1,1,3,2,3,5,1,0,2,
                5,5,5,2,1,2,1,0,5,3,1,2,2,1,2,3,1,5,0,0,
                1,3,3,2,2,1,2,4,4,0,2,4,1,3,0,1,5,4,2,3,
                2,0,5,4,0,2,5,1,0,0,4,3,2,1,4,3,2,4,0,0,
                5,0,1,5,0,0,2,1,0,3,0,1,3,5,4,2,3,2,0,1,
                2,4,3,2,1,0,2,0,0,0,5,2,5,4,1,2,4,2,1,0,
                1,4,3,2,5,0,4,3,5,4,4,1,3,4,3,0,5,4,1,3,
                1,3,1,0,2,1,3,1,1,4,0,0,1,0,4,1,5,5,2,4,
                0,1,2,4,1,2,5,3,2,4,4,0,1,4,0,3,4,1,5,5,
                1,4,2,3,0,2,4,3,5,5,2,1,4,5,2,4,1,0,5,2,
                2,1,3,1,4,3,3,1,1,2,1,2,1,0,1,0,0,0,2,1,
                1,3,5,5,3,1,5,5,0,1,5,2,1,3,5,0,3,4,0,4,
        };
        int[] Tab2 ={1,1,0,0,4,1,2,1,2,3,0,1,2,4,3,3,5,4,0,0,
                1,2,2,1,0,0,0,2,2,3,5,1,5,3,5,3,2,1,4,1,
                0,5,1,1,2,2,3,4,1,3,2,2,4,0,4,4,3,3,4,5,
                2,2,0,5,3,1,3,5,4,2,3,2,4,0,3,1,2,3,3,4,
                2,1,1,1,1,3,4,1,3,3,5,5,2,0,4,0,1,3,2,0,
                3,1,0,5,3,2,4,0,4,1,4,5,5,3,5,3,4,1,5,3,
                2,4,5,1,4,2,4,0,3,2,2,4,4,3,4,4,1,5,4,4,
                1,3,4,0,3,5,3,4,4,1,3,1,2,1,4,3,2,0,1,5,
                1,4,3,5,4,3,1,2,3,2,5,0,3,2,1,1,4,3,2,0,
                4,3,2,2,1,3,4,1,4,2,4,4,4,5,1,1,2,5,4,1,
                5,0,2,1,1,0,5,5,5,0,2,5,3,4,5,4,1,2,2,5,
                5,0,1,4,0,3,3,0,0,1,2,2,2,1,1,3,2,3,1,4,
                0,1,1,3,4,5,0,1,3,4,5,5,5,2,0,2,3,4,1,2,
                3,1,2,1,3,2,2,2,1,0,3,1,1,4,3,2,4,2,5,5,
                1,3,4,5,5,2,2,5,0,0,1,1,0,4,0,1,4,5,4,4,
                3,0,3,0,1,1,2,5,4,0,0,4,1,0,2,5,4,0,2,0,
                0,4,2,3,4,5,2,0,1,0,5,3,2,1,2,4,0,1,3,0,
                2,0,2,5,0,4,2,5,3,4,5,2,5,4,1,2,0,5,0,1,
                3,0,4,2,5,4,3,1,1,0,4,2,5,2,4,5,0,1,5,5,
                5,4,0,5,4,2,3,5,3,2,1,2,0,3,1,0,5,2,5,4,
        };
        int[] Tab3 ={5,2,2,3,2,1,5,3,5,2,5,0,4,4,4,5,1,2,3,1,
                1,1,2,1,4,1,4,0,5,4,0,0,1,5,1,5,2,4,5,2,
                1,3,4,3,1,4,4,2,4,0,1,0,2,1,3,1,2,1,1,3,
                2,4,5,0,0,5,0,4,0,3,4,5,1,4,1,2,1,2,1,5,
                3,3,2,0,5,4,0,5,2,4,1,3,2,3,0,1,4,1,5,3,
                5,4,3,3,5,5,5,1,2,1,0,0,0,0,4,2,3,2,2,1,
                0,2,1,1,5,3,5,0,4,3,1,3,5,4,0,5,5,1,5,1,
                4,3,5,3,4,4,5,0,4,3,5,1,1,1,3,3,1,2,0,4,
                1,4,3,4,5,5,5,1,5,3,2,3,0,5,0,2,1,3,1,2,
                3,1,3,4,2,5,0,1,2,1,0,5,5,4,3,1,2,3,0,2,
                4,3,4,0,0,2,4,5,0,3,4,1,0,2,3,4,2,4,4,4,
                5,5,5,1,3,1,0,2,2,3,2,2,3,4,2,0,1,4,5,4,
                2,3,0,5,1,0,1,4,0,3,2,4,4,4,1,2,0,0,3,4,
                1,5,5,2,1,4,1,3,4,1,1,3,1,0,5,0,0,5,3,2,
                2,2,1,5,1,4,2,3,5,2,2,2,4,0,5,4,5,5,3,4,
                0,1,1,3,2,4,3,1,2,2,3,5,3,2,0,0,0,2,1,3,
                5,0,0,1,2,4,3,1,2,3,3,0,0,3,1,0,0,4,0,3,
                5,2,2,1,2,2,4,1,3,0,4,2,4,2,4,3,3,3,5,5,
                5,0,4,2,0,4,5,3,4,2,4,2,1,0,2,0,4,0,3,1,
                1,0,1,3,1,4,0,3,0,4,1,4,0,4,4,3,3,0,0,4,
        };

        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=35;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=22;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=19;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=15;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }

    private void level1L(){
        int[] Tab0 = {2,2,5,0,4,2,1,4,0,3,4,5,2,4,0,1,2,1,5,1,3,1,0,4,2,1,3,5,5,2,5,3,4,3,0,3,2,4,2,0,
                2,1,5,2,4,2,2,1,3,3,5,1,5,0,3,5,2,1,1,5,1,5,0,0,4,5,0,5,1,0,4,4,0,4,5,2,5,3,0,5,
                5,4,0,4,3,1,4,1,3,4,1,5,3,3,2,1,0,4,0,4,2,1,3,1,0,4,0,4,2,1,2,3,0,3,5,2,0,4,4,1,
                2,1,0,1,4,1,0,2,2,3,1,3,2,5,3,3,5,2,4,0,0,3,0,1,0,5,5,1,1,4,1,5,1,0,1,3,1,5,4,3,
                4,5,3,1,2,3,1,0,0,4,5,5,0,3,4,4,1,4,2,5,3,5,3,1,3,2,0,0,5,4,0,4,4,5,2,3,0,1,0,1,
                1,2,3,2,3,4,4,4,5,3,5,0,0,5,0,0,2,1,5,3,4,1,1,0,4,2,1,0,2,3,1,0,1,5,5,5,4,1,3,1,
                3,1,3,4,5,2,4,4,3,1,5,3,2,2,3,4,3,1,1,0,1,5,3,1,4,5,0,1,3,4,4,3,4,3,2,5,0,2,5,1,
                2,3,1,2,0,3,2,2,3,4,2,3,0,0,2,0,2,5,0,1,3,3,4,1,0,2,0,1,0,4,3,2,0,0,2,5,3,3,3,4,
                5,4,3,1,0,0,4,2,4,5,1,1,4,3,4,3,2,2,2,3,3,2,3,3,1,0,2,3,4,5,1,4,1,4,0,4,0,2,3,5,
                4,2,1,0,5,0,1,3,4,4,1,4,2,0,5,0,5,4,1,4,2,3,4,0,2,1,1,1,0,3,5,3,5,3,2,1,2,0,3,0,
                2,0,5,4,4,0,3,0,4,3,4,1,1,2,2,1,4,2,2,5,4,4,5,5,5,2,2,0,0,1,2,4,3,2,2,2,4,4,2,3,
                2,2,4,1,5,0,2,1,0,1,5,0,5,2,5,1,0,0,0,4,4,1,1,3,3,2,5,0,4,4,1,3,5,4,1,3,1,2,2,5,
                3,1,0,5,0,1,0,5,0,1,2,2,3,0,1,2,5,2,5,1,0,1,4,3,3,0,3,5,4,2,2,1,1,3,1,2,0,4,2,0,
                3,5,3,5,4,2,4,3,5,3,1,4,5,3,3,1,2,4,0,4,1,1,5,1,2,0,0,2,1,5,5,4,0,0,4,3,4,4,0,2,
                3,3,3,0,0,4,0,5,2,1,4,1,2,0,0,2,4,1,3,4,4,0,4,5,5,2,1,3,0,0,2,4,3,5,5,3,2,4,2,3,
                5,5,1,0,0,1,3,4,3,1,5,1,5,4,3,2,5,0,5,0,3,4,2,1,5,0,2,0,2,5,0,3,2,2,0,2,2,2,4,3,
                3,0,4,0,0,5,5,5,0,2,2,0,2,1,2,5,3,1,3,0,0,0,0,4,3,4,2,5,1,0,2,1,0,1,5,1,2,0,2,0,
                0,1,0,4,4,5,3,3,1,3,1,3,4,3,4,5,5,3,5,2,3,3,1,0,3,1,3,2,5,4,0,5,2,0,3,1,2,1,4,5,
                2,2,0,4,1,0,3,0,4,3,5,2,0,3,4,3,5,4,0,4,1,1,2,3,2,0,1,3,2,1,5,5,4,3,0,4,0,5,3,3,
                3,1,2,0,1,0,4,5,0,5,2,2,3,1,5,0,1,1,4,1,5,2,2,2,2,5,2,4,2,4,2,4,2,4,4,1,3,4,2,0,
                1,0,0,1,0,5,5,4,1,0,3,2,1,3,0,0,0,3,0,0,5,2,0,1,4,2,3,1,5,3,3,2,3,4,3,4,5,1,0,2,
                5,3,5,4,1,2,1,5,0,0,1,4,0,5,3,1,0,1,2,0,2,3,0,1,0,3,5,1,1,5,5,2,3,2,1,2,1,2,4,3,
                1,4,0,5,0,5,3,1,4,0,3,1,0,1,4,3,5,3,1,5,4,3,4,1,1,1,5,1,2,0,3,4,3,0,3,3,2,0,2,1,
                0,5,1,1,5,5,4,3,2,1,0,1,4,1,0,1,1,3,5,1,3,0,2,1,5,3,5,2,4,2,2,1,2,5,1,3,2,3,1,2,
                3,3,2,3,0,5,2,4,5,0,2,1,5,0,4,5,4,4,0,4,4,2,5,1,2,3,3,2,4,3,1,1,0,2,3,5,0,2,3,4,
                5,1,2,4,1,5,0,0,4,4,3,4,5,4,1,1,2,1,4,0,4,3,5,2,3,4,2,0,0,0,4,1,3,2,0,4,2,3,2,0,
                3,5,2,1,5,4,4,2,0,2,5,4,4,4,3,1,0,2,2,4,3,5,2,0,3,3,1,2,3,0,0,3,5,0,4,0,4,2,1,4,
                2,4,2,1,5,4,5,0,3,3,3,1,5,5,2,0,5,3,1,3,5,4,5,3,4,2,2,5,0,4,2,5,2,1,5,1,1,1,4,0,
                0,3,4,1,5,0,5,3,4,4,1,0,3,1,1,5,0,4,4,1,5,4,5,0,1,5,1,1,1,1,1,4,1,1,2,2,4,1,0,3,
                3,3,1,5,4,5,5,2,3,3,1,5,0,1,5,3,0,0,2,4,3,5,0,1,4,4,3,2,1,2,5,4,2,2,0,4,1,4,1,5,
                4,5,5,1,3,3,3,4,1,5,3,4,4,3,1,1,3,1,3,3,2,3,5,2,2,3,0,5,1,5,3,5,0,3,4,2,2,0,5,0,
                1,4,3,3,1,0,1,4,0,3,5,5,1,4,4,2,1,2,5,3,1,4,0,3,3,4,1,3,0,4,5,5,0,0,0,2,1,1,0,4,
                1,4,0,2,1,3,0,2,3,0,5,2,1,3,4,4,0,5,1,2,3,3,2,3,0,2,1,3,5,3,1,5,4,1,0,0,4,4,2,1,
                5,3,3,0,2,4,2,0,2,3,5,0,2,0,0,5,1,1,4,4,0,1,2,4,2,4,2,5,0,0,3,5,5,2,3,4,0,3,1,4,
                5,1,1,0,5,3,3,1,2,2,0,0,1,3,3,4,1,5,4,2,1,1,5,4,4,2,3,4,4,5,1,1,0,2,5,1,1,0,4,1,
                5,5,2,2,5,2,3,0,4,4,2,1,5,1,4,4,0,2,3,0,0,4,2,3,3,2,4,1,0,5,4,2,1,1,4,2,5,2,2,4,
                0,1,4,2,3,4,5,5,3,2,0,2,3,0,4,4,2,0,3,0,5,0,5,4,0,3,2,3,1,3,3,0,1,5,0,4,5,0,2,4,
                1,1,3,3,4,5,2,0,4,3,3,2,5,1,1,1,0,0,1,5,4,3,0,2,4,0,2,0,5,4,5,2,4,1,5,4,2,3,3,2,
                1,5,0,4,5,1,3,2,3,3,4,3,2,5,5,1,0,3,2,2,4,0,1,1,2,2,0,0,0,3,3,4,4,5,2,2,2,0,3,5,
                2,2,1,0,2,5,0,2,5,1,2,1,4,4,1,1,3,2,0,3,5,1,2,4,3,1,2,4,1,0,2,0,3,0,3,2,4,1,5,2};
        int[] Tab1 ={1,0,3,0,5,3,0,5,4,5,1,0,1,3,1,2,2,2,5,5,2,5,4,2,3,4,1,3,2,2,5,5,4,4,2,4,4,4,3,4,
                5,3,1,0,2,5,2,1,3,1,3,3,4,1,1,5,2,3,4,4,4,5,4,1,5,5,1,2,5,1,5,3,0,1,3,5,5,0,0,5,
                4,2,2,3,2,5,1,5,3,5,5,0,4,4,2,1,4,1,2,0,1,4,0,0,4,4,4,4,5,2,3,0,5,2,2,0,3,3,2,3,
                3,0,4,0,5,3,4,5,0,0,5,1,4,1,0,5,3,3,4,2,4,5,1,4,5,4,1,4,0,1,2,4,2,3,2,2,2,2,4,3,
                1,1,5,4,3,4,0,3,2,1,5,5,1,0,2,4,2,1,5,0,4,3,4,5,3,5,0,2,0,0,0,4,1,0,1,3,0,3,2,3,
                2,3,4,3,3,5,1,2,4,0,4,1,0,1,3,4,2,5,5,5,0,3,1,0,4,2,5,5,2,1,0,2,1,5,0,0,3,0,0,2,
                5,3,4,3,3,2,1,5,4,4,3,0,1,3,1,1,4,2,2,1,3,4,1,4,4,2,1,4,5,5,5,4,5,3,0,5,5,2,2,3,
                1,1,2,1,2,2,4,0,1,4,2,5,3,0,0,5,5,4,5,0,4,0,5,5,0,5,1,4,0,5,4,5,2,3,1,5,0,0,0,1,
                3,5,4,4,5,3,1,3,2,3,3,5,4,4,3,0,2,5,5,2,5,2,2,5,0,4,1,3,1,0,5,3,4,4,3,3,5,2,2,2,
                1,1,0,5,2,3,5,2,1,2,2,0,3,1,3,4,5,5,2,2,2,2,1,4,0,4,5,5,3,3,4,3,2,1,1,3,5,2,3,1,
                1,2,2,1,5,1,5,5,1,1,4,0,1,4,0,4,3,4,1,4,0,5,5,3,5,2,2,1,4,4,1,1,4,4,1,1,5,5,3,5,
                4,2,0,2,5,4,4,3,3,3,3,1,3,3,0,0,0,1,1,4,4,2,2,4,2,4,5,5,4,5,5,0,0,3,5,3,0,2,3,5,
                0,2,3,1,2,5,4,4,5,3,3,1,4,3,5,1,3,5,3,2,1,3,1,4,5,2,2,3,3,3,1,5,4,3,2,0,3,5,3,5,
                4,1,5,1,5,1,2,1,4,4,0,1,1,2,0,5,3,3,1,3,0,1,4,3,2,1,3,1,1,2,5,5,5,4,3,5,1,1,1,3,
                1,1,4,5,0,0,0,2,1,1,4,4,5,0,3,0,0,1,2,5,0,4,2,5,4,2,3,5,5,5,1,3,1,1,5,0,0,4,0,3,
                4,5,4,2,3,0,4,2,1,3,4,2,2,1,3,5,3,0,1,1,3,3,2,5,0,1,1,2,4,1,2,2,0,1,4,4,1,1,3,5,
                1,2,4,1,0,5,4,3,1,5,5,0,4,4,4,3,1,4,4,4,3,4,2,4,4,5,4,3,2,4,3,4,3,4,4,5,2,4,5,4,
                0,0,1,0,2,0,5,3,2,3,0,3,0,2,3,1,1,3,2,5,5,2,1,5,3,2,3,2,3,4,0,4,1,4,0,5,2,2,1,4,
                5,2,0,2,2,5,2,5,5,2,4,5,3,3,5,2,1,2,3,3,5,0,5,0,4,3,1,5,0,5,2,3,4,4,5,4,1,4,3,4,
                3,5,4,2,1,2,3,1,1,0,0,3,0,2,2,2,2,4,5,3,3,2,0,1,2,0,3,1,1,1,4,3,5,2,0,1,3,4,5,2,
                1,1,4,0,3,4,0,4,5,0,2,0,5,2,3,0,4,5,0,5,5,0,2,3,3,1,5,2,3,5,0,0,5,1,3,5,4,2,3,4,
                4,2,0,1,5,1,1,2,0,4,3,3,2,3,1,5,1,4,2,4,0,4,1,2,5,4,5,3,3,2,1,0,2,1,3,2,2,0,4,5,
                0,0,1,1,1,0,0,1,1,2,5,0,3,0,0,4,1,3,1,0,5,5,0,3,1,5,0,0,1,3,2,0,5,2,1,1,4,0,3,1,
                3,2,2,0,5,5,5,0,1,3,2,5,5,1,1,4,1,3,2,4,1,2,4,4,0,4,5,5,4,5,0,5,1,2,1,3,4,5,1,4,
                5,0,0,5,2,2,1,3,2,2,2,0,3,3,0,4,5,5,3,0,5,1,4,4,1,4,0,2,5,5,3,0,1,0,1,1,3,2,0,0,
                2,5,4,3,4,0,0,0,1,0,2,5,2,4,3,3,3,1,1,1,5,0,5,5,4,0,1,1,4,1,3,3,3,5,0,0,3,4,1,2,
                5,3,1,3,3,5,4,1,1,3,3,5,3,2,1,4,4,4,4,0,3,4,2,0,3,2,2,3,3,5,4,2,3,3,4,0,5,2,2,0,
                5,2,1,5,1,4,1,4,0,4,0,5,1,1,1,0,3,3,4,5,2,1,5,5,2,0,1,1,5,4,5,2,3,4,5,5,1,0,0,1,
                2,5,2,4,0,4,5,3,4,0,2,1,3,2,4,5,0,4,5,5,0,4,3,5,1,0,1,2,0,1,3,5,1,3,3,0,0,3,3,2,
                2,3,5,3,0,2,0,2,2,2,3,4,3,3,0,1,4,4,2,1,5,1,0,4,1,3,5,2,4,0,3,3,0,0,0,2,4,2,5,3,
                4,5,1,3,1,2,3,1,4,3,1,0,1,1,1,1,4,5,0,2,2,4,1,2,4,1,3,5,3,4,5,4,3,3,4,5,5,3,1,0,
                0,4,3,2,3,0,3,5,0,2,3,1,5,4,2,1,1,3,2,0,4,0,5,3,3,1,3,2,2,5,4,4,0,4,4,0,1,0,2,5,
                5,5,5,2,4,0,0,0,5,2,2,0,4,2,4,0,2,0,0,3,3,4,0,1,1,4,4,0,3,5,2,5,5,0,3,1,0,3,5,1,
                1,1,0,0,2,4,4,4,0,5,2,5,4,2,2,3,1,0,0,5,4,4,4,3,4,0,1,3,1,2,4,2,3,1,1,3,0,5,5,4,
                5,5,3,3,4,3,4,2,0,5,3,2,1,1,1,5,0,4,3,5,1,1,0,2,3,4,5,1,2,5,2,5,5,5,5,3,1,4,0,1,
                2,2,5,2,1,5,1,5,1,1,4,4,5,5,3,2,3,1,0,3,1,3,3,1,0,3,0,1,2,1,1,4,4,0,4,2,4,3,2,2,
                5,0,5,2,2,3,5,0,3,4,3,0,0,2,3,2,2,2,0,1,1,4,4,3,1,2,3,0,3,2,3,1,0,2,4,0,5,1,3,0,
                3,3,3,2,4,5,4,2,4,1,3,0,5,3,1,5,5,2,5,4,1,5,1,4,0,4,1,1,5,5,5,3,5,5,2,4,2,0,0,0,
                4,1,4,2,2,2,2,0,0,5,1,1,1,4,0,5,3,2,5,3,4,5,3,0,1,5,2,5,4,0,4,3,3,1,1,1,1,4,2,2,
                1,4,5,0,1,4,2,4,2,1,1,4,4,5,2,3,5,1,0,3,0,5,4,0,5,0,1,0,0,2,1,2,5,4,1,2,0,5,2,3,
        };
        int[] Tab2 ={3,4,3,2,2,1,5,4,2,0,2,2,0,5,1,2,2,2,2,4,5,0,2,1,2,2,1,5,4,0,3,3,4,1,0,5,5,3,0,4,
                0,2,2,2,3,4,1,1,2,5,3,0,4,5,5,3,3,1,1,5,1,0,1,3,2,4,5,5,1,4,5,0,0,5,0,3,1,4,2,3,
                1,2,5,5,1,2,4,2,0,1,2,1,1,2,5,3,4,5,5,5,1,5,4,3,0,0,0,1,0,2,0,0,1,3,2,5,5,4,0,2,
                5,3,1,3,5,2,2,1,2,2,1,1,4,1,5,3,0,1,3,0,0,4,0,0,0,2,4,4,4,2,0,5,2,2,3,5,1,1,0,4,
                1,1,3,0,0,1,1,1,3,0,5,5,1,4,3,2,3,3,0,5,2,1,0,0,4,4,4,5,2,5,2,3,5,2,0,4,4,1,2,5,
                1,1,0,5,3,5,1,1,4,2,5,5,0,4,3,0,4,3,3,4,1,4,4,2,4,5,3,3,4,0,2,3,2,0,2,0,0,3,0,1,
                5,4,5,5,1,4,5,1,0,1,1,5,4,0,3,5,3,3,3,5,5,0,3,2,1,1,0,3,4,1,4,2,3,2,0,2,5,0,2,1,
                0,0,1,2,2,5,1,4,4,1,5,2,4,2,1,0,1,4,0,4,4,2,1,5,2,0,5,3,3,1,2,0,0,2,2,1,2,4,3,0,
                3,5,2,0,5,0,4,4,5,1,1,0,3,3,0,4,2,1,0,5,4,4,4,5,0,5,0,2,0,1,4,1,2,4,5,0,2,3,1,3,
                1,4,2,0,4,4,3,1,4,3,1,2,5,4,4,3,1,4,0,3,1,0,0,2,3,0,0,3,1,0,0,3,4,2,2,2,2,1,2,3,
                4,1,1,3,3,4,4,4,5,4,3,4,4,0,5,0,5,4,0,2,0,2,1,4,5,3,1,3,3,5,3,5,2,1,4,1,0,0,2,2,
                3,5,0,2,3,3,0,0,3,4,2,5,1,0,4,0,2,4,2,3,1,1,3,5,0,2,5,4,4,3,2,0,5,4,4,3,1,2,0,3,
                5,5,1,4,2,5,3,2,3,5,0,2,4,0,0,1,3,4,2,0,4,1,5,2,5,1,4,2,5,1,5,0,1,3,4,4,5,4,5,5,
                4,1,4,1,4,5,2,2,1,5,2,1,3,0,1,0,0,5,5,2,3,5,4,1,5,0,1,2,4,2,0,3,5,2,1,5,4,5,5,1,
                0,3,2,5,0,5,4,3,3,2,3,3,2,0,3,4,1,2,3,4,1,4,0,0,3,3,0,5,1,3,5,4,3,0,4,4,2,2,0,2,
                1,5,3,0,1,1,5,2,0,5,5,4,3,0,1,0,0,1,0,5,5,3,3,1,0,4,1,1,3,1,1,5,3,4,0,2,5,1,0,0,
                4,1,2,3,5,1,1,0,1,0,3,3,1,3,2,2,1,1,2,2,2,3,1,0,0,1,2,1,1,1,1,0,3,4,3,4,1,5,0,3,
                4,5,4,2,5,2,0,0,1,1,0,4,0,3,0,4,5,0,2,1,1,4,1,2,0,2,4,1,2,3,5,3,3,1,2,5,0,0,2,4,
                2,0,0,2,3,4,3,0,5,1,3,1,1,3,2,2,5,1,0,5,4,3,4,3,1,2,3,3,1,4,5,2,4,0,1,0,3,5,2,2,
                1,2,2,5,2,5,0,5,3,1,2,1,0,0,3,3,0,4,4,4,1,0,1,0,4,5,4,3,5,5,5,0,1,5,4,0,5,1,2,2,
                4,5,5,5,5,4,0,5,4,5,3,0,1,3,1,0,2,1,3,5,3,1,3,3,1,2,0,1,1,4,4,5,4,1,4,5,0,2,5,4,
                5,1,2,4,1,1,3,1,2,3,5,2,1,1,3,1,5,5,5,4,0,1,0,2,2,2,3,0,3,4,5,4,5,1,2,2,3,3,0,2,
                2,1,3,2,1,5,1,0,1,0,3,4,5,5,0,2,3,0,3,5,5,3,4,4,3,2,2,3,0,0,5,5,5,4,5,5,3,2,2,5,
                3,1,1,1,1,4,0,1,0,0,1,4,1,4,4,0,2,2,0,4,1,3,5,0,2,3,2,1,3,0,2,0,3,5,2,0,4,3,4,3,
                0,4,4,0,3,4,3,4,0,4,0,1,1,1,0,0,4,0,4,3,1,3,2,1,0,2,1,4,5,0,0,2,0,5,1,5,2,2,5,3,
                3,4,2,5,1,1,5,4,5,4,0,0,1,0,2,2,4,3,3,5,5,4,3,4,3,0,4,4,5,5,1,4,5,5,1,0,3,0,0,0,
                5,2,0,1,3,1,5,3,3,3,2,3,1,5,5,5,3,3,2,4,4,4,3,3,4,3,4,1,2,5,5,4,4,0,2,2,1,5,4,4,
                2,0,1,3,1,2,2,1,0,2,2,5,0,5,1,2,3,3,3,1,3,1,0,0,5,2,3,5,3,1,3,0,1,1,5,0,2,4,5,5,
                5,3,5,2,4,4,1,0,2,3,4,2,2,4,1,5,3,4,5,5,1,4,2,1,4,3,3,2,5,4,4,2,0,2,0,3,4,4,5,3,
                5,3,5,2,1,1,1,4,3,0,4,2,4,5,4,5,2,4,4,2,2,3,4,5,2,5,0,0,5,3,0,2,5,1,5,2,5,4,0,3,
                0,0,5,5,1,3,1,1,3,3,0,1,1,0,5,3,3,2,0,2,4,3,0,3,2,3,1,3,2,4,2,4,5,0,3,5,2,1,4,1,
                1,1,2,5,5,4,2,3,1,1,2,0,0,3,3,0,5,3,1,1,4,1,1,0,1,3,2,2,4,2,1,1,1,3,5,0,2,0,4,3,
                5,4,3,4,1,3,5,0,0,5,3,5,1,4,3,0,3,4,0,1,3,5,2,3,3,2,2,1,0,5,1,5,3,0,4,5,0,2,2,0,
                5,3,4,3,1,5,3,0,0,1,2,4,2,5,3,1,1,0,0,1,5,4,4,4,3,4,1,4,4,5,0,4,1,1,2,0,2,3,5,4,
                4,5,4,4,0,1,3,3,0,1,2,0,1,2,4,5,1,2,5,5,3,3,3,2,1,3,4,5,0,2,3,2,5,3,4,3,2,2,5,5,
                0,5,0,3,0,0,1,4,3,3,4,5,2,3,2,5,4,5,5,4,1,1,3,3,2,5,1,4,5,1,3,1,5,5,3,0,2,1,1,1,
                2,1,0,2,2,4,3,3,3,1,1,2,1,3,3,2,3,4,5,2,5,0,1,5,5,4,4,2,1,1,1,5,1,2,2,2,1,1,4,4,
                5,2,1,3,0,5,5,5,5,4,1,5,4,4,5,1,5,4,5,2,4,3,1,4,2,2,5,1,4,5,4,3,1,4,0,5,2,4,0,0,
                0,3,1,4,3,0,5,5,1,4,2,0,3,1,2,2,3,0,1,5,0,1,1,3,5,0,5,3,0,5,4,2,2,3,4,5,5,3,2,4,
                5,4,4,1,4,1,2,0,2,2,5,2,5,0,1,4,5,1,3,0,2,5,1,3,0,0,5,2,1,1,0,0,4,5,5,1,2,2,3,3,
        };
        int[] Tab3 ={3,4,5,5,0,3,2,3,1,5,5,2,1,2,5,5,2,4,4,4,2,1,1,3,0,4,5,0,2,1,0,1,5,2,4,3,2,5,5,0,
                4,5,1,1,3,0,2,4,5,2,5,2,2,5,4,1,2,1,1,2,3,5,1,1,5,2,2,2,2,4,1,0,4,0,3,5,1,1,0,3,
                4,5,1,3,3,5,4,3,4,3,3,0,5,2,5,0,3,1,0,2,4,3,1,0,4,4,0,0,3,4,1,5,2,3,5,1,3,2,4,4,
                5,4,1,4,2,4,3,0,1,0,0,1,4,1,2,3,0,2,3,4,5,2,1,0,0,0,0,3,5,4,3,1,0,1,4,4,0,0,4,5,
                2,5,4,4,4,5,0,4,0,0,2,4,3,4,3,3,2,5,5,5,0,2,1,5,2,0,0,1,5,1,0,2,5,3,1,5,4,2,5,1,
                5,5,5,4,0,4,1,3,3,0,3,3,2,1,2,1,5,1,1,5,1,2,3,1,3,3,1,2,4,2,4,2,0,0,4,4,2,5,4,2,
                1,0,3,0,5,5,3,1,4,2,4,4,2,0,4,0,1,3,5,2,0,4,0,2,2,2,4,0,3,2,2,3,3,0,3,0,5,5,5,1,
                2,1,1,1,2,5,2,5,4,1,4,1,2,3,1,0,2,5,2,2,1,2,4,5,3,3,5,5,0,3,1,0,2,1,5,2,2,0,0,1,
                3,5,3,3,2,5,5,1,4,0,4,5,2,2,3,3,2,2,1,5,2,3,1,3,4,4,3,1,2,5,2,5,0,3,5,3,2,5,1,5,
                4,3,0,0,3,4,1,0,5,3,3,2,3,0,1,3,1,2,4,2,3,4,2,0,5,2,5,1,0,1,4,5,1,2,3,5,0,3,3,0,
                3,5,5,5,4,3,2,5,5,1,3,4,5,1,1,0,1,2,2,1,0,4,4,2,4,5,4,0,4,2,1,3,2,4,1,3,0,0,3,1,
                5,1,3,3,5,5,2,0,4,1,5,2,5,4,2,0,2,0,4,5,0,2,3,2,0,2,0,2,3,5,0,1,2,1,2,5,1,4,5,3,
                2,1,1,0,1,1,1,2,5,2,2,4,4,4,2,0,4,4,0,4,0,2,5,2,3,5,2,1,4,4,0,3,0,5,4,3,1,0,1,3,
                2,1,5,2,5,5,1,4,5,4,3,3,5,4,4,0,1,1,3,5,1,5,0,5,0,3,1,1,5,5,2,0,0,3,3,2,2,5,1,5,
                5,1,0,5,0,4,1,5,0,1,3,0,2,5,4,4,3,1,5,4,0,0,3,3,4,2,1,4,5,1,2,3,4,2,5,5,4,4,0,3,
                1,5,2,2,3,5,4,3,0,1,1,3,4,4,3,1,0,4,5,0,0,2,4,1,1,0,0,0,5,0,2,1,2,5,4,1,2,1,5,4,
                0,3,0,5,2,4,2,1,4,0,5,2,1,1,1,3,1,4,1,5,2,4,2,0,2,5,2,3,5,4,4,3,2,2,3,4,1,4,1,3,
                1,2,5,4,2,5,3,0,5,4,2,4,5,1,4,3,2,0,4,0,2,1,1,0,5,1,1,4,1,2,3,5,0,0,2,3,3,2,4,5,
                3,1,3,0,4,4,4,0,5,2,4,3,3,1,4,0,0,2,5,1,2,3,4,0,4,2,5,3,4,5,5,2,0,3,3,4,3,1,2,4,
                3,3,5,3,3,1,2,5,0,4,2,3,4,5,2,0,0,4,4,0,0,1,4,4,4,2,2,4,2,4,0,4,5,4,3,0,5,2,2,3,
                0,1,4,5,3,1,4,5,1,0,2,5,4,3,0,2,3,2,0,0,5,2,4,0,2,2,4,3,3,0,4,5,5,4,0,4,4,0,1,5,
                0,0,3,2,0,2,0,5,2,3,1,5,2,1,1,5,1,1,2,5,4,2,3,0,2,3,4,3,2,2,3,0,5,1,3,5,3,3,2,1,
                5,4,4,1,5,2,0,0,0,0,5,3,0,3,2,3,4,3,4,2,1,0,1,0,2,5,5,5,3,2,0,3,2,3,4,2,5,0,3,5,
                3,1,3,1,2,5,1,4,0,3,3,1,5,3,0,2,4,2,1,4,5,4,5,4,0,0,5,4,4,5,4,1,3,3,0,3,2,0,4,2,
                0,3,0,2,4,0,4,5,5,1,2,2,3,4,4,0,0,3,0,5,3,2,0,5,1,0,0,3,1,1,4,0,5,3,1,4,5,0,3,5,
                2,2,0,0,2,0,2,2,3,2,5,4,4,2,4,3,0,3,3,4,5,4,2,0,3,0,0,4,4,3,2,1,1,1,1,2,0,5,3,3,
                4,4,2,0,0,2,5,0,3,3,1,3,0,4,4,3,4,0,2,5,2,0,2,3,1,4,4,2,1,1,3,5,0,1,0,4,3,3,5,5,
                0,0,2,1,4,4,5,4,3,3,1,3,3,3,1,4,2,1,4,5,3,1,1,5,2,5,3,0,4,0,2,5,5,2,5,0,1,0,5,0,
                2,3,5,2,4,0,1,1,1,4,1,5,2,5,4,4,2,2,0,0,2,1,3,0,5,0,2,5,3,3,5,5,1,0,1,2,1,3,3,2,
                1,1,3,0,0,5,0,3,3,0,0,4,3,3,1,5,5,2,4,5,4,4,4,1,5,2,4,0,4,4,4,0,3,3,1,5,1,0,1,0,
                3,5,3,5,2,4,0,5,0,0,3,2,0,4,2,1,1,2,4,0,5,0,0,4,0,2,5,0,5,0,5,5,3,0,1,0,2,2,2,2,
                0,0,5,2,3,2,1,2,4,4,3,0,4,5,0,5,3,2,2,1,4,4,2,4,3,3,0,2,5,1,2,5,4,4,1,1,1,3,4,1,
                1,1,1,3,3,0,3,0,1,3,0,4,0,0,0,4,0,4,1,1,5,0,1,4,0,2,1,1,4,0,0,1,5,4,5,5,2,2,3,1,
                3,5,3,1,0,2,0,3,4,4,0,5,5,3,2,4,0,4,2,3,1,1,5,4,1,0,4,0,3,0,1,2,2,3,4,1,0,3,3,5,
                0,4,1,2,2,0,5,0,5,1,0,5,2,3,2,1,1,3,0,4,4,3,3,4,1,5,2,5,1,3,1,3,4,0,0,1,2,3,2,5,
                5,5,3,0,4,5,3,3,2,5,0,2,1,5,1,2,1,3,3,3,1,1,0,0,3,5,5,0,1,0,0,0,2,2,2,2,5,3,5,2,
                2,2,0,2,1,5,1,1,2,3,3,3,5,3,2,1,0,1,2,1,3,4,4,5,3,0,2,5,4,2,3,1,2,0,4,2,0,4,3,4,
                1,1,3,1,5,4,3,5,0,4,4,1,3,5,4,3,3,5,1,2,5,0,1,1,1,5,5,3,3,3,5,1,2,2,5,3,4,1,1,0,
                0,1,5,2,1,2,0,4,3,4,3,5,3,5,4,0,1,1,3,5,2,5,2,1,0,2,1,0,3,2,3,4,1,2,1,4,0,4,2,1,
                0,1,5,2,1,2,2,1,3,0,3,5,4,0,5,0,3,1,0,3,2,2,3,2,2,4,5,2,0,0,0,1,5,0,3,1,5,0,5,1,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=73;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=43;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=35;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=28;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level2L(){
        int[] Tab0 = {4,5,5,5,3,4,3,3,0,1,0,3,1,5,5,0,4,4,4,2,5,4,2,3,3,4,3,0,5,5,1,2,1,0,1,1,4,3,4,5,
                2,2,2,0,3,4,2,4,4,1,2,0,3,4,0,4,5,0,4,3,4,2,4,2,4,0,0,3,5,1,1,3,5,1,2,1,1,3,0,2,
                1,0,0,0,0,2,3,3,1,2,1,1,2,1,3,4,0,0,0,3,0,4,1,2,0,3,1,4,3,0,3,1,5,5,3,4,4,2,5,0,
                5,1,4,1,5,3,5,5,2,2,4,1,0,4,5,5,5,0,2,5,3,0,2,4,1,5,5,4,3,5,5,4,3,3,5,2,1,0,1,4,
                4,2,2,1,2,0,0,2,4,0,4,5,5,2,2,2,1,3,0,4,5,2,0,0,1,4,0,4,1,1,3,4,1,3,1,3,2,1,4,4,
                2,5,1,2,2,0,3,5,2,1,1,2,1,1,4,3,5,5,0,5,5,1,2,0,5,3,5,3,5,0,0,1,1,4,5,1,3,2,5,0,
                4,1,2,0,2,0,0,4,4,0,0,1,4,2,3,5,2,0,4,3,0,5,5,4,1,4,2,2,2,3,4,3,5,2,1,2,4,2,3,4,
                2,1,4,4,4,2,4,0,0,3,1,1,2,2,1,3,2,1,5,1,3,0,2,3,4,3,3,3,5,4,4,2,3,4,5,4,0,2,2,0,
                0,3,4,0,2,2,3,5,2,5,4,5,3,2,5,4,4,5,2,0,5,2,1,1,2,3,2,5,5,1,0,2,0,4,1,3,5,2,1,2,
                1,1,1,2,4,5,5,3,5,3,0,3,3,3,4,0,1,4,4,5,5,5,4,5,1,4,0,4,4,0,2,2,0,1,1,5,3,1,0,5,
                0,0,2,1,4,4,5,3,3,1,3,0,1,1,1,4,5,0,1,2,3,4,1,2,1,1,0,4,5,5,2,5,5,0,4,0,2,1,5,1,
                1,4,1,5,2,5,0,0,2,0,1,1,1,1,4,1,4,2,0,1,2,2,1,0,3,0,4,4,3,0,4,4,3,3,1,5,1,5,5,1,
                1,1,5,5,1,0,1,0,3,3,4,2,3,2,3,5,0,4,4,0,2,0,4,5,5,2,3,0,1,1,1,4,4,2,5,1,3,1,0,2,
                4,2,2,2,1,0,2,1,3,0,5,0,1,2,0,0,2,5,5,5,2,3,5,0,4,2,5,3,0,1,1,0,0,1,2,5,5,1,5,1,
                3,0,3,1,3,4,4,0,4,3,1,2,3,3,4,4,4,4,5,4,0,5,0,1,1,3,5,0,4,5,2,4,4,5,2,1,3,2,4,4,
                1,0,3,3,4,3,4,4,3,3,5,2,2,1,0,1,5,1,1,0,1,0,3,5,4,2,5,2,3,5,0,1,1,0,2,3,1,1,1,1,
                4,0,0,5,2,0,3,2,0,1,0,1,5,3,1,3,0,3,2,2,2,2,3,3,2,4,3,0,5,1,5,5,1,0,0,5,0,2,1,1,
                2,5,1,4,4,5,5,2,2,1,1,3,5,4,0,0,2,5,0,3,1,0,5,3,3,2,1,2,5,5,5,3,3,4,0,0,3,4,1,5,
                1,2,1,3,5,3,0,3,1,3,5,2,5,0,1,2,2,2,2,4,1,3,0,1,2,2,5,3,1,4,4,2,4,0,2,4,3,3,3,3,
                4,4,4,5,0,2,5,1,2,3,5,0,5,0,0,5,5,1,2,1,2,4,1,4,0,0,2,2,4,4,4,3,5,5,1,2,1,5,4,3,
                1,0,1,0,3,0,4,2,2,4,5,4,3,5,5,5,2,2,0,2,5,2,3,2,0,2,5,5,3,3,5,3,1,4,2,5,1,2,3,3,
                0,0,0,4,4,5,3,4,2,1,0,2,2,3,3,0,4,3,3,0,3,2,1,2,3,5,4,5,2,1,2,3,2,1,1,3,1,3,5,1,
                3,2,0,5,4,4,1,5,5,1,3,4,0,1,3,0,5,1,5,0,4,5,5,4,0,3,5,1,2,4,1,2,3,1,0,3,4,5,1,0,
                4,1,2,3,0,5,1,0,5,0,1,1,4,4,1,4,1,0,5,5,1,0,2,2,0,0,1,5,4,5,1,2,3,1,1,0,4,1,1,0,
                1,4,1,4,2,3,1,0,5,3,0,0,3,1,3,2,0,3,5,3,3,1,1,4,0,3,1,3,1,1,0,3,3,0,0,2,3,3,3,1,
                2,5,1,0,4,0,5,1,3,3,2,0,3,1,1,4,3,4,0,4,2,1,1,5,2,0,2,0,4,1,2,4,2,0,3,2,3,2,3,4,
                0,0,5,5,4,5,5,1,5,0,0,2,2,2,1,5,2,1,5,2,1,3,3,2,0,4,5,0,1,2,5,4,1,4,2,4,1,2,5,3,
                0,0,1,1,3,5,3,3,3,1,1,1,1,5,3,1,4,2,0,4,3,2,2,2,2,1,4,3,3,3,4,0,1,0,2,0,0,2,5,0,
                1,2,2,0,3,2,4,5,2,3,1,3,0,4,5,0,3,5,3,0,4,2,3,5,0,4,3,2,0,5,2,2,0,5,5,5,4,5,4,5,
                4,1,2,1,4,5,2,0,5,2,1,3,2,0,4,0,5,3,0,5,0,4,3,0,3,2,0,2,3,4,0,0,4,4,5,5,2,0,2,3,
                2,5,0,4,1,0,4,3,1,4,4,4,1,5,0,5,0,4,2,2,3,4,1,3,5,4,3,1,1,5,3,5,3,4,2,3,0,0,1,5,
                2,5,3,2,3,4,2,4,5,0,3,2,3,3,4,0,5,0,2,1,2,0,4,3,1,3,0,4,0,0,0,3,2,4,5,5,1,0,0,2,
                0,2,1,2,5,5,0,4,0,5,3,3,2,2,4,3,4,3,3,1,1,3,4,4,1,4,1,5,4,2,2,0,1,2,1,4,4,4,5,3,
                0,5,4,3,0,2,5,2,2,0,1,5,1,2,5,5,3,1,5,0,3,4,1,1,2,4,2,4,5,4,2,0,3,0,1,3,0,1,1,0,
                4,0,0,1,0,1,4,0,0,2,1,0,1,0,2,0,1,3,5,1,2,3,0,1,5,3,4,3,5,3,4,4,0,3,0,3,5,1,2,3,
                0,1,4,0,0,3,1,4,3,5,5,3,4,3,5,3,3,2,1,1,2,0,2,1,0,1,1,0,3,3,2,3,4,2,2,3,1,2,3,5,
                0,5,1,3,1,2,2,5,5,3,5,4,5,3,5,2,2,4,0,2,4,5,1,4,0,0,5,1,0,4,1,4,2,0,4,5,1,4,4,0,
                3,4,3,2,0,3,2,5,2,0,3,0,4,3,1,0,2,5,5,3,1,1,1,3,2,5,1,0,0,0,1,1,1,1,1,4,2,4,3,4,
                4,4,5,4,2,4,4,0,4,0,1,2,2,3,0,1,0,5,1,2,4,0,0,3,5,4,1,2,5,5,4,5,5,5,2,1,0,3,1,0,
                2,2,3,0,1,3,1,0,0,3,3,5,0,5,0,3,4,1,3,1,0,5,4,2,1,4,0,5,5,2,5,1,3,1,1,4,1,0,4,1};
        int[] Tab1 ={0,4,0,3,0,2,5,0,5,2,1,4,2,4,1,4,2,3,2,4,2,0,0,5,4,2,3,4,3,1,2,3,1,2,0,4,1,2,2,5,
                1,0,2,5,1,3,1,0,2,3,1,4,2,4,0,5,1,4,0,3,4,3,5,0,4,1,5,4,3,3,3,1,1,4,1,4,0,0,4,1,
                1,0,3,5,4,5,0,0,4,5,4,1,1,5,1,4,0,5,0,0,3,4,1,3,4,0,3,1,0,1,2,5,5,2,1,0,3,5,0,1,
                1,3,1,3,3,4,2,3,0,5,0,1,2,5,0,1,1,4,5,5,0,2,1,3,1,2,3,1,1,0,0,0,3,4,4,4,4,4,0,0,
                4,5,1,3,0,0,2,2,0,5,5,3,4,3,4,0,2,5,1,4,0,3,4,0,1,0,4,5,5,5,2,5,1,1,2,5,2,4,4,4,
                4,4,4,2,1,2,3,0,3,1,0,4,2,2,1,3,2,1,5,0,2,4,2,4,1,5,2,1,3,3,4,0,2,3,1,5,1,1,2,4,
                5,1,3,1,5,1,1,4,1,5,2,2,5,2,2,1,2,3,5,0,1,5,0,0,3,5,0,5,4,3,4,1,2,3,5,1,2,1,4,3,
                2,3,2,3,2,5,5,4,2,5,2,4,1,1,1,5,4,0,2,5,2,2,5,3,5,3,1,2,5,5,1,1,0,0,1,4,0,3,1,2,
                0,2,2,2,2,0,1,0,5,0,4,4,4,0,1,3,5,3,5,0,4,1,0,3,4,1,2,1,1,4,1,3,2,1,0,4,2,4,0,3,
                5,1,0,1,4,4,0,1,5,3,4,4,3,3,0,1,2,2,4,3,0,1,5,4,1,4,0,5,3,3,5,4,0,0,3,1,0,2,5,3,
                4,5,3,0,4,1,3,1,5,3,1,5,3,5,3,0,1,1,5,5,1,2,2,5,2,2,0,3,0,2,1,4,4,2,4,2,1,2,1,1,
                3,3,0,0,4,0,3,3,2,4,2,1,4,2,1,2,3,2,4,1,0,0,4,2,0,5,1,5,2,3,4,5,1,2,4,0,2,4,3,1,
                3,3,5,4,3,1,2,1,1,3,3,2,3,2,1,4,1,0,3,0,2,0,0,1,1,1,4,5,1,0,4,2,0,2,4,1,4,5,4,4,
                3,3,3,4,1,0,5,2,4,3,0,0,3,5,4,5,1,5,0,4,3,5,5,2,4,2,2,3,3,3,4,3,5,1,1,0,4,5,3,0,
                2,0,4,5,3,1,0,0,2,2,0,2,2,1,0,5,3,4,3,3,2,5,1,4,4,1,3,4,0,3,0,0,1,3,3,0,1,5,5,3,
                0,1,0,5,2,0,1,5,2,0,5,0,0,5,1,2,5,4,1,5,1,0,3,2,2,2,3,2,5,1,3,4,0,4,3,5,1,2,0,2,
                4,4,4,5,1,2,1,4,0,5,3,5,5,2,3,0,0,2,2,2,4,3,2,4,2,4,1,1,1,2,5,0,3,2,2,1,4,5,0,0,
                0,4,3,0,5,0,4,1,2,3,4,2,0,1,5,5,5,5,4,4,3,5,1,5,0,0,1,4,0,0,4,1,3,4,5,3,5,5,2,1,
                1,4,5,2,5,2,2,4,0,3,1,5,1,3,2,1,4,4,4,5,2,1,3,1,0,5,3,1,5,1,3,3,0,0,3,1,5,1,5,5,
                3,4,1,2,1,4,5,3,2,5,2,2,3,0,3,4,5,5,1,3,1,5,5,3,4,3,0,4,1,2,0,3,1,1,3,2,1,5,4,1,
                5,3,3,0,1,4,0,2,2,2,3,0,1,5,3,5,3,4,4,2,1,1,0,2,3,4,2,1,3,0,1,2,2,1,4,1,5,4,3,2,
                2,4,3,5,4,5,4,2,2,2,4,0,2,3,3,2,0,3,2,0,4,5,3,2,3,2,3,2,1,0,5,4,1,3,5,3,4,0,4,0,
                4,5,4,0,0,0,3,1,1,4,5,5,4,5,3,0,3,0,5,1,3,4,2,5,3,0,5,4,5,0,1,0,1,3,4,0,5,0,5,1,
                4,4,2,5,3,3,0,2,5,0,3,0,3,1,5,3,1,0,3,2,3,4,3,3,2,3,5,3,2,4,1,2,4,2,4,1,3,3,3,2,
                0,2,1,4,0,0,1,4,4,4,5,4,4,1,2,5,2,0,2,0,3,5,0,4,5,1,4,0,0,3,3,0,5,3,4,2,2,5,3,0,
                0,0,4,3,0,1,5,4,4,2,1,4,2,1,0,3,1,3,3,3,1,4,5,0,4,4,5,5,3,2,5,4,5,0,4,4,0,0,4,4,
                3,5,5,1,1,4,3,1,2,2,3,5,4,0,4,4,4,0,5,0,1,5,1,0,1,2,1,1,2,0,3,5,2,2,1,2,4,1,1,4,
                2,1,3,5,5,0,5,4,0,0,1,0,2,3,1,3,3,1,2,1,2,0,5,1,4,0,4,3,5,1,2,0,2,1,2,0,4,1,0,0,
                3,4,3,2,5,3,0,5,2,4,4,1,0,0,2,4,5,3,5,0,0,2,5,5,2,3,0,1,3,3,5,2,2,0,3,0,1,1,4,4,
                0,2,5,0,4,0,4,0,2,0,4,4,0,2,4,4,3,5,0,3,1,2,4,2,0,4,4,4,4,5,1,4,5,2,2,0,1,2,4,3,
                4,4,3,5,5,2,4,5,4,5,3,5,2,0,2,4,3,1,5,1,5,0,2,0,2,5,4,3,5,2,4,0,4,3,1,1,1,0,5,5,
                4,2,3,5,0,4,4,4,2,5,3,0,1,1,3,4,5,0,2,4,0,4,0,2,5,4,1,3,0,4,3,3,2,4,5,0,1,2,5,2,
                4,5,5,0,0,4,0,5,0,2,5,2,2,3,3,3,3,5,4,4,4,5,3,3,0,4,4,2,3,0,3,5,2,0,1,3,1,1,2,4,
                1,5,3,0,3,5,5,4,1,4,3,5,4,1,3,2,1,3,4,0,2,2,4,1,1,0,2,2,1,0,3,3,1,0,2,1,0,4,0,3,
                4,3,4,5,0,4,3,3,4,1,4,4,3,4,4,5,3,3,0,4,4,2,5,0,0,2,3,3,1,5,5,1,1,0,4,2,0,4,1,4,
                1,3,3,3,0,2,4,4,3,4,0,4,0,4,1,3,3,3,0,4,4,2,2,2,5,5,2,4,2,1,5,3,1,1,5,0,5,1,0,2,
                0,2,5,0,1,4,5,4,1,3,1,1,5,5,3,5,2,3,2,4,3,3,0,0,0,0,1,2,4,1,2,2,3,1,5,1,1,4,1,1,
                1,3,4,5,2,4,3,2,3,3,3,4,1,1,5,5,3,3,0,2,1,2,1,1,3,4,5,1,3,2,3,4,1,4,0,5,4,3,4,5,
                5,1,0,4,1,1,2,2,4,2,4,1,1,3,5,1,2,4,0,5,3,1,0,1,2,3,0,5,5,2,2,0,1,3,1,1,2,5,1,3,
                3,1,4,0,2,4,4,2,3,1,5,0,5,4,5,2,2,4,5,4,3,2,5,5,4,5,0,1,1,2,0,5,5,1,5,2,4,3,5,4,
        };
        int[] Tab2 ={5,4,5,4,0,5,1,5,4,4,5,1,0,5,4,0,3,3,0,3,4,3,1,1,4,2,1,5,3,2,5,3,4,4,3,2,5,4,5,0,
                0,2,5,2,3,3,4,0,4,5,0,5,5,1,0,4,2,2,0,1,2,3,1,3,3,0,5,1,1,0,0,5,3,3,5,1,1,0,1,1,
                2,2,1,5,4,1,4,0,5,1,1,0,0,2,2,5,0,2,5,0,5,2,1,5,3,1,1,2,1,2,1,3,5,2,4,0,3,5,4,4,
                4,4,4,5,4,0,5,3,3,2,0,2,5,1,2,1,5,3,2,3,4,2,3,4,1,4,5,0,3,0,5,0,0,3,4,5,3,4,4,3,
                2,0,3,2,5,1,3,1,4,1,4,3,0,1,5,4,3,2,0,5,5,3,0,2,3,4,1,4,1,4,3,1,4,3,0,0,1,3,5,3,
                4,3,5,1,3,3,5,2,5,4,3,5,3,0,2,4,1,4,0,1,4,1,5,4,2,4,3,3,4,0,1,0,5,0,5,3,5,0,4,4,
                2,2,4,4,4,2,0,2,2,3,3,1,5,0,1,4,0,3,1,0,1,4,4,2,0,2,3,1,3,2,3,2,1,3,5,3,2,2,0,1,
                5,3,2,5,5,3,2,4,0,2,5,2,5,0,1,2,4,5,4,5,4,1,5,4,1,2,4,0,1,3,5,1,5,4,1,0,0,5,2,3,
                5,1,3,4,5,5,5,3,5,4,4,0,5,0,3,2,3,2,0,4,5,1,0,0,2,1,4,5,3,5,0,4,4,2,5,0,3,2,5,4,
                1,5,1,1,5,0,3,0,4,3,2,0,0,1,2,0,4,0,3,2,3,5,5,1,1,4,1,5,5,0,4,2,0,4,2,4,1,0,5,1,
                0,2,0,4,2,2,1,0,2,1,2,2,2,0,4,2,0,5,4,0,1,1,1,3,3,4,2,5,0,2,2,5,1,4,1,1,1,0,2,2,
                1,1,2,0,4,1,3,3,5,1,0,2,2,3,4,4,3,5,3,3,1,3,1,4,1,5,5,1,5,0,3,3,5,0,3,4,4,2,2,4,
                5,1,0,2,1,1,0,5,4,3,4,2,2,2,3,2,0,5,3,4,4,5,5,0,0,0,2,0,5,0,0,3,3,5,4,5,5,2,1,0,
                4,0,0,1,0,4,4,3,4,4,5,3,3,1,3,2,4,4,5,1,3,4,0,4,5,0,3,3,4,3,2,3,2,3,5,5,4,3,1,4,
                5,4,0,5,2,1,2,0,4,2,2,5,0,2,5,0,1,4,1,3,4,5,3,0,4,0,4,4,4,5,2,3,4,2,0,2,2,4,1,2,
                3,2,0,1,5,5,0,3,4,1,4,2,1,2,3,0,0,1,3,3,2,2,0,2,3,3,5,4,0,4,4,1,1,5,3,4,3,4,5,5,
                4,0,0,4,1,0,4,3,3,1,5,5,1,1,3,1,0,1,1,1,0,0,5,1,4,5,0,0,5,5,2,4,5,5,3,3,0,2,1,3,
                2,2,4,1,0,4,2,4,0,3,0,0,2,5,3,0,2,0,2,2,5,3,2,4,3,2,1,2,1,3,0,0,5,0,5,4,2,5,2,1,
                3,2,0,0,1,5,2,4,4,2,1,2,5,3,0,2,4,3,0,5,0,4,2,4,4,2,0,5,5,5,4,5,5,1,1,5,0,1,2,0,
                1,4,1,2,0,4,1,4,2,0,0,5,4,4,0,0,2,0,3,1,3,5,5,2,4,4,3,1,1,4,4,2,5,4,4,0,1,0,3,0,
                1,4,5,3,0,3,1,4,1,4,0,4,3,3,0,2,2,5,2,4,3,5,0,4,0,2,2,0,3,2,5,4,5,4,5,1,4,4,0,0,
                2,5,3,1,4,0,4,0,5,2,0,1,3,2,3,4,1,5,5,3,0,3,5,2,0,0,2,2,1,4,2,0,4,3,4,5,0,5,4,1,
                4,4,2,5,3,1,3,4,1,3,1,3,4,5,3,2,4,2,5,2,4,3,4,0,4,0,1,1,3,5,1,5,3,3,4,4,3,2,4,1,
                0,3,0,1,4,3,4,3,3,0,0,5,3,0,4,1,1,1,0,1,2,1,4,5,2,1,3,4,4,4,5,4,2,5,0,4,5,4,2,5,
                0,2,2,2,2,4,3,3,4,4,3,4,0,1,2,3,2,1,1,0,3,1,4,1,0,3,0,1,5,0,1,0,1,1,5,4,0,5,5,2,
                1,3,3,0,3,2,1,5,3,5,1,0,0,5,5,1,0,5,1,5,4,0,2,5,0,5,4,1,5,2,1,2,0,3,4,5,0,4,0,2,
                1,1,5,2,0,1,3,1,4,1,1,4,5,5,2,3,4,0,3,4,2,0,3,5,5,4,3,1,0,0,1,2,1,2,0,3,4,4,1,5,
                5,4,3,3,3,5,2,0,0,3,3,2,0,1,0,5,0,2,2,1,1,1,1,0,2,2,5,5,2,3,2,0,4,2,4,1,2,3,1,1,
                2,0,5,0,1,4,4,5,1,1,2,5,5,3,0,1,3,5,0,3,4,5,5,4,1,2,2,3,1,4,1,4,2,1,1,5,0,4,2,5,
                0,3,3,0,3,5,1,1,3,4,3,3,3,0,0,4,4,4,2,4,1,1,4,3,4,5,3,4,5,3,0,1,0,5,0,0,2,4,5,0,
                2,5,2,2,2,1,0,5,4,0,0,2,2,4,5,5,5,2,3,5,1,0,1,5,5,0,0,1,0,3,5,3,0,2,5,5,1,4,3,0,
                5,3,4,0,2,4,2,0,2,0,1,1,5,4,0,3,5,2,1,2,0,0,2,2,5,1,0,4,2,4,0,5,0,3,0,1,4,4,5,2,
                5,2,1,3,3,3,2,2,4,1,5,5,5,0,1,3,5,1,5,4,2,2,2,3,5,4,2,4,3,0,5,5,0,0,1,4,5,4,0,2,
                5,3,0,5,3,4,3,0,5,2,3,3,2,2,5,4,4,4,4,4,1,5,0,3,2,2,0,1,2,0,1,3,5,0,2,0,2,2,5,3,
                5,2,3,1,3,2,0,2,2,2,0,5,3,4,3,0,2,1,1,4,1,2,2,1,4,1,1,3,5,1,0,0,3,0,3,2,1,2,5,3,
                1,0,4,2,0,3,0,4,0,3,2,4,2,2,4,2,5,1,1,5,1,4,5,0,2,4,0,2,2,0,2,5,0,2,4,4,4,3,5,0,
                4,2,2,4,4,1,0,4,4,2,2,2,0,5,5,5,3,3,1,4,1,1,2,3,0,1,0,2,1,1,1,0,2,3,1,1,0,4,2,2,
                3,0,0,5,1,1,4,5,4,3,2,4,5,0,4,1,1,3,0,3,0,1,2,2,5,0,4,2,0,3,1,2,1,5,0,3,3,5,3,4,
                1,4,4,3,1,4,1,3,1,0,5,4,4,4,0,2,2,3,5,0,4,3,1,0,5,3,5,1,2,5,4,4,4,4,1,0,5,5,2,3,
                2,1,5,4,3,5,4,5,5,2,3,1,1,5,5,5,1,4,3,0,4,4,0,4,2,3,1,2,1,4,0,1,5,0,3,3,5,4,3,5,
        };
        int[] Tab3 ={4,5,1,4,4,3,4,3,4,3,3,5,5,3,4,4,2,5,0,0,3,3,0,0,2,2,4,0,1,4,0,4,0,3,1,5,3,5,5,1,
                4,2,0,2,3,3,0,4,5,0,3,1,3,4,0,1,4,0,1,4,2,1,2,1,0,3,3,1,5,0,1,1,3,3,2,0,5,5,0,5,
                1,2,1,2,0,0,1,2,0,1,5,1,3,5,1,1,1,4,0,2,2,4,0,1,2,4,2,5,4,1,4,1,3,5,3,3,3,0,5,5,
                3,1,5,2,0,4,2,2,0,2,3,4,1,4,4,1,0,1,1,4,5,4,2,3,0,4,4,3,4,3,2,1,1,5,5,5,3,0,2,1,
                0,4,0,1,3,2,1,2,3,1,2,0,4,4,1,5,4,3,3,2,2,4,5,4,4,2,2,1,1,1,3,1,3,2,0,1,0,5,3,0,
                1,0,0,5,3,2,1,1,3,3,0,5,4,4,4,2,4,2,2,4,4,3,2,3,0,2,4,2,5,5,5,0,2,1,5,3,5,4,0,2,
                5,3,4,1,2,0,4,4,1,4,5,3,4,3,1,5,2,2,0,3,1,1,5,4,1,1,2,5,4,3,4,3,4,5,3,2,3,3,4,1,
                2,0,4,5,1,1,4,0,1,3,1,2,4,1,1,0,4,4,4,4,2,5,5,2,1,3,1,1,4,1,5,5,1,0,1,2,2,5,0,5,
                1,2,3,2,5,0,1,1,1,3,0,0,1,1,2,1,4,5,0,0,2,2,0,4,2,4,5,2,4,2,3,3,2,2,3,4,3,1,1,2,
                1,1,5,4,0,4,5,1,2,5,0,5,1,5,1,2,4,0,0,2,2,1,3,3,2,2,3,0,1,1,2,5,0,0,1,1,4,3,0,4,
                5,0,0,1,2,4,3,0,5,4,0,1,0,3,0,0,0,4,1,0,1,3,2,4,4,4,5,5,2,3,5,2,1,3,0,5,1,4,0,1,
                0,0,4,3,1,2,0,5,1,2,0,5,3,4,3,4,2,1,4,4,0,5,2,3,3,0,3,2,3,5,2,3,1,4,4,3,4,1,5,3,
                5,2,4,2,5,3,5,3,0,5,1,5,1,0,1,5,5,4,2,2,5,4,3,4,2,4,1,1,5,5,1,3,2,3,0,1,4,1,2,3,
                0,4,5,5,2,5,3,4,3,0,0,5,5,1,5,1,1,0,3,4,3,5,5,3,0,2,0,3,2,1,2,5,1,0,5,0,3,5,4,4,
                3,0,3,4,3,5,1,2,0,4,3,1,5,1,5,0,3,5,1,0,1,5,0,0,3,4,0,1,3,0,4,3,3,5,2,2,3,3,5,5,
                0,0,0,0,3,1,5,0,4,5,0,5,4,2,4,0,4,5,2,0,3,3,5,5,2,1,5,5,5,2,1,1,2,2,2,4,0,2,1,1,
                3,2,3,3,2,2,0,5,2,1,1,4,5,0,5,4,5,3,4,4,1,5,1,3,4,0,4,5,1,3,4,0,3,4,3,2,2,0,1,4,
                4,5,1,0,2,0,5,0,2,5,3,4,3,1,5,1,0,1,3,5,2,1,2,2,2,3,1,1,3,4,2,3,0,1,3,4,1,5,5,2,
                5,0,5,4,1,3,4,5,2,5,4,3,4,5,5,3,2,3,2,2,1,1,5,0,0,4,5,1,3,4,5,0,0,4,0,3,1,4,2,2,
                0,1,2,2,4,1,5,3,3,3,3,3,2,1,1,5,5,2,2,1,4,1,1,0,5,3,4,4,5,1,1,1,5,2,2,5,3,3,0,3,
                3,1,2,1,1,0,4,2,3,0,5,4,5,0,1,3,1,3,2,0,2,0,3,2,3,2,1,0,3,4,5,3,1,0,0,3,0,1,2,3,
                0,4,1,3,1,2,5,5,2,0,1,2,5,5,4,5,5,2,5,5,5,1,5,1,4,5,3,4,3,5,4,2,3,4,3,1,0,0,4,2,
                2,2,5,4,4,1,2,5,5,1,5,1,4,5,4,4,4,2,5,2,4,5,2,3,0,0,0,2,1,1,0,2,0,1,0,4,0,4,2,2,
                3,1,2,1,4,4,5,1,5,4,0,5,4,2,5,0,3,4,0,3,2,2,1,3,0,1,2,1,5,0,2,4,4,0,2,4,4,3,2,0,
                1,1,4,2,0,5,2,5,3,2,2,1,4,0,0,5,5,5,4,1,4,1,4,2,0,4,4,0,4,4,2,1,4,5,4,4,3,3,2,4,
                4,4,5,2,0,2,2,5,5,5,5,5,2,2,3,1,1,3,4,5,5,5,5,3,3,4,0,4,1,1,2,4,4,2,5,1,1,2,5,1,
                2,0,3,2,0,3,0,5,3,0,2,2,2,5,2,4,2,0,5,0,3,2,0,5,4,4,5,2,0,4,0,1,2,4,2,4,0,0,5,4,
                1,0,1,4,2,2,2,2,2,4,4,4,4,4,5,5,2,0,1,5,1,3,3,0,5,5,5,2,5,4,3,0,5,1,2,1,0,2,0,4,
                5,0,4,1,4,3,1,5,4,2,1,0,0,4,2,0,4,4,4,2,0,4,4,2,5,0,1,5,0,0,3,1,5,5,4,2,2,0,0,4,
                5,1,4,1,2,4,2,5,2,1,1,2,5,1,0,1,4,0,4,5,3,5,5,1,1,0,2,3,3,3,2,0,1,3,3,2,3,3,3,0,
                5,0,4,1,5,0,2,3,4,3,1,4,0,4,1,0,1,3,5,0,1,3,2,3,2,1,2,4,3,2,2,0,5,5,2,0,1,4,4,0,
                3,0,0,4,3,2,5,5,5,0,2,4,1,0,4,5,2,3,1,2,2,2,4,3,4,1,3,2,5,2,1,1,5,1,5,0,4,5,1,2,
                1,3,3,0,4,2,5,1,3,0,0,3,5,2,3,5,0,1,0,5,2,3,0,1,5,0,1,3,0,3,1,4,4,3,3,2,2,2,4,4,
                4,0,4,3,1,2,1,3,2,5,2,5,3,2,5,2,4,2,5,3,5,5,3,1,2,5,0,1,0,4,2,2,4,1,0,4,5,5,1,2,
                2,5,4,4,4,1,2,2,3,5,5,2,3,5,0,5,3,1,5,4,5,2,4,3,3,3,2,2,5,5,1,4,5,3,4,2,4,3,4,1,
                2,2,4,5,4,3,2,5,4,1,2,5,5,1,2,2,1,5,0,0,4,1,1,1,5,3,5,3,0,4,3,2,5,3,1,3,5,0,3,4,
                1,0,3,2,5,4,1,3,0,4,5,1,5,2,3,2,2,4,3,4,1,1,3,2,4,4,4,0,5,2,3,2,4,3,4,4,0,2,1,2,
                0,0,3,0,0,5,1,5,5,5,2,3,1,2,0,5,0,1,5,1,5,4,0,1,0,2,0,4,1,2,4,5,4,3,2,3,0,5,5,0,
                4,0,5,3,3,4,2,5,2,0,3,5,5,4,5,4,5,4,5,4,3,4,4,0,3,2,5,0,2,2,2,0,1,4,3,3,2,4,5,1,
                3,3,0,0,0,4,4,5,3,4,0,3,4,4,2,1,5,2,4,1,0,5,2,4,4,0,2,5,4,1,2,3,1,1,4,0,3,3,3,5,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=67;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=46;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=38;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=26;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level3L(){
        int[] Tab0 = {3,1,1,1,1,5,1,0,3,2,2,5,0,5,3,2,0,1,1,0,2,5,3,5,4,0,1,0,5,5,2,4,5,5,5,0,5,1,2,3,
                3,4,0,5,1,5,4,3,0,4,5,4,2,5,3,4,5,3,4,0,1,4,4,1,5,2,5,5,0,5,1,1,2,3,4,2,4,4,5,0,
                5,0,1,5,4,3,0,3,3,1,1,1,4,2,3,4,5,2,0,5,1,1,0,5,1,0,5,5,3,0,5,4,3,0,2,2,4,2,0,5,
                1,0,2,1,0,5,1,3,1,0,4,3,1,4,5,3,4,3,5,2,3,2,4,2,5,2,2,3,5,2,5,4,2,1,0,4,5,3,2,1,
                0,5,1,1,2,1,2,5,5,3,2,4,3,2,1,2,2,3,5,2,4,0,0,3,2,2,5,3,3,0,1,3,3,5,4,3,5,2,2,0,
                0,1,3,5,3,5,4,1,2,0,0,4,1,2,3,5,3,2,4,2,2,2,4,0,4,1,2,2,2,5,3,2,1,1,5,2,1,1,4,4,
                0,3,3,2,4,0,5,4,4,0,1,2,1,4,4,1,4,4,2,5,1,4,4,2,2,2,2,2,2,0,1,4,2,5,5,0,4,5,0,3,
                4,0,1,3,0,4,5,2,4,0,5,2,3,5,2,3,2,5,2,1,5,5,1,4,2,3,1,3,1,3,1,1,1,1,0,1,1,3,1,2,
                0,4,3,0,0,5,3,1,1,3,2,3,5,1,2,4,5,3,2,1,5,4,0,1,2,0,3,0,3,1,4,0,2,1,4,1,3,2,2,3,
                0,2,4,1,5,3,0,5,0,2,2,4,5,0,1,2,3,1,5,1,5,4,0,1,5,1,1,0,3,1,4,5,4,5,2,3,0,4,5,3,
                0,5,5,3,0,5,2,3,4,5,3,4,5,3,5,0,0,5,4,1,2,4,1,5,5,1,3,4,0,5,3,4,3,5,4,2,4,2,5,5,
                5,5,0,4,3,0,3,3,2,0,2,5,0,5,3,3,0,0,3,3,1,1,0,0,5,4,2,0,1,0,0,2,4,0,3,2,1,5,4,1,
                1,2,3,5,1,3,4,3,5,1,1,5,3,3,0,5,1,3,0,0,3,2,1,5,5,1,5,3,0,2,5,5,1,2,4,3,4,0,2,2,
                4,2,3,1,4,4,1,2,2,1,5,5,5,4,2,1,1,0,3,1,3,4,0,4,1,3,5,1,5,2,5,4,3,2,4,5,2,4,0,5,
                1,2,1,2,4,0,3,1,1,3,2,5,2,1,4,3,5,0,5,0,4,1,1,4,0,2,2,2,1,3,3,3,0,3,0,5,2,0,4,4,
                0,5,1,3,1,3,5,0,2,0,1,2,3,3,0,1,1,3,0,0,2,1,3,1,3,1,3,5,2,0,1,4,4,5,1,3,5,3,3,4,
                5,1,4,3,0,3,0,0,3,0,5,3,1,5,2,2,1,2,5,0,0,2,0,4,1,2,0,1,0,3,1,0,4,3,4,5,5,0,0,4,
                5,3,0,5,2,0,3,5,5,4,5,0,0,2,1,1,2,3,3,1,0,1,1,1,0,1,5,0,3,2,4,5,0,5,0,4,0,3,2,0,
                5,3,1,3,3,3,3,1,1,4,0,2,5,2,4,0,4,0,0,0,2,3,1,0,0,0,2,3,2,2,3,2,1,4,0,3,5,1,2,1,
                1,0,4,4,3,4,5,1,4,3,2,1,2,5,1,2,5,5,5,2,0,1,2,4,4,1,3,5,5,4,3,5,1,1,1,1,0,5,0,3,
                0,1,5,2,0,0,3,2,2,1,2,3,2,4,2,4,1,5,3,1,5,1,1,2,1,4,4,5,2,2,5,2,1,2,3,5,4,5,1,1,
                3,0,2,0,0,4,4,3,0,0,4,5,3,2,2,4,2,0,0,2,5,1,0,4,4,4,1,2,4,0,5,3,1,4,3,3,5,3,0,5,
                4,3,1,4,0,3,5,3,4,1,4,1,5,4,1,1,3,4,0,0,4,3,1,1,5,3,4,1,2,5,0,3,0,3,4,5,2,5,5,0,
                0,5,5,4,0,3,0,3,3,4,5,2,0,5,4,4,5,3,3,0,3,5,1,3,4,4,2,4,4,4,0,1,1,0,2,5,0,3,1,5,
                4,4,2,2,2,4,4,5,4,5,3,0,4,5,2,3,0,1,0,2,1,2,2,1,1,4,4,2,4,0,5,1,4,4,2,5,5,5,2,3,
                4,2,4,3,4,3,1,4,5,1,3,5,3,2,5,2,2,2,2,5,4,4,5,2,3,3,3,1,1,2,2,4,5,5,3,5,2,2,4,4,
                2,3,2,3,0,4,1,5,3,5,1,4,2,0,1,3,1,0,1,2,4,5,5,5,2,4,2,5,0,0,5,2,3,5,3,4,2,2,2,1,
                5,0,4,5,3,4,0,5,4,0,3,5,3,3,3,1,2,4,2,5,4,3,5,4,5,5,1,3,4,5,0,2,4,1,2,5,2,1,1,2,
                0,5,3,4,4,4,5,3,4,2,5,5,2,0,0,1,1,1,2,4,3,3,0,5,0,1,4,0,5,5,2,3,5,0,1,2,5,2,5,4,
                2,2,2,5,1,3,5,0,5,0,3,1,5,2,3,1,4,1,5,4,2,4,0,2,1,0,4,2,3,3,5,3,2,5,0,5,2,2,4,1,
                5,4,3,0,2,0,5,1,5,2,3,4,4,2,1,1,3,3,5,5,3,1,3,5,0,2,2,5,5,5,2,1,2,5,1,5,3,2,1,0,
                4,4,3,0,5,5,1,0,3,0,1,0,1,3,0,0,0,4,1,2,0,5,3,3,3,4,3,1,4,0,5,5,3,3,1,4,5,2,2,2,
                0,4,1,3,5,1,2,5,2,5,4,2,0,4,3,4,2,5,4,2,5,4,4,0,4,3,5,4,3,1,0,5,3,5,4,5,4,1,0,3,
                3,3,2,5,3,4,2,3,4,4,3,5,3,5,0,0,5,1,4,0,2,3,5,5,0,0,3,2,3,4,1,4,3,4,5,2,1,0,4,4,
                3,1,3,3,1,5,2,0,4,1,0,4,4,0,1,1,1,0,0,0,0,1,3,3,3,3,4,0,5,0,3,4,4,5,2,5,3,5,2,3,
                5,5,5,2,3,0,2,4,5,2,2,5,3,0,1,5,2,2,5,3,0,2,3,1,2,3,2,0,5,5,4,0,1,0,4,5,0,2,4,2,
                5,4,5,2,5,4,4,5,4,1,1,0,4,5,2,1,3,0,4,4,4,3,0,3,4,5,2,3,3,0,5,1,1,4,4,3,2,4,0,2,
                4,1,4,1,3,1,0,2,0,2,3,0,3,1,4,5,1,0,2,0,1,4,1,3,2,4,3,0,4,5,5,1,4,1,3,4,0,2,5,4,
                3,0,4,0,4,3,0,1,1,4,3,0,4,0,2,3,5,3,5,5,4,1,3,5,4,4,2,1,2,1,1,2,4,4,0,0,5,4,0,2,
                0,2,4,3,0,5,4,2,2,1,2,4,3,2,2,2,2,0,2,5,1,4,1,2,0,5,3,3,5,4,4,1,0,5,4,0,0,3,5,0};
        int[] Tab3 ={5,3,2,1,3,2,5,0,1,3,4,4,5,4,1,1,3,2,4,1,0,4,3,4,5,1,0,2,2,0,5,1,1,3,2,3,3,0,2,4,
                3,4,5,3,2,2,3,2,3,5,1,3,4,2,1,3,1,3,1,1,2,0,4,0,2,0,0,3,2,1,3,4,5,5,2,5,3,2,3,3,
                2,3,5,4,1,2,4,4,3,5,5,5,1,2,3,4,5,1,5,0,4,4,0,3,5,3,3,1,0,3,2,2,0,3,3,1,5,4,0,0,
                4,2,1,5,2,4,3,4,4,1,2,2,1,1,5,5,1,1,5,3,0,3,2,4,3,0,4,0,1,0,2,4,0,1,0,2,1,3,2,4,
                0,2,1,5,0,4,3,5,3,2,5,3,2,5,0,4,0,1,3,4,1,0,5,4,1,2,3,1,1,5,1,3,2,5,5,5,4,0,4,4,
                5,5,0,0,4,0,4,1,0,3,3,2,3,0,3,4,0,2,1,0,2,2,0,5,0,3,4,2,3,1,3,1,5,4,4,0,2,4,3,2,
                2,2,1,0,4,3,1,4,0,5,3,2,2,5,5,5,2,0,5,2,1,3,0,5,4,4,4,2,4,0,0,1,2,2,0,5,5,4,5,5,
                2,0,2,3,2,2,2,1,3,3,4,2,2,0,2,2,1,5,1,3,1,5,3,1,0,0,3,2,0,5,0,2,0,2,3,2,4,2,4,5,
                0,5,4,0,1,1,1,3,4,2,2,0,2,0,0,0,1,3,1,5,1,2,4,1,2,1,5,0,5,3,2,4,5,4,3,4,3,4,4,5,
                1,4,1,4,5,3,3,0,2,0,5,0,5,3,0,4,5,0,5,1,3,5,2,4,3,1,3,2,0,5,0,1,4,1,4,1,4,0,4,5,
                1,2,3,3,4,0,2,5,0,2,0,2,0,2,0,0,1,5,1,2,4,3,1,4,3,1,0,0,5,0,3,5,3,1,0,4,1,4,4,5,
                1,4,4,0,1,0,1,4,5,5,2,3,0,0,5,4,0,0,1,1,3,3,4,1,2,4,0,4,2,3,2,5,2,4,0,1,1,2,2,4,
                1,3,1,3,2,0,4,0,4,0,1,4,3,5,2,5,3,4,2,3,3,1,0,1,2,1,4,5,2,5,1,1,4,0,5,4,4,2,4,1,
                0,0,2,1,3,0,2,0,3,1,2,5,5,5,2,3,2,2,3,3,0,5,4,1,3,1,1,2,4,0,5,4,0,3,0,4,2,2,4,5,
                2,4,1,2,5,0,4,4,3,3,5,4,2,1,5,1,4,0,4,1,4,4,1,0,2,2,0,4,2,1,5,0,4,5,5,1,5,1,2,2,
                5,1,5,0,2,2,3,1,2,0,2,4,0,3,4,4,4,1,2,5,3,3,2,0,4,1,4,4,5,0,4,4,0,1,2,4,2,5,2,5,
                0,1,5,3,2,3,2,0,0,3,4,5,0,5,3,3,1,5,1,2,0,4,0,5,5,5,3,2,3,0,5,1,0,0,4,5,5,4,0,5,
                2,3,1,0,3,0,3,3,1,1,0,0,0,4,5,5,3,0,3,5,4,1,4,0,0,2,3,3,2,4,1,4,3,0,1,2,1,1,2,1,
                3,5,5,3,5,4,5,5,2,1,5,3,1,4,2,0,1,0,3,2,0,0,1,3,3,2,5,4,2,1,2,2,1,3,3,4,0,2,1,4,
                5,1,2,1,2,0,5,2,0,3,0,4,0,2,3,0,5,1,5,0,0,4,1,2,2,5,1,3,4,2,5,3,5,0,5,2,4,4,1,3,
                1,1,1,4,1,0,1,4,0,3,4,4,0,4,1,3,3,5,1,5,5,2,2,3,4,2,3,2,4,5,0,2,5,3,0,2,3,5,3,1,
                0,5,0,2,3,1,5,5,0,0,3,0,1,5,0,3,4,1,3,3,1,1,0,3,3,1,2,3,3,3,4,0,0,5,0,2,5,3,3,4,
                5,2,4,4,5,5,2,3,2,2,2,0,4,3,3,3,3,0,2,0,5,2,2,2,0,3,1,3,2,2,2,0,1,5,0,3,3,3,1,0,
                3,5,2,0,2,2,1,2,1,1,1,1,1,3,4,0,0,4,0,5,5,0,3,0,1,0,5,2,5,3,3,0,5,0,2,3,1,2,4,4,
                4,0,4,3,2,4,0,2,2,3,2,5,4,0,0,2,5,2,0,1,4,1,2,1,4,0,2,0,3,1,2,1,3,0,2,5,4,5,4,5,
                0,4,2,0,2,2,4,1,2,5,5,1,1,4,2,5,3,2,3,4,5,1,5,1,4,0,0,3,4,1,2,3,4,0,1,3,5,2,3,1,
                1,0,5,5,0,4,1,3,3,1,3,1,2,2,3,3,5,3,5,1,4,4,0,0,2,4,4,1,1,5,3,2,2,0,5,0,1,5,1,1,
                2,5,3,4,3,5,3,1,1,5,0,5,4,4,4,0,4,5,4,4,3,5,4,4,1,2,2,5,5,5,4,1,5,3,4,0,3,0,1,2,
                5,3,4,3,5,2,1,4,4,2,4,1,2,2,1,3,5,2,4,2,0,4,5,3,5,4,1,3,5,0,4,3,3,4,0,0,0,4,1,0,
                5,5,1,4,2,1,4,3,3,3,2,2,5,1,5,1,2,4,4,3,5,3,0,3,0,4,4,4,1,2,3,0,3,3,1,5,1,0,0,1,
                3,0,2,1,1,3,2,2,3,2,1,4,3,5,4,0,1,4,3,1,4,3,3,5,3,0,5,3,2,1,2,0,0,5,1,5,5,2,5,0,
                0,3,5,2,0,3,2,3,0,3,5,1,0,0,1,0,0,3,1,4,1,3,0,2,1,5,4,1,4,4,3,0,1,4,3,0,5,5,5,4,
                0,5,5,1,5,2,4,5,4,1,4,3,4,0,5,1,4,5,1,3,3,1,4,5,4,1,5,2,4,1,2,3,3,5,1,3,0,4,4,1,
                3,4,0,3,2,4,2,2,1,0,0,0,1,1,1,0,5,4,3,4,1,5,3,4,3,2,2,3,3,1,4,3,2,5,0,1,4,2,1,1,
                1,2,0,5,1,5,4,2,0,4,4,4,4,5,0,0,4,4,3,3,2,4,1,5,2,5,5,4,0,0,0,1,3,1,5,3,0,5,5,1,
                2,3,1,2,4,4,5,3,2,4,1,5,0,4,1,1,5,1,0,4,4,2,1,3,1,0,5,3,4,5,2,0,2,5,5,3,0,5,2,4,
                1,1,2,3,2,1,5,3,4,3,2,2,5,1,0,3,1,4,3,5,0,2,3,4,4,2,2,1,2,2,3,3,0,2,2,5,4,3,4,1,
                2,0,0,4,1,1,5,4,0,0,1,2,5,0,0,0,4,4,3,2,4,3,2,0,0,1,2,2,4,2,0,5,3,5,5,2,0,4,4,2,
                1,1,0,4,2,2,1,1,0,5,2,2,1,4,2,4,3,0,1,0,5,2,1,0,5,0,0,4,4,0,0,5,5,5,0,5,3,0,4,0,
                2,5,4,3,4,5,0,1,3,5,1,5,5,0,3,1,3,0,2,4,3,1,2,5,2,3,4,3,2,4,1,5,2,3,3,2,1,5,2,0,
        };
        int[] Tab2 ={5,4,1,5,2,2,1,3,3,1,2,4,4,1,3,2,3,5,0,2,2,5,5,5,3,1,3,2,4,4,5,1,2,3,4,0,2,4,5,3,
                3,5,0,0,3,5,5,4,3,2,0,4,4,4,2,5,2,4,3,0,2,5,5,4,1,2,5,0,5,2,5,2,1,1,5,4,5,1,2,2,
                3,3,0,5,2,2,2,2,0,2,0,2,1,0,5,0,1,3,4,0,2,2,0,4,5,2,0,3,2,3,3,5,3,1,5,4,3,3,2,4,
                2,4,5,5,1,2,5,0,5,1,1,4,5,3,4,1,2,0,3,4,5,4,2,3,0,3,4,0,3,5,2,3,2,4,5,0,3,3,5,3,
                0,5,1,2,2,4,0,1,3,3,1,0,4,5,3,5,0,1,2,4,4,0,2,3,5,1,2,1,5,1,0,1,3,2,3,1,3,5,5,0,
                0,4,1,2,3,4,5,3,4,5,1,3,2,0,4,0,4,5,5,5,3,3,0,2,3,4,4,1,2,2,2,3,4,0,4,5,2,1,4,4,
                1,2,0,4,1,4,5,0,2,3,3,5,5,2,3,3,0,1,2,5,0,4,1,4,3,0,4,4,3,5,2,2,2,1,2,2,0,0,5,4,
                4,2,1,4,5,4,5,3,1,0,2,4,3,3,0,2,1,0,4,0,1,4,5,4,4,4,1,3,3,2,0,2,0,2,2,4,4,1,2,0,
                5,0,5,3,5,5,4,1,4,2,3,4,0,4,0,0,5,3,0,3,4,4,3,1,1,2,2,1,2,5,3,4,2,1,1,5,0,1,3,0,
                2,1,1,2,0,0,2,4,0,4,2,1,3,3,2,1,2,0,3,0,5,5,5,1,3,1,3,1,4,2,5,5,0,0,3,5,1,2,2,0,
                4,1,1,5,4,2,5,1,0,0,4,0,3,4,4,4,2,1,2,0,0,0,5,1,0,5,2,3,1,0,2,2,2,0,0,1,4,5,5,5,
                2,3,0,2,4,2,4,1,4,4,5,5,0,3,5,5,1,2,0,1,4,4,2,1,5,2,4,4,5,5,2,4,0,2,4,3,1,1,3,1,
                5,0,4,3,2,2,1,3,4,4,4,3,1,4,4,3,1,4,3,4,5,2,0,1,3,4,5,1,0,4,5,0,5,4,2,3,0,5,5,3,
                4,0,3,2,4,0,1,1,3,3,2,4,4,0,2,0,0,1,5,2,2,0,1,5,1,5,1,4,4,3,5,5,2,0,3,5,5,3,4,0,
                2,1,4,1,2,0,0,1,4,1,0,1,2,3,1,2,1,1,2,3,2,0,0,3,2,1,5,1,4,1,0,2,5,2,2,4,4,5,4,2,
                2,0,5,1,3,1,1,5,0,2,4,1,4,4,5,1,1,4,4,5,3,4,5,1,1,0,2,4,2,1,0,5,3,4,0,3,1,4,0,0,
                0,5,1,2,5,2,0,1,0,1,5,4,5,3,5,1,2,2,3,0,2,2,5,3,0,5,3,5,3,3,4,1,4,5,5,1,5,1,1,5,
                4,4,4,1,0,4,0,4,1,5,4,3,0,5,0,1,1,0,2,0,1,5,4,0,1,4,4,4,2,1,0,3,4,0,0,3,2,3,0,2,
                1,4,1,0,4,4,0,5,5,3,2,2,3,2,2,1,3,3,4,4,1,2,3,4,0,0,0,3,5,0,4,5,3,1,5,2,2,3,5,0,
                5,1,4,2,3,2,4,0,3,3,3,1,5,3,1,3,3,4,4,4,3,5,4,5,2,4,5,4,4,3,4,3,1,5,3,2,2,0,1,3,
                4,4,1,4,0,2,5,2,2,3,5,5,4,0,2,2,4,1,3,1,1,5,2,0,2,5,5,3,0,0,2,2,3,4,2,2,3,0,2,4,
                4,0,1,5,1,5,3,4,2,2,0,1,1,1,5,0,2,0,1,0,4,0,4,5,3,4,0,4,2,5,0,1,2,5,3,5,0,0,5,3,
                1,1,1,1,2,1,1,5,3,0,2,4,4,3,0,0,4,1,1,3,0,3,5,2,4,2,4,3,3,1,3,2,2,1,0,3,5,5,0,4,
                2,3,2,2,4,4,3,2,3,3,1,2,1,1,5,5,5,0,1,4,3,2,1,1,0,5,0,4,0,0,2,1,4,1,0,3,1,1,4,3,
                5,2,2,0,2,5,5,1,5,4,5,0,1,1,1,3,4,1,4,4,1,1,0,3,5,2,1,2,2,0,5,2,5,5,3,0,4,4,2,2,
                2,4,4,3,5,3,0,2,3,5,1,4,2,4,1,2,0,4,5,4,1,0,0,2,0,3,0,3,5,5,1,0,0,3,3,2,2,0,2,5,
                1,4,1,0,3,1,1,4,0,0,5,2,1,4,4,5,1,1,1,2,3,4,2,1,5,0,0,3,0,2,3,4,5,1,2,2,4,1,3,1,
                4,3,4,4,4,0,3,4,1,1,3,2,5,3,1,5,0,3,0,0,3,4,1,3,0,5,4,5,3,5,2,1,5,5,4,5,5,4,4,0,
                0,1,1,1,3,0,4,2,5,2,4,2,2,5,0,2,0,4,2,1,5,1,5,2,4,0,0,3,4,5,1,1,3,2,1,4,1,0,3,0,
                5,2,2,2,1,3,5,1,2,2,4,2,0,5,2,2,5,2,5,3,0,5,4,1,3,3,4,1,3,4,3,1,1,4,5,5,3,2,3,4,
                4,4,0,1,0,5,1,1,5,3,0,1,2,0,0,2,0,5,3,3,1,4,3,3,3,0,2,4,5,1,4,1,2,5,3,3,5,0,4,0,
                3,3,4,2,4,3,3,0,1,2,1,2,5,0,5,3,4,2,2,4,2,2,2,4,0,3,2,2,1,3,0,1,4,5,1,4,2,5,5,5,
                2,2,5,3,3,2,1,5,4,1,5,4,3,1,5,5,0,2,0,4,5,2,5,1,3,2,5,2,5,2,0,3,0,2,5,1,4,5,3,4,
                3,0,2,0,2,1,3,4,1,5,4,0,3,3,1,4,5,0,5,2,3,4,5,4,1,3,5,3,2,4,1,2,1,1,2,0,4,3,3,5,
                1,1,4,4,5,5,1,2,0,5,0,3,0,5,0,4,1,1,0,3,3,1,2,0,4,5,5,1,5,5,5,0,5,4,4,4,1,3,1,2,
                5,1,3,2,2,4,1,4,3,0,1,3,1,1,0,2,3,5,0,5,1,5,3,1,4,3,1,3,3,5,1,4,1,2,4,2,5,0,5,0,
                1,1,5,4,0,4,5,1,2,1,0,2,4,2,4,3,5,5,4,5,4,1,4,2,4,4,4,2,5,1,2,5,3,3,1,5,5,2,0,4,
                4,2,1,3,3,4,2,0,1,4,2,1,4,5,0,0,2,4,2,1,2,5,2,1,2,2,3,4,2,4,1,3,4,0,2,2,0,4,2,5,
                2,0,0,1,5,0,5,1,0,3,2,0,5,3,5,1,1,3,4,5,3,2,0,3,1,3,0,2,2,2,2,5,1,4,2,1,5,1,4,4,
                2,3,1,5,4,2,4,1,4,5,5,1,0,3,0,3,0,3,3,0,2,4,0,2,5,5,1,2,4,4,1,5,3,0,0,4,5,4,2,5,
        };
        int[] Tab1 ={3,1,5,4,0,1,4,3,0,0,4,4,3,4,0,2,2,2,0,5,2,3,0,5,1,3,0,0,3,0,3,2,4,0,5,3,0,3,3,0,
                1,3,2,3,0,5,5,3,4,2,5,4,4,0,1,4,2,5,0,5,4,5,0,3,2,0,0,0,0,1,1,2,1,1,3,0,4,3,2,2,
                2,2,1,5,2,2,2,4,1,0,1,4,5,1,5,5,4,3,5,0,3,5,3,0,3,0,5,1,3,5,3,2,3,4,2,0,5,4,0,2,
                5,4,2,1,2,5,2,4,3,4,4,3,2,2,3,4,3,2,5,4,1,2,0,2,0,5,5,1,4,1,4,1,1,1,3,2,5,0,4,0,
                4,3,3,3,1,2,2,3,0,2,1,0,0,0,1,4,1,5,3,0,3,2,2,1,0,4,4,0,0,3,4,1,5,2,2,3,3,1,5,5,
                0,2,5,3,1,4,2,1,4,2,3,0,3,5,4,5,3,5,4,5,1,5,4,3,4,5,4,1,4,5,2,3,2,0,4,1,1,0,5,4,
                0,4,4,1,2,3,5,0,3,2,3,3,1,3,3,2,0,0,2,1,3,2,3,0,1,2,2,4,2,4,5,3,3,5,4,1,2,5,4,0,
                5,5,2,3,4,5,1,4,0,5,0,3,4,1,5,3,0,2,2,5,3,2,4,1,2,0,3,1,4,5,2,4,4,2,3,3,1,2,0,3,
                3,1,4,2,2,2,3,3,3,5,3,2,3,2,3,1,5,1,0,5,2,4,3,3,3,2,2,3,4,3,3,2,4,0,3,2,5,5,1,2,
                3,5,1,3,4,3,3,2,5,0,3,3,3,0,1,5,1,1,4,4,2,3,5,3,5,4,5,5,5,2,4,3,5,4,0,3,1,3,2,2,
                4,5,0,3,5,3,3,3,3,1,1,2,1,3,3,0,2,1,2,4,4,0,4,0,2,4,2,4,0,3,5,5,5,4,2,0,2,0,0,2,
                2,4,0,1,4,1,5,4,0,5,3,5,0,1,5,4,5,5,2,0,1,2,2,3,3,3,5,3,0,0,4,1,5,2,1,3,2,4,3,2,
                4,1,1,4,5,0,3,2,5,5,3,0,4,0,0,5,3,5,1,1,1,1,1,4,2,1,2,1,3,3,0,2,3,3,0,2,1,0,2,0,
                0,4,5,2,3,4,4,1,2,0,0,2,1,5,1,1,4,2,5,1,3,3,1,5,1,3,0,1,2,2,4,5,0,1,4,4,4,0,5,1,
                3,0,2,2,4,0,2,2,1,5,2,5,2,1,1,4,5,5,0,5,4,5,5,2,3,2,2,5,3,4,4,4,5,2,1,5,1,5,2,4,
                0,0,5,2,2,2,0,2,1,1,5,5,0,5,1,4,5,3,4,5,0,1,3,5,5,3,0,0,2,1,4,1,5,1,2,4,3,4,4,0,
                1,0,5,5,4,5,2,5,1,5,5,4,3,4,2,3,2,5,5,2,2,0,3,1,3,2,3,2,3,4,2,0,0,0,0,3,5,3,5,2,
                0,5,5,5,4,3,5,3,3,1,2,0,3,4,1,3,1,4,2,4,1,3,0,4,2,4,1,2,1,1,4,2,1,5,0,2,5,1,1,5,
                3,3,2,0,2,4,4,0,0,1,3,4,2,1,3,5,1,5,0,2,5,1,2,1,1,4,1,1,4,1,2,1,2,5,3,4,1,2,2,5,
                5,5,0,2,2,5,2,4,4,0,3,5,1,4,0,4,1,2,1,5,0,0,1,0,5,2,1,4,3,1,5,2,5,2,4,2,4,1,5,4,
                2,1,3,2,2,4,0,4,1,2,4,4,3,3,5,3,2,2,3,4,3,2,1,0,1,2,5,5,3,4,5,2,0,3,5,5,5,1,4,3,
                0,4,2,4,4,5,4,3,2,0,4,1,5,5,1,4,4,0,1,0,5,2,0,4,1,4,1,4,0,1,3,0,3,5,5,5,1,0,4,1,
                5,5,4,4,2,5,1,4,3,4,1,5,5,3,1,1,2,3,5,5,5,4,2,0,1,4,4,3,3,1,4,5,0,2,1,2,5,5,0,3,
                5,2,1,5,5,4,3,0,0,3,3,4,2,1,1,1,4,5,4,5,0,3,3,2,4,2,2,5,0,4,3,4,3,4,1,5,3,5,4,1,
                5,0,0,4,3,4,5,0,2,4,1,0,1,1,1,5,0,1,2,1,3,5,0,4,2,2,1,4,3,3,4,3,2,3,3,0,2,2,1,4,
                2,4,5,0,3,2,5,4,5,2,3,2,1,1,2,5,1,3,3,0,3,0,2,4,3,3,2,4,1,4,1,4,5,4,5,5,4,1,2,2,
                0,3,1,5,3,2,2,0,2,3,3,1,5,5,1,1,1,2,1,2,0,5,0,4,0,0,0,1,4,4,0,0,4,2,5,3,5,1,0,5,
                2,0,1,3,5,5,3,3,0,0,5,0,5,4,4,2,0,5,4,2,5,0,5,2,0,1,3,5,1,3,0,5,2,2,0,5,1,2,5,0,
                5,2,5,2,3,1,1,4,0,0,1,5,5,2,2,1,5,4,5,1,3,3,3,5,5,3,1,5,5,1,3,0,4,1,3,5,5,3,2,3,
                1,1,5,1,2,4,4,4,4,0,0,1,4,4,5,1,1,1,5,5,3,1,4,3,1,3,5,5,3,0,0,3,1,5,1,4,4,4,1,5,
                4,5,5,1,0,2,3,1,1,4,3,0,5,3,3,1,3,5,5,4,4,4,0,4,4,2,0,4,4,1,4,3,1,5,0,0,4,2,2,3,
                3,5,0,4,3,2,4,2,4,5,4,5,0,5,0,1,1,3,0,3,1,1,3,1,3,4,3,4,4,1,2,3,4,0,2,1,5,2,4,2,
                2,0,3,4,0,5,2,3,4,5,5,3,3,3,3,3,5,2,4,5,5,0,0,1,1,3,0,0,5,2,3,0,0,2,2,0,0,5,1,1,
                2,5,0,4,5,2,1,4,1,2,4,0,5,0,2,1,5,3,0,1,0,0,3,5,1,4,1,1,5,5,4,0,5,5,1,1,5,2,5,5,
                2,4,4,5,2,2,5,3,1,4,2,2,4,4,3,3,1,3,1,1,3,0,3,2,2,4,0,3,0,0,5,4,3,4,2,2,4,0,4,3,
                5,5,3,2,0,0,4,3,0,3,4,3,0,5,1,3,3,4,2,4,4,3,2,4,4,2,2,1,3,1,5,3,1,5,1,5,1,2,5,5,
                3,2,0,3,2,4,5,4,5,1,0,0,4,0,5,0,4,4,1,3,1,4,2,2,2,1,1,1,1,3,2,4,4,5,2,3,3,0,3,5,
                5,3,4,3,2,2,4,2,5,5,2,3,4,4,2,5,4,0,0,3,3,1,3,3,3,2,1,1,1,2,1,4,2,0,4,0,4,4,2,5,
                0,3,5,0,0,0,0,2,1,4,2,5,4,3,3,3,3,4,5,1,2,5,4,3,3,5,1,3,0,1,3,0,0,5,2,4,3,2,4,0,
                4,1,5,3,4,4,3,0,2,3,2,0,4,0,1,1,1,0,3,2,2,3,4,4,4,2,5,0,5,1,3,3,5,2,1,5,5,0,0,0,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=71;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=46;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=36;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=21;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level4L(){
        int[] Tab0 = {1,4,3,0,2,3,2,0,3,2,3,1,3,4,3,4,2,0,3,2,5,4,4,2,0,1,4,2,5,3,3,1,1,5,4,2,1,1,3,5,
                2,1,3,0,3,3,1,4,5,3,2,3,4,3,1,5,1,0,4,0,4,2,0,3,4,3,4,2,5,5,1,4,5,3,4,5,4,0,4,0,
                3,2,4,4,5,2,1,1,2,0,4,5,4,1,5,3,5,4,5,2,5,3,5,2,2,0,1,2,3,2,1,5,2,3,2,4,5,5,2,3,
                4,3,5,0,0,4,0,0,0,2,4,3,4,2,5,1,4,0,5,4,1,2,4,3,3,2,0,2,3,1,2,3,5,3,1,1,5,4,2,4,
                5,1,2,0,4,3,5,1,1,1,1,2,0,3,2,4,1,3,5,2,0,5,3,2,4,0,5,1,0,3,4,3,5,1,1,0,2,3,5,5,
                1,1,4,4,1,0,2,1,2,0,2,2,4,2,3,4,4,3,5,5,3,0,4,0,2,4,0,5,1,4,3,1,1,1,3,5,1,0,1,4,
                4,3,0,3,1,3,5,3,3,4,3,3,2,1,4,4,2,4,5,3,1,1,4,0,3,3,4,5,4,4,2,5,5,2,0,0,3,4,2,2,
                1,4,0,5,1,5,5,3,5,5,0,5,3,3,0,5,3,4,2,2,1,5,1,1,1,1,5,3,3,5,4,4,5,3,0,2,1,5,3,1,
                3,1,1,3,0,3,5,5,0,0,0,2,3,4,5,5,5,3,3,5,3,5,2,5,3,5,2,1,0,4,4,4,5,3,4,2,3,0,5,5,
                2,4,2,5,4,1,4,3,5,2,3,4,4,1,3,1,1,5,2,4,0,1,2,3,4,4,0,1,2,3,2,2,3,2,3,3,3,1,0,3,
                3,1,0,3,2,2,1,5,3,3,3,0,0,1,3,1,0,4,5,4,3,3,1,4,0,3,1,3,3,4,1,0,0,2,3,3,2,2,5,1,
                5,3,0,5,3,5,5,0,1,0,4,4,5,3,0,1,2,2,2,0,2,1,0,0,2,1,5,0,1,4,2,2,0,5,1,2,2,2,4,2,
                1,0,4,3,3,4,5,5,4,5,5,5,5,0,3,4,3,3,2,4,3,1,0,2,0,4,1,2,2,5,5,2,0,1,2,0,0,1,0,4,
                5,3,1,2,5,2,5,0,0,0,3,4,5,2,3,3,5,5,5,1,5,5,2,3,3,1,1,4,1,1,5,4,4,3,2,3,4,2,5,0,
                2,5,3,1,4,3,5,4,0,4,4,3,0,5,1,1,5,1,1,3,4,3,4,3,0,1,2,5,3,1,0,5,2,3,1,1,5,2,0,5,
                4,1,1,1,4,1,5,0,1,5,1,1,4,4,3,4,0,1,1,0,5,1,5,0,0,2,2,1,4,2,5,5,3,2,5,0,5,2,3,2,
                3,3,2,5,2,0,3,1,0,0,2,1,4,2,4,3,3,4,2,4,0,2,4,5,4,5,5,0,5,4,1,2,3,5,3,0,4,2,0,4,
                2,3,4,1,1,1,2,2,2,3,0,2,0,1,3,4,4,3,3,0,5,2,4,5,1,5,1,0,3,2,2,4,0,4,3,0,5,1,3,0,
                3,3,3,5,0,2,4,1,0,1,2,0,3,0,0,1,0,3,3,4,4,5,0,5,2,3,5,3,3,0,1,1,5,2,4,0,3,0,2,5,
                4,1,1,3,5,1,5,4,3,5,3,2,5,3,0,3,3,4,0,4,4,4,2,3,3,0,4,4,0,4,3,0,2,4,4,2,3,3,2,3,
                5,3,4,5,1,3,5,3,1,2,3,4,2,4,1,5,0,3,1,3,5,0,4,4,0,3,0,5,0,4,5,0,2,5,1,4,5,0,1,4,
                4,3,3,5,3,5,4,2,4,3,2,2,1,2,4,1,3,2,3,1,3,3,0,5,4,1,0,3,5,1,4,3,3,2,4,4,4,0,1,4,
                1,5,0,5,1,3,0,1,5,2,1,2,1,2,5,1,3,4,5,1,4,5,4,3,2,3,0,4,4,1,4,5,3,5,2,1,3,1,1,0,
                2,5,2,2,0,3,4,5,1,2,5,4,3,1,0,0,4,3,0,1,0,5,3,2,3,0,1,1,4,1,3,0,3,3,0,1,5,2,5,3,
                3,2,4,4,0,4,0,0,5,4,4,0,0,0,1,0,0,2,2,1,3,5,5,2,4,4,0,5,3,3,0,3,0,2,5,0,4,1,5,3,
                1,0,1,5,5,5,2,4,3,2,2,1,0,2,3,4,1,2,4,2,3,1,0,2,3,3,3,3,4,5,1,1,1,5,2,2,4,2,4,5,
                0,4,5,0,2,2,1,0,0,5,3,1,3,0,3,1,2,3,4,4,4,5,2,3,1,5,5,4,5,0,3,0,4,4,2,3,4,4,5,3,
                2,1,4,1,1,4,5,4,5,5,5,1,1,0,1,1,1,4,1,4,5,0,3,3,2,5,0,4,3,5,4,0,2,2,0,1,5,0,4,5,
                2,0,0,0,0,0,4,3,5,0,5,1,5,3,4,4,4,3,5,0,0,2,2,5,1,4,0,3,1,5,5,3,2,4,5,4,2,4,5,2,
                3,1,4,5,2,2,2,5,4,5,2,3,1,4,1,2,3,1,0,1,0,0,4,0,0,3,4,2,4,4,4,3,4,5,3,0,2,4,4,2,
                4,5,5,2,2,4,1,1,5,1,5,5,1,1,5,0,1,1,1,5,4,5,3,0,3,2,3,3,4,4,0,1,2,3,0,2,2,1,0,1,
                4,2,2,0,0,3,2,2,1,2,3,1,2,3,4,1,1,2,1,5,3,3,0,2,1,3,5,3,4,0,5,1,4,2,2,1,2,3,1,1,
                4,0,4,4,5,3,1,5,2,2,3,5,1,4,3,2,3,1,4,4,3,0,4,3,3,5,4,1,1,4,5,5,2,5,1,5,0,4,1,4,
                4,0,4,5,3,2,4,3,1,4,4,5,5,4,2,5,2,4,2,5,4,2,5,0,4,0,1,2,2,3,2,4,1,3,5,3,0,4,5,1,
                1,0,3,0,3,3,0,2,0,1,1,1,4,5,3,0,2,0,3,2,4,2,2,0,5,1,2,2,0,2,3,3,3,4,0,3,0,3,3,0,
                0,1,1,1,4,4,2,2,4,3,0,0,0,3,2,3,4,1,0,3,2,5,4,0,0,2,4,2,2,1,0,1,1,3,4,1,1,4,5,5,
                5,4,4,2,0,3,1,5,4,5,5,2,1,1,1,0,0,0,4,1,0,5,5,5,0,1,2,2,1,2,0,3,1,4,0,5,3,5,1,0,
                0,1,5,1,5,1,4,5,3,1,5,3,1,0,3,0,4,4,1,5,0,5,4,4,4,1,1,5,3,3,1,3,4,1,5,1,5,1,2,3,
                1,5,5,1,2,2,4,4,5,5,2,3,5,2,2,3,1,0,0,3,4,1,3,4,2,2,2,3,4,0,2,1,1,5,1,5,1,0,4,5,
                1,0,3,0,1,2,2,3,4,1,0,2,0,2,3,2,3,3,2,5,0,3,0,2,1,2,2,5,1,5,1,3,5,5,3,2,3,5,2,2};
        int[] Tab2 ={5,4,1,0,4,1,2,2,3,1,0,5,5,1,1,1,3,2,2,0,0,1,2,0,1,0,2,0,5,0,3,0,1,3,3,5,2,0,3,3,
                4,2,5,4,1,4,0,1,5,4,0,1,4,3,5,5,1,1,5,2,2,1,2,1,4,4,3,1,3,0,4,4,5,3,0,1,5,2,0,4,
                4,4,0,2,5,0,2,4,1,0,5,4,4,0,2,5,4,4,0,1,5,5,3,2,5,2,1,0,3,3,5,3,0,2,1,0,5,0,2,3,
                4,3,3,2,3,2,4,3,3,1,0,1,4,4,2,0,2,5,3,0,3,5,0,0,3,2,0,2,1,0,1,0,1,4,0,2,4,2,0,5,
                5,1,0,1,0,2,1,0,4,5,1,4,5,1,3,5,5,4,4,3,2,2,1,0,1,5,1,1,2,3,3,5,4,0,1,1,5,4,0,0,
                0,1,0,3,4,5,2,3,3,0,2,4,1,5,4,5,5,3,4,2,2,2,5,1,3,4,4,2,1,3,3,4,4,5,3,3,3,2,3,1,
                5,0,2,5,4,3,2,5,0,3,2,5,0,2,5,3,1,5,0,2,0,1,5,3,0,4,1,4,5,5,5,3,5,4,3,1,4,3,2,0,
                2,3,0,1,5,4,5,1,3,5,1,5,0,1,2,3,4,5,5,5,4,3,2,5,4,0,2,1,4,4,0,2,1,4,3,1,2,0,4,5,
                3,5,0,3,3,3,4,0,4,4,5,3,1,5,1,0,4,2,5,3,0,5,0,1,0,0,4,1,3,5,1,0,3,2,3,3,1,4,1,4,
                3,2,0,3,1,4,4,4,3,2,0,5,1,1,4,3,5,0,1,5,1,1,0,1,5,4,2,2,4,1,3,3,0,1,5,1,0,3,3,3,
                0,0,0,0,4,3,3,4,5,4,3,0,2,5,5,1,2,4,1,5,2,5,5,1,3,3,4,5,1,3,1,4,5,1,5,3,0,0,5,0,
                2,5,1,3,3,3,5,5,3,2,0,0,1,2,5,1,4,2,2,2,1,1,3,4,4,1,4,5,3,1,0,1,0,5,4,0,4,4,0,4,
                5,3,2,1,5,1,5,3,1,4,3,0,2,0,1,3,4,5,4,0,5,5,4,3,4,0,3,0,1,0,0,0,2,1,5,1,5,5,1,3,
                2,3,2,0,5,3,4,3,5,4,5,2,2,3,5,3,5,4,1,3,0,0,1,2,2,3,0,5,3,3,3,2,2,3,1,3,3,5,3,3,
                5,0,4,4,3,0,2,1,0,3,1,1,4,4,5,2,5,5,4,0,1,0,1,1,5,0,0,1,1,3,2,5,2,0,5,1,4,2,2,4,
                4,1,5,4,0,3,4,4,1,1,1,0,0,1,0,4,1,2,4,5,3,5,2,1,3,3,0,3,1,4,5,0,4,2,2,2,5,0,3,5,
                1,3,4,1,3,5,2,1,3,1,3,5,3,4,1,2,3,5,2,3,3,0,0,5,5,0,3,3,3,0,2,5,4,5,1,0,3,5,3,2,
                3,1,2,4,5,2,4,1,3,2,0,3,5,1,2,0,1,1,2,5,3,0,3,5,4,5,4,2,0,0,2,0,2,4,1,3,4,0,3,0,
                1,2,0,1,0,5,3,5,4,4,2,2,4,3,2,5,5,3,1,3,1,5,0,4,2,0,0,4,4,0,2,5,4,5,5,1,4,5,0,1,
                5,5,0,0,4,2,1,5,3,5,0,3,1,3,5,4,1,3,3,3,5,1,2,3,1,4,2,5,4,3,3,0,1,5,3,4,3,1,3,4,
                2,5,2,1,0,5,2,4,3,4,1,3,0,2,1,0,0,2,4,1,5,1,5,4,4,2,0,0,4,3,4,5,3,3,5,4,5,4,1,5,
                2,3,2,3,2,2,1,5,5,3,4,0,1,4,3,0,1,1,2,5,5,4,2,4,0,3,0,2,1,4,4,3,5,3,5,1,0,0,4,5,
                4,0,5,3,1,3,0,3,1,2,0,0,2,1,3,1,1,4,2,2,0,2,4,0,3,1,3,0,1,1,0,5,1,0,1,3,2,5,4,3,
                1,4,4,5,1,4,1,3,1,2,0,3,1,0,1,2,2,5,1,4,3,0,1,0,3,3,1,2,3,3,0,0,5,0,5,2,0,0,4,4,
                0,0,1,3,1,1,4,3,4,4,3,3,2,2,5,1,5,0,2,2,4,4,4,3,1,4,2,2,5,5,4,1,3,5,5,2,2,5,2,3,
                3,5,2,1,3,0,5,5,1,3,3,1,1,3,2,0,3,5,2,0,5,2,4,0,4,5,1,3,1,3,4,0,5,0,5,1,5,1,5,5,
                0,0,5,2,5,4,0,0,2,0,4,4,3,3,2,5,1,5,0,1,3,4,1,0,3,5,3,2,5,4,3,0,3,2,0,0,4,3,2,4,
                2,1,0,5,3,5,2,1,4,0,3,4,1,5,5,3,3,2,1,3,2,1,0,4,5,3,1,4,2,1,5,5,3,0,1,4,2,3,3,5,
                5,2,3,1,1,4,3,5,4,5,0,1,0,0,5,2,0,4,1,1,3,1,5,2,4,2,4,1,4,4,3,3,3,0,2,5,2,5,3,4,
                0,5,5,2,4,4,0,5,0,1,5,5,3,3,0,0,1,2,2,4,4,3,4,5,1,5,3,2,3,1,1,3,3,5,2,1,5,3,2,4,
                5,2,4,4,2,2,2,4,0,0,4,0,3,1,0,2,1,1,5,4,0,3,3,2,2,1,0,0,3,0,1,1,2,5,2,2,0,2,1,1,
                4,3,0,0,3,1,3,2,5,4,3,4,2,5,5,5,3,5,0,4,2,0,4,2,1,0,2,0,5,3,0,4,1,0,5,0,3,2,3,4,
                3,1,4,5,0,5,2,5,0,3,2,2,1,1,3,3,0,3,2,1,5,2,5,1,3,3,2,3,4,1,0,4,3,5,1,5,4,4,4,5,
                1,2,5,2,0,2,3,4,0,5,2,2,4,4,2,1,3,3,0,0,1,4,1,2,2,2,5,1,3,5,1,3,0,2,1,3,3,1,5,4,
                1,2,2,5,0,2,5,0,3,1,0,1,1,4,0,5,4,3,4,2,4,5,3,1,0,1,2,1,0,1,5,3,4,3,4,4,1,4,4,2,
                5,5,2,2,4,5,1,1,2,3,4,0,3,4,2,4,4,4,0,2,1,3,1,2,5,2,1,3,1,2,5,2,1,0,3,4,4,0,2,5,
                0,4,1,1,3,5,3,4,0,1,3,0,2,2,2,5,3,1,5,2,0,2,1,3,0,4,2,0,4,5,5,0,4,3,4,2,3,0,0,1,
                2,1,2,3,5,1,1,0,3,3,5,3,0,2,2,1,4,1,2,4,1,1,5,4,4,2,3,2,3,2,3,4,4,2,3,3,0,0,4,2,
                4,0,4,5,2,3,2,4,3,5,0,3,4,2,2,5,4,2,3,1,0,5,2,4,2,2,5,3,4,1,4,3,4,3,0,4,5,2,1,3,
                0,2,5,4,4,5,2,3,2,3,1,1,1,0,3,1,5,1,3,2,5,3,5,4,1,1,4,3,3,0,5,2,3,4,0,5,5,0,5,5,
        };
        int[] Tab3 ={0,4,2,3,3,5,5,2,1,1,3,5,5,5,1,1,3,5,4,0,0,5,3,1,4,2,5,0,2,4,1,0,4,2,5,2,4,1,2,5,
                2,5,4,1,0,4,4,4,3,4,2,1,1,4,4,3,5,3,4,0,0,4,2,0,1,0,5,0,5,4,0,1,1,0,2,5,1,4,4,3,
                4,1,0,4,1,3,2,2,5,2,2,4,3,5,1,1,1,4,3,1,2,5,1,4,1,3,5,3,2,3,0,5,1,1,0,3,4,5,1,1,
                2,3,3,4,5,0,1,2,1,2,1,1,4,1,2,5,1,0,0,2,3,0,0,0,1,3,3,5,2,5,5,4,3,0,4,1,2,5,0,0,
                3,2,1,1,5,4,0,1,2,5,3,0,4,4,1,5,1,4,0,4,5,0,5,5,2,5,4,3,2,3,0,1,2,5,4,1,3,3,1,4,
                2,1,0,5,3,0,4,2,0,0,1,0,1,3,2,2,2,2,5,2,1,1,2,4,2,1,2,5,2,1,0,5,3,5,0,3,0,1,3,4,
                3,0,2,1,5,2,0,0,1,4,3,1,5,4,3,2,0,5,5,5,5,5,1,1,2,1,0,5,5,5,1,3,3,4,2,1,3,4,3,3,
                1,1,2,2,5,3,5,1,5,4,2,0,0,2,3,2,0,0,0,2,1,3,2,3,2,4,2,1,4,4,5,1,3,3,3,3,4,3,2,0,
                0,1,2,4,2,3,1,3,0,2,0,3,4,4,5,4,3,5,2,0,5,5,2,3,0,0,3,5,4,4,1,2,2,2,5,3,0,0,5,1,
                2,0,1,2,5,5,5,1,0,1,2,4,0,2,2,4,3,4,4,4,4,3,3,2,4,4,5,2,4,0,1,4,1,0,3,2,2,3,0,5,
                4,3,2,5,4,2,2,1,5,0,2,0,4,3,2,2,3,3,5,5,5,2,0,1,3,3,1,0,0,2,4,3,3,2,5,3,0,3,2,3,
                2,0,2,4,1,3,1,2,2,2,3,5,0,1,3,5,2,2,1,0,3,5,4,1,0,5,4,0,4,0,5,4,2,2,3,2,1,0,3,5,
                2,2,4,4,5,2,1,0,5,5,1,2,2,1,5,5,4,5,1,3,4,4,4,5,2,0,3,0,5,4,4,5,1,5,4,0,4,5,0,3,
                2,0,2,3,4,5,1,4,2,4,0,2,2,2,1,5,3,0,4,5,3,0,2,2,0,2,5,5,3,5,4,1,1,0,5,2,5,0,3,4,
                4,5,5,2,4,3,5,0,0,3,2,1,3,5,2,0,5,2,2,5,0,3,0,2,3,3,2,2,3,4,2,1,2,2,1,1,0,1,3,4,
                3,1,0,4,1,2,5,3,5,3,2,3,4,0,1,5,3,2,1,4,0,2,0,3,4,5,5,1,2,1,3,2,3,4,5,0,1,2,5,3,
                2,3,1,2,4,1,1,1,4,1,1,3,2,3,5,2,4,0,1,3,5,0,1,0,2,4,1,0,1,1,3,2,1,3,5,4,4,4,4,5,
                1,2,2,0,3,0,4,0,1,5,2,2,2,1,4,0,5,5,0,0,1,0,2,4,0,0,5,5,5,2,0,5,0,5,1,5,3,4,0,3,
                2,1,4,3,5,3,5,0,0,3,3,0,1,3,0,3,2,0,1,1,0,4,1,4,5,3,4,2,0,1,1,1,2,4,1,5,1,3,2,1,
                2,0,1,3,3,4,1,5,1,2,2,2,1,5,1,1,5,1,3,0,0,0,2,4,2,2,4,3,2,3,3,0,4,3,5,3,1,3,4,2,
                4,4,4,3,3,2,1,0,2,5,1,5,2,2,2,2,3,5,1,4,5,0,3,0,5,4,4,2,0,4,0,2,0,1,1,2,4,1,1,4,
                0,5,2,2,3,2,1,5,1,5,1,3,4,1,5,5,2,1,1,1,2,2,3,5,0,1,4,0,4,5,4,0,4,3,5,5,3,5,5,3,
                3,1,2,1,0,2,5,2,4,5,0,4,0,5,1,5,3,1,3,5,2,5,5,4,4,5,5,0,1,4,5,1,1,1,4,5,3,2,4,0,
                4,0,3,3,0,0,5,0,4,2,5,1,2,0,0,3,1,2,0,0,3,4,2,0,4,5,5,4,3,1,1,4,3,0,1,1,5,2,4,3,
                2,4,5,0,5,3,4,0,2,2,0,5,1,1,4,0,0,5,5,1,0,3,2,0,4,1,1,0,5,5,3,3,1,3,1,0,1,3,0,3,
                2,1,1,3,2,2,1,0,2,5,3,5,0,1,1,2,1,1,2,4,3,0,0,0,2,0,5,0,2,0,2,1,5,5,2,4,2,0,4,4,
                1,2,4,0,5,0,5,2,3,4,3,1,0,5,5,5,3,3,5,1,2,1,2,3,3,2,0,4,4,1,2,5,4,4,3,0,5,2,1,4,
                4,2,3,4,2,2,1,4,3,5,4,3,5,1,5,0,5,5,3,4,4,0,5,2,4,0,2,2,0,5,5,5,3,2,2,3,1,1,3,4,
                1,5,1,2,0,3,2,4,0,4,5,1,4,0,2,2,0,5,4,0,0,1,0,2,0,0,4,0,3,2,0,4,3,0,2,0,3,2,1,0,
                5,3,2,5,2,0,2,3,5,4,5,3,3,1,3,1,4,0,0,4,2,4,0,1,4,5,3,1,5,2,0,1,3,2,5,2,2,5,1,0,
                2,5,3,3,0,0,4,5,1,2,3,3,2,1,1,2,4,4,2,0,4,0,4,5,4,1,3,2,2,0,0,0,0,5,5,4,1,2,3,4,
                0,2,5,0,1,0,4,5,5,2,2,3,0,1,2,3,0,1,4,3,5,0,3,3,3,4,0,2,3,3,1,5,1,1,2,3,4,4,2,4,
                2,3,2,5,4,0,3,0,2,4,2,4,5,3,4,2,2,1,0,3,0,2,1,3,1,1,3,0,0,2,1,4,4,3,0,4,4,2,1,4,
                5,2,4,5,1,2,3,2,5,1,1,0,5,1,5,1,3,4,2,1,3,5,1,4,4,4,2,2,5,0,4,4,4,5,5,2,5,0,4,4,
                1,0,2,2,2,3,4,2,2,2,3,2,2,3,1,1,5,4,3,4,2,0,1,5,3,3,1,0,5,2,5,0,4,3,3,5,2,3,3,4,
                3,5,5,4,2,1,2,4,2,4,4,0,5,1,5,5,5,4,4,3,4,3,5,0,3,3,3,1,0,5,4,1,3,0,1,5,3,2,0,5,
                4,4,1,4,5,4,2,5,5,1,4,5,0,5,1,5,2,2,4,4,2,2,5,2,5,5,1,4,0,1,2,3,2,4,2,2,3,2,5,2,
                4,0,0,3,2,3,0,1,2,0,2,4,2,3,3,0,1,3,5,5,3,3,3,1,3,0,4,3,1,4,1,2,4,3,5,4,3,0,2,2,
                1,5,2,0,0,1,3,4,5,5,0,2,4,4,1,3,0,0,5,5,5,4,5,2,2,2,5,3,1,3,5,4,4,3,4,4,2,5,5,1,
                0,2,2,4,3,5,4,1,4,3,3,2,5,3,4,5,2,3,2,0,3,5,5,4,0,3,1,3,2,0,2,1,0,4,5,3,2,5,1,4,
        };

        int[] Tab1 ={1,2,4,0,3,4,3,0,5,2,3,1,0,2,5,1,2,0,1,5,4,5,1,2,5,2,4,3,5,3,5,4,5,3,1,4,0,5,2,1,
                3,0,3,2,1,3,5,3,2,3,2,3,4,2,4,5,1,1,4,2,5,2,3,1,1,4,2,0,2,5,0,3,0,1,5,3,2,0,5,1,
                3,3,4,0,5,0,1,4,0,5,4,0,3,1,2,3,4,0,5,1,2,4,2,5,3,1,5,4,2,5,5,3,2,2,3,1,1,1,1,2,
                4,1,2,3,0,4,0,0,2,5,2,2,3,2,3,3,0,4,5,5,1,3,2,4,3,2,3,4,5,3,4,5,5,3,4,5,2,3,5,4,
                0,5,3,5,2,2,3,0,0,1,5,5,2,5,1,4,4,1,3,0,2,0,4,5,0,1,5,4,2,0,1,0,1,1,1,1,5,1,5,3,
                0,5,2,2,0,1,0,0,5,5,1,5,4,3,2,5,3,2,3,3,3,5,2,4,0,1,2,0,3,4,0,1,0,4,3,1,5,0,4,1,
                0,3,1,0,5,1,5,1,0,5,1,1,0,5,2,2,0,4,0,1,1,5,0,1,4,1,2,4,4,4,4,4,1,5,1,3,2,4,4,0,
                4,2,5,4,0,4,5,1,5,0,4,0,5,2,4,4,5,0,3,5,4,5,2,2,1,5,4,5,1,0,2,4,4,1,5,3,3,1,5,3,
                4,3,4,0,2,2,5,3,4,1,4,3,0,2,3,4,0,1,2,4,3,3,1,4,0,5,2,0,0,3,4,3,1,4,2,1,0,4,5,5,
                5,5,3,0,5,2,2,3,4,2,5,4,3,1,1,4,5,2,4,0,5,0,3,4,4,2,0,1,2,0,5,3,3,5,1,1,2,0,1,3,
                5,3,4,1,3,2,1,5,5,2,1,3,2,1,2,1,3,5,1,0,2,0,0,4,4,3,2,1,0,0,3,1,5,0,5,5,2,5,4,2,
                4,0,1,2,2,4,3,0,3,5,0,4,0,5,0,3,1,5,3,2,5,2,5,3,3,2,4,2,1,4,3,4,1,1,0,5,0,4,1,1,
                2,3,0,1,3,0,0,2,2,0,4,4,2,1,2,4,3,5,2,0,1,4,3,2,0,3,3,2,4,0,1,3,0,5,5,1,1,1,1,0,
                0,4,1,3,2,0,2,5,1,3,1,3,3,0,2,5,0,4,1,1,3,0,4,1,5,0,4,4,5,5,2,5,4,1,3,5,5,4,2,1,
                1,3,1,5,0,2,5,2,0,1,0,0,2,2,0,2,5,0,2,5,5,1,3,2,0,4,0,4,2,5,3,2,1,5,1,5,3,1,1,4,
                1,2,2,5,4,5,1,5,1,5,3,0,5,3,2,3,2,0,0,0,5,1,3,1,0,4,5,5,4,5,0,2,2,3,3,4,2,0,5,4,
                4,2,1,4,0,5,5,1,3,3,5,1,0,1,5,1,0,1,3,2,2,4,5,5,1,1,5,1,3,1,5,1,0,4,3,3,2,0,3,3,
                5,5,3,5,3,5,0,1,5,3,0,0,4,2,1,2,1,1,1,3,5,3,3,1,2,3,1,5,0,5,2,4,4,2,1,5,4,4,1,2,
                5,1,2,4,4,3,0,3,2,3,3,0,1,4,0,1,5,1,1,3,4,2,2,1,0,3,2,4,2,1,0,5,4,4,1,0,5,3,1,1,
                0,3,3,1,4,3,0,4,1,5,1,5,1,0,2,5,3,5,0,2,3,2,5,3,5,4,4,1,1,1,3,2,3,0,4,2,3,5,2,4,
                3,1,5,5,4,5,1,3,1,0,3,2,2,2,1,5,4,1,0,2,0,2,2,1,4,0,0,1,5,2,5,2,2,4,0,5,1,2,4,4,
                2,4,1,2,0,5,1,4,5,1,4,3,3,5,5,2,3,4,5,3,0,2,5,5,4,0,2,0,1,4,0,1,1,0,4,5,4,0,4,5,
                2,4,5,3,5,2,1,0,5,2,4,4,3,2,1,4,5,1,5,5,5,5,1,3,0,1,3,1,3,0,3,5,5,0,0,3,2,3,0,2,
                5,0,0,3,5,5,4,5,1,0,4,0,2,1,2,1,1,5,5,3,3,1,4,1,0,5,0,5,0,5,3,3,2,3,2,5,3,3,5,2,
                4,1,0,1,3,3,2,5,3,4,5,1,1,5,2,2,1,0,0,5,1,4,5,5,5,0,4,4,4,3,2,4,3,5,1,4,3,4,3,4,
                1,0,1,5,0,1,2,4,3,3,0,4,0,5,4,4,2,3,3,0,4,2,0,4,0,5,0,5,3,4,1,0,1,2,5,1,3,0,2,0,
                2,4,0,3,4,1,5,1,4,2,4,1,4,4,4,5,5,2,4,5,3,4,5,0,2,3,5,3,3,0,1,2,4,0,0,4,5,2,1,5,
                5,2,1,3,1,1,0,0,0,1,4,3,2,0,5,0,2,5,2,1,5,2,2,3,4,5,4,3,1,3,1,0,1,1,5,2,4,4,5,4,
                2,3,4,1,1,1,3,3,1,4,1,5,1,2,5,5,2,5,1,0,2,4,4,0,2,5,5,2,0,0,0,2,3,2,4,4,4,0,3,3,
                0,3,5,1,2,1,1,1,1,5,5,0,1,4,2,2,1,5,0,3,1,5,3,4,2,0,4,3,2,3,2,2,1,2,2,3,3,4,1,2,
                3,2,2,1,1,3,3,1,0,4,2,5,0,0,1,0,2,5,1,4,1,3,1,1,4,5,2,1,0,3,5,5,2,1,3,2,4,4,2,4,
                5,2,1,1,0,1,1,5,4,3,1,2,1,2,5,4,5,3,3,0,3,5,4,3,4,1,2,3,0,2,0,3,2,3,5,3,0,1,5,5,
                5,0,0,3,2,4,4,5,4,1,3,0,5,0,1,5,4,4,0,5,0,1,2,3,3,4,5,4,1,1,0,5,5,4,1,1,0,1,5,4,
                3,1,5,0,2,0,2,2,4,2,1,4,0,4,4,4,3,0,4,0,0,3,5,5,5,0,4,0,2,5,5,5,2,5,0,2,3,4,4,1,
                3,5,3,4,5,3,1,5,2,0,3,0,0,1,0,2,5,4,3,2,4,2,3,1,0,0,1,1,1,1,2,5,5,1,5,5,2,0,4,0,
                2,1,0,2,1,2,0,4,3,2,4,3,1,1,5,0,1,3,0,5,4,0,2,3,4,1,1,5,1,0,3,0,3,0,4,0,2,1,2,0,
                3,1,0,3,0,5,1,4,3,0,3,5,5,3,1,4,5,1,1,0,1,2,5,2,5,5,2,5,2,2,3,5,0,0,4,4,0,1,3,4,
                4,3,4,2,0,4,4,2,0,3,3,0,1,3,5,5,4,2,4,5,5,4,4,0,4,1,3,4,2,3,5,5,3,5,2,4,5,2,4,4,
                5,4,5,1,4,1,2,4,4,1,2,5,0,5,5,0,1,2,3,3,4,5,2,2,0,0,2,5,2,2,0,0,0,1,0,5,1,4,4,2,
                4,4,2,3,3,0,4,0,3,3,5,3,3,3,1,5,5,2,5,5,3,5,0,1,1,4,1,4,1,1,4,4,0,1,4,1,0,4,0,1,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=71;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=45;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=32;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=22;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level5L(){
        int[] Tab0 = {2,0,4,3,2,4,1,4,4,4,3,5,5,0,2,3,4,3,1,4,3,3,4,4,0,5,5,4,3,4,3,1,3,0,4,5,0,1,3,3,
                2,4,3,0,4,1,4,1,0,0,3,4,3,3,3,4,2,2,5,5,1,5,3,3,0,1,2,0,5,4,1,2,3,4,2,3,1,5,2,3,
                1,2,0,2,2,0,0,0,3,1,1,0,2,5,5,1,4,0,4,4,3,5,3,2,1,5,5,0,1,5,4,1,5,3,5,4,0,4,2,1,
                4,3,3,2,1,5,3,2,4,4,1,3,3,3,1,3,1,1,3,5,0,5,2,5,3,2,2,4,3,2,4,5,4,4,2,1,3,2,3,5,
                3,1,5,5,5,1,3,3,1,2,0,5,5,2,1,0,4,0,3,3,1,2,0,1,0,4,5,1,0,1,3,2,1,1,0,2,2,1,0,5,
                4,2,1,2,0,3,4,5,3,5,1,1,4,0,4,4,5,1,3,0,1,5,3,0,4,3,0,0,2,0,5,1,3,3,2,0,1,5,4,2,
                0,0,4,4,0,0,3,0,0,5,5,2,1,2,5,2,5,4,5,1,5,3,3,3,5,3,1,2,0,3,3,2,5,3,2,1,3,5,2,0,
                5,4,0,4,2,5,1,5,2,2,5,2,2,3,0,3,3,2,3,2,1,1,1,2,3,1,3,2,3,3,1,1,5,5,0,0,4,3,1,0,
                3,0,4,0,5,1,2,2,0,3,0,2,4,2,1,3,5,1,5,3,4,3,1,2,2,4,1,0,5,0,5,3,1,5,4,1,1,2,2,5,
                3,0,1,3,1,3,4,3,2,4,4,2,0,5,1,0,1,1,0,3,1,3,2,2,2,1,5,1,2,3,3,1,3,3,5,3,4,1,4,3,
                1,4,5,0,5,2,5,3,2,2,4,5,4,4,2,2,1,3,3,1,0,4,4,0,5,3,5,4,5,5,1,4,4,2,2,1,3,5,3,3,
                2,3,5,4,2,5,2,0,1,2,4,0,5,4,5,3,0,2,2,2,0,0,4,0,3,0,1,3,3,4,2,5,4,5,5,1,3,1,5,5,
                3,0,1,0,2,4,0,3,3,3,3,4,3,2,4,4,5,2,3,1,1,2,3,4,3,0,3,4,1,5,1,4,0,1,2,2,1,2,2,4,
                2,2,1,5,0,4,1,3,1,2,2,3,4,2,2,1,0,4,5,5,3,3,0,5,5,3,5,1,1,1,1,3,4,0,3,4,1,5,0,4,
                0,4,0,3,3,3,4,0,4,3,5,1,0,4,1,0,3,2,1,2,1,1,0,1,3,2,2,1,1,2,0,3,0,3,0,4,2,4,2,4,
                0,1,2,4,2,1,5,0,5,1,1,1,5,4,4,4,0,0,0,2,4,4,5,2,2,5,3,0,2,3,2,0,3,5,1,2,0,5,4,3,
                4,4,5,4,4,5,1,3,3,5,4,3,3,3,1,0,5,3,3,1,0,1,4,2,0,5,2,0,0,3,1,1,1,1,5,3,3,3,2,3,
                3,2,3,5,3,2,1,3,2,2,2,0,0,1,1,1,4,5,1,0,0,2,5,0,1,2,3,3,0,0,3,2,4,1,4,1,4,4,0,1,
                3,5,3,1,0,0,0,0,2,3,1,0,4,1,2,3,1,1,2,2,1,2,3,5,2,2,0,2,3,0,4,4,2,4,3,2,4,5,1,4,
                4,3,0,5,4,4,2,3,4,3,1,0,4,1,1,5,0,4,0,4,5,1,0,3,4,4,0,1,4,1,4,3,1,4,4,5,3,0,4,3,
                4,2,4,2,5,0,3,1,5,5,3,1,4,3,1,1,0,5,2,3,2,1,4,2,1,0,4,2,3,2,1,5,4,2,1,0,5,4,5,1,
                4,5,2,5,2,3,1,3,1,5,2,0,2,3,2,5,5,1,5,0,3,0,5,0,4,5,1,4,3,1,0,1,2,2,2,0,5,2,2,3,
                0,4,2,4,3,4,3,3,5,5,1,0,3,0,5,5,3,1,1,3,1,3,1,5,1,0,1,5,1,3,3,0,4,5,0,4,4,5,5,3,
                0,4,2,2,2,1,4,2,5,3,3,1,0,2,2,4,5,5,0,4,0,5,5,5,0,2,3,4,1,4,4,1,0,0,1,2,4,1,0,4,
                3,3,5,2,3,1,4,0,1,3,3,3,2,2,2,4,2,2,4,4,3,2,1,1,3,5,5,1,2,0,5,4,0,5,3,3,0,1,0,1,
                4,3,4,1,4,1,3,4,0,3,1,1,4,5,1,1,4,0,0,1,2,0,0,4,5,5,0,0,0,4,4,2,3,1,0,4,3,0,1,4,
                1,5,2,5,5,3,3,3,5,5,2,4,4,2,4,3,1,3,5,1,3,0,4,0,2,3,4,5,1,1,0,0,3,1,5,2,2,1,1,5,
                0,2,5,3,4,0,3,0,3,3,0,4,5,2,3,0,2,5,4,4,1,4,0,3,5,1,3,5,2,0,1,1,0,2,0,0,1,3,2,3,
                2,0,5,4,4,0,4,3,5,3,2,1,4,3,2,2,5,3,3,2,3,4,0,5,0,5,2,5,5,2,1,1,5,2,1,1,1,5,1,5,
                4,2,1,1,4,5,1,0,2,1,5,5,1,0,0,3,4,5,1,5,1,0,4,5,5,5,2,5,0,4,5,2,1,2,5,4,4,2,3,3,
                4,3,3,3,4,5,4,5,3,0,5,4,4,1,1,2,1,1,4,5,0,3,4,1,1,3,4,4,0,1,2,1,2,3,2,0,4,2,5,5,
                3,4,0,4,4,0,0,2,2,0,4,5,5,5,0,2,3,3,5,1,4,5,1,2,5,3,3,0,3,4,3,3,1,5,3,3,4,1,1,4,
                0,3,1,3,5,5,1,3,2,0,5,1,0,4,3,3,2,0,2,2,3,2,5,4,2,5,5,0,1,2,4,1,2,4,3,0,3,2,3,4,
                1,1,4,5,2,3,3,0,4,4,5,4,4,0,1,0,3,5,2,5,1,4,3,3,2,5,2,4,5,3,0,0,5,4,2,4,4,4,5,1,
                2,3,4,2,0,3,2,5,4,3,1,2,0,2,0,1,5,2,5,5,0,5,2,2,5,3,5,0,0,5,1,1,1,1,0,0,3,0,1,0,
                4,5,2,2,0,0,0,5,5,5,3,2,0,5,3,5,4,4,2,5,2,3,4,5,2,0,2,3,4,4,0,2,4,5,1,3,3,1,1,4,
                5,3,4,1,5,3,3,5,1,1,0,3,0,1,1,0,2,5,2,5,3,0,5,4,5,1,1,4,5,4,4,3,0,2,0,0,3,0,1,3,
                1,3,1,0,2,3,1,1,2,0,2,0,5,5,5,0,5,0,2,5,4,4,4,1,5,0,2,5,3,5,3,2,1,0,4,1,0,2,0,2,
                3,3,3,5,2,3,1,4,4,2,1,4,5,2,1,3,0,5,2,5,5,2,0,0,1,2,5,0,0,1,2,1,5,4,4,0,3,1,5,0,
                5,5,0,3,5,1,3,5,3,3,4,5,2,2,5,4,4,3,2,5,5,5,5,5,3,5,3,3,0,2,0,3,4,1,3,2,2,3,5,4};
        int[] Tab3 ={1,3,2,2,0,1,1,5,4,5,0,4,5,3,0,5,1,4,2,1,1,5,2,5,2,2,2,0,3,3,2,5,4,1,3,2,3,0,1,0,
                0,5,1,5,0,3,3,1,0,5,5,3,2,1,1,4,1,5,0,1,0,1,4,4,3,1,5,0,1,4,0,4,5,3,0,2,2,3,3,5,
                0,1,4,1,1,0,2,5,0,0,3,1,2,0,5,1,0,4,3,4,0,1,2,1,0,2,5,3,2,2,1,5,1,5,1,5,4,4,3,5,
                5,5,1,3,3,1,0,5,5,5,5,5,2,4,0,3,4,3,3,2,2,5,4,5,1,2,1,0,3,2,3,3,3,5,3,1,4,3,0,0,
                1,3,0,1,0,0,2,0,3,4,0,1,0,4,0,3,3,4,1,5,1,3,5,5,0,4,3,4,0,3,2,5,1,0,1,4,5,5,3,0,
                5,1,1,3,3,4,2,4,0,1,3,0,1,4,1,4,0,4,2,4,5,5,1,2,3,3,0,1,2,2,5,0,2,4,4,2,2,4,5,0,
                5,1,1,2,2,4,1,3,4,0,0,1,0,3,2,5,3,1,3,1,5,4,5,0,3,0,0,1,5,0,2,1,5,2,3,2,0,1,2,2,
                0,0,4,0,0,2,4,2,1,0,0,0,2,3,1,2,5,2,5,5,0,4,3,2,4,3,5,5,1,4,4,3,0,4,2,1,1,4,4,1,
                4,0,5,3,5,2,4,2,4,3,3,5,1,3,0,4,2,0,1,0,3,1,5,4,4,5,0,5,3,1,3,3,1,3,4,3,5,4,3,4,
                0,5,4,2,4,5,2,3,3,0,1,2,0,5,4,3,0,5,2,0,5,5,5,2,2,3,5,1,5,5,1,1,0,2,5,1,5,4,0,2,
                3,3,5,3,2,1,3,1,0,2,4,5,0,5,3,4,5,3,4,2,4,4,5,1,3,2,5,2,5,4,5,1,5,3,5,0,0,1,3,4,
                1,4,2,1,4,2,5,3,3,0,3,1,1,2,0,4,5,5,0,2,1,3,2,5,3,3,2,3,2,0,2,0,1,2,4,4,4,3,2,1,
                1,4,3,3,0,2,3,4,2,2,4,2,3,5,4,4,3,3,3,4,3,5,5,3,0,4,2,5,3,0,2,3,1,1,3,0,3,4,3,5,
                4,3,0,0,1,4,0,3,2,1,2,5,2,3,4,0,0,0,2,1,3,2,4,3,3,5,5,3,2,3,3,0,4,4,1,4,4,0,0,1,
                1,1,0,5,1,0,2,5,2,1,2,5,0,2,3,5,3,0,4,1,4,2,3,1,5,0,3,5,1,3,4,4,2,5,4,5,1,5,4,3,
                5,5,2,2,1,1,2,1,0,1,4,0,2,5,0,3,1,5,3,5,3,1,1,2,1,2,2,2,4,5,1,5,1,0,2,4,3,3,4,5,
                3,2,3,0,1,1,2,1,4,4,0,5,2,2,5,4,4,1,5,1,4,2,4,5,5,1,1,4,0,2,4,3,2,0,4,4,1,1,2,0,
                3,3,2,0,0,2,1,1,0,4,1,1,3,1,2,1,5,4,1,4,1,0,1,2,1,3,5,4,4,1,0,2,4,4,2,2,4,3,1,1,
                0,3,2,5,1,1,4,4,3,5,3,4,4,5,4,2,5,5,5,5,2,2,1,2,5,2,4,2,3,3,4,3,0,0,2,1,2,0,5,4,
                0,1,2,0,0,1,4,3,0,2,0,2,3,3,3,4,1,1,1,3,1,1,0,4,5,1,5,2,1,5,4,5,5,0,0,2,4,2,2,3,
                2,2,0,3,5,0,2,1,3,1,3,4,5,3,4,4,3,5,5,5,3,3,2,5,0,0,0,1,0,5,1,0,0,0,3,3,1,2,1,4,
                3,2,5,2,5,5,5,0,2,2,0,1,0,1,4,2,3,3,5,5,5,0,3,3,1,3,5,1,5,2,2,3,5,4,3,2,3,2,4,4,
                5,0,4,3,3,3,4,5,1,4,3,1,5,3,5,2,1,5,4,4,2,3,1,5,2,2,3,1,3,1,0,5,2,0,1,1,1,5,3,4,
                2,5,4,4,4,3,3,5,2,4,4,5,5,5,5,3,5,5,3,3,5,3,4,3,2,2,1,4,1,1,5,4,4,0,4,2,1,3,4,0,
                5,4,4,5,2,1,3,5,0,5,5,5,1,2,1,2,1,3,0,5,4,0,3,2,2,4,1,2,5,0,2,3,3,5,2,3,4,4,1,3,
                5,4,1,2,3,3,2,5,1,5,4,1,3,2,3,1,3,1,1,4,0,5,2,5,2,0,1,5,4,4,5,3,1,0,3,2,5,5,2,4,
                1,0,2,3,3,5,2,4,3,4,4,1,1,0,3,2,4,2,1,2,0,0,4,5,0,5,5,0,5,4,2,5,3,2,4,1,0,2,5,1,
                2,1,4,3,3,1,0,2,4,3,2,5,0,2,4,4,0,0,1,4,2,1,3,2,4,5,3,4,2,0,5,3,4,4,5,4,0,3,1,5,
                4,0,3,0,1,3,1,2,4,5,4,3,0,3,5,3,5,2,1,5,5,2,5,0,0,3,0,3,2,3,2,3,2,0,0,0,5,0,4,5,
                2,2,0,1,4,5,5,0,0,4,3,3,5,1,4,0,1,4,4,2,0,2,4,3,1,4,2,1,1,2,5,1,0,4,2,0,5,5,3,2,
                1,3,3,1,4,3,5,2,5,0,5,1,5,3,4,2,4,4,2,1,2,0,3,3,0,3,2,5,3,1,2,5,4,1,1,4,3,2,1,2,
                0,4,2,3,2,2,0,2,3,2,0,5,4,0,4,5,2,2,5,0,0,0,2,4,4,0,3,3,3,3,5,1,1,4,3,5,3,4,2,5,
                0,0,2,4,2,1,3,1,2,5,3,2,3,5,0,0,2,4,5,4,2,3,2,4,1,5,0,5,2,1,1,5,1,3,4,0,2,1,2,3,
                3,0,1,1,3,5,5,2,2,4,3,2,2,3,5,3,4,2,1,5,4,2,2,1,1,5,0,5,3,5,0,1,1,5,4,3,2,3,4,0,
                0,0,1,4,4,4,4,4,0,0,5,5,0,4,0,1,5,2,0,1,0,2,4,1,0,4,4,1,1,1,0,1,5,3,5,2,5,2,0,0,
                3,5,0,0,2,3,3,4,1,3,2,5,1,2,3,1,1,0,5,5,1,0,0,3,5,5,5,3,2,4,3,1,2,3,3,5,4,5,2,2,
                5,0,4,4,4,4,4,4,0,0,3,0,0,3,1,2,2,4,1,2,1,1,4,5,4,4,2,2,1,2,5,5,4,3,0,2,0,3,4,0,
                3,0,4,2,4,2,4,4,5,5,1,4,2,5,3,2,1,5,1,3,2,4,1,2,3,1,1,3,2,2,0,5,2,1,2,4,4,0,2,2,
                3,4,4,2,0,5,5,4,3,0,5,5,0,5,2,2,3,4,2,5,4,1,5,0,3,2,5,5,5,4,0,2,2,5,1,3,2,5,4,1,
                5,5,4,0,5,3,3,5,1,2,1,1,1,0,4,3,4,0,1,1,3,4,1,3,2,2,0,4,0,4,0,2,3,1,5,5,1,4,3,2,
        };
        int[] Tab2 ={3,3,2,1,0,2,3,1,4,5,0,4,2,3,3,0,2,2,3,3,2,3,1,0,0,2,0,5,1,0,2,1,5,3,0,3,0,4,2,1,
                1,1,1,1,0,2,1,0,2,4,3,3,3,0,2,4,0,3,3,2,3,4,1,3,4,5,3,2,5,0,5,4,2,3,3,1,0,2,1,3,
                5,0,4,0,1,2,0,3,1,4,3,1,2,1,3,5,5,1,2,1,1,1,3,3,2,0,2,3,5,0,3,0,5,4,4,0,1,3,2,2,
                3,1,1,3,0,2,2,2,2,2,4,3,3,2,2,1,4,0,1,0,2,5,3,3,0,3,0,1,4,2,2,4,2,2,0,2,3,1,4,0,
                1,1,4,1,2,0,0,0,0,1,3,3,3,2,1,4,5,4,1,5,4,3,3,5,1,3,4,1,3,5,1,3,0,4,4,3,1,4,1,2,
                3,3,3,0,5,4,4,1,3,3,2,0,2,3,4,5,2,4,2,1,2,2,4,4,2,2,5,3,2,0,3,1,2,0,0,3,5,2,4,3,
                3,5,4,4,3,1,3,3,3,3,5,2,4,1,5,1,0,0,4,5,0,5,2,0,4,3,1,4,1,3,2,2,2,5,3,1,1,2,1,0,
                1,0,4,0,3,3,0,4,2,2,4,2,2,1,1,0,3,5,2,3,4,1,3,0,5,5,0,5,3,5,1,0,2,1,4,2,3,3,3,4,
                0,5,4,3,2,3,0,5,3,0,4,2,1,5,4,1,5,4,2,4,0,3,3,3,2,4,4,3,0,2,2,4,4,5,0,5,3,2,5,5,
                0,5,0,2,0,5,0,1,3,2,0,1,0,5,1,5,2,4,2,3,4,5,5,4,4,0,5,2,0,5,1,3,5,3,0,5,4,0,5,4,
                2,3,1,0,2,4,0,3,0,3,3,4,3,1,5,0,0,3,4,2,3,4,5,1,3,2,4,0,0,4,4,5,4,3,2,0,1,5,3,4,
                0,0,2,1,0,3,2,3,5,0,2,5,5,4,5,1,5,1,3,2,5,2,3,4,3,5,3,4,1,3,4,2,5,1,2,5,3,2,0,2,
                2,2,2,1,3,4,2,2,5,0,1,5,5,0,4,5,3,5,3,4,5,1,2,1,5,1,0,1,2,5,4,2,5,1,5,0,5,5,0,1,
                2,5,5,0,2,4,1,1,4,2,1,0,4,2,1,5,4,2,2,3,4,5,1,4,2,5,3,3,4,2,3,2,1,4,2,2,2,3,5,1,
                4,2,4,1,2,3,0,5,0,2,5,3,0,4,1,1,5,4,3,4,1,2,2,1,2,0,0,1,4,1,0,1,5,0,3,0,4,2,5,0,
                4,3,0,0,2,5,3,1,1,4,0,4,5,3,3,1,1,1,0,3,2,2,0,1,2,2,4,3,5,4,3,2,5,0,2,1,1,2,1,0,
                4,2,3,4,0,5,5,2,4,0,4,5,4,5,1,5,5,3,2,1,3,1,1,4,0,0,4,3,2,2,5,4,0,2,5,4,5,4,1,3,
                5,3,3,2,3,1,1,2,4,1,4,3,5,2,5,2,5,1,1,4,2,1,3,1,4,5,2,5,3,0,0,3,5,1,3,4,0,2,5,3,
                4,4,1,3,4,3,5,0,5,1,3,2,0,4,0,1,0,3,5,3,0,2,1,4,0,2,2,5,2,5,0,0,4,0,2,5,3,3,4,3,
                1,3,1,2,4,0,4,4,0,2,1,1,0,3,2,2,1,1,3,2,2,4,0,3,3,4,5,2,1,3,0,1,3,4,2,5,4,4,3,3,
                2,5,1,0,4,0,0,3,4,2,2,4,4,2,0,1,5,2,3,1,4,4,3,1,5,2,5,1,1,1,1,0,3,0,4,3,2,2,3,2,
                5,1,5,3,0,4,5,0,1,1,5,2,0,5,5,5,2,0,2,4,1,2,0,3,5,4,3,3,2,0,1,5,3,0,3,0,1,5,3,4,
                2,3,4,0,4,5,0,3,1,5,4,1,0,5,1,5,4,2,4,4,0,2,0,2,4,1,5,4,2,1,1,3,4,3,3,5,3,3,4,2,
                0,1,1,5,2,0,0,3,3,4,4,4,0,1,3,5,4,5,5,5,4,5,4,3,4,5,3,4,2,0,3,5,5,1,3,2,0,1,5,5,
                4,3,4,3,1,4,0,0,1,0,4,5,0,3,0,2,5,3,3,2,0,5,0,0,4,1,4,3,3,0,0,1,2,2,1,0,1,2,4,3,
                0,1,1,3,0,1,1,0,4,5,1,5,5,5,5,5,1,1,3,5,2,2,3,3,1,3,2,5,0,1,2,3,5,3,5,2,3,0,3,3,
                5,3,1,0,3,3,4,5,2,2,0,4,1,2,2,4,1,4,5,5,5,5,3,0,0,0,5,4,2,2,4,1,4,5,5,5,1,2,4,0,
                1,2,0,0,1,5,1,5,2,5,5,1,5,5,4,2,2,0,2,4,5,3,1,4,4,1,0,0,1,3,2,3,1,2,5,4,3,5,3,0,
                0,4,5,5,2,5,3,2,3,5,5,3,1,0,0,0,5,3,0,4,3,2,0,0,4,0,4,1,1,1,4,3,4,2,0,3,1,1,5,2,
                5,1,0,1,1,4,1,4,5,3,0,0,3,2,1,1,3,4,0,3,5,5,2,3,3,2,1,2,1,5,4,4,3,0,2,4,3,2,2,2,
                2,2,1,5,0,2,1,5,4,4,3,3,2,0,0,1,1,2,2,1,4,3,4,5,3,2,0,5,4,1,2,0,4,3,0,1,5,5,4,5,
                3,4,4,2,0,4,2,1,1,1,0,4,5,2,1,0,1,5,0,3,1,5,1,5,5,2,0,0,3,1,5,5,2,5,2,2,3,4,5,1,
                0,2,2,4,3,0,2,5,4,1,4,4,2,1,2,2,1,2,2,1,0,1,2,1,3,5,0,1,0,5,3,1,3,5,3,3,1,5,5,2,
                0,4,5,1,4,5,2,3,1,1,0,3,1,3,1,4,1,1,1,4,0,4,4,4,4,5,2,0,0,2,2,0,5,4,3,1,0,4,0,2,
                2,2,1,1,1,0,2,2,1,5,1,4,3,0,4,5,5,0,3,0,0,5,5,0,3,4,3,0,0,1,3,0,1,1,4,3,2,1,1,5,
                5,3,5,3,4,0,4,2,4,2,3,3,5,1,2,0,5,4,2,0,1,5,1,1,3,1,1,3,1,1,0,0,5,3,1,3,3,5,0,3,
                2,2,3,4,2,0,4,0,5,3,0,4,4,4,5,1,0,4,1,1,3,3,3,4,1,4,5,3,5,2,1,1,0,2,2,1,2,5,1,1,
                5,1,2,3,3,1,3,5,4,4,2,0,2,1,5,1,1,0,3,0,3,4,1,0,5,5,3,0,2,0,1,3,4,3,1,5,1,5,5,0,
                1,1,1,4,5,3,2,5,0,2,3,5,3,0,2,4,5,3,1,4,1,2,4,4,4,1,4,4,3,3,5,2,5,3,4,1,5,0,4,3,
                2,5,0,0,0,5,3,1,1,4,1,2,1,4,0,5,1,1,2,1,2,2,0,1,0,0,5,1,1,4,2,5,4,5,2,3,5,5,4,1,
        };
        int[] Tab1 ={3,0,5,2,0,3,4,5,4,5,5,0,3,3,3,3,1,2,4,2,4,2,5,0,4,4,1,5,1,3,0,3,2,1,1,3,1,5,4,1,
                2,1,1,4,5,0,1,0,1,5,4,0,4,3,1,2,0,0,4,0,5,4,3,3,4,3,1,1,3,4,4,4,1,4,5,2,0,2,5,0,
                3,2,0,5,4,1,2,1,2,4,5,2,3,0,1,2,4,4,4,3,5,4,1,1,4,1,2,4,0,2,0,5,0,1,0,4,4,3,2,2,
                2,2,4,2,5,2,1,5,0,5,3,5,2,2,3,3,4,4,0,5,3,3,4,2,1,3,1,1,0,0,1,5,5,1,3,0,4,4,3,1,
                3,4,5,1,4,5,2,1,5,1,1,1,3,2,5,5,3,3,5,2,0,4,2,2,2,4,2,4,0,3,2,2,3,4,5,0,3,1,3,3,
                2,0,2,0,2,0,0,2,5,3,1,0,0,4,0,0,2,2,0,0,3,2,0,5,3,4,1,2,5,5,5,1,2,5,5,1,4,3,2,5,
                4,5,2,1,2,3,5,0,2,5,2,2,5,4,0,3,5,5,5,5,2,1,1,4,2,1,3,0,2,4,0,1,5,1,3,3,4,3,5,5,
                0,4,0,3,4,0,3,1,2,4,5,5,4,5,5,1,0,4,1,5,0,3,0,3,4,3,5,3,2,5,5,3,2,5,2,4,5,2,3,2,
                3,3,5,2,1,4,5,5,5,2,0,2,1,0,0,4,1,0,5,5,4,3,2,3,1,2,1,1,2,3,3,2,1,1,1,5,0,3,0,5,
                5,1,1,2,5,3,1,4,5,4,4,3,2,2,4,2,0,5,4,2,4,2,1,5,0,0,1,3,4,2,1,1,3,5,3,1,1,3,4,2,
                2,5,3,0,5,2,4,0,4,4,0,5,4,2,3,4,0,2,4,4,3,3,0,3,0,3,4,5,3,4,3,5,0,5,5,4,3,1,1,2,
                3,0,0,4,2,0,0,4,0,4,2,0,0,4,5,2,3,1,2,5,2,2,2,1,0,3,2,5,5,0,1,1,0,5,5,4,1,1,3,4,
                5,2,5,3,3,4,2,0,5,1,1,3,1,5,4,1,2,1,3,1,5,3,0,0,2,3,5,4,2,3,3,3,1,1,2,5,4,0,1,5,
                4,5,3,4,4,3,4,2,3,3,3,4,2,2,1,3,0,0,5,4,3,3,2,3,4,4,5,1,2,0,0,1,3,5,2,4,3,1,4,5,
                4,3,3,5,2,2,3,3,4,2,5,0,4,4,4,3,3,4,2,2,1,2,0,5,2,3,0,3,4,3,5,4,2,5,4,3,4,4,4,3,
                4,0,5,4,3,1,1,5,1,2,5,5,2,4,4,4,2,1,0,3,1,2,0,1,2,0,5,2,5,2,1,5,3,4,3,4,0,5,5,0,
                3,2,0,4,3,1,0,3,4,0,2,4,0,5,3,0,3,5,3,4,3,0,5,1,0,4,0,5,5,2,4,1,1,5,3,2,0,2,0,0,
                3,4,4,1,3,3,2,2,4,0,1,3,2,1,2,4,4,0,3,3,2,5,1,2,4,1,5,2,5,5,2,2,3,1,1,3,5,5,0,3,
                0,0,2,3,4,3,0,0,2,2,2,4,4,3,0,2,4,4,0,0,2,2,0,0,1,0,1,2,5,2,0,3,0,1,4,5,1,5,2,3,
                3,4,2,1,2,3,0,2,1,4,2,0,5,0,2,4,4,0,0,5,5,4,4,3,3,0,0,2,2,2,5,3,2,0,1,2,0,0,2,3,
                4,3,1,1,2,3,1,3,1,1,2,3,0,2,5,0,4,5,4,3,2,0,3,4,3,5,1,3,3,1,2,5,5,4,0,5,5,0,4,2,
                0,2,3,1,0,5,4,1,4,3,2,0,3,2,5,0,2,3,5,1,5,2,4,1,4,3,0,0,3,0,5,3,5,3,5,1,2,3,5,4,
                2,5,3,5,1,5,4,2,2,0,5,4,5,4,5,4,4,2,5,4,0,1,4,1,1,3,4,5,0,4,4,0,0,3,0,4,4,4,2,3,
                5,3,4,2,1,2,4,0,0,1,4,3,1,2,4,3,1,2,0,1,1,1,4,0,5,3,3,0,3,4,5,1,2,2,4,2,0,2,4,2,
                5,2,3,2,0,2,4,5,4,5,1,0,3,4,2,2,1,4,4,0,0,1,2,5,3,3,5,4,4,5,1,1,4,0,0,2,0,0,1,4,
                5,1,1,3,2,2,4,5,0,4,2,5,2,4,0,1,5,0,0,2,3,5,5,5,4,1,2,0,5,3,3,5,0,2,0,5,4,2,5,4,
                5,3,3,1,5,3,5,4,0,5,0,1,1,5,5,5,2,5,3,2,2,0,0,1,3,4,1,0,1,2,3,2,2,1,1,0,4,4,5,3,
                3,0,1,3,4,1,4,0,4,4,2,0,2,5,1,0,5,2,0,1,5,1,5,5,1,4,1,0,5,5,5,4,5,3,3,4,4,5,4,3,
                3,4,2,2,1,5,1,5,1,4,2,1,0,0,2,1,3,2,2,2,4,2,4,0,1,3,5,1,1,4,5,0,1,1,5,5,2,2,2,5,
                4,0,1,2,0,4,4,1,5,2,4,2,3,4,2,0,0,4,5,4,1,4,5,3,5,0,3,2,4,0,0,0,2,0,1,0,1,0,1,4,
                2,2,5,2,0,3,2,3,3,4,2,3,4,4,0,4,1,3,0,2,1,3,4,3,0,0,5,5,1,3,0,3,1,3,3,3,5,4,5,2,
                5,5,0,4,0,4,4,2,0,0,5,1,5,0,1,4,4,4,3,1,2,0,2,1,3,2,3,5,4,0,1,5,3,3,3,5,0,0,2,1,
                4,0,0,1,3,3,2,0,4,4,4,1,3,5,5,4,1,2,4,0,1,2,3,0,0,5,4,5,2,5,5,5,0,2,5,2,0,2,1,2,
                5,3,0,5,4,1,0,1,3,1,4,0,4,1,5,0,5,3,4,1,5,3,1,5,4,0,0,5,0,5,0,3,1,3,5,0,2,1,5,3,
                2,5,0,0,3,5,5,4,1,3,5,1,5,1,4,0,5,1,5,0,3,2,0,0,4,4,4,4,3,1,5,4,2,2,0,2,3,3,5,5,
                2,1,1,3,3,1,4,1,0,1,2,3,1,2,4,3,1,0,2,3,4,2,1,3,0,0,0,4,4,4,5,2,3,3,1,5,3,2,3,5,
                2,0,0,1,3,3,2,5,5,5,5,4,3,0,3,3,5,5,5,1,3,5,5,1,4,2,5,5,4,3,1,2,4,3,3,0,1,0,4,0,
                5,4,5,5,1,3,4,4,5,3,5,3,4,3,0,5,0,4,5,4,5,0,3,0,3,4,5,2,4,2,4,3,4,3,3,2,3,0,0,1,
                1,0,3,5,2,2,1,2,1,1,0,3,4,4,1,2,2,0,1,2,4,0,4,1,1,4,3,3,3,4,1,5,5,1,1,5,3,5,3,1,
                4,1,5,0,1,2,2,5,3,4,5,0,4,3,0,2,3,1,0,5,3,3,3,4,1,4,3,3,4,3,4,1,0,4,0,5,2,3,5,2,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=70;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=46;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=36;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=21;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level6L(){
        int[] Tab0 = {4,1,1,2,3,0,2,4,0,1,3,4,3,1,2,0,3,1,4,1,3,0,4,3,1,1,3,3,5,2,1,4,2,0,1,2,5,0,2,4,
                0,2,4,2,3,0,0,4,0,0,4,2,0,2,0,1,0,5,0,3,0,2,3,2,3,4,3,5,3,2,5,5,2,4,5,2,3,4,2,1,
                2,4,2,5,0,4,0,2,4,0,5,3,4,4,5,5,4,4,2,2,4,5,5,2,4,1,0,0,2,4,1,0,3,1,3,0,1,3,3,5,
                3,0,3,4,2,1,0,0,3,2,3,4,0,5,0,3,1,3,5,3,5,2,1,3,4,2,1,5,4,2,0,4,2,1,5,4,1,2,5,4,
                5,2,2,3,0,5,2,4,5,1,5,3,0,4,3,2,5,2,0,1,0,3,5,3,1,5,5,4,1,5,2,0,1,1,3,1,3,2,4,0,
                0,1,2,3,0,5,0,2,4,1,4,2,1,2,5,1,5,0,1,2,0,0,0,4,3,4,0,4,0,0,1,5,5,0,2,3,2,4,4,2,
                5,4,5,3,0,0,5,1,2,0,3,5,5,4,2,1,5,1,2,3,2,3,3,2,0,2,4,0,4,4,0,3,0,1,2,4,2,2,5,4,
                3,5,2,1,0,4,4,3,4,0,0,2,5,2,5,3,1,3,4,2,1,1,3,2,0,0,2,4,0,1,4,3,5,3,3,4,4,4,1,0,
                0,2,3,0,2,0,3,5,0,0,0,3,1,1,1,2,1,0,4,1,5,1,1,4,4,4,3,2,4,3,4,5,1,3,5,3,3,1,4,2,
                3,0,3,3,5,1,1,0,3,1,2,4,4,2,5,5,2,5,0,5,2,5,0,0,5,1,0,4,0,3,5,1,4,1,1,2,3,4,2,2,
                2,4,2,2,1,3,4,3,0,4,1,3,3,1,5,1,4,3,2,1,3,5,2,1,1,1,4,4,1,4,3,5,4,4,0,3,5,3,5,3,
                4,2,5,4,2,4,3,5,4,4,4,4,4,4,4,3,3,3,1,1,0,5,4,1,3,3,1,2,2,0,5,4,0,5,2,4,2,2,3,3,
                2,2,0,5,0,2,4,4,4,3,2,1,2,1,5,3,2,5,2,2,2,0,4,4,4,3,0,5,0,3,4,4,0,3,4,2,1,4,5,1,
                0,0,5,2,4,2,0,1,1,4,0,1,3,4,1,0,0,3,5,5,1,2,5,1,5,3,5,1,4,5,0,1,3,2,4,2,2,4,0,0,
                2,5,3,5,0,5,0,2,0,1,3,4,4,4,1,1,2,5,5,2,2,5,3,1,5,5,1,4,2,2,3,4,2,2,5,5,5,4,0,0,
                2,2,1,3,1,5,4,3,0,4,4,3,4,3,4,4,1,4,1,0,3,1,3,0,0,5,0,2,0,1,0,5,3,2,1,0,5,0,5,3,
                3,2,4,0,3,1,0,5,3,2,5,5,1,3,3,4,2,2,1,4,3,4,5,5,3,5,0,2,1,3,5,4,3,2,0,1,1,3,2,1,
                3,1,1,1,1,0,0,5,3,1,4,3,2,2,4,4,2,0,4,1,5,1,0,4,4,0,2,5,1,1,1,2,1,3,3,3,5,4,1,2,
                1,4,3,3,4,0,1,1,4,0,3,2,0,3,1,0,2,4,0,0,2,1,0,1,3,1,5,3,0,3,1,0,0,2,1,1,3,0,5,3,
                1,5,0,5,0,3,1,1,5,5,5,1,5,3,5,2,0,3,5,0,3,5,4,4,5,1,4,1,0,2,4,3,0,5,1,5,3,0,4,1,
                5,5,5,2,5,2,5,0,0,5,4,5,0,5,4,5,0,1,4,0,4,0,1,0,5,5,2,5,2,4,3,1,4,5,1,0,0,5,0,5,
                1,0,5,4,0,5,0,2,2,3,2,2,4,2,5,1,0,3,3,3,4,5,1,1,1,5,3,3,5,5,5,0,1,3,4,5,2,0,1,5,
                2,1,4,2,1,0,2,1,1,0,3,5,3,3,4,5,4,3,3,2,1,2,1,4,2,2,3,0,1,3,2,5,0,1,2,4,3,5,1,4,
                5,1,2,1,4,2,5,0,1,2,4,2,3,4,2,0,3,3,4,5,2,4,0,4,2,1,3,4,5,2,5,3,0,1,2,1,0,4,3,3,
                3,4,3,1,2,3,0,1,0,3,1,0,4,2,5,5,3,5,1,0,4,1,5,4,1,3,0,3,4,0,5,3,4,2,5,3,4,2,3,4,
                3,0,1,3,5,0,0,1,3,1,2,2,3,2,3,4,3,4,2,3,3,3,1,5,4,1,5,2,0,2,5,2,0,4,2,0,2,4,4,3,
                0,0,0,0,1,1,2,1,3,1,2,2,2,1,0,0,3,3,4,4,3,2,3,4,5,3,0,1,1,2,5,3,4,4,1,0,3,0,5,2,
                4,1,2,5,2,2,3,5,3,2,3,5,0,4,2,3,2,1,0,3,2,1,1,2,5,3,2,2,5,5,4,5,2,1,1,4,3,2,2,5,
                0,2,0,5,2,0,3,0,3,1,2,4,5,4,5,5,1,0,2,4,0,1,4,2,2,1,0,2,0,3,4,5,0,2,3,2,1,4,2,4,
                3,4,0,3,3,3,5,1,5,1,1,2,5,1,2,2,4,0,4,1,2,3,3,5,2,3,3,0,0,4,2,1,4,3,1,2,3,4,3,1,
                5,0,3,0,0,3,2,5,0,2,5,0,5,3,2,4,2,5,4,0,0,4,4,5,4,3,5,2,4,0,5,0,3,2,2,4,5,3,1,1,
                4,3,5,3,5,5,0,0,0,4,1,1,0,3,1,3,2,2,2,3,3,0,3,3,1,2,5,3,3,4,2,5,1,4,0,3,5,4,2,0,
                0,3,2,1,5,3,1,5,2,0,5,1,3,4,3,5,1,4,0,1,2,5,2,0,5,4,5,0,3,3,5,2,2,0,3,5,4,1,0,2,
                0,0,2,3,5,3,1,3,2,2,1,3,1,3,3,0,5,5,1,2,0,4,5,3,3,3,1,1,4,2,0,3,0,2,3,1,5,0,5,2,
                4,5,3,2,5,0,5,3,4,3,5,1,5,4,1,1,3,0,2,3,1,2,2,0,2,0,5,0,0,3,0,3,3,2,2,4,2,4,0,2,
                3,3,5,1,0,4,1,5,0,5,2,2,4,2,0,0,4,5,0,0,1,2,4,3,3,3,0,0,3,2,1,3,1,1,5,0,0,2,0,5,
                0,3,1,0,4,5,2,5,2,3,4,5,1,2,5,1,2,4,1,5,2,3,2,0,4,3,5,4,4,2,5,2,4,1,1,4,1,5,1,0,
                5,0,4,1,1,2,2,5,4,0,2,3,1,3,3,0,1,4,2,5,1,5,0,5,2,4,3,4,4,0,2,2,5,2,2,1,3,2,4,4,
                0,1,5,2,0,0,1,1,0,5,4,5,4,0,0,5,5,1,0,0,1,3,1,4,2,1,0,5,0,5,3,0,2,5,0,4,3,3,1,4,
                5,4,1,4,2,3,4,5,2,4,5,2,2,5,1,3,2,5,0,4,5,4,4,4,0,1,4,3,3,5,2,1,5,5,2,4,0,4,3,0};

        int[] Tab3 ={5,5,4,3,5,5,1,3,3,5,3,3,1,3,0,2,3,5,2,0,0,5,5,0,2,1,1,2,1,3,4,3,4,2,1,4,1,5,1,3,
                4,0,1,4,3,3,4,2,0,5,4,1,0,3,5,0,2,0,0,2,5,3,3,3,1,3,2,1,1,3,0,0,1,1,4,5,2,3,0,5,
                4,5,2,0,1,0,3,0,5,4,3,4,5,5,4,4,0,0,5,0,1,4,2,3,4,4,0,0,3,4,3,0,5,3,2,0,0,4,3,1,
                3,0,2,5,0,3,1,5,0,5,3,0,5,3,1,5,3,4,0,4,1,1,4,1,0,1,1,1,4,0,0,4,2,4,1,1,2,4,4,2,
                4,3,4,3,3,0,1,1,0,5,2,4,1,0,0,1,3,0,1,4,3,1,1,4,2,2,1,2,3,2,3,2,2,4,5,0,5,3,2,3,
                3,5,2,0,1,5,5,4,0,2,1,2,3,3,4,1,3,5,1,1,1,1,2,0,4,5,0,5,2,2,2,5,2,3,4,1,0,3,1,5,
                3,0,1,3,2,2,0,4,4,5,5,0,2,5,5,3,3,4,2,4,2,5,1,5,2,4,5,5,5,1,3,2,2,5,1,0,1,3,4,5,
                1,0,1,4,5,0,4,4,0,3,4,2,4,4,5,3,3,4,5,0,1,5,2,4,2,3,5,5,4,1,0,1,4,3,1,1,4,2,5,1,
                5,5,5,1,3,2,3,4,3,5,4,2,5,4,3,1,1,2,3,5,2,2,1,2,1,1,3,3,0,0,3,2,3,2,3,2,5,3,0,5,
                2,4,5,1,0,3,5,0,5,4,1,2,4,2,4,5,2,0,0,4,4,5,4,0,0,3,4,4,3,4,4,5,4,3,2,3,3,0,2,5,
                4,0,3,3,3,0,2,1,0,4,2,0,5,2,2,5,1,5,1,1,4,0,2,3,2,2,3,2,2,1,3,5,1,3,4,2,0,5,0,5,
                3,4,3,3,0,5,4,5,4,5,0,1,5,5,4,4,4,2,5,4,4,3,1,3,0,3,5,5,0,2,4,0,2,4,4,1,3,4,1,5,
                4,2,5,0,3,0,5,0,0,2,2,1,2,3,2,2,4,2,4,4,1,1,4,1,0,3,3,0,5,5,1,1,2,3,1,2,3,3,4,2,
                4,0,0,5,2,1,2,3,0,4,3,5,2,0,1,3,2,5,1,3,3,3,3,4,2,2,5,5,2,0,4,1,0,0,4,4,1,1,4,5,
                4,2,5,2,4,2,0,3,3,2,4,0,0,5,4,0,4,5,0,5,2,1,0,4,3,5,2,5,4,4,4,5,4,3,1,1,5,2,0,4,
                3,5,0,4,5,0,3,3,5,4,3,2,4,4,4,3,1,3,4,0,0,1,2,5,5,3,1,2,4,5,2,0,1,2,5,3,0,3,4,2,
                1,1,3,2,5,3,0,5,4,2,0,0,0,0,0,2,5,2,4,3,1,3,2,1,1,2,5,5,0,4,4,3,2,4,1,0,4,5,1,4,
                2,3,3,1,1,4,0,2,4,3,5,1,0,2,0,2,5,2,5,3,5,3,4,4,2,5,2,5,0,1,2,1,1,3,2,4,4,4,2,2,
                0,5,5,4,0,4,3,3,0,0,4,5,1,2,4,0,5,1,4,3,5,4,2,5,4,2,2,3,5,2,2,4,5,4,2,1,0,4,4,0,
                4,2,2,2,4,2,1,0,1,2,5,1,2,0,0,3,4,0,1,0,5,5,4,5,1,2,2,0,4,2,4,5,4,0,5,5,4,0,4,4,
                4,2,5,3,0,5,4,4,5,4,0,3,3,5,0,5,1,1,5,2,4,2,4,5,1,5,3,0,2,3,3,2,5,4,0,4,2,5,1,2,
                1,2,4,1,2,4,4,3,5,4,2,3,2,4,2,2,2,0,0,3,0,3,1,5,4,4,1,2,3,1,4,5,0,3,1,1,1,4,0,1,
                3,2,1,2,1,5,5,4,0,5,1,4,3,4,1,5,3,2,4,0,5,2,3,4,1,3,5,5,2,1,1,2,3,1,2,2,2,3,0,2,
                3,2,2,5,2,3,3,2,4,0,1,0,1,3,4,3,0,4,5,3,0,3,5,3,1,5,0,3,3,3,1,5,0,0,2,0,3,3,2,3,
                5,2,1,0,4,3,0,2,0,3,0,0,2,5,4,1,0,1,5,3,4,3,1,2,3,3,1,5,1,3,1,4,3,4,0,4,5,4,5,4,
                5,1,2,1,5,2,5,1,0,4,5,2,4,5,4,2,0,1,3,1,3,3,3,0,2,0,1,0,1,2,5,5,3,2,5,4,2,0,5,0,
                3,0,1,0,1,1,5,0,1,1,5,5,0,5,0,5,2,5,2,0,4,1,4,0,3,2,3,3,3,1,2,3,5,1,1,1,3,2,5,5,
                5,4,1,3,3,2,5,4,5,4,5,2,3,0,0,0,2,0,2,0,5,5,5,1,0,5,3,5,3,2,2,2,0,2,1,2,1,1,1,3,
                0,2,4,4,5,0,3,4,4,2,4,0,3,0,3,1,5,5,0,4,1,1,2,4,3,5,5,5,1,2,0,1,1,0,3,2,1,2,3,2,
                0,5,1,3,4,0,5,1,4,0,4,4,0,1,2,1,3,4,5,2,1,5,3,1,3,1,4,4,2,4,0,4,2,1,2,0,1,2,0,0,
                3,0,1,2,4,5,2,0,3,5,0,4,2,5,5,2,1,3,5,1,1,4,4,1,5,2,5,3,0,3,0,0,0,5,1,5,4,2,2,1,
                0,3,1,5,3,5,0,1,2,3,3,1,0,0,2,0,1,3,3,1,2,1,1,4,3,4,3,5,3,5,5,5,4,5,2,5,3,4,5,3,
                3,3,5,5,0,0,3,4,5,1,0,2,5,0,4,4,3,1,3,3,5,2,2,0,4,5,5,4,3,0,1,1,2,3,5,5,0,0,3,2,
                2,4,0,4,1,4,2,1,5,0,5,3,1,3,5,5,4,1,2,2,1,1,0,2,4,2,5,4,2,3,1,3,0,0,5,1,0,1,4,1,
                3,4,2,3,1,1,3,4,2,5,3,4,0,4,5,3,5,4,0,2,0,1,5,0,5,2,1,1,0,5,5,4,3,0,4,4,1,5,5,1,
                0,2,3,2,1,2,3,1,3,0,2,2,1,1,2,5,0,4,3,2,4,5,1,3,5,2,0,2,3,2,1,5,3,2,4,2,4,3,4,1,
                0,3,0,1,5,4,1,4,3,0,2,4,4,3,5,0,0,0,2,2,3,4,0,4,3,1,0,0,4,5,3,1,1,5,0,3,1,0,4,2,
                0,1,4,3,5,0,0,5,2,3,1,4,0,3,3,0,4,1,2,1,5,2,3,0,0,4,0,0,4,4,0,5,3,2,1,5,4,1,4,3,
                4,5,4,0,2,0,3,2,0,2,2,1,2,2,5,2,5,2,1,0,1,0,4,4,4,1,3,4,5,4,3,1,0,3,2,4,4,2,3,1,
                1,2,5,4,1,5,3,4,2,4,3,3,5,4,5,3,1,0,2,2,5,2,4,5,2,4,5,2,3,1,1,5,4,1,3,4,1,0,2,0,
        };
        int[] Tab2 ={3,0,2,4,0,3,0,5,5,3,0,1,4,2,4,5,4,5,5,2,1,3,3,5,5,2,1,5,5,5,4,3,5,5,2,0,5,0,3,3,
                4,5,4,4,0,1,1,3,4,5,3,4,4,3,0,0,5,3,2,0,5,1,2,3,3,4,0,4,3,4,3,0,0,2,5,4,3,2,2,1,
                4,1,2,5,2,1,4,0,2,3,1,2,1,2,4,2,2,2,0,2,2,1,1,2,0,4,0,5,5,2,3,5,3,1,5,5,1,2,3,3,
                4,0,2,1,2,2,3,5,0,1,3,4,2,0,1,4,3,4,4,3,5,2,3,2,2,4,0,3,3,4,5,4,5,4,4,0,0,4,5,2,
                0,4,1,5,3,4,2,0,0,0,3,1,3,5,0,1,0,2,3,5,5,1,0,4,2,2,3,1,5,3,5,0,4,1,4,3,1,4,1,5,
                2,5,3,2,1,5,3,1,2,5,0,1,3,5,2,4,1,2,1,4,5,3,1,4,1,2,3,4,4,1,4,0,0,2,4,0,5,3,1,1,
                5,3,0,3,0,3,5,2,1,4,5,2,2,5,3,1,5,0,4,5,4,1,4,5,3,4,1,5,1,0,4,3,0,4,0,2,0,2,2,1,
                2,5,2,1,2,5,1,4,3,3,4,4,0,3,2,0,0,4,5,3,5,0,4,3,2,5,4,4,1,0,4,0,4,0,4,0,5,1,3,5,
                5,2,4,3,0,2,4,1,2,4,0,2,1,2,4,4,4,4,4,1,0,1,5,1,4,5,1,5,4,2,5,0,3,4,5,4,1,5,4,0,
                0,1,0,5,2,4,1,3,3,5,1,4,5,4,5,1,2,1,5,4,2,5,4,2,4,1,2,0,5,5,1,1,0,5,0,0,0,1,0,3,
                1,4,1,1,5,1,0,2,2,2,3,1,1,4,1,0,5,3,4,4,5,5,5,4,5,4,5,5,0,5,5,3,5,4,0,0,2,5,0,5,
                3,0,2,0,2,3,3,5,5,3,3,3,1,5,3,1,1,2,5,3,4,2,4,3,1,5,0,2,5,3,1,3,2,5,0,2,0,0,2,4,
                5,1,1,3,0,4,4,0,1,1,2,0,0,3,4,4,2,2,1,5,4,5,2,5,2,0,4,4,1,2,4,5,0,1,0,1,1,5,2,5,
                1,0,2,3,3,2,4,0,4,4,1,5,5,2,4,0,4,5,4,0,2,2,0,3,3,5,4,2,2,1,0,4,4,1,4,4,1,1,4,2,
                4,1,4,3,0,5,3,2,4,4,0,1,3,2,0,1,0,5,1,2,3,3,2,3,2,4,3,1,1,1,0,3,5,5,1,1,1,5,5,0,
                3,5,5,0,0,0,1,3,5,5,1,5,0,3,3,4,5,2,3,4,0,3,2,3,4,3,1,5,0,3,3,3,3,0,5,0,1,0,2,2,
                3,4,3,0,1,4,0,5,2,4,2,5,2,5,0,4,3,1,5,5,5,2,0,1,3,0,0,1,4,5,5,5,2,2,3,2,2,5,5,4,
                0,0,3,1,1,5,2,2,0,1,4,0,4,4,5,4,1,5,3,5,3,2,3,2,3,1,1,3,1,5,0,2,4,1,1,5,5,2,5,5,
                3,5,3,4,3,0,2,5,0,1,1,4,4,1,0,3,4,5,0,1,1,5,2,5,5,5,2,1,0,2,0,0,2,5,2,1,3,1,3,2,
                0,2,2,1,5,0,0,3,0,4,2,2,1,1,0,3,2,2,4,1,2,1,1,0,4,3,3,4,1,0,3,1,0,4,0,4,0,0,2,4,
                5,3,5,4,3,2,0,1,4,4,5,5,3,5,0,4,4,2,1,1,5,4,5,4,2,3,0,5,5,1,5,4,4,0,2,1,4,3,3,0,
                5,2,4,1,4,5,0,0,1,2,5,0,2,1,1,4,3,0,3,3,2,2,0,1,2,5,4,1,1,3,0,3,1,0,4,4,4,3,2,5,
                0,1,1,0,0,0,1,0,0,3,1,2,5,4,1,1,0,4,4,1,4,3,1,2,3,2,3,2,3,4,5,0,4,1,1,3,4,4,2,3,
                0,0,2,1,0,1,4,5,4,3,1,3,0,5,0,0,1,2,5,2,2,3,3,0,1,0,0,3,4,0,3,3,4,5,2,2,3,0,1,1,
                2,4,1,4,5,2,4,0,4,4,3,0,2,5,2,3,0,3,2,1,3,5,4,1,5,4,0,4,5,0,2,5,2,4,4,4,3,3,2,4,
                4,1,5,2,5,0,5,3,5,0,1,1,1,2,2,4,2,0,3,4,4,4,3,0,4,5,4,1,0,3,0,3,0,5,5,0,0,5,3,4,
                1,5,4,1,0,1,2,3,2,3,1,5,3,1,5,5,5,3,5,3,4,5,0,0,4,5,4,2,3,4,0,5,0,0,2,4,2,1,4,0,
                0,1,0,5,1,0,0,2,4,2,2,2,1,2,1,2,5,2,1,4,4,5,1,4,1,3,3,2,5,3,1,3,3,0,4,0,4,3,5,2,
                0,0,3,3,3,4,4,1,0,3,0,0,0,4,0,1,5,4,1,1,3,0,1,4,0,2,0,5,3,3,5,0,4,2,2,3,4,2,0,3,
                2,3,1,1,3,5,3,5,5,0,1,2,4,3,4,5,5,3,3,2,3,4,2,3,5,2,2,2,4,3,1,5,2,2,3,2,4,1,2,3,
                2,5,1,3,0,0,4,5,1,4,3,1,0,0,5,1,1,2,2,4,5,2,3,1,5,5,2,3,1,0,5,3,5,5,0,2,3,4,1,1,
                4,5,5,1,4,1,2,3,1,4,2,1,5,0,3,3,0,4,5,4,0,1,0,0,1,5,4,3,5,4,1,5,1,0,1,1,5,0,5,4,
                4,0,2,5,0,5,4,1,4,5,5,4,1,3,0,5,5,4,3,5,0,3,1,4,0,5,1,2,5,0,4,0,5,5,1,1,2,2,1,0,
                0,1,2,0,3,1,0,1,2,4,2,0,5,0,2,2,0,2,0,3,3,4,1,2,2,1,3,4,2,2,0,5,3,3,0,2,0,0,1,1,
                2,4,4,4,0,5,0,4,0,4,2,5,4,3,4,1,0,5,0,0,4,4,3,3,0,5,3,3,1,4,5,3,2,0,1,2,2,1,5,5,
                5,1,4,5,2,4,0,3,1,0,1,2,3,3,1,0,5,4,0,2,2,0,2,5,2,4,5,0,4,5,0,4,0,1,4,2,4,4,4,1,
                4,1,1,3,0,2,3,5,5,2,1,2,5,1,3,4,5,3,1,4,0,4,4,4,4,0,4,2,1,1,1,0,4,2,4,0,3,1,5,1,
                2,0,2,5,0,2,5,2,2,5,5,5,3,3,2,5,0,4,1,1,3,4,3,0,0,0,2,1,3,1,1,0,1,0,5,3,0,0,4,5,
                1,1,1,5,2,5,2,5,1,2,2,3,1,2,2,4,5,5,4,2,1,5,4,1,0,3,0,0,5,1,4,5,3,4,4,2,3,1,2,3,
                2,0,5,2,1,0,5,1,3,4,2,3,3,4,1,1,0,4,3,3,2,2,0,1,4,2,1,5,0,3,5,2,0,4,2,3,5,5,0,4,
        };
        int[] Tab1 ={3,1,5,1,2,4,0,1,4,2,3,4,4,5,5,0,1,3,1,3,0,1,2,5,4,5,5,1,4,3,1,0,0,0,2,2,5,0,5,1,
                0,5,3,3,4,5,1,4,1,0,5,0,3,5,1,2,5,1,0,1,2,1,0,5,0,2,0,3,2,1,5,3,4,5,3,3,1,0,2,3,
                4,2,2,5,4,5,5,0,5,0,1,3,0,0,0,3,0,5,4,1,5,0,1,4,3,1,4,3,1,4,1,1,0,1,3,0,5,3,1,2,
                4,4,0,0,0,0,5,2,0,2,2,2,5,2,0,0,0,5,5,5,1,2,0,0,4,3,5,3,4,3,4,4,3,4,2,1,1,1,3,2,
                1,5,3,5,2,4,3,0,4,5,0,2,5,4,5,4,0,4,5,5,0,0,3,1,5,5,4,3,3,1,5,5,4,0,2,2,0,3,4,2,
                3,4,4,2,3,4,4,3,1,1,3,0,3,3,2,1,4,4,4,5,4,5,5,1,1,0,1,2,1,0,0,0,2,5,2,0,3,5,4,5,
                2,5,5,0,3,0,1,3,3,3,5,4,1,1,1,0,5,2,3,4,5,5,4,1,0,2,1,1,2,2,4,0,0,3,3,0,0,5,0,4,
                4,0,4,0,2,5,1,4,3,3,5,3,4,4,4,1,3,2,2,2,2,3,3,4,0,1,3,1,3,1,4,2,5,4,0,0,3,2,3,5,
                2,3,0,2,4,5,2,5,4,3,5,0,5,2,3,3,0,5,0,2,0,2,0,3,4,2,2,4,4,5,2,1,5,2,0,4,2,4,0,1,
                5,2,2,2,1,5,5,1,1,5,3,5,3,0,3,5,1,3,0,1,3,4,1,4,3,5,4,4,2,1,0,1,0,2,4,1,2,5,0,0,
                0,2,0,3,2,2,5,2,1,5,0,3,4,3,3,4,5,1,5,5,2,0,1,5,1,5,4,2,3,3,4,4,2,1,0,3,1,3,4,0,
                0,0,5,3,1,3,0,0,1,3,3,2,1,4,1,1,4,0,3,4,0,1,1,5,4,3,3,0,5,1,4,3,3,5,0,4,1,0,4,4,
                2,1,1,2,0,1,5,4,4,1,3,0,0,5,0,4,2,1,0,4,3,4,4,4,5,4,2,1,4,4,1,1,1,1,0,0,1,3,5,2,
                5,4,5,3,2,4,4,5,1,3,2,1,1,0,4,0,3,2,0,0,4,0,3,5,4,4,2,4,3,3,1,4,0,1,4,5,5,0,2,3,
                5,1,4,2,4,2,2,5,4,2,0,5,2,3,4,1,2,5,4,3,4,2,4,0,0,2,2,5,2,1,5,3,2,3,3,0,3,3,5,2,
                2,4,0,1,3,5,4,1,5,2,5,3,3,0,4,2,2,3,4,1,3,1,4,1,0,4,1,2,2,3,3,4,2,5,3,5,3,4,0,5,
                1,1,5,5,0,5,5,3,1,1,5,0,5,2,4,2,1,1,0,1,2,4,4,3,3,3,5,3,1,3,0,1,5,1,3,3,4,1,1,3,
                1,5,4,4,4,0,3,3,4,5,5,2,4,1,3,0,2,5,2,5,1,4,0,2,2,3,2,2,0,5,3,1,3,1,1,4,0,3,3,4,
                3,0,3,4,0,5,2,5,2,2,2,4,1,0,5,1,0,1,4,1,0,2,1,0,4,5,5,1,5,5,1,5,3,4,5,3,0,0,5,2,
                1,4,2,1,2,2,3,0,5,0,4,3,2,0,3,4,5,3,3,0,1,0,3,2,5,0,2,3,2,4,5,5,2,5,3,3,0,5,2,1,
                3,3,5,0,5,5,4,0,1,3,3,2,1,4,0,5,1,3,4,1,2,1,0,4,2,2,4,0,0,0,5,5,1,0,5,0,4,2,4,1,
                4,3,0,0,5,3,0,4,2,0,4,1,1,2,0,5,0,2,3,1,1,4,0,5,4,1,3,2,1,5,1,3,1,0,2,1,3,4,5,1,
                3,3,3,4,3,4,1,4,1,3,5,0,0,2,3,3,4,5,4,3,5,2,4,0,5,3,0,0,3,3,4,3,1,2,3,2,0,1,3,3,
                3,0,3,2,0,3,1,5,1,3,1,5,3,5,5,4,1,0,5,0,4,0,1,5,1,2,3,4,0,5,1,2,5,5,3,1,5,2,0,3,
                3,3,4,4,3,0,4,1,0,1,5,4,4,2,4,3,4,4,4,0,0,1,4,1,4,3,0,4,4,5,2,4,5,2,2,5,2,1,0,4,
                3,4,0,1,3,2,1,2,5,4,4,5,2,1,0,0,5,1,3,0,3,5,4,0,2,1,2,1,4,5,5,2,1,5,5,2,5,4,0,0,
                4,2,0,0,1,5,1,1,5,3,4,0,4,4,0,4,0,4,2,1,0,0,5,3,2,4,2,4,2,2,2,2,0,5,1,0,2,1,1,0,
                3,2,4,3,3,2,2,4,3,5,5,1,5,4,0,2,4,4,5,1,4,3,4,4,2,2,1,0,4,3,3,2,0,0,0,2,5,2,2,5,
                4,3,4,0,0,1,1,2,5,3,5,4,3,1,0,5,3,0,1,1,0,5,5,3,4,5,0,0,4,1,3,3,3,3,3,2,3,0,4,5,
                4,4,3,4,4,4,2,5,5,5,2,2,2,1,3,4,2,2,3,0,3,5,5,4,5,3,1,4,1,4,1,2,1,2,3,4,1,3,4,4,
                1,3,0,0,3,1,0,3,5,0,0,5,3,2,4,4,5,2,5,2,4,5,3,1,3,1,4,0,0,5,4,0,5,5,0,2,1,3,0,5,
                5,4,2,2,5,3,3,1,5,0,5,5,2,4,0,4,3,0,5,1,0,0,1,5,3,2,5,4,4,5,5,2,5,2,1,2,4,4,2,1,
                2,3,2,0,1,1,2,3,0,5,2,3,1,2,3,1,3,4,1,2,4,3,5,0,0,2,4,5,2,1,3,0,5,5,5,0,5,2,0,4,
                5,1,4,3,0,4,2,4,0,1,3,1,2,4,3,3,1,3,0,4,2,3,3,2,1,0,3,4,1,0,2,5,5,1,2,2,2,0,1,0,
                0,1,2,3,4,2,1,0,1,0,3,5,4,1,4,5,5,0,0,5,3,3,2,5,0,1,3,1,5,3,4,5,2,0,0,4,5,3,5,4,
                5,2,2,1,4,1,0,1,2,5,5,2,1,2,5,5,1,1,0,5,1,5,4,5,4,0,2,4,4,4,4,2,0,3,3,0,3,5,0,0,
                1,3,1,2,1,3,3,4,3,2,3,3,1,0,3,5,2,0,5,0,2,2,4,1,4,3,1,5,3,4,0,5,2,4,4,2,5,0,0,5,
                0,5,4,2,5,1,1,1,4,1,2,0,0,1,1,5,4,3,3,4,5,4,0,4,4,3,0,1,0,1,1,4,5,4,2,2,1,4,3,2,
                3,1,4,2,2,3,2,4,3,3,0,3,4,1,4,0,2,2,2,4,4,2,4,3,2,2,4,4,1,0,4,0,1,0,3,1,5,0,4,0,
                2,2,1,3,4,2,1,5,4,4,0,5,3,2,5,2,3,1,5,0,5,0,5,0,3,4,3,4,5,1,4,0,2,1,4,1,5,4,0,4,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=68;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=42;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=33;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=22;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level7L(){
        int[] Tab0 = {0,3,2,3,0,5,5,1,3,1,0,1,5,4,1,4,2,1,1,5,4,3,4,2,4,5,3,1,3,3,1,3,5,0,1,3,0,0,1,1,
                2,0,2,4,2,5,0,5,3,2,5,5,4,0,0,4,0,1,2,1,4,5,4,2,5,2,0,1,4,2,2,4,1,0,0,3,0,1,4,0,
                3,4,4,1,5,1,3,4,4,2,2,1,0,1,2,3,2,4,0,1,5,1,4,1,5,1,3,5,2,3,2,5,3,3,4,4,4,2,4,5,
                2,2,3,1,2,2,3,0,3,4,4,2,1,0,2,2,5,3,1,3,2,5,2,5,1,1,0,4,4,4,1,3,3,5,5,0,3,3,1,5,
                0,2,5,1,5,4,1,3,1,0,2,5,0,0,1,4,3,0,3,5,4,4,4,0,3,3,2,3,1,3,3,4,3,4,1,3,0,2,1,4,
                3,3,2,0,2,0,0,0,2,4,3,4,2,4,1,3,0,0,4,2,1,1,3,3,3,4,3,3,0,1,2,4,5,0,0,1,5,2,3,2,
                1,5,1,0,0,5,0,4,3,4,4,3,4,0,5,0,0,4,2,1,4,0,3,4,2,2,5,0,3,2,4,1,1,1,1,1,3,3,3,4,
                4,3,2,3,4,1,2,4,2,4,4,1,1,2,0,0,0,1,1,3,0,1,2,5,3,0,3,2,4,2,1,5,1,0,5,5,4,0,4,1,
                3,4,2,3,3,3,1,4,3,3,4,1,3,4,5,0,2,2,4,0,2,5,2,5,5,3,0,5,0,1,0,3,4,0,2,2,3,4,2,3,
                4,5,5,5,5,0,3,4,3,1,3,1,0,5,2,3,2,3,2,2,1,1,0,5,4,5,2,4,0,3,5,3,3,3,2,5,5,3,4,0,
                3,2,2,4,0,1,4,4,0,0,4,3,4,2,0,0,4,5,1,4,4,1,4,4,4,4,0,2,4,5,0,5,2,2,1,5,4,2,5,0,
                0,1,0,0,3,2,0,4,0,5,2,5,2,0,1,0,4,3,0,1,4,2,5,3,4,5,2,2,3,3,3,5,0,4,3,4,0,0,3,1,
                4,3,3,3,3,5,1,5,1,0,4,4,1,4,5,4,5,2,4,5,2,3,3,4,0,5,2,5,2,4,3,3,4,2,4,0,4,0,2,5,
                3,5,1,1,2,4,5,3,5,1,2,4,0,5,3,3,4,2,2,3,5,5,5,5,3,2,5,5,5,2,4,4,3,0,4,1,4,3,1,3,
                5,0,4,3,2,4,2,3,4,1,0,2,3,2,3,2,2,0,1,5,3,5,0,0,1,0,0,1,0,2,1,0,1,1,2,5,0,5,4,3,
                3,0,2,2,3,4,1,4,4,5,1,2,3,0,3,3,5,4,0,3,3,4,4,0,3,1,2,3,3,1,2,0,0,3,3,3,4,4,0,1,
                3,1,3,1,1,2,3,1,1,3,1,1,3,2,3,5,4,0,1,5,0,0,1,3,5,0,4,0,4,1,0,4,5,4,5,1,5,4,5,0,
                0,0,1,0,3,4,4,0,2,3,4,3,1,1,1,1,2,1,2,4,0,2,5,0,2,4,0,0,0,1,1,3,1,5,3,3,0,5,2,4,
                4,2,1,5,1,2,2,3,5,0,0,3,3,0,5,0,4,5,0,2,2,0,0,2,1,2,4,1,0,3,1,5,2,5,4,3,3,5,0,2,
                0,5,0,3,2,4,0,3,4,1,5,2,3,0,0,5,2,5,3,4,1,2,3,5,4,4,0,5,5,3,1,1,0,5,1,4,1,5,4,4,
                4,2,1,1,3,2,4,5,3,1,3,0,2,5,3,1,0,2,3,0,4,0,0,2,0,0,0,4,0,2,3,5,4,4,3,5,4,5,3,1,
                5,2,0,0,1,0,3,5,2,3,5,0,1,0,5,2,3,3,2,1,1,1,2,1,3,3,1,3,2,3,1,5,4,3,3,3,0,3,1,5,
                5,0,4,4,2,4,4,3,1,0,0,4,3,2,3,0,3,4,4,1,1,1,2,1,4,4,3,0,5,3,1,3,0,5,3,3,2,3,5,1,
                4,0,2,2,2,2,4,0,5,0,1,5,5,1,1,4,3,4,3,5,0,1,3,5,3,2,0,1,0,4,1,3,2,5,1,5,0,1,3,3,
                1,1,3,3,1,2,5,2,5,5,2,2,3,4,3,1,0,0,0,3,2,4,2,2,4,5,0,5,0,4,2,4,5,2,4,0,3,2,2,3,
                1,5,5,2,4,5,1,3,5,5,0,2,1,4,4,4,1,0,0,4,5,4,2,2,0,3,1,3,1,0,5,0,4,2,0,5,3,3,0,4,
                0,2,1,0,2,1,1,1,2,2,2,0,2,1,3,5,2,0,2,3,2,2,2,2,2,2,2,2,1,1,0,5,4,4,0,1,2,4,5,4,
                5,5,1,1,4,4,3,4,1,0,5,1,3,2,0,1,0,1,0,0,0,3,5,5,1,5,3,4,3,2,1,3,4,5,3,0,1,1,2,1,
                5,5,0,1,4,3,5,3,3,1,1,3,2,2,3,3,2,4,3,0,1,5,2,4,1,2,1,1,0,1,2,0,2,4,3,0,0,3,2,1,
                0,4,4,4,2,2,3,2,5,2,3,3,4,4,4,0,0,4,2,3,4,1,0,2,5,3,1,2,2,2,4,3,3,0,1,5,1,2,3,2,
                4,5,2,1,3,1,4,4,4,3,1,1,1,4,4,3,5,0,4,4,5,0,5,4,0,1,1,1,5,4,5,3,0,5,3,5,0,1,1,0,
                2,1,3,3,4,1,4,0,0,5,1,5,2,1,5,2,5,3,5,5,3,1,3,2,1,4,2,4,3,2,1,4,0,0,4,0,1,3,2,5,
                2,2,1,1,3,2,3,1,5,2,2,3,2,5,5,2,5,4,0,2,5,3,1,1,2,0,3,2,5,5,0,4,1,5,1,1,0,1,1,4,
                2,1,5,3,3,1,2,5,2,5,2,2,1,1,5,3,4,0,1,3,3,1,5,1,1,0,2,5,2,0,1,1,4,2,0,2,2,3,1,1,
                3,3,3,3,0,5,4,4,2,4,3,5,5,4,5,2,5,2,4,1,4,2,3,5,4,4,2,5,0,0,2,1,0,5,4,3,5,4,3,3,
                1,5,4,0,1,0,0,2,3,1,0,5,1,4,1,1,0,1,2,4,0,5,2,3,3,0,2,5,2,1,5,1,3,5,3,0,5,4,0,4,
                1,0,3,1,2,5,1,3,2,0,5,4,4,1,0,3,1,4,5,5,1,5,4,0,0,5,0,2,1,4,5,1,2,1,4,3,1,5,3,3,
                3,5,0,1,3,1,4,4,4,0,0,5,3,3,5,4,1,1,4,3,1,1,3,5,2,5,2,4,0,5,4,5,5,4,0,1,0,4,1,3,
                4,2,0,3,4,4,4,0,0,5,0,4,1,5,1,5,2,4,3,1,4,3,4,0,2,5,3,2,0,5,4,4,1,2,4,5,2,1,3,4,
                1,3,3,5,5,1,3,0,2,1,2,4,3,1,5,5,3,3,2,5,3,4,1,3,4,1,5,5,1,1,2,3,1,5,2,2,1,5,5,5};

        int[] Tab3 ={2,5,2,4,5,4,3,3,3,2,5,3,3,1,1,5,5,1,5,1,4,5,5,1,3,0,0,4,3,0,3,2,2,5,5,4,4,3,3,1,
                0,2,1,0,2,5,5,2,5,5,4,0,2,5,4,5,1,4,5,5,4,2,1,0,3,5,5,5,4,1,0,1,1,1,5,4,3,0,0,1,
                4,1,0,0,0,5,3,5,0,1,5,4,0,0,3,2,1,2,3,1,2,5,0,4,2,0,0,3,5,0,2,4,0,3,0,0,1,5,0,4,
                4,4,2,4,3,0,1,3,2,4,0,1,1,0,0,4,5,4,4,4,5,2,3,0,1,5,2,1,0,5,1,2,5,4,2,4,2,3,0,5,
                4,2,5,1,5,1,5,3,4,2,5,0,0,1,0,3,4,4,0,2,0,0,0,1,1,0,0,0,5,3,2,0,2,2,2,1,0,2,1,2,
                1,3,4,1,2,3,4,1,3,0,1,3,1,0,1,2,5,3,3,0,2,4,3,2,4,4,5,3,3,2,4,1,2,1,0,4,0,5,3,5,
                5,4,2,4,3,3,2,5,2,0,1,5,4,0,0,4,5,4,3,5,5,3,5,2,5,3,1,0,0,2,1,5,0,0,5,1,1,3,3,4,
                2,4,0,2,4,3,2,1,2,5,4,3,3,1,3,4,3,4,0,5,1,4,3,3,5,3,3,5,0,0,1,0,2,5,3,0,5,4,5,0,
                1,1,5,1,3,0,4,5,4,3,2,0,3,0,1,2,4,4,3,0,3,0,0,1,5,0,5,4,0,3,5,3,5,0,3,1,2,5,0,5,
                1,3,3,4,1,5,1,2,5,5,5,1,3,3,2,5,5,0,4,4,4,2,3,0,5,0,4,5,4,0,5,2,2,2,1,2,5,1,4,1,
                0,3,3,2,0,4,0,5,1,4,5,1,2,1,5,5,3,5,5,5,1,0,0,2,0,1,5,2,2,4,3,2,0,3,4,4,4,3,4,3,
                3,1,5,1,0,4,0,3,1,0,2,3,2,0,2,2,5,3,0,5,4,0,2,5,0,2,5,2,0,0,2,1,3,1,2,1,0,5,4,0,
                5,1,4,3,5,5,3,1,3,3,4,2,4,1,3,4,3,3,4,2,3,2,0,2,5,1,0,2,3,2,5,0,5,5,0,4,4,2,1,3,
                4,4,2,3,3,0,3,5,0,1,1,5,2,0,4,4,1,2,4,2,5,2,0,2,1,1,2,5,2,3,3,4,1,2,5,2,2,3,4,3,
                3,0,1,2,3,2,0,1,1,3,1,3,1,5,2,0,3,2,4,4,2,5,0,0,3,2,5,2,0,0,4,2,4,3,5,2,3,1,3,5,
                5,1,1,5,1,5,3,2,3,1,3,0,1,0,4,3,4,1,4,3,2,5,2,4,5,5,2,5,4,5,5,5,3,5,5,2,4,1,5,1,
                1,3,2,1,2,4,3,4,0,2,1,3,5,1,1,2,3,3,4,2,5,5,4,1,5,5,4,0,4,1,1,5,4,2,3,2,3,0,5,2,
                0,2,1,2,1,2,1,0,3,4,3,0,0,1,4,1,1,4,2,1,2,2,5,0,0,5,5,4,2,0,3,2,5,1,4,0,2,5,4,1,
                5,0,2,1,2,2,2,0,1,3,1,1,3,4,0,5,0,1,5,2,0,5,4,4,1,4,1,1,1,5,1,5,0,2,0,4,4,3,5,2,
                5,2,2,2,4,3,2,0,0,0,5,3,3,0,0,2,2,0,5,3,2,3,4,4,0,5,0,0,3,2,1,1,5,1,4,0,1,3,5,5,
                3,4,5,5,3,1,5,4,2,2,2,4,5,0,0,0,1,0,2,3,2,5,5,4,4,1,5,1,0,3,4,2,5,2,2,2,3,4,4,1,
                2,1,0,1,3,4,2,3,2,2,0,2,4,0,3,1,2,4,1,1,5,1,4,2,2,4,3,3,3,2,1,0,0,4,0,4,0,2,0,4,
                1,2,3,4,0,3,5,2,4,3,1,5,0,0,1,1,0,5,4,3,2,3,0,4,2,4,5,1,5,4,0,3,1,4,1,1,1,5,2,0,
                2,1,2,5,4,4,3,3,1,0,4,5,5,3,2,1,5,3,5,5,5,2,0,5,2,3,5,0,0,4,2,1,1,4,5,5,4,1,5,3,
                3,3,3,2,1,2,2,5,1,4,4,3,1,5,1,0,0,2,0,2,1,0,0,1,3,3,4,0,1,4,1,1,0,1,1,4,2,0,4,0,
                3,0,5,5,3,3,1,0,5,0,2,1,5,4,3,2,4,0,1,1,4,3,1,5,0,5,0,1,4,3,0,1,5,3,3,2,3,1,4,2,
                5,0,2,0,0,0,1,1,2,3,0,4,4,2,2,2,3,0,2,4,0,5,1,4,1,0,5,5,4,2,2,4,4,4,0,0,5,5,2,3,
                4,4,1,2,3,1,3,2,5,5,1,5,1,5,5,1,5,1,3,3,3,0,3,2,1,3,5,2,0,4,4,5,3,5,4,0,0,2,2,3,
                4,1,3,2,0,1,3,3,1,2,3,3,1,5,3,4,0,4,0,0,4,1,0,1,0,5,4,1,2,5,3,0,0,3,4,1,2,3,4,4,
                5,3,0,3,1,2,2,1,2,0,1,0,1,4,2,2,2,0,3,2,2,0,1,3,1,5,0,2,5,4,4,2,3,0,3,5,3,5,1,1,
                1,2,2,0,4,0,0,1,1,5,2,3,2,4,4,4,2,0,4,3,3,5,5,1,3,2,3,0,3,4,3,0,3,0,2,5,3,5,4,0,
                3,1,2,0,3,5,2,5,5,3,2,4,5,2,5,0,1,1,0,5,2,5,5,3,1,1,2,3,3,5,4,5,2,5,5,0,1,5,4,3,
                5,2,1,4,2,0,5,3,2,0,0,1,1,4,2,4,1,2,4,0,3,4,0,2,1,2,3,4,5,5,3,3,2,4,4,4,5,5,1,2,
                0,4,5,1,5,3,5,3,4,4,1,1,2,2,1,5,3,4,5,3,0,2,5,0,5,5,3,3,2,2,5,2,2,4,0,5,0,4,2,3,
                1,4,3,3,1,1,1,1,2,2,0,4,1,2,4,2,0,3,0,4,0,3,3,3,0,5,0,5,1,2,0,2,0,3,4,1,3,3,2,5,
                5,1,4,3,3,0,1,1,5,1,4,1,4,1,3,0,1,3,2,0,1,5,5,4,0,2,2,0,5,1,3,5,4,0,4,3,0,4,0,5,
                2,2,4,0,3,3,5,1,5,3,1,4,5,3,5,2,0,3,1,4,3,3,5,3,3,0,1,5,1,0,4,5,5,4,0,5,1,3,2,0,
                5,4,1,3,5,5,3,4,4,2,1,0,3,3,4,3,4,3,3,1,2,5,3,2,4,2,1,0,5,1,0,1,4,3,0,1,5,2,4,2,
                2,3,0,2,2,5,2,5,2,4,2,4,2,3,1,5,1,1,2,1,1,2,3,4,4,1,2,1,0,2,0,2,3,1,2,5,4,1,1,4,
                3,0,5,0,4,5,3,3,4,4,1,4,0,0,0,2,5,4,2,4,5,0,1,3,4,0,1,2,3,4,4,2,4,2,3,0,3,0,2,3,
        };
        int[] Tab2 ={2,5,4,5,0,2,3,2,5,4,2,5,2,5,3,5,3,1,0,5,4,3,3,1,4,4,1,2,0,4,2,4,1,0,2,4,2,0,5,2,
                4,3,5,5,5,3,0,2,2,3,3,1,4,2,0,3,1,3,3,5,4,3,5,2,2,3,0,2,3,2,1,4,4,0,1,0,1,0,4,3,
                4,0,0,2,1,5,1,2,0,2,2,0,4,3,4,1,2,1,3,3,2,5,2,1,1,0,0,0,3,5,3,5,1,3,3,2,2,4,1,2,
                5,3,1,3,5,3,1,1,3,0,3,3,0,2,1,0,3,0,0,5,3,5,3,4,1,5,2,1,1,5,1,3,3,2,2,4,2,4,5,3,
                4,4,4,0,1,3,4,3,2,3,2,4,3,2,2,1,3,0,4,3,1,5,5,4,0,0,3,3,2,3,5,4,0,1,5,4,5,2,3,5,
                1,3,5,0,3,3,1,2,3,4,1,4,1,5,0,2,1,5,4,0,3,4,4,3,1,5,4,3,4,1,4,4,2,4,0,5,1,4,0,2,
                3,0,5,4,1,5,5,2,2,4,1,3,0,3,1,3,3,2,0,5,3,1,2,3,1,1,1,5,2,1,3,1,2,3,1,3,4,3,5,5,
                5,1,5,4,0,1,2,0,3,5,1,0,5,5,3,2,4,1,0,2,0,4,5,2,4,5,3,3,2,4,4,5,3,0,5,5,5,5,5,4,
                1,2,5,5,0,4,3,2,1,4,1,2,2,3,5,0,2,0,5,3,1,1,1,0,5,5,3,4,0,4,3,4,5,5,4,4,0,3,3,4,
                3,4,1,2,1,0,0,0,1,5,1,5,2,1,5,5,5,0,2,5,3,2,5,2,1,0,2,2,3,0,2,1,2,5,3,5,0,2,1,2,
                1,0,2,4,4,0,2,5,2,0,5,4,5,5,4,4,1,4,2,0,4,5,5,1,0,4,2,5,0,0,0,2,2,1,4,5,5,0,1,5,
                2,4,1,0,2,4,1,1,1,4,3,2,2,2,3,4,5,0,3,5,3,3,0,1,5,5,3,4,2,2,3,3,2,3,2,5,0,3,2,1,
                3,4,3,3,1,4,4,3,1,3,0,3,0,1,3,2,4,1,0,4,2,3,0,0,2,5,4,2,2,5,1,3,0,1,4,4,5,0,2,5,
                3,0,5,2,2,4,0,3,3,3,5,2,4,1,5,1,3,5,3,3,0,5,3,5,4,1,2,3,5,3,2,0,1,3,2,2,4,4,4,2,
                4,3,1,0,1,4,1,3,0,4,0,5,3,3,4,0,3,2,5,4,5,5,1,0,5,2,5,0,1,4,5,2,3,1,0,2,2,0,0,2,
                1,5,1,4,5,4,4,4,2,3,2,5,4,2,2,2,1,2,0,4,5,1,2,2,2,4,4,0,1,5,1,3,2,5,4,0,2,0,5,0,
                4,5,1,2,3,1,0,5,3,5,5,4,1,4,2,1,4,1,1,4,1,1,1,0,0,1,2,5,3,2,2,3,5,2,1,5,2,3,5,0,
                4,1,1,2,0,4,0,1,5,2,1,2,5,3,5,2,4,4,3,2,4,2,0,0,0,0,4,2,2,3,0,3,5,1,0,0,5,5,4,5,
                1,5,5,4,4,0,1,4,0,3,5,0,2,5,0,2,5,0,0,3,3,1,2,3,0,0,3,2,3,5,5,3,0,1,0,1,5,2,2,5,
                1,3,1,1,5,4,0,4,5,0,0,2,5,3,5,0,3,2,0,2,0,2,2,4,0,5,0,4,4,5,3,4,5,0,3,1,4,1,1,5,
                0,4,1,2,0,2,1,2,0,1,5,0,5,1,1,2,4,3,0,3,2,3,0,1,3,1,3,1,4,5,2,3,3,1,0,2,0,1,1,0,
                2,0,2,4,3,3,1,4,4,1,3,3,0,0,4,1,1,4,0,4,5,2,3,2,3,0,4,2,2,0,3,4,1,4,1,2,3,1,5,3,
                4,3,0,1,3,0,1,2,0,5,2,4,0,2,1,5,4,1,2,5,0,3,1,0,0,1,5,0,2,1,3,2,0,0,5,1,2,0,4,1,
                5,3,1,4,2,0,3,2,4,2,5,3,3,5,5,3,3,1,1,2,5,5,2,0,0,1,0,0,2,3,4,1,2,5,5,5,4,3,2,1,
                2,1,2,4,0,1,5,4,4,2,5,3,5,5,3,2,2,0,3,4,3,3,0,5,1,1,1,3,5,2,4,1,1,3,0,3,4,2,5,2,
                4,1,1,5,0,3,2,5,1,3,5,1,4,0,1,3,1,3,1,3,4,3,1,4,3,3,0,4,0,0,5,4,5,1,3,3,5,5,2,4,
                5,1,5,3,2,0,3,4,0,4,5,5,1,0,5,1,5,3,1,4,0,5,1,5,2,4,5,1,5,2,2,2,0,4,4,0,3,1,2,1,
                2,4,2,2,1,0,0,3,3,1,2,3,3,4,4,0,3,2,0,0,0,0,1,2,4,0,0,2,3,5,5,5,1,1,4,4,5,4,3,5,
                0,4,4,1,0,2,2,3,0,5,0,4,5,2,2,3,5,0,4,3,5,5,2,0,5,0,1,2,0,3,5,5,0,3,3,0,2,5,1,5,
                3,2,2,3,1,5,1,5,4,3,1,1,1,3,5,0,2,4,2,0,5,3,2,1,2,2,1,0,0,1,4,0,1,2,4,1,3,1,4,5,
                5,1,5,4,2,0,2,0,1,5,5,2,0,2,3,2,3,3,1,1,2,4,0,5,2,3,0,2,0,5,0,3,0,4,0,0,1,3,2,5,
                0,3,2,0,0,1,2,3,3,2,1,0,1,4,5,0,5,2,1,2,1,3,0,4,5,1,0,3,4,0,0,4,1,0,0,2,2,1,4,4,
                4,4,5,3,4,1,2,0,2,5,1,1,1,5,1,0,0,4,1,5,4,4,3,2,0,3,5,2,5,4,0,1,5,0,0,1,4,4,0,3,
                5,0,0,5,4,3,1,0,4,4,2,2,5,1,1,5,5,5,5,4,2,4,5,1,0,4,1,0,4,4,0,2,1,0,0,2,3,5,3,4,
                1,4,5,5,3,1,5,3,3,2,5,2,4,0,0,3,1,0,5,4,0,3,4,5,0,1,5,4,3,3,2,2,4,0,3,2,5,4,0,1,
                2,0,1,0,1,1,5,0,2,5,2,0,5,3,5,5,0,4,4,5,3,2,4,1,4,2,0,4,0,2,0,0,2,2,3,3,2,1,0,0,
                3,0,3,4,4,4,0,3,1,5,4,4,2,5,0,1,2,2,2,2,0,5,0,3,5,3,0,1,4,1,2,0,0,5,5,3,0,4,1,2,
                5,2,5,4,2,1,3,2,0,1,0,2,0,4,3,0,3,3,0,3,0,4,1,2,3,2,4,4,2,1,3,3,4,0,5,3,2,4,4,0,
                5,3,2,1,5,4,3,4,0,4,5,1,5,4,3,1,1,5,1,1,2,0,5,5,4,0,4,2,3,4,3,5,3,0,0,0,4,0,5,2,
                2,1,3,4,1,5,4,5,2,1,5,4,5,5,1,5,4,4,4,1,4,3,2,2,1,2,0,5,0,2,2,3,0,2,0,0,1,4,5,5,
        };
        int[] Tab1 ={1,4,0,2,0,3,5,4,2,2,2,5,5,3,0,2,2,5,4,4,4,3,2,0,1,0,5,1,1,5,3,0,4,4,3,5,4,5,0,5,
                5,5,4,4,1,1,5,0,0,3,4,0,3,2,4,2,3,5,1,3,2,2,3,2,4,0,4,3,0,4,3,3,4,5,3,5,5,2,5,5,
                5,2,4,1,3,3,5,2,1,1,4,2,5,0,1,4,4,0,2,0,2,2,0,5,4,1,2,5,3,4,2,4,0,3,1,2,5,4,5,1,
                5,3,4,0,5,2,3,2,0,3,1,1,2,1,4,5,4,1,1,2,1,0,3,0,2,1,4,0,1,3,3,4,1,4,4,1,1,0,0,1,
                5,0,3,0,3,0,1,4,0,0,5,0,5,0,3,4,3,4,2,3,1,5,5,4,0,4,3,2,3,2,4,2,5,0,5,3,2,1,0,3,
                0,5,3,3,5,4,0,5,0,5,0,3,1,5,0,0,3,2,0,0,3,1,4,4,3,0,3,3,5,2,5,3,2,0,0,5,4,1,3,3,
                1,1,3,1,0,4,1,4,4,3,3,4,1,5,2,2,0,0,5,2,0,2,5,5,1,0,5,4,2,5,3,5,2,2,4,1,0,4,4,5,
                1,3,5,5,1,2,3,1,2,1,2,3,1,5,3,5,0,4,1,4,5,0,4,0,3,3,0,1,5,5,4,4,5,5,0,1,3,2,5,3,
                3,4,4,3,3,1,5,0,4,1,5,4,4,5,3,1,0,1,2,1,5,1,2,4,3,2,0,3,3,2,1,3,0,1,0,0,5,5,3,1,
                5,5,2,2,3,2,4,3,3,5,1,1,0,3,2,0,5,0,2,3,4,4,0,0,4,0,3,0,5,5,3,1,5,4,3,0,0,1,5,4,
                3,5,0,1,0,4,4,4,2,2,2,3,4,0,4,3,1,4,0,2,5,3,1,1,1,3,4,5,5,1,0,3,5,0,4,2,4,5,0,5,
                5,3,4,0,5,5,0,3,4,5,4,0,5,3,5,0,0,3,4,0,2,0,5,3,5,5,5,0,2,3,2,4,2,2,1,2,5,1,5,4,
                4,3,2,2,2,4,3,5,3,3,1,1,1,0,0,5,1,4,2,5,0,3,3,2,1,5,4,1,3,0,3,1,5,2,1,2,5,4,3,2,
                0,2,0,3,1,1,3,0,3,4,5,1,5,4,0,0,3,4,0,5,2,1,0,0,1,2,2,4,2,5,5,2,1,4,3,0,4,5,0,2,
                5,2,1,4,1,1,1,0,3,5,0,2,2,2,2,3,3,5,2,2,0,0,4,2,3,0,1,2,4,3,5,0,5,0,0,3,2,1,1,3,
                1,1,2,0,0,1,5,0,4,3,0,3,1,5,2,0,5,3,5,4,5,0,2,0,4,5,3,4,4,0,1,4,4,4,1,3,2,1,2,1,
                0,4,1,3,1,1,4,3,3,5,2,4,4,0,1,4,0,4,3,3,1,0,1,4,5,4,5,0,2,0,0,1,4,2,3,0,1,4,4,3,
                1,3,2,2,0,5,4,2,4,1,1,0,5,4,0,0,4,4,1,5,3,0,1,0,0,3,5,2,5,2,5,2,0,0,3,4,3,3,5,4,
                5,5,4,5,0,4,0,4,1,3,4,2,2,3,4,3,3,5,5,1,0,5,5,5,3,2,2,0,4,4,4,5,5,1,3,0,3,1,0,3,
                3,4,5,1,5,4,2,5,0,2,0,2,2,3,2,5,1,0,0,2,3,5,0,1,5,5,1,4,1,5,2,3,3,0,0,0,0,1,5,2,
                5,5,2,1,3,3,1,2,0,1,1,0,0,5,3,3,4,0,1,1,1,0,1,0,5,5,2,4,2,0,2,5,5,3,2,2,4,1,2,5,
                2,3,3,1,4,2,2,0,0,5,4,1,0,3,1,4,4,3,2,5,3,0,3,1,0,2,0,1,4,4,4,1,4,5,1,5,3,2,0,2,
                3,2,3,4,1,3,2,5,4,3,3,3,1,5,4,3,4,5,0,0,5,0,3,3,5,0,4,2,0,3,3,4,0,4,3,0,0,1,2,2,
                5,5,5,2,1,3,1,0,2,4,5,1,5,1,1,0,5,0,1,4,0,5,4,5,4,4,0,2,0,0,5,3,5,5,0,4,4,5,5,5,
                1,4,3,3,3,2,0,3,4,0,0,4,3,5,4,0,3,3,3,0,5,1,3,3,2,1,5,0,0,5,2,1,1,0,0,4,3,5,5,0,
                4,5,2,0,3,5,5,2,5,4,2,2,1,5,2,5,2,2,1,2,5,5,3,0,5,3,1,4,0,2,0,2,3,4,5,2,2,3,5,4,
                1,1,2,0,1,5,1,3,5,3,1,0,0,0,0,5,4,1,1,4,0,1,4,2,3,3,4,2,1,4,2,5,2,2,3,2,3,4,4,0,
                1,3,4,2,1,0,5,3,0,3,4,5,1,3,5,5,3,5,1,2,5,0,4,1,0,4,2,5,0,0,4,2,0,2,4,4,0,3,1,4,
                0,2,0,1,4,2,3,4,0,1,5,1,4,1,2,5,2,0,2,5,4,1,5,1,1,5,5,3,0,4,4,3,0,4,2,1,2,0,2,0,
                4,1,1,1,4,2,3,2,3,3,2,3,3,5,1,3,2,3,4,5,2,5,1,4,0,2,4,1,3,0,3,3,3,4,0,3,5,1,1,3,
                3,0,2,2,5,1,5,4,2,2,2,4,4,2,4,0,4,1,4,5,3,2,4,0,1,1,4,1,0,0,5,3,1,3,2,1,4,0,1,3,
                0,3,2,2,2,0,1,5,4,2,1,0,5,2,0,0,0,0,1,5,2,4,2,2,5,4,1,0,3,3,4,4,4,3,0,1,1,0,4,1,
                0,1,3,4,3,3,0,5,2,2,3,1,5,1,0,5,1,2,2,0,3,4,3,4,0,2,5,1,5,1,0,3,3,4,5,4,4,1,4,5,
                0,2,4,4,1,5,2,1,5,5,5,5,1,3,2,4,5,3,4,5,5,0,5,1,2,0,1,5,0,4,0,5,0,0,3,3,3,5,2,1,
                5,5,4,2,4,2,3,1,2,3,4,3,1,4,0,5,1,1,5,4,1,5,4,1,5,5,5,0,4,1,4,0,1,2,0,0,0,4,0,4,
                0,4,1,5,3,0,4,0,2,1,2,4,4,4,0,2,2,2,5,1,2,3,3,4,0,5,4,5,1,1,3,1,5,1,4,3,0,3,5,1,
                1,2,2,3,5,5,4,1,1,4,4,0,3,2,5,2,5,5,3,4,0,3,0,5,5,5,5,3,1,1,3,4,2,5,1,4,5,4,4,0,
                3,5,5,5,5,0,1,1,0,3,5,4,3,1,3,3,3,2,1,4,0,4,2,3,5,3,0,5,1,4,2,2,5,5,0,5,3,5,3,0,
                1,3,3,3,0,1,2,2,4,5,5,0,3,2,5,1,1,3,0,4,2,3,2,2,3,5,0,2,2,5,1,0,4,0,1,3,5,3,5,5,
                0,1,2,1,5,3,5,2,3,1,4,5,5,3,5,3,3,5,5,2,4,3,1,1,5,3,2,5,5,0,1,0,3,3,3,1,0,0,1,2,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=63;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=42;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=35;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=21;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level8L(){
        int[] Tab0 = {3,3,5,0,4,4,3,3,3,2,2,0,3,1,1,5,5,4,5,1,4,4,0,1,5,1,5,0,5,3,5,1,1,0,1,1,5,5,0,5,
                5,1,5,3,3,2,5,2,1,1,1,4,2,0,3,3,2,2,4,4,5,5,4,4,3,5,3,2,5,0,4,2,1,0,0,5,2,1,5,3,
                1,2,3,3,1,5,1,1,4,1,3,5,0,3,1,4,3,3,2,0,5,2,4,1,1,1,5,4,2,1,2,5,0,1,2,0,3,4,2,0,
                0,3,1,5,4,5,5,3,2,1,0,4,1,3,0,0,2,4,1,0,4,3,0,2,0,4,5,3,0,3,1,0,4,4,0,4,0,3,4,0,
                3,1,4,5,3,5,5,4,3,3,4,3,1,0,2,0,4,0,3,1,1,2,4,1,5,5,4,0,5,0,3,3,3,4,5,1,0,3,4,5,
                0,4,4,5,1,2,2,3,3,2,0,4,2,4,4,1,5,1,2,5,3,2,1,4,4,0,0,5,5,5,5,1,5,5,0,2,0,5,3,4,
                1,1,1,3,3,3,2,2,5,1,3,5,4,3,0,4,2,2,2,3,4,1,5,1,5,0,2,1,0,3,3,3,1,0,3,3,3,3,0,5,
                0,0,1,3,5,0,1,4,5,5,3,3,1,3,3,4,5,5,1,2,4,1,1,1,3,3,5,5,5,3,0,0,0,4,2,3,1,3,4,0,
                0,5,3,1,4,5,5,2,0,2,4,3,3,3,2,4,1,1,5,2,5,3,2,0,4,4,5,5,4,2,3,4,4,3,5,5,0,4,1,1,
                2,1,0,4,1,5,4,4,3,1,5,0,1,5,0,2,0,5,5,0,2,2,0,1,4,4,1,0,4,1,1,5,1,1,0,1,4,2,0,4,
                1,1,0,4,2,1,2,4,4,5,3,5,1,3,3,5,1,3,3,4,0,4,1,1,4,1,3,5,1,4,3,4,3,0,5,0,4,0,2,5,
                1,4,3,2,2,5,2,0,1,2,2,1,4,1,4,2,4,5,0,0,2,1,3,5,2,1,5,0,1,3,0,4,4,3,0,4,0,2,5,1,
                2,2,4,1,3,4,5,0,5,4,0,4,3,0,2,1,4,4,5,2,0,3,2,0,5,4,0,5,3,1,4,4,4,4,4,2,0,1,2,5,
                1,0,4,5,4,5,4,5,4,2,4,2,2,0,1,0,1,3,5,0,1,2,1,4,0,2,3,5,3,3,0,3,4,0,1,1,1,0,5,2,
                5,2,5,5,5,5,3,5,5,0,2,3,1,3,2,5,3,1,4,4,2,1,0,1,2,1,4,0,4,4,2,4,5,0,4,1,1,1,5,0,
                1,5,5,1,5,5,1,4,0,2,1,3,5,2,2,0,5,2,0,5,3,1,2,4,5,5,3,2,1,0,3,3,4,4,2,3,5,0,4,0,
                5,1,0,2,3,2,3,2,2,1,4,4,1,1,5,5,2,0,5,4,5,0,4,1,4,3,0,1,3,2,1,3,2,1,0,1,5,1,1,5,
                5,3,2,1,3,0,4,4,4,5,2,5,4,1,4,5,0,1,0,5,1,2,2,4,1,3,4,4,3,3,0,4,5,1,1,1,4,5,0,4,
                5,3,1,4,1,3,5,3,3,1,1,4,5,2,4,0,4,0,5,0,3,1,5,3,5,3,5,1,5,0,3,2,4,5,2,3,0,3,4,0,
                0,2,4,1,3,2,5,4,5,2,2,5,4,4,4,1,1,4,0,5,1,1,0,4,0,1,4,0,3,2,5,4,2,3,4,2,1,5,2,1,
                1,5,3,3,0,3,4,5,0,5,3,2,0,5,3,5,1,2,3,5,4,2,1,0,0,2,4,2,1,5,2,4,0,3,4,4,5,4,3,5,
                3,5,4,0,3,2,2,2,4,5,0,2,0,0,4,4,1,3,2,5,3,0,5,4,5,3,4,2,4,3,5,2,0,0,5,1,2,4,1,0,
                3,5,0,2,1,5,2,4,2,2,1,2,5,4,3,5,3,5,2,0,2,0,0,5,3,0,5,2,5,2,1,2,0,4,0,3,3,2,1,1,
                1,2,1,5,2,0,4,0,0,1,5,0,1,1,4,3,1,2,5,3,3,2,2,0,5,0,5,2,2,5,0,1,2,5,3,1,4,0,5,2,
                5,2,1,2,1,1,0,1,3,4,4,3,4,0,3,1,0,5,2,0,0,2,1,1,2,1,5,5,3,2,5,1,0,3,1,0,1,3,2,4,
                1,0,5,1,1,1,0,4,5,4,0,4,1,1,5,3,3,1,2,5,4,1,2,0,3,3,5,2,0,1,3,5,1,0,5,0,4,3,5,2,
                3,3,0,3,2,5,2,5,2,0,0,3,2,1,0,1,2,4,2,5,0,3,5,0,0,5,3,5,1,0,1,2,3,4,1,5,5,4,4,5,
                2,0,4,4,1,3,4,4,1,3,2,3,5,1,2,4,5,0,3,0,5,5,0,1,4,0,2,5,4,4,0,0,5,0,4,0,5,3,3,4,
                4,1,0,2,0,3,3,0,1,2,5,2,1,2,3,1,4,4,4,5,1,4,2,2,0,1,1,1,2,3,5,4,4,2,1,4,1,4,2,4,
                2,3,4,1,3,5,0,3,3,5,1,2,1,0,1,2,5,0,0,5,3,3,5,0,0,1,4,5,0,2,0,1,2,2,0,1,3,3,3,2,
                4,0,2,0,1,0,5,3,0,0,4,2,1,1,4,3,3,1,4,4,0,1,3,5,0,2,3,0,2,2,4,3,3,5,5,2,5,5,1,2,
                0,1,4,2,4,2,0,2,5,1,0,5,5,2,2,0,3,3,3,2,4,4,0,5,1,1,3,0,0,5,1,3,0,3,5,3,4,5,4,2,
                4,5,3,1,0,5,3,3,3,2,5,2,2,3,1,2,5,3,2,1,2,2,3,1,3,5,4,2,0,2,2,0,4,1,4,5,2,2,2,3,
                0,0,1,4,0,2,4,5,1,5,1,4,1,5,5,5,5,0,1,1,3,0,4,5,0,4,2,4,2,0,3,2,4,2,1,0,2,3,2,1,
                0,3,3,0,4,4,1,0,4,5,4,4,3,0,5,3,5,4,5,1,3,5,2,4,5,2,2,1,3,4,3,1,4,0,1,5,2,5,1,5,
                4,3,2,2,4,3,5,1,2,3,2,0,2,1,1,3,1,0,3,0,0,2,1,2,0,0,4,4,1,4,3,3,1,0,5,2,0,1,3,1,
                2,1,5,4,3,1,2,4,3,2,1,1,3,1,2,0,2,3,1,3,3,2,5,5,1,3,1,0,0,4,1,1,5,3,1,1,4,3,3,2,
                4,3,3,1,0,5,4,2,5,1,1,2,1,4,3,5,5,1,5,3,1,2,3,5,3,4,2,1,4,5,0,3,5,5,4,2,2,0,5,5,
                2,2,3,3,3,5,2,0,5,4,3,3,5,0,1,3,2,1,1,3,5,4,2,1,0,2,1,2,5,1,3,0,1,0,0,1,1,1,1,1,
                0,5,0,0,4,0,3,5,5,2,2,4,1,2,4,2,1,1,1,3,4,3,0,4,3,3,5,4,0,3,4,2,3,3,1,3,5,2,5,4};
        int[] Tab3 ={3,3,1,5,3,4,0,5,5,4,1,2,2,2,4,4,1,0,3,3,1,4,5,3,3,2,2,1,1,0,3,5,0,5,2,1,3,2,4,1,
                1,1,1,1,2,0,2,4,4,0,4,3,1,5,3,2,3,5,0,3,3,3,1,2,3,0,5,3,1,0,3,1,4,1,1,0,5,4,0,0,
                4,0,0,3,2,4,5,1,2,4,5,1,3,4,3,0,2,5,3,2,4,2,0,2,2,1,4,3,5,4,3,1,4,5,0,1,3,5,2,5,
                2,0,1,1,1,5,5,1,2,4,2,1,0,5,2,1,1,0,0,5,5,5,2,4,2,4,2,4,2,0,5,0,1,5,0,5,4,5,5,0,
                5,5,0,0,5,2,0,5,4,5,1,4,4,1,1,2,3,1,0,3,3,4,4,1,0,2,4,2,4,3,0,1,1,1,1,5,2,0,1,3,
                5,1,5,3,3,5,3,0,2,0,2,4,2,0,1,2,3,1,2,0,4,5,2,3,0,3,1,2,1,1,3,0,5,1,5,0,3,0,5,1,
                2,1,3,5,4,4,2,3,0,0,0,0,3,5,1,0,2,3,3,1,1,1,3,5,1,0,0,5,0,0,1,1,4,3,3,4,3,5,1,3,
                1,4,0,1,4,5,1,5,4,3,5,4,5,2,0,3,2,2,5,2,5,1,2,4,2,5,0,5,2,4,2,3,0,2,3,5,4,3,5,1,
                2,4,4,1,5,4,2,3,4,0,2,4,1,2,3,1,1,3,1,0,1,1,1,2,2,0,1,2,0,1,2,5,0,4,4,4,2,1,3,3,
                4,4,1,5,3,0,2,0,1,3,0,5,2,4,5,5,1,2,2,4,0,2,3,4,4,5,3,1,4,4,2,0,0,0,2,2,0,1,3,1,
                0,0,3,1,0,2,0,5,1,3,1,1,1,3,4,3,0,4,4,0,5,3,3,5,1,5,0,5,5,4,5,0,3,1,3,3,1,2,0,3,
                2,1,2,1,0,0,2,1,2,4,1,3,1,1,2,5,0,4,4,5,4,1,0,0,1,4,2,3,2,2,4,0,4,1,1,4,5,3,2,3,
                2,1,2,3,0,1,3,2,1,3,5,4,2,1,0,3,0,5,2,2,1,5,5,1,3,1,3,2,2,0,4,2,5,5,3,0,5,1,5,5,
                1,0,5,3,4,3,1,5,5,4,0,3,2,0,0,0,2,3,2,3,4,3,1,2,5,5,2,1,4,3,3,4,5,0,4,0,2,1,5,3,
                4,4,0,0,1,1,1,2,1,4,0,0,3,4,4,0,4,4,2,4,2,0,0,0,1,5,4,1,2,0,3,2,2,4,0,0,1,0,3,1,
                2,0,5,2,4,4,2,0,3,0,2,2,4,1,0,4,3,3,1,0,5,0,3,1,5,0,4,1,4,2,1,4,4,0,2,1,0,5,5,1,
                0,5,2,1,5,3,0,1,2,5,2,0,1,3,1,1,4,0,3,3,1,2,1,3,4,5,1,4,5,2,5,3,3,2,2,3,2,2,3,2,
                1,2,2,1,3,4,0,5,4,1,3,0,3,0,0,3,4,1,4,5,2,4,4,1,2,5,4,4,3,4,4,5,2,0,3,2,2,4,5,0,
                3,3,4,4,1,1,5,0,3,2,0,2,0,1,4,0,5,0,4,0,3,0,1,0,2,5,5,4,4,2,0,4,3,2,1,3,3,5,1,3,
                3,0,5,1,2,4,0,3,2,4,1,5,5,1,0,0,1,0,0,3,2,4,2,1,5,1,2,3,2,0,0,0,0,0,1,5,3,2,1,4,
                1,5,1,1,4,5,3,1,4,3,2,0,0,4,0,2,1,1,1,4,4,2,0,4,5,4,3,4,0,5,5,3,5,1,0,4,5,2,0,5,
                5,2,4,2,2,5,3,3,1,4,3,0,3,4,2,0,0,3,1,3,4,1,1,1,0,2,5,2,2,2,4,1,5,2,2,5,2,4,1,2,
                5,0,2,3,3,5,4,1,2,2,1,4,5,5,5,5,0,2,0,1,0,1,0,3,3,0,0,5,0,0,1,4,3,4,4,1,4,0,4,0,
                2,0,0,5,2,1,4,4,3,1,2,1,0,3,4,0,5,0,3,0,5,5,4,5,1,1,4,2,3,2,4,4,3,4,0,2,0,4,1,3,
                3,2,1,0,3,1,1,2,5,4,1,4,5,1,0,0,5,1,2,0,2,3,1,0,0,3,1,3,5,5,5,2,3,5,3,3,0,0,3,1,
                2,5,4,3,1,1,0,2,4,0,2,2,2,4,4,0,5,5,5,3,0,0,3,3,5,2,0,2,5,0,3,2,2,1,3,5,4,3,3,4,
                2,3,2,2,2,3,0,5,5,4,1,4,5,0,3,1,3,1,0,2,3,3,0,1,1,1,2,3,1,0,3,3,5,0,3,5,3,3,3,1,
                2,3,0,3,3,1,1,1,3,1,2,1,1,1,1,0,5,4,5,2,5,3,5,2,5,1,2,3,1,3,0,1,0,1,5,4,0,4,0,2,
                3,3,5,0,2,0,3,0,2,0,1,0,0,2,2,0,4,1,4,1,1,5,2,5,5,4,0,5,0,4,4,4,4,3,1,3,1,4,0,4,
                0,5,2,4,5,0,4,0,3,5,1,5,1,5,5,4,4,4,3,5,4,3,5,2,4,4,0,0,4,4,2,5,1,3,4,0,5,5,2,5,
                4,3,2,1,3,4,2,2,1,3,0,0,4,5,1,1,3,2,4,5,4,0,0,2,2,3,0,4,1,0,3,4,3,1,0,3,3,5,2,2,
                3,4,4,1,1,1,3,2,3,3,5,3,3,1,1,1,3,1,2,5,2,0,2,2,0,5,1,1,2,3,2,2,3,4,0,3,1,4,2,1,
                4,2,4,3,1,4,0,5,4,3,0,5,0,3,5,0,0,0,5,3,0,0,4,1,2,5,3,2,4,2,3,3,5,0,3,0,0,5,4,0,
                0,2,3,3,0,1,1,1,3,5,3,3,4,2,2,0,1,3,3,5,1,0,4,2,2,1,1,3,0,1,3,5,4,3,1,3,5,1,1,1,
                0,2,0,5,0,2,2,2,0,1,4,5,4,3,2,5,1,3,4,2,0,5,2,4,2,3,2,1,0,0,1,5,0,1,0,5,0,1,1,2,
                2,4,0,2,2,1,4,5,0,3,2,3,4,1,4,0,5,3,2,3,2,2,0,5,3,1,1,5,1,1,5,3,1,0,3,0,5,2,2,4,
                1,1,0,0,1,3,1,1,3,3,2,5,3,2,3,2,1,4,1,1,5,0,0,0,3,3,2,3,4,1,1,4,0,3,0,2,0,4,0,4,
                0,3,0,3,2,0,0,0,5,4,2,0,3,2,5,0,0,1,1,1,0,5,2,2,1,4,4,0,3,5,4,1,5,3,4,5,0,5,0,2,
                3,1,2,4,1,1,3,4,3,5,2,3,3,2,1,2,5,3,2,4,1,2,3,1,2,1,3,3,0,2,4,5,3,1,5,2,4,5,3,4,
                4,0,4,0,0,3,3,4,4,4,2,5,4,1,2,1,3,2,1,4,1,1,4,4,1,0,2,2,3,5,1,1,1,4,2,2,4,4,3,5,
        };
        int[] Tab2 ={2,4,5,0,4,3,5,5,5,1,4,3,2,4,5,3,1,2,0,5,0,4,4,4,4,5,4,2,4,1,1,4,3,1,2,0,4,4,2,5,
                4,3,5,5,3,4,1,0,4,4,1,3,1,1,0,4,2,1,4,3,1,0,1,4,5,0,1,0,3,5,4,1,2,5,5,0,0,3,2,5,
                1,3,2,4,1,3,3,4,2,2,5,1,4,2,5,3,0,3,0,4,3,5,5,1,4,5,4,2,0,0,3,5,2,5,4,5,2,4,0,0,
                3,2,5,0,3,0,3,2,3,2,4,1,4,3,1,2,1,5,1,1,1,4,4,0,2,3,4,2,0,3,1,3,0,4,3,2,1,1,3,5,
                1,5,2,0,4,2,4,0,4,1,1,3,0,5,3,4,0,3,1,3,3,0,4,2,4,0,4,2,0,5,2,5,1,5,0,4,1,3,5,2,
                3,2,2,1,0,4,5,3,5,1,3,3,1,4,0,0,0,3,0,4,5,5,2,5,4,2,0,1,3,2,0,3,1,2,0,1,1,2,5,0,
                5,1,4,2,2,4,2,3,3,0,5,2,4,0,0,4,5,4,2,2,4,2,0,5,2,2,3,3,3,5,1,1,1,3,5,0,0,2,2,4,
                4,1,1,5,1,3,5,5,5,3,3,0,4,4,1,5,1,4,0,4,0,4,0,0,2,5,5,4,3,1,4,3,0,0,3,5,0,1,5,4,
                5,1,0,3,3,3,4,4,4,2,3,2,0,5,0,4,1,1,1,4,5,5,3,3,2,5,0,2,3,5,0,1,0,5,1,3,4,2,2,3,
                4,5,0,2,0,4,2,2,0,4,2,0,5,4,2,1,1,4,4,3,5,2,3,1,0,5,0,3,3,2,3,3,2,5,5,2,5,2,3,1,
                4,4,1,5,1,0,4,5,2,0,2,0,2,2,3,3,5,1,2,3,3,3,0,0,1,0,2,4,3,2,1,4,5,3,2,5,4,3,1,5,
                3,5,4,1,1,2,2,0,4,1,3,3,0,4,5,4,2,4,5,0,1,2,4,3,4,0,1,4,5,2,5,5,3,5,0,5,2,5,1,1,
                5,1,1,5,3,2,2,4,4,3,5,0,0,0,3,1,4,0,5,3,0,3,3,5,2,0,3,4,1,4,5,4,4,2,2,5,4,1,0,5,
                4,3,2,5,0,4,1,4,0,1,3,3,0,3,4,5,3,5,1,2,1,2,2,1,2,4,2,2,1,5,0,0,5,2,2,3,4,1,2,1,
                3,4,5,5,4,3,4,5,4,2,3,1,4,3,3,3,5,5,1,2,4,3,3,0,2,1,4,1,0,2,3,1,1,3,5,4,4,2,2,0,
                2,2,3,3,5,2,0,4,4,5,3,5,1,1,5,5,0,0,1,3,3,1,5,0,2,2,3,5,0,1,1,3,5,4,3,3,3,4,1,4,
                4,2,2,2,0,0,0,3,4,5,2,3,3,4,2,3,3,0,4,5,2,4,4,4,1,1,2,0,0,2,4,3,4,5,4,2,1,3,0,1,
                4,3,0,2,3,5,0,1,0,1,5,1,2,4,0,0,3,2,1,2,5,1,5,4,1,4,3,3,2,0,1,3,1,5,0,4,3,3,0,5,
                3,4,0,3,4,2,4,5,4,5,2,5,1,5,2,4,2,1,1,0,5,4,1,1,5,1,2,5,5,3,1,5,1,2,5,3,2,1,3,0,
                1,5,1,1,2,1,0,0,2,4,1,0,0,2,1,1,3,2,1,5,3,3,5,2,4,0,3,1,5,0,2,4,5,4,3,2,0,2,4,2,
                0,1,3,2,5,1,1,2,5,0,1,1,5,1,3,3,3,3,1,3,4,1,1,5,1,4,4,3,5,3,0,5,5,4,5,4,1,4,2,1,
                4,2,4,5,1,4,2,0,1,3,1,2,5,1,2,2,4,0,4,5,3,1,2,2,2,4,0,0,3,0,3,1,0,0,1,3,3,2,2,0,
                3,3,1,4,5,5,5,2,0,2,0,1,3,0,3,5,0,1,3,4,3,2,2,0,1,1,4,2,4,4,1,0,3,3,5,5,5,4,5,0,
                3,3,1,1,0,3,2,5,3,1,2,1,0,2,3,1,4,2,1,0,0,3,4,2,5,5,4,4,3,3,3,5,0,4,5,3,2,0,0,5,
                2,2,3,5,4,1,1,5,3,3,1,1,0,3,0,3,0,1,1,5,4,4,0,3,5,3,0,5,0,1,5,2,5,0,3,2,5,0,1,0,
                0,4,5,4,2,3,3,4,5,4,0,1,4,0,0,2,1,2,3,2,5,0,1,2,3,2,2,5,2,2,2,5,0,0,3,3,0,0,0,3,
                3,2,3,2,1,1,5,3,0,4,4,5,1,3,1,3,0,0,2,0,5,0,2,3,4,0,5,0,1,1,3,3,3,4,3,4,1,3,5,3,
                3,5,5,1,3,5,2,3,3,0,2,3,3,4,1,2,0,2,4,0,5,1,1,3,4,5,0,4,2,4,0,3,0,3,4,2,5,4,4,0,
                4,4,5,2,0,1,2,1,1,3,3,5,1,3,3,2,1,2,0,2,4,4,0,0,0,0,1,4,5,3,4,3,3,4,3,4,2,0,4,1,
                5,5,0,1,0,3,1,0,2,2,3,0,3,4,0,0,0,0,4,5,1,5,0,5,3,0,0,4,5,0,5,2,3,5,5,4,0,0,5,0,
                1,1,5,2,3,2,0,4,1,5,1,0,5,4,2,2,2,1,3,5,1,2,1,1,0,3,5,5,5,5,0,1,3,4,2,0,4,5,1,5,
                2,3,4,5,1,5,4,3,2,5,4,5,0,0,0,5,3,2,4,1,5,3,1,5,0,1,2,3,2,0,4,1,0,1,3,0,5,4,0,2,
                4,2,4,1,1,3,2,3,2,3,0,3,0,3,4,3,5,4,3,0,1,1,1,4,4,3,2,4,1,1,4,4,1,0,5,4,0,4,1,4,
                4,0,4,5,4,5,0,4,4,4,3,4,5,1,1,5,1,1,3,1,5,5,1,0,3,1,1,4,4,2,4,0,4,3,3,5,1,0,5,2,
                2,5,2,0,4,0,0,2,2,4,3,3,5,3,2,5,4,4,1,5,5,2,2,5,3,5,1,1,5,5,2,0,1,2,0,1,1,2,3,3,
                2,4,4,4,3,5,0,2,5,5,4,4,5,0,5,4,1,2,4,2,4,5,4,3,4,0,2,0,4,3,5,0,0,0,2,1,1,5,5,3,
                0,4,0,3,1,5,1,0,5,2,2,1,3,3,5,3,0,3,4,0,4,3,5,1,2,0,4,3,5,3,1,0,5,5,3,0,3,0,2,2,
                1,1,0,1,3,2,1,1,2,5,4,1,3,1,2,2,3,4,3,5,1,1,1,4,1,4,2,5,0,0,2,2,2,5,1,3,0,0,1,2,
                1,5,1,5,0,1,0,5,1,0,1,5,4,5,5,3,2,0,5,0,1,4,5,1,1,0,5,5,1,0,0,3,2,3,0,1,3,2,3,4,
                1,1,0,0,0,2,4,5,5,5,0,4,2,0,4,4,4,4,0,5,5,5,2,0,0,1,1,5,0,5,5,1,3,3,2,1,4,4,1,0,
        };
        int[] Tab1 ={2,0,0,1,1,2,4,5,3,4,1,3,2,3,4,0,5,4,1,1,1,1,4,0,1,2,3,5,0,4,2,1,0,3,2,5,5,0,0,0,
                4,2,5,3,1,5,3,1,2,1,3,1,0,2,1,5,2,3,0,5,2,3,0,1,4,0,3,0,3,4,5,0,3,0,1,5,2,4,2,0,
                1,4,3,0,2,1,2,0,0,0,5,0,5,4,1,1,2,4,2,4,5,5,5,2,0,1,0,3,5,3,4,5,5,4,1,1,1,1,3,0,
                0,2,1,3,0,5,0,5,5,3,2,4,4,2,2,2,5,5,4,0,2,2,2,1,4,3,1,4,3,5,2,3,4,2,1,0,0,3,1,0,
                3,0,1,3,1,4,4,2,5,4,5,2,3,0,4,2,3,4,3,5,0,3,3,5,0,0,1,5,5,2,4,0,5,4,1,1,2,5,1,3,
                1,3,3,3,0,4,4,3,4,3,0,2,4,1,0,5,4,5,2,1,3,1,1,0,5,5,0,0,0,2,2,3,1,4,2,1,3,1,1,4,
                0,2,4,1,3,3,4,4,1,5,1,3,5,2,4,1,1,4,5,1,0,5,3,4,5,4,1,0,2,0,4,5,0,0,4,0,3,5,3,4,
                1,0,5,5,2,0,4,5,1,1,2,3,0,3,0,1,3,1,0,2,1,3,1,1,5,1,5,2,3,4,5,3,4,0,3,0,5,3,5,4,
                5,3,0,0,5,0,5,1,1,3,4,0,1,4,1,2,5,4,1,1,4,5,2,0,5,5,4,1,5,2,3,2,3,2,0,2,2,5,4,2,
                0,2,0,5,4,4,0,3,4,2,1,2,0,5,4,5,0,5,3,0,4,3,4,2,1,1,5,2,2,1,2,0,2,2,1,0,4,5,1,1,
                1,3,4,4,2,1,4,0,3,4,4,5,3,5,3,3,0,2,0,5,4,5,1,4,5,5,0,5,4,5,0,1,0,3,3,1,4,0,2,4,
                5,2,2,5,3,5,2,1,1,5,0,0,2,0,0,0,4,1,3,4,1,1,0,4,1,2,1,2,1,2,0,4,2,3,2,1,2,2,4,4,
                4,0,3,1,2,3,0,3,5,2,5,1,5,1,4,5,3,0,5,5,0,0,0,4,0,5,0,1,0,3,2,0,5,5,2,4,1,3,2,1,
                3,5,5,1,1,0,2,0,3,1,4,4,4,5,0,4,0,4,3,3,0,1,2,0,1,4,0,2,0,1,5,2,2,0,0,3,2,2,4,5,
                2,3,0,5,0,4,1,1,0,1,0,4,0,1,0,3,5,1,3,0,4,0,5,1,2,4,1,0,1,3,1,0,5,0,0,2,2,3,5,5,
                2,0,2,3,0,1,3,1,5,0,4,1,0,4,3,2,1,3,0,3,4,2,5,3,3,0,5,1,0,1,0,4,4,4,2,4,4,4,5,2,
                2,1,1,1,0,3,5,2,5,5,4,2,4,3,4,2,3,2,4,1,3,2,0,5,1,3,1,2,0,0,2,5,3,1,4,2,4,2,3,0,
                3,3,5,3,0,0,4,1,4,1,4,5,0,1,1,3,0,2,4,2,2,0,2,3,5,4,2,2,5,3,4,0,2,3,3,0,4,0,5,5,
                4,1,1,0,0,1,4,2,0,5,3,2,4,0,3,2,4,5,2,4,2,0,2,2,0,4,2,4,3,2,1,2,1,1,4,0,0,3,0,2,
                1,2,5,1,2,5,3,0,0,4,4,2,0,5,2,4,3,3,2,2,2,0,2,0,3,1,0,3,2,2,5,4,4,5,1,3,4,1,1,1,
                3,2,4,0,2,5,1,3,5,0,0,3,3,2,3,0,2,2,4,3,1,4,1,0,0,5,2,0,2,5,5,0,4,5,4,2,4,2,2,2,
                2,5,0,3,1,1,4,2,3,2,3,3,2,5,4,3,1,3,1,4,0,1,1,0,5,2,1,3,5,4,4,5,1,2,5,0,5,5,3,4,
                4,2,5,4,3,2,5,3,5,1,0,5,1,4,1,1,0,0,4,3,4,4,1,0,4,1,4,1,5,2,0,2,5,4,1,5,2,5,4,0,
                3,5,0,3,1,5,3,5,2,3,1,5,3,3,2,4,0,2,3,5,4,3,5,2,5,0,0,3,2,0,2,1,4,2,4,1,0,3,2,3,
                3,5,2,3,5,2,5,1,5,5,3,5,1,0,1,4,2,2,1,0,1,0,5,1,2,3,3,4,3,5,2,0,1,2,4,1,4,0,4,3,
                5,3,3,1,5,2,4,2,3,4,3,2,1,3,5,2,1,0,2,0,5,4,1,2,5,4,1,5,3,0,3,5,3,5,2,5,4,5,4,1,
                2,0,2,1,0,0,5,2,1,5,3,5,2,1,1,3,1,3,1,0,5,5,1,2,1,5,5,0,5,2,5,2,5,0,3,0,0,4,0,4,
                2,4,1,3,5,5,2,5,5,3,5,2,2,0,4,0,5,4,1,3,3,1,2,4,4,2,5,0,1,0,0,2,0,4,5,4,5,1,5,5,
                0,4,3,5,4,2,3,3,5,1,3,2,4,5,5,0,3,2,1,4,5,4,4,1,3,5,0,0,5,5,4,2,0,0,2,3,0,5,5,2,
                3,2,1,5,5,3,1,5,4,4,4,0,2,2,0,5,0,0,3,0,4,2,3,0,3,0,2,3,0,0,1,1,2,0,0,3,5,2,3,3,
                5,1,3,4,4,1,5,0,1,5,2,5,2,2,4,5,1,5,4,4,2,5,4,3,1,3,1,5,0,3,0,1,1,1,0,4,4,0,1,1,
                1,3,0,1,5,4,5,4,1,5,3,5,5,1,0,3,0,3,3,0,3,3,3,5,5,3,2,0,5,4,5,5,4,4,3,2,2,2,1,1,
                1,5,3,4,0,2,4,1,1,4,1,0,3,3,0,5,1,1,4,2,1,4,4,2,4,4,3,4,2,3,3,0,5,3,5,1,3,5,0,5,
                0,2,5,3,0,2,0,2,3,2,4,1,5,5,4,2,3,3,0,5,0,5,3,0,4,5,3,1,1,4,2,5,3,0,0,2,1,3,5,0,
                4,4,0,1,1,4,3,4,3,3,2,1,3,0,3,0,0,2,1,5,5,3,2,0,5,4,1,2,1,3,5,4,2,2,5,1,2,3,4,4,
                3,0,4,3,1,5,5,2,4,4,5,4,3,3,1,0,2,4,2,3,4,3,4,5,4,5,3,0,3,0,5,2,0,0,2,1,3,4,4,2,
                4,0,3,0,2,4,1,5,0,4,4,3,5,5,5,1,2,1,3,0,0,2,4,3,0,3,3,2,4,1,0,1,5,0,0,1,3,4,0,5,
                0,5,1,4,3,0,0,2,0,0,0,4,2,3,3,2,1,3,4,5,4,5,0,3,4,4,2,2,3,2,0,4,2,1,4,4,3,2,1,2,
                3,4,3,4,0,2,2,5,5,2,1,0,3,1,4,1,0,0,4,2,1,5,3,0,3,2,3,1,3,4,2,4,3,5,5,4,3,3,5,2,
                2,4,3,2,4,3,0,4,3,4,3,3,3,3,2,5,3,4,1,2,4,2,5,4,2,5,4,4,2,5,4,3,5,2,0,1,5,1,3,2,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=75;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=42;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=34;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level9L(){
        int[] Tab0 = {0,5,4,3,0,3,1,0,1,2,3,1,0,4,2,0,1,4,3,5,3,2,0,3,0,5,4,3,3,4,2,1,4,0,0,2,2,1,3,4,
                4,4,5,0,5,0,1,3,0,0,4,0,5,3,5,4,3,5,5,1,3,3,2,3,4,4,3,1,2,4,4,2,5,0,4,3,5,2,0,2,
                2,2,1,2,3,4,0,4,1,3,1,1,2,1,3,1,0,1,1,0,3,1,1,3,2,0,2,4,4,0,1,5,5,5,4,2,0,1,2,2,
                0,1,0,2,1,0,4,4,0,2,2,1,2,4,0,4,2,0,5,5,0,3,3,5,3,5,3,4,2,1,1,1,4,2,1,2,5,2,3,1,
                2,3,2,5,0,3,0,5,1,1,2,0,0,0,3,5,2,0,0,4,5,1,1,2,3,2,2,1,3,4,0,5,2,4,1,1,2,5,2,3,
                3,4,5,5,0,4,1,5,5,5,0,2,2,1,3,2,3,1,1,2,2,1,0,3,1,4,1,5,5,1,3,5,3,5,0,0,1,5,0,0,
                4,4,0,1,3,2,2,5,2,0,1,2,4,1,1,0,1,4,0,4,0,5,1,2,3,0,0,0,0,3,3,2,3,4,5,1,2,5,1,3,
                3,4,2,5,4,5,2,0,4,4,3,4,3,5,1,5,5,0,3,5,2,1,4,2,0,1,1,3,2,4,2,5,0,3,0,4,3,5,1,4,
                0,2,1,1,1,2,2,3,1,3,1,0,1,0,0,3,5,3,2,1,5,1,4,1,1,1,1,0,0,3,2,4,3,4,3,4,1,5,4,5,
                5,1,1,5,4,2,2,0,2,1,2,0,2,5,2,4,0,5,0,3,2,1,4,5,0,5,0,5,2,2,4,2,1,1,3,2,2,0,2,2,
                0,5,0,4,3,1,4,2,1,5,4,2,2,5,0,5,1,4,2,5,1,2,1,1,1,0,1,4,4,5,5,4,5,0,3,1,3,5,3,0,
                3,5,2,1,3,5,4,1,0,1,5,2,0,4,3,1,4,2,4,1,5,5,2,5,1,1,2,5,3,3,0,4,4,3,0,3,5,3,3,2,
                3,2,1,0,0,0,5,1,2,3,2,2,2,0,2,3,4,4,1,2,1,2,0,1,2,3,5,0,3,1,4,5,0,4,0,3,0,5,4,2,
                3,0,1,0,4,5,3,0,4,3,2,4,0,1,3,4,5,1,2,1,0,3,5,4,3,2,0,1,5,5,5,5,3,1,5,1,5,5,4,1,
                2,0,5,1,5,3,1,2,5,1,3,0,2,2,1,1,5,2,1,4,2,0,2,2,1,4,3,5,0,0,2,4,2,0,1,5,1,4,1,0,
                3,1,2,3,2,0,1,1,4,0,2,4,3,4,3,4,5,2,4,0,4,2,5,5,4,4,2,4,5,0,3,1,1,5,1,4,5,0,2,5,
                4,4,3,5,5,3,2,1,5,2,1,4,3,2,0,3,4,0,4,0,0,5,1,4,1,1,0,3,0,5,3,2,4,1,4,1,3,1,4,0,
                5,0,4,5,2,1,5,0,4,2,0,5,1,1,1,0,5,5,2,0,0,1,0,5,5,0,2,1,2,1,3,3,5,0,2,3,5,3,1,1,
                1,2,4,2,4,2,0,0,3,2,5,1,4,1,5,2,1,1,4,5,3,0,5,3,2,1,0,4,0,1,1,5,5,5,4,3,2,1,3,3,
                4,2,4,0,5,1,1,5,5,4,3,4,2,4,3,1,1,5,2,2,2,0,4,5,3,0,2,5,4,0,3,2,4,5,3,5,1,5,5,3,
                3,2,1,2,4,1,0,4,0,3,0,0,2,0,5,2,2,5,3,0,4,4,4,3,5,2,4,1,3,1,2,5,1,0,3,0,2,2,5,1,
                0,1,2,5,4,5,0,5,3,3,1,5,3,4,1,1,3,4,0,0,4,1,3,2,4,4,4,2,3,4,3,1,4,1,0,5,0,4,0,3,
                4,0,4,4,3,2,5,2,0,5,5,3,3,5,4,0,0,2,2,1,2,2,4,3,0,4,3,1,2,2,3,1,4,0,3,3,5,0,4,3,
                2,5,1,4,5,2,0,2,0,2,0,4,1,4,2,4,5,1,2,5,5,2,2,0,5,4,2,4,3,3,1,4,4,0,4,2,0,5,5,5,
                5,1,3,3,3,0,0,3,2,2,5,2,0,2,0,5,0,0,4,0,3,2,5,4,0,5,1,3,3,4,2,0,2,0,4,2,0,3,5,3,
                5,3,4,4,5,0,5,1,2,5,5,0,2,3,2,5,2,5,0,2,3,1,2,5,4,1,5,5,2,4,2,5,2,3,1,0,0,1,2,5,
                3,3,3,4,0,1,1,3,0,2,3,4,3,4,1,0,1,0,0,0,3,5,4,3,3,4,4,2,3,4,2,2,4,0,2,0,0,1,3,3,
                0,0,3,3,2,1,1,2,4,3,0,3,1,1,5,2,0,1,4,2,3,1,2,4,4,1,4,5,2,1,5,4,5,4,2,5,4,0,2,5,
                3,1,3,2,5,2,2,2,2,2,1,2,5,4,2,4,1,3,2,4,4,5,3,5,5,4,2,2,4,2,0,3,0,3,3,4,1,5,4,4,
                3,5,5,2,1,2,5,4,1,0,1,3,3,2,0,1,2,0,0,2,3,4,4,2,3,5,0,2,2,3,3,0,1,5,1,0,5,4,4,5,
                5,2,5,4,3,1,3,3,4,2,0,3,0,4,5,4,4,4,4,4,3,4,4,5,4,5,4,0,2,3,1,1,4,0,5,5,3,0,2,0,
                3,0,4,5,2,4,2,4,2,0,5,5,0,4,5,5,5,5,1,0,2,3,3,3,2,0,2,1,1,2,2,4,0,0,1,4,4,0,3,0,
                1,1,5,2,4,0,3,2,4,3,4,2,2,4,1,0,2,4,5,4,3,4,5,3,3,4,5,2,2,2,1,4,1,2,1,1,3,1,5,1,
                0,0,2,4,5,3,3,3,4,5,2,3,5,0,4,0,1,5,4,2,3,2,1,5,0,3,2,5,1,5,5,0,4,2,4,1,5,1,0,3,
                5,0,5,0,2,2,0,3,3,2,2,5,0,1,0,1,4,3,1,2,3,5,3,5,1,1,2,1,5,0,3,5,3,5,4,2,4,1,4,5,
                4,4,0,1,3,1,2,5,0,2,3,0,4,3,3,5,0,4,4,3,4,1,1,2,1,0,4,5,4,0,3,2,4,2,2,1,2,3,1,5,
                0,4,0,5,2,2,1,5,5,0,4,3,1,5,4,3,4,0,2,3,5,3,3,5,2,1,2,3,0,4,3,4,4,4,0,3,3,1,5,1,
                1,5,4,2,1,2,0,3,4,4,1,0,2,1,1,2,2,3,3,3,1,2,5,0,4,1,3,1,3,0,4,3,4,1,3,2,5,2,1,1,
                3,1,1,4,5,5,5,4,3,3,5,2,2,4,5,0,4,3,3,1,0,3,4,2,0,3,1,4,3,4,4,2,0,4,3,5,3,1,0,4,
                0,3,0,2,2,4,1,5,3,0,4,0,2,1,3,4,1,3,5,5,0,5,3,4,4,4,0,1,4,1,3,5,5,5,3,4,4,2,4,4};
        int[] Tab3 ={0,1,4,3,3,4,5,4,2,0,4,3,0,1,4,0,5,2,2,2,5,4,1,3,0,3,0,0,3,4,2,4,0,3,3,5,1,4,5,3,
                4,0,2,1,5,4,4,4,5,1,4,3,2,5,2,1,5,1,5,3,0,3,5,2,5,1,5,5,3,5,2,4,4,0,2,1,3,3,5,1,
                1,0,1,0,1,0,2,3,1,2,2,5,1,0,3,1,3,3,2,3,3,2,0,1,5,2,5,2,0,5,0,4,1,3,1,2,3,4,2,5,
                0,1,4,4,3,2,3,0,3,4,2,5,1,3,1,5,4,4,4,0,1,2,4,5,3,2,1,4,1,0,3,1,0,0,1,4,4,4,0,5,
                0,2,2,4,1,3,0,0,3,1,5,2,5,3,5,3,4,0,5,3,1,2,0,5,3,5,2,5,5,4,3,2,4,5,3,0,1,5,5,0,
                2,3,3,1,4,3,1,3,4,2,5,5,5,5,2,3,2,4,1,5,3,1,4,1,1,1,3,2,4,1,4,2,3,2,4,0,1,2,5,0,
                2,0,3,1,2,2,0,1,1,3,1,5,4,0,4,0,1,3,1,5,4,2,2,4,3,3,2,2,3,1,4,5,2,0,2,2,3,1,1,0,
                2,0,2,3,1,3,1,4,1,3,0,1,0,1,5,3,1,4,1,2,5,1,0,0,0,4,2,5,1,3,4,2,5,4,4,5,0,1,1,0,
                2,5,2,1,3,0,4,5,1,3,5,0,0,2,3,0,1,4,2,5,4,1,0,3,5,2,5,5,5,5,1,4,4,4,0,2,5,4,0,2,
                1,1,5,1,4,5,2,3,3,5,3,3,4,5,0,5,3,4,1,0,3,2,5,1,2,2,4,3,3,2,0,2,0,2,3,4,1,1,0,3,
                5,1,3,2,2,4,5,2,5,4,0,4,1,2,5,0,4,5,2,3,4,3,2,4,5,2,3,2,1,4,2,4,4,3,1,2,1,1,4,1,
                4,2,2,4,2,1,0,3,4,4,2,5,0,4,0,2,4,0,5,4,5,1,2,1,5,5,2,2,1,1,4,4,2,1,3,1,2,2,3,3,
                1,1,0,2,3,5,5,5,5,0,0,2,3,3,1,0,5,1,0,5,2,3,5,5,1,5,5,0,3,3,5,1,1,2,0,2,2,0,2,2,
                0,4,0,5,2,4,1,2,3,2,5,1,5,5,2,4,4,4,2,4,0,4,5,3,1,2,4,2,4,2,0,2,2,4,3,4,4,3,5,2,
                1,2,3,5,3,1,0,3,1,5,4,1,4,5,2,3,4,1,4,1,1,4,2,3,2,0,5,0,0,3,3,1,1,5,2,3,3,2,2,5,
                1,2,0,0,2,5,1,5,1,3,4,1,3,3,0,4,4,5,2,0,3,5,4,0,1,5,0,5,3,5,0,4,3,1,4,3,2,5,4,5,
                1,3,0,5,3,5,4,1,0,2,1,0,1,4,5,1,5,4,3,4,1,5,1,0,2,0,3,4,2,4,3,4,3,5,4,3,2,3,3,4,
                5,3,2,3,1,4,1,3,3,5,0,5,4,5,1,1,3,2,5,1,5,0,3,4,2,5,2,2,2,4,4,5,4,0,0,2,1,5,0,4,
                3,1,4,2,1,1,5,1,3,5,1,5,4,0,4,3,5,0,1,0,0,0,2,2,2,5,0,2,1,0,0,2,0,2,1,1,0,3,2,1,
                2,1,5,0,3,3,4,5,5,0,1,3,5,0,5,4,2,0,4,4,2,4,4,4,0,2,4,2,3,2,5,5,4,3,4,2,0,3,0,3,
                2,3,5,2,1,5,0,3,1,4,5,3,5,0,3,2,5,1,3,2,4,2,1,1,3,1,3,5,0,3,2,1,5,3,3,5,0,0,2,1,
                1,0,2,5,5,3,3,5,2,5,2,0,1,3,4,3,1,4,0,3,4,4,5,5,5,2,4,4,1,1,2,2,1,0,3,5,2,1,4,4,
                5,3,4,4,2,5,3,1,0,4,1,5,0,3,0,3,3,3,4,2,5,5,4,4,5,0,4,0,2,3,3,0,0,4,1,0,0,1,4,1,
                4,5,0,5,5,4,0,1,2,3,4,4,0,1,2,4,3,2,1,3,5,5,1,2,1,5,2,2,4,2,0,1,0,3,5,0,2,3,2,0,
                5,2,1,0,5,1,1,2,3,2,2,3,5,0,1,0,2,0,1,1,2,5,4,2,4,0,4,5,1,4,3,3,5,4,3,0,1,3,0,0,
                4,5,5,0,4,1,1,5,5,0,2,2,4,2,3,4,0,3,1,5,0,2,2,0,3,5,2,5,4,4,0,2,1,3,4,3,3,2,4,5,
                5,2,1,3,2,0,3,3,5,5,5,5,4,1,4,2,2,3,0,5,0,0,4,4,0,2,0,3,4,0,4,4,4,2,4,2,2,4,1,0,
                5,2,1,2,4,5,0,1,2,1,3,1,2,5,5,4,2,3,0,3,4,0,3,2,2,2,5,2,2,2,4,0,0,3,1,4,0,1,5,2,
                2,0,4,2,5,3,2,4,3,2,2,2,4,5,0,1,5,3,4,3,3,1,3,1,0,3,0,0,5,2,4,1,2,5,2,4,3,4,1,1,
                3,5,3,1,4,3,5,2,2,1,3,0,2,2,4,4,5,3,2,5,0,3,0,3,2,4,1,0,1,2,0,3,5,5,1,4,2,5,4,5,
                2,2,3,2,3,5,1,3,3,3,2,1,1,0,2,3,3,5,3,5,2,2,4,4,1,4,3,0,3,2,2,2,3,4,0,5,2,5,3,3,
                0,5,4,2,2,2,2,5,0,4,2,0,0,3,3,1,4,4,5,2,0,2,0,3,2,5,3,2,2,5,5,4,2,1,3,5,4,4,3,3,
                1,2,3,2,5,5,4,2,0,4,4,0,2,2,5,4,3,4,0,4,0,3,4,5,5,2,5,2,2,0,5,4,0,4,0,1,0,3,3,5,
                1,4,0,5,3,5,0,0,2,4,2,4,1,4,3,1,3,3,0,2,2,1,2,4,0,1,4,4,4,3,5,0,4,4,1,5,3,5,4,1,
                2,2,5,5,1,1,4,4,0,3,4,0,3,0,5,5,1,5,1,5,1,4,2,1,2,5,5,5,5,5,4,5,3,4,4,0,4,3,3,2,
                5,2,3,4,1,4,4,1,1,2,4,4,3,5,5,2,5,5,0,5,0,2,2,2,0,2,0,5,5,0,1,0,4,5,3,1,3,3,1,2,
                1,4,5,5,1,4,5,1,5,2,2,3,4,5,5,2,4,4,5,1,5,1,5,4,2,3,2,2,0,1,5,2,2,0,2,0,4,2,1,1,
                1,3,5,0,3,2,5,5,5,3,4,0,4,3,2,2,1,2,0,0,0,5,5,2,0,4,1,0,2,2,0,0,5,4,5,2,5,1,5,2,
                5,2,2,2,0,2,0,0,1,0,3,4,3,2,2,0,3,2,0,5,4,3,1,0,2,3,2,2,0,4,5,1,0,4,5,5,0,5,2,4,
                1,1,3,1,0,5,3,1,1,5,3,0,1,2,5,5,0,0,0,4,4,4,2,0,3,5,1,0,3,2,4,0,1,5,0,0,2,5,5,2,
        };
        int[] Tab2 ={5,3,1,4,3,4,1,2,5,0,3,4,3,2,2,0,3,1,4,3,4,0,2,0,3,0,1,5,0,3,2,1,3,1,5,1,2,2,4,2,
                1,3,2,5,0,1,0,4,4,0,2,5,1,1,1,2,1,2,1,4,4,1,1,2,3,0,3,3,4,4,3,0,3,2,3,3,5,0,2,4,
                5,5,0,0,3,4,4,5,5,0,5,2,0,2,2,1,3,3,4,2,5,1,1,2,4,0,4,3,3,3,1,1,2,3,2,5,1,5,5,2,
                5,4,4,1,1,1,4,2,1,2,1,1,4,0,2,1,0,1,1,3,0,1,1,5,5,4,1,1,2,5,0,5,5,5,0,3,3,1,1,2,
                4,2,3,5,1,5,0,5,3,3,0,0,5,3,2,0,5,3,1,2,2,2,3,4,5,1,1,3,3,3,5,3,4,1,3,1,0,4,5,2,
                0,2,0,0,4,2,2,0,0,0,3,3,3,3,2,4,4,0,4,0,0,0,4,4,1,2,3,5,2,0,5,1,5,2,5,5,0,3,5,4,
                2,5,4,2,5,3,5,5,1,4,4,0,5,3,4,5,5,4,0,1,2,4,1,3,0,3,0,3,0,3,5,1,4,2,2,4,5,3,5,1,
                5,3,1,1,4,0,1,2,3,1,1,4,0,3,1,2,0,1,4,4,2,0,4,1,2,5,2,2,1,4,0,2,2,1,5,5,3,2,2,5,
                1,5,3,5,5,5,3,4,1,4,4,2,1,4,3,0,4,2,4,5,3,1,0,2,2,4,0,0,2,5,1,2,3,4,5,5,2,1,5,0,
                5,2,5,5,2,1,4,4,3,3,4,0,4,1,4,5,5,1,1,4,2,2,4,3,2,0,0,0,0,3,1,3,5,0,1,5,4,3,3,0,
                3,0,1,3,3,5,1,0,0,2,3,2,1,5,1,2,2,3,4,2,4,1,2,3,5,2,5,4,1,5,1,5,0,3,2,2,1,0,3,5,
                4,4,4,5,5,5,4,1,0,5,4,4,0,2,2,4,1,3,5,1,0,0,5,3,5,0,3,3,3,5,1,0,0,3,2,3,5,3,0,4,
                0,3,5,2,0,0,0,3,4,1,5,2,2,5,1,4,4,1,5,3,1,1,5,0,2,0,2,2,4,2,1,3,2,4,1,3,1,5,1,2,
                0,3,1,0,5,5,4,5,1,2,3,4,5,0,5,3,4,1,0,4,5,1,3,4,0,4,1,4,4,0,4,5,1,3,2,4,2,5,2,5,
                5,2,0,1,2,3,2,1,0,5,5,5,4,1,2,0,0,4,1,1,2,2,1,5,4,2,2,0,2,5,0,2,0,3,5,3,3,0,0,1,
                4,3,2,3,0,1,0,5,1,3,2,5,0,0,3,3,3,3,5,0,1,2,0,4,2,4,5,4,1,0,0,0,5,3,5,3,2,2,2,5,
                2,5,4,0,2,3,0,3,2,4,1,5,0,3,5,3,1,5,0,4,3,5,3,2,4,0,3,3,2,3,2,3,3,3,3,5,5,0,1,1,
                5,1,0,2,0,1,2,4,1,1,1,0,5,2,2,3,2,1,3,0,5,3,4,5,1,1,2,5,0,2,1,1,2,0,5,1,5,2,3,2,
                5,4,3,5,0,0,4,0,1,2,2,1,4,3,0,1,3,1,3,4,1,5,0,2,0,1,0,5,2,4,4,1,2,5,1,1,1,0,3,2,
                3,1,5,2,4,2,0,5,2,1,5,2,3,4,3,5,0,0,0,1,2,2,0,1,0,1,0,1,2,3,2,5,0,5,5,4,5,3,5,1,
                2,5,0,4,4,3,3,2,0,3,4,1,2,5,0,1,3,2,5,0,4,4,3,5,5,5,4,0,0,4,1,5,1,3,5,3,5,5,2,3,
                0,4,3,2,2,4,2,2,1,2,0,2,4,0,2,3,3,3,3,4,5,4,4,3,1,3,5,1,1,4,4,3,0,5,0,0,0,2,1,2,
                0,1,2,5,4,0,1,1,5,5,3,2,1,4,4,2,5,1,0,1,0,1,4,5,0,3,3,5,0,0,2,3,5,3,0,0,1,1,4,5,
                3,1,1,5,1,1,1,3,1,3,3,5,0,2,3,3,1,1,3,2,1,5,5,3,1,2,2,0,4,4,2,2,5,0,2,3,5,5,5,3,
                5,3,3,0,5,5,1,3,1,1,4,2,5,3,3,3,0,4,0,5,1,0,1,3,3,4,2,3,3,3,4,1,0,5,1,1,3,5,5,5,
                4,2,2,4,4,0,2,4,0,2,5,0,5,1,1,4,2,0,2,2,1,1,5,4,4,5,4,4,4,4,3,4,1,2,3,2,5,1,1,0,
                3,1,3,5,3,3,2,1,2,2,1,2,3,2,0,2,1,1,2,4,2,2,4,3,2,2,3,4,1,4,1,3,5,1,3,3,3,1,2,0,
                5,5,2,4,3,3,3,0,4,3,4,5,4,2,4,2,2,4,5,5,3,0,1,4,2,5,0,3,4,3,0,0,2,5,4,4,1,1,5,4,
                3,0,4,5,1,1,0,5,3,5,1,0,2,5,5,5,3,1,2,5,5,0,4,2,5,0,4,5,3,5,4,4,4,2,4,1,1,5,3,1,
                3,5,4,5,2,4,4,2,1,0,2,1,4,5,2,0,0,4,2,4,3,1,5,5,0,0,2,0,3,4,3,4,3,2,1,5,5,5,0,0,
                4,0,3,2,0,2,0,4,3,5,5,1,5,3,5,5,1,1,0,3,1,1,0,4,3,4,0,1,0,3,3,2,4,4,2,3,4,2,2,5,
                3,1,0,4,0,0,5,5,5,3,1,2,0,4,5,4,3,1,2,3,1,3,5,2,1,2,2,0,0,3,1,4,5,4,4,0,3,2,2,2,
                5,1,5,3,5,1,2,5,5,5,2,5,4,0,4,4,0,4,0,4,3,2,1,3,0,2,2,0,2,5,2,0,1,0,3,1,5,1,4,3,
                5,4,1,2,1,3,1,3,3,4,1,1,2,4,5,0,0,1,0,4,4,4,5,5,3,0,4,5,4,0,1,2,4,2,0,3,3,3,2,5,
                0,0,1,2,0,0,0,1,2,1,3,5,3,5,4,3,3,1,2,0,2,1,3,1,5,5,0,4,5,3,3,1,4,0,3,4,0,1,1,1,
                1,0,0,4,2,1,2,1,1,3,2,4,5,2,2,0,4,2,2,1,2,0,4,0,3,0,0,3,2,4,3,3,3,2,2,1,0,2,0,1,
                5,0,0,5,0,4,1,0,3,5,2,5,4,5,5,2,3,2,4,4,0,2,0,1,0,4,4,2,0,0,4,3,5,1,1,0,1,3,3,4,
                4,3,1,1,2,1,5,3,4,2,2,3,5,5,1,5,3,4,0,4,4,2,5,3,0,1,1,5,5,2,2,4,5,2,4,2,5,5,4,5,
                3,0,4,0,4,4,4,0,2,3,3,2,5,2,1,1,5,4,4,5,4,0,0,0,1,1,5,0,3,3,2,3,1,1,3,3,4,1,5,3,
                1,1,2,0,2,4,4,5,5,1,3,1,3,0,5,4,2,4,3,1,5,2,3,5,5,3,5,2,0,1,5,2,0,5,0,3,5,3,2,2,
        };
        int[] Tab1 ={0,3,0,3,0,2,1,0,2,3,1,5,4,0,3,2,3,1,0,3,2,4,5,4,5,3,4,3,2,2,1,4,2,4,5,0,4,0,3,2,
                4,3,5,0,1,4,1,4,5,4,4,4,4,3,1,4,4,0,1,0,2,4,0,4,1,4,5,4,2,3,1,4,1,2,5,3,4,2,1,1,
                3,3,4,4,5,1,0,4,4,0,3,3,2,1,3,0,4,0,1,1,1,2,5,0,3,4,1,5,0,0,3,4,4,5,5,1,5,5,2,5,
                3,1,3,4,2,5,0,3,4,4,1,1,3,1,1,2,1,5,2,5,0,4,1,1,5,3,4,3,2,1,3,4,2,3,0,4,4,3,3,2,
                0,4,0,0,0,0,5,5,3,5,4,5,0,5,0,5,2,2,4,0,2,3,1,0,0,4,4,5,3,1,2,4,1,2,5,0,4,5,1,5,
                5,4,4,3,2,0,3,1,2,4,4,0,4,0,5,1,5,4,4,3,4,1,2,2,3,1,3,2,4,2,5,2,3,4,5,5,4,5,2,4,
                0,3,0,0,1,4,2,4,4,3,2,2,5,0,1,2,4,4,3,4,5,0,4,4,4,3,0,1,3,2,4,5,4,0,2,4,3,1,4,5,
                1,5,3,0,4,0,1,5,2,2,4,0,5,0,4,0,3,5,0,1,0,5,5,2,4,4,1,4,2,5,0,0,2,0,2,3,5,3,5,2,
                0,1,5,2,1,0,2,0,1,0,2,3,3,2,1,2,3,4,5,3,3,3,3,3,3,1,2,1,5,0,3,4,2,4,5,4,3,2,4,3,
                1,4,1,2,2,1,2,1,2,1,3,0,2,5,3,4,0,2,5,1,1,1,0,4,5,4,3,5,3,2,2,2,5,3,2,2,4,5,2,5,
                1,3,4,5,3,3,1,2,3,5,2,5,3,2,0,0,0,0,2,5,0,4,5,2,4,0,2,5,5,1,0,2,3,4,2,3,5,0,5,5,
                4,1,2,5,4,2,5,1,3,0,5,2,4,5,1,4,4,2,4,1,3,5,2,0,3,1,2,0,5,2,3,4,3,5,0,2,4,1,3,4,
                2,4,5,0,0,3,0,0,4,3,1,4,3,1,1,4,3,5,2,2,3,1,2,2,2,3,3,4,1,1,0,3,0,2,5,1,1,1,2,1,
                1,4,1,5,4,1,0,4,3,5,5,4,1,2,0,5,0,2,2,2,4,3,4,5,0,0,1,1,5,1,4,4,5,3,1,4,2,2,5,5,
                4,0,2,2,0,5,0,2,1,3,1,1,1,5,3,0,4,5,2,5,4,5,5,2,1,3,5,3,5,2,1,4,3,0,4,1,3,0,4,5,
                4,5,0,3,4,5,3,1,5,0,2,5,0,0,1,0,3,5,4,4,2,5,0,3,0,2,0,0,2,4,2,4,4,4,3,4,2,5,5,0,
                4,1,4,3,2,0,0,2,0,4,4,1,4,4,2,3,3,3,5,0,5,1,0,4,1,5,2,3,0,3,1,4,5,1,4,4,2,1,0,1,
                1,1,2,0,1,5,1,4,1,5,0,4,5,5,3,0,4,3,2,2,0,4,2,4,4,0,3,1,0,1,1,0,3,4,0,4,1,4,3,2,
                3,3,3,2,4,5,1,0,1,4,0,3,0,3,2,0,4,5,4,0,2,4,4,2,1,5,0,5,3,1,3,1,3,4,5,1,0,5,3,5,
                0,1,1,0,1,4,1,1,4,1,4,4,2,3,2,2,3,3,3,3,5,3,5,3,2,0,2,3,4,1,3,2,5,1,3,1,4,3,4,0,
                2,1,2,1,2,1,5,1,3,5,0,5,3,2,0,5,5,2,5,0,4,2,1,2,4,5,3,2,5,4,3,2,1,4,5,1,5,2,0,1,
                3,4,2,0,2,4,3,1,4,3,2,0,4,4,5,0,3,3,1,3,4,3,3,0,2,2,0,0,5,4,3,3,0,1,2,0,3,0,4,4,
                4,1,4,4,4,5,0,5,5,1,2,3,5,1,3,0,3,5,0,3,0,5,1,4,2,2,5,0,4,0,2,1,1,1,3,2,0,1,2,5,
                3,2,2,2,3,5,5,3,2,2,4,2,3,1,4,5,0,2,0,4,5,3,2,1,5,2,0,1,0,4,0,1,5,2,0,4,5,1,5,0,
                4,4,5,5,1,5,1,1,0,2,3,3,2,5,0,3,4,2,5,0,3,1,1,4,3,3,3,2,5,1,3,0,0,0,2,0,4,5,0,4,
                1,4,0,3,4,0,2,5,0,4,3,2,3,5,1,5,1,3,2,1,3,3,3,4,2,3,4,3,0,5,3,5,2,0,4,1,4,0,2,4,
                3,5,5,2,4,0,5,3,5,5,3,5,2,5,1,5,2,1,2,5,5,2,2,5,1,5,2,0,2,3,2,1,0,3,2,0,5,4,2,1,
                3,4,2,3,2,0,3,2,1,3,2,4,2,4,5,3,1,1,3,0,4,2,1,1,0,3,1,1,2,1,0,3,4,3,2,1,3,0,2,5,
                2,4,3,2,4,4,0,4,4,4,3,5,3,4,4,5,5,0,3,5,3,3,5,2,3,2,2,0,0,5,1,1,2,4,2,3,5,0,3,0,
                3,0,1,5,5,3,5,1,1,5,5,2,3,5,4,3,0,4,4,4,2,4,2,3,3,2,0,5,3,0,2,0,2,5,4,4,2,0,1,2,
                0,2,0,4,3,0,4,2,5,4,2,4,3,5,4,3,1,1,3,2,1,5,5,2,5,3,1,5,4,0,5,5,4,4,3,2,2,3,1,1,
                4,4,1,3,3,4,4,4,3,3,2,3,1,1,0,0,0,2,0,0,3,0,5,1,1,2,4,0,5,4,5,3,5,1,2,1,1,2,2,4,
                0,3,3,4,1,4,3,0,4,2,3,1,1,4,2,5,2,2,4,2,0,5,4,0,2,2,0,4,5,5,3,1,3,2,5,1,0,0,4,2,
                0,3,1,2,5,2,0,1,4,3,3,0,1,0,3,3,2,3,1,5,2,5,1,4,4,4,1,2,5,1,2,3,1,2,5,4,5,2,1,0,
                3,2,2,4,0,2,1,3,5,3,2,5,5,1,4,4,1,4,0,5,0,0,3,3,2,2,4,3,1,1,1,2,1,3,3,1,5,2,4,2,
                4,3,5,4,3,1,0,3,3,5,5,1,3,2,4,3,1,1,1,1,1,0,3,5,5,5,3,1,3,5,3,3,2,2,3,5,5,3,1,0,
                2,5,0,4,5,2,5,2,5,2,0,4,2,0,1,1,2,1,0,0,0,3,2,1,1,0,0,4,0,2,5,4,1,5,3,0,1,2,1,4,
                0,1,3,4,5,5,2,0,3,5,1,4,2,0,5,1,0,3,3,4,3,0,4,2,5,0,5,5,3,2,4,4,3,5,3,2,4,4,2,3,
                2,2,2,1,0,0,2,4,3,1,1,5,3,5,0,3,2,2,0,1,5,0,2,4,5,5,3,5,4,0,1,4,5,1,2,5,5,0,1,5,
                4,1,3,2,5,2,3,4,5,2,0,2,0,3,3,4,4,3,4,5,3,1,4,0,1,3,0,0,1,4,2,1,1,1,4,4,1,1,5,5,
        };
        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=73;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=46;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=37;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }
    private void level10L(){
        int[] Tab0 = {2,0,4,1,2,4,2,2,5,5,1,0,1,5,0,2,2,2,4,1,2,2,0,1,1,2,0,1,4,0,3,3,2,3,1,0,1,2,0,3,
                3,5,4,5,2,0,5,3,2,3,5,5,0,2,2,1,4,5,3,1,1,2,3,2,5,4,0,2,2,2,1,3,2,2,0,2,3,2,4,3,
                4,3,2,1,4,3,3,4,2,5,4,4,4,3,4,1,0,1,2,0,3,4,4,2,5,5,2,1,2,5,3,4,4,0,2,4,3,3,4,2,
                3,4,0,5,5,1,3,0,4,2,2,5,5,0,3,4,2,5,5,3,0,1,2,2,2,1,1,0,4,2,2,5,3,4,3,5,3,2,1,1,
                1,5,5,0,1,2,2,5,0,2,3,2,4,2,4,1,2,0,0,0,2,2,4,1,0,2,0,1,1,2,3,0,0,2,2,4,4,3,1,2,
                2,2,5,5,2,5,3,5,1,3,1,4,4,3,3,0,3,3,2,4,2,4,1,5,0,4,5,2,2,4,1,3,5,4,0,0,0,4,3,4,
                5,2,3,5,0,1,3,1,4,4,1,2,3,5,1,4,4,2,0,1,4,1,4,3,4,2,1,2,3,5,2,2,3,5,5,1,1,0,0,4,
                0,3,0,3,2,1,1,1,5,1,2,2,2,0,4,4,5,5,1,0,1,4,0,5,4,0,0,3,5,3,0,2,4,0,2,2,0,1,1,2,
                3,1,0,4,0,4,4,0,3,4,2,5,2,3,4,5,3,0,1,2,4,4,2,1,5,0,3,2,3,3,3,1,5,2,1,3,4,5,0,2,
                5,5,4,1,4,4,5,1,3,4,2,5,4,5,5,2,0,2,3,4,3,3,5,4,2,3,4,2,0,3,0,2,4,5,2,2,3,4,3,1,
                4,1,2,1,1,1,0,5,1,4,4,4,4,2,2,4,0,4,1,3,5,2,3,1,5,1,3,1,1,5,2,2,1,5,1,5,0,1,5,4,
                5,0,1,5,0,2,0,5,4,2,3,3,1,4,3,4,2,5,0,1,2,3,2,0,3,2,4,0,5,3,4,1,1,3,4,4,2,4,3,0,
                4,0,4,0,2,1,0,3,4,1,4,0,1,3,3,4,1,1,4,3,0,5,1,5,5,0,4,4,4,2,2,0,3,4,3,5,2,5,5,4,
                3,0,3,2,0,1,0,0,2,2,5,5,1,5,3,1,4,5,3,3,4,0,1,1,5,5,1,3,1,4,4,3,1,5,1,2,5,5,5,2,
                4,4,1,4,2,4,4,1,2,5,0,2,5,5,0,4,2,1,0,5,0,0,3,0,4,4,1,3,3,1,2,4,2,0,4,3,0,5,0,5,
                4,1,4,3,4,5,4,4,5,5,0,2,3,5,0,3,0,2,1,4,3,3,0,3,3,4,2,4,5,2,0,4,0,1,5,1,5,1,5,1,
                4,4,3,3,2,1,0,2,0,3,5,3,5,3,5,1,4,3,4,2,5,3,2,5,2,3,4,1,1,0,1,3,2,2,1,5,5,5,5,2,
                2,1,1,1,2,4,2,1,0,5,5,1,4,1,0,2,1,0,2,4,4,2,5,4,1,5,5,3,4,1,1,4,1,2,2,0,2,4,3,5,
                1,2,0,4,0,1,5,5,1,3,0,1,2,1,5,2,4,2,4,0,1,1,5,2,2,2,1,2,3,1,2,0,0,1,3,5,2,3,1,2,
                1,2,0,2,3,5,3,1,2,2,3,3,4,0,1,5,0,1,5,2,0,4,2,4,0,1,5,1,4,3,4,0,5,2,0,4,2,5,4,3,
                3,1,4,1,2,1,4,4,3,3,2,2,3,3,3,0,0,5,3,2,0,3,1,1,4,2,0,5,1,4,0,3,4,5,4,1,5,0,2,5,
                2,2,3,4,5,4,1,0,5,5,3,3,0,4,4,0,1,4,5,2,4,4,1,0,2,5,0,4,1,5,5,5,1,2,3,5,3,2,0,4,
                2,3,2,5,2,3,5,1,2,0,1,0,5,5,3,4,0,2,1,0,3,1,5,0,2,4,0,4,2,0,0,2,2,1,5,5,2,0,0,3,
                2,3,0,0,4,2,5,4,3,1,1,2,5,4,4,4,3,2,3,0,3,4,0,3,5,1,1,0,3,1,0,0,0,0,1,2,4,5,5,0,
                2,4,2,4,3,2,3,1,3,1,1,4,0,2,4,5,2,3,0,5,5,3,5,2,1,3,4,5,1,4,1,1,5,5,4,2,1,0,0,5,
                4,0,2,1,2,5,2,5,4,2,2,1,4,2,3,1,0,1,5,2,0,0,4,3,3,4,2,3,5,3,2,4,2,5,1,0,5,2,5,2,
                3,0,3,4,1,2,1,5,1,4,2,2,4,0,4,3,5,5,4,3,3,5,5,4,2,2,1,3,3,5,4,4,0,2,4,3,3,0,1,4,
                1,3,2,5,0,2,4,1,2,0,4,0,3,4,3,5,5,2,5,1,2,0,5,0,4,5,1,0,0,3,3,2,2,3,4,1,5,4,0,3,
                3,2,3,5,2,5,1,5,1,4,3,2,1,0,1,5,4,1,4,2,3,1,2,5,0,4,1,5,1,3,0,1,0,3,2,3,0,2,5,2,
                0,4,1,1,2,3,3,3,3,3,0,3,0,4,4,3,1,1,1,0,1,0,4,1,4,0,5,5,5,4,1,0,4,3,5,4,5,5,2,4,
                0,1,2,2,5,5,1,4,3,0,3,5,5,3,4,5,2,4,3,2,3,3,1,5,4,0,2,2,2,5,4,0,2,3,3,3,0,5,2,4,
                1,5,1,5,1,0,1,3,1,1,3,1,2,5,0,1,5,1,2,1,5,2,3,5,1,1,1,5,0,0,1,4,1,3,4,2,2,0,1,3,
                2,5,4,1,3,5,4,4,1,4,3,2,1,2,1,1,2,3,1,4,3,2,4,0,0,5,5,2,3,1,1,1,2,3,5,4,4,1,1,5,
                4,4,4,1,1,1,3,0,2,5,0,1,3,2,1,3,3,5,2,0,0,3,1,1,3,4,2,3,3,0,0,4,3,2,2,1,5,5,1,3,
                2,0,0,1,4,4,1,5,5,2,1,3,2,5,1,3,4,5,2,4,4,5,1,4,0,2,4,3,0,1,1,1,5,3,4,3,0,0,5,1,
                1,3,4,0,5,1,0,2,4,4,1,0,5,2,4,2,1,5,0,0,1,4,4,4,5,1,3,4,2,1,0,3,5,0,3,3,0,3,4,0,
                4,1,4,3,5,5,2,4,4,0,1,5,0,2,0,5,3,2,0,5,2,2,4,1,2,2,4,5,5,3,3,4,1,5,2,4,4,2,1,5,
                4,1,0,5,4,5,5,4,2,0,3,5,2,1,0,3,1,1,1,5,3,3,5,2,1,5,5,5,0,4,3,4,5,4,1,3,4,4,1,5,
                1,0,2,1,0,2,3,2,0,1,5,4,3,3,3,3,3,1,0,1,3,0,3,3,0,0,5,5,2,0,5,1,5,4,2,0,0,3,3,1,
                2,2,5,2,1,0,3,5,2,3,4,0,3,2,5,0,4,0,1,3,2,5,4,4,4,3,5,3,4,5,4,0,1,2,1,1,5,4,1,2};

        int[] Tab3 ={0,3,4,5,5,5,0,0,0,1,3,4,2,0,4,0,5,1,1,2,2,5,1,5,2,0,4,5,1,0,2,4,1,0,5,0,3,2,4,4,
                0,2,5,2,3,4,1,2,3,1,3,5,2,5,4,1,1,2,1,4,2,3,4,0,2,2,3,1,0,5,4,1,4,5,1,4,0,1,3,2,
                0,1,1,2,2,2,1,1,2,2,2,1,1,5,1,2,4,0,3,2,2,0,1,1,1,1,5,3,3,3,1,0,5,3,4,2,2,5,0,0,
                4,5,2,5,4,1,1,3,4,5,0,1,4,1,2,2,0,5,5,2,0,0,1,3,5,5,5,2,4,0,1,0,5,3,0,3,3,3,5,1,
                1,4,4,5,5,5,0,3,0,0,3,0,4,5,3,3,0,5,0,3,5,5,1,1,4,0,1,1,1,5,5,1,2,5,3,0,5,3,2,2,
                0,2,3,4,1,5,5,3,1,5,4,0,3,3,5,3,2,4,3,4,2,0,5,3,3,2,5,3,3,5,5,2,2,1,5,1,4,2,5,3,
                5,2,2,4,0,0,4,2,1,3,2,1,0,2,4,4,3,0,0,1,1,3,4,5,1,2,2,0,3,2,4,4,0,2,3,0,0,4,4,0,
                3,3,3,3,5,1,4,0,5,4,3,5,2,4,2,1,0,4,0,3,4,3,4,0,1,2,0,5,5,0,4,1,1,1,4,4,3,4,3,2,
                4,1,3,3,0,3,5,4,2,3,3,3,3,4,4,1,5,1,5,3,2,3,3,5,1,3,5,2,2,0,0,4,1,1,4,1,0,4,5,0,
                4,5,0,2,5,4,0,5,5,1,4,5,1,3,1,2,4,4,2,4,3,3,5,2,2,4,3,2,1,5,5,1,4,2,0,1,4,3,2,0,
                0,5,0,5,4,4,2,4,1,5,0,1,0,5,0,2,4,5,2,1,1,4,3,0,5,2,3,0,1,4,5,0,1,1,4,4,5,5,2,3,
                2,0,4,2,1,0,2,2,2,3,4,0,4,0,5,5,0,5,2,2,0,5,3,5,5,5,5,2,0,0,5,3,0,4,5,0,0,1,5,0,
                1,2,0,5,3,4,0,5,2,4,2,3,5,0,0,3,4,3,3,2,0,2,0,0,1,1,1,5,3,1,1,0,4,2,3,5,4,2,3,2,
                4,0,0,2,1,0,4,0,1,5,1,2,5,4,3,3,1,3,1,3,3,4,5,4,0,4,2,3,3,0,5,2,4,3,0,5,4,1,1,4,
                1,5,2,0,2,4,3,4,3,4,0,3,2,4,1,2,1,3,1,4,0,5,0,4,5,3,3,5,2,2,2,5,5,4,2,3,2,4,2,2,
                2,4,4,2,2,2,5,1,4,5,2,0,3,2,3,2,2,4,0,3,5,3,2,4,0,5,2,5,0,4,4,4,2,1,0,3,3,5,4,3,
                4,3,4,0,4,5,1,1,0,2,1,2,1,0,5,2,4,3,1,0,4,3,2,0,4,0,5,0,3,5,4,5,4,0,3,2,3,0,0,0,
                2,1,5,5,0,4,1,3,4,2,4,0,3,5,0,5,5,0,3,2,2,4,3,5,3,4,5,1,3,1,3,3,2,0,3,5,2,2,4,0,
                5,0,1,1,3,2,0,4,4,2,5,2,4,1,5,4,1,3,4,3,2,3,5,0,1,5,3,3,2,4,2,1,4,1,0,4,3,4,4,3,
                0,0,4,5,4,4,2,0,1,0,3,5,2,5,3,3,1,1,2,1,4,0,0,2,0,0,4,5,4,5,5,0,4,0,2,5,5,3,3,4,
                0,0,3,3,2,1,3,4,2,1,5,3,4,3,1,3,1,2,5,4,0,1,1,3,2,5,2,4,2,5,5,5,5,1,1,3,0,1,3,3,
                0,1,2,0,5,5,0,5,0,4,5,0,0,5,0,2,1,5,3,5,5,4,2,1,5,4,1,0,3,1,0,2,3,0,4,2,0,3,3,0,
                0,1,4,0,3,3,3,5,0,3,0,4,1,3,4,4,0,4,4,1,1,4,2,3,2,3,0,5,3,1,1,1,2,1,1,1,5,5,4,2,
                2,4,3,5,4,5,3,0,3,1,3,3,3,5,5,0,3,4,3,4,3,1,3,2,5,4,5,1,2,2,1,2,0,4,2,4,4,2,1,5,
                5,3,2,1,0,4,3,5,3,1,1,5,0,4,1,3,1,0,3,1,1,4,2,2,2,0,1,1,3,0,1,5,5,1,0,1,2,4,0,4,
                5,5,5,1,3,4,1,1,3,2,5,0,1,5,4,4,3,0,3,5,3,3,0,2,4,4,2,5,4,5,4,0,0,4,2,3,2,4,2,0,
                2,3,4,5,4,5,0,5,4,5,0,4,0,4,3,0,2,2,0,2,1,5,5,1,0,3,4,4,2,1,0,3,1,4,3,0,1,0,2,2,
                2,5,2,2,3,0,0,5,4,4,5,5,1,5,4,4,4,3,2,3,0,3,3,0,3,3,0,3,4,2,0,3,4,1,2,0,0,1,1,4,
                0,5,0,1,2,5,2,1,4,0,1,5,0,2,5,3,4,2,4,3,2,1,4,3,5,2,1,5,3,3,0,2,0,5,3,2,1,0,0,5,
                2,5,1,3,0,0,2,0,1,1,4,3,3,4,0,0,1,4,4,4,5,3,2,3,2,4,3,5,0,2,5,4,0,4,5,2,1,4,1,2,
                0,4,5,4,0,2,0,2,1,0,2,3,5,5,4,2,4,1,3,0,0,3,3,0,5,0,0,3,4,5,1,1,1,5,5,5,4,0,3,3,
                2,2,5,1,1,1,5,5,5,5,5,4,1,2,0,1,3,4,2,1,3,5,1,4,4,3,2,4,5,3,0,1,0,0,5,1,4,0,5,2,
                3,4,2,5,5,0,0,5,0,0,0,5,5,2,1,4,5,2,4,5,1,4,3,0,3,1,5,0,4,5,4,2,5,0,3,2,2,5,5,2,
                3,2,1,0,0,4,5,3,3,5,0,3,5,3,0,4,4,4,2,1,3,4,4,1,2,0,5,2,3,1,1,2,3,3,3,5,1,2,3,0,
                2,1,0,3,5,1,1,1,1,0,1,1,4,3,2,0,2,5,4,4,4,5,0,3,4,2,1,4,4,3,4,0,3,0,4,0,2,0,3,5,
                3,3,5,2,1,3,2,5,4,0,1,3,2,5,1,3,2,1,4,4,0,3,5,1,3,3,1,4,4,4,0,4,5,5,2,0,3,3,2,4,
                2,1,3,2,4,0,4,0,1,5,1,5,2,5,3,5,1,4,2,4,1,0,0,4,1,5,1,5,3,3,3,3,4,0,2,4,3,4,3,4,
                4,3,0,4,4,1,0,4,2,3,2,2,3,0,0,3,4,4,3,3,5,1,1,2,5,2,5,2,5,3,3,1,0,3,2,5,0,5,0,4,
                0,2,5,4,5,5,0,5,0,1,2,5,1,2,2,1,0,4,5,1,5,4,1,2,3,5,2,1,3,0,1,3,0,3,0,3,2,2,2,3,
                1,2,3,2,0,4,4,3,1,1,2,2,3,0,2,3,3,0,0,1,5,4,2,1,5,0,2,5,2,4,4,3,0,3,3,1,0,1,2,2,
        };
        int[] Tab2 ={4,1,3,3,1,2,4,1,2,2,5,0,3,5,0,0,4,4,1,2,3,1,3,3,5,0,2,4,1,2,5,4,2,1,5,1,1,0,4,1,
                5,1,2,3,5,3,0,5,4,3,1,2,4,5,2,1,1,0,3,3,4,4,0,1,3,1,4,0,2,0,0,2,5,1,5,2,3,1,2,2,
                5,5,2,4,2,4,4,4,4,2,5,5,5,2,4,5,5,2,3,4,3,5,5,1,5,1,4,0,2,1,4,5,5,0,3,2,5,2,5,3,
                4,2,0,3,3,3,1,3,0,1,1,1,2,5,3,0,3,2,0,5,4,3,4,0,2,3,0,0,0,5,5,0,4,0,0,4,0,0,3,2,
                2,5,5,2,0,4,2,4,1,1,0,3,2,5,1,0,4,2,4,2,0,3,2,3,1,1,2,4,3,2,1,3,3,4,1,4,0,0,1,0,
                3,5,5,2,5,1,2,1,3,1,1,2,2,0,1,2,5,5,5,1,0,1,4,5,1,1,1,4,5,2,5,2,1,5,3,3,4,4,3,3,
                1,1,1,1,4,2,1,4,4,3,5,5,0,4,0,5,0,1,4,3,0,2,0,0,4,0,0,0,2,4,4,5,0,4,0,3,1,4,0,2,
                4,2,0,1,0,3,5,2,0,3,5,3,0,3,4,2,4,4,5,1,2,1,3,5,0,3,2,4,1,4,3,1,0,0,1,1,3,2,4,3,
                5,0,4,1,2,0,3,2,5,2,1,5,3,5,3,2,5,4,3,3,1,4,2,5,5,2,1,1,3,0,0,1,4,4,0,4,5,3,2,1,
                5,5,0,5,4,4,3,2,3,1,4,1,1,4,1,2,4,0,1,2,5,5,5,0,3,4,3,2,0,0,4,3,3,1,4,2,0,1,5,1,
                1,5,0,2,4,4,5,0,2,1,5,1,4,2,1,2,3,1,1,2,3,0,3,3,3,1,2,3,3,4,1,0,1,0,1,5,5,3,0,0,
                1,1,3,3,5,0,5,5,4,0,2,3,0,3,0,1,1,1,1,4,0,3,0,4,2,5,3,3,5,3,5,0,1,2,0,5,5,3,1,0,
                5,0,2,3,4,3,3,1,1,4,3,3,3,0,1,0,2,0,5,3,2,2,4,2,4,1,1,3,1,1,3,4,0,4,1,5,1,4,2,2,
                1,4,5,5,5,5,0,5,2,0,4,4,0,2,5,5,5,4,4,5,0,3,2,2,4,0,2,2,5,4,2,4,2,5,2,3,3,1,1,2,
                4,4,5,0,0,5,1,1,0,5,0,4,3,5,4,2,0,5,0,0,3,5,3,3,2,4,4,4,1,4,0,2,4,2,4,3,5,0,1,0,
                4,1,2,4,5,3,5,0,4,2,2,0,5,4,2,1,0,5,3,3,2,4,4,5,4,1,3,0,5,3,1,4,3,3,5,3,1,3,1,2,
                0,4,2,3,4,1,5,1,2,5,0,0,0,3,4,5,3,2,1,1,4,0,1,2,2,1,2,5,0,5,2,1,0,0,4,3,3,0,2,3,
                1,2,0,2,1,5,2,3,2,4,1,2,3,1,4,3,5,5,2,5,5,5,2,1,0,4,1,1,5,5,0,1,5,2,5,4,4,5,0,5,
                3,4,5,3,1,0,2,3,0,1,2,2,1,2,2,0,4,1,5,4,3,0,2,4,2,0,0,2,4,5,3,0,2,3,0,1,3,0,2,2,
                1,2,1,3,2,0,4,1,4,2,1,2,2,2,2,4,2,4,0,3,2,1,1,4,0,2,2,5,5,5,0,0,4,1,2,0,3,3,2,4,
                4,0,4,0,3,3,0,1,5,5,3,0,3,3,2,3,0,1,4,2,4,1,0,1,1,0,0,4,0,1,1,1,0,2,4,1,3,3,5,0,
                5,1,1,5,4,1,3,0,2,3,1,0,4,2,5,5,4,5,2,2,2,4,5,1,5,0,2,4,3,0,1,3,4,5,5,3,0,1,1,3,
                3,5,3,0,1,2,2,1,3,5,0,2,1,1,2,0,4,5,4,3,0,0,0,4,4,2,0,5,2,0,2,0,4,3,4,3,4,5,5,4,
                2,0,5,1,0,0,0,2,5,0,4,1,5,2,3,0,1,2,2,3,3,0,0,5,1,4,3,5,5,3,0,0,3,3,0,0,0,3,5,0,
                5,4,1,5,1,4,5,3,0,2,2,0,1,5,4,0,3,5,5,5,4,1,2,3,4,3,5,5,2,0,3,4,4,0,2,1,5,0,0,3,
                2,5,5,1,5,5,0,2,4,3,0,4,0,5,3,5,0,4,0,5,3,5,1,0,1,0,2,2,3,4,4,4,1,4,1,4,5,2,5,4,
                3,5,5,4,4,1,5,4,3,3,2,1,0,4,1,4,4,3,2,3,0,4,0,1,2,0,2,2,0,1,3,0,2,2,4,4,3,0,5,5,
                0,3,4,5,0,2,5,1,0,2,5,0,4,3,4,0,2,5,5,4,4,4,0,2,2,5,0,1,4,2,0,5,5,3,5,4,4,1,3,1,
                3,4,4,4,1,4,0,1,3,4,5,0,0,5,4,2,3,0,4,1,4,3,2,4,3,2,5,3,3,3,0,2,3,4,2,2,2,1,0,1,
                2,3,0,5,0,2,5,0,5,4,0,1,4,1,5,2,0,5,2,5,2,2,1,1,2,5,3,0,1,5,2,2,3,0,2,4,0,3,2,3,
                0,0,4,4,2,2,4,3,3,1,1,4,0,0,0,0,4,4,2,3,4,5,2,0,0,0,0,5,5,4,2,1,4,5,3,0,1,0,1,5,
                5,0,3,3,3,0,4,1,1,3,2,0,4,1,2,4,1,2,5,5,1,2,0,0,1,4,0,2,3,5,4,5,0,1,2,1,4,0,3,3,
                5,3,5,4,2,4,4,0,0,2,0,1,2,3,2,3,5,1,3,1,3,4,2,5,4,5,2,0,5,4,1,2,4,5,3,1,5,3,2,0,
                1,5,5,2,1,3,1,0,2,5,5,1,1,5,2,5,1,4,2,3,3,4,5,4,0,5,1,0,0,4,4,3,2,2,0,3,5,5,1,4,
                3,3,5,5,2,4,2,4,3,1,1,2,4,4,5,0,0,0,2,4,4,1,0,1,1,0,2,0,4,4,2,5,4,0,1,0,0,1,0,2,
                2,0,2,2,5,0,2,2,4,5,1,1,2,3,5,0,4,4,2,5,0,0,1,3,0,1,3,3,2,4,1,3,2,3,3,2,5,5,5,0,
                3,3,4,2,4,1,3,1,3,1,1,5,4,0,5,0,1,0,2,3,2,4,3,4,2,2,4,1,1,3,3,0,3,5,3,4,5,1,2,4,
                0,1,5,3,2,2,0,2,4,2,3,0,3,3,5,3,1,1,2,4,3,4,1,5,5,0,1,1,5,4,5,5,5,1,4,2,4,4,2,2,
                1,2,0,4,4,3,3,4,3,1,2,3,1,3,4,0,1,3,2,2,5,2,5,2,4,0,0,5,1,3,2,3,1,5,2,0,2,0,1,4,
                3,2,0,3,0,3,4,4,2,0,3,4,0,1,4,5,2,3,0,4,5,3,4,3,3,0,4,1,4,0,2,5,4,5,3,5,3,0,1,5,
        };
        int[] Tab1 ={2,4,1,1,3,2,4,0,0,0,3,4,0,1,1,5,4,0,2,4,0,4,0,2,0,3,2,5,0,4,1,3,0,5,2,2,5,0,0,3,
                2,3,3,3,3,4,1,0,3,0,0,1,2,2,2,3,2,3,3,3,4,5,4,5,0,4,4,4,4,4,0,3,0,5,5,5,4,1,0,3,
                2,3,1,1,5,2,2,0,3,1,1,1,0,5,3,1,3,0,3,3,0,4,2,4,4,1,2,1,4,3,5,2,2,3,3,0,5,1,5,1,
                2,2,0,1,2,2,1,1,5,4,5,0,1,0,0,3,0,1,4,4,1,2,1,4,0,1,4,5,2,3,1,0,5,1,3,5,0,0,3,1,
                1,1,5,2,4,0,2,5,5,1,2,3,3,3,1,2,0,5,4,3,3,2,3,2,0,5,2,0,2,5,4,2,4,2,1,5,1,4,3,4,
                2,0,3,2,1,4,5,0,2,2,2,2,3,4,4,4,4,2,5,3,5,1,5,5,0,2,4,1,2,0,3,1,1,3,3,2,3,0,4,1,
                4,0,0,1,5,3,5,1,2,1,4,2,4,2,3,2,2,2,3,2,0,4,5,3,1,5,4,4,2,5,1,4,3,2,0,4,1,3,5,3,
                1,3,1,1,2,0,1,5,2,3,1,5,3,0,2,2,3,1,3,1,2,1,1,3,3,0,5,1,0,2,0,2,5,1,0,0,5,4,1,5,
                3,2,3,5,3,2,1,3,4,1,0,4,3,4,3,4,0,1,0,5,3,4,5,3,1,1,2,4,3,4,2,3,4,0,3,2,0,0,3,4,
                1,3,5,4,2,1,4,4,1,5,1,4,2,1,3,0,4,0,5,0,4,5,5,0,1,0,2,0,3,0,0,5,4,2,2,4,2,0,2,2,
                3,4,3,5,1,4,2,4,4,2,1,0,3,3,4,2,0,4,4,0,2,4,3,4,0,3,3,1,4,4,4,4,5,5,4,4,4,2,5,2,
                4,0,1,1,3,5,4,3,1,3,5,1,3,0,5,3,5,3,4,2,1,3,1,5,1,1,4,1,1,1,2,2,5,3,2,3,5,1,1,2,
                4,5,5,5,1,1,2,0,2,4,5,1,4,2,5,4,4,3,0,3,1,2,4,5,0,2,0,2,5,4,0,4,3,5,3,2,2,5,5,2,
                4,1,5,3,2,5,2,3,1,4,0,1,1,3,5,3,1,2,4,1,3,3,3,1,2,5,5,2,5,5,4,0,5,5,0,0,1,4,5,2,
                0,2,3,5,3,5,5,1,4,1,0,0,3,0,2,3,5,2,2,2,4,2,0,0,0,5,0,2,3,5,0,0,0,3,0,3,5,4,3,4,
                4,3,1,2,2,1,2,0,0,5,3,3,3,0,5,4,0,3,4,2,5,2,0,2,0,0,3,4,1,5,1,4,3,5,3,3,4,0,3,4,
                0,3,5,3,0,4,4,0,2,3,1,4,3,3,4,4,3,4,2,4,5,0,0,1,3,3,3,5,5,5,1,1,5,1,3,2,5,3,4,0,
                2,2,1,3,2,2,0,1,5,2,0,2,0,3,0,2,4,5,0,1,2,4,3,2,5,4,3,3,5,1,5,2,5,0,1,0,4,1,5,3,
                3,2,1,2,0,2,3,2,4,2,0,2,1,1,4,0,3,3,4,1,1,4,0,5,0,5,1,3,3,0,2,4,3,5,5,0,3,5,3,3,
                1,0,1,4,2,1,4,0,0,3,2,0,3,0,0,2,1,1,4,2,2,4,0,0,4,5,0,3,4,4,1,4,5,0,1,3,0,1,1,1,
                0,1,3,2,1,5,2,3,5,2,0,3,3,2,0,5,5,1,2,3,4,1,3,5,1,1,0,0,3,3,0,2,0,0,5,5,5,5,1,4,
                1,5,0,5,2,4,1,3,4,1,3,1,2,2,0,1,0,2,1,3,5,4,1,0,5,5,5,4,3,0,5,5,3,2,1,4,3,5,4,5,
                5,2,2,2,4,5,5,3,5,1,2,0,1,2,2,3,0,1,3,2,0,3,5,0,4,3,5,1,0,1,3,2,4,5,5,4,5,5,5,1,
                1,4,3,4,3,2,2,2,0,5,1,3,4,1,4,1,5,1,0,3,4,5,1,1,3,1,0,1,0,2,0,0,0,0,2,4,3,0,3,0,
                1,1,1,1,2,4,3,5,0,4,3,1,0,1,3,0,4,5,3,1,5,3,1,0,0,4,2,0,5,5,2,5,1,2,3,1,2,1,1,0,
                4,3,5,2,2,0,2,3,5,0,4,1,4,4,1,5,3,3,1,4,0,3,1,3,1,4,2,1,0,0,5,1,0,4,0,5,4,4,5,5,
                0,1,4,2,3,2,3,0,5,2,3,5,4,0,3,2,0,1,0,3,5,4,4,5,4,0,1,2,4,1,2,5,5,3,3,0,2,1,4,2,
                1,2,2,4,5,0,1,0,5,4,5,4,4,5,2,0,5,1,0,1,0,1,4,1,4,3,3,5,4,4,3,0,0,4,4,3,2,1,3,5,
                3,3,3,5,2,0,4,3,2,0,4,3,1,4,2,3,2,3,4,5,5,0,1,4,4,2,2,2,3,1,0,0,2,2,2,2,2,5,1,3,
                4,2,3,3,1,0,3,0,2,0,2,3,2,3,2,2,0,2,3,3,2,2,3,1,3,0,4,3,1,4,1,2,2,0,4,3,0,3,4,1,
                4,4,3,4,1,0,1,3,0,5,3,5,2,4,5,5,3,2,5,3,4,3,3,4,0,3,2,1,1,5,3,0,5,4,2,0,4,2,5,0,
                3,3,3,5,5,3,5,3,4,3,0,1,0,0,2,4,4,4,1,1,2,2,3,2,5,4,1,0,4,3,4,0,2,3,2,3,5,5,2,5,
                2,2,3,1,2,3,5,2,3,0,3,2,2,0,5,4,5,1,2,2,1,1,3,5,0,3,0,3,3,0,5,2,2,0,0,3,0,4,5,3,
                0,0,3,5,5,2,4,0,0,1,1,4,3,4,0,1,4,4,4,1,5,4,2,2,4,4,2,4,4,1,5,4,2,1,1,4,1,0,5,0,
                5,5,2,1,0,4,1,5,0,4,2,0,5,0,4,3,0,0,2,4,2,0,2,1,3,0,3,5,1,1,2,5,0,5,2,3,2,4,1,5,
                2,2,5,1,5,4,2,5,2,4,2,5,0,3,4,3,4,0,5,3,4,1,0,0,2,0,5,1,2,3,5,4,3,1,4,4,2,3,4,2,
                4,3,5,4,2,4,4,2,4,5,5,4,5,5,4,5,2,5,1,3,3,3,1,3,4,2,0,1,5,0,5,0,0,3,1,1,0,4,5,5,
                1,0,5,4,1,1,5,3,2,1,0,5,1,1,1,2,4,0,5,1,1,1,1,0,0,0,3,2,1,3,2,1,0,3,3,5,3,0,4,0,
                1,3,2,2,0,3,1,0,2,4,2,1,2,5,3,1,0,5,3,5,1,4,2,0,0,5,2,5,2,3,5,3,0,0,0,3,2,2,4,1,
                4,3,3,1,4,1,5,0,4,2,0,2,2,4,3,0,0,0,0,4,2,2,3,3,3,3,4,4,5,0,3,0,5,2,2,3,5,5,5,3,
        };

        model = new DotInfo[sizeOfGame][sizeOfGame];

        int count=0;
        if (modeOfGame==0){
            numbStepsMax=69;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab0[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==1) {
            numbStepsMax=42;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab1[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==2) {
            numbStepsMax=35;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab2[count]);
                    count++;
                }
            }
        }
        else if (modeOfGame==3) {
            numbStepsMax=20;
            for(int i = 0; i < sizeOfGame; i++){
                for(int j = 0; j < sizeOfGame; j++){
                    model[i][j] = new DotInfo(i,j,Tab3[count]);
                    count++;
                }
            }
        }
        start();
    }

    private void start(){
        currentSelectedColor = model[0][0].getColor();
        model[0][0].setCaptured(true);
        numberOfSteps = 0;
        numberCaptured = 1;
    }

    public String getLevel(){
        return currentSelectedLevel;
    }

    public String setCurrentSelectedLevel(String a) {
        currentSelectedLevel=a;
        return currentSelectedLevel;
    }
}