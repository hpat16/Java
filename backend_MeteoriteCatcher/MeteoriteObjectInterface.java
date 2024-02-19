/**
 * This is the interface of a type that represents a single meteorite (simple accessors and
 * mutators defined)
 */
public interface MeteoriteObjectInterface extends Comparable<MeteoriteObjectInterface> {

  /*
   * public MeteoriteObjectInterface (String name, double latitude, double longitude, String fall,
   *       double mass) {
   *     this.name = name;
   *     this.latitude = latitude;
   *     this.longitude = longitude;
   *     this.fall = fall;
   *     this.mass = mass;
   * }
   */

  /**
   * Return the name of the meteorite
   * @return The name of the meteorite
   */
  public String getName();

  /**
   * Return the latitude of the meteorite
   * @return latitude of the meteorite
   */
  public double getLatitude();

  /**
   * Return the Longitude of the meteorite
   * @return Longitude of the meteorite
   */
  public double getLongitude();

  /**
   * Return the fall status of the meteorite
   * @return fall status of the meteorite
   */
  public String getFall();

  /**
   * Return the mass of the meteorite
   * @return the mass of the meteorite
   */
  public double getMass();

  /**
   * Sets the name of the meteorite
   * @param name The name of the meteorite
   */
  public void setName(String name);

  /**
   * Sets the latitude of the meteorite
   * @param latitude The latitude of the meteorite
   */
  public void setLatitude(double latitude);

  /**
   * Sets the longitude of the meteorite
   * @param longitude The longitude of the meteorite
   */
  public void setLongitude(double longitude);

  /**
   * Sets the fall status of the meteorite
   * @param fall the fall status of the meteorite
   */
  public void setFall(String fall);

  /**
   * Sets the mass of the meteorite
   * @param mass the mass of the meteorite
   */
  public void setMass(double mass);

  /**
   * Equals method meant to "override" the object class' equals method (for the purpose of using in
   * testers since it is more convenient to have one equal method than having to check every field
   * to conclude that two Meteorite objects are equal)
   * @param other Object implementing MeteoriteObjectInterface
   * @return true if two MeteoriteObjectInterface objects are equal (have the same data in fields)
   *         otherwise, false
   */
  public boolean equals(Object other);
}
