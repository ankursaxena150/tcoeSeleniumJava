package DDDD;

import com4j.DISPID;
import com4j.IID;
import com4j.VTID;

/**
 * Business Process Model Element.
 */
@IID("{B2930187-40A3-4C14-A8E1-18D19FA24B85}")
public interface IBPModelElement extends DDDD.IBaseFieldExMail {
  // Methods:
  /**
   * <p>
   * The Business Process ModelElement's name
   * </p>
   * <p>
   * Getter method for the COM property "Name"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(15) //= 0xf. The runtime will prefer the VTID if present
  @VTID(24)
  String name();


  /**
   * <p>
   * The Business Process ModelElement's name
   * </p>
   * <p>
   * Setter method for the COM property "Name"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(15) //= 0xf. The runtime will prefer the VTID if present
  @VTID(25)
  void name(
          String pVal);


  /**
   * <p>
   * The description of the BPModel element.
   * </p>
   * <p>
   * Getter method for the COM property "Description"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(16) //= 0x10. The runtime will prefer the VTID if present
  @VTID(26)
  String description();


  /**
   * <p>
   * The description of the BPModel element.
   * </p>
   * <p>
   * Setter method for the COM property "Description"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(16) //= 0x10. The runtime will prefer the VTID if present
  @VTID(27)
  void description(
          String pVal);


  // Properties:
}
