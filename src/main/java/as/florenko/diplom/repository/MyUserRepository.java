package as.florenko.diplom.repository;

import as.florenko.diplom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyUserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
