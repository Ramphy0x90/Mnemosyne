package devracom.Mnemosyne.services;

import devracom.Mnemosyne.models.Human;
import devracom.Mnemosyne.repositories.HumanRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@Transactional
public class HumanService {
    private final HumanRepository humanRepository;

    public HumanService(HumanRepository userRepository) {
        this.humanRepository = userRepository;
    }


    /**
     * Get all Humans
     * @return List<Human>
     */
    public List<Human> getHumans() {
        return humanRepository.findAll();
    }
}
