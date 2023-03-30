package devracom.Mnemosyne.controllers;

import devracom.Mnemosyne.models.Human;
import devracom.Mnemosyne.services.HumanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/human")
public class HumanController {
    private final HumanService humanService;

    /**
     * Returns all Humans
     * @return List<Human>
     */
    @Operation(summary = "Get all humans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Authorization denied", content = @Content),
    })
    @GetMapping(path = "/all")
    public List<Human> getHumans() {
        return humanService.getHumans();
    }
}
