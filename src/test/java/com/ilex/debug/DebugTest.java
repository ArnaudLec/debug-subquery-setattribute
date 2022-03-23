package com.ilex.debug;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilex.debug.entity.Person;
import com.ilex.debug.entity.Person_;
import com.ilex.debug.entity.TemporaryDataListAttr;
import com.ilex.debug.entity.TemporaryDataListAttr_;
import com.ilex.debug.entity.TemporaryDataSetAttr;
import com.ilex.debug.entity.TemporaryDataSetAttr_;
import com.ilex.debug.entity.TemporaryDataSingularAttr;
import com.ilex.debug.entity.TemporaryDataSingularAttr_;
import com.ilex.debug.repository.PersonRepository;
import com.ilex.debug.repository.TemporaryDataListAttrRepository;
import com.ilex.debug.repository.TemporaryDataSetAttrRepository;
import com.ilex.debug.repository.TemporaryDataSingularAttrRepository;

@SpringBootTest
class DebugTest {

  static final String CORREL_ID = "abc";

  @Autowired
  PersonRepository personRepo;

  @Autowired
  TemporaryDataSetAttrRepository setAttrTmpDataRepo;

  @Autowired
  TemporaryDataListAttrRepository listAttrTmpDataRepo;

  @Autowired
  TemporaryDataSingularAttrRepository singularAttrTmpDataRepo;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void prepare() throws Exception {
    Person p1 = newPerson("01");
    Person p2 = newPerson("02");
    Person p3 = newPerson("03");
    Person p4 = newPerson("04");
    Person p5 = newPerson("05");
    Person p6 = newPerson("06");

    personRepo.saveAll(List.of(p1, p2, p3, p4, p5, p6));
  }

  @AfterEach
  void cleanup() throws Exception {
    singularAttrTmpDataRepo.deleteAll();
    setAttrTmpDataRepo.deleteAll();
    personRepo.deleteAll();
  }

  @Nested
  class SetAttr {
    @BeforeEach
    void prepare() throws Exception {
      TemporaryDataSetAttr tmpDataSetAttr = new TemporaryDataSetAttr();
      tmpDataSetAttr.setCorrelationId(CORREL_ID);
      tmpDataSetAttr.setStrValues(Set.of("01", "02"));
      setAttrTmpDataRepo.save(tmpDataSetAttr);
    }

    @Test
    void jpaSpecification() {

      List<Person> people = personRepo.findAll((root, query, cb) -> {
        Subquery<Set<String>> subquery = query.subquery(TemporaryDataSetAttr_.strValues.getJavaType());
        Root<TemporaryDataSetAttr> subRoot = subquery.from(TemporaryDataSetAttr.class);

        subquery.select(subRoot.get(TemporaryDataSetAttr_.strValues))
            .where(cb.equal(subRoot.get(TemporaryDataSetAttr_.correlationId), CORREL_ID));

        return cb.not(root.get(Person_.id).in(subquery));
      });

      assertThat(people).hasSize(4);
    }

    @Test
    void nativeQuery() throws Exception {
      String subQuery = "(select temporary_data_set_attr_str_values.str_values from temporary_data_set_attr cross join temporary_data_set_attr_str_values where temporary_data_set_attr.id=temporary_data_set_attr_str_values.temporary_data_set_attr_id and temporary_data_set_attr.correlation_id='"
          + CORREL_ID + "')";
      String strQuery = "select id, name from person where person.id not in " + subQuery;

      Query query = entityManager.createNativeQuery(strQuery, Person.class);
      List<?> people = query.getResultList();

      assertThat(people).hasSize(4);
    }
  }

  @Nested
  class ListAttr {
    @BeforeEach
    void prepare() throws Exception {
      TemporaryDataListAttr tmpDataSetAttr = new TemporaryDataListAttr();
      tmpDataSetAttr.setCorrelationId(CORREL_ID);
      tmpDataSetAttr.setStrValues(List.of("01", "02"));
      listAttrTmpDataRepo.save(tmpDataSetAttr);
    }

    @Test
    void jpaSpecification() {

      List<Person> people = personRepo.findAll((root, query, cb) -> {
        Subquery<List<String>> subquery = query.subquery(TemporaryDataListAttr_.strValues.getJavaType());
        Root<TemporaryDataListAttr> subRoot = subquery.from(TemporaryDataListAttr.class);

        subquery.select(subRoot.get(TemporaryDataListAttr_.strValues))
            .where(cb.equal(subRoot.get(TemporaryDataListAttr_.correlationId), CORREL_ID));

        return cb.not(root.get(Person_.id).in(subquery));
      });

      assertThat(people).hasSize(4);
    }

    @Test
    void nativeQuery() throws Exception {
      String subQuery = "(select temporary_data_list_attr_str_values.str_values from temporary_data_list_attr cross join temporary_data_list_attr_str_values where temporary_data_list_attr.id=temporary_data_list_attr_str_values.temporary_data_list_attr_id and temporary_data_list_attr.correlation_id='"
          + CORREL_ID + "')";
      String strQuery = "select id, name from person where person.id not in " + subQuery;

      Query query = entityManager.createNativeQuery(strQuery, Person.class);
      List<?> people = query.getResultList();

      assertThat(people).hasSize(4);
    }
  }

  @Nested
  class SingularAttr {
    @BeforeEach
    void prepare() throws Exception {
      TemporaryDataSingularAttr tmpDataSingularAttr1 = new TemporaryDataSingularAttr();
      tmpDataSingularAttr1.setCorrelationId(CORREL_ID);
      tmpDataSingularAttr1.setValue("01");

      TemporaryDataSingularAttr tmpDataSingularAttr2 = new TemporaryDataSingularAttr();
      tmpDataSingularAttr2.setCorrelationId(CORREL_ID);
      tmpDataSingularAttr2.setValue("02");

      singularAttrTmpDataRepo.saveAll(List.of(tmpDataSingularAttr1, tmpDataSingularAttr2));
    }

    @Test
    void jpaSpecification() {

      List<Person> people = personRepo.findAll((root, query, cb) -> {
        Subquery<String> subquery = query.subquery(TemporaryDataSingularAttr_.value.getJavaType());
        Root<TemporaryDataSingularAttr> subRoot = subquery.from(TemporaryDataSingularAttr.class);

        subquery.select(subRoot.get(TemporaryDataSingularAttr_.value))
            .where(cb.equal(subRoot.get(TemporaryDataSingularAttr_.correlationId), CORREL_ID));

        return cb.not(root.get(Person_.id).in(subquery));
      });

      assertThat(people).hasSize(4);
    }

    @Test
    void nativeQuery() throws Exception {
      String strQuery = "select id, name from person where person.id not in "
          + "(select temporary_data_singular_attr.value from temporary_data_singular_attr where temporary_data_singular_attr.correlation_id='"
          + CORREL_ID + "')";

      Query query = entityManager.createNativeQuery(strQuery, Person.class);
      List<?> people = query.getResultList();

      assertThat(people).hasSize(4);
    }
  }

  Person newPerson(final String id) {
    Person person = new Person();
    person.setId(id);
    person.setName("Person " + id);
    return person;
  }
}
