package com.vectorgarman;

import com.vectorgarman.api.ClienteAPI;
import com.vectorgarman.dto.ApiResponse;

public class Main {
    public static void main(String[] args) {
        ClienteAPI clienteAPI = new ClienteAPI();
        try {
            ApiResponse<?> apiResponse = clienteAPI.getUsuarioPorId(26L);
            System.out.println("Usuario: " + apiResponse.getData());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Para mostrar la imagen, obteniendo los bytes de la base de datos: "archivo" (No lo he probado, puede que no funcione):
//    byte[] bytesImagen = evidencia.getArchivo(); // viene de la base de datos
//
//    InputStream in = new ByteArrayInputStream(bytesImagen);
//    BufferedImage image = ImageIO.read(in);
//    ImageIcon icon = new ImageIcon(image);
//
//    // Mostrar en un JLabel
//    JLabel label = new JLabel(icon);
}