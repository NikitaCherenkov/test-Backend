package com.example.demo.repositiory;

import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.CustomerRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static nu.studer.sample.Tables.CUSTOMER;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final DSLContext dsl;

    public List<CustomerRecord> getAllCustomers() {
        return dsl.selectFrom(CUSTOMER)
                .fetch();
    }

    public CustomerRecord createNewRecord() {
        return dsl.newRecord(CUSTOMER);
    }

    public CustomerRecord save(CustomerRecord record) {
        record.store();
        return record;
    }

    public Optional<CustomerRecord> findById(int id) {
        return Optional.ofNullable(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.ID.eq(id))
                        .fetchOne()
        );
    }

    public boolean existsByCode(String code) {
        return dsl.fetchExists(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.CUSTOMER_CODE.eq(code))
        );
    }

    public boolean existsById(int id) {
        return dsl.fetchExists(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.ID.eq(id))
        );
    }

    public int deleteById(int id) {
        return dsl.deleteFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(id))
                .execute();
    }
}