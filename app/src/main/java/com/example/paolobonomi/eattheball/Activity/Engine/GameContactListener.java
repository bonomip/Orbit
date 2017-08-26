package com.example.paolobonomi.eattheball.Activity.Engine;

import com.example.paolobonomi.eattheball.Activity.Engine.Script.TargetScript;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/*
 * Created by Paolo Bonomi on 12/04/2017.
 */

 class GameContactListener implements ContactListener {

    @Override
    public void beginContact (Contact contact) {

        if(contact.getFixtureA().getUserData().toString().equals(GameContract.PLAYER_TAG) && contact.getFixtureB().getUserData().toString().equals(GameContract.TARGET_TAG))
        {
            TargetScript eat = (TargetScript) contact.getFixtureB().getBody().getUserData();
            eat.setTag(GameContract.DESTROY_TAG);
            ComboCalculator.setTime();
        } else if (contact.getFixtureB().getUserData().toString().equals(GameContract.PLAYER_TAG) && contact.getFixtureA().getUserData().toString().equals(GameContract.TARGET_TAG))
        {
            TargetScript eat = (TargetScript) contact.getFixtureA().getBody().getUserData();
            eat.setTag(GameContract.DESTROY_TAG);
            ComboCalculator.setTime();
        }
    }

    @Override
    public void endContact (Contact contact){
    }

    @Override
    public void preSolve (Contact contact, Manifold oldManifold){

    }

    @Override
    public void postSolve (Contact contact, ContactImpulse impulse){
    }


}
