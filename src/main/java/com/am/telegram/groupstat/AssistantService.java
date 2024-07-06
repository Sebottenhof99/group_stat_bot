package com.am.telegram.groupstat;

import com.am.telegram.groupstat.user.Assistant;
import com.am.telegram.groupstat.user.AssistantDTO;
import com.am.telegram.groupstat.user.Assistants;
import com.am.telegram.groupstat.user.AssistentRepository;

import java.util.Optional;

public class AssistantService {

    private final AssistentRepository assistentRepository;

    public AssistantService(AssistentRepository assistentRepository) {
        this.assistentRepository = assistentRepository;
    }

    public Optional<Assistant> assistantOf(String username) {
        Optional<AssistantDTO> userByUsername = assistentRepository.getUserByUsername(username);
        return userByUsername.map(Assistant::new);
    }

    public void save(Assistant assistant) {
        assistentRepository.saveUser(assistant.toDTO());
    }

    public void save(AssistantDTO assistantDTO) {
        assistentRepository.saveUser(assistantDTO);
    }

    public Assistants admins() {
        return new Assistants(assistentRepository.findAllAdmins());
    }

    public Assistants users() {
        return new Assistants(assistentRepository.findAllUsers());
    }
}
