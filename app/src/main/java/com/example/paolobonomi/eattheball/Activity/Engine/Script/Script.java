package com.example.paolobonomi.eattheball.Activity.Engine.Script;

import org.jbox2d.dynamics.Body;

/*
 * Created by Paolo Bonomi on 10/05/2017.
 */

public abstract class Script {

    public abstract void update();

    public abstract void addBody(Body b);

}
