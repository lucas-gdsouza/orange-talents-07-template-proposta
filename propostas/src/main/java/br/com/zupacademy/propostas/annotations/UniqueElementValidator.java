package br.com.zupacademy.propostas.annotations;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.List;

public class UniqueElementValidator implements ConstraintValidator<UniqueElement, Object> {
    private String field;
    private Class<?> klass;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void initialize(UniqueElement uniqueElementGeneric) {
        field = uniqueElementGeneric.fieldName();
        klass = uniqueElementGeneric.domainClass();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Query query = manager.createQuery("SELECT 1 FROM " + klass.getName() + " WHERE " + field + " =:value");
        query.setParameter("value", o);

        List<?> list = query.getResultList();
        Assert.state(list.size() <= 1, "O elemento " + field + " está em uso");

        return list.isEmpty();
    }
}