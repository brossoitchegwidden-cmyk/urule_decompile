package com.bstek.urule.runtime;

/**
 * Immutable result of validating a vendor-issued URule license.
 */
public final class LicenseValidationResult {
   private final boolean valid;
   private final String error;
   private final String licensee;
   private final long limit;
   private final boolean unlimited;
   private final boolean expired;
   private final boolean portable;

   private LicenseValidationResult(boolean valid, String error, String licensee, long limit, boolean unlimited, boolean expired, boolean portable) {
      this.valid = valid;
      this.error = error;
      this.licensee = licensee;
      this.limit = limit;
      this.unlimited = unlimited;
      this.expired = expired;
      this.portable = portable;
   }

   static LicenseValidationResult valid(String licensee, long limit, boolean portable) {
      return new LicenseValidationResult(true, null, licensee, limit, limit == -1L, limit >= 0L && System.currentTimeMillis() > limit, portable);
   }

   static LicenseValidationResult invalid(String error) {
      return new LicenseValidationResult(false, error, null, 0L, false, false, false);
   }

   public boolean isValid() {
      return this.valid;
   }

   public String getError() {
      return this.error;
   }

   public String getLicensee() {
      return this.licensee;
   }

   public long getLimit() {
      return this.limit;
   }

   public boolean isUnlimited() {
      return this.unlimited;
   }

   public boolean isExpired() {
      return this.expired;
   }

   public boolean isPortable() {
      return this.portable;
   }
}
