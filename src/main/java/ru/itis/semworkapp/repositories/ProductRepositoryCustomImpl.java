package ru.itis.semworkapp.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.itis.semworkapp.entities.ProductEntity;
import ru.itis.semworkapp.entities.TagEntity;

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
        Join<ProductEntity, TagEntity> tags = product.join("tags", JoinType.LEFT);

        Predicate predicate = buildPredicate(cb, product, tags, query, tagsNames);
        cq.select(product).distinct(true);
        if (predicate != null) {
            cq.where(predicate);
        }
        cq.orderBy(cb.desc(product.get("createdAt")));

        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<ProductEntity> products = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProductEntity> countRoot = countQuery.from(ProductEntity.class);
        Join<ProductEntity, TagEntity> countTags = countRoot.join("tags", JoinType.LEFT);

        Predicate countPredicate = buildPredicate(cb, countRoot, countTags, query, tagsNames);
        countQuery.select(cb.countDistinct(countRoot));
        if (countPredicate != null) {
            countQuery.where(countPredicate);
        }
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(products, pageable, total);
    }

    private Predicate buildPredicate(
            CriteriaBuilder cb,
            Root<ProductEntity> product,
            Join<ProductEntity, TagEntity> tags,
            String query,
            List<String> tagsNames
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (query != null && !query.isBlank()) {
            String pattern = "%" + query.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(product.get("title")), pattern),
                    cb.like(cb.lower(product.get("description")), pattern),
                    cb.like(cb.lower(tags.get("name")), pattern)
            ));
        }
        if (tagsNames != null && !tagsNames.isEmpty()) {
            predicates.add(tags.get("name").in(tagsNames));
        }

        if (predicates.isEmpty()) {
            return null;
        } else if (predicates.size() == 1) {
            return predicates.get(0);
        } else {
            return cb.and(predicates.toArray(new Predicate[0]));
        }
    }
}
