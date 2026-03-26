const {
  registerUser,
  loginUser,
  verifyAdminCredentials,
  requestPasswordReset,
  resetPassword,
} = require("../store-mysql");

/**
 * Authentication business service.
 * Delegates to existing domain logic in store-mysql while keeping route handlers thin.
 */
const authService = {
  /**
   * Register a new customer account.
   * @param {Object} payload Registration payload
   * @returns {Promise<Object>} Registration result
   */
  register(payload) {
    return registerUser(payload || {});
  },

  /**
   * Authenticate a customer/admin.
   * @param {Object} payload Login payload
   * @returns {Promise<Object>} Authenticated profile
   */
  login(payload) {
    return loginUser(payload || {});
  },

  /**
   * Verify admin credentials for lock screen access.
   * @param {Object} payload Admin credential payload
   * @returns {Promise<Object>} Verification result
   */
  verifyAdmin(payload) {
    return verifyAdminCredentials(payload || {});
  },

  /**
   * Initiate password reset via OTP.
   * @param {Object} payload Reset request payload
   * @returns {Promise<Object>} Reset initiation metadata
   */
  forgotPassword(payload) {
    return requestPasswordReset(payload || {});
  },

  /**
   * Complete password reset with OTP verification.
   * @param {Object} payload Reset confirmation payload
   * @returns {Promise<Object>} Reset status
   */
  resetPassword(payload) {
    return resetPassword(payload || {});
  },
};

module.exports = authService;