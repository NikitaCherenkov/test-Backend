package com.example.demo.repositiory;

import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Customer;
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

    private final CustomerMapper customerMapper;
    private final DSLContext dsl;

    public List<CustomerRecord> getAllCustomers() {
        return dsl.selectFrom(CUSTOMER)
                .fetch();
    }

    public Customer create(Customer customer) {
        CustomerRecord newRecord = dsl.newRecord(CUSTOMER);
        customerMapper.toRecord(customer, newRecord);
        newRecord.store();
        return customerMapper.fromRecord(newRecord);
    }

    public Customer update(CustomerRecord record, Customer customer) {
        customerMapper.toRecord(customer, record);
        record.update();
        return customerMapper.fromRecord(record);
    }

    public int delete(CustomerRecord record) {
        return record.delete();
    }

    public Optional<CustomerRecord> findByID(Long customerID) {
        return Optional.ofNullable(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.ID.eq(customerID))
                        .fetchOne()
        );
    }

    public Optional<CustomerRecord> findByCode(String code) {
        return Optional.ofNullable(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.CUSTOMER_CODE.eq(code))
                        .fetchOne()
        );
    }

    public boolean existsByCode(String code) {
        return dsl.fetchExists(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.CUSTOMER_CODE.eq(code))
        );
    }

    public boolean existsByCodeMain(String code) {
        return dsl.fetchExists(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.CUSTOMER_CODE_MAIN.eq(code))
        );
    }

    public boolean existsById(Long customerID) {
        return dsl.fetchExists(
                dsl.selectFrom(CUSTOMER)
                        .where(CUSTOMER.ID.eq(customerID))
        );
    }

    public int updateMainCustomerCode(String oldCustomerCode, String newCustomerCode) {
        return dsl.update(CUSTOMER)
                .set(CUSTOMER.CUSTOMER_CODE_MAIN, newCustomerCode)
                .where(CUSTOMER.CUSTOMER_CODE_MAIN.eq(oldCustomerCode))
                .execute();
    }
}