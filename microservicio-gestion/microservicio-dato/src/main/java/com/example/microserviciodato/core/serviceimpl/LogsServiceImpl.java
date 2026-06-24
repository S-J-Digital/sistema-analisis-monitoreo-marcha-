package com.example.microserviciodato.core.serviceimpl;

import com.example.microserviciodato.core.dto.LogDto;
import com.example.microserviciodato.core.entities.Logs;
import com.example.microserviciodato.core.repository.LogsRepository;
import com.example.microserviciodato.core.service.LogsService;
import com.example.microserviciodato.core.util.IpUtils;
import com.example.microserviciodato.core.util.UsuarioUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogsServiceImpl implements LogsService {
    private LogsRepository logsRepository;
    @Autowired
    public LogsServiceImpl(LogsRepository logsRepository){
        this.logsRepository = logsRepository;
    }

    @Override
    public void insertarLog(HttpServletRequest request, String estado,String mensaje) {
        String ip = IpUtils.hostIpV4Http(request);
        String user = UsuarioUtil.Usuario(request);
        LogDto logDTO = new LogDto(estado,user,ip,mensaje);
        logsRepository.save(new Logs(logDTO));
    }
}
