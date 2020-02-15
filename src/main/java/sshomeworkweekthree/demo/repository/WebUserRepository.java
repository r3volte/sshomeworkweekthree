package sshomeworkweekthree.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sshomeworkweekthree.demo.model.WebUser;

import java.util.Optional;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Long> {

    Optional<WebUser> findWebUserByUsername(String username);

    WebUser findWebUserById(Long id);
}
