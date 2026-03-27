package com.example.demo.mapper;

import com.example.demo.dto.request.LotRequest;
import com.example.demo.dto.response.LotResponse;
import com.example.demo.enums.Currency;
import com.example.demo.enums.Nds;
import com.example.demo.model.Lot;
import nu.studer.sample.tables.records.LotRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LotMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ndsRate", source = "ndsRate", qualifiedByName = "stringToNds")
    Lot fromRequest(LotRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ndsRate", source = "ndsRate", qualifiedByName = "ndsToString")
    void toRecord(Lot lot, @MappingTarget LotRecord record);

    @Mapping(target = "ndsRate", source = "ndsRate", qualifiedByName = "stringToNds")
    Lot fromRecord(LotRecord record);

    @Mapping(target = "ndsRate", source = "ndsRate", qualifiedByName = "ndsToString")
    LotResponse toResponse(Lot lot);

    @Named("ndsToString")
    default String map(Nds nds) {
        return nds == null ? null : nds.getDisplayName();
    }

    @Named("stringToNds")
    default Nds mapNds(String value) {
        for (Nds nds : Nds.values()) {
            if (nds.getDisplayName().equals(value)) {
                return nds;
            }
        }

        throw new IllegalArgumentException("Unknown NDS value: " + value);
    }

    @Named("currencyToString")
    default String map(Currency currency) {
        return currency == null ? null : currency.getDisplayName();
    }

    @Named("stringToCurrency")
    default Currency mapCurrency(String value) {
        if (value == null) {
            return null;
        }

        for (Currency currency : Currency.values()) {
            if (currency.getDisplayName().equals(value)) {
                return currency;
            }
        }

        throw new IllegalArgumentException("Unknown currency value: " + value);
    }
}
