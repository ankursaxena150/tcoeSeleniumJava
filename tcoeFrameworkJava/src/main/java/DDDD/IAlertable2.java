package DDDD;

import com4j.*;

/**
 * Services for managing auto-alerts.
 */
@IID("{B19C1039-D667-48D1-81B0-C864E9CC3109}")
public interface IAlertable2 extends IAlertable {
  // Methods:
  /**
   * <p>
   * Adds a new alert to the specified entity.
   * </p>
   * @param changedEntityType Mandatory java.lang.String parameter.
   * @param changedEntityID Mandatory int parameter.
   * @param alertSubject Mandatory java.lang.String parameter.
   * @param alertDescription Mandatory java.lang.String parameter.
   * @return  Returns a value of type com4j.Com4jObject
   */

  @DISPID(1610743808) //= 0x60020000. The runtime will prefer the VTID if present
  @VTID(9)
  @ReturnValue(type=NativeType.Dispatch)
  Com4jObject addAlert(
          String changedEntityType,
          int changedEntityID,
          String alertSubject,
          String alertDescription);


  // Properties:
}
