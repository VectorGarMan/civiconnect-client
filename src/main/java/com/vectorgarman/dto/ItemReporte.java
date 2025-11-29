package com.vectorgarman.dto;

import javax.swing.*;
import java.util.Map;

public class ItemReporte {

    private Map<String, Object> data;
    private JLabel lblVotos;

    public ItemReporte(Map<String, Object> data, JLabel lblVotos) {
        this.data = data;
        this.lblVotos = lblVotos;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public JLabel getLblVotos() {
        return lblVotos;
    }
}
