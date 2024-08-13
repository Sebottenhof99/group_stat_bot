package com.am.telegram.groupstat.logic;

import com.am.telegram.groupstat.logic.user.UserDTO;
import java.time.LocalDateTime;

public class UserManagementPanel {

  public UserDTO createUser(String addedBy) {
    UserDTO userDTO = new UserDTO();
    userDTO.setAddedBy(addedBy);
    userDTO.setAddedAt(LocalDateTime.now());
    userDTO.setAdmin(false);
    userDTO.setSubscribed(false);
    userDTO.setHasReadAccess(true);
    return userDTO;
  }

  public UserDTO createAdmin(String addedBy) {
    UserDTO userDTO = createUser(addedBy);
    userDTO.setAdmin(true);
    return userDTO;
  }
}
