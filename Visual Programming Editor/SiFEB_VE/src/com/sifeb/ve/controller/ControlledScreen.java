/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sifeb.ve.controller;

import com.sifeb.ve.ScreensController;

/**
 *
 * @author Hashini Senaratne
 */
public interface ControlledScreen {
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(ScreensController screenPage);
}
