package com.core.observer;

import java.util.ArrayList;

public class Observable {

    private ArrayList<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }
    
    public void notify(Object data){
        for (Observer observer: observers) {
            observer.update(data);
        }
    }
    
    public void addObserver(Observer o){
        this.observers.add(o);
    }
    
    public void removeObserver(Observer o){
        this.observers.remove(o);
    }

}
