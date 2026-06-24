package com.example.microserviciodato.core.repository;

import com.example.microserviciodato.core.entities.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository <Logs,Long>{}
