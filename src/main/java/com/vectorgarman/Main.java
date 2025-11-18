package com.vectorgarman;

public class Main {
    public static void main(String[] args) {
        ClienteAPI clienteAPI = new ClienteAPI();
        try {
            dto.Usuario usuario = clienteAPI.getUsuarioPorId(3L);
            System.out.println("Usuario: " + usuario);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}