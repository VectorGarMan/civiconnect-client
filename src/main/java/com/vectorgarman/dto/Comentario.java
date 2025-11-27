package com.vectorgarman.dto;

import java.time.LocalDate;

public class Comentario {

    private Long idcomentario;

    private Long idusuario;

    private Long idreporte;

    private Long idcomentariopadre;

    private String contenido;

    private LocalDate fechacreacion;

    private LocalDate fechaactualizacion;

    private Boolean editado;

    private Boolean esoficial;
}
