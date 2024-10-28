package com.smd.ufccursos.domain.entity;

public enum UserRole{
  ADMIN("admin"),
  STUDENT("student");

  private String role;
  UserRole(String role){
    this.role = role;
  }
  public String getRole(){
    return role;
  }
}
