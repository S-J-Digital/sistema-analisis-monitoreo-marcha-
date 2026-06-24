package com.example.patologia.core.controller;

import com.example.patologia.core.dto.DatoPatologiaDto;
import com.example.patologia.core.dto.PatologiaDto;
import com.example.patologia.core.entities.Patologia;
import com.example.patologia.core.service.DatoPatologiaService;
import com.example.patologia.core.service.LogsService;
import com.example.patologia.core.service.PatologiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patologia")
@Tag(name = "Controlador de patologias",
        description = "Controlador encargardo de todo lo referente con las patologias")
public class PatologiaController {
    private PatologiaService patologiaService;
    private LogsService logsService;
    private String aceptado = "Aceptado";
    private String error = "Error";
    private DatoPatologiaService datoPatologiaService;
    @Autowired
    public PatologiaController(PatologiaService patologiaService, LogsService logsService, DatoPatologiaService datoPatologiaService){
        this.patologiaService = patologiaService;
        this.datoPatologiaService = datoPatologiaService;
        this.logsService = logsService;
    }

    @PostMapping("/create")
    @Operation(summary = "Insertar patologia",
            description = "Permite insertar una patologia")
    public ResponseEntity<?> insertarPatologia(@RequestBody PatologiaDto patologiaDto, HttpServletRequest request){
        try{
            patologiaService.insertarPatologia(patologiaDto);
            logsService.insertarLog(request,aceptado,"Se insertó una patologia");
            return ResponseEntity.ok("Se insertó una patologia correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @PostMapping("/createrelacion")
    @Operation(summary = "Crear la relacion dato-patologia",
            description = "Permite crear relacion dato-patologia")
    public ResponseEntity<?> crearRelacion(@RequestBody DatoPatologiaDto datoPatologiaDto, HttpServletRequest request){
        try{
            datoPatologiaService.crearRelacion(datoPatologiaDto);
            logsService.insertarLog(request,aceptado,"Se insertó una patologia");
            return ResponseEntity.ok("Se insertó una patologia correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @PutMapping("/update/{idpatologia}")
    @Operation(summary = "Modificar patologia",
            description = "Permite modificar una patologia")
    public ResponseEntity<?> modificarPatologia(@RequestBody PatologiaDto patologiaDto,@PathVariable Long idpatologia,HttpServletRequest request){
        try{
            patologiaService.modificarPatologia(patologiaDto,idpatologia);
            logsService.insertarLog(request,aceptado,"Se modificó un usuario");
            return ResponseEntity.ok("Se ha modificado correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @DeleteMapping("/delete/{idpatologia}")
    @Operation(summary = "Eliminar patologia",
            description = "Permite eliminar una patologia")
    public ResponseEntity<?> eliminarPatologia(@PathVariable Long idpatologia,HttpServletRequest request){
        try{
            patologiaService.eliminarPatologia(idpatologia);
            logsService.insertarLog(request,aceptado,"Se elimonó una patologia");
            return ResponseEntity.ok("Se eliminó una patologia correctamente");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todas las patologias",
            description = "Permite obtener todas las patologias")
    public ResponseEntity<?> obtenerPatologias(){
        try{
            return ResponseEntity.ok(patologiaService.obtenerPatologias());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/obtenerPatologiaXDato/{id_dato}")
    @Operation(summary = "Obtener participantes para un usuario", description = "Permite obtener una lista de participantes a raíz del usuario")
    public ResponseEntity<?> obtenerPatologiaXDato(@PathVariable Long id_dato){
        try{
            List<Patologia> patologiaList = patologiaService.obtenerPatologiaXDato(id_dato);
            return ResponseEntity.ok(patologiaList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/obtenerIdbyNombre/{nombre}")
    public ResponseEntity<?> obtenerIdbynombre(@PathVariable String nombre){
        try{
            return ResponseEntity.ok(patologiaService.obtenerIdbynombre(nombre));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminarrelacion/iddato={iddato}&fecha={fecha}")
    public ResponseEntity<?> eliminarRelacion(@PathVariable Long iddato, @PathVariable String fecha){
        try{
            datoPatologiaService.eliminarRelacion(iddato,fecha);
            return ResponseEntity.ok("Se eliminó la relación");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
