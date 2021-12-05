package com.cmas.solution.utils.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UserNotExistProblem extends AbstractThrowableProblem {

    public UserNotExistProblem(Long userId) {
        super(
                null,
                "USER NOT FOUND",
                Status.NOT_FOUND,
                String.format("User '%s' does not exist!", userId)
        );
    }

}
