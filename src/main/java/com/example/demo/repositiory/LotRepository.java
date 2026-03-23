package com.example.demo.repositiory;

import lombok.RequiredArgsConstructor;
import nu.studer.sample.tables.records.LotRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nu.studer.sample.Tables.LOT;

@Repository
@RequiredArgsConstructor
public class LotRepository {

    private final DSLContext dsl;

    public Optional<LotRecord> findById(int id) {
        return Optional.ofNullable(
                dsl.selectFrom(LOT)
                        .where(LOT.ID.eq(id))
                        .fetchOne()
        );
    }

    public List<LotRecord> findByCustomerCode(String customerCode) {
        return dsl.selectFrom(LOT)
                .where(LOT.CUSTOMER_CODE.eq(customerCode))
                .fetch();
    }

    public boolean existsByCustomerCode(String customerCode) {
        return dsl.fetchExists(
                dsl.selectFrom(LOT)
                        .where(LOT.CUSTOMER_CODE.eq(customerCode))
        );
    }

    public List<LotRecord> getAllLots() {
        return dsl.selectFrom(LOT)
                .fetch()
                .collect(Collectors.toList());
    }

    public LotRecord createNewRecord() {
        return dsl.newRecord(LOT);
    }

    public LotRecord save(LotRecord record) {
        record.store();
        return record;
    }

    public int deleteById(int id) {
        return dsl.deleteFrom(LOT)
                .where(LOT.ID.eq(id))
                .execute();
    }

    public int updateCustomerCode(String oldCustomerCode, String newCustomerCode) {
        return dsl.update(LOT)
                .set(LOT.CUSTOMER_CODE, newCustomerCode)
                .where(LOT.CUSTOMER_CODE.eq(oldCustomerCode))
                .execute();
    }
}
