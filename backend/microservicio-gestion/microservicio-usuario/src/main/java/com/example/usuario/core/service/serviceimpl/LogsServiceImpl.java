package com.example.usuario.core.service.serviceimpl;

import com.example.usuario.core.dto.LogDto;
import com.example.usuario.core.model.Logs;
import com.example.usuario.core.repository.LogsRepository;
import com.example.usuario.core.service.LogsService;
import com.example.usuario.util.IpUtils;
import com.example.usuario.util.UsuarioUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogsServiceImpl implements LogsService {
    private LogsRepository logsRepository;
    @Autowired
    public LogsServiceImpl (LogsRepository logsRepository){
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
