package com.example.encriptacion;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncriptacionTest {

    @Test
    public void getencriptacion_devuelveLaContraseñaEncriptada() {
        String password = String.valueOf(123);
        String encript = Encriptacion.getencriptacion(password);
        assertNotNull(encript);
    }

    @Test
    public void getdesencriptacion() {
    }
}