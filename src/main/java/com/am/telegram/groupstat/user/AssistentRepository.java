package com.am.telegram.groupstat.user;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AssistentRepository {

    private final Map<String, AssistantDTO> persistedUsers = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(AssistentRepository.class);

    @PostConstruct
    public void init() {
        AssistantDTO user1 = new AssistantDTO();
        user1.setUserName("f_zoidberg");
        user1.setAdmin(true);
        user1.setHasReadAccess(true);
        user1.setSubscribed(false);
        persistedUsers.put("f_zoidberg", user1);
    }


    public Optional<AssistantDTO> getUserByUsername(String username) {
        return Optional.ofNullable(persistedUsers.get(username));
    }

    public void saveUser(AssistantDTO assistantDTO) {
        log.info("save user: {}", assistantDTO.getUserName());
        persistedUsers.put(assistantDTO.getUserName(), assistantDTO);
    }

    public List<AssistantDTO> findAllAdmins() {
        return persistedUsers.values().stream().filter(AssistantDTO::isAdmin).toList();
    }

    public List<AssistantDTO> findAllUsers() {
        return persistedUsers.values().stream()
                .filter(AssistantDTO::isHasReadAccess)
                .filter(a -> !a.isAdmin()).toList();
    }
}
