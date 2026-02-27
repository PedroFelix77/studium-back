package com.studium.studium_academico.infrastructure.config;

import com.studium.studium_academico.business.dto.request.AddressRequestDTO;
import com.studium.studium_academico.business.dto.request.CreateAdminRequestDTO;
import com.studium.studium_academico.business.service.AdminService;
import com.studium.studium_academico.infrastructure.entity.Institution;
import com.studium.studium_academico.infrastructure.repository.AdminRepository;
import com.studium.studium_academico.infrastructure.repository.InstitutionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@DependsOn("dataInitializer")
public class AdminInitializer {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private InstitutionRepository institutionRepository;

    @PostConstruct
    @Transactional
    public void init() {

        if (adminRepository.count() > 0) {
            return;
        }

        Institution institution = institutionRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        CreateAdminRequestDTO dto = new CreateAdminRequestDTO(
                "Administrador",
                "12345678901",
                "admin@studium.com",
                LocalDate.of(1990, 1, 1),
                "87999999999",
                "Admin@123",
                institution.getId(), // AGORA CORRETO (UUID)
                new AddressRequestDTO(
                        "Rua Admin",
                        "100",
                        "Centro",
                        "Arcoverde",
                        "Pernambuco",
                        "56506100"
                ),
                LocalDate.now()
        );

        adminService.createAdmin(dto);

        System.out.println("ADMIN PADRÃO CRIADO COM SUCESSO");
    }


}
