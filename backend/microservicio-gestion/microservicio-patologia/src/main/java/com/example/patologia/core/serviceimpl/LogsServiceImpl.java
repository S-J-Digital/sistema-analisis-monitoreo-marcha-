package com.example.patologia.core.serviceimpl;

import com.example.patologia.core.dto.LogDto;
import com.example.patologia.core.entities.Logs;
import com.example.patologia.core.repository.LogsRepository;
import com.example.patologia.core.service.LogsService;
import com.example.patologia.core.util.IpUtils;
import com.example.patologia.core.util.UsuarioUtil;
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
