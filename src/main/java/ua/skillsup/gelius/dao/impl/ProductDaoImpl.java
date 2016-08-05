package ua.skillsup.gelius.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.skillsup.gelius.dao.ProductDao;
import ua.skillsup.gelius.dao.entities.Product;
import ua.skillsup.gelius.dto.ProductDto;
import ua.skillsup.gelius.dto.ProductsFilteringAndSortingDTO;

import java.util.ArrayList;
import java.util.List;

import static ua.skillsup.gelius.converters.EntityDtoConverter.convert;


@Repository
@Transactional
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Long createProduct(ProductDto productDto) {
        Product product = convert(productDto);
        return (Long) sessionFactory.getCurrentSession().save(product);
    }

    @Override
    public void editProduct(ProductDto productDto) {
        Product product = convert(productDto);
        sessionFactory.getCurrentSession().merge(product);
    }

    @Override
    public List<ProductDto> findAll() {
        /*List<Product> products = sessionFactory
                .getCurrentSession()
                .createQuery("from Product")
                .list();*/
        List<Product> products = sessionFactory.getCurrentSession().createCriteria(Product.class).list();
        List<ProductDto> result = new ArrayList<>(products.size());
        for (Product product : products) {
            result.add(convert(product));
        }
        return result;
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = (Product) sessionFactory
                .getCurrentSession()
                .createQuery("select i from Product i where i.id = :id")
                .setParameter("id", id)
                .uniqueResult();

        if (product == null) {
            return null;
        } else {
            return convert(product);
        }
    }

    @Override
    public List<ProductDto> findByClient(String client) {
        List<Product> products = sessionFactory.getCurrentSession().createQuery("from Product").list();
        List<ProductDto> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getClient().getCompanyName().equals(client)) {
                result.add(convert(product));
            }
        }
        return result;
    }

    @Override
    public ProductDto findByName(String name) {
        Product product = (Product) sessionFactory.getCurrentSession().createQuery("select n from Product n where n.name = :name")
                .setParameter("name", name);
        if (product == null) {
            return null;
        } else {
            return convert(product);
        }

    }

    @Override
    public List<ProductDto> findByCardboardBrand(String cardboardBrand) {
        List<Product> products = sessionFactory.getCurrentSession().createQuery("select cb " +
                "from Product cb where cb.cardboardBrend = :cardboardBrend").list();
        List<ProductDto> result = new ArrayList<>(products.size());
        for (Product product : products) {
            if (product.getCardboardBrandID().equals(cardboardBrand))
                result.add(convert(product));
        }
        return result;

    }

    @Override
    public List<ProductDto> findByProfile(String profileID) {
        List<Product> products = sessionFactory.getCurrentSession().createQuery("select p from Product p where p.profileID = :profileID").list();
        List<ProductDto> result = new ArrayList<>(products.size());
        for (Product product : products) {
            if (product.getProfileID().equals(profileID))
                result.add(convert(product));
        }
        return result;
    }

    @Override
    public List<ProductDto> findByColour(String colour) {
        List<Product> products = sessionFactory.getCurrentSession().createQuery("select c from Product c where c.colour = :colour").list();
        List<ProductDto> result = new ArrayList<>(products.size());
        for (Product product : products) {
            if (product.getColour().equals(colour))
                result.add(convert(product));
        }
        return result;
    }

    @Override
    public List<ProductDto> findByActivity(Character activity) {
        List<Product> products = sessionFactory.getCurrentSession().createQuery("select a from Product a where a.activity = :activity").list();
        List<ProductDto> result = new ArrayList<>(products.size());
        for (Product product : products) {
            if (product.getActivity().equals(activity))
                result.add(convert(product));
        }
        return result;
    }

    @Override
    public List<ProductDto> findByFilterAndSorting(ProductsFilteringAndSortingDTO filter) {
        Criteria criteria = getFilterCriteria(filter);
        criteria = getSortingCriteria(criteria, filter);
        List<Product> products = criteria.list();
        List<ProductDto> result = new ArrayList<ProductDto>(products.size());
        for (Product product : products) {
            result.add(convert(product));
        }
        return result;
    }

    @Override
    public List findFilterParameters(ProductsFilteringAndSortingDTO filter, String filterName) {
        Criteria criteria = getFilterCriteria(filter);
        switch (filterName) {
            case "ids":
                criteria.setProjection(Projections.distinct(Projections.property("id")));
                criteria.addOrder(Order.asc("id"));
                break;
            case "clients":
                criteria.setProjection(Projections.distinct(Projections.property("client.companyName")));
                criteria.addOrder(Order.asc("client.companyName"));
                break;
            case "names":
                criteria.setProjection(Projections.distinct(Projections.property("productsName")));
                criteria.addOrder(Order.asc("productsName"));
                break;
            case "types":
                criteria.setProjection(Projections.distinct(Projections.property("productsTypeID")));
                criteria.addOrder(Order.asc("productsTypeID"));
                break;
            case "lengths":
                criteria.setProjection(Projections.distinct(Projections.property("innerLength")));
                criteria.addOrder(Order.asc("innerLength"));
                break;
            case "widths":
                criteria.setProjection(Projections.distinct(Projections.property("innerWidth")));
                criteria.addOrder(Order.asc("innerWidth"));
                break;
            case "heights":
                criteria.setProjection(Projections.distinct(Projections.property("innerHeight")));
                criteria.addOrder(Order.asc("innerHeight"));
                break;
            case "grades":
                criteria.setProjection(Projections.distinct(Projections.property("cardboardBrandID")));
                criteria.addOrder(Order.asc("cardboardBrandID"));
                break;
            case "profiles":
                criteria.setProjection(Projections.distinct(Projections.property("profileID")));
                criteria.addOrder(Order.asc("profileID"));
                break;
            case "colours":
                criteria.setProjection(Projections.distinct(Projections.property("colour")));
                criteria.addOrder(Order.asc("colour"));
                break;
            case "prints":
                criteria.setProjection(Projections.distinct(Projections.property("printID")));
                criteria.addOrder(Order.asc("printID"));
                break;
        }
        return criteria.list();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = (Product) sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product WHERE :id = id")
                .setParameter("id", id)
                .uniqueResult();

        sessionFactory.getCurrentSession().delete(product);

    }

    private Criteria getSortingCriteria(Criteria criteria, ProductsFilteringAndSortingDTO filter) {
        if (filter.getSortableColumn() != null)
        switch (filter.getSortableColumn()) {
            case "ids":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("id"));
                else
                    criteria.addOrder(Order.desc("id"));
                break;
            case "clients":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("client.companyName"));
                else
                    criteria.addOrder(Order.desc("client.companyName"));
                break;
            case "names":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("productsName"));
                else
                    criteria.addOrder(Order.desc("productsName"));
                break;
            case "types":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("productsType"));
                else
                    criteria.addOrder(Order.desc("productsType"));
                break;
            case "lengths":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("innerLength"));
                else
                    criteria.addOrder(Order.desc("innerLength"));
                break;
            case "widths":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("innerWidth"));
                else
                    criteria.addOrder(Order.desc("innerWidth"));
                break;
            case "heights":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("innerHeight"));
                else
                    criteria.addOrder(Order.desc("innerHeight"));
                break;
            case "grades":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("grade"));
                else
                    criteria.addOrder(Order.desc("grade"));
                break;
            case "profiles":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("profile"));
                else
                    criteria.addOrder(Order.desc("profile"));
                break;
            case "colours":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("colour"));
                else
                    criteria.addOrder(Order.desc("colour"));
                break;
            case "prints":
                if (filter.getSortingDirection().equals("asc"))
                    criteria.addOrder(Order.asc("print"));
                else
                    criteria.addOrder(Order.desc("print"));
                break;
        }

        return criteria;
    }

    private Criteria getFilterCriteria(ProductsFilteringAndSortingDTO filter) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class, "product");
        criteria.createAlias("product.client", "client");
        if (!filter.isEmpty()) {
            if (!filter.getIds().isEmpty()) {
                criteria.add(Restrictions.in("product.id", filter.getIds()));
            }
            if (!filter.getClients().isEmpty()) {
                criteria.add(Restrictions.in("client.companyName", filter.getClients()));
            }
            if (!filter.getNames().isEmpty()) {
                criteria.add(Restrictions.in("product.productsName", filter.getNames()));
            }
            if (!filter.getTypes().isEmpty()) {
                criteria.add(Restrictions.in("product.productsTypeID", filter.getTypes()));
            }
            if (!filter.getLengths().isEmpty()) {
                criteria.add(Restrictions.in("product.innerLength", filter.getLengths()));
            }
            if (!filter.getWidths().isEmpty()) {
                criteria.add(Restrictions.in("product.innerWidth", filter.getWidths()));
            }
            if (!filter.getHeights().isEmpty()) {
                criteria.add(Restrictions.in("product.innerHeight", filter.getHeights()));
            }
            if (!filter.getGrades().isEmpty()) {
                criteria.add(Restrictions.in("product.cardboardBrandID", filter.getGrades()));
            }
            if (!filter.getProfiles().isEmpty()) {
                criteria.add(Restrictions.in("product.profileID", filter.getProfiles()));
            }
            if (!filter.getColours().isEmpty()) {
                criteria.add(Restrictions.in("product.colour", filter.getColours()));
            }
            if (!filter.getPrints().isEmpty()) {
                criteria.add(Restrictions.in("product.print", filter.getPrints()));
            }
        }
        return criteria;
    }
}
