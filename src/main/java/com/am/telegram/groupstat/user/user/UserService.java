package com.am.telegram.groupstat.user.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;

public class UserService {

  private final DataSource ds;
  private final UserRepository userRepository;

  public UserService(DataSource ds, UserRepository userRepository) {
    this.ds = ds;
    this.userRepository = userRepository;
  }

  public String regularUserNames() {
    try (Connection connection = ds.getConnection()) {
      List<UserDTO> regularUsers = userRepository.findRegularUsers(connection);
      return regularUsers.stream().map(UserDTO::getUserName).collect(Collectors.joining("\n"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String adminNames() {
    try (Connection connection = ds.getConnection()) {
      List<UserDTO> admins = userRepository.findAdmins(connection);
      return admins.stream().map(UserDTO::getUserName).collect(Collectors.joining("\n"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Optional<UserDTO> getUserByName(String userName) {
    try (Connection con = ds.getConnection()) {
      return userRepository.findUserByName(con, userName);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void save(UserDTO userDTO) {
    try (Connection con = ds.getConnection()) {
      if (userDTO.getUserId() > 0) {
        userRepository.update(con, userDTO);
      } else {
        userRepository.persist(con, userDTO);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void remove(UserDTO userDTO) {
    try (Connection con = ds.getConnection()) {
      userRepository.removeByUserId(con, userDTO.getUserId());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
