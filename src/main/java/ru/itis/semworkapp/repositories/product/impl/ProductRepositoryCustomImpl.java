package ru.itis.semworkapp.repositories.product.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.TagEntity;
import ru.itis.semworkapp.repositories.product.ProductRepositoryCustom;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductEntity> searchByTitleDescriptionOrTags(String query, List<String> tagsNames, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> product = cq.from(ProductEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        if (query != null && !query.isBlank()) {
            String pattern = "%" + query.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(product.get("title")), pattern),
                    cb.like(cb.lower(product.get("description")), pattern)
            ));
        }

        if (tagsNames != null && !tagsNames.isEmpty()) {
            Subquery<Long> tagsSubquery = cq.subquery(Long.class);
            Root<ProductEntity> subProduct = tagsSubquery.from(ProductEntity.class);
            Join<ProductEntity, TagEntity> subTags = subProduct.join("tags", JoinType.INNER);
            tagsSubquery.select(subProduct.get("id"))
                    .where(
                            cb.and(
                                    cb.equal(subProduct.get("id"), product.get("id")),
                                    subTags.get("name").in(tagsNames)
                            )
                    );
            predicates.add(cb.exists(tagsSubquery));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.orderBy(cb.desc(product.get("createdAt")));
        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<ProductEntity> products = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProductEntity> countRoot = countQuery.from(ProductEntity.class);

        List<Predicate> countPredicates = new ArrayList<>();
        if (query != null && !query.isBlank()) {
            String pattern = "%" + query.toLowerCase() + "%";
            countPredicates.add(cb.or(
                    cb.like(cb.lower(countRoot.get("title")), pattern),
                    cb.like(cb.lower(countRoot.get("description")), pattern)
            ));
        }

        if (tagsNames != null && !tagsNames.isEmpty()) {
            Subquery<Long> countTagsSubquery = countQuery.subquery(Long.class);
            Root<ProductEntity> subCountProduct = countTagsSubquery.from(ProductEntity.class);
            Join<ProductEntity, TagEntity> subCountTags = subCountProduct.join("tags", JoinType.INNER);
            countTagsSubquery.select(subCountProduct.get("id"))
                    .where(cb.and(cb.equal(subCountProduct.get("id"), countRoot.get("id")), subCountTags.get("name").in(tagsNames)));
            countPredicates.add(cb.exists(countTagsSubquery));
        }

        if (!countPredicates.isEmpty()) {
            countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        }
        countQuery.select(cb.countDistinct(countRoot));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(products, pageable, total);
    }
}
