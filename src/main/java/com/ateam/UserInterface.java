/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ateam;

import java.util.Scanner;

/**
 *
 * @author pferna12
 */
public class UserInterface {

    public void welcomeUser() {
        System.out.println("""
                           WELCOME TO THE CHAT
                           Type login or register
                           Exit to close the programm
                           """);
        String option = readString("Please, type an option.").toLowerCase();
        do {
            switch (option) {
                case "login": // LOGIN
                    loginUser();
                    break;
                case "register": // REGISTER
                    loginUser();
                    break;
            }
        } while (option != "exit");
    }

    public static UserA loginUser() {
        String username = readString("Insert your username: ");
        String password = readString("Insert your password: ");
        return new UserA(username, password);
    }

    public static String readString(String mensaje) {
        System.out.println(mensaje);
        Scanner sc = new Scanner(System.in);
        String texto;
        texto = sc.nextLine();
        return texto;
    }

    public static int readInteger(String mensaje) {
        Scanner sc = new Scanner(System.in);
        System.out.println(mensaje);
        int num;

        //Antes de leer hay que comprobar que el tipo de valor introducido se corresponde con el solicitado
        //Si no lo hacemos y leemos directamente un valor no válido se lanzaría la excepción InputMismatchException
        while (!sc.hasNextInt()) {
            sc.next(); //si lo que se ha introducido no es un número lo sacamos del buffer
            System.out.println("Please enter a valid number: ");
        }
        num = sc.nextInt();

        return num;
    }
}
