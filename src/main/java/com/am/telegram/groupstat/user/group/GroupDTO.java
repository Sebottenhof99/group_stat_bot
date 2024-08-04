package com.am.telegram.groupstat.user.group;

import java.time.LocalDateTime;

public class GroupDTO {
  private int groupId;
  private String groupName;
  private String groupCity;
  private String groupCategory;
  private LocalDateTime addedAt;
  private String addedBy;

  public GroupDTO(String groupName, String groupCity, String groupCategory, String addedBy) {
    this.groupName = groupName;
    this.groupCity = groupCity;
    this.groupCategory = groupCategory;
    this.addedAt = LocalDateTime.now();
    this.addedBy = addedBy;
  }

  public GroupDTO() {}

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getGroupCity() {
    return groupCity;
  }

  public void setGroupCity(String groupCity) {
    this.groupCity = groupCity;
  }

  public String getGroupCategory() {
    return groupCategory;
  }

  public void setGroupCategory(String groupCategory) {
    this.groupCategory = groupCategory;
  }

  public LocalDateTime getAddedAt() {
    return addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public String getAddedBy() {
    return addedBy;
  }

  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }
}
