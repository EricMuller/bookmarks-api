package com.emu.apps.shared.metrics.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Component
public class TimerAopConfiguration {

    @Pointcut("@annotation(com.emu.apps.shared.metrics.Timer))")
    public void timerAnnotations() {
    }

    @Pointcut("execution(public !void org.springframework.data.repository.Repository+.*(..))")
    public void repositoryMethods() {
    }

    @Pointcut("repositoryMethods() || timerAnnotations()")
    public void monitor() {

    }

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor(false);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.emu.apps.shared.metrics.config.TimerAopConfiguration.timerAnnotations()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }

}