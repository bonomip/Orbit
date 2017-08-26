package com.example.paolobonomi.eattheball.Activity.Engine.Script;

/*
 * Created by Paolo Bonomi on 07/08/2017.
 */


/*
*
* this class creates a virtual representation of the field where the target and also the player are with a multidimensional matrix of integer.
* otherwise gives methods to update the matrix and to have access to that
*
 */

public abstract class PlayGround {

    private static int heigth;
    private static int width;
    private static int[][] matrix;

    private static void setHeigth(int engine_heigth){
        heigth = engine_heigth;
    }

    private static void setWidth(int eingine_width){
        width = eingine_width;
    }

    /*
    *
    * this method creates a matrix:
    *   each positions of the matrix assume a greater value if near borders,
    *   a smaller value if near center
    *
    * each position can assume a value between 1 and 99.
    *
     */

    public static void createPlayground(int engine_heigth, int engine_width){
        matrix = new int[engine_width*2][engine_heigth*2];
        for(int i = 0; i < matrix.length; i++ )
            for(int j = 0; j < matrix[0].length; j++ )
                matrix[i][j] = 1;
    }




}
