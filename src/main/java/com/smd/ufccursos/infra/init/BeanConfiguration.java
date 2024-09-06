package com.smd.ufccursos.infra.init;

import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;
import com.smd.ufccursos.domain.service.CourseService;
import com.smd.ufccursos.domain.service.DisciplineService;
import com.smd.ufccursos.domain.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public StudentServicePort studentServicePort(StudentRepositoryPort studentRepositoryPort){
        return  new StudentService(studentRepositoryPort);
    }

    @Bean
    public DisciplineServicePort disciplineServicePort(DisciplineRepositoryPort disciplineRepositoryPort){
        return new DisciplineService(disciplineRepositoryPort);
    }

    @Bean
    public CourseServicePort courseServicePort(CourseRepositoryPort courseRepositoryPort){
        return new CourseService(courseRepositoryPort);
    }
}
