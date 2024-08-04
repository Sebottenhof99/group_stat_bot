package com.am.telegram.groupstat.user.report.group;

import java.time.LocalDate;

public class GroupDTO {
  private int id;
  private String internalName;
  private String publicName;
  private String city;
  private String category;
  private LocalDate addedAt;
  private String addedBy;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getInternalName() {
    return internalName;
  }

  public void setInternalName(String internalName) {
    this.internalName = internalName;
  }

  public String getPublicName() {
    return publicName;
  }

  public void setPublicName(String publicName) {
    this.publicName = publicName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public LocalDate getAddedAt() {
    return addedAt;
  }

  public void setAddedAt(LocalDate addedAt) {
    this.addedAt = addedAt;
  }

  public String getAddedBy() {
    return addedBy;
  }

  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }
}
