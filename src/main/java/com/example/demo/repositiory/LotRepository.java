package com.example.demo.repositiory;

import com.example.demo.mapper.LotMapper;
import com.example.demo.model.Lot;
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

    private final LotMapper lotMapper;
    private final DSLContext dsl;

    public Lot create(Lot lot) {
        LotRecord newRecord = dsl.newRecord(LOT);
        lotMapper.toRecord(lot, newRecord);
        newRecord.store();
        return lotMapper.fromRecord(newRecord);
    }

    public Lot update(LotRecord record, Lot lot) {
        lotMapper.toRecord(lot, record);
        record.update();
        return lotMapper.fromRecord(record);
    }

    public int delete(LotRecord record) {
        return record.delete();
    }

    public int updateCustomerCode(String oldCustomerCode, String newCustomerCode) {
        return dsl.update(LOT)
                .set(LOT.CUSTOMER_CODE, newCustomerCode)
                .where(LOT.CUSTOMER_CODE.eq(oldCustomerCode))
                .execute();
    }

    public Optional<LotRecord> findByID(Integer lotID) {
        return Optional.ofNullable(
                dsl.selectFrom(LOT)
                        .where(LOT.ID.eq(lotID))
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
}
