package devracom.Mnemosyne.repositories;

import devracom.Mnemosyne.models.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {
    Optional<Human> findByName(String name);
}
