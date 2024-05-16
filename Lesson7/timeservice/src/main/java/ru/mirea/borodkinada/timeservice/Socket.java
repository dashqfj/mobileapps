package ru.mirea.borodkinada.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Socket {

    public static BufferedReader getReader(java.net.Socket s) throws IOException {
        return (new BufferedReader(new InputStreamReader(s.getInputStream())));
    }

    public static PrintWriter getWriter(java.net.Socket s) throws IOException {
        return (new PrintWriter(s.getOutputStream(), true));
    }
}