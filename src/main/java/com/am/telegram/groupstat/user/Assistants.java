package com.am.telegram.groupstat.user;

import java.util.List;
import java.util.stream.Collectors;

public class Assistants {

    private final List<AssistantDTO> assistantList;

    public Assistants(List<AssistantDTO> assistantList) {
        this.assistantList = assistantList;
    }

    public String toStringList() {
        return assistantList.stream()
                .map(AssistantDTO::getUserName)
                .collect(Collectors.joining("\n"));
    }

}
