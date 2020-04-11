package ru.geekbrains.supershop.aspects;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import ru.geekbrains.supershop.exceptions.WrongCaptchaCodeException;
import ru.geekbrains.supershop.persistence.pojo.ReviewPojo;

import javax.servlet.http.HttpSession;

@Slf4j
@Aspect
@Configuration
public class CaptchaAspect {

    @Pointcut("within(ru.geekbrains.supershop.controllers..*)")
    public void controllerLayer() {}

    @Before(value = "controllerLayer() && " + "args(reviewPojo, session,..)", argNames = "reviewPojo, session")
    public void checkCaptcha(ReviewPojo reviewPojo, HttpSession session) throws WrongCaptchaCodeException {
        if (!reviewPojo.getCaptchaCode().equals(session.getAttribute("captchaCode"))) {
            throw new WrongCaptchaCodeException("Error! Captcha code is incorrect! Please try again!");
        }
    }

}