/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ateam;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author miromero
 */
public class ChatRoom {

    private String name;
    private ArrayList<ClientHandler> clientList;

    public ChatRoom(String name, ArrayList<ClientHandler> clientList) {
        this.name = name;
    }

    public int getNumberClients() {
        return clientList.size();
    }

    public String getRoomName() {
        return name;
    }

    public ArrayList<ClientHandler> getUsers() {
        return clientList;
    }

    public boolean remove(ClientHandler client) {
        if (isInRoom(client)) {
            clientList.remove(client);
            return true;

        }else return false;

    }
    
    public boolean isInRoom(ClientHandler client){
        return clientList.contains(client);
    }
    
    public boolean add(ClientHandler client){
        if(!isInRoom(client)){
        clientList.add(client);
        return true;
        }else return false;
    }
}
