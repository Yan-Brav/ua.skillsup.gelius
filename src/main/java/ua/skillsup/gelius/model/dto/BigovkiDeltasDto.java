package ua.skillsup.gelius.model.dto;

import ua.skillsup.gelius.model.dto.dictionary.ProfileDto;

public class BigovkiDeltasDto {

    private Long id;
    private ProfileDto profile;
    private Double delta1;
    private Double delta2;
    private Double delta3;

    public BigovkiDeltasDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileDto getProfile() {
        return profile;
    }

    public void setProfile(ProfileDto profile) {
        this.profile = profile;
    }

    public Double getDelta1() {
        return delta1;
    }

    public void setDelta1(Double delta1) {
        this.delta1 = delta1;
    }

    public Double getDelta2() {
        return delta2;
    }

    public void setDelta2(Double delta2) {
        this.delta2 = delta2;
    }

    public Double getDelta3() {
        return delta3;
    }

    public void setDelta3(Double delta3) {
        this.delta3 = delta3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigovkiDeltasDto that = (BigovkiDeltasDto) o;

        return profile.equals(that.profile);

    }

    @Override
    public int hashCode() {
        return profile.hashCode();
    }

    @Override
    public String toString() {
        return "BigovkiDeltasDto{" +
            "id=" + id +
            ", profile=" + profile +
            ", delta1=" + delta1 +
            ", delta2=" + delta2 +
            ", delta3=" + delta3 +
            '}';
    }
}
