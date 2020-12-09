package com.example.kubermarket.domain;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepositoryJpa{

    private final EntityManager em;

    public CategoryRepositoryImpl(EntityManager em) {
        this.em=em;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories= em.createQuery("select cate from Category cate",Category.class).getResultList();
        return categories;
    }

    @Override
    public void save(Category category) {
        em.persist(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(em.find(Category.class,id));
    }

    @Override
    public void deleteById(Long id) {
        em.createQuery("delete from Category cate where cate.id = :id",Category.class);
    }

    @Override
    public Optional<Category> findByName(String name) {
        List<Category> result= em.createQuery("select cate from Category cate where cate.name=:name",Category.class)
               .setParameter("name",name)
                .getResultList();
        return result.stream().findAny();
    }
}
