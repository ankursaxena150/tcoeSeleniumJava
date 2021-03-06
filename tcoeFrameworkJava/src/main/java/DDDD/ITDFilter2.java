package DDDD;

import com4j.DISPID;
import com4j.Holder;
import com4j.IID;
import com4j.VTID;

/**
 * Services for creation of a filtered list of field objects without SQL.
 */
@IID("{B14CE979-E8D8-426B-AFCF-B8AA9AFE267B}")
public interface ITDFilter2 extends ITDFilter {
  // Methods:
  /**
   * <p>
   * Sets the filter for second entity, called a cross filter.
   * </p>
   * @param crossEntities Mandatory java.lang.String parameter.
   * @param inclusive Mandatory boolean parameter.
   * @param filterText Mandatory java.lang.String parameter.
   */

  @DISPID(13) //= 0xd. The runtime will prefer the VTID if present
  @VTID(28)
  void setXFilter(
          String crossEntities,
          boolean inclusive,
          String filterText);


  /**
   * <p>
   * Gets the cross filter specified by CrossEntities.
   * </p>
   * @param crossEntities Mandatory java.lang.String parameter.
   * @param inclusive Mandatory Holder<Boolean> parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(14) //= 0xe. The runtime will prefer the VTID if present
  @VTID(29)
  String getXFilter(
          String crossEntities,
          Holder<Boolean> inclusive);


  /**
   * <p>
   * Checks whether the filter is clear, including cross-filter settings.
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(15) //= 0xf. The runtime will prefer the VTID if present
  @VTID(30)
  boolean isClear();


  // Properties:
}
