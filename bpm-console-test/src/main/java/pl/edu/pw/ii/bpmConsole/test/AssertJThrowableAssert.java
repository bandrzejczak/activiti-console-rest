package pl.edu.pw.ii.bpmConsole.test;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;

public class AssertJThrowableAssert {
    public static ThrowableAssert assertThrown(ExceptionThrower exceptionThrower) {
        try {
            exceptionThrower.throwException();
        } catch (Throwable throwable) {
            return (ThrowableAssert) Assertions.assertThat(throwable);
        }
        throw new AssertionError("Expected exception was not thrown.");
    }
}