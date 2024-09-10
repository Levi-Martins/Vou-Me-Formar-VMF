package com.smd.ufccursos.infra.init;

import com.smd.ufccursos.domain.ports.repositoryPort.CourseRepositoryPort;
import com.smd.ufccursos.domain.ports.repositoryPort.DisciplineRepositoryPort;
import com.smd.ufccursos.domain.ports.repositoryPort.PhoneRepositoryPort;
import com.smd.ufccursos.domain.ports.repositoryPort.StudentRepositoryPort;
import com.smd.ufccursos.domain.ports.servicePort.CourseServicePort;
import com.smd.ufccursos.domain.ports.servicePort.DisciplineServicePort;
import com.smd.ufccursos.domain.ports.servicePort.PhoneServicePort;
import com.smd.ufccursos.domain.ports.servicePort.StudentServicePort;
import com.smd.ufccursos.domain.service.CourseService;
import com.smd.ufccursos.domain.service.DisciplineService;
import com.smd.ufccursos.domain.service.PhoneService;
import com.smd.ufccursos.domain.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {


    @Bean
    public DisciplineServicePort disciplineServicePort(DisciplineRepositoryPort disciplineRepositoryPort,
                                                       CourseServicePort courseServicePort) {
        return new DisciplineService(disciplineRepositoryPort, courseServicePort);
    }

    @Bean
    public CourseServicePort courseServicePort(CourseRepositoryPort courseRepositoryPort) {
        return new CourseService(courseRepositoryPort);
    }

    @Bean
    public StudentServicePort studentServicePort(StudentRepositoryPort studentRepositoryPort,
                                                 CourseServicePort courseServicePort) {
        return new StudentService(studentRepositoryPort, courseServicePort);
    }

    @Bean
    public PhoneServicePort phoneServicePort(PhoneRepositoryPort phoneRepositoryPort,
                                             StudentServicePort studentServicePort) {
        return new PhoneService(phoneRepositoryPort, studentServicePort);
    }
}
