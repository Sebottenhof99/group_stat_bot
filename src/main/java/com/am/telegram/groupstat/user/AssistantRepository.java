package com.am.telegram.groupstat.user;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AssistantRepository {

    private final Map<String, AssistantDTO> persistedUsers = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(AssistantRepository.class);

    @PostConstruct
    public void init() {
        AssistantDTO user1 = new AssistantDTO();
        user1.setUserName("f_zoidberg");
        user1.setAdmin(true);
        user1.setHasReadAccess(true);
        user1.setSubscribed(false);

        AssistantDTO user2 = new AssistantDTO();
        user2.setUserName("kat_iakov");
        user2.setAdmin(true);
        user2.setHasReadAccess(true);
        user2.setSubscribed(false);

        persistedUsers.put("f_zoidberg", user1);
        persistedUsers.put("kat_iakov", user2);
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
