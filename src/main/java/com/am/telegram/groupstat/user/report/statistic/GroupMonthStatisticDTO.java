package com.am.telegram.groupstat.user.report.statistic;

import java.time.LocalDate;

public class GroupMonthStatisticDTO {
  private int groupStatisticId;
  private int groupId;
  private LocalDate measuredAt;
  private int memberCount;

  public int getGroupStatisticId() {
    return groupStatisticId;
  }

  public void setGroupStatisticId(int groupStatisticId) {
    this.groupStatisticId = groupStatisticId;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public LocalDate getMeasuredAt() {
    return measuredAt;
  }

  public void setMeasuredAt(LocalDate measuredAt) {
    this.measuredAt = measuredAt;
  }

  public String getMemberCount() {
    return Integer.toString(memberCount);
  }

  public void setMemberCount(int memberCount) {
    this.memberCount = memberCount;
  }
}
