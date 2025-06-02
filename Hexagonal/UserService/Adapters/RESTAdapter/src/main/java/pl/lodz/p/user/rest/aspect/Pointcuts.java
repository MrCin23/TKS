package pl.lodz.p.user.rest.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut(
            "execution(@pl.lodz.p.user.rest.aspect.Counted * *(..)) " +
                    "|| within(@pl.lodz.p.user.rest.aspect.Counted *)"
    )
    public void methodCounted() {}

    @Pointcut("@annotation(pl.lodz.p.user.rest.aspect.Counted)")
    public void methodAnnotatedWithCounted() {}

    @Pointcut("@within(pl.lodz.p.user.rest.aspect.Counted)")
    public void classAnnotatedWithCounted() {}

    @Pointcut("methodAnnotatedWithCounted() || classAnnotatedWithCounted() || methodCounted()")
    public void countedMethodOrClass() {}
}
