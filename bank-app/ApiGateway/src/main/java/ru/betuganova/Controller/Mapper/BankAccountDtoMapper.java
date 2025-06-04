package ru.betuganova.Controller.Mapper;

import org.modelmapper.ModelMapper;
import ru.betuganova.Controller.Dto.BankAccountDto;
import ru.betuganova.Service.Model.BankAccount;

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
