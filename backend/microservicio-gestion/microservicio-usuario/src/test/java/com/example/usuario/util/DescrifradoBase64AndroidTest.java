package com.example.usuario.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescrifradoBase64AndroidTest {

    @Test
    void getdesencriptacion() {
        String valor = DescrifradoBase64Android.getdesencriptacion("xONlsEmjn2PrlmJhay3URQ==");
        System.out.println(valor);
        Assertions.assertNotNull(valor);
    }
}