package com.varentechnologies.pagingmissioncontrol.helpers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ResultCaptor<T> implements Answer {
    private T result = null;
    public T getResult() {
        return result;
    }

    @Override
    public T answer(InvocationOnMock invocationOnMock) throws Throwable {
        result = (T) invocationOnMock.callRealMethod();
        return result;
    }
}
