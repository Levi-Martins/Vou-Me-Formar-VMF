package com.smd.ufccursos.infra.init;

import com.smd.ufccursos.domain.ports.repositoryPort.*;
import com.smd.ufccursos.domain.ports.servicePort.*;
import com.smd.ufccursos.domain.service.*;
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

    @Bean
    public AuthorizationService authorizationService(UserRepositoryPort userRepositoryPort){
        return new AuthorizationService(userRepositoryPort);
    }

    @Bean
    public TokenService tokenService(){
        return new TokenService();
    }
}
