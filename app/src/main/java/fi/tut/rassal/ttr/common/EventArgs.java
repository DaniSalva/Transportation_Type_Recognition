package fi.tut.rassal.ttr.common;

public interface EventArgs {
  //region Nested classes

  /**
   * For events without its own args
   */
  class Empty implements EventArgs {
    public static final Empty Instance = new Empty();

    private Empty() {
    }
  }

  //endregion
}
