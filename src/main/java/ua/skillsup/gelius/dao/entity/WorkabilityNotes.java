package ua.skillsup.gelius.dao.entity;

import ua.skillsup.gelius.dao.entity.dictionary.Workability;

import javax.persistence.*;

@Entity
@Table(name = "workability_notes")
public class WorkabilityNotes {

    @Id
    @Column(name = "workability_notes_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="service_center")
    private Workability serviceCenter;

    public WorkabilityNotes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Workability getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(Workability serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorkabilityNotes{");
        sb.append("id=").append(id);
        sb.append(", product=").append(product);
        sb.append(", serviceCenter=").append(serviceCenter);
        sb.append('}');
        return sb.toString();
    }
}