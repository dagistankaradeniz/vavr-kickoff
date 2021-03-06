import helpers.MethodExecutor
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static OptionMonad.DEFAULT

class OptionMonadSpec extends Specification {

  def exec = Mock(MethodExecutor)

  @Subject
  OptionMonad option = new OptionMonad(exec)

  @Unroll
  def "should legacy null check work with #input"() {
    when:
    def result = option.legacyNullCheck(input)

    then:
    result == expectedResult

    where:
    input   | expectedResult
    null    | null
    "InPuT" | "input"
  }

  @Unroll
  def "should vavr null check work with #input"() {
    when:
    def result = option.vavrNullCheck(input)

    then:
    result == expectedResult

    where:
    input   | expectedResult
    null    | null
    "InPuT" | "input"
  }

  def "should legacy null conditional check work with null value"() {
    when:
    option.legacyNullConditionalExecution(null)

    then:
    0 * exec.methodOne(_)
  }

  def "should legacy null conditional check work with non null value"() {
    given:
    def input = "non value"
    when:
    option.legacyNullConditionalExecution(input)

    then:
    1 * exec.methodOne(input)
  }

  def "should vavr null conditional check work with null value"() {
    when:
    option.vavrNullConditionalExecution(null)

    then:
    0 * exec.methodOne(_)
  }

  def "should vavr null conditional check work with non null value"() {
    given:
    def input = "non value"
    when:
    option.vavrNullConditionalExecution(input)

    then:
    1 * exec.methodOne(input)
  }

  def "should legacy null conditional execution of the same method work with null value"() {
    when:
    option.legacyConditionalExecutionOfTheSameMethod(null)

    then:
    1 * exec.methodOne(DEFAULT)
  }

  def "should legacy null conditional execution of the same method work with non null value"() {
    given:
    def input = "input"

    when:
    option.legacyConditionalExecutionOfTheSameMethod(input)

    then:
    1 * exec.methodOne(input)
  }

  def "should vavr null conditional execution of the same method work with null value"() {
    when:
    option.vavrConditionalExecutionOfTheSameMethod(null)

    then:
    1 * exec.methodOne(DEFAULT)
  }

  def "should vavr null conditional execution of the same method work with non null value"() {
    given:
    def input = "input"

    when:
    option.vavrConditionalExecutionOfTheSameMethod(input)

    then:
    1 * exec.methodOne(input)
  }

  def "should legacy null conditional execution of different methods work with null value"() {
    when:
    option.legacyConditionalExecutionOfDifferentMethods(null)

    then:
    1 * exec.methodOne(DEFAULT)
  }

  def "should legacy null conditional execution of different methods work with non null value"() {
    given:
    def input = "input"

    when:
    option.legacyConditionalExecutionOfDifferentMethods(input)

    then:
    1 * exec.methodTwo(input)
  }

  def "should vavr null conditional execution of different methods work with null value"() {
    when:
    option.vavrConditionalExecutionOfDifferentMethods(null)

    then:
    1 * exec.methodOne(DEFAULT)
  }

  def "should vavr null conditional execution of different methods work with non null value"() {
    given:
    def input = "input"

    when:
    option.vavrConditionalExecutionOfDifferentMethods(input)

    then:
    1 * exec.methodTwo(input)
  }

  def "should legacy throw exception on null value"() {
    when:
    option.legacyConditionalException(null)

    then:
    RuntimeException ex = thrown()
    ex.message == "don't like nulls"
  }

  def "should legacy not throw exception on non null value"() {
    given:
    def input = "input"

    when:
    option.legacyConditionalException(input)

    then:
    1 * exec.methodOne(input)
  }

  def "should vavr throw exception on null value"() {
    when:
    option.vavrConditionalException(null)

    then:
    RuntimeException ex = thrown()
    ex.message == "don't like nulls"
  }

  def "should vavr not throw exception on non null value"() {
    given:
    def input = "input"

    when:
    option.vavrConditionalException(input)

    then:
    1 * exec.methodOne(input)
  }

  @Unroll
  def "should legacy complex AND condition work with #input"() {
    when:
    def result = option.legacyComplexAndConditional(input)

    then:
    result == expectedResult

    where:
    input    | expectedResult
    null     | DEFAULT
    "ONEabc" | "1st condition"
    "abcONE" | DEFAULT
  }

  @Unroll
  def "should vavr complex AND condition work with #input"() {
    when:
    def result = option.vavrComplexAndConditional(input)

    then:
    result == expectedResult

    where:
    input    | expectedResult
    null     | DEFAULT
    "ONEabc" | "1st condition"
    "abcONE" | DEFAULT
  }

  @Unroll
  def "should legacy nested conditions work with #input"() {
    when:
    def result = option.legacyNestedConditionsWithReturn(input)

    then:
    result == expectedResult

    where:
    input  | expectedResult
    null   | DEFAULT
    "ONE"  | "ONE123"
    "TWO" | "TWO456"
    "XYZ"  | "XYZ789"
  }

  @Unroll
  def "should vavr nested conditions work with #input"() {
    when:
    def result = option.vavrNestedConditionsWithReturn(input)

    then:
    result == expectedResult

    where:
    input  | expectedResult
    null   | DEFAULT
    "ONE"  | "ONE123"
    "TWO" | "TWO456"
    "XYZ"  | "XYZ789"
  }


  def "should legacy nested conditions with multiple values work"() {
    when:
    option.legacyNestedConditionWithMultipleValues(null, null)
    then:
    1 * exec.methodOne(DEFAULT)

    when:
    option.legacyNestedConditionWithMultipleValues(null, "ONE")
    then:
    1 * exec.methodOne("ONE")

    when:
    option.legacyNestedConditionWithMultipleValues("TWO", null)
    then:
    1 * exec.methodTwo("TWO")

    when:
    option.legacyNestedConditionWithMultipleValues("TWO", "ONE")
    then:
    1 * exec.methodThree(DEFAULT)
  }

  def "should vavr nested conditions with multiple values work"() {
    when:
    option.vavrNestedConditionWithMultipleValues(null, null)
    then:
    1 * exec.methodOne(DEFAULT)

    when:
    option.vavrNestedConditionWithMultipleValues(null, "ONE")
    then:
    1 * exec.methodOne("ONE")

    when:
    option.vavrNestedConditionWithMultipleValues("TWO", null)
    then:
    1 * exec.methodTwo("TWO")

    when:
    option.vavrNestedConditionWithMultipleValues("TWO", "ONE")
    then:
    1 * exec.methodThree(DEFAULT)
  }

  def "should legacy nested conditions with execution work"() {
    when:
    option.legacyNestedConditionsWithExecution(null)
    then:
    1 * exec.methodOne(DEFAULT)

    when:
    option.legacyNestedConditionsWithExecution("ONE")
    then:
    1 * exec.methodOne("ONE")

    when:
    option.legacyNestedConditionsWithExecution("TWO")
    then:
    1 * exec.methodTwo("TWO")

    when:
    option.legacyNestedConditionsWithExecution("XYZ")
    then:
    1 * exec.methodThree("XYZ")
  }

  def "should vavr nested conditions with execution work"() {
    when:
    option.vavrNestedConditionsWithExecution(null)
    then:
    1 * exec.methodOne(DEFAULT)

    when:
    option.vavrNestedConditionsWithExecution("ONE")
    then:
    1 * exec.methodOne("ONE")

    when:
    option.vavrNestedConditionsWithExecution("TWO")
    then:
    1 * exec.methodTwo("TWO")

    when:
    option.vavrNestedConditionsWithExecution("XYZ")
    then:
    1 * exec.methodThree("XYZ")
  }

  @Unroll
  def "should legacy complex OR condition work with #input"() {
    when:
    def result = option.legacyComplexOrConditional(input)

    then:
    result == expectedResult

    where:
    input    | expectedResult
    null     | "1st condition"
    "ONEabc" | "1st condition"
    "abcONE" | DEFAULT
  }

  @Unroll
  def "should vavr complex OR condition work with #input"() {
    when:
    def result = option.vavrComplexOrConditional(input)

    then:
    result == expectedResult

    where:
    input    | expectedResult
    null     | "1st condition"
    "ONEabc" | "1st condition"
    "abcONE" | DEFAULT
  }

}