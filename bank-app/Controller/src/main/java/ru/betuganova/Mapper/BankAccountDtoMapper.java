package ru.betuganova.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.betuganova.Dto.BankAccountDto;
import ru.betuganova.Model.BankAccount;

@Component
public class BankAccountDtoMapper {
    private final ModelMapper modelMapper;

    public BankAccountDtoMapper() {
        this.modelMapper = new ModelMapper();
    }

    public BankAccountDto toDto(BankAccount bankAccount) {
        return bankAccount == null ? null : modelMapper.map(bankAccount, BankAccountDto.class);
    }

    public BankAccount toModel(BankAccountDto bankAccount) {
        return bankAccount == null ? null : modelMapper.map(bankAccount, BankAccount.class);
    }
}
