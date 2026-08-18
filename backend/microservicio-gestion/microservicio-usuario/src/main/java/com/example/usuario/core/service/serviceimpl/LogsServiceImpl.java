package com.example.usuario.core.service.serviceimpl;

import com.example.usuario.core.dto.LogDto;
import com.example.usuario.core.model.Logs;
import com.example.usuario.core.repository.LogsRepository;
import com.example.usuario.core.service.LogsService;
import com.example.usuario.util.IpUtils;
import com.example.usuario.util.UsuarioUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsService {
    private final LogsRepository logsRepository;
    @Override
    public void insertarLog(HttpServletRequest request, String estado,String mensaje,String method) {
        String ip = IpUtils.hostIpV4Http(request);
        String user = UsuarioUtil.Usuario(request);
        LogDto logDTO = new LogDto(estado,user,ip,mensaje,method);
        logsRepository.save(new Logs(logDTO));
    }
}
