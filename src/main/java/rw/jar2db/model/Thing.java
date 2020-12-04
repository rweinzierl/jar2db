package rw.jar2db.model;

import java.util.Objects;

public abstract class Thing {
    public abstract String getLongName();

    @Override
    public String toString() {
        return getLongName();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Thing otherThing = (Thing) other;
        return getLongName().equals(otherThing.getLongName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLongName());
    }
}
