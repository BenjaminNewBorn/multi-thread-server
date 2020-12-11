package container;

import http.HttpMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.logging.Logger;

public class TargetMethod {
    private static final Logger logger = Logger.getLogger(TargetMethod.class.getPackageName());

    private final String methodDescription;
    private final Object controller;
    private final Method method;
    private final Parameter[] parameters;
    private final HttpMethod httpMethod;

    TargetMethod(Object controller, Method method, HttpMethod httpMethod){
        this.controller = controller;
        this.method = method;
        this.parameters = method.getParameters();
        this.methodDescription = method.getDeclaringClass().getName() + ":" + method.getName();
        this.httpMethod = httpMethod;
    }



}
