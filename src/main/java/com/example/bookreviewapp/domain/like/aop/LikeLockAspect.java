package com.example.bookreviewapp.domain.like.aop;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
@Aspect
@Component
@RequiredArgsConstructor
public class LikeLockAspect {

    private final RedisLockService redisLockService;

    @Around("@annotation(likeLock)")
    public Object lockLikeOperation(ProceedingJoinPoint joinPoint, LikeLock likeLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String key = resolveKey(signature, joinPoint.getArgs(), likeLock.key());

        String lockId = redisLockService.lock(key);
        if (lockId == null) {
            throw new ApiException(ErrorStatus.TOO_MANY_REQUESTS); // 락 실패 시 429 에러
        }

        try {
            return joinPoint.proceed();
        } finally {
            redisLockService.unlock(key, lockId);
        }
    }

    private String resolveKey(MethodSignature signature, Object[] args, String spelKey) {
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = signature.getParameterNames();

        if (paramNames == null) {
            throw new IllegalStateException("Could not resolve method parameter names. Please ensure '-parameters' compiler flag is set.");
        }

        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(spelKey).getValue(context, String.class);
    }
}