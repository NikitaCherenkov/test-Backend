package com.example.demo.mapper;

import com.example.demo.dto.request.LotRequest;
import com.example.demo.dto.response.LotResponse;
import com.example.demo.model.Lot;
import nu.studer.sample.enums.NdsRateType;
import nu.studer.sample.tables.records.LotRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LotMapper {

    @Mapping(target = "id", ignore = true)
    Lot fromRequest(LotRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ndsRate", source = "ndsRate", qualifiedByName = "stringToNdsRateType")
    void toRecord(Lot lot, @MappingTarget LotRecord record);

    @Mapping(target = "ndsRate", source = "ndsRate", qualifiedByName = "ndsRateTypeToNds")
    Lot fromRecord(LotRecord record);

    LotResponse toResponse(Lot lot);

    @Named("stringToNdsRateType")
    default NdsRateType mapNdsRateType(String nds) {
        for (NdsRateType ndsRateType : NdsRateType.values()) {
            if (nds.equals(ndsRateType.getLiteral())) {
                return ndsRateType;
            }
        }

        throw new IllegalArgumentException("Unknown NDS value: " + nds);
    }

    @Named("ndsRateTypeToNds")
    default String mapNdsString(NdsRateType ndsRateType) {
        return ndsRateType.getLiteral();
    }
}
