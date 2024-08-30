package com.am.telegram.groupstat.logic.assistant;

import com.am.telegram.groupstat.logic.user.UserDTO;
import com.am.telegram.groupstat.logic.user.UserRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

public class AssistantService {

  private final UserRepository userRepository;
  private final AssistantRepository assistantRepository;
  private final DataSource ds;

  public AssistantService(
      DataSource ds, UserRepository userRepository, AssistantRepository assistantRepository) {
    this.ds = ds;
    this.userRepository = userRepository;
    this.assistantRepository = assistantRepository;
  }

  public Optional<Assistant> assistantOf(String username) {
    try (Connection con = ds.getConnection()) {
      Optional<UserDTO> userByName = userRepository.findUserByName(con, username);
      if (userByName.isEmpty()) {
        return Optional.empty();
      }

      var assistantDTO = assistantRepository.findByUserId(con, userByName.get().getUserId());
      return Optional.of(new Assistant(userByName.get(), assistantDTO));

    } catch (SQLException e) {
      throw new AssistantException("Could not get assistant for name", e);
    }
  }

  public void save(Assistant assistant) {
    try (Connection con = ds.getConnection()) {
      if (assistantRepository.existAssistant(con, assistant.getUserDTO().getUserId())) {
        assistantRepository.update(
            con, assistant.getAssistantDTO(), assistant.getUserDTO().getUserId());
      } else {
        assistantRepository.persist(
            con, assistant.getAssistantDTO(), assistant.getUserDTO().getUserId());
      }
    } catch (SQLException e) {
      throw new AssistantException("Could not save assistant", e);
    }
  }
}
