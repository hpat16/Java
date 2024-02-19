/**
 * A class that defines a single meteorite and exposes properties required by the frontend: name,
 * latitude, longitude, fall, and mass (in grams).
 */
public class MeteoriteObject implements MeteoriteObjectInterface {

  // the name of this meteorite
  private String name;
  // the latitude position of this meteorite
  private double latitude;
  // the longitude position of this meteorite
  private double longitude;
  // the fall status of this meteorite
  private String fall;
  // the mass of this meteorite
  private double mass;

  public MeteoriteObject(String name, double latitude, double longitude,
      String fall, double mass) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.fall = fall;
    this.mass = mass;
  }

  public MeteoriteObject() {}

  /**
   * Return the name of the meteorite
   * @return The name of the meteorite
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Return the latitude of the meteorite
   * @return latitude of the meteorite
   */
  @Override
  public double getLatitude() {
    return this.latitude;
  }

  /**
   * Return the Longitude of the meteorite
   * @return Longitude of the meteorite
   */
  @Override
  public double getLongitude() {
    return this.longitude;
  }

  /**
   * Return the fall status of the meteorite
   * @return fall status of the meteorite
   */
  @Override
  public String getFall() {
    return this.fall;
  }

  /**
   * Return the mass of the meteorite
   * @return the mass of the meteorite
   */
  @Override
  public double getMass() {
    return this.mass;
  }

  /**
   * Sets the name of the meteorite
   * @param name The name of the meteorite
   */
  @Override
  public void setName(String name) { this.name = name; }

  /**
   * Sets the latitude of the meteorite
   * @param latitude The latitude of the meteorite
   */
  @Override
  public void setLatitude(double latitude) { this.latitude = latitude; }

  /**
   * Sets the longitude of the meteorite
   * @param longitude The longitude of the meteorite
   */
  @Override
  public void setLongitude(double longitude) { this.longitude = longitude; }

  /**
   * Sets the fall status of the meteorite
   * @param fall the fall status of the meteorite
   */
  @Override
  public void setFall(String fall) { this.fall = fall; }

  /**
   * Sets the mass of the meteorite
   * @param mass the mass of the meteorite
   */
  @Override
  public void setMass(double mass) { this.mass = mass; }

  /**
   * Equals method meant to "override" the object class' equals method (for the purpose of using in
   * testers since it is more convenient to have one equal method than having to check every field
   * to conclude that two Meteorite objects are equal)
   * @param other Object implementing MeteoriteObjectInterface
   * @return true if two MeteoriteObjectInterface objects are equal (have the same data in fields)
   *         otherwise, false
   */
  @Override
  public boolean equals(Object other) {
     if (other instanceof MeteoriteObjectInterface) {
       return this.name.equals(((MeteoriteObject) other).name) &&
           ((Double) this.mass).equals(((MeteoriteObject) other).mass) &&
           this.fall.equals(((MeteoriteObject) other).fall) &&
           ((Double) this.latitude).equals(((MeteoriteObject) other).latitude) &&
           ((Double) this.longitude).equals(((MeteoriteObject) other).longitude);
     } else {
       return false;
     }
  }

  /**
   * Compares two Meteorite objects based on their masses.
   *
   * @param other the other meteorite object to be compared.
   * @return the value 0 if other meteorite is equal in terms of mass to this meteorite; a value
   *     less than 0 if this meteorite has less mass than other meteorite; and a value greater than
   *     0 if this meteorite has greater mass than other meteorite.
   */
  @Override
  public int compareTo(MeteoriteObjectInterface other) {
     return Double.compare(this.mass, other.getMass());
  }


}
