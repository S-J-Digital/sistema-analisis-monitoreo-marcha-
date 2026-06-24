package com.example.usuario.core.controllerException;
import com.example.usuario.core.service.LogsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@ControllerAdvice
public class ControllerException {
    private Logger logger =  Logger.getLogger(ControllerException.class.getName());
    private LinkedHashMap<String,String> errors = new LinkedHashMap<>();
    private static final String ACEPTADO = "Aceptado";
    private static final String ERROR = "Error";
    private final LogsService logsService;


    static {
        //Amorcito esto es para el almacenamiento de errores
    }

    @Autowired
    public ControllerException(LogsService logsService) {
        this.logsService = logsService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception, HttpServletRequest request) {
        logger.warning("Se ha producido un error: " + exception.getMessage());
        logsService.insertarLog(request, ERROR,exception.getMessage());
        return ResponseEntity.badRequest().body(errors.entrySet().stream()
                .filter(e -> e.getKey().equals(exception.getMessage()))
                .findFirst().map(Map.Entry::getValue).orElse(exception.getMessage()));
    }
}
