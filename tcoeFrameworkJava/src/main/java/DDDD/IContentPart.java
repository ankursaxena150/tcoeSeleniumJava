package DDDD;

import com4j.DISPID;
import com4j.IID;
import com4j.VTID;

/**
 * Data on an entity type in a library.
 */
@IID("{3EC71871-100A-4646-AB64-8328A92B4872}")
public interface IContentPart extends IBaseField {
  // Methods:
  /**
   * <p>
   * The ID of the object that owns this content part.
   * </p>
   * <p>
   * Getter method for the COM property "HolderId"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(14) //= 0xe. The runtime will prefer the VTID if present
  @VTID(20)
  int holderId();


  /**
   * <p>
   * The type of the object that owns this content part.
   * </p>
   * <p>
   * Getter method for the COM property "HolderType"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(15) //= 0xf. The runtime will prefer the VTID if present
  @VTID(21)
  String holderType();


  /**
   * <p>
   * The Content Part Name.
   * </p>
   * <p>
   * Getter method for the COM property "Name"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(16) //= 0x10. The runtime will prefer the VTID if present
  @VTID(22)
  String name();


  /**
   * <p>
   * The Content Part Name.
   * </p>
   * <p>
   * Setter method for the COM property "Name"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(16) //= 0x10. The runtime will prefer the VTID if present
  @VTID(23)
  void name(
          String pVal);


  /**
   * <p>
   * The entity type represented by this content part.
   * </p>
   * <p>
   * Getter method for the COM property "EntityType"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(17) //= 0x11. The runtime will prefer the VTID if present
  @VTID(24)
  String entityType();


  /**
   * <p>
   * The entity type represented by this content part.
   * </p>
   * <p>
   * Setter method for the COM property "EntityType"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(17) //= 0x11. The runtime will prefer the VTID if present
  @VTID(25)
  void entityType(
          String pVal);


  /**
   * <p>
   * The Content Part Filter.
   * </p>
   * <p>
   * Getter method for the COM property "Filter"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(18) //= 0x12. The runtime will prefer the VTID if present
  @VTID(26)
  String filter();


  /**
   * <p>
   * The Content Part Filter.
   * </p>
   * <p>
   * Setter method for the COM property "Filter"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(18) //= 0x12. The runtime will prefer the VTID if present
  @VTID(27)
  void filter(
          String pVal);


  // Properties:
}
