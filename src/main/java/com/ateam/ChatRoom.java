/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ateam;

import java.util.ArrayList;

/**
 *
 * @author miromero
 */
public class ChatRoom {
    private String name;
    private ArrayList<ClientHandler> clientList = new ArrayList<>();

    public ChatRoom(String name) {
        this.name = name;
    }

    public int size() {
        return clientList.size();
    }

    public String getRoomName() {
        return name;
    }

    public ArrayList<ClientHandler> getUsers() {
        return clientList;
    }

    public boolean remove(ClientHandler client) {
        if (contains(client)) {
            clientList.remove(client);
            return true;

        }else return false;

    }

    public boolean contains(ClientHandler client){
        return clientList.contains(client);
    }

    public boolean add(ClientHandler client){
        if(!contains(client)){
        clientList.add(client);
        return true;
        }else return false;
    }
}
