package com.am.telegram.groupstat;

import com.am.telegram.groupstat.user.Assistant;
import com.am.telegram.groupstat.user.AssistantDTO;
import com.am.telegram.groupstat.user.AssistantRepository;
import com.am.telegram.groupstat.user.Assistants;

import java.util.Optional;

public class AssistantService {

    private final AssistantRepository assistantRepository;

    public AssistantService(AssistantRepository assistantRepository) {
        this.assistantRepository = assistantRepository;
    }

    public Optional<Assistant> assistantOf(String username) {
        Optional<AssistantDTO> userByUsername = assistantRepository.getUserByUsername(username);
        return userByUsername.map(Assistant::new);
    }

    public void save(Assistant assistant) {
        assistantRepository.saveUser(assistant.toDTO());
    }

    public void save(AssistantDTO assistantDTO) {
        assistantRepository.saveUser(assistantDTO);
    }

    public Assistants admins() {
        return new Assistants(assistantRepository.findAllAdmins());
    }

    public Assistants users() {
        return new Assistants(assistantRepository.findAllUsers());
    }
}
