package com.kent.gmail.com.runtime.security;

public enum Roles {
  User(false),

  Admin(true);

  private final boolean admin;

  Roles(boolean admin) {
    this.admin = admin;
  }

  public boolean isAdmin() {
    return admin;
  }
}
