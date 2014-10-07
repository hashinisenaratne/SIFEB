/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.resources;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Udith Arosha
 */
public class Strings {
    
    private static Locale locale;
    private static ResourceBundle resourceBundle;

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static void setLocale(Locale l){
        locale = l;
        resourceBundle = ResourceBundle.getBundle("com.sifeb.ve.resources.Strings", locale);
    }
    
    public static String getString(String key){
        return resourceBundle.getString(key);
    }
}
