package com.design_pattern.sidecar_pattern.Handler;

public class ErrorHandler {
    public static void logErrorAndThrow(String className, String methodName, Exception ex) {
        System.out.println("Error in class: " + className);
        System.out.println("Error in method: " + methodName);
        System.out.println("Error message: " + ex.getMessage());
        throw new RuntimeException("Error in class: " + className + " Error in method: " + methodName + " Error message: " + ex.getMessage());
    }
}
