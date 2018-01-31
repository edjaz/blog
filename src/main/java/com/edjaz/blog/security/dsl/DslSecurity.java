package com.edjaz.blog.security.dsl;

import com.edjaz.blog.repository.BlogRepository;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class DslSecurity extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final BlogRepository blogRepository;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public Object getFilterObject() {
        return this.filterObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getReturnObject() {
        return this.returnObject;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return this.target;
    }


    public DslSecurity(Authentication authentication, BlogRepository blogRepository) {
        super(authentication);
        this.blogRepository = blogRepository;
    }

    public boolean isFirstSetup() {
        return blogRepository.count() == 0;
    }

}
