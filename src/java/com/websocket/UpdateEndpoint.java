/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.websocket;

import com.data.repository.MongoRepository;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author benja
 */
@ServerEndpoint(value = "/updates")
public class UpdateEndpoint {

    private MongoRepository mongoRepository;
    private Session session;
    private Gson gson;
    private ScheduledExecutorService scheduler;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        gson = new Gson();
        mongoRepository = MongoRepository.getInstance();
        // Inicializa el programador para enviar datos cada 30 segundos.
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::sendData, 0, 10, TimeUnit.SECONDS);
        System.out.println("Cliente conectado con id-> " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        try {
            this.session = null;
        } catch (Exception e) {
            this.session = null;
            System.out.println("Error -> " + e.getMessage());
        }

    }

    @OnMessage
    public String onMessage(Session session, String message) {
        try {
            System.out.println("Mensaje recibido -> " + message);
        } catch (Exception e) {
            System.out.println("Error -> " + e.getMessage());
        }
        return message;
    }

    private void sendData() {
        try {
            String jsonInfo = "{\"trucks\":" + gson.toJson(mongoRepository.getTrucks());
            jsonInfo = jsonInfo.concat(",\"lights\":"+ gson.toJson(mongoRepository.getLights()));
            session.getBasicRemote().sendText(jsonInfo);
        } catch (IOException e) {
            this.session = null;
            System.out.println("Error sending message -> " + e.getMessage());
        }
    }

}
