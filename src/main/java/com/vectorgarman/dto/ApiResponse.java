package com.vectorgarman.dto;

public class ApiResponse<T> {
    private String status;
    private String mensaje;
    private String error;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return true;
    }
}
