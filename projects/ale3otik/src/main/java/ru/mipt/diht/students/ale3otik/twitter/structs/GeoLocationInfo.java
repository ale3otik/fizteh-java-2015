package ru.mipt.diht.students.ale3otik.twitter.structs;

import twitter4j.GeoLocation;

import java.util.Objects;

/**
 * Created by alex on 10.10.15.
 */
public final class GeoLocationInfo {
    private final GeoLocation location;
    private final double radius;

    public GeoLocationInfo(final GeoLocation newLocation,
                           final double newRadius) {
        radius = newRadius;
        location = newLocation;
    }

    public double getRadius() {
        return radius;
    }

    public GeoLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        GeoLocationInfo second = (GeoLocationInfo) obj;
        return this.location.equals(second.location)
                && (Double.compare(this.radius, second.radius) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.location, this.radius);
    }
}
