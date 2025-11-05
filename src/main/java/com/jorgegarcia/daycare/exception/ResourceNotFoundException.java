package com.jorgegarcia.daycare.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String msg) { 
    super(msg); 
  }
}
