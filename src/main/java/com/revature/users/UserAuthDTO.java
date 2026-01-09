package com.revature.users;

/**
 * (Theoretically) used to perform logins. Not actually used at the moment,
 * but can be implemented in future upgrades.
 */
public class UserAuthDTO {
		private String username;
		private String password;

		public UserAuthDTO() {

		}

		public UserAuthDTO(String username, String password) {
				this.username = username;
				this.password = password;
		}

		public String getUsername() {
				return username;
		}

		public void setUsername(String username) {
				this.username = username;
		}

		public String getPassword() {
				return password;
		}

		public void setPassword(String password) {
				this.password = password;
		}
}
