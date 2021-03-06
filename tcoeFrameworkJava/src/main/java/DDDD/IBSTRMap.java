package DDDD;

import com4j.*;

/**
 * For HP use. Represents a map with BSTR keys and BSTR values
 */
@IID("{B3D8DB4D-66D4-48C1-A578-E8264F401A4E}")
public interface IBSTRMap extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Add key-value pair to the map
   * </p>
   * @param key Mandatory java.lang.String parameter.
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  void put(
          String key,
          String value);


  /**
   * <p>
   * Get a value by key
   * </p>
   * @param key Mandatory java.lang.String parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  String get(
          String key);


  /**
   * <p>
   * Get a key list
   * </p>
   * <p>
   * Getter method for the COM property "KeyList"
   * </p>
   * @return  Returns a value of type DDDD.IList
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(9)
  IList keyList();


  @VTID(9)
  @ReturnValue(type=NativeType.VARIANT,defaultPropertyThrough={IList.class})
  Object keyList(
          int index);

  // Properties:
}
