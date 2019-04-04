package org.folio.dao.snapshot;


import org.folio.rest.jaxrs.model.ProfileSnapshotWrapper;

/**
 * Class represents an output row of the job profile snapshot.
 */
public class ProfileSnapshotItem {
  /**
   * Unique identifier of the profile association
   */
  private String associationId;
  /**
   * Unique identifier of the master profile
   */
  private String masterId;
  /**
   * Unique identifier of the detail profile
   */
  private String detailId;
  /**
   * Type of the detail profile
   */
  private ProfileSnapshotWrapper.ContentType detailType;
  /**
   * Profile content (plain Object)
   */
  private Object detail;

  public String getAssociationId() {
    return associationId;
  }

  public void setAssociationId(String associationId) {
    this.associationId = associationId;
  }

  public String getMasterId() {
    return masterId;
  }

  public void setMasterId(String masterId) {
    this.masterId = masterId;
  }

  public String getDetailId() {
    return detailId;
  }

  public void setDetailId(String detailId) {
    this.detailId = detailId;
  }

  public ProfileSnapshotWrapper.ContentType getDetailType() {
    return detailType;
  }

  public void setDetailType(ProfileSnapshotWrapper.ContentType detailType) {
    this.detailType = detailType;
  }

  public Object getDetail() {
    return detail;
  }

  public void setDetail(Object detail) {
    this.detail = detail;
  }
}
