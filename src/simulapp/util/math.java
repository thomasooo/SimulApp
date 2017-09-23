package simulapp.util;

import java.util.Random;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public final class math {

    //uniformna dostribucia
    public static final int randomUniform(int min, int max){
        if(min == max){
            return min;
        }
        if(min > max){
            int tmp = min;
            min = max;
            max = tmp;
        }
        Random rand = new Random();
        return rand.nextInt((max + 1) - min) + min;
    }

    //uniformna distribucia
    public static final double randomUniform(double min, double max){
        if(min == max){
            return min;
        }
        if(min > max){
            double tmp = min;
            min = max;
            max = tmp;
        }

        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    public static int randomNormal(int mean, int deviance){
        Random rand = new Random();
        return (int) (rand.nextGaussian() * deviance + mean);
    }

    //normalna distribucia - kedy ma byt distribuovany, s akou odchylkou
    public static double randomNormal(double mean, double deviance){
        Random rand = new Random();
        return rand.nextGaussian() * deviance + mean;
    }

    //exponencialna distribucia
    public static final double randomExponential(double lambda){
        Random r = new Random();
        return Math.log(1 - r.nextDouble()) / (-lambda);
    }

    public static final double parseDoubleFromString(String str, double def){
        double res;

        try{
            res = Double.parseDouble(str);
        }catch(NumberFormatException e){
           res = def;
        }
        if(res > 999999){
            res = 999999;
        }
        return res;
    }

    public static final double parseDoubleFromString(String str){
        return parseDoubleFromString(str, 0);
    }

    public static final int parseIntFromString(String str, int def){
        int res;

        try{
            res = Integer.parseInt(str);
        }catch(NumberFormatException e){
            res = def;
        }
        if(res > 999999){
            res = 999999;
        }
        return res;
    }

    public static final int parseIntFromString(String str){
        return parseIntFromString(str, 0);
    }

}
