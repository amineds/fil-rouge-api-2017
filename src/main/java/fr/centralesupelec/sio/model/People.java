package fr.centralesupelec.sio.model;

import java.util.EnumSet;

public class People {

    private long id;
    private String firstName;
    private String lastName;
    private EnumSet<PeopleSpeciality> specialities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EnumSet<PeopleSpeciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(EnumSet<PeopleSpeciality> specialities) {
        this.specialities = specialities;
    }
}

