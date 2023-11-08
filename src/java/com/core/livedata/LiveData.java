package com.core.livedata;

public class LiveData<T> {
    
    private ObserveCallback callback;
    private T data;
    
    public LiveData(T data){
        this.data = data;
        this.callback = null;
    }
    
    public LiveData(){
        this.data = null;
        this.callback = null;
    }
    
    public void observe(ObserveCallback callback){
        this.callback = callback;
    }
    
    public void postValue(T data){
        this.data = data;
        
        if(this.callback != null){
            this.callback.observe(data);
        }
    }
    
    public T getValue(){
        return this.data;
    }
    
}
