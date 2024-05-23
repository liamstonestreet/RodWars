package me.liam.rodwars.utils;

import org.bukkit.util.Vector;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtil {
    
    protected final double PI = Math.PI;
    
    public MathUtil(){
    
    }
    
    public double formatDouble(double d, int numDecimalPlaces) {
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(Math.max(0, numDecimalPlaces)));
        df.setRoundingMode(RoundingMode.HALF_UP); // Set rounding mode
        return Double.parseDouble(df.format(d));
    }
    
    public double sin(double angle){
        return Math.sin(angle);
    }
    
    public double cos(double angle){
        return Math.cos(angle);
    }
    
    public double tan(double angle){
        return Math.tan(angle);
    }
    
    /**
     * @param v the vector that is going to be rotated based on pitch and yaw
     * @param pitch the pitch of interest
     * @param yaw the yaw of interest
     * @return a new vector that has been rotated based on the pitch and yaw of loc
     */
    public Vector rotateVector(Vector v, double pitch, double yaw) {
        return v.rotateAroundZ(pitch).rotateAroundY(yaw);
    }
    
}

