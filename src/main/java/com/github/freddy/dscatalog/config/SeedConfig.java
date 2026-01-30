package com.github.freddy.dscatalog.config;

import com.github.freddy.dscatalog.model.Role;
import com.github.freddy.dscatalog.model.RoleAuthority;
import com.github.freddy.dscatalog.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedConfig implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se a Role Operator já existe, se não, cria
        if (roleRepository.findByAuthority(RoleAuthority.ROLE_OPERATOR).isEmpty()) {
            Role operator = new Role();
            operator.setAuthority(RoleAuthority.ROLE_OPERATOR);
            roleRepository.save(operator);
        }

        // Você pode adicionar a ROLE_ADMIN também
        if (roleRepository.findByAuthority(RoleAuthority.ROLE_ADMIN).isEmpty()) {
            Role admin = new Role();
            admin.setAuthority(RoleAuthority.ROLE_ADMIN);
            roleRepository.save(admin);
        }
    }
}