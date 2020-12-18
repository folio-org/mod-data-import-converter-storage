package org.folio.dao.snapshot;

public enum SnapshotProfileType {
  JOB_PROFILE("job_profiles"),
  MATCH_PROFILE("match_profiles"),
  ACTION_PROFILE("action_profiles"),
  MAPPING_PROFILE("mapping_profiles");

  private final String tableName;

  SnapshotProfileType(String tableName) {
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
